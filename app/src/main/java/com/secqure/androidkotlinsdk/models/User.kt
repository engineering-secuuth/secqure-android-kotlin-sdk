package com.secqure.secqureauth.models

data class User(
    val createTime: String,
    val deviceName: String,
    val deviceType: String,
    val pubKey: String,
    val systemId: String,
    val updateTime: String,
    val userId: String
)