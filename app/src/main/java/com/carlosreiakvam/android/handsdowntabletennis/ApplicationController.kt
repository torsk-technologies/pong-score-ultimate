package com.carlosreiakvam.android.handsdowntabletennis

import android.app.Application
import android.util.Log
import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
    }
}