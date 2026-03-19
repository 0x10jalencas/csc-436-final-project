package com.example.csc_436_final_project.ui.enneagram.rheti

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RhetiTestScreen(
    viewModel: RhetiViewModel,
    onExit: () -> Unit = {},
    onFinished: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        if (uiState.index > 0) viewModel.prev() else onExit()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = "Which statement is more\ntrue of you most of the\ntime?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(36.dp))

            ChoiceBox(
                label = "A.",
                text = uiState.current.optionA.text,
                onClick = {
                    val done = viewModel.choose(RhetiChoice.A)
                    if (done) onFinished()
                }
            )

            Spacer(modifier = Modifier.height(26.dp))

            ChoiceBox(
                label = "B.",
                text = uiState.current.optionB.text,
                onClick = {
                    val done = viewModel.choose(RhetiChoice.B)
                    if (done) onFinished()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        Text(
            text = "${uiState.index + 1}/${uiState.total}",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF2B2B2B)
        )
    }
}

@Composable
private fun ChoiceBox(
    label: String,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Black)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$label $text",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF2B2B2B)
        )
    }
}
