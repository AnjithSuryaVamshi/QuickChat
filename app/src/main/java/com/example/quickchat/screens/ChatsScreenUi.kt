package com.example.quickchat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quickchat.AppState
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.dialogs.CustomDialogBox

@Composable
fun ChatsScreenUi(viewmodel: ChatViewmodel,state :  AppState) {
    var expanded by remember { mutableStateOf(false) }

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
                        text = { Text("") },
                        onClick = {
                            expanded = false
                            viewmodel.showDialog()
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.QrCode, contentDescription = "Camera")
                        },
                        text = { Text("") },
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
                    viewmodel.addChat(state.srEmail),
                    viewmodel.hideDialog(),
                    viewmodel.setSrEmail("")
                },
                setEmail = {
                    viewmodel.setSrEmail(it)
                }

            ) {

            }
        }

    }
}