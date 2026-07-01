package com.diabetes.prediction.data.api

import com.diabetes.prediction.data.model.PredictionRequest
import com.diabetes.prediction.data.model.PredictionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DiabetesApiService {

    @GET("health")
    suspend fun health(): Map<String, Any>

    @POST("predict")
    suspend fun predict(@Body request: PredictionRequest): PredictionResponse
}
