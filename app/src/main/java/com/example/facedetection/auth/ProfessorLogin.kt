package com.example.facedetection.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.facedetection.ProfessorActivity
import com.example.facedetection.R
import com.example.facedetection.StudentActivity
import com.example.facedetection.databinding.ActivityProfessorLoginBinding
import com.google.firebase.auth.FirebaseAuth

class ProfessorLogin : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            loginProfessor()
        }
    }
    private fun loginProfessor() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnFailureListener {
                    Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ProfessorActivity::class.java))
                    finish()
                }
        }
    }
}