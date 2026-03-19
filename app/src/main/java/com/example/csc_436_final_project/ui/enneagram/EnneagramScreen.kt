package com.example.csc_436_final_project.ui.enneagram

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.csc_436_final_project.R
import com.example.csc_436_final_project.ui.enneagram.rheti.RhetiViewModel

@Composable
fun EnneagramScreen(
    viewModel: RhetiViewModel,
    onDiscoverClick: () -> Unit = {},
    onCompatibilityClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isTestCompleted = viewModel.answeredCount() == uiState.total

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable scrolling for landscape
                .padding(horizontal = 28.dp)
                .padding(top = 64.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.title_enneagram),
                contentDescription = "Enneagram title",
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "The Enneagram is a powerful tool for\n" +
                        "self-discovery and personal growth. It\n" +
                        "identifies nine distinct personality\n" +
                        "types, each with unique motivations,\n" +
                        "fears, and patterns of thinking.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Understanding your type can help you\n" +
                        "recognize your strengths, navigate\n" +
                        "challenges, and build deeper\n" +
                        "connections with others.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(44.dp))

            Button(
                onClick = onDiscoverClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7A8793),
                    contentColor = Color.White
                )
            ) {
                Text(text = if (isTestCompleted) "Retake Test" else "Discover Yours")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onCompatibilityClick("None") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2B2B2B)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text(text = "Check Compatibility")
            }
        }
    }
}
