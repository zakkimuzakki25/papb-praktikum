package com.papb.projectpapb.api

import com.papb.projectpapb.data.model.network.GithubProfile
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {
    @GET("users/{username}")
    suspend fun getGithubProfile(@Path("username") username: String): GithubProfile
}
