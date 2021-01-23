package com.example.projeto1.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto1.R
import com.example.projeto1.adapter.FruitsAdapter
import com.example.projeto1.databinding.ActivityMainBinding
import com.example.projeto1.model.Fruit

class MainActivity : AppCompatActivity() {
    companion object{
        const val ADD_FRUIT_REQUEST_CODE = 0
        const val REMOVE_FRUIT_REQUEST_CODE = 1
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter :FruitsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BitmapFactory.decodeResource(resources, R.drawable.laranja);

        val data = ArrayList<Fruit>();
        data.add(Fruit("Laranja", "A laranja é muito conhecida por ser fonte de vitamina C. A vitamina C é o nutriente mais importante da laranja. Duas laranjas por dia fornecem a quantidade de vitamina C de que o organismo precisa.", getUriFromID(R.drawable.laranja)))
        data.add(Fruit("Morango", "O morango é rico em vitaminas como, por exemplo, vitamina C, A, E, B5 e B6. Os morangos também são ricos em flavonoides, importante agente antioxidante no organismo dos seres humanos.", getUriFromID(R.drawable.morango)))
        data.add(Fruit("Abacate", "Abacate é ruim. Trata-se da pior fruta do universo, extremamente prejudicial ao ser humano. Quem come abacate deveria ser preso.", getUriFromID(R.drawable.abacate)))

        adapter = FruitsAdapter(this, data);
        binding.fruitsListView.layoutManager = LinearLayoutManager(this)
        binding.fruitsListView.adapter = adapter;

        binding.addFruit.setOnClickListener {
            val addfruitActivity = Intent(this, AddFruitActivity::class.java)
            startActivityForResult(addfruitActivity, ADD_FRUIT_REQUEST_CODE);
        }
    }

    private fun getUriFromID(resourceId :Int) :Uri{
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resourceId) + '/' + resources.getResourceTypeName(resourceId) + '/' + resources.getResourceEntryName(resourceId) );
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            val fruit = data!!.getParcelableExtra<Fruit>(FruitDetailActivity.EXTRA)
            if(requestCode==REMOVE_FRUIT_REQUEST_CODE){
                adapter.remove(fruit!!);
            }else if(requestCode==ADD_FRUIT_REQUEST_CODE){
                adapter.add(fruit!!);
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("list", adapter.dataSet)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.dataSet = savedInstanceState.getParcelableArrayList<Fruit>("list")!!
        adapter.notifyDataSetChanged()
    }
}