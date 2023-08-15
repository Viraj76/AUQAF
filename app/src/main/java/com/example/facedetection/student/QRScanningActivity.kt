package com.example.facedetection.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.facedetection.R
import com.example.facedetection.databinding.ActivityQrscanningBinding
import com.example.facedetection.prof.StudentModel
import com.example.facedetection.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

class QRScanningActivity : AppCompatActivity(),ResultHandler {
    private lateinit var binding: ActivityQrscanningBinding
    private lateinit var scannerView : ZXingScannerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scannerView = ZXingScannerView(this)
        initializeQRScanner()
    }

    private fun initializeQRScanner() {
        scannerView.apply {
            setBackgroundColor(ContextCompat.getColor(this@QRScanningActivity,R.color.colorTranslucent))
            setBorderColor(ContextCompat.getColor(this@QRScanningActivity,R.color.blue))
            setLaserColor(ContextCompat.getColor(this@QRScanningActivity,R.color.blue))
            setBorderStrokeWidth(10)
            setAutoFocus(true)
            setSquareViewFinder(true)
            setResultHandler(this@QRScanningActivity)
            binding.containerScanner.addView(scannerView)
            startQRCamera()
        }
    }

    private fun startQRCamera() {
        scannerView.startCamera()
    }
    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }
    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }
    override fun onDestroy() {
        super.onDestroy()
        scannerView.stopCamera()
    }
    override fun handleResult(rawResult: Result?) {
//        Config.showDialog(this@QRScanningActivity)
        val result = rawResult.toString()
//        Toast.makeText(this@QRScanningActivity, result.toString(), Toast.LENGTH_SHORT).show()
        FirebaseDatabase.getInstance().getReference("Students")
            .addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("pp","hey")
                    for(ids in snapshot.children){
                        val currentStudentId = FirebaseAuth.getInstance().currentUser?.uid
                        val studentNames = ids.getValue(StudentModel::class.java)
                        if(currentStudentId == studentNames?.studentId){
//                            val studentNames = ids.getValue(StudentModel::class.java)
                            if(result.contains(studentNames?.studentName.toString())) {
//                                scannerView.resumeCameraPreview(this@QRScanningActivity)
                                FirebaseDatabase.getInstance().getReference("Attendance")
                                    .child(currentStudentId!!).setValue(studentNames)
                                val builder = AlertDialog.Builder(this@QRScanningActivity)
                                val customLayout = LayoutInflater.from(this@QRScanningActivity).inflate(R.layout.dialog, null)
                                builder.setView(customLayout)
                                builder.setCancelable(false) // prevent dialog from being dismissed on outside touch or back button press
                                val alertDialog = builder.create()
                                val okButton = customLayout.findViewById<Button>(R.id.Done) // replace "ok_button" with the ID of your button
                                okButton.setOnClickListener {
                                    startActivity(Intent(this@QRScanningActivity,StudentActivity::class.java))
                                    finish()
                                    alertDialog.dismiss() // dismiss the dialog when the button is clicked
                                }
//                                Config.hideDialog()
                                alertDialog.show()
                            }
                            else {
                                startActivity(Intent(this@QRScanningActivity,StudentActivity::class.java))
                                finish()
                                Toast.makeText(this@QRScanningActivity,"Invalid User", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
//        Toast.makeText(this,rawResult.toString(), Toast.LENGTH_SHORT).show()

    }
}