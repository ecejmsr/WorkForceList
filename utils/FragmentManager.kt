package com.example.pln8992.kotlindemo.app.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction


inline fun FragmentManager.transaction(block: FragmentTransaction.() -> Unit) {
    val txn = beginTransaction()
    block(txn)
    txn.commit()
}