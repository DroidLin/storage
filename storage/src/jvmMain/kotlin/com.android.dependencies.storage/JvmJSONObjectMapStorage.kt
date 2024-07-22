package com.android.dependencies.storage

import org.json.JSONObject
import java.io.*
import java.nio.channels.FileLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * @author liuzhongao
 * @since 2024/3/29 00:11
 */
internal open class JvmJSONObjectMapStorage(
    filePath: String,
    fileName: String
) : MapStorage {

    private val fileSystemFile: File = File(filePath, fileName)

    init {
        if (!this.fileSystemFile.exists()) {
            val parentDirectory = this.fileSystemFile.parentFile
            if (!parentDirectory.exists()) {
                if (parentDirectory.mkdirs()) {
                    this.fileSystemFile.createNewFile()
                }
            } else this.fileSystemFile.createNewFile()
        }
    }

    protected val innerMutableMap = LinkedHashMap<String, Any?>()
    protected val reentrantLock = ReentrantReadWriteLock()

    private var lastFileModifiedTimestamp: Long = 0

    override val absolutePath: String = filePath
    override val filename: String = fileName
    override val all: Map<String, Any?>
        get() {
            this.trySyncStorageFromFileSystem()
            return this.innerMutableMap.toMap()
        }

    override fun get(key: String, default: Any?): Any? = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] ?: default
        }
    }

    override fun contains(key: String): Boolean = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] != null
        }
    }

    override fun getInt(key: String, default: Int): Int = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Int ?: default
        }
    }

    override fun getLong(key: String, default: Long): Long = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Long ?: default
        }
    }

    override fun getFloat(key: String, default: Float): Float = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Float ?: default
        }
    }

    override fun getDouble(key: String, default: Double): Double = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Double ?: default
        }
    }

    override fun getBoolean(key: String, default: Boolean): Boolean = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Boolean ?: default
        }
    }

    override fun getByte(key: String, default: Byte): Byte = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Byte ?: default
        }
    }

    override fun getChar(key: String, default: Char): Char = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Char ?: default
        }
    }

    override fun getShort(key: String, default: Short): Short = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Short ?: default
        }
    }

    override fun getString(key: String, default: String?): String? = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? String ?: default
        }
    }

    protected inline fun <T> keepLatest(function: () -> T): T {
        this.trySyncStorageFromFileSystem()
        return function()
    }

    protected fun flushIntoFileSystem() {
        this.reentrantLock.write {
            this.tryCreateFileAndDirectories(this.fileSystemFile)

            val randomAccessFile = RandomAccessFile(this.fileSystemFile, "rw")
            var lock: FileLock?
            do {
                lock = randomAccessFile.channel.lock()
            } while(lock == null)
            lock.use { _ ->
                val jsonMapStringContent = JSONObject(this.innerMutableMap).toString()
                randomAccessFile.writeBytes(jsonMapStringContent)
                this.lastFileModifiedTimestamp = this.fileSystemFile.lastModified()
            }
        }
    }

    private fun trySyncStorageFromFileSystem() {
        val lastModifiedTimestamp = this.fileSystemFile.lastModified()
        if (lastModifiedTimestamp != this.lastFileModifiedTimestamp) {
            this.syncStorageDataFromFileSystem()
            this.lastFileModifiedTimestamp = lastModifiedTimestamp
        }
    }

    private fun syncStorageDataFromFileSystem() {
        val byteArrayInFile = this.reentrantLock.read {
            this.readByteArray()
        }
        val contentString = byteArrayInFile.decodeToString()
        kotlin.runCatching {
            JSONObject(contentString)
        }.onSuccess { jsonObject ->
            this.reentrantLock.write {
                val lastMutableMap = this.innerMutableMap.toMap()
                this.innerMutableMap.clear()
                this.innerMutableMap.putAll(jsonObject.toMap())
                // we assume values in current storage is the latest.
                this.innerMutableMap.putAll(lastMutableMap)
            }
        }.onFailure { it.printStackTrace() }
    }

    /**
     * may block current thread execution.
     */
    private fun readByteArray(): ByteArray {
        if (!this.fileSystemFile.exists()) {
            return ByteArray(0)
        }
        return FileInputStream(this.fileSystemFile).use { fileInputStream ->
            val byteArray = ByteArray(1024)
            val byteArrayOutputStream = ByteArrayOutputStream()
            var bytesRead = 0
            while (fileInputStream.read(byteArray).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(byteArray, 0, bytesRead)
            }
            byteArrayOutputStream.toByteArray()
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