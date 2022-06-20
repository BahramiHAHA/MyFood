package com.example.myfood.mainScreen

import com.example.myfood.model.Food

interface MainScreenContract {
    interface Presenter {
        fun onAttach(view: View)
        fun onDetach()
        fun addFood(food: Food)
        fun editFood(food: Food, position: Int)
        fun deleteFood(food: Food, position: Int)
        fun searchFood(filter: String?)
    }

    interface View {
        fun showFoods(foodList: List<Food>)
        fun setAddedFood(food: Food)
        fun setEditedFood(food: Food, position: Int)
        fun setDeletedFood(position: Int)
    }
}