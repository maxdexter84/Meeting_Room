package com.example.core_module.component_manager

interface IHasComponent<out T> {
    /**
     * Returns the component that will be saved in the store.
     */
    fun getComponent(): T

    /**
     * Return the key, this key identifies the component in the store.
     * The key must be unique for every component.
     */
    fun getComponentKey(): String = javaClass.toString()
}