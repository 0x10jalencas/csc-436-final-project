package com.example.csc_436_final_project.ui.enneagram.compatibility

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

object EnneagramAiCoach {

    private val config = generationConfig {
        temperature = 0.7f
        topK = 40
        topP = 0.95f
    }

    private val safety = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE)
    )

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = com.example.csc_436_final_project.BuildConfig.GEMINI_API_KEY,
        generationConfig = config,
        safetySettings = safety,
        systemInstruction = content {
            text("You are a strict, concise Enneagram Relationship Coach. When given two Enneagram types, provide exactly one sentence on their shared strength and one sentence on a conflict pro-tip. You must keep the total response strictly under 40 words. Do NOT include any intro text, conversational filler, or formatting.")
        }
    )

    suspend fun getRelationshipInsight(userType: String, partnerType: String): String {
        // B/c the system instructions handle the rules,
        // the prompt can just be the raw data.
        val prompt = "User: Type $userType\nPartner: Type $partnerType"

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text?.trim() ?: "Focus on mutual respect and active listening today!"

        } catch (e: ResponseStoppedException) {
            // This catches the specific MAX_TOKENS error so the app doesn't crash
            Log.w("ENNEAGRAM_AI", "Generation stopped early (MAX_TOKENS): ${e.message}")
            "You two have a dynamic connection! Focus on clear communication to stay aligned today."

        } catch (e: Exception) {
            // My catching for offline errors, API outages, etc.
            Log.e("ENNEAGRAM_AI", "Gemini API Error: ${e.message}", e)
            "You two have a dynamic connection! Focus on clear communication to stay aligned today."
        }
    }
}