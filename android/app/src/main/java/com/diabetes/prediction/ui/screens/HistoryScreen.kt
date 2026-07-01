package com.diabetes.prediction.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diabetes.prediction.data.db.PredictionEntity
import com.diabetes.prediction.ui.theme.*
import com.diabetes.prediction.viewmodel.PredictionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: PredictionViewModel,
    onNavigateBack: () -> Unit
) {
    val historyList by viewModel.historyList.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor   = Navy700,
            title  = { Text("Clear History", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text   = { Text("Delete all prediction history? This cannot be undone.", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearHistory(); showClearDialog = false }) {
                    Text("Clear All", color = Red400, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = Teal400)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepNavy, Navy900)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── Top Bar ───────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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

                Text(
                    "Prediction History",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    modifier   = Modifier.align(Alignment.Center)
                )

                if (historyList.isNotEmpty()) {
                    IconButton(
                        onClick  = { showClearDialog = true },
                        modifier = Modifier.align(Alignment.CenterEnd).size(44.dp)
                    ) {
                        Icon(Icons.Filled.DeleteSweep, "Clear history", tint = Red400)
                    }
                }
            }

            // ── Content ───────────────────────────────────────────────────
            if (historyList.isEmpty()) {
                // Empty state
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(Navy700, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.History,
                                contentDescription = null,
                                tint     = TextHint,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Text("No history yet", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            "Your prediction history\nwill appear here",
                            fontSize  = 14.sp,
                            color     = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Record count badge
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Teal500.copy(alpha = 0.14f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${historyList.size} Records",
                            fontSize   = 12.sp,
                            color      = Teal400,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                LazyColumn(
                    contentPadding    = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historyList, key = { it.id }) { record ->
                        HistoryCard(record)
                    }
                    item { Spacer(Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(record: PredictionEntity) {
    val isDiabetic   = record.prediction == 1
    val accentColor  = if (isDiabetic) Red400  else Green400
    val borderColor  = if (isDiabetic) Red500.copy(alpha = 0.25f) else Green500.copy(alpha = 0.25f)
    val headerBg     = if (isDiabetic) Red500.copy(alpha = 0.06f) else Green500.copy(alpha = 0.06f)

    val dateStr = remember(record.timestamp) {
        SimpleDateFormat("MMM dd, yyyy  •  hh:mm a", Locale.getDefault())
            .format(Date(record.timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Navy700)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBg)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector      = if (isDiabetic) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint             = accentColor,
                    modifier         = Modifier.size(20.dp)
                )
                Text(
                    if (isDiabetic) "Diabetic" else "Not Diabetic",
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = accentColor
                )
            }
            Text(
                "${(record.probability * 100).toInt()}% risk",
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color      = accentColor
            )
        }

        // Metrics grid
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                MetricItem("Glucose",        "${record.glucose.toInt()} mg/dL")
                MetricItem("Blood Pressure", "${record.bloodPressure.toInt()} mm Hg")
                MetricItem("BMI",            String.format("%.1f", record.bmi))
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                MetricItem("Age",         "${record.age.toInt()} yrs")
                MetricItem("Insulin",     "${record.insulin.toInt()} μU/mL")
                MetricItem("Pregnancies", "${record.pregnancies.toInt()}")
            }

            HorizontalDivider(color = CardBorder, thickness = 0.5.dp)

            Text("📅  $dateStr", fontSize = 11.sp, color = TextHint)
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column {
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Text(label, fontSize = 10.sp, color = TextHint)
    }
}
