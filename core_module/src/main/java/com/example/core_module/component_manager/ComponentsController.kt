package com.example.core_module.component_manager

import android.app.Application
import com.example.core_module.component_manager.callbacks.ILifecycleListener
import com.example.core_module.component_manager.callbacks.IRemoveComponentCallback

internal class ComponentsController(
    private val componentsStore: ComponentsStore,
    private val platformLifecycleCallbacks: ILifecycleListener
) : IRemoveComponentCallback {

    private val keysForCustomLifecycle = mutableSetOf<String>()

    override fun onRemove(key: String) {
        if (keysForCustomLifecycle.contains(key)) return
        componentsStore.remove(key)
    }

    fun addLifecycleCallbackListeners(app: Application) {
        platformLifecycleCallbacks.addLifecycleListener(app, this)
    }

    fun <T> bindComponent(owner: IHasComponent<T>) = buildOrCreateComponent(owner)


    @Suppress("UNCHECKED_CAST")
    private fun <T> buildOrCreateComponent(owner: IHasComponent<T>): T {
        with(owner.getComponentKey()) {
            if (componentsStore.isExist(this)) {
                return componentsStore.get(this) as T
            }
            return owner.getComponent().also { componentsStore.add(this, it as Any) }
        }
    }
}