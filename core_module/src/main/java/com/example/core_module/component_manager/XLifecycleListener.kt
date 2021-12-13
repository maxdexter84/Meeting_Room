package com.example.core_module.component_manager

import android.app.Application
import com.example.core_module.component_manager.callbacks.ILifecycleListener
import com.example.core_module.component_manager.callbacks.IRemoveComponentCallback

internal class XLifecycleListener : ILifecycleListener {
    override fun addLifecycleListener(app: Application, removeCallback: IRemoveComponentCallback) {
        app.registerActivityLifecycleCallbacks(XActivityLifecycleHelper(removeCallback))
    }
}