package com.example.myfood.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfood.R
import com.example.myfood.databinding.ListItemRcvMainBinding
import com.example.myfood.model.Food

class AdapterRcvMain(val eventListener: AdapterRcvMainEventListener) :
    RecyclerView.Adapter<AdapterRcvMain.MyHolder>() {
    private var foodList = ArrayList<Food>()

    inner class MyHolder(val binding: ListItemRcvMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                eventListener.onclick(
                    foodList[adapterPosition],
                    adapterPosition
                )
            }
            binding.root.setOnLongClickListener {
                eventListener.onLongClick(
                    foodList[adapterPosition],
                    adapterPosition
                )
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ListItemRcvMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val food = foodList[position]

        Glide.with(holder.binding.root).load(food.urlImage).centerCrop()
            .placeholder(R.drawable.ic_baseline_image_24).into(holder.binding.itemImgMain)
        holder.binding.ItemTxtSubject.text = food.txtSubject
        holder.binding.itemTxtCity.text = food.txtCity
        holder.binding.itemTxtPrice.text = food.txtPrice + "$"
        holder.binding.itemTxtDistance.text = food.txtDistance + " miles from you"
        holder.binding.itemRatingBar.rating = food.rating
        holder.binding.itemTxtRate.text = "( ${food.numOfRating} Ratings )"
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun setData(foodList: List<Food>) {
        this.foodList.clear()
        this.foodList.addAll(foodList)
        notifyDataSetChanged()
    }

    fun addFood(food: Food) {
        foodList.add(food)
        notifyItemInserted(foodList.size - 1)
    }

    fun deleteFood(position: Int) {
        foodList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateFood(food: Food, position: Int) {
        foodList[position] = food
        notifyItemChanged(position)
    }

    interface AdapterRcvMainEventListener {
        fun onclick(food: Food, position: Int)
        fun onLongClick(food: Food, position: Int)
    }
}