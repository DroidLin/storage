package com.android.dependencies.storage

import android.content.SharedPreferences

/**
 * @author liuzhongao
 * @since 2024/4/1 23:16
 */
internal class SharedPreferenceMutableMapStorage(
    fileName: String
) : SharedPreferenceMapStorage(fileName), MutableMapStorage {

    @Volatile
    private var tempEditor: SharedPreferences.Editor? = null

    override fun put(key: String, value: Any?) {
        when (value) {
            is Int -> this.putInt(key, value)
            is Long -> this.putLong(key, value)
            is Float -> this.putFloat(key, value)
            is Double -> this.putDouble(key, value)
            is Boolean -> this.putBoolean(key, value)
            is Byte -> this.putByte(key, value)
            is Char -> this.putChar(key, value)
            is Short -> this.putShort(key, value)
            is String -> this.putString(key, value)
            else -> {
                if (value == null) {
                    this.putString(key, null)
                } else throw UnsupportedOperationException("unsupported value: ${value}.")
            }
        }
    }

    override fun putInt(key: String, value: Int) = this.prepareSharedPreferenceEditor {
        this.putInt(key, value)
    }

    override fun putLong(key: String, value: Long) = this.prepareSharedPreferenceEditor {
        this.putLong(key, value)
    }

    override fun putFloat(key: String, value: Float) = this.prepareSharedPreferenceEditor {
        this.putFloat(key, value)
    }

    override fun putDouble(key: String, value: Double) = this.prepareSharedPreferenceEditor {
        this.putFloat(key, value.toFloat())
    }

    override fun putBoolean(key: String, value: Boolean) = this.prepareSharedPreferenceEditor {
        this.putBoolean(key, value)
    }

    override fun putByte(key: String, value: Byte) = this.prepareSharedPreferenceEditor {
        this.putInt(key, value.toInt())
    }

    override fun putChar(key: String, value: Char) = this.prepareSharedPreferenceEditor {
        this.putInt(key, value.code)
    }

    override fun putShort(key: String, value: Short) = this.prepareSharedPreferenceEditor {
        this.putInt(key, value.toInt())
    }

    override fun putString(key: String, value: String?) = this.prepareSharedPreferenceEditor {
        this.putString(key, value)
    }

    override fun clearAndFlush() = this.prepareSharedPreferenceEditor {
        this.clear()
        this@SharedPreferenceMutableMapStorage.flush()
    }

    override fun flush() {
        synchronized(this) {
            this.tempEditor?.commit()
            this.tempEditor = null
        }
    }

    private inline fun prepareSharedPreferenceEditor(function: SharedPreferences.Editor.() -> Unit) {
        if (this.tempEditor == null) {
            synchronized(this) {
                if (this.tempEditor == null) {
                    this.tempEditor = this.sharedPreference.edit()
                }
            }
        }
        function(requireNotNull(this.tempEditor))
    }
}