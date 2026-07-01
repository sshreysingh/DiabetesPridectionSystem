package com.diabetes.prediction.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.diabetes.prediction.data.db.PredictionDatabase
import com.diabetes.prediction.data.db.PredictionEntity
import com.diabetes.prediction.data.model.PredictionRequest
import com.diabetes.prediction.data.model.PredictionResponse
import com.diabetes.prediction.data.repository.PredictionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ── Form State ────────────────────────────────────────────────────────────────

data class DiabetesFormState(
    val pregnancies: String = "0",
    val glucose: String = "",
    val bloodPressure: String = "",
    val skinThickness: String = "0",
    val insulin: String = "0",
    val bmi: String = "",
    val diabetesPedigreeFunction: String = "",
    val age: String = ""
)

// ── UI State ──────────────────────────────────────────────────────────────────

sealed class PredictionUiState {
    object Idle : PredictionUiState()
    object Loading : PredictionUiState()
    data class Success(val response: PredictionResponse) : PredictionUiState()
    data class Error(val message: String) : PredictionUiState()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

class PredictionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository by lazy {
        PredictionRepository(PredictionDatabase.getDatabase(application).predictionDao())
    }

    // Form input state
    private val _formState = MutableStateFlow(DiabetesFormState())
    val formState: StateFlow<DiabetesFormState> = _formState.asStateFlow()

    // API call state
    private val _uiState = MutableStateFlow<PredictionUiState>(PredictionUiState.Idle)
    val uiState: StateFlow<PredictionUiState> = _uiState.asStateFlow()

    // Local history from Room
    val historyList: StateFlow<List<PredictionEntity>> = repository.allPredictions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Form field updaters ───────────────────────────────────────────────

    fun updatePregnancies(v: String) { _formState.update { it.copy(pregnancies = v) } }
    fun updateGlucose(v: String) { _formState.update { it.copy(glucose = v) } }
    fun updateBloodPressure(v: String) { _formState.update { it.copy(bloodPressure = v) } }
    fun updateSkinThickness(v: String) { _formState.update { it.copy(skinThickness = v) } }
    fun updateInsulin(v: String) { _formState.update { it.copy(insulin = v) } }
    fun updateBmi(v: String) { _formState.update { it.copy(bmi = v) } }
    fun updateDiabetesPedigreeFunction(v: String) { _formState.update { it.copy(diabetesPedigreeFunction = v) } }
    fun updateAge(v: String) { _formState.update { it.copy(age = v) } }

    // ── Validation ────────────────────────────────────────────────────────

private data class Range(val min: Double, val max: Double, val unit: String)

private val FIELD_RANGES = mapOf(
    "Pregnancies" to Range(0.0, 20.0, "times"),
    "Glucose" to Range(40.0, 300.0, "mg/dL"),
    "Blood Pressure" to Range(40.0, 200.0, "mm Hg"),
    "Skin Thickness" to Range(0.0, 100.0, "mm"),
    "Insulin" to Range(0.0, 900.0, "µU/mL"),
    "BMI" to Range(10.0, 70.0, "kg/m²"),
    "Diabetes Pedigree Function" to Range(0.05, 2.5, "score"),
    "Age" to Range(1.0, 120.0, "years")
)

private fun validate(form: DiabetesFormState): List<String> {
    val errors = mutableListOf<String>()

    val fields = listOf(
        "Pregnancies" to form.pregnancies,
        "Glucose" to form.glucose,
        "Blood Pressure" to form.bloodPressure,
        "Skin Thickness" to form.skinThickness,
        "Insulin" to form.insulin,
        "BMI" to form.bmi,
        "Diabetes Pedigree Function" to form.diabetesPedigreeFunction,
        "Age" to form.age
    )

    for ((label, rawValue) in fields) {
        val value = rawValue.toDoubleOrNull()
        if (value == null) {
            errors.add("$label must be a valid number")
            continue
        }
        val range = FIELD_RANGES[label] ?: continue
        if (value < range.min || value > range.max) {
            errors.add(
                "$label ($value) is outside the plausible range " +
                    "(${range.min}–${range.max} ${range.unit})"
            )
        }
    }

    return errors
}

// ── Actions ───────────────────────────────────────────────────────────

fun predict() {
    val form = _formState.value

    val validationErrors = validate(form)
    if (validationErrors.isNotEmpty()) {
        _uiState.value = PredictionUiState.Error(
            buildString {
                append("Please fix the following before predicting:\n\n")
                append(validationErrors.joinToString("\n") { "• $it" })
            }
        )
        return
    }

    val request = try {
        PredictionRequest(
            pregnancies = form.pregnancies.toFloat(),
            glucose = form.glucose.toFloat(),
            bloodPressure = form.bloodPressure.toFloat(),
            skinThickness = form.skinThickness.toFloat(),
            insulin = form.insulin.toFloat(),
            bmi = form.bmi.toFloat(),
            diabetesPedigreeFunction = form.diabetesPedigreeFunction.toFloat(),
            age = form.age.toFloat()
        )
    } catch (e: NumberFormatException) {
        _uiState.value = PredictionUiState.Error(
            "Please fill in all fields with valid numbers before predicting."
        )
        return
    }

    viewModelScope.launch {
        _uiState.value = PredictionUiState.Loading
        try {
            val response = repository.predict(request)
            repository.saveToHistory(request, response)
            _uiState.value = PredictionUiState.Success(response)
        } catch (e: Exception) {
            _uiState.value = PredictionUiState.Error(
                buildString {
                    append("Could not reach the prediction server.\n\n")
                    append("Make sure the Flask API is running.\n")
                    append("Details: ${e.localizedMessage}")
                }
            )
        }
    }
}
    fun resetForm() {
        _formState.value = DiabetesFormState()
        _uiState.value = PredictionUiState.Idle
    }

    fun dismissError() {
        _uiState.value = PredictionUiState.Idle
    }

    fun clearHistory() {
        viewModelScope.launch { repository.clearHistory() }
    }

    // ── Factory ───────────────────────────────────────────────────────────

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PredictionViewModel::class.java)) {
                return PredictionViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
