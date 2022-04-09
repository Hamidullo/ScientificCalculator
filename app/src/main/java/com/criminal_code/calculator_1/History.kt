package com.criminal_code.calculator_1

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class History : AppCompatActivity() {

    private var lv: ListView? = null
    private var dbHelper: DBHelper? = null
    private var list: ArrayList<String>? = null
    private var adapter: ArrayAdapter<String>? = null
    private var calcName = ""
    private val EmptyList = arrayOf("There is  no history yet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        lv = findViewById<ListView>(R.id.listView)
        dbHelper = DBHelper(this)
        calcName = intent.getStringExtra("calcName").toString()
        list = dbHelper!!.showHistory(calcName)

        adapter = if ((list as ArrayList<String>?)!!.isNotEmpty()) ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list!!)
        else ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, EmptyList)
        lv!!.adapter = adapter
    }

    fun onClick(v: View?) {
        dbHelper!!.deleteRecords(calcName)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, EmptyList)
        lv!!.adapter = adapter
    }

}