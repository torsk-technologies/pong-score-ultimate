package com.carlosreiakvam.android.handsdowntabletennis

import android.app.Application
import com.carlosreiakvam.android.handsdowntabletennis.local_db.ChoDatabase
import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
    }

    val database by lazy { ChoDatabase.getDatabase(this) }
}