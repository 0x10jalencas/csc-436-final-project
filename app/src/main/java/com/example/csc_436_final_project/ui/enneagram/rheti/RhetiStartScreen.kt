package com.example.csc_436_final_project.ui.enneagram.rheti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RhetiStartScreen(
    onStartTest: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable scrolling for landscape
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RHETI Test",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "You are now going to take the Enneagram RHETI test.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Disclaimer: For the most accurate results, answer as honestly as possible " +
                        "based on how you typically think, feel, and behave, not how you wish you did.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2B2B2B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onStartTest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7A8793),
                    contentColor = Color.White
                )
            ) {
                Text("Start Test")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAEAEA),
                    contentColor = Color(0xFF2B2B2B)
                )
            ) {
                Text("Back")
            }
        }
    }
}
