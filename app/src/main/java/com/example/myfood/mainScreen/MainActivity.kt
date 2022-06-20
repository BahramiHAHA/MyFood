package com.example.myfood.mainScreen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfood.databinding.ActivityMainBinding
import com.example.myfood.databinding.AddFoodLayoutBinding
import com.example.myfood.databinding.DeleteFoodLayoutBinding
import com.example.myfood.databinding.EditFoodLayoutBinding
import com.example.myfood.model.AppDatabase
import com.example.myfood.model.Food
import com.example.myfood.utils.imageList

class MainActivity : AppCompatActivity(), AdapterRcvMain.AdapterRcvMainEventListener,
    MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainScreenPresenter
    private lateinit var adapter: AdapterRcvMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = MainScreenPresenter(AppDatabase.getInstance(this).foodDao())

        setupRcv()

        presenter.onAttach(this)

        binding.imgAddFood.setOnClickListener {
            addFood()
        }

        binding.edtSearch.addTextChangedListener {
            presenter.searchFood(it.toString())
        }


    }

    private fun setupRcv() {
        adapter = AdapterRcvMain(this)
        binding.rcvMain.adapter = adapter
        binding.rcvMain.layoutManager = LinearLayoutManager(this)
    }

    override fun showFoods(foodList: List<Food>) {
        runOnUiThread {
            adapter.setData(foodList)
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
                presenter.addFood(
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
                binding.rcvMain.smoothScrollToPosition(adapter.itemCount)
                addDialog.dismiss()
            } else {
                Toast.makeText(this, "fill all fields. ", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.show()
    }

    override fun setAddedFood(food: Food) {
        runOnUiThread {
            adapter.addFood(food)
        }
    }

    override fun setEditedFood(food: Food, position: Int) {
        adapter.updateFood(food, position)
    }

    override fun setDeletedFood(position: Int) {
        runOnUiThread { adapter.deleteFood(position) }
    }

    override fun onclick(food: Food, position: Int) {
        binding.edtSearch.clearFocus()
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
                presenter.editFood(
                    Food(
                        editBinding.dialogEdtNameFood.text.toString(),
                        editBinding.dialogEdtFoodPrice.text.toString(),
                        editBinding.dialogEdtFoodDistance.text.toString(),
                        editBinding.dialogEdtFoodCity.text.toString(),
                        food.urlImage,
                        food.numOfRating,
                        food.rating, food.id
                    ), position
                )
                editDialog.dismiss()
            } else {
                Toast.makeText(this, "fill all fields. ", Toast.LENGTH_SHORT).show()
            }
        }

        editBinding.dialogBtnCancel.setOnClickListener { editDialog.dismiss() }

        editDialog.show()

    }

    override fun onLongClick(food: Food, position: Int) {
        val deleteBinding = DeleteFoodLayoutBinding.inflate(layoutInflater)

        val deleteDialog =
            AlertDialog.Builder(this).setView(deleteBinding.root).setCancelable(false).create()
        deleteBinding.txtMessage.text = "do you want to delete ${food.txtSubject}"
        deleteBinding.dialogBtnDelete.setOnClickListener {
            presenter.deleteFood(food, position)
            deleteDialog.dismiss()
        }

        deleteBinding.dialogBtnCancel.setOnClickListener { deleteDialog.dismiss() }

        deleteDialog.show()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (binding.edtSearch.text.isNotEmpty()) {
            binding.edtSearch.setText("")
        } else {
            super.onBackPressed()
        }
    }
}