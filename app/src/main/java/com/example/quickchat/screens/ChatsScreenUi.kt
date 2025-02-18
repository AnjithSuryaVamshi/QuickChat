package com.example.quickchat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.quickchat.AppState
import com.example.quickchat.ChatData
import com.example.quickchat.ChatUserData
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.R
import com.example.quickchat.dialogs.CustomDialogBox
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatsScreenUi(viewmodel: ChatViewmodel,state :  AppState) {
    var expanded by remember { mutableStateOf(false) }
    val  chats = viewmodel.chats
    val filterChats = chats
    val selectedItem = remember {
        mutableStateListOf<String>()
    }


    Scaffold(
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                ) {
                    Icon(imageVector = Icons.Default.AddComment, contentDescription = "Add")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Message")
                        },
                        text = { Text("Qr Scanner") },
                        onClick = {
                            expanded = false
                            viewmodel.showDialog()
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.QrCode, contentDescription = "Camera")
                        },
                        text = { Text("Qr Code") },
                        onClick = {
                            expanded = false
                            // Handle action for second option
                        }
                    )
                }
            }
        }
    ) {
        it
        AnimatedVisibility(visible = state.showDialog) {
            CustomDialogBox(
                state =state,
                hideDialog = { viewmodel.hideDialog() },
                addChat = {
                    viewmodel.addChat(state.srEmail)
                    viewmodel.hideDialog()
                    viewmodel.setSrEmail("")
                },
                setEmail = {
                    viewmodel.setSrEmail(it)
                }

            )


        }
        Column(
            modifier =  Modifier
                .padding(top = 20.dp)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(filterChats){
                    val chatUser = if (it.user1?.userId != state.userData?.userId) {
                        it.user1
                    } else {
                        it.user2
                    }
                    ChatItem(
                        state = state,
                        chatUser!!,
                        chat = it,
                        mode = false,
                        isSelected = selectedItem.contains(it.chatId)

                    )
                }

            }

        }

    }
}

@Composable
fun ChatItem(
    state: AppState,
    userData : ChatUserData,
    chat : ChatData,
    mode : Boolean,
    isSelected : Boolean
) {

    val formatter = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }
    val color = if (!isSelected) Color.Transparent else MaterialTheme.colorScheme.onPrimary
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(color = color)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userData.ppurl)
                .crossfade(true)
                .allowHardware(true)
                .build(),
            placeholder = painterResource(id = R.drawable.blankprofile),
            error = painterResource(id = R.drawable.blankprofile),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp).clip(CircleShape)

        )

        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(.95f)
            ) {
                Text(
                    text = if (userData.userId == state.userData?.userId)
                        userData.username.orEmpty() + "(You)" else userData.username.orEmpty(),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (chat.last?.time != null)
                        formatter.format(chat.last?.time) else "",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                    maxLines = 1,
                    color = Color.Gray
                )


            }
            AnimatedVisibility(chat.last?.time != null && userData.typing) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(chat.last?.senderId == state.userData?.userId){
                        Icon(
                            imageVector = Icons.Default.Check, contentDescription = "Check",
                            modifier = Modifier.size(10.dp).padding(end = 5.dp),
                            tint = if(chat.last?.read ==true) Color.Green else Color.Gray
                        )


                    }

                }
            }
        }

    }
}
