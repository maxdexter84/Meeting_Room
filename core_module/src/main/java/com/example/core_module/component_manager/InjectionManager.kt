package com.example.core_module.component_manager

import android.app.Application
import com.example.core_module.component_manager.callbacks.ILifecycleListener

class InjectionManager(lifecycleListener: ILifecycleListener) {

    private val componentsStore = ComponentsStore()

    private val componentsController: ComponentsController

    init {
        componentsController =
            ComponentsController(componentsStore, lifecycleListener)
    }

    /**
     * Adds the lifecycle callbacks listeners
     */
    fun init(app: Application) {
        componentsController.addLifecycleCallbackListeners(app)
    }

    /**
     * Returns the created or saved component and binds it the [owner]'s lifecycle, so
     * when the [owner] is destroyed, the component will be destroyed too.
     */
    fun <T> bindComponent(owner: IHasComponent<T>): T = componentsController.bindComponent(owner)



    /**
     * Finds the component by the given class
     */
    inline fun <reified T> findComponent(): T {
        val predicate = object : (Any) -> Boolean {
            override fun invoke(component: Any): Boolean = component is T

            override fun toString(): String = T::class.java.simpleName
        }
        return findComponent(predicate) as T
    }

    /**
     * Finds the component by [predicate]
     */
    fun findComponent(predicate: (Any) -> Boolean): Any =
        componentsStore.findComponent(predicate)
            ?: throw ComponentNotFoundException(predicate.toString())
}