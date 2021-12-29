package com.secqure.secqureauth.models

data class FetchTokensResp(
    val tokens: Tokens,
    val user: User
)