package com.example.realtorapp

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class Query1SoldActivity : ComponentActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query1)

        db = DatabaseHelper(this)

        val spinnerHouse = findViewById<Spinner>(R.id.spinnerHouse)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val listView = findViewById<ListView>(R.id.listView)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = "Проданные квартиры в доме"

        val houses = db.getAllHouses()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, houses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHouse.adapter = adapter

        btnSearch.setOnClickListener {
            val selectedHouse = spinnerHouse.selectedItem.toString()
            val result = db.getSoldApartments(selectedHouse)
            if (result.isEmpty()) {
                Toast.makeText(this, "Проданных квартир не найдено", Toast.LENGTH_SHORT).show()
            }
            listView.adapter = ApartmentAdapter(this, result)
        }
    }
}