package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/2 22:07
 */
internal actual val platformName: String get() = "AndroidPlatform"

internal actual fun instantiateMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MapStorage? {
    return when (storageType) {
        AndroidStorageType.SharedPreference -> SharedPreferenceMapStorage(storageName)
        else -> null
    }
}

internal actual fun instantiateMutableMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MutableMapStorage? {
    return when (storageType) {
        AndroidStorageType.SharedPreference -> SharedPreferenceMutableMapStorage(storageName)
        else -> null
    }
}

internal actual fun instantiateFileStorage(storageType: StorageType, absolutePath: String, filename: String): FileStorage? {
    return FileStorage(absolutePath, filename)
}