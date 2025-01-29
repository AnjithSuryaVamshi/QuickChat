package com.example.quickchat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickchat.R


@Composable
fun SigninScreen(
    onSignInClick: () -> Unit
) {
    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFA9503),
            Color(0xFFFA3333)
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1368FD),
                        Color(0xFF5DD9FF)
                    ),
                    center = androidx.compose.ui.geometry.Offset(500f, 500f),
                    radius = 1000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.chatimg),
                contentDescription = "Logo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Quick Chat",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is a chat application for temporary interaction",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 71.dp)
            )
            Spacer(modifier = Modifier.height(86.dp))
            Button(
                onClick = { onSignInClick() },
                modifier = Modifier
                    .background(brush, CircleShape)
                    .fillMaxWidth(.7f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                shape = CircleShape
            ) {
                Text(
                    "continue with Google",
                    modifier = Modifier.padding(end = 20.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    painter = painterResource(id = R.drawable.google1),
                    contentDescription = "google",
                    modifier = Modifier.scale(2.5f)
                )

            }
        }
    }
}
