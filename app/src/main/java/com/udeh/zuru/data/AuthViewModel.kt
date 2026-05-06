package com.udeh.zuru.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.udeh.zuru.models.User
import com.udeh.zuru.navigation.ROUT_REGISTER

class AuthViewModel(var navController: NavController, var context: Context) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signup(username: String, email: String, password: String, confirmpassword: String) {

        if (email.isBlank() || password.isBlank() || confirmpassword.isBlank()) {
            Toast.makeText(context, "Please email and password cannot be blank", Toast.LENGTH_LONG)
                .show()
        } else if (password != confirmpassword) {
            Toast.makeText(context, "Password do not match", Toast.LENGTH_LONG).show()
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {

                    val uid = mAuth.currentUser!!.uid

                    // DEFAULT role = "user"
                    val role = "user"

                    val userdata = User(
                        username = username,
                        email = email,
                        password = password,
                        uid = uid,
                        role = role
                    )

                    val regRef = FirebaseDatabase.getInstance().getReference("Users/$uid")

                    regRef.setValue(userdata).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "${result.exception!!.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigate(ROUT_REGISTER)
                        }
                    }
                } else {
                    navController.navigate(ROUT_REGISTER)
                }
            }
        }
    }
}