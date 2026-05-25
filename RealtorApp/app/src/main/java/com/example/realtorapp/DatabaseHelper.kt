package com.example.realtorapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "realtor.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE houses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                address TEXT NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE layout_types (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                code TEXT,
                area REAL,
                num_rooms INTEGER,
                bathroom_type TEXT
            )
        """)
        db.execSQL("""
            CREATE TABLE apartments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                code TEXT,
                apartment_number INTEGER,
                house_id INTEGER,
                floor INTEGER,
                layout_type_id INTEGER,
                status TEXT,
                FOREIGN KEY (house_id) REFERENCES houses(id),
                FOREIGN KEY (layout_type_id) REFERENCES layout_types(id)
            )
        """)

        // === ТЕСТОВЫЕ ДАННЫЕ (можно менять под свой вариант) ===
        // Дома
        db.execSQL("INSERT INTO houses (address) VALUES ('ул. Пушкина, д. 15')")
        db.execSQL("INSERT INTO houses (address) VALUES ('пр. Гагарина, д. 42')")

        // Типы планировок
        db.execSQL("INSERT INTO layout_types (code, area, num_rooms, bathroom_type) VALUES ('П1', 42.5, 2, 'раздельный')")
        db.execSQL("INSERT INTO layout_types (code, area, num_rooms, bathroom_type) VALUES ('П2', 58.0, 3, 'совмещенный')")
        db.execSQL("INSERT INTO layout_types (code, area, num_rooms, bathroom_type) VALUES ('П3', 35.0, 1, 'раздельный')")
        db.execSQL("INSERT INTO layout_types (code, area, num_rooms, bathroom_type) VALUES ('П4', 72.5, 3, 'раздельный')")

        // Квартиры (дом 1)
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К101', 101, 1, 2, 1, 'свободна')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К102', 102, 1, 2, 2, 'продана')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К103', 103, 1, 3, 1, 'свободна')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К104', 104, 1, 3, 3, 'продана')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К105', 105, 1, 4, 4, 'свободна')")

        // Квартиры (дом 2)
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К201', 201, 2, 2, 1, 'свободна')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К202', 202, 2, 3, 2, 'свободна')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К203', 203, 2, 3, 4, 'продана')")
        db.execSQL("INSERT INTO apartments (code, apartment_number, house_id, floor, layout_type_id, status) VALUES ('К204', 204, 2, 2, 3, 'продана')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS apartments")
        db.execSQL("DROP TABLE IF EXISTS layout_types")
        db.execSQL("DROP TABLE IF EXISTS houses")
        onCreate(db)
    }

    // === ЗАПРОСЫ ===
    fun getSoldApartments(houseAddress: String): List<ApartmentInfo> {
        val list = mutableListOf<ApartmentInfo>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
            SELECT a.apartment_number, a.floor, l.area, l.num_rooms, l.bathroom_type, a.status, h.address
            FROM apartments a
            JOIN layout_types l ON a.layout_type_id = l.id
            JOIN houses h ON a.house_id = h.id
            WHERE h.address = ? AND a.status = 'продана'
        """, arrayOf(houseAddress))

        while (cursor.moveToNext()) {
            list.add(ApartmentInfo(
                apartmentNumber = cursor.getInt(0),
                floor = cursor.getInt(1),
                area = cursor.getDouble(2),
                numRooms = cursor.getInt(3),
                bathroomType = cursor.getString(4),
                status = cursor.getString(5),
                houseAddress = cursor.getString(6)
            ))
        }
        cursor.close()
        return list
    }

    fun getFreeApartmentsByAreaAndRooms(area: Double, rooms: Int): List<ApartmentInfo> {
        val list = mutableListOf<ApartmentInfo>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
            SELECT a.apartment_number, a.floor, l.area, l.num_rooms, l.bathroom_type, a.status, h.address
            FROM apartments a
            JOIN layout_types l ON a.layout_type_id = l.id
            JOIN houses h ON a.house_id = h.id
            WHERE a.status = 'свободна' 
              AND a.floor IN (2, 3) 
              AND l.area >= ? 
              AND l.num_rooms = ?
        """, arrayOf(area.toString(), rooms.toString()))

        while (cursor.moveToNext()) {
            list.add(ApartmentInfo(
                apartmentNumber = cursor.getInt(0),
                floor = cursor.getInt(1),
                area = cursor.getDouble(2),
                numRooms = cursor.getInt(3),
                bathroomType = cursor.getString(4),
                status = cursor.getString(5),
                houseAddress = cursor.getString(6)
            ))
        }
        cursor.close()
        return list
    }

    fun getFreeApartmentsLimited(houseAddress: String, maxArea: Double): List<ApartmentInfo> {
        val list = mutableListOf<ApartmentInfo>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
            SELECT a.apartment_number, a.floor, l.area, l.num_rooms, l.bathroom_type, a.status, h.address
            FROM apartments a
            JOIN layout_types l ON a.layout_type_id = l.id
            JOIN houses h ON a.house_id = h.id
            WHERE h.address = ? 
              AND a.status = 'свободна' 
              AND l.area <= ? 
              AND l.bathroom_type = 'раздельный'
        """, arrayOf(houseAddress, maxArea.toString()))

        while (cursor.moveToNext()) {
            list.add(ApartmentInfo(
                apartmentNumber = cursor.getInt(0),
                floor = cursor.getInt(1),
                area = cursor.getDouble(2),
                numRooms = cursor.getInt(3),
                bathroomType = cursor.getString(4),
                status = cursor.getString(5),
                houseAddress = cursor.getString(6)
            ))
        }
        cursor.close()
        return list
    }

    fun getAllHouses(): List<String> {
        val list = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT address FROM houses", null)
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0))
        }
        cursor.close()
        return list
    }
}