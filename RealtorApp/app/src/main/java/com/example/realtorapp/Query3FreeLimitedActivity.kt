package com.example.realtorapp

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class Query3FreeLimitedActivity : ComponentActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query3)

        db = DatabaseHelper(this)

        val spinnerHouse = findViewById<Spinner>(R.id.spinnerHouse)
        val etMaxArea = findViewById<EditText>(R.id.etMaxArea)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val listView = findViewById<ListView>(R.id.listView)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = "Свободные квартиры в доме (площадь ≤ + раздельный санузел)"

        val houses = db.getAllHouses()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, houses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHouse.adapter = adapter

        btnSearch.setOnClickListener {
            val selectedHouse = spinnerHouse.selectedItem.toString()
            val maxAreaStr = etMaxArea.text.toString()

            if (maxAreaStr.isEmpty()) {
                Toast.makeText(this, "Введите максимальную площадь", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val maxArea = maxAreaStr.toDoubleOrNull() ?: 0.0
            val result = db.getFreeApartmentsLimited(selectedHouse, maxArea)
            if (result.isEmpty()) {
                Toast.makeText(this, "Квартир не найдено", Toast.LENGTH_SHORT).show()
            }
            listView.adapter = ApartmentAdapter(this, result)
        }
    }
}