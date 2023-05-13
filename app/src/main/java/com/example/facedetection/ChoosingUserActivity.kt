package com.example.facedetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.facedetection.databinding.ActivityChoosingUserBinding

class ChoosingUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChoosingUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnProfessor.setOnClickListener {  }
            btnStudent.setOnClickListener {  }
        }

    }
}