package com.example.quickchat.screens

import android.content.Context
import android.graphics.ImageDecoder
import android.icu.text.CaseMap.Title
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil3.Uri
import coil3.compose.AsyncImage
import com.example.quickchat.AppState
import com.example.quickchat.ChatUserData
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.Message
import com.example.quickchat.UserData
import com.example.quickchat.ViewImageScreen
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
    var imgUri by remember {
        mutableStateOf<android.net.Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imgUri = it
    }
    var bitmap by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }
    imgUri?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            var src = ImageDecoder.createSource(context.contentResolver, it)
            bitmap = ImageDecoder.decodeBitmap(src)
        }
        ImagePreview(
            uri = imgUri,
            hideDialog = {
                imgUri = null
            },
            sendImg = { imgUrl ->
                viewmodel.sendImage(imgUrl, chatId)
                imgUri = null
            }
        )

    }
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
                                .clickable {
                                    viewmodel.showImage(userData.ppurl.toString())
                                    navController.navigate(ViewImageScreen)
                                }
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
                        state = state,
                        navController = navController,
                        viewmodel = viewmodel
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {

                IconButton(
                    onClick = { launcher.launch("image/*") },
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
                            .focusRequester(focusRequester),
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
fun MessageItem(message: Message, index: Int, prevId: String, nextId: String, state: AppState,navController: NavController,viewmodel: ChatViewmodel) {

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
    val color = if (isCurrentUser) brush else brush2
    val allignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
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
            .indication(interactionSource, indication)
            .background(Color.Transparent)
            .fillMaxWidth(),
        contentAlignment = allignment
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(end = 5.dp, bottom = 5.dp)

        ) {
            if (!message.imgUrl.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .background(
                            color = if (isCurrentUser) Color(0xFFFFDF80) else Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))

                ) {
                    AsyncImage(
                        model = message.imgUrl,
                        contentDescription = "Shared Image",
                        modifier = Modifier
                            .widthIn(max = 240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .clickable {
                                viewmodel.showImage(message.imgUrl)
                                navController.navigate(ViewImageScreen)
                            }
                    )

                    if (message.content.isNotEmpty()) {
                        Text(
                            text = message.content.trim(),
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 4.dp)
                        )
                    }

                    Text(
                        text = formatter.format(message.time?.toDate()),
                        color = Color.Gray,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 8.dp, top = 2.dp, bottom = 4.dp)
                    )
                }
            }else if (message.content.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isCurrentUser) Color(0xFFFFDF80) else Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text(
                            text = message.content.trim(),
                            color = Color.Black,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatter.format(message.time?.toDate()),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }



        }
    }

}