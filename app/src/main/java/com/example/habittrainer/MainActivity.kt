package com.example.habittrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var tvDescription: TextView
    private lateinit var tvIcon: ImageView
    private lateinit var tvTitle: TextView

    private fun initIcon() {
        tvIcon = findViewById(R.id.tv_icon)
        tvIcon.setImageResource(R.drawable.water)
        tvIcon.contentDescription = getString(R.string.iconWater)
    }

    private fun initTitle() {
        tvTitle = findViewById(R.id.tv_title)
        tvTitle.text = getString(R.string.drinkWater)
    }

    private fun initDescription() {
        tvDescription = findViewById(R.id.tv_description)
        tvDescription.text = getString(R.string.stayHydrated)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initIcon()
        initTitle()
        initDescription()
    }


}