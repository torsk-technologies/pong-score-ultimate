package com.carlosreiakvam.android.pongcounter

import android.app.Application
import com.carlosreiakvam.android.pongcounter.local_db.ChoDatabase
import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
    }

    val database by lazy { ChoDatabase.getDatabase(this) }
}