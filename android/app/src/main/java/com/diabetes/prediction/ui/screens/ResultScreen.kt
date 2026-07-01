package com.diabetes.prediction.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
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
        Box(Modifier.fillMaxSize().background(DeepNavy), Alignment.Center) {
            Text("No result available", color = TextSecondary)
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

    val primaryColor = if (isDiabetic) Red400   else Green400
    val bgTint       = if (isDiabetic) Color(0xFF1A0808) else Color(0xFF081A0E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(bgTint, DeepNavy, Navy900)))
    ) {
        // Ambient glow
        Box(
            modifier = Modifier
                .size(420.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-120).dp)
                .background(
                    Brush.radialGradient(listOf(primaryColor.copy(alpha = 0.07f), Color.Transparent)),
                    CircleShape
                )
        )

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
                    modifier = Modifier.size(44.dp).background(Navy700, CircleShape)
                ) {
                    Icon(Icons.Filled.Home, "Home", tint = TextPrimary)
                }
                Text("Analysis Result", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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
                        ringColor  = primaryColor,
                        trackColor = Navy700,
                        modifier   = Modifier.fillMaxSize()
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isDiabetic) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint     = primaryColor,
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${(animatedProgress * 100).toInt()}%",
                            fontSize   = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = primaryColor
                        )
                        Text("Risk Level", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

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
                            .clip(RoundedCornerShape(50.dp))
                            .background(primaryColor.copy(alpha = 0.14f))
                            .border(1.dp, primaryColor.copy(alpha = 0.35f), RoundedCornerShape(50.dp))
                            .padding(horizontal = 22.dp, vertical = 11.dp)
                    ) {
                        Text(
                            text       = if (isDiabetic) "⚠️  Diabetic Risk Detected" else "✅  No Diabetes Risk",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = primaryColor
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text      = if (isDiabetic)
                            "Your results indicate an elevated risk. Please consult a healthcare professional for a proper diagnosis."
                        else
                            "Great news! Your results suggest a lower risk of diabetes. Keep up your healthy lifestyle.",
                        fontSize  = 13.sp,
                        color     = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 21.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stats Row ─────────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(600, 500))
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard("Risk Score",  "${riskPct.toInt()}%",                    primaryColor,        Modifier.weight(1f))
                    StatCard("Non-Risk",    "${(100 - riskPct).toInt()}%",             Teal400,             Modifier.weight(1f))
                    StatCard("Prediction",  if (isDiabetic) "Positive" else "Negative", primaryColor,      Modifier.weight(1f))
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

            Spacer(modifier = Modifier.height(22.dp))

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
                        shape          = RoundedCornerShape(14.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Brush.horizontalGradient(listOf(Teal500, Purple500)), RoundedCornerShape(14.dp)),
                            Alignment.Center
                        ) {
                            Row(
                                verticalAlignment    = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Filled.Refresh, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                Text("New Prediction", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick  = onViewHistory,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape    = RoundedCornerShape(14.dp),
                        border   = BorderStroke(1.dp, CardBorder),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                    ) {
                        Row(
                            verticalAlignment    = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.History, null, modifier = Modifier.size(18.dp))
                            Text("View History", fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Navy700)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = TextHint, textAlign = TextAlign.Center)
    }
}

// ── Health Tips card ──────────────────────────────────────────────────────────

@Composable
private fun HealthTipsCard(isDiabetic: Boolean) {
    val tips = if (isDiabetic) listOf(
        "🩺" to "Consult your doctor immediately for proper diagnosis",
        "🥗" to "Follow a balanced diet low in sugar and refined carbs",
        "🏃" to "Aim for at least 30 minutes of moderate exercise daily",
        "💊" to "Monitor your blood glucose levels regularly",
        "🚰" to "Stay well-hydrated — drink plenty of water"
    ) else listOf(
        "🥦" to "Maintain a diet rich in vegetables, fibre and whole grains",
        "🏃" to "Stay active with ≥150 min of moderate exercise per week",
        "⚖️" to "Keep your BMI in the healthy range (18.5 – 24.9)",
        "🩺" to "Schedule regular check-ups to catch any changes early",
        "😴" to "Prioritise 7–9 hours of quality sleep each night"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Navy700)
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Health Recommendations", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        HorizontalDivider(color = CardBorder, thickness = 0.8.dp)
        tips.forEach { (emoji, tip) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment     = Alignment.Top
            ) {
                Text(emoji, fontSize = 16.sp)
                Text(tip, fontSize = 13.sp, color = TextSecondary, lineHeight = 20.sp, modifier = Modifier.weight(1f))
            }
        }
    }
}
