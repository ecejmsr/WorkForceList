package com.example.pln8992.kotlindemo.app

import android.app.Application
import android.content.Context
import com.example.pln8992.kotlindemo.app.recipients.RecipientListViewModel
import com.example.pln8992.kotlindemo.lib.recipients.service.IRecipientService
import com.example.pln8992.kotlindemo.lib.recipients.service.RecipientService
import com.usaa.mobile.android.inf.factory.FactoryBuilder
import com.usaa.mobile.android.inf.factory.UsaaFactory

class KotlinDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildFactory()
    }

    fun buildFactory() {
        val builder = FactoryBuilder()
        builder.registerInstance(Context::class.java, instance)
        builder.register(IRecipientService::class.java, RecipientService::class.java)
        builder.register(RecipientListViewModel::class.java, RecipientListViewModel::class.java)
        UsaaFactory.setFactory(builder.build())
    }

    companion object {
        @JvmStatic
        lateinit var instance: KotlinDemoApp
    }
}
