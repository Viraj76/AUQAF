package com.example.facedetection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.facedetection.auth.ProfessorLogin
import com.example.facedetection.auth.StudentLogin
import com.example.facedetection.databinding.ActivityChoosingUserBinding

class ChoosingUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChoosingUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnProfessor.setOnClickListener {
                btnProfessor.setBackgroundResource(R.drawable.btn_back)
                startActivity(Intent(this@ChoosingUserActivity,ProfessorLogin::class.java))
                finish()
            }
            btnStudent.setOnClickListener {
                btnStudent.setBackgroundResource(R.drawable.btn_back)
                startActivity(Intent(this@ChoosingUserActivity,StudentLogin::class.java))
                finish()
            }
        }

    }
}