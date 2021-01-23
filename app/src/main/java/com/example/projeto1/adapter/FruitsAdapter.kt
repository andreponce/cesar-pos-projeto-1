package com.example.projeto1.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto1.R
import com.example.projeto1.activity.FruitDetailActivity
import com.example.projeto1.activity.MainActivity
import com.example.projeto1.databinding.ItemFruitBinding
import com.example.projeto1.model.Fruit

class FruitsAdapter(private val context: Activity, var dataSet: ArrayList<Fruit>) : RecyclerView.Adapter<FruitsAdapter.FruiteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruiteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fruit, parent, false);
        return FruiteHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    override fun onBindViewHolder(holder: FruiteHolder, position: Int) {
        val fruit = dataSet.get(position)
        with(holder) {
            binding.image.setImageURI(fruit.imageURI)
            binding.nameTxt.text = fruit.name
            binding.descriptionTxt.text = fruit.description

            binding.image.setOnClickListener {
                val fruitDetailActivity = Intent(context, FruitDetailActivity::class.java)
                fruitDetailActivity.putExtra(FruitDetailActivity.EXTRA,fruit)
                context.startActivityForResult(fruitDetailActivity, MainActivity.REMOVE_FRUIT_REQUEST_CODE);
            }
        }
    }

    fun remove(fruit :Fruit){
        dataSet.remove(fruit);
        notifyDataSetChanged()
    }

    fun add(fruit :Fruit){
        dataSet.add(fruit);
        notifyDataSetChanged()
    }

    class FruiteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemFruitBinding.bind(itemView)
    }
}