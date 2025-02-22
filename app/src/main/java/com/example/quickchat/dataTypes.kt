package com.example.quickchat

import com.google.firebase.Timestamp

data class SignInResult(
    val data:  UserData?,
    val errorMessage: String?


)
data class UserData(
    val userId: String="",
    val username: String?="",
    val ppurl: String?="",
    val email : String?=""

)

data class  AppState(
    val isSignedIn : Boolean = false,
    val userData : UserData? = null,
    val errorMessage : String? = null,
    val  srEmail  : String = "",
    val showDialog  : Boolean = false,
    val  user2 : ChatUserData?=null,
    val chatId : String = "",

)

data class ChatData(
    val chatId  : String="",
    val last :  Message? = null,
    val user1 : ChatUserData? = null,
    val user2 : ChatUserData? = null
)
data class Message(
    val msgId : String = "",
    val senderId : String = "",
    val repliedMessage : Message? = null,
    val imgUrl  : String? = null,
    val fileUrl : String? = null,
    val fileName : String? = null,
    val fileSize : String? = null,
    val videoUrl : String? = null,
    val progress : String? = null,
    val time : Timestamp? = null,
    val content : String = "",
    val forwarded : Boolean = false,
    val read : Boolean = false

)
data class ChatUserData(
    val userId : String = "",
    val username : String? = null,
    val ppurl : String? = null,
    val email : String? = null,
    val typing : Boolean = false,
    val unread : Int = 0,
)
