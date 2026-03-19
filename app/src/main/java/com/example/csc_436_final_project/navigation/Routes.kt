package com.example.csc_436_final_project.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object Splash : Routes()

    @Serializable
    object Enneagram : Routes()

    @Serializable
    object RhetiStart : Routes()

    @Serializable
    object RhetiTest : Routes()

    @Serializable
    object RhetiResults : Routes()

    @Serializable data class Compatibility(val partnerType: String) : Routes()
}