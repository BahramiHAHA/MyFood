package com.example.myfood.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food(
    val txtSubject: String,
    val txtPrice: String,
    val txtDistance: String,
    val txtCity: String,
    val urlImage: String,
    val numOfRating: Int,
    val rating: Float,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null

)