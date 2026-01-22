package com.example.myapp.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    /**
     * Defines all the screens in the application for navigation purposes.
     * */

    @Serializable
    data object Login : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Album : Screen

    @Serializable
    data object AlbumDetail : Screen

    /**
     * Here we define screens that require parameters to be passed during navigation.
     * */
    @Serializable
    data class AlbumItemDetail(
        val itemId: String,
        val itemTitle: String
    ) : Screen

    @Serializable
    data class CardDetail(
        val cardId: String,
        val cardTitle: String
    ) : Screen
}