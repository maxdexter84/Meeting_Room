package com.example.core_module.component_manager

internal class ComponentsStore {

    private val components = mutableMapOf<String, Any>()

    fun isExist(key: String) = components.containsKey(key)

    fun add(key: String, component: Any) {
        components[key] = component
    }

    fun get(key: String): Any? = components[key]

    fun findComponent(predicate: (Any) -> Boolean): Any? {
        for ((_, component) in components) {
            if (predicate(component)) return component
        }
        return null
    }

    fun remove(key: String) {
        components.remove(key)
    }
}