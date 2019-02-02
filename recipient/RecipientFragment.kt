package com.example.pln8992.kotlindemo.app.recipient

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pln8992.kotlindemo.app.R
import com.example.pln8992.kotlindemo.app.databinding.RecipientFragmentBinding
import com.example.pln8992.kotlindemo.app.recipient.rx.EditTextSubject
import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function5
import kotlinx.android.synthetic.main.recipient_fragment.doneButton
import kotlinx.android.synthetic.main.recipient_fragment.emailAddressEditText
import kotlinx.android.synthetic.main.recipient_fragment.firstNameEditText
import kotlinx.android.synthetic.main.recipient_fragment.lastNameEditText
import kotlinx.android.synthetic.main.recipient_fragment.middleNameEditText
import kotlinx.android.synthetic.main.recipient_fragment.phoneNumberEditText


class RecipientFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var recipient: Recipient

    private lateinit var viewModel: RecipientViewModel

    //region RX

    private lateinit var firstNameObservable: Observable<String?>
    private lateinit var firstNameValidObservable: Observable<Boolean>

    private lateinit var middleNameObservable: Observable<String?>
    private lateinit var middleNameValidObservable: Observable<Boolean>

    private lateinit var lastNameObservable: Observable<String?>
    private lateinit var lastNameValidObservable: Observable<Boolean>

    private lateinit var emailAddressObservable: Observable<String?>
    private lateinit var emailAddressValidObservable: Observable<Boolean>

    private lateinit var phoneNumberObservable: Observable<String?>
    private lateinit var phoneNumberValidObservable: Observable<Boolean>

    private lateinit var formValidObservable: Observable<Boolean>

    //endregion

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(RecipientViewModel::class.java)

        val binding = DataBindingUtil.inflate<RecipientFragmentBinding>(inflater, R.layout.recipient_fragment, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setupLiveDataObservers()

        setupRxObservables()

        subscribeRxObservables()

        // quick and dirty back
        doneButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onPause() {
        compositeDisposable.dispose()
        super.onPause()
    }

    private fun setupRxObservables() {

        firstNameObservable = EditTextSubject(firstNameEditText).observable

        firstNameValidObservable = firstNameObservable.flatMap {
            Observable.create<Boolean> { emitter ->
                try {
                    when {
                        it.isNotEmpty() && it.length <= 32 -> emitter.onNext(true)
                        else -> emitter.onNext(false)
                    }
                } catch (e: Exception) {
                    // TODO: log it
                    emitter.onNext(false)
                }
            }
        }

        middleNameObservable = EditTextSubject(middleNameEditText).observable

        middleNameValidObservable = middleNameObservable.flatMap {
            Observable.create<Boolean> { emitter ->
                try {
                    when {
                        it.isEmpty() || it.length <= 32 -> emitter.onNext(true)
                        else -> emitter.onNext(false)
                    }
                } catch (e: Exception) {
                    // TODO: log it
                    emitter.onNext(false)
                }
            }
        }

        lastNameObservable = EditTextSubject(lastNameEditText).observable

        lastNameValidObservable = lastNameObservable.flatMap {
            Observable.create<Boolean> { emitter ->
                try {
                    when {
                        it.isNotEmpty() && it.length <= 32 -> emitter.onNext(true)
                        else -> emitter.onNext(false)
                    }
                } catch (e: Exception) {
                    // TODO: log it
                    emitter.onNext(false)
                }
            }
        }

        emailAddressObservable = EditTextSubject(emailAddressEditText).observable

        emailAddressValidObservable = emailAddressObservable.flatMap {
            Observable.create<Boolean> { emitter ->
                try {
                    emitter.onNext(
                        Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    )
                } catch (e: Exception) {
                    // TODO: log it
                    emitter.onNext(false)
                }
            }
        }

        phoneNumberObservable = EditTextSubject(phoneNumberEditText).observable

        phoneNumberValidObservable = phoneNumberObservable.flatMap {
            Observable.create<Boolean> { emitter ->
                try {
                    emitter.onNext(
                        Patterns.PHONE.matcher(it).matches()
                    )
                } catch (e: Exception) {
                    // TODO: log it
                    emitter.onNext(false)
                }
            }
        }

        formValidObservable = Observable.combineLatest(
            firstNameValidObservable,
            middleNameValidObservable,
            lastNameValidObservable,
            emailAddressValidObservable,
            phoneNumberValidObservable,
            Function5<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean> { firstNameValid, middleNameValid, lastNameValid, emailAddressValid, phoneNumberValid ->
                firstNameValid && middleNameValid && lastNameValid && emailAddressValid && phoneNumberValid
            }
        )
    }

    private fun subscribeRxObservables() {

        compositeDisposable.addAll(
            firstNameValidObservable.subscribe { valid ->
                when (valid) {
                    true -> firstNameEditText.error = null
                    false -> firstNameEditText.error = "Please enter a value less than 32 chars"
                }
            },
            middleNameValidObservable.subscribe { valid ->
                when (valid) {
                    true -> middleNameEditText.error = null
                    false -> middleNameEditText.error = "Please enter a value less than 32 chars"
                }
            },
            lastNameValidObservable.subscribe { valid ->
                when (valid) {
                    true -> lastNameEditText.error = null
                    false -> lastNameEditText.error = "Please enter a value less than 32 chars"
                }
            },
            emailAddressValidObservable.subscribe { valid ->
                when (valid) {
                    true -> emailAddressEditText.error = null
                    false -> emailAddressEditText.error = "Please enter a va;lid Email Address"
                }
            },
            phoneNumberValidObservable.subscribe { valid ->
                when (valid) {
                    true -> phoneNumberEditText.error = null
                    false -> phoneNumberEditText.error = "Please enter a valid Phone Number"
                }
            },
            formValidObservable.subscribe { valid ->
                doneButton.isEnabled = valid
            }
        )
    }

    private fun setupLiveDataObservers() {

        // set initial values
        viewModel.apply {
            firstName.value = recipient.firstName
            middleName.value = recipient.middleName
            lastName.value = recipient.lastName
            emailAddress.value = recipient.emailAddress
            phoneNumber.value = recipient.phoneNumber
        }

        // observers
        viewModel.firstName.observe(this, Observer {
            recipient = recipient.copy(firstName = it ?: "")
        })

        viewModel.lastName.observe(this, Observer {
            recipient = recipient.copy(lastName = it ?: "")
        })

        viewModel.middleName.observe(this, Observer {
            recipient = recipient.copy(middleName = it)
        })

        viewModel.emailAddress.observe(this, Observer {
            recipient = recipient.copy(emailAddress = it ?: "")
        })

        viewModel.phoneNumber.observe(this, Observer {
            recipient = recipient.copy(phoneNumber = it ?: "")
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(recipient: Recipient): RecipientFragment {
            return RecipientFragment().apply {
                this.recipient = recipient
            }
        }
    }
}