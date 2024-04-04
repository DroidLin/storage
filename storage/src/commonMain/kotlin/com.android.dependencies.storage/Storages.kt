package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/1 23:02
 */
internal expect val platformName: String

internal expect fun ImmutableMapStorage(filePath: String, fileName: String): MapStorage

internal expect fun MutableMapStorage(filePath: String, fileName: String): MutableMapStorage

internal expect fun instantiateMapStorage(storageType: StorageType, directory: String, storageName: String): MapStorage?

internal expect fun instantiateMutableMapStorage(storageType: StorageType, directory: String, storageName: String): MutableMapStorage?