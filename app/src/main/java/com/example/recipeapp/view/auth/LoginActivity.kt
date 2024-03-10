package com.example.recipeapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.recipeapp.view.main.MainActivity
import com.example.recipeapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btn.setOnClickListener{
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(!email.isNullOrBlank() && !password.isNullOrBlank()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed, please ensure that your email and password is correct.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please ensure that you have entered a username and password.", Toast.LENGTH_LONG).show()
            }
        }

        binding.toSignUpText.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}