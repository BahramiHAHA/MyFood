package com.example.myfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfood.R
import com.example.myfood.databinding.ListItemRcvMainBinding
import com.example.myfood.model.Food

class AdapterRcvMain() : RecyclerView.Adapter<AdapterRcvMain.MyHolder>() {
    private var foodList = ArrayList<Food>()

    inner class MyHolder(val Binding: ListItemRcvMainBinding) :
        RecyclerView.ViewHolder(Binding.root)

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

        Glide.with(holder.Binding.root).load(food.urlImage).centerCrop()
            .placeholder(R.drawable.ic_baseline_image_24).into(holder.Binding.itemImgMain)
        holder.Binding.ItemTxtSubject.text = food.txtSubject
        holder.Binding.itemTxtCity.text = food.txtCity
        holder.Binding.itemTxtPrice.text = food.txtPrice + "$"
        holder.Binding.itemTxtDistance.text = food.txtDistance
        holder.Binding.itemRatingBar.rating = food.rating
        holder.Binding.itemTxtRate.text = food.numOfRating.toString()
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun setData(foodList: List<Food>) {
        this.foodList.clear()
        this.foodList.addAll(foodList)
        notifyDataSetChanged()
    }
}