package com.papb.projectpapb

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GithubProfileViewModel : ViewModel() {
    private val _profile = mutableStateOf<GithubProfile?>(null)
    val profile: State<GithubProfile?> = _profile

    fun fetchGithubProfile(username: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getGithubProfile(username)
                _profile.value = result
            } catch (e: Exception) {

            }
        }
    }
}