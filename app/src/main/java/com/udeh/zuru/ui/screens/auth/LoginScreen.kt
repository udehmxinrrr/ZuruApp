package com.udeh.zuru.ui.screens.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udeh.zuru.ui.theme.zurublue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.R
import com.udeh.zuru.navigation.ROUT_HOME
import com.udeh.zuru.navigation.ROUT_LOGIN


@Composable
fun LoginScreen( navController: NavController){
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

        Spacer(modifier = Modifier
            .height(20.dp)
            .padding(start = 20.dp, end = 20.dp))

        Text(
            text= " Welcome Back!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(40.dp))



        //Variables
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }


        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
            label = { Text(text = "Enter Your Username",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White) },
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
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(45.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
            label = { Text(text = "Create a Password",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White) },
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


        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { navController.navigate(ROUT_HOME)  },
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.width(300.dp),
        ) {
            Text(text = "Login!",
                fontWeight = FontWeight.ExtraBold,
                color = zurublue)

        }
        TextButton(onClick = {navController.navigate(ROUT_LOGIN)}) {
            Text(text = "Don't have an Account? Sign Up!",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,)
        }


    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}