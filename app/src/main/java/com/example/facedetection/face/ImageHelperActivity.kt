package com.example.facedetection.face

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.example.facedetection.R
import com.example.facedetection.databinding.ActivityImageHelperBinding
import com.example.facedetection.auth.ProfessorActivity
import com.example.facedetection.prof.AttendanceActivity
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList

abstract class ImageHelperActivity : AppCompatActivity() {
    val LOG_TAG = "MLImageHelper"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val PICK_IMAGE_ACTIVITY_REQUEST_CODE = 1064
    val REQUEST_READ_EXTERNAL_STORAGE = 2031
    var photoFile: File? = null
    private lateinit var binding : ActivityImageHelperBinding
    private var inputImageView: ImageView? = null
    private var outputTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageHelperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTakePhoto.setOnClickListener {
            val intent = Intent(this, AttendanceActivity::class.java)
            intent.putExtra("numberOfStudents",binding.textView.text.toString())

//            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//            val editor = sharedPref.edit()
//            editor.putString("user_preference",outputTextView.toString())
//            editor.apply()

            startActivity(intent)
            finish()

        }

        inputImageView = findViewById(R.id.imageView)
        outputTextView = findViewById(R.id.textView)

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }
//    fun onTakeImage(view: View?) {
//        // create Intent to take a picture and return control to the calling application
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        // Create a File reference for future access
//        photoFile = getPhotoFileUri(SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpg")
//
//        // wrap File object into a content provider
//        // required for API >= 24
//        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//        val fileProvider = FileProvider.getUriForFile(
//            this, "com.iago.fileprovider1",
//            photoFile!!
//        )
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
//
//        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
//        // So as long as the result is not null, it's safe to use the intent.
//        if (intent.resolveActivity(packageManager) != null) {
//            // Start the image capture intent to take photo
//            startActivityForResult(
//                intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
//            )
//        }
//    }
    fun getPhotoFileUri(fileName: String): File? {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), LOG_TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(LOG_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    private fun getCapturedImage(): Bitmap? {
        // Get the dimensions of the View
        val targetW = inputImageView!!.width
        val targetH = inputImageView!!.height
        var bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoFile!!.absolutePath)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight
        val scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH))
        bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inMutable = true
        return BitmapFactory.decodeFile(photoFile!!.absolutePath, bmOptions)
    }
    private fun rotateIfRequired(bitmap: Bitmap) {
        try {
            val exifInterface = ExifInterface(
                photoFile!!.absolutePath
            )
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotateImage(bitmap, 90f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotateImage(bitmap, 180f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotateImage(bitmap, 270f)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
    fun onPickImage(view: View?) {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(
                intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE
            )
        }
    }
    protected fun getOutputTextView(): TextView? {
        return outputTextView
    }

    protected fun getInputImageView(): ImageView? {
        return inputImageView
    }

    protected abstract fun runDetection(bitmap: Bitmap?)

    protected open fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > 27) {
                val source = ImageDecoder.createSource(
                    this.contentResolver,
                    photoUri!!
                )
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val bitmap = getCapturedImage()
                rotateIfRequired(bitmap!!)
                inputImageView!!.setImageBitmap(bitmap)
                runDetection(bitmap)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val takenImage = loadFromUri(data?.data)
                inputImageView!!.setImageBitmap(takenImage)
                runDetection(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't selected!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected open fun drawDetectionResult(
        bitmap: Bitmap,
        detectionResults: ArrayList<BoxWithText>
    ): Bitmap? {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT
        if (detectionResults != null) {
            for (box in detectionResults) {
                // draw bounding box
                pen.color = Color.RED
                pen.strokeWidth = 8f
                pen.style = Paint.Style.STROKE
                canvas.drawRect(box!!.rect, pen)
                val tagSize = Rect(0, 0, 0, 0)

                // calculate the right font size
                pen.style = Paint.Style.FILL_AND_STROKE
                pen.color = Color.YELLOW
                pen.strokeWidth = 2f
                pen.textSize = 96f
//                pen.getTextBounds(box.text, 0, box.text.length, tagSize)
                val fontSize = pen.textSize * box.rect.width() / tagSize.width()

                // adjust the font size so texts are inside the bounding box
                if (fontSize < pen.textSize) {
                    pen.textSize = fontSize
                }
                var margin = (box.rect.width() - tagSize.width()) / 2.0f
                if (margin < 0f) margin = 0f
//                canvas.drawText(
//                    box.text, box.rect.left + margin,
//                    (box.rect.top + tagSize.height()).toFloat(), pen
//                )
            }
        }
        return outputBitmap
    }

}