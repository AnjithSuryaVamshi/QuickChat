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
            Color(0xFFFFC823),
            Color(0xFFFF5722)
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFCA72B),
                        Color(0xFF000936)
                    ),
                    center = androidx.compose.ui.geometry.Offset(500f, 600f),
                    radius = 1000f
                )
            )
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(76.dp))
            Image(
                painter = painterResource(id = R.drawable.imglogopng),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentDescription = "Logo"
            )

            Spacer(modifier = Modifier.height(76.dp))

            Text(
                text = "Quick Chat",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color(0xFFFFFFFF),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Chat application for temporary interaction",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,

                color = Color.White,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 71.dp)
            )
            Spacer(modifier = Modifier.height(56.dp))
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
