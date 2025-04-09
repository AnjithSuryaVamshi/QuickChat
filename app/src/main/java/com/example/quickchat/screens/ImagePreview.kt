package com.example.quickchat.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.quickchat.CloudinaryHelper
import kotlinx.coroutines.launch

@Composable
fun ImagePreview(
    uri: Uri?,
    hideDialog: () -> Unit,
    sendImg: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Dialog(
        onDismissRequest = hideDialog,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = uri,
                contentDescription = "Preview Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = hideDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Cancel", color = Color.White)
                }

                Button(
                    onClick = {

                        coroutineScope.launch {
                            uri?.let {
                                val imageUrl = CloudinaryHelper.uploadImage(context, it)
                                if (imageUrl != null) {
                                    Toast.makeText(context,"Sending Image", Toast.LENGTH_SHORT).show()
                                    sendImg(imageUrl)
                                    hideDialog()
                                } else {
                                    Toast.makeText(context,"Failed to send Image", Toast.LENGTH_SHORT).show()
                                    Log.e("ImageUpload", "Failed to upload")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ){
                    Text("Send", color = Color.White)
                }
            }
        }
    }
}
