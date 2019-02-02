package com.example.pln8992.kotlindemo.app.recipients

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.telephony.PhoneNumberUtils
import android.util.Log
import com.example.pln8992.kotlindemo.app.utils.fullName
import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient
import com.example.pln8992.kotlindemo.lib.recipients.service.IRecipientService
import io.reactivex.schedulers.Schedulers

class RecipientListViewModel(
    private val recipientService: IRecipientService
) : ViewModel() {

    // local holder/cache for the data
    private val recipientList: MutableList<Recipient> = ArrayList()

    // THE selected recipient
    val selectedRecipient: MutableLiveData<Recipient> = MutableLiveData()

    // Progress indicator for screen
    val progressVisible: MutableLiveData<Boolean> = MutableLiveData()

    // the public observable flag to force a refresh
    val refresh: MutableLiveData<Boolean> = MutableLiveData()

    // the public observable for the filterCriteria
    val filterCriteria: MutableLiveData<String?> = MutableLiveData()

    // the private observable backing the list
    private val listData: MutableLiveData<List<Recipient>> = MutableLiveData()

    // The public observable LiveData object to be observed.  Transformed from the filtercriteria
    //  livedata so that every time it changes, we produce a new list
    val recipients: LiveData<List<Recipient>> = Transformations.switchMap(filterCriteria) { criteria ->

        // go get the data, show progress indicator
        progressVisible.postValue(true)

        // region RxJava

        when {
            recipientList.isEmpty() || refresh.value ?: true -> {

                recipientService.recipientsObservable()
                    .map {
                        it.map {
                            formatPhone(it)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {

                            // save to local cached copy
                            recipientList.clear()
                            recipientList.addAll(it)

                            // update refresh flag
                            refresh.postValue(false)

                            // update livedata with full or filtered list
                            when {
                                criteria.isNullOrEmpty() -> {
                                    // unfiltered
                                    listData.postValue(recipientList)
                                }
                                else -> {
                                    // filtered
                                    listData.postValue(recipientList.filter {
                                        it.fullName().contains(criteria!!, true)
                                    })
                                }
                            }

                            // hide progressbar
                            progressVisible.postValue(false)
                        },
                        { error ->
                            // failed
                            Log.e(TAG, "Exception getting Recipient List", error)

                            recipientList.clear()
                            listData.postValue(recipientList)

                            progressVisible.postValue(false)
                        }
                    )
            }
            else -> {
                // update livedata with full or filtered list
                when {
                    criteria.isNullOrEmpty() -> {
                        // unfiltered
                        listData.postValue(recipientList)
                    }
                    else -> {
                        // filtered
                        listData.postValue(recipientList.filter {
                            it.fullName().contains(criteria!!, true)
                        })
                    }
                }

                progressVisible.postValue(false)
            }
        }

        // endregion

        // region Coroutines

//        launch(CommonPool) {
//
//            // repopulate local cached copy
//            when {
//                recipientList.isEmpty() || refresh.value ?: true -> {
//                    recipientList.clear()
//                    recipientList.addAll(
//                        recipientService.recipients().map {
//                            formatPhone(it)
//                        }
//                    )
//                    refresh.postValue(false)
//                }
//            }
//
//            // update livedata with full or filtered list
//            when {
//                criteria.isNullOrEmpty() -> {
//                    // unfiltered
//                    listData.postValue(recipientList)
//                }
//                else -> {
//                    // filtered
//                    listData.postValue(recipientList.filter {
//                        it.fullName().contains(criteria!!, true)
//                    })
//                }
//            }
//
//            progressVisible.postValue(false)
//        }

        //endregion

        listData
    }

    init {
        // No progressVisible bar
        progressVisible.value = false

        // initial set refresh to true
        refresh.value = true
    }

    private fun formatPhone(recipient: Recipient): Recipient {
        return when {
            recipient.phoneNumber.isNotEmpty() && recipient.phoneNumber.length == 10 -> {
                val cleaned = recipient.phoneNumber.replace(Regex("[^\\d.]"), "")
                recipient.copy(phoneNumber = PhoneNumberUtils.formatNumber(cleaned, "US"))
            }
            else -> {
                recipient
            }
        }

    }

    companion object {
        private val TAG: String = RecipientListViewModel::class.java.name
    }
}