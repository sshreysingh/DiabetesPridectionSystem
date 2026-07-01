package com.diabetes.prediction.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            containerColor   = MaterialTheme.colorScheme.surface,
            title  = { Text("Clear Records", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
            text   = { Text("Delete all patient prediction records? This cannot be undone.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearHistory(); showClearDialog = false }) {
                    Text("Delete All", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Filled.ArrowBack, "Back", tint = MaterialTheme.colorScheme.onBackground)
                }

                Text(
                    "Patient Records",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground,
                    modifier   = Modifier.align(Alignment.Center)
                )

                if (historyList.isNotEmpty()) {
                    IconButton(
                        onClick  = { showClearDialog = true },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Filled.DeleteOutline, "Clear records", tint = MaterialTheme.colorScheme.error)
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
                        Icon(
                            Icons.Filled.SnippetFolder,
                            contentDescription = null,
                            tint     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Text("No records found", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            "Patient assessments will appear here.",
                            fontSize  = 15.sp,
                            color     = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding    = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "${historyList.size} Past Assessments",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
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
    val headerColor  = if (isDiabetic) WarningRed else SuccessGreen
    val headerBg     = if (isDiabetic) WarningRedBg else SuccessGreenBg

    val dateStr = remember(record.timestamp) {
        SimpleDateFormat("MMM dd, yyyy  •  hh:mm a", Locale.getDefault())
            .format(Date(record.timestamp))
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column {
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
                        tint             = headerColor,
                        modifier         = Modifier.size(20.dp)
                    )
                    Text(
                        if (isDiabetic) "ELEVATED RISK" else "NO RISK",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = headerColor
                    )
                }
                Text(
                    "${(record.probability * 100).toInt()}% conf",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = headerColor
                )
            }

            // Metrics grid
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

                Text("Record Date: $dateStr", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column {
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
