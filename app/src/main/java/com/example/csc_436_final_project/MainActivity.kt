package com.example.csc_436_final_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.csc_436_final_project.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize a variable to hold the type if we find one
        var sharedPartnerType: String? = null

        // 2. Grab the intent that launched this activity
        val action: String? = intent?.action
        val data: Uri? = intent?.data

        // 3. Check if it's our specific "enneagram://" deep link
        if (Intent.ACTION_VIEW == action && data != null && data.scheme == "enneagram") {
            // 4. Extract the value assigned to "partnerType" (e.g., "7w8")
            sharedPartnerType = data.getQueryParameter("partnerType")
        }

        setContent {
            // 5. Pass that variable into nav graph!
            // (If they just opened the app normally, this will be null)
            AppNavGraph(incomingPartnerType = sharedPartnerType)
        }
    }
}