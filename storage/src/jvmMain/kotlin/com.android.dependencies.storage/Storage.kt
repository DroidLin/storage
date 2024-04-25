package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/1 23:11
 */
internal actual val platformName: String get() = "JvmOnlyPlatform"

internal actual fun instantiateMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MapStorage? {
    return when (storageType) {
        JvmStorageType.JSON -> JvmJSONObjectMapStorage(directory, storageName)
        else -> null
    }
}

internal actual fun instantiateMutableMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MutableMapStorage? {
    return when (storageType) {
        JvmStorageType.JSON -> JvmJSONObjectMutableMapStorage(directory, storageName)
        else -> null
    }
}

internal actual fun instantiateFileStorage(
    storageType: StorageType,
    absolutePath: String,
    filename: String
): FileStorage? {
    return FileStorage(absolutePath, filename)
}