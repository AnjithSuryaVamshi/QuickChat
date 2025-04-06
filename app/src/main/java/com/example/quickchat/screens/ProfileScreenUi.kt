package com.example.quickchat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.quickchat.AppState
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.dialogs.EditUserNameDialog
import kotlinx.coroutines.Job


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenUi(
    viewModel: ChatViewmodel,
    state: AppState,
    navController: NavHostController,
    function: () -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    val user = state.userData
    if(editing){
        EditUserNameDialog(
            onConfirm = { newName ->
                user?.username = newName
                if(user!=null)viewModel.updateUserName(user)
                editing = false
            },
            onDismiss = { editing = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .shadow(8.dp)
                    .height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFCF50),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(
                        text = "Edit Profile",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { editing = true },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFFEFAE0))
                .padding(horizontal = 24.dp),

        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user?.ppurl)
                        .crossfade(true)
                        .allowHardware(false)
                        .build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.White, CircleShape)
                        .border(4.dp, Color(0xFFFFCF50), CircleShape)
                        .shadow(4.dp, CircleShape)
                )

                IconButton(
                    onClick = { /* handle edit pic */ },
                    modifier = Modifier
                        .offset(x = 40.dp, y = 60.dp)
                        .size(36.dp)
                        .background(Color(0xFFFEFAE0), CircleShape)
                        .border(2.dp, Color(0xFFFFCF50), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Photo",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Username
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = false
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFCF50),
                                Color(0xFFFBAA36)
                            )
                        )
                    )
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Username
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.labelMedium.copy(color = Color.DarkGray)
                    )
                    Text(
                        text = user?.username ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )


                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.labelMedium.copy(color = Color.DarkGray)
                    )
                    Text(
                        text = user?.email ?: "example@email.com",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { function() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBAA36),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }



        }
    }
}
