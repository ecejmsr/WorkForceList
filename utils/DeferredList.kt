package com.example.pln8992.kotlindemo.app.utils

import kotlinx.coroutines.experimental.Deferred


suspend fun List<Deferred<*>>.awaitAll() {
    forEach {
        it.await()
    }
}