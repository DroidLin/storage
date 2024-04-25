package com.android.dependencies.storage

import android.content.Context
import android.content.SharedPreferences
import com.android.dependencies.common.ComponentFacade
import com.android.dependencies.common.android.IContext

/**
 * @author liuzhongao
 * @since 2024/4/1 23:16
 */
internal open class SharedPreferenceMapStorage(
    fileName: String
) : MapStorage {

    protected val sharedPreference: SharedPreferences = ComponentFacade[IContext::class.java]
        .applicationContext
        .getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override val all: Map<String, Any?> get() = this.sharedPreference.all

    override val absolutePath: String = ""
    override val filename: String = fileName

    override fun get(key: String, default: Any?): Any? {
        return when (default) {
            is Int -> this.getInt(key, default)
            is Long -> this.getLong(key, default)
            is Float -> this.getFloat(key, default)
            is Double -> this.getDouble(key, default)
            is Boolean -> this.getBoolean(key, default)
            is Byte -> this.getByte(key, default)
            is Char -> this.getChar(key, default)
            is Short -> this.getShort(key, default)
            is String -> this.getString(key, default)
            else -> default
        }
    }

    override fun contains(key: String): Boolean {
        return this.sharedPreference.contains(key)
    }

    override fun getInt(key: String, default: Int): Int {
        return this.sharedPreference.getInt(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return this.sharedPreference.getLong(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        return this.sharedPreference.getFloat(key, default)
    }

    /**
     * may have a loss of precision
     */
    override fun getDouble(key: String, default: Double): Double {
        return this.sharedPreference.getFloat(key, default.toFloat()).toDouble()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return this.sharedPreference.getBoolean(key, default)
    }

    override fun getByte(key: String, default: Byte): Byte {
        return this.sharedPreference.getInt(key, default.toInt()).toByte()
    }

    override fun getChar(key: String, default: Char): Char {
        return this.sharedPreference.getInt(key, default.code).toChar()
    }

    override fun getShort(key: String, default: Short): Short {
        return this.sharedPreference.getInt(key, default.toInt()).toShort()
    }

    override fun getString(key: String, default: String?): String? {
        return this.sharedPreference.getString(key, default)
    }

}