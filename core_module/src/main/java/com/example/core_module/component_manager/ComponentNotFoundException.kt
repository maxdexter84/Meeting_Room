package com.example.core_module.component_manager


open class ComponentNotFoundException(key: String) :
    Throwable("Component of the $key type was not found")