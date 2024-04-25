package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/1 23:02
 */
internal expect val platformName: String

internal expect fun instantiateMapStorage(storageType: StorageType, directory: String, storageName: String): MapStorage?

internal expect fun instantiateMutableMapStorage(storageType: StorageType, directory: String, storageName: String): MutableMapStorage?

internal expect fun instantiateFileStorage(storageType: StorageType, absolutePath: String, filename: String): FileStorage?