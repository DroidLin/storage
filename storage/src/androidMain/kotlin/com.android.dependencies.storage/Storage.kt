package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/2 22:07
 */
internal actual val platformName: String get() = "AndroidPlatform"

internal actual fun ImmutableMapStorage(filePath: String, fileName: String): MapStorage {
    return SharedPreferenceMapStorage(fileName)
}

internal actual fun MutableMapStorage(filePath: String, fileName: String): MutableMapStorage {
    return SharedPreferenceMutableMapStorage(fileName)
}

internal actual fun instantiateMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MapStorage? {
    return when (storageType) {
        AndroidStorageType.SharedPreference -> ImmutableMapStorage(directory, storageName)
        else -> null
    }
}

internal actual fun instantiateMutableMapStorage(
    storageType: StorageType,
    directory: String,
    storageName: String
): MutableMapStorage? {
    return when (storageType) {
        AndroidStorageType.SharedPreference -> MutableMapStorage(directory, storageName)
        else -> null
    }
}