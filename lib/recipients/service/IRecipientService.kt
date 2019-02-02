package com.example.pln8992.kotlindemo.lib.recipients.service

import com.example.pln8992.kotlindemo.lib.recipients.model.ICallback
import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient
import io.reactivex.Single

interface IRecipientService {

    fun recipients(): List<Recipient>

    fun recipients(callback: ICallback<List<Recipient>, Exception>)

    fun recipientsObservable(): Single<List<Recipient>>

}
