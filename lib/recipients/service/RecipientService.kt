package com.example.pln8992.kotlindemo.lib.recipients.service

import android.content.Context
import com.example.pln8992.kotlindemo.lib.R
import com.example.pln8992.kotlindemo.lib.recipients.json.RecipientTypeAdapterFactory
import com.example.pln8992.kotlindemo.lib.recipients.model.ICallback
import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient
import com.google.gson.GsonBuilder
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream
import java.util.Arrays

class RecipientService(context: Context) : IRecipientService {

    private val gson = GsonBuilder()
        .registerTypeAdapterFactory(RecipientTypeAdapterFactory())
        .create()

    private val recipientsJson: String?

    init {
        val inputStream = context
            .applicationContext
            .resources
            .openRawResource(R.raw.recipientsjson)

        recipientsJson = inputStreamToString(inputStream)
    }

    private fun inputStreamToString(inputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)

        } catch (e: IOException) {
            null
        }
    }

    override fun recipients(): List<Recipient> {

        // Make network/database/whatever long running call

        val recipients = gson.fromJson(recipientsJson, Array<Recipient>::class.java)

        return ArrayList(Arrays.asList(*recipients))
    }

    override fun recipients(callback: ICallback<List<Recipient>, Exception>) {
        try {

            // Handle background threading, etc.

            callback.onSuccess(recipients())

        } catch (exception: Exception) {

            callback.onFailure(exception)

        }
    }

    override fun recipientsObservable(): Single<List<Recipient>> {
        return Single.create<List<Recipient>> { emitter ->
            try {
                val recipients = recipients()
                emitter.onSuccess(recipients)

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}
