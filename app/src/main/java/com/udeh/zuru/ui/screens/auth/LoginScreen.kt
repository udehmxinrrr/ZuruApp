package com.udeh.zuru.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.udeh.zuru.R
import com.udeh.zuru.navigation.ROUT_HOME
import com.udeh.zuru.navigation.ROUT_REGISTER
import com.udeh.zuru.ui.theme.zurublue

// ── Login logic ───────────────────────────────────────────────────────────────
fun loginUser(
    email: String,
    password: String,
    context: Context,
    navController: NavController
) {
    val mAuth = FirebaseAuth.getInstance()

    when {
        email.isBlank() || password.isBlank() -> {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
        }
        else -> {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show()
                        navController.navigate(ROUT_HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            task.exception?.message ?: "Login failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}

// ── Login Screen ──────────────────────────────────────────────────────────────
@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(zurublue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.z),
            contentDescription = "logo",
            modifier = Modifier.size(60.dp),
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
                .padding(start = 20.dp, end = 20.dp)
        )

        Text(
            text = "Welcome Back!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ── Email field ───────────────────────────────────────────────────────
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
            },
            label = {
                Text(
                    text = "Enter Your Email",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedLeadingIconColor = Color.White,
                focusedTextColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Password field ────────────────────────────────────────────────────
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock")
            },
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

        Spacer(modifier = Modifier.height(25.dp))

        // ── Login button ──────────────────────────────────────────────────────
        Button(
            onClick = {
                loginUser(
                    email = email,
                    password = password,
                    context = context,
                    navController = navController
                )
            },
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}