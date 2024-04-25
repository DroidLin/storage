package com.android.dependencies.storage

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

/**
 * @author liuzhongao
 * @since 2024/4/14 13:44
 */
fun FileStorage(absolutePath: String, filename: String): FileStorage = FileStorageImpl(absolutePath, filename)

internal class FileStorageImpl(
    override val absolutePath: String,
    override val filename: String
) : FileStorage {

    private val reentrantLock = ReentrantReadWriteLock()

    private val fileSystemFile: File = File(this.absolutePath, this.filename)
    private val randomAccessFile = RandomAccessFile(this.fileSystemFile, "rw")

    private var lastFileModifiedTimestamp: Long = this.fileSystemFile.lastModified()

    override val byteArray: ByteArray
        get() = this.readByteArray()
    override val isBeingModified: Boolean
        get() = this.lastFileModifiedTimestamp != this.fileSystemFile.lastModified()

    override fun write(byteArray: ByteArray) {
        this.writeData(byteArray)
    }

    /**
     * may block current thread execution.
     */
    private fun readByteArray(): ByteArray {
        if (!this.fileSystemFile.exists()) {
            return ByteArray(0)
        }
        return FileInputStream(this.fileSystemFile).use { fileInputStream ->
            this.randomAccessFile.channel.lock().use { _ ->
                val byteArray = ByteArray(1024)
                val byteArrayOutputStream = ByteArrayOutputStream()
                var bytesRead = 0
                while (fileInputStream.read(byteArray).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(byteArray, 0, bytesRead)
                }
                byteArrayOutputStream.toByteArray()
            }
        }
    }

    private fun writeData(byteArray: ByteArray) {
        this.reentrantLock.write {
            this.tryCreateFileAndDirectories(this.fileSystemFile)
            FileOutputStream(this.fileSystemFile).use { fileOutputStream ->
                this.randomAccessFile.channel.lock().use { _ ->
                    fileOutputStream.write(byteArray)
                    fileOutputStream.flush()
                    this.lastFileModifiedTimestamp = this.fileSystemFile.lastModified()
                }
            }
        }
    }

    private fun tryCreateFileAndDirectories(file: File) {
        val directoryFile = file.parentFile
        if (!directoryFile.exists() && !directoryFile.mkdirs()) {
            throw RuntimeException("cannot create directory for path: ${directoryFile.absolutePath}")
        }
        if (!file.exists() && !file.createNewFile()) {
            throw RuntimeException("cannot create new file for path: ${file.absolutePath}")
        }
    }
}