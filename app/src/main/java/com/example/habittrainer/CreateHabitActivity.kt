package com.example.habittrainer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class CreateHabitActivity : AppCompatActivity() {

    private val simpleNameTag = CreateHabitActivity::class.simpleName
    private var ivImage: ImageView? = null
    private var etTitle: EditText? = null
    private var etDescription: EditText? = null
    private var imageUri: android.net.Uri? = null
    private var tvError: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_habit)

        ivImage = findViewById(R.id.add_image_preview)
        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        tvError = findViewById(R.id.habit_error)
    }

    private val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->

        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data

            if (ivImage != null && imageUri != null) {
                ivImage?.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Error setting media", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, "Error retrieving media", Toast.LENGTH_LONG).show()
        }
    }

    fun handleClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        if (intent.resolveActivity(packageManager) != null) {
            getAction.launch(intent)
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

        Log.d(simpleNameTag, "Intent to choose image sent...")
    }

    fun handleClickSave(view: View) {
        var errorMessage: String? = null

        when {
            etTitle?.text.toString().isBlank() ->
                errorMessage = "Error: Title missing"
            etDescription?.text.toString().isBlank() ->
                errorMessage = "Error: Description missing"
            imageUri == null ->
                errorMessage = "Error: Image missing"
        }

        if (errorMessage != null) {
            Log.d(simpleNameTag, errorMessage)
            errorMessageShow(errorMessage)
        } else {
            // TODO: Save the data
            Toast.makeText(this, "No Error", Toast.LENGTH_SHORT).show()
        }
    }

    // Toast is preferable here because the error itself is only relevant until the user makes changes,
    // leaving this function in place until the tutorial is complete just in case it has a greater purpose
    private fun errorMessageShow(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        tvError?.text = errorMessage
        tvError?.visibility = View.VISIBLE
    }

}