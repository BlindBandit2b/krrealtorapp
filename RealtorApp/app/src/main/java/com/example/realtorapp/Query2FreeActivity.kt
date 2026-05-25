package com.example.realtorapp

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class Query2FreeActivity : ComponentActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query2)

        db = DatabaseHelper(this)

        val etArea = findViewById<EditText>(R.id.etArea)
        val etRooms = findViewById<EditText>(R.id.etRooms)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val listView = findViewById<ListView>(R.id.listView)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = "Свободные квартиры (площадь + комнаты, этажи 2-3)"

        btnSearch.setOnClickListener {
            val areaStr = etArea.text.toString()
            val roomsStr = etRooms.text.toString()

            if (areaStr.isEmpty() || roomsStr.isEmpty()) {
                Toast.makeText(this, "Введите площадь и количество комнат", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val area = areaStr.toDoubleOrNull() ?: 0.0
            val rooms = roomsStr.toIntOrNull() ?: 0

            val result = db.getFreeApartmentsByAreaAndRooms(area, rooms)
            if (result.isEmpty()) {
                Toast.makeText(this, "Квартир не найдено", Toast.LENGTH_SHORT).show()
            }
            listView.adapter = ApartmentAdapter(this, result)
        }
    }
}