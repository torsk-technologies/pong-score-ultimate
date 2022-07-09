package com.carlosreiakvam.android.handsdowntabletennis.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameStateEntity::class], version = 4, exportSchema = false
)
abstract class HandsDownDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDAO

    companion object {

        @Volatile
        private var INSTANCE: HandsDownDatabase? = null

        fun getDatabase(context: Context): HandsDownDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    HandsDownDatabase::class.java,
                    "hands_down_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }
}