package com.secqure.secqureauth.models

data class Tokens(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String
)