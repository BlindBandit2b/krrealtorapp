package com.example.realtorapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ApartmentAdapter(
    private val context: Context,
    private val apartments: List<ApartmentInfo>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount() = apartments.size
    override fun getItem(position: Int) = apartments[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.list_item_apartment, parent, false)
        val item = apartments[position]

        // Заполнение данных
        view.findViewById<TextView>(R.id.tvNumber).text = "№${item.apartmentNumber}"
        view.findViewById<TextView>(R.id.tvFloor).text = "${item.floor} эт."
        view.findViewById<TextView>(R.id.tvArea).text = "${item.area} м²"
        view.findViewById<TextView>(R.id.tvRooms).text = "${item.numRooms} комн."
        view.findViewById<TextView>(R.id.tvBathroom).text = item.bathroomType
        view.findViewById<TextView>(R.id.tvStatus).text = item.status

        // Иконки
        val imgStatus = view.findViewById<ImageView>(R.id.imgStatus)
        val imgBath = view.findViewById<ImageView>(R.id.imgBath)
        imgStatus.setImageResource(if (item.status == "свободна") R.drawable.ic_free else R.drawable.ic_sold)
        imgBath.setImageResource(if (item.bathroomType == "раздельный") R.drawable.ic_bath_separate else R.drawable.ic_bath_combined)

        // Кнопка календаря
        view.findViewById<Button>(R.id.btnAddToCalendar).setOnClickListener {
            CalendarHelper.addViewingToCalendar(context, item)
        }

        return view
    }
}