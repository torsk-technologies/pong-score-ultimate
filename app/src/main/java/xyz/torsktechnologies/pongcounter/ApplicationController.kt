package xyz.torsktechnologies.pongcounter

import android.app.Application
import xyz.torsktechnologies.pongcounter.local_db.ChoDatabase
import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
    }

    val database by lazy { ChoDatabase.getDatabase(this) }
}