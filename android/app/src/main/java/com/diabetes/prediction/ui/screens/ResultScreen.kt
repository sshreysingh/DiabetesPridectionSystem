package com.diabetes.prediction.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diabetes.prediction.ui.theme.*
import com.diabetes.prediction.viewmodel.PredictionUiState
import com.diabetes.prediction.viewmodel.PredictionViewModel

@Composable
fun ResultScreen(
    viewModel: PredictionViewModel,
    onNewPrediction: () -> Unit,
    onViewHistory: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val result  = (uiState as? PredictionUiState.Success)?.response

    if (result == null) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), Alignment.Center) {
            Text("No result available", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    val isDiabetic = result.prediction == 1
    val probability = result.probability
    val riskPct     = result.riskPercentage

    // Animated ring progress
    var targetProgress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue    = targetProgress,
        animationSpec  = tween(durationMillis = 1400, easing = EaseOutCubic),
        label          = "ring_progress"
    )

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
        kotlinx.coroutines.delay(300)
        targetProgress = probability
    }

    val primaryColor = if (isDiabetic) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer
    val primaryText  = if (isDiabetic) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Header row ────────────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick  = onNavigateHome,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(Icons.Filled.Home, "Home", tint = MaterialTheme.colorScheme.onBackground)
                }
                Text("Assessment Report", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.size(44.dp))
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Circular Ring ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = scaleIn(tween(600, easing = EaseOutBack)) + fadeIn(tween(400))
            ) {
                Box(
                    modifier         = Modifier.size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressRing(
                        progress   = animatedProgress,
                        ringColor  = primaryText,
                        trackColor = MaterialTheme.colorScheme.outline,
                        modifier   = Modifier.fillMaxSize()
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isDiabetic) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint     = primaryText,
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${(animatedProgress * 100).toInt()}%",
                            fontSize   = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = primaryText
                        )
                        Text("Risk Level", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Result Badge ──────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = slideInVertically(tween(600, 300)) { it / 2 } + fadeIn(tween(600, 300))
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDiabetic) WarningRedBg else SuccessGreenBg)
                            .padding(horizontal = 22.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text       = if (isDiabetic) "ELEVATED RISK DETECTED" else "NO ELEVATED RISK",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = primaryText
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text      = if (isDiabetic)
                            "The model indicates an elevated risk based on the provided metrics. Please consult a healthcare professional for a formal diagnosis."
                        else
                            "The model does not indicate an elevated risk of diabetes based on the provided metrics. Continue maintaining a healthy lifestyle.",
                        fontSize  = 14.sp,
                        color     = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Stats Row ─────────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(600, 500))
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard("Risk Score",  "${riskPct.toInt()}%",                    primaryText,        Modifier.weight(1f))
                    StatCard("Non-Risk",    "${(100 - riskPct).toInt()}%",             MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    StatCard("Prediction",  if (isDiabetic) "Positive" else "Negative", primaryText,      Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ── Health Tips ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(600, 700))
            ) {
                HealthTipsCard(isDiabetic)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Action Buttons ────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = slideInVertically(tween(500, 800)) { it / 3 } + fadeIn(tween(500, 800))
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick        = onNewPrediction,
                        modifier       = Modifier.fillMaxWidth().height(56.dp),
                        shape          = RoundedCornerShape(12.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Row(
                            verticalAlignment    = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Refresh, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Text("New Assessment", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    OutlinedButton(
                        onClick  = onViewHistory,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment    = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.History, null, modifier = Modifier.size(18.dp))
                            Text("View Patient History", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

// ── Custom circular progress ring drawn with Canvas ───────────────────────────

@Composable
private fun ProgressRing(
    progress:   Float,
    ringColor:  Color,
    trackColor: Color,
    modifier:   Modifier = Modifier
) {
    val sweep = progress * 360f
    Box(
        modifier = modifier.drawBehind {
            val stroke = 18.dp.toPx()
            val diam   = size.minDimension - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)
            val arcSize = Size(diam, diam)

            // Track
            drawArc(
                color      = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter  = false,
                topLeft    = topLeft,
                size       = arcSize,
                style      = Stroke(stroke, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                color      = ringColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter  = false,
                topLeft    = topLeft,
                size       = arcSize,
                style      = Stroke(stroke, cap = StrokeCap.Round)
            )
        }
    )
}

// ── Stat mini-card ────────────────────────────────────────────────────────────

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

// ── Health Tips card ──────────────────────────────────────────────────────────

@Composable
private fun HealthTipsCard(isDiabetic: Boolean) {
    val tips = if (isDiabetic) listOf(
        "🩺" to "Consult a physician for a formal clinical diagnosis",
        "🥗" to "Adopt a balanced diet low in refined carbohydrates",
        "🏃" to "Aim for 30+ minutes of moderate exercise daily",
        "💊" to "Monitor fasting blood glucose levels regularly",
        "🚰" to "Maintain proper hydration throughout the day"
    ) else listOf(
        "🥦" to "Maintain a diet rich in vegetables and whole grains",
        "🏃" to "Stay active with ≥150 min of exercise per week",
        "⚖️" to "Keep BMI within the healthy clinical range",
        "🩺" to "Schedule regular annual physical check-ups",
        "😴" to "Ensure 7–9 hours of quality sleep nightly"
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Clinical Recommendations", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            tips.forEach { (emoji, tip) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment     = Alignment.Top
                ) {
                    Text(emoji, fontSize = 16.sp)
                    Text(tip, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
