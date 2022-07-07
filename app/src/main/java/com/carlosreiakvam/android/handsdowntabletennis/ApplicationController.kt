package com.carlosreiakvam.android.handsdowntabletennis

import android.app.Application
import com.carlosreiakvam.android.handsdowntabletennis.local_db.HandsDownDatabase

class ApplicationController : Application() {
    override fun onCreate() {
    }

    val database by lazy { HandsDownDatabase.getDatabase(this) }
}