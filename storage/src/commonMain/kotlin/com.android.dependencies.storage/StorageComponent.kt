package com.android.dependencies.storage

import com.android.dependencies.common.Component
import com.android.dependencies.common.ComponentInstaller

/**
 * @author liuzhongao
 * @since 2024/4/4 11:51
 */
class StorageComponent : Component {
    override val name: String
        get() = "${platformName}StorageComponent"

    override fun collect(installer: ComponentInstaller) {
        installer.installLazily(StorageCenter::class.java) { StorageCenterImpl }
    }
}