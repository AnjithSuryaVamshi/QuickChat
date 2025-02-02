package com.example.quickchat

data class SignInResult(
    val data:  UserData?,
    val errorMessage: String?


)
data class UserData(
    val userId: String,
    val username: String?,
    val ppurl: String?,
    val email : String?

)

data class  AppState(
    val isSignedIn : Boolean = false,
    val userData : UserData? = null,
    val errorMessage : String? = null,
    val  srEmail  : String = "",
    val showDialog  : Boolean = false
)