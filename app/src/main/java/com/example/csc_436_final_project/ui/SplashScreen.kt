package com.example.csc_436_final_project.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.csc_436_final_project.R
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * SplashScreen that shows a logo image centered.
 *
 * - onContinue: callback invoked when the splash should navigate away.
 * - autoNavigate: when true, the composable will call onContinue after [autoDelayMs].
 * - autoDelayMs: delay in milliseconds before auto-navigation (only used when autoNavigate == true).
 * - logoSize: size of the logo
 *
 */

@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    autoNavigate: Boolean = false,
    autoDelayMs: Long = 1500L,
    logoSize: Dp = 240.dp
) {
    // When previewing in Android Studio, avoid launching real navigation delays
    val isPreview = LocalInspectionMode.current

    // If autoNavigate is requested, trigger it once after the delay
    LaunchedEffect(key1 = autoNavigate) {
        if (autoNavigate && !isPreview) {
            delay(autoDelayMs)
            onContinue()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // treat the whole screen as a single button for "tap to continue"
            .clickable(
                role = Role.Button,
                onClick = { onContinue() }
            )
            // add an accessible content description so talkback users know what the screen is
            .semantics { contentDescription = "Splash screen. Tap to continue." },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.persona_logo),
            contentDescription = "Persona logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(logoSize)
        )
    }
}