package com.example.myfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfood.adapter.AdapterRcvMain
import com.example.myfood.database.AppDatabase
import com.example.myfood.databinding.ActivityMainBinding
import com.example.myfood.databinding.AddFoodLayoutBinding
import com.example.myfood.model.Food
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageList: ArrayList<String>
    private lateinit var database: AppDatabase
    private lateinit var adapter: AdapterRcvMain
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = AppDatabase.getInstance(this)
        setupRcv()
        binding.imgAddFood.setOnClickListener {
            addFood()
        }
        imageList = arrayListOf(
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
            "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
        )
    }

    private fun setupRcv() {
        adapter = AdapterRcvMain()
        binding.rcvMain.adapter = adapter
        binding.rcvMain.layoutManager = LinearLayoutManager(this)
        getFoods(null)
    }

    private fun getFoods(filter: String?) {
        if (filter == null || filter.isEmpty()) {
            executor.execute {
                val foodList = database.foodDao().getAllFood()
                runOnUiThread {
                    adapter.setData(foodList)
                }
            }
        } else {

        }
    }

    private fun addFood() {
        val addBinding = AddFoodLayoutBinding.inflate(layoutInflater)
        val addDialog =
            AlertDialog.Builder(this).setView(addBinding.root).setCancelable(true).create()
        addBinding.dialogBtnDone.setOnClickListener {
            if (addBinding.dialogEdtNameFood.length() > 0 &&
                addBinding.dialogEdtFoodCity.length() > 0 &&
                addBinding.dialogEdtFoodPrice.length() > 0 &&
                addBinding.dialogEdtFoodDistance.length() > 0
            ) {
                executor.execute {
                    database.foodDao().insertAndUpdateFood(
                        Food(
                            addBinding.dialogEdtNameFood.text.toString(),
                            addBinding.dialogEdtFoodPrice.text.toString(),
                            addBinding.dialogEdtFoodDistance.text.toString(),
                            addBinding.dialogEdtFoodCity.text.toString(),
                            imageList.random(),
                            (1..1000).random(),
                            (1..5).random().toFloat()
                        )
                    )
                }
                getFoods(null)
                addDialog.dismiss()
            } else {
                Toast.makeText(this, "fill all fields. ", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.show()
    }
}