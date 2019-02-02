package com.example.pln8992.kotlindemo.app.recipients

import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient

class RecipientSortComparator : Comparator<Recipient> {
    override fun compare(first: Recipient?, second: Recipient?): Int {
        return when {
            first == null -> when (second) {
                null -> 0
                else -> -1
            }
            second == null -> 1
            else -> {
                val result = first.lastName.compareTo(second.lastName, true)
                if (result != 0) {
                    result
                } else {
                    first.firstName.compareTo(second.firstName, true)
                }
            }
        }
    }
}