package com.example.pln8992.kotlindemo.lib.recipients.model


interface ICallback<T, E : Throwable> {

    fun onSuccess(data: T)

    fun onFailure(error: E)
}