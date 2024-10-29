package com.papb.projectpapb.data.model.network

data class GithubProfile(
    val avatar_url: String,
    val login: String,
    val name: String?,
    val following: Int,
    val followers: Int
)
