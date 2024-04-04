package com.android.dependencies.storage

import kotlin.concurrent.write

/**
 * @author liuzhongao
 * @since 2024/4/1 23:08
 */
internal class JvmJSONObjectMutableMapStorage(
    filePath: String,
    fileName: String
) : JvmJSONObjectMapStorage(filePath, fileName), MutableMapStorage {

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

    override fun putInt(key: String, value: Int) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putLong(key: String, value: Long) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putFloat(key: String, value: Float) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putDouble(key: String, value: Double) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putBoolean(key: String, value: Boolean) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putByte(key: String, value: Byte) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putChar(key: String, value: Char) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putShort(key: String, value: Short) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putString(key: String, value: String?) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun flush() {
        this.flushIntoFileSystem()
    }
}