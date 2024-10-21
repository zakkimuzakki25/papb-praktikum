package com.papb.projectpapb.navgation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.papb.projectpapb.InputScreen
import com.papb.projectpapb.screen.GithubProfileScreen
import com.papb.projectpapb.viewmodel.GithubProfileViewModel
import com.papb.projectpapb.screen.ScheduleScreen
import com.papb.projectpapb.screen.TugasScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()

    NavHost(navController = navController, startDestination = if (auth.currentUser != null) "matkul" else "login", modifier = modifier) {

        composable("login") {
            InputScreen(
                auth = auth,
                onLoginSuccess = {
                    // Navigate to "matkul" screen after successful login
                    navController.navigate("matkul") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Route for Matkul Screen
        composable("matkul") {
            ScheduleScreen(navController)
        }

        // Route for Profile Screen
        composable("profile") {
            val viewModel = viewModel<GithubProfileViewModel>()
            GithubProfileScreen(viewModel, "zakkimuzakki25")
        }

        // Route for Tugas Screen
        composable("tugas") {
            TugasScreen()
        }
    }
}
