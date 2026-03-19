package com.example.csc_436_final_project.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.csc_436_final_project.ui.enneagram.EnneagramScreen
import com.example.csc_436_final_project.ui.enneagram.compatibility.CompatibilityScreen
import com.example.csc_436_final_project.ui.enneagram.rheti.RhetiResultsScreen
import com.example.csc_436_final_project.ui.enneagram.rheti.RhetiStartScreen
import com.example.csc_436_final_project.ui.enneagram.rheti.RhetiTestScreen
import com.example.csc_436_final_project.ui.enneagram.rheti.RhetiViewModel
import com.example.csc_436_final_project.ui.splash.SplashScreen

@Composable
fun AppNavGraph(incomingPartnerType: String? = null) {
    val navController = rememberNavController()

    // One shared VM instance for the whole project
    // to keep DataStore state in sync
    val rhetiViewModel: RhetiViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash
    ) {
        composable<Routes.Splash> {
            SplashScreen(
                onContinue = {
                    if (incomingPartnerType != null) {
                        // redirect logic for Deep Links (tricky part for me)
                        navController.navigate(route = Routes.Compatibility(partnerType = incomingPartnerType)) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    } else {
                        // default app launch
                        navController.navigate(route = Routes.Enneagram) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable<Routes.Enneagram> {
            EnneagramScreen(
                viewModel = rhetiViewModel, // Now passing VM to check test status
                onDiscoverClick = {
                    rhetiViewModel.reset()
                    navController.navigate(route = Routes.RhetiStart)
                },
                onCompatibilityClick = { partnerType ->
                    // Navigate to Compatibility with "None" or whatever string is passed
                    navController.navigate(route = Routes.Compatibility(partnerType = partnerType))
                }
            )
        }

        composable<Routes.Compatibility> { backStackEntry ->
            // extract the type-safe partnerType parameter
            val partnerType = backStackEntry.toRoute<Routes.Compatibility>().partnerType

            CompatibilityScreen(
                partnerType = partnerType,
                viewModel = rhetiViewModel,
                onTakeTest = {
                    rhetiViewModel.reset()
                    navController.navigate(route = Routes.RhetiStart)
                },
                onBack = {
                    // Go back to the main Enneagram screen
                    navController.navigate(route = Routes.Enneagram) {
                        popUpTo(Routes.Enneagram) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.RhetiStart> {
            RhetiStartScreen(
                onStartTest = { navController.navigate(route = Routes.RhetiTest) },
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.RhetiTest> {
            RhetiTestScreen(
                viewModel = rhetiViewModel,
                onExit = {
                    navController.popBackStack(route = Routes.Enneagram, inclusive = false)
                },
                onFinished = {
                    navController.navigate(route = Routes.RhetiResults)
                }
            )
        }

        composable<Routes.RhetiResults> {
            RhetiResultsScreen(
                viewModel = rhetiViewModel,
                onBackHome = {
                    navController.popBackStack(route = Routes.Enneagram, inclusive = false)
                }
            )
        }
    }
}