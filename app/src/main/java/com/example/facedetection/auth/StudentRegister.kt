package com.example.facedetection.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.facedetection.R
import com.example.facedetection.databinding.ActivityStudentRegisterBinding
import com.example.facedetection.prof.StudentModel
import com.example.facedetection.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentRegister : AppCompatActivity() {
    private lateinit var binding: ActivityStudentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this,StudentLogin::class.java))
            finish()
        }
        binding.btnRegister.setOnClickListener {
            registerStudent()
        }
    }

    private fun registerStudent() {
        Config.showDialog(this)
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            if(password == confirmPassword){
                val databaseReference = FirebaseDatabase.getInstance().getReference("Students")
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener{
                            val uId = task.result.user?.uid.toString()
                            val studentDetail = StudentModel(name,uId)
                            databaseReference.child(uId).setValue(studentDetail)
                            Config.hideDialog()
                            val intent = Intent(this, StudentLogin::class.java)
                            startActivity(intent)
                            finish()
                        }
                            ?.addOnFailureListener {
                                Config.hideDialog()
                                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        Config.hideDialog()
                        Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Config.hideDialog()
                Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Config.hideDialog()
            Toast.makeText(this,"Empty fields are not allowed",Toast.LENGTH_SHORT).show()
        }
    }
}