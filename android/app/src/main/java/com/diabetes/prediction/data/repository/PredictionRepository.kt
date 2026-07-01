package com.diabetes.prediction.data.repository

import com.diabetes.prediction.data.api.RetrofitClient
import com.diabetes.prediction.data.db.PredictionDao
import com.diabetes.prediction.data.db.PredictionEntity
import com.diabetes.prediction.data.model.PredictionRequest
import com.diabetes.prediction.data.model.PredictionResponse
import kotlinx.coroutines.flow.Flow

class PredictionRepository(private val dao: PredictionDao) {

    val allPredictions: Flow<List<PredictionEntity>> = dao.getAllPredictions()

    suspend fun predict(request: PredictionRequest): PredictionResponse {
        return RetrofitClient.apiService.predict(request)
    }

    suspend fun saveToHistory(request: PredictionRequest, response: PredictionResponse) {
        val entity = PredictionEntity(
            pregnancies = request.pregnancies,
            glucose = request.glucose,
            bloodPressure = request.bloodPressure,
            skinThickness = request.skinThickness,
            insulin = request.insulin,
            bmi = request.bmi,
            diabetesPedigreeFunction = request.diabetesPedigreeFunction,
            age = request.age,
            prediction = response.prediction,
            probability = response.probability,
            message = response.message
        )
        dao.insert(entity)
    }

    suspend fun clearHistory() {
        dao.deleteAll()
    }
}
