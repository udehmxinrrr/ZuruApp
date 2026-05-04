package com.udeh.zuru.ui.screens.auth

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.R
import com.udeh.zuru.navigation.ROUT_HOME
import com.udeh.zuru.navigation.ROUT_REGISTER
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

// Swap these for your actual travel/destination drawable resources
val carouselImages = listOf(
    R.drawable.lakevasha,
    R.drawable.hab,
    R.drawable.lakenaivasha,
    R.drawable.savannah,
)

@Composable
fun LoginScreen(navController: NavController) {

    val pagerState = rememberPagerState(pageCount = { carouselImages.size })

    // Auto-scroll every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(8000)
            val nextPage = (pagerState.currentPage + 1) % carouselImages.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = EaseInOutCubic
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background carousel ───────────────────────────────────────────────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val alpha = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)

            Image(
                painter = painterResource(id = carouselImages[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { this.alpha = alpha }
            )
        }

        // ── Dark overlay so the form stays readable ───────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        // ── Login form on top ─────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.z),
                contentDescription = "logo",
                modifier = Modifier.size(60.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome Back!",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(40.dp))

            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(45.dp),
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
                label = {
                    Text(
                        text = "Enter Your Username",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedLeadingIconColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    focusedTextColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(45.dp),
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
                label = {
                    Text(
                        text = "Enter Your Password",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedLeadingIconColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    focusedTextColor = Color.White,
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate(ROUT_HOME) },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier.width(300.dp),
            ) {
                Text(
                    text = "Login!",
                    fontWeight = FontWeight.ExtraBold,
                    color = zurublue
                )
            }

            TextButton(onClick = { navController.navigate(ROUT_REGISTER) }) {
                Text(
                    text = "Don't have an Account? Sign Up!",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}