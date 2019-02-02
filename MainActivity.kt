package com.example.pln8992.kotlindemo.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pln8992.kotlindemo.app.recipient.RecipientFragment
import com.example.pln8992.kotlindemo.app.recipients.RecipientListFragment
import com.example.pln8992.kotlindemo.app.utils.transaction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        when (savedInstanceState) {
            null -> {
                supportFragmentManager.transaction {
                    replace(R.id.fragmentContainer, RecipientListFragment.newInstance(), "CURRENT")
                }
            }
        }
    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentByTag("CURRENT")

        if (currentFragment != null && currentFragment is RecipientFragment) {
            supportFragmentManager.transaction {
                replace(R.id.fragmentContainer, RecipientListFragment.newInstance(), "CURRENT")
            }
        } else {
            super.onBackPressed()
        }
    }
}
