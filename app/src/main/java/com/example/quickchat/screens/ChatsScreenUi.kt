package com.example.quickchat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.quickchat.ActionIcon
import com.example.quickchat.AppState
import com.example.quickchat.CHAT_COLLECTION
import com.example.quickchat.ChatData
import com.example.quickchat.ChatUserData
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.ProfileScreen
import com.example.quickchat.R
import com.example.quickchat.SwipableItemActions
import com.example.quickchat.dialogs.CustomDialogBox
import com.example.quickchat.dialogs.DeleteChatDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreenUi(
    viewmodel: ChatViewmodel,
    state: AppState,
    showSingleChat: (ChatUserData, String) -> Unit,
    showQr : () -> Unit,
    showScanner : ()->Unit,
    navController: NavController = rememberNavController()
) {
    var expanded by remember { mutableStateOf(false) }
    val chats = viewmodel.chats
    val filterChats = chats
    val selectedItem = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                    .shadow(8.dp)
                ,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFCF50),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(
                        text = "Session Chat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(ProfileScreen) },
                        modifier = Modifier
                            .padding(20.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer) // Optional background
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Menu",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                }
            )
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.AddComment,
                        contentDescription = "Add Chat",
                        modifier = Modifier.size(24.dp),
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "QR Scanner",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        text = {
                            Text(
                                text = "QR Scanner",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            expanded = false
                            showScanner()
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        text = {
                            Text(
                                text = "QR Code",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            expanded = false
                            showQr()
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFEFAE0))
        ) {
            AnimatedVisibility(visible = state.showDialog) {
                CustomDialogBox(
                    state = state,
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
            var showDeleteDialog by remember { mutableStateOf(false) }
            var chatIdToDelete by remember { mutableStateOf<String?>(null) }
            if (showDeleteDialog && chatIdToDelete != null) {
                DeleteChatDialog(
                    onConfirm = {
                        viewmodel.deleteChat(chatIdToDelete!!)
                        showDeleteDialog = false
                        chatIdToDelete = null
                    },
                    onDismiss = {
                        showDeleteDialog = false
                        chatIdToDelete = null
                    }
                )
            }

            if (chats.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.emp),
                        contentDescription = "No active sessions image",
                        modifier = Modifier.size(280.dp)
                    )
                    Text(
                        text = "No active sessions",
                        modifier = Modifier.padding(top = 16.dp),
                        fontSize = 24.sp,  
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filterChats) { chat ->
                        val chatUser = if (chat.user1?.userId != state.userData?.userId) {
                            chat.user1
                        } else {
                            chat.user2
                        }
                        if (chatUser != null) {
                            var isRevealed by remember { mutableStateOf(false) }
                            SwipableItemActions(
                                isRevealed = isRevealed,
                                onExpanded = { isRevealed = true },
                                onCollapsed = { isRevealed = false },
                                actions = {
                                    ActionIcon(
                                        onClick = {
                                            isRevealed = false
                                            chatIdToDelete = chat.chatId
                                            showDeleteDialog = true
                                        },
                                        backgroundColor = Color.Red,
                                        icon = Icons.Default.Delete
                                    )
                                }

                            ) {
                                ChatItem(
                                    state = state,
                                    userData = chatUser,
                                    chat = chat,
                                    mode = false,
                                    isSelected = selectedItem.contains(chat.chatId),
                                    showSingleChat = { user, id -> showSingleChat(user, id) }
                                )
                            }
                        }

                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    state: AppState,
    userData: ChatUserData,
    chat: ChatData,
    mode: Boolean,
    isSelected: Boolean,
    showSingleChat: (ChatUserData, String) -> Unit
) {
    val formatter = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color(0xFFFEFAE0)
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val date = chat.last?.time?.toDate()

    Column {
        Row(
            modifier = Modifier
                .clickable { showSingleChat(userData, chat.chatId) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .shadow(3.dp, RoundedCornerShape(12.dp), clip = false)
                .background(backgroundColor)
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
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (userData.userId == state.userData?.userId)
                            "${userData.username.orEmpty()} (You)" else userData.username.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColor,
                        modifier = Modifier.weight(0.7f)
                    )
                    Text(
                        text = date?.let { formatter.format(it) } ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                AnimatedVisibility(visible = chat.last?.time != null || userData.typing) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (chat.last?.imgUrl == null)
                                chat.last?.content.orEmpty()
                            else if (chat.last.senderId == state.userData?.userId)
                                "Image Sent"
                            else
                                "Image Received",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (chat.last?.senderId == state.userData?.userId) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check",
                                modifier = Modifier
                                    .size(14.dp)
                                    .padding(end = 5.dp),
                                tint = if (chat.last?.read == true) Color.Green else Color.Gray
                            )
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEFAE0))
        ) {
            Divider(
                color = Color.LightGray.copy(alpha = 0.4f),
                thickness = 0.6.dp,
            )
        }

    }
}
