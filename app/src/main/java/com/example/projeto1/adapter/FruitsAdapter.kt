package com.example.projeto1.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto1.R
import com.example.projeto1.activity.FruitDetailActivity
import com.example.projeto1.activity.MainActivity
import com.example.projeto1.databinding.ItemFruitBinding
import com.example.projeto1.model.Fruit

class FruitsAdapter(private val context: Activity, var originalDataSet: ArrayList<Fruit>) : RecyclerView.Adapter<FruitsAdapter.FruiteHolder>(), Filterable {

    public var dataSet: ArrayList<Fruit> = ArrayList<Fruit>()
    public var hideDuplicated : Boolean = false
    public var orderByName : Boolean = false

    init {
        dataSet.addAll(originalDataSet)
    }

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

            binding.root.setOnClickListener {
                val fruitDetailActivity = Intent(context, FruitDetailActivity::class.java)
                fruitDetailActivity.putExtra(FruitDetailActivity.EXTRA,fruit)
                context.startActivityForResult(fruitDetailActivity, MainActivity.REMOVE_FRUIT_REQUEST_CODE);
            }
        }
    }

    fun remove(fruit :Fruit){
        originalDataSet.remove(fruit);
//        notifyDataSetChanged()
        filter.filter("");
    }

    fun add(fruit :Fruit){
        originalDataSet.add(fruit);
//        notifyDataSetChanged()
        filter.filter("");
    }

    class FruiteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemFruitBinding.bind(itemView)
    }

    fun filter(hideDuplicated :Boolean, orderByName :Boolean){
        this.hideDuplicated = hideDuplicated
        this.orderByName = orderByName
        filter.filter("");
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                dataSet.clear()
                dataSet.addAll(results.values as List<Fruit>)
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredResults: List<Fruit> = getFilteredResults()
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            private fun getFilteredResults(): List<Fruit> {
                var results = ArrayList<Fruit>()
                if (hideDuplicated) {
                    results.addAll(originalDataSet.distinct())
                }else results.addAll(originalDataSet)
                if(orderByName){
                    val sorted = results.sortedBy { it.name?.toLowerCase() }
                    results.clear()
                    results.addAll(sorted)
                }
                return results
            }
        }
    }


}