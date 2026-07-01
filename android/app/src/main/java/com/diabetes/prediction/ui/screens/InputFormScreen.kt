package com.diabetes.prediction.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diabetes.prediction.ui.theme.*
import com.diabetes.prediction.viewmodel.PredictionUiState
import com.diabetes.prediction.viewmodel.PredictionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFormScreen(
    viewModel: PredictionViewModel,
    onNavigateBack: () -> Unit,
    onPredictionSuccess: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    val uiState   by viewModel.uiState.collectAsState()

    // Navigate when prediction succeeds
    LaunchedEffect(uiState) {
        if (uiState is PredictionUiState.Success) onPredictionSuccess()
    }

    // Error dialog
    if (uiState is PredictionUiState.Error) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissError() },
            containerColor   = Navy700,
            title = {
                Text("Error", color = Red400, fontWeight = FontWeight.Bold)
            },
            text = {
                Text((uiState as PredictionUiState.Error).message, color = TextSecondary)
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissError() }) {
                    Text("OK", color = Teal400)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepNavy, Navy900)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top App Bar ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick  = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(44.dp)
                        .background(Navy700, CircleShape)
                ) {
                    Icon(Icons.Filled.ArrowBack, "Back", tint = TextPrimary)
                }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Health Analysis", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Enter your medical data", fontSize = 12.sp, color = TextSecondary)
                }
            }

            // ── Form Fields ───────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Section: Personal Info
                SectionLabel("Personal Info")

                DiabetesTextField(
                    value        = formState.pregnancies,
                    onValueChange = viewModel::updatePregnancies,
                    label        = "Pregnancies",
                    hint         = "Number of times pregnant (0 – 17)",
                    icon         = Icons.Outlined.Person,
                    keyboardType = KeyboardType.Number,
                    unit         = "times"
                )
                DiabetesTextField(
                    value        = formState.age,
                    onValueChange = viewModel::updateAge,
                    label        = "Age",
                    hint         = "Your age in years (21 – 81)",
                    icon         = Icons.Outlined.DateRange,
                    keyboardType = KeyboardType.Number,
                    unit         = "yrs"
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Section: Blood Tests
                SectionLabel("Blood Tests")

                DiabetesTextField(
                    value        = formState.glucose,
                    onValueChange = viewModel::updateGlucose,
                    label        = "Glucose",
                    hint         = "Plasma glucose concentration (0 – 200 mg/dL)",
                    icon         = Icons.Outlined.Opacity,
                    keyboardType = KeyboardType.Number,
                    unit         = "mg/dL"
                )
                DiabetesTextField(
                    value        = formState.bloodPressure,
                    onValueChange = viewModel::updateBloodPressure,
                    label        = "Blood Pressure",
                    hint         = "Diastolic blood pressure (0 – 122 mm Hg)",
                    icon         = Icons.Outlined.Favorite,
                    keyboardType = KeyboardType.Number,
                    unit         = "mm Hg"
                )
                DiabetesTextField(
                    value        = formState.insulin,
                    onValueChange = viewModel::updateInsulin,
                    label        = "Insulin",
                    hint         = "2-Hour serum insulin (0 – 846 μU/mL)",
                    icon         = Icons.Outlined.Science,
                    keyboardType = KeyboardType.Number,
                    unit         = "μU/mL"
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Section: Body Measurements
                SectionLabel("Body Measurements")

                DiabetesTextField(
                    value        = formState.skinThickness,
                    onValueChange = viewModel::updateSkinThickness,
                    label        = "Skin Thickness",
                    hint         = "Triceps skin fold thickness (0 – 99 mm)",
                    icon         = Icons.Outlined.ShowChart,
                    keyboardType = KeyboardType.Number,
                    unit         = "mm"
                )
                DiabetesTextField(
                    value        = formState.bmi,
                    onValueChange = viewModel::updateBmi,
                    label        = "BMI",
                    hint         = "Body Mass Index (0 – 67.1 kg/m²)",
                    icon         = Icons.Outlined.FitnessCenter,
                    keyboardType = KeyboardType.Decimal,
                    unit         = "kg/m²"
                )
                DiabetesTextField(
                    value        = formState.diabetesPedigreeFunction,
                    onValueChange = viewModel::updateDiabetesPedigreeFunction,
                    label        = "Diabetes Pedigree Function",
                    hint         = "Genetic diabetes risk score (0.078 – 2.42)",
                    icon         = Icons.Outlined.AccountTree,
                    keyboardType = KeyboardType.Decimal,
                    unit         = "score"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ── Submit Button ─────────────────────────────────────────
                val isLoading = uiState is PredictionUiState.Loading

                Button(
                    onClick        = { viewModel.predict() },
                    enabled        = !isLoading,
                    modifier       = Modifier.fillMaxWidth().height(60.dp),
                    shape          = RoundedCornerShape(16.dp),
                    colors         = ButtonDefaults.buttonColors(
                        containerColor        = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (!isLoading)
                                    Brush.horizontalGradient(listOf(Teal500, Purple500))
                                else
                                    Brush.horizontalGradient(listOf(Navy600, Navy700)),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            Row(
                                verticalAlignment    = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(22.dp),
                                    color       = Teal400,
                                    strokeWidth = 2.5.dp
                                )
                                Text("Analyzing...", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                            }
                        } else {
                            Row(
                                verticalAlignment    = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Filled.Analytics, null, tint = Color.White, modifier = Modifier.size(22.dp))
                                Text("Analyze & Predict", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun SectionLabel(title: String) {
    Row(
        verticalAlignment    = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.size(4.dp).background(Teal500, CircleShape))
        Text(
            text          = title.uppercase(),
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            color         = Teal400,
            letterSpacing = 1.5.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiabetesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    icon: ImageVector,
    keyboardType: KeyboardType,
    unit: String = ""
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
    value         = value,
    onValueChange = { input ->
        // Allow only digits and a single decimal point — blocks
        // letters/symbols from ever reaching the form state.
        val filtered = if (keyboardType == KeyboardType.Decimal) {
            input.filterIndexed { index, c ->
                c.isDigit() || (c == '.' && input.indexOf('.') == index)
            }
        } else {
            input.filter { it.isDigit() }
        }
        onValueChange(filtered)
    },
            placeholder   = { Text("0", color = TextHint) },
            leadingIcon   = { Icon(icon, contentDescription = null, tint = Teal400) },
            suffix        = if (unit.isNotEmpty()) {
                { Text(unit, color = TextHint, fontSize = 12.sp) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier      = Modifier.fillMaxWidth(),
            shape         = RoundedCornerShape(14.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor     = Teal500,
                unfocusedBorderColor   = CardBorder,
                focusedLabelColor      = Teal400,
                unfocusedLabelColor    = TextSecondary,
                cursorColor            = Teal400,
                focusedTextColor       = TextPrimary,
                unfocusedTextColor     = TextPrimary,
                focusedContainerColor  = Navy700,
                unfocusedContainerColor = Navy700
            ),
            singleLine = true
        )
        Text(hint, fontSize = 11.sp, color = TextHint, modifier = Modifier.padding(start = 4.dp))
    }
}
