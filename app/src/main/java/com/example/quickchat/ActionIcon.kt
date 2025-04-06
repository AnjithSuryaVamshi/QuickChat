package com.example.quickchat.ui

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun ActionIcon(
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Int = Color.WHITE
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(backgroundColor)
    ) {

    }

}