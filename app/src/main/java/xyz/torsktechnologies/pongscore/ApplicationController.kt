package xyz.torsktechnologies.pongscore

import android.app.Application
import xyz.torsktechnologies.pongscore.local_db.ChoDatabase
import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
    }

    val database by lazy { ChoDatabase.getDatabase(this) }
}