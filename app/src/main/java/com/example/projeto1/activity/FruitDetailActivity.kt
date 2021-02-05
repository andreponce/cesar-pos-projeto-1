package com.example.projeto1.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto1.R
import com.example.projeto1.databinding.ActivityFuitDetailBinding
import com.example.projeto1.model.Fruit

class FruitDetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA = "fruit";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFuitDetailBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar.toolbar)
        setContentView(binding.root)

        val fruit = intent.getParcelableExtra<Fruit>(EXTRA)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = fruit.name

        binding.nameTxt.text = fruit.name;
        binding.descriptionTxt.text = fruit.description
        binding.image.setImageURI(fruit.imageURI)

        binding.removeBt.setOnClickListener {
            val intent = Intent()
            intent.putExtra(EXTRA, fruit)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}