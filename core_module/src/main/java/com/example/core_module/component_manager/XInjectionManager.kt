package com.example.core_module.component_manager

import android.app.Application

object XInjectionManager {
    @JvmStatic
    val instance = InjectionManager(XLifecycleListener())

    @JvmStatic
    fun init(app: Application) = instance.init(app)

    @JvmStatic
    fun <T> bindComponent(owner: IHasComponent<T>): T =
        instance.bindComponent(owner)

    @JvmStatic
    inline fun <reified T> findComponent(): T =
        instance.findComponent()

    @JvmStatic
    fun findComponent(predicate: (Any) -> Boolean): Any =
        instance.findComponent(predicate)
}