package com.example.csc_436_final_project.ui.enneagram.rheti

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.csc_436_final_project.R

@Composable
fun RhetiResultsScreen(
    viewModel: RhetiViewModel,
    onBackHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val primary = viewModel.primaryType()
    val wing = viewModel.wingFor(primary)
    val code = if (wing == null) "Type $primary" else "${primary}w$wing"
    val info = viewModel.typeInfo(primary)

    val context = LocalContext.current

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
            Image(
                painter = painterResource(id = enneagramDiagramRes(primary)),
                contentDescription = "Enneagram diagram result",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )

            Spacer(Modifier.height(22.dp))

            Text(
                text = code,
                style = MaterialTheme.typography.displayLarge,
                color = Color.Black
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = info.title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = info.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color(0xFF2B2B2B),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(34.dp))

            Button(
                onClick = {
                    val deepLink = "enneagram://match?partnerType=$code"
                    val shareText = buildString {
                        append("I just took the Enneagram test and I'm a $code (${info.title})! 🌟\n\n")
                        append("Tap this link to see our compatibility in the app:\n")
                        append(deepLink)
                    }
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        putExtra(Intent.EXTRA_TITLE, "My Enneagram Result")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share your Enneagram Card"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7A8793),
                    contentColor = Color.White
                )
            ) {
                Text("Share Card")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onBackHome,
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

private fun enneagramDiagramRes(type: Int): Int {
    return when (type) {
        1 -> R.drawable.enneagram_1
        2 -> R.drawable.enneagram_2
        3 -> R.drawable.enneagram_3
        4 -> R.drawable.enneagram_4
        5 -> R.drawable.enneagram_5
        6 -> R.drawable.enneagram_6
        7 -> R.drawable.enneagram_7
        8 -> R.drawable.enneagram_8
        9 -> R.drawable.enneagram_9
        else -> R.drawable.enneagram_9
    }
}
