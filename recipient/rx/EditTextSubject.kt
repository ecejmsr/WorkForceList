package com.example.pln8992.kotlindemo.app.recipient.rx

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


class EditTextSubject(editText: EditText) {

    private val subject: BehaviorSubject<String> = BehaviorSubject.createDefault(editText.text.toString())

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                subject.onNext(s?.toString() ?: "")
            }

            //region unused
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            //endregion
        })
    }

    val observable: Observable<String?> = subject

}