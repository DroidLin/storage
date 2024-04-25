package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/3 21:30
 */
internal object StorageCenterImpl : StorageCenter {

    override fun newMapStorage(
        storageType: StorageType,
        directory: String,
        storageName: String
    ): MapStorage = instantiateMapStorage(storageType, directory, storageName)
        ?: throw NullPointerException("no storage matched type: $storageType")

    override fun newMutableMapStorage(
        storageType: StorageType,
        directory: String,
        storageName: String
    ): MutableMapStorage = instantiateMutableMapStorage(storageType, directory, storageName)
        ?: throw NullPointerException("no storage matched type: $storageType")

    override fun newFileStorage(
        storageType: StorageType,
        directory: String,
        storageName: String
    ): FileStorage = instantiateFileStorage(storageType, directory, storageName)
        ?: throw NullPointerException("no storage matched type: $storageType")
}