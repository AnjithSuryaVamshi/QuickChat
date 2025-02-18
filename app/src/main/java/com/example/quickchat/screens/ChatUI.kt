package com.example.quickchat.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quickchat.AppState
import com.example.quickchat.ChatUserData
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.Message

@Composable
fun ChatUI(
    viewmodel: ChatViewmodel,
    userData: ChatUserData,
    chatId: String,
    message: List<Message>,
    state : AppState,
    onBack: () -> Unit,
    context : Context = LocalContext.current

) {

    


}