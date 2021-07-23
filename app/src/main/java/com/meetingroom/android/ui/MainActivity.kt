package com.meetingroom.android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meetingroom.android.R
import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.di.Injector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}