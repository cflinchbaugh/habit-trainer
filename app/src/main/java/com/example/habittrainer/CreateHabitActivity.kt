package com.example.habittrainer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.habittrainer.db.HabitDbTable
import java.io.IOException

class CreateHabitActivity : AppCompatActivity() {

    private val simpleNameTag = CreateHabitActivity::class.simpleName
    private var ivImage: ImageView? = null
    private var etTitle: EditText? = null
    private var etDescription: EditText? = null
    private var imageBitmap: Bitmap? = null
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

        if (result.resultCode == Activity.RESULT_OK ) {
            val bitmapData = result.data?.data
            if (bitmapData != null) {
                val bitmap = tryReadBitmap(bitmapData)

                bitmap?.let {
                    this.imageBitmap = bitmap
                    ivImage?.setImageBitmap(bitmap)
                }
            } else {
                Toast.makeText(this, "Error setting media", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, "Error retrieving media", Toast.LENGTH_LONG).show()
        }
    }

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(contentResolver, data)
        } catch (e: IOException) {
            e.printStackTrace()
            null
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

    private fun validateFields(): String {
        var errorMessage = ""

        when {
            etTitle?.text.toString().isBlank() ->
                errorMessage = "Error: Title missing"
            etDescription?.text.toString().isBlank() ->
                errorMessage = "Error: Description missing"
            imageBitmap == null ->
                errorMessage = "Error: ImageBitmap missing"
        }

        return errorMessage
    }


    fun handleClickSave(view: View) {
        val errorMessage = validateFields()

        if (errorMessage.isNotEmpty()) {
            Log.d(simpleNameTag, errorMessage)
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            errorMessageShow(errorMessage)
        } else {
            if (imageBitmap!= null) {
                val title = etTitle?.text.toString()
                val description = etDescription?.text.toString()
                val habit = Habit(title, description, imageBitmap!!)

                if (habit != null) {
                    val id = HabitDbTable(this).store(habit)

                    if (id == -1L) {
                        errorMessageShow("Save failed")
                    } else {
                        val intent = Intent(this, MainActivity::class.java)

                        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Image failed", Toast.LENGTH_SHORT).show()
                }

            }

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