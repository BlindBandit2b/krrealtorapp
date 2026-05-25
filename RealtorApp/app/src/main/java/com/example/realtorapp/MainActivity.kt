package com.example.realtorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnQuery1).setOnClickListener {
            startActivity(Intent(this, Query1SoldActivity::class.java))
        }
        findViewById<Button>(R.id.btnQuery2).setOnClickListener {
            startActivity(Intent(this, Query2FreeActivity::class.java))
        }
        findViewById<Button>(R.id.btnQuery3).setOnClickListener {
            startActivity(Intent(this, Query3FreeLimitedActivity::class.java))
        }
    }
}