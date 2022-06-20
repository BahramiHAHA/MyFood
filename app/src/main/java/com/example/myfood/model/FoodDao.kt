package com.example.myfood.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.myfood.model.Food

@Dao
interface FoodDao {

    @Insert(onConflict = REPLACE)
    fun insertAndUpdateFood(food: Food)

    @Delete()
    fun deleteFood(food: Food)

    @Query("SELECT * FROM food_table")
    fun getAllFood(): List<Food>

    @Query("SELECT * FROM food_table WHERE txtSubject LIKE '%' || :filter || '%'")
    fun searchFood(filter: String): List<Food>


}