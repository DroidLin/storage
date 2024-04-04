package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/1 23:11
 */
internal actual val platformName: String get() = "JvmOnlyPlatform"

internal actual fun ImmutableMapStorage(filePath: String, fileName: String): MapStorage {
    return JvmJSONObjectMapStorage(filePath, fileName)
}

internal actual fun MutableMapStorage(filePath: String, fileName: String): MutableMapStorage {
    return JvmJSONObjectMutableMapStorage(filePath, fileName)
}

internal actual fun instantiateMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MapStorage? {
    return when (storageType) {
        JvmStorageType.JSON -> ImmutableMapStorage(directory, storageName)
        else -> null
    }
}

internal actual fun instantiateMutableMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MutableMapStorage? {
    return when (storageType) {
        JvmStorageType.JSON -> MutableMapStorage(directory, storageName)
        else -> null
    }
}