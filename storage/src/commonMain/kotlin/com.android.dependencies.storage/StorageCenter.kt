package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/3/27 23:20
 */
interface StorageCenter {

    fun newMapStorage(
        storageType: StorageType,
        directory: String,
        storageName: String
    ): MapStorage

    fun newMutableMapStorage(
        storageType: StorageType,
        directory: String,
        storageName: String
    ): MutableMapStorage

    fun newFileStorage(
        storageType: StorageType,
        directory: String,
        storageName: String
    ): FileStorage
}