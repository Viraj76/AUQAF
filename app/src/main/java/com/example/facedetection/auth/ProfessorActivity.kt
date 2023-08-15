package com.example.facedetection.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.facedetection.R
import com.example.facedetection.databinding.ActivityFacultyBinding
import com.example.facedetection.face.FaceDetectionActivity
import com.example.facedetection.prof.AttendanceActivity
import com.google.firebase.auth.FirebaseAuth
class ProfessorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacultyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profToolBar.apply {
            title = "Professor"
            setTitleTextColor(ContextCompat.getColor(this@ProfessorActivity, R.color.white))
            setSupportActionBar(this)
        }
        binding.btnCountStudents.setOnClickListener {
            startActivity(Intent(this,FaceDetectionActivity::class.java))
        }
        binding.btnSeeAttendance.setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
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
                        val intent = Intent(this, ProfessorLogin::class.java)
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