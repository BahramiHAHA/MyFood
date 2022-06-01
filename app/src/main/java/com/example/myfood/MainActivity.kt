package com.example.myfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfood.adapter.AdapterRcvMain
import com.example.myfood.database.AppDatabase
import com.example.myfood.databinding.ActivityMainBinding
import com.example.myfood.databinding.AddFoodLayoutBinding
import com.example.myfood.databinding.DeleteFoodLayoutBinding
import com.example.myfood.databinding.EditFoodLayoutBinding
import com.example.myfood.model.Food
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), AdapterRcvMain.AdapterRcvMainEventListener {
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
        binding.edtSearch.addTextChangedListener {
            getFoods(it.toString())
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
        adapter = AdapterRcvMain(this)
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
            executor.execute {
                val foodList = database.foodDao().searchFood(filter)
                runOnUiThread {
                    adapter.setData(foodList)
                }
            }
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
                binding.rcvMain.smoothScrollToPosition(adapter.itemCount)
                addDialog.dismiss()
            } else {
                Toast.makeText(this, "fill all fields. ", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.show()
    }

    private fun editFood(food: Food, position: Int) {
        val editBinding = EditFoodLayoutBinding.inflate(layoutInflater)
        editBinding.dialogEdtNameFood.setText(food.txtSubject)
        editBinding.dialogEdtFoodPrice.setText(food.txtPrice)
        editBinding.dialogEdtFoodDistance.setText(food.txtDistance)
        editBinding.dialogEdtFoodCity.setText(food.txtCity)
        val editDialog =
            AlertDialog.Builder(this).setView(editBinding.root).setCancelable(false).create()
        editBinding.dialogBtnEdit.setOnClickListener {
            if (editBinding.dialogEdtNameFood.length() > 0 &&
                editBinding.dialogEdtFoodCity.length() > 0 &&
                editBinding.dialogEdtFoodPrice.length() > 0 &&
                editBinding.dialogEdtFoodDistance.length() > 0
            ) {
                executor.execute {
                    database.foodDao().insertAndUpdateFood(
                        Food(
                            editBinding.dialogEdtNameFood.text.toString(),
                            editBinding.dialogEdtFoodPrice.text.toString(),
                            editBinding.dialogEdtFoodDistance.text.toString(),
                            editBinding.dialogEdtFoodCity.text.toString(),
                            food.urlImage,
                            food.numOfRating,
                            food.rating, food.id
                        )
                    )
                    runOnUiThread {
                        getFoods(null)
                    }
                }
                editDialog.dismiss()
            } else {
                Toast.makeText(this, "fill all fields. ", Toast.LENGTH_SHORT).show()
            }
        }
        editBinding.dialogBtnCancel.setOnClickListener { editDialog.dismiss() }
        editDialog.show()
    }

    override fun onclick(food: Food, position: Int) {
        editFood(food, position)
    }

    override fun onLongClick(food: Food, position: Int) {
        val deleteBinding = DeleteFoodLayoutBinding.inflate(layoutInflater)
        val deleteDialog =
            AlertDialog.Builder(this).setView(deleteBinding.root).setCancelable(false).create()
        deleteBinding.txtMessage.text = "do you want to delete ${food.txtSubject}"
        deleteBinding.dialogBtnDelete.setOnClickListener {
            deleteFood(food, position)
            deleteDialog.dismiss()
        }
        deleteBinding.dialogBtnCancel.setOnClickListener { deleteDialog.dismiss() }
        deleteDialog.show()
    }

    private fun deleteFood(food: Food, position: Int) {
        executor.execute {
            database.foodDao().deleteFood(food)
            runOnUiThread { adapter.deleteFood(food, position) }
        }
    }
}