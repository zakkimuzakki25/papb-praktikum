package com.papb.projectpapb

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            ScheduleScreen(navController)
        }
        composable("githubProfile") {
            val viewModel = viewModel<GithubProfileViewModel>()
            GithubProfileScreen(viewModel, "zakkimuzakki25")
        }
    }
}
