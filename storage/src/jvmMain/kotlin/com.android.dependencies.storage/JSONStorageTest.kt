package com.android.dependencies.storage

import java.util.concurrent.Executors

/**
 * @author liuzhongao
 * @since 2024/4/2 23:22
 */
internal fun main() {
    val executor = Executors.newFixedThreadPool(4)
    val storage = instantiateMutableMapStorage(JvmStorageType.JSON, "/Users/liuzhongao", "test.txt") ?: return

    val future1 = executor.submit {
        val startTimestamp = System.currentTimeMillis()
        for (index in 0 until 1000) {
            storage.putInt("key_extra_${index}", index)
        }
        storage.flush()
        println("future1 cost: ${System.currentTimeMillis() - startTimestamp}ms")
    }
    val future2 = executor.submit {
        val startTimestamp = System.currentTimeMillis()
        for (index in 1000 until 2000) {
            storage.putInt("key_extra_${index}", index)
        }
        storage.flush()
        println("future2 cost: ${System.currentTimeMillis() - startTimestamp}ms")
    }

    val future3 = executor.submit {
        val startTimestamp = System.currentTimeMillis()
        Thread.sleep(100)
        storage.clearAndFlush()
        println("future3 cost: ${System.currentTimeMillis() - startTimestamp}ms")
    }

    future1.get()
    future2.get()
    future3.get()
    executor.shutdown()
}