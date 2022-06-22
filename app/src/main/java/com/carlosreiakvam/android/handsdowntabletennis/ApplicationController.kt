package com.carlosreiakvam.android.handsdowntabletennis

import android.app.Application
import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
    }
}