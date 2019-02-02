package com.example.pln8992.kotlindemo.app.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.usaa.mobile.android.inf.factory.UsaaFactory
import kotlin.reflect.KProperty


class UsaaViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = UsaaFactory.getComponent(modelClass)
}

interface ReadOnlyProperty<in R, out T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
}

fun <T> inject(modelClass: Class<T>) = object : ReadOnlyProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        @Suppress("UNCHECKED_CAST")
        return UsaaFactory.getComponent(modelClass) as T
    }
}
