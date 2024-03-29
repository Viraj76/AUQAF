package com.example.facedetection.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.facedetection.databinding.ActivityProfessorLoginBinding
import com.example.facedetection.utils.Config
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
        Config.showDialog(this)
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                    startActivity(Intent(this, ProfessorActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
        }
        else{
            Config.hideDialog()
            Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onStart(){
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser!=null){
            startActivity(Intent(this,ProfessorActivity::class.java))
            finish()
        }
    }
}