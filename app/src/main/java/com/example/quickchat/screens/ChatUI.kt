package com.example.quickchat.screens

import android.content.Context
import android.icu.text.CaseMap.Title
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.quickchat.AppState
import com.example.quickchat.ChatUserData
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.Message
import com.example.quickchat.UserData
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatUI(
    viewmodel: ChatViewmodel,
    userData: ChatUserData,
    chatId: String,
    messages: List<Message>,
    state: AppState,
    onBack: () -> Unit,
    context: Context = LocalContext.current,
    navController: NavController
) {
    val listState = rememberLazyListState()
    val tp = viewmodel.tp
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) {
        viewmodel.popMessages(state.chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFCF50),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,


                    ) {
                        // Profile Picture
                        AsyncImage(
                            model = userData.ppurl,
                            contentDescription = "profile pic",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        )
                        // Username and Typing Indicator
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                text = userData.username.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (tp?.user1?.userId == userData.userId) {
                                AnimatedVisibility(tp.user1.typing) {
                                    Text(
                                        text = "typing...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            if (tp.user2?.userId == userData.userId) {
                                AnimatedVisibility(tp.user2.typing) {
                                    Text(
                                        text = "typing...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },

                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFEFAE0))
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                reverseLayout = true,
                state = listState
            ) {
                items(messages.size) { index ->
                    val message = messages.get(index)
                    val prevMessage = if (index > 0) messages[index - 1] else null
                    val nextMessage = if (index < messages.size - 1) messages[index + 1] else null

                    MessageItem(
                        message = message,
                        index = index,
                        prevId = prevMessage?.senderId.toString(),
                        nextId = nextMessage?.senderId.toString(),
                        state = state
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {

                IconButton(
                    onClick = { /* Handle camera action */ },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "camera",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(32.dp)
                        )
                ) {
                    TextField(
                        value = viewmodel.reply,
                        onValueChange = { viewmodel.reply = it },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                        ,
                        placeholder = {
                            Text(
                                text = "Type a message",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 3,
                        shape = RoundedCornerShape(32.dp)
                    )


                    AnimatedVisibility(visible = viewmodel.reply.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewmodel.sendReply(
                                    msg = viewmodel.reply,
                                    chatId = chatId,
//                                replyMessage = viewmodel.replyMessage
                                )
                                viewmodel.reply = ""
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Send,
                                contentDescription = "send",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, index: Int, prevId: String, nextId: String, state: AppState) {

    val context = LocalContext.current
    val brush = Brush.linearGradient(
        listOf(
            Color(0xFFAAE3F5),
            Color(0xFFD8ECFA)
        )
    )

    val brush2 = Brush.linearGradient(
        listOf(
            Color(0xFFFADA7A), // Peach
            Color(0xFFFFE8B3)  // Warm Gold
        )
    )





    val isCurrentUser = if (state.userData?.userId == message.senderId) true else false
    val shape = if (isCurrentUser) {
        if (prevId == message.senderId && nextId == message.senderId) {
            RoundedCornerShape(
                16.dp,
                3.dp,
                3.dp,
                16.dp
            )
        } else if (prevId == message.senderId) {
            RoundedCornerShape(
                16.dp,
                16.dp,
                3.dp,
                16.dp
            )
        } else if (nextId == message.senderId) {
            RoundedCornerShape(
                16.dp,
                3.dp,
                16.dp,
                3.dp
            )
        } else {
            RoundedCornerShape(
                16.dp,
                16.dp,
                16.dp,
                16.dp
            )
        }
    } else {
        if (prevId == message.senderId && nextId == message.senderId) {
            RoundedCornerShape(
                3.dp,
                16.dp,
                16.dp,
                3.dp
            )
        } else if (prevId == message.senderId) {
            RoundedCornerShape(
                3.dp,
                3.dp,
                16.dp,
                3.dp
            )
        } else if (nextId == message.senderId) {
            RoundedCornerShape(
                3.dp,
                16.dp,
                3.dp,
                16.dp
            )
        } else {
            RoundedCornerShape(
                16.dp,
                16.dp,
                16.dp,
                16.dp
            )
        }

    }
    val  color = if (isCurrentUser) brush else brush2
    val allignment = if(isCurrentUser)Alignment.CenterEnd else Alignment.CenterStart
    val formatter = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(
       bounded = true,
        color = Color(0xFFFFFFFF)
    )

    Box(
        modifier = Modifier
            .indication(interactionSource,indication)
            .background(Color.Transparent)
            .fillMaxWidth(),
        contentAlignment = allignment
    ){
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(end = 5.dp,bottom = 5.dp)

        ) {
            Column(
                modifier =  Modifier.shadow(2.dp,shape=shape).widthIn(max = 270.dp).fillMaxHeight().background(color,shape).padding(3.dp), horizontalAlignment =Alignment.End
            ) {
                if(message.content!=""){
                    Column {
                        Text(text = message.content.toString().trim(), color = Color.Black, modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),)
                        Text(text = formatter.format(message.time?.toDate()), color = Color.Gray, modifier = Modifier, fontSize = 12.sp)
                    }


                }
            }

        }
    }

}
