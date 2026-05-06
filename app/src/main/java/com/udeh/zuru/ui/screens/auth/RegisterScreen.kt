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
import androidx.compose.material.icons.filled.Person
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
import com.google.firebase.database.FirebaseDatabase
import com.udeh.zuru.R
import com.udeh.zuru.navigation.ROUT_HOME
import com.udeh.zuru.navigation.ROUT_LOGIN
import com.udeh.zuru.ui.theme.zurublue

// ── User model ────────────────────────────────────────────────────────────────
data class User(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val role: String = ""
)

// ── Register logic ────────────────────────────────────────────────────────────
fun registerUser(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    context: Context,
    navController: NavController
) {
    val mAuth = FirebaseAuth.getInstance()

    when {
        email.isBlank() || password.isBlank() || username.isBlank() -> {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
        }
        password != confirmPassword -> {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
        }
        password.length < 6 -> {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
        }
        else -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = mAuth.currentUser!!.uid

                        val userdata = User(
                            username = username,
                            email = email,
                            password = password,
                            uid = uid,
                            role = "user"
                        )

                        FirebaseDatabase.getInstance()
                            .getReference("Users/$uid")
                            .setValue(userdata)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_LONG).show()
                                    navController.navigate(ROUT_HOME) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        dbTask.exception?.message ?: "Failed to save user data",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            task.exception?.message ?: "Registration failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}

// ── Register Screen ───────────────────────────────────────────────────────────
@Composable
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }

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
            text = " See Kenya in a New and Fun Way!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Person") },
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

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
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

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            label = {
                Text(
                    text = "Create a Password",
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
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = confirmpassword,
            onValueChange = { confirmpassword = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
            label = {
                Text(
                    text = "Repeat your Password",
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
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                registerUser(
                    username = username,
                    email = email,
                    password = password,
                    confirmPassword = confirmpassword,
                    context = context,
                    navController = navController
                )
            },
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.width(300.dp),
        ) {
            Text(
                text = "Sign Up!",
                fontWeight = FontWeight.ExtraBold,
                color = zurublue
            )
        }

        TextButton(onClick = { navController.navigate(ROUT_LOGIN) }) {
            Text(
                text = "Already have an Account? Login.",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(rememberNavController())
}