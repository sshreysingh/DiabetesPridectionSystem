package com.diabetes.prediction.data.model

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("prediction") val prediction: Int,
    @SerializedName("probability") val probability: Float,
    @SerializedName("message") val message: String,
    @SerializedName("risk_percentage") val riskPercentage: Float
)
