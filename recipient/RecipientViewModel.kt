package com.example.pln8992.kotlindemo.app.recipient

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class RecipientViewModel : ViewModel() {

    val firstName: MutableLiveData<String?> = MutableLiveData()

    val middleName: MutableLiveData<String?> = MutableLiveData()

    val lastName: MutableLiveData<String?> = MutableLiveData()

    val emailAddress: MutableLiveData<String?> = MutableLiveData()

    val phoneNumber: MutableLiveData<String?> = MutableLiveData()

}