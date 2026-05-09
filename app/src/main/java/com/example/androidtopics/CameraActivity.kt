package com.example.androidtopics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CameraActivity : AppCompatActivity() {

    private val TAKE_PICTURE_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val takePhotoButton: Button = findViewById(R.id.takePhotoButton)
        val photoImageView: ImageView = findViewById(R.id.photoImageView)

        takePhotoButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PICTURE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            val photoUri: Uri = data?.data ?: return
            findViewById<ImageView>(R.id.photoImageView).setImageURI(photoUri)
        }
    }
}