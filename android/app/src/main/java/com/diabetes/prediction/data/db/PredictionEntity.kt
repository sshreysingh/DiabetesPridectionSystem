package com.diabetes.prediction.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "predictions")
data class PredictionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pregnancies: Float,
    val glucose: Float,
    val bloodPressure: Float,
    val skinThickness: Float,
    val insulin: Float,
    val bmi: Float,
    val diabetesPedigreeFunction: Float,
    val age: Float,
    val prediction: Int,
    val probability: Float,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
