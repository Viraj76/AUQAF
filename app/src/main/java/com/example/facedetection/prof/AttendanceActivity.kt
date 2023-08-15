package com.example.facedetection.prof

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facedetection.R
import com.example.facedetection.auth.ProfessorActivity
import com.example.facedetection.databinding.ActivityAttendanceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAttendanceBinding
    private lateinit var studentsAdapter: StudentsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnDone.setBackgroundResource(R.drawable.done_back)
        prepareRvForStudentAdapter()
        showingStudents()
        val numOfStudents = intent.getStringExtra("numberOfStudents")
        binding.tvPresentStudent.text = numOfStudents
//        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//        val numOfStudents = sharedPreferences.getString("user_preference","")
//        binding.tvPresentStudent.text = numOfStudents
        if(binding.tvPresentStudent.text == binding.tvQRScanning.text){
            binding.btnDone.setBackgroundResource(R.drawable.back_gradient)
        }
    }

    private fun showingStudents() {
        FirebaseDatabase.getInstance().getReference("Attendance")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val studentsList = ArrayList<StudentModel>()
                    for(names in snapshot.children){
                        val students = names.getValue(StudentModel::class.java)
                        studentsList.add(students!!)
                    }
                    binding.tvQRScanning.text = studentsList.size.toString()
                    studentsAdapter.setStudentList(studentsList)
                    if(binding.tvPresentStudent.text == binding.tvQRScanning.text){
                        binding.btnDone.setBackgroundResource(R.drawable.back_gradient)
                        binding.btnDone.setOnClickListener {
                            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())  //here dont keep date as dd/MM/yyyy ow firebase will break the date while storing
                            val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
                            FirebaseDatabase.getInstance().getReference("Attendance Data base")
                                .child("$currentDate($currentTime)").setValue(studentsList)
                                .addOnCompleteListener {
                                    val builder = AlertDialog.Builder(this@AttendanceActivity)
                                    val alertDialog = builder.create()
                                    builder
                                        .setTitle("Log Out")
                                        .setMessage("Are you sure both COUNT and SCANNED QR numbers are same?")
                                        .setPositiveButton("Yes"){dialogInterface,which->
                                            val database =FirebaseDatabase.getInstance().getReference("Attendance")
                                            database.removeValue()
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@AttendanceActivity,"Attendance Data Saved Successfully!",Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this@AttendanceActivity, ProfessorActivity::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                .addOnFailureListener {
                                                   Toast.makeText(this@AttendanceActivity,it.message.toString(), Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                        .setNegativeButton("No"){dialogInterface, which->
                                            alertDialog.dismiss()
                                        }
                                        .show()
                                        .setCancelable(false)
                                    true
//
//                                    startActivity(Intent(this@AttendanceActivity,ProfessorActivity::class.java))
//                                    finish()
                                }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun prepareRvForStudentAdapter() {
        studentsAdapter = StudentsAdapter()
        binding.rvStudents.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
            adapter= studentsAdapter
        }

    }
}