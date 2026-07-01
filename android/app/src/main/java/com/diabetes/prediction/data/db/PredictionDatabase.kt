package com.diabetes.prediction.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PredictionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PredictionDatabase : RoomDatabase() {

    abstract fun predictionDao(): PredictionDao

    companion object {
        @Volatile
        private var INSTANCE: PredictionDatabase? = null

        fun getDatabase(context: Context): PredictionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PredictionDatabase::class.java,
                    "diabetes_predictions_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
