package com.example.facedetection.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.facedetection.student.StudentActivity
import com.example.facedetection.databinding.ActivityStudentLoginBinding
import com.example.facedetection.prof.StudentModel
import com.example.facedetection.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentLogin : AppCompatActivity() {
    private lateinit var binding: ActivityStudentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            loginStudent()
        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this,StudentRegister::class.java))
            finish()
        }
    }
    private fun loginStudent() {
        Config.showDialog(this)
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
//        val name = binding.etName.text.toString()
        val studentId = FirebaseAuth.getInstance().currentUser?.uid
//        val names = StudentModel(name,studentId)
        if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    Toast.makeText(this,"Logged In", Toast.LENGTH_SHORT).show()
//                    val databaseReference = FirebaseDatabase.getInstance().getReference("Students")
//                    databaseReference.child(studentId!!).setValue(names)
                    Config.hideDialog()
                    startActivity(Intent(this, StudentActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
                }
        }
        else{
            Config.hideDialog()
            Toast.makeText(this,"Empty fields are not permitted" ,Toast.LENGTH_SHORT).show()
        }
    }
    override fun onStart(){
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser!=null){
            startActivity(Intent(this,StudentActivity::class.java))
            finish()
        }
    }
}