package com.example.pln8992.kotlindemo.app.utils

import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient


fun Recipient.fullName(): String {
    return "${this.lastName}, ${this.firstName}"
}