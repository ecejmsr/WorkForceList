package com.example.pln8992.kotlindemo.lib.recipients.json

import android.util.Log

import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException

class RecipientTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

        val delegateTypeAdapter = gson.getDelegateAdapter(this, type)

        val classType = type.rawType

        return if (classType == Recipient::class.java) {

            // Return a Recipient Type Adapter
            object : TypeAdapter<T>() {

                @Throws(IOException::class)
                override fun write(writer: JsonWriter, value: T) {
                    delegateTypeAdapter.write(writer, value)
                }

                override fun read(reader: JsonReader): T? {

                    var recipient: Recipient? = null

                    try {
                        recipient = delegateTypeAdapter.read(reader) as Recipient

                    } catch (e: Exception) {
                        Log.e(TAG, "Exception deserializing JSON to Recipient", e)
                    }

                    if (recipient != null) {
                        // check individual fields on the object, and either throw exception or set defaults
                    }

                    @Suppress("UNCHECKED_CAST")
                    return recipient as T?
                }
            }

        } else {
            null
        }
    }

    companion object {
        private val TAG = RecipientTypeAdapterFactory::class.java.simpleName
    }
}
