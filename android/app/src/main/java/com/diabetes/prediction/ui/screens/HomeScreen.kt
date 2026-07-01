package com.diabetes.prediction.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diabetes.prediction.ui.theme.*

@Composable
fun HomeScreen(
    onStartPrediction: () -> Unit,
    onViewHistory: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepNavy, Navy900)))
    ) {
        // Decorative ambient glows
        Box(
            modifier = Modifier
                .offset(x = (-60).dp, y = (-100).dp)
                .size(340.dp)
                .background(
                    Brush.radialGradient(listOf(Teal500.copy(alpha = 0.1f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = 180.dp)
                .size(260.dp)
                .background(
                    Brush.radialGradient(listOf(Purple500.copy(alpha = 0.08f), Color.Transparent)),
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
            Spacer(modifier = Modifier.height(44.dp))

            // ── Header ────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { -it / 2 }, animationSpec = tween(600)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .background(
                                Brush.linearGradient(listOf(Teal500, Purple500)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector      = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint             = Color.White,
                            modifier         = Modifier.size(44.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text       = "GlucoCheck AI",
                        fontSize   = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text      = "Advanced diabetes risk assessment\npowered by machine learning",
                        fontSize  = 14.sp,
                        color     = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── Feature Cards ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(700, 200)) + slideInVertically(
                    initialOffsetY = { it / 3 }, animationSpec = tween(700, 200)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeatureCard(
                        icon  = Icons.Filled.Psychology,
                        title = "AI-Powered Analysis",
                        desc  = "Trained on 768 patient records using a Random Forest classifier",
                        tints = listOf(Teal600, Teal500)
                    )
                    FeatureCard(
                        icon  = Icons.Filled.Speed,
                        title = "Instant Results",
                        desc  = "Receive your risk assessment and confidence score in seconds",
                        tints = listOf(Purple600, Purple500)
                    )
                    FeatureCard(
                        icon  = Icons.Filled.History,
                        title = "Track Your History",
                        desc  = "All predictions are saved locally — monitor your health over time",
                        tints = listOf(Green600, Green500)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── CTA Buttons ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(800, 400))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Primary gradient button
                    Button(
                        onClick          = onStartPrediction,
                        modifier         = Modifier.fillMaxWidth().height(58.dp),
                        shape            = RoundedCornerShape(16.dp),
                        colors           = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding   = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(Teal500, Purple500)),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment    = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(22.dp))
                                Text("Start Prediction", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    // Secondary outlined button
                    OutlinedButton(
                        onClick  = onViewHistory,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(16.dp),
                        border   = BorderStroke(1.dp, CardBorder),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                    ) {
                        Row(
                            verticalAlignment    = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.History, null, modifier = Modifier.size(20.dp))
                            Text("View History", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text      = "⚠️  For informational purposes only.\nNot a substitute for professional medical advice.",
                fontSize  = 11.sp,
                color     = TextHint,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun FeatureCard(
    icon:  ImageVector,
    title: String,
    desc:  String,
    tints: List<Color>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Navy700)
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment    = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Brush.linearGradient(tints), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(3.dp))
            Text(desc, fontSize = 12.sp, color = TextSecondary, lineHeight = 18.sp)
        }
    }
}
