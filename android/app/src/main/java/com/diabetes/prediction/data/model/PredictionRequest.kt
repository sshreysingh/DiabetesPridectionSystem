package com.diabetes.prediction.data.model

import com.google.gson.annotations.SerializedName

data class PredictionRequest(
    @SerializedName("pregnancies") val pregnancies: Float,
    @SerializedName("glucose") val glucose: Float,
    @SerializedName("blood_pressure") val bloodPressure: Float,
    @SerializedName("skin_thickness") val skinThickness: Float,
    @SerializedName("insulin") val insulin: Float,
    @SerializedName("bmi") val bmi: Float,
    @SerializedName("diabetes_pedigree_function") val diabetesPedigreeFunction: Float,
    @SerializedName("age") val age: Float
)
