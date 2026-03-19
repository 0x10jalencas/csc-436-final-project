package com.example.csc_436_final_project.ui.enneagram.compatibility

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csc_436_final_project.ui.enneagram.rheti.RhetiViewModel
import kotlinx.coroutines.launch

@Composable
fun CompatibilityScreen(
    partnerType: String, // Initial type from Deep Link or "None"
    viewModel: RhetiViewModel,
    onTakeTest: () -> Unit,
    onBack: () -> Unit
) {
    val lightGreyBackground = Color(0xFFF0F0F0)
    val slateGreyButton = Color(0xFF7A8793)

    // --- STATE ---
    // Using rememberSaveable to survive configuration changes (rotation)
    var currentPartnerType by rememberSaveable { mutableStateOf(partnerType) }
    var showManualPicker by rememberSaveable { mutableStateOf(false) }
    var aiInsight by rememberSaveable { mutableStateOf<String?>(null) }
    var isAiLoading by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val isTestCompleted = viewModel.answeredCount() == uiState.total

    // Logic for extraction
    val partnerCore = currentPartnerType.firstOrNull { it.isDigit() }?.digitToIntOrNull() ?: 0
    val displayPartnerType = if (currentPartnerType.startsWith("Type", ignoreCase = true)) {
        currentPartnerType
    } else if (currentPartnerType == "None") {
        "Select a Type"
    } else {
        "Type $currentPartnerType"
    }

    val userCore = if (isTestCompleted) viewModel.primaryType() else 0
    val userWing = if (isTestCompleted) viewModel.wingFor(userCore) else null
    val userDisplay = if (userWing == null) "Type $userCore" else "${userCore}w$userWing"

    // Recalculate match result based on current selections (survives rotation)
    val currentMatch = if (isTestCompleted && partnerCore != 0) {
        CompatibilityEngine.calculateMatch(userCore, partnerCore)
    } else null

    Box(modifier = Modifier.fillMaxSize().background(lightGreyBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Allow scrolling in landscape mode
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header with Back Arrow
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(text = "COMPATIBILITY", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(vertical = 12.dp))

            // THEIRS SECTION
            Text(text = "Theirs:", modifier = Modifier.align(Alignment.Start), fontSize = 18.sp, color = Color.Black)
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).border(2.dp, slateGreyButton, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(50.dp).background(Color.LightGray, RoundedCornerShape(25.dp)))
                    Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                        Text(text = displayPartnerType, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                        Text(text = "Selected Partner Type", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            // MANUAL PICKER TOGGLE
            TextButton(onClick = { showManualPicker = !showManualPicker }) {
                Text(text = if (showManualPicker) "✕ Close Picker" else "OR: Select Type Manually", color = slateGreyButton, fontWeight = FontWeight.Bold)
            }

            if (showManualPicker) {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(0.9f)) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val types = (1..9).toList()
                        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(180.dp)) {
                            items(types) { type ->
                                Surface(
                                    modifier = Modifier.padding(4.dp).aspectRatio(1f).clickable {
                                        currentPartnerType = type.toString()
                                        showManualPicker = false
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (partnerCore == type) slateGreyButton else Color(0xFFEFEFEF)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = type.toString(), fontWeight = FontWeight.Bold, color = if (partnerCore == type) Color.White else Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // YOURS SECTION
            Text(text = "Yours:", modifier = Modifier.align(Alignment.Start), fontSize = 18.sp, color = Color.Black)
            if (!isTestCompleted) {
                Button(onClick = onTakeTest, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).height(56.dp), shape = RoundedCornerShape(999.dp), colors = ButtonDefaults.buttonColors(containerColor = slateGreyButton)) {
                    Text(text = "+ Complete Your Enneagram Test", color = Color.White)
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).border(2.dp, slateGreyButton, RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(50.dp).background(slateGreyButton, RoundedCornerShape(25.dp)))
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(text = userDisplay, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                            Text(text = "Your result is saved.", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI LOADING INDICATOR
            if (isAiLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 8.dp), color = slateGreyButton)
            }

            // CALCULATION BUTTON
            Button(
                onClick = {
                    scope.launch {
                        isAiLoading = true
                        // AI Call
                        aiInsight = EnneagramAiCoach.getRelationshipInsight(userDisplay, displayPartnerType)
                        isAiLoading = false
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(999.dp),
                enabled = isTestCompleted && partnerCore != 0,
                colors = ButtonDefaults.buttonColors(containerColor = if (isTestCompleted && partnerCore != 0) slateGreyButton else Color.LightGray)
            ) {
                Text(text = if (isTestCompleted) "Check Compatibility" else "Complete Test to Check", color = if (isTestCompleted) Color.White else Color.DarkGray)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // --- DIALOG ---
    if (showDialog && currentMatch != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Close", color = slateGreyButton, fontWeight = FontWeight.Bold) } },
            title = { Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "${currentMatch.scorePercentage}% Match", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = slateGreyButton)
                Text(text = currentMatch.dynamicName, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
            }},
            text = {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Text("Strengths:", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(currentMatch.strengths, color = Color.DarkGray)

                    Spacer(Modifier.height(8.dp))

                    Text("Challenges:", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(currentMatch.challenges, color = Color.DarkGray)

                    // AI INSIGHT SECTION
                    aiInsight?.let { insight ->
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Text("✨ AI COACH PRO-TIP", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = slateGreyButton)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = insight,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = Color.Black
                        )
                    }
                }
            },
            containerColor = Color.White, shape = RoundedCornerShape(16.dp)
        )
    }
}
