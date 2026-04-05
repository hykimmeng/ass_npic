package com.example.assmobile.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assmobile.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1400),
        label = "welcomeAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2600)
        onContinue()
    }

    val scheme = MaterialTheme.colorScheme
    val gradient = Brush.verticalGradient(
        colors = listOf(
            scheme.primary.copy(alpha = 0.14f),
            scheme.surface,
            scheme.surfaceVariant.copy(alpha = 0.35f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Centered logo from drawable/npic_logo_png.png — fits inside card without cropping
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = scheme.primary.copy(alpha = 0.25f),
                        spotColor = scheme.primary.copy(alpha = 0.35f)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .padding(20.dp)
                    .size(260.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.npic_logo_png),
                    contentDescription = "School logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alphaAnim.value),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = scheme.primary,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                modifier = Modifier.alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "NIPC School Management",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = scheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
}
