package com.android.dependencies.storage

import kotlin.reflect.KProperty


/**
 * @author liuzhongao
 * @since 2024/4/4 11:23
 */
fun <T> MapStorage.delegate(key: String, default: T): MapStorageState<T> =
    MapStorageState(key, default, this)

fun <T> MutableMapStorage.delegate(key: String, default: T): MutableMapStorageState<T> =
    MutableMapStorageState(key, default, this)

open class MapStorageState<T>(
    internal val key: String,
    internal val default: T,
    internal open val mapStorage: MapStorage
)

class MutableMapStorageState<T>(
    key: String,
    default: T,
    override val mapStorage: MutableMapStorage
) : MapStorageState<T>(key, default, mapStorage)

operator fun <T> MapStorageState<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    return this.mapStorage.get(this.key, this.default) as T
}

operator fun <T> MutableMapStorageState<T>.setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
) {
    this.mapStorage.put(this.key, value)
}