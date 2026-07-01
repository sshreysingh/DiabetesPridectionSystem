package com.diabetes.prediction.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diabetes.prediction.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var iconVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (iconVisible) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessLow
        ),
        label = "icon_scale"
    )

    LaunchedEffect(Unit) {
        delay(120)
        iconVisible = true
        delay(500)
        textVisible = true
        delay(1800)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(DeepNavy, Color(0xFF0D1B2A), Navy900))
            ),
        contentAlignment = Alignment.Center
    ) {
        // Ambient glow behind icon
        Box(
            modifier = Modifier
                .size(320.dp)
                .scale(scale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Teal500.copy(alpha = 0.14f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // App icon
            Box(
                modifier = Modifier
                    .scale(scale)
                    .size(104.dp)
                    .background(
                        Brush.linearGradient(listOf(Teal500, Purple500)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "GlucoCheck AI",
                    tint   = Color.White,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(
                visible = textVisible,
                enter   = fadeIn(tween(700)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec  = tween(700)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text       = "GlucoCheck AI",
                        fontSize   = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text          = "SMART DIABETES PREDICTION",
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.Medium,
                        color         = Teal400,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        // Bottom credit
        AnimatedVisibility(
            visible  = textVisible,
            enter    = fadeIn(tween(900, delayMillis = 600)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp)
        ) {
            Text(
                text      = "Powered by Machine Learning",
                fontSize  = 12.sp,
                color     = TextHint,
                textAlign = TextAlign.Center
            )
        }
    }
}
