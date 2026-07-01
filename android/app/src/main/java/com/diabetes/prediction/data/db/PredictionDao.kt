package com.diabetes.prediction.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prediction: PredictionEntity): Long

    @Query("SELECT * FROM predictions ORDER BY timestamp DESC")
    fun getAllPredictions(): Flow<List<PredictionEntity>>

    @Delete
    suspend fun delete(prediction: PredictionEntity)

    @Query("DELETE FROM predictions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM predictions")
    suspend fun getCount(): Int
}
