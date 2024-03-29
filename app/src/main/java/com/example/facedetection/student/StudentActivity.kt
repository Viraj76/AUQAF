package com.example.facedetection.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.facedetection.R
import com.example.facedetection.auth.ProfessorLogin
import com.example.facedetection.auth.StudentLogin
import com.example.facedetection.databinding.ActivityStudentBinding
import com.example.facedetection.databinding.ItemViewBinding
import com.google.firebase.auth.FirebaseAuth

class StudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.studentToolBar.apply {
            title = "Student"
            setTitleTextColor(ContextCompat.getColor(this@StudentActivity, R.color.white))
            setSupportActionBar(this)
        }
        binding.btnMarkAttendance.setOnClickListener {
            startActivity(Intent(this,QRScanningActivity::class.java))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity,menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOut -> {
                val builder = AlertDialog.Builder(this)
                val alertDialog = builder.create()
                builder
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes"){dialogInterface,which->
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, StudentLogin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No"){dialogInterface, which->
                        alertDialog.dismiss()
                    }
                    .show()
                    .setCancelable(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}