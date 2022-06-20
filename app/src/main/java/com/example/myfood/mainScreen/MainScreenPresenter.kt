package com.example.myfood.mainScreen

import com.example.myfood.model.Food
import com.example.myfood.model.FoodDao
import java.util.concurrent.Executors

class MainScreenPresenter(private val foodDao: FoodDao) : MainScreenContract.Presenter {

    private var mainView: MainScreenContract.View? = null
    private val executor = Executors.newSingleThreadExecutor()

    override fun onAttach(view: MainScreenContract.View) {
        mainView = view
        executor.execute {
            mainView!!.showFoods(foodDao.getAllFood())
        }
    }

    override fun onDetach() {
        mainView = null
    }

    override fun addFood(food: Food) {
        executor.execute {
            foodDao.insertAndUpdateFood(food)
            mainView?.setAddedFood(food)
        }
    }

    override fun editFood(food: Food, position: Int) {
        foodDao.insertAndUpdateFood(food)
        mainView?.setEditedFood(food, position)
    }

    override fun deleteFood(food: Food, position: Int) {
        executor.execute {
            foodDao.deleteFood(food)
            mainView?.setDeletedFood(position)
        }
    }

    override fun searchFood(filter: String?) {
        executor.execute {
            if (filter == null || filter.isEmpty()) {
                mainView?.showFoods(foodDao.getAllFood())
            } else {
                mainView?.showFoods(foodDao.searchFood(filter))
            }
        }
    }


}