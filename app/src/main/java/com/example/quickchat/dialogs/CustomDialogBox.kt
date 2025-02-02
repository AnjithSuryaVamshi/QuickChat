package com.example.quickchat.dialogs

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quickchat.AppState


@Composable
fun CustomDialogBox(
    state: AppState,
    setEmail: (String) -> Unit,
    hideDialog: () -> Unit,
    addChat: () -> Unit
) {
    Dialog(
        onDismissRequest = { hideDialog() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primary
            )

        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Text("enter your email",modifier = Modifier.align(Alignment.CenterHorizontally))
                OutlinedTextField(
                    label = { Text("email") },
                    value = state.srEmail,
                    onValueChange = { setEmail(it) },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { hideDialog }
                    ) {
                        Text(
                            "cancel",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
                        onClick = { addChat }
                    ) {
                        Text(
                            "Add",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}