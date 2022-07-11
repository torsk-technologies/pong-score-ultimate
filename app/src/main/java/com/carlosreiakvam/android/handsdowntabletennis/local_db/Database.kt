package com.carlosreiakvam.android.handsdowntabletennis.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameStateEntity::class], version = 4, exportSchema = false
)
abstract class ChoDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDAO

    companion object {

        @Volatile
        private var INSTANCE: ChoDatabase? = null

        fun getDatabase(context: Context): ChoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ChoDatabase::class.java,
                    "cho_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }
}