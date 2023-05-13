package com.example.facedetection.face

import android.graphics.Bitmap
import android.os.Bundle
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.lang.String

public class FaceDetectionActivity : ImageHelperActivity() {
    private lateinit var  faceDetector: FaceDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // High-accuracy landmark detection and face classification
//        val highAccuracyOpts = FaceDetectorOptions.Builder()
//            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//            .build()
        val highAccuracyOpts= FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .enableTracking()
            .build()
        faceDetector = FaceDetection.getClient(highAccuracyOpts)
    }

    override fun runDetection(bitmap: Bitmap?) {
        val finalBitmap = bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
        val image = InputImage.fromBitmap(finalBitmap, 0)
        faceDetector.process(image)
//            .addOnFailureListener(Throwable::printStackTrace())
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    getOutputTextView()!!.text = "No faces detected"
                } else {
                    getOutputTextView()!!.text = String.format("%d faces detected", faces.size)
                    val boxes= ArrayList<BoxWithText>()
                    for (face in faces) {
                        boxes.add(BoxWithText(face?.trackingId.toString(),face.boundingBox))

                    }
                    getInputImageView()!!.setImageBitmap(drawDetectionResult(finalBitmap, boxes))
                }
            }
    }



}