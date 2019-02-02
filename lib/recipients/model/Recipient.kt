package com.example.pln8992.kotlindemo.lib.recipients.model

data class Recipient(
    val id: Int,
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val emailAddress: String,
    val phoneNumber: String
)