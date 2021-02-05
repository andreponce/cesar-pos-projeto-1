package com.example.projeto1.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var filterDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar.toolbar)
        setContentView(binding.root)
        setupFilterDialog();

        BitmapFactory.decodeResource(resources, R.drawable.laranja);

        val data = ArrayList<Fruit>();
        data.add(Fruit("Laranja", "A laranja é muito conhecida por ser fonte de vitamina C. A vitamina C é o nutriente mais importante da laranja. Duas laranjas por dia fornecem a quantidade de vitamina C de que o organismo precisa.", getUriFromID(R.drawable.laranja)))
        data.add(Fruit("Laranja", "A laranja é muito conhecida por ser fonte de vitamina C. A vitamina C é o nutriente mais importante da laranja. Duas laranjas por dia fornecem a quantidade de vitamina C de que o organismo precisa.", getUriFromID(R.drawable.laranja)))
        data.add(Fruit("Morango", "O morango é rico em vitaminas como, por exemplo, vitamina C, A, E, B5 e B6. Os morangos também são ricos em flavonoides, importante agente antioxidante no organismo dos seres humanos.", getUriFromID(R.drawable.morango)))
        data.add(Fruit("Morango", "O morango é rico em vitaminas como, por exemplo, vitamina C, A, E, B5 e B6. Os morangos também são ricos em flavonoides, importante agente antioxidante no organismo dos seres humanos.", getUriFromID(R.drawable.morango)))
        data.add(Fruit("Abacate", "Abacate é ruim. Trata-se da pior fruta do universo, extremamente prejudicial ao ser humano. Quem come abacate deveria ser preso.", getUriFromID(R.drawable.abacate)))
        data.add(Fruit("Abacate", "Abacate é ruim. Trata-se da pior fruta do universo, extremamente prejudicial ao ser humano. Quem come abacate deveria ser preso.", getUriFromID(R.drawable.abacate)))
        data.add(Fruit("Banana", "Lorem ipsum dolor site amet lorem ipsum dolor site amet lorem ipsum dolor site amet lorem ipsum dolor site amet lorem.", getUriFromID(R.drawable.banana)))
        data.add(Fruit("Mamão", "Lorem ipsum dolor site amet lorem ipsum dolor site amet lorem ipsum dolor site amet lorem ipsum dolor site amet lorem.", getUriFromID(R.drawable.papaya)))
        data.add(Fruit("Pitaia", "Lorem ipsum dolor site amet lorem ipsum dolor site amet lorem ipsum dolor site amet lorem ipsum dolor site amet lorem.", getUriFromID(R.drawable.pitaia)))

        adapter = FruitsAdapter(this, data);
        binding.fruitsListView.layoutManager = LinearLayoutManager(this)
        binding.fruitsListView.adapter = adapter;

        binding.addFruit.setOnClickListener {
            val addfruitActivity = Intent(this, AddFruitActivity::class.java)
            startActivityForResult(addfruitActivity, ADD_FRUIT_REQUEST_CODE);
        }
    }

    private fun setupFilterDialog(){
        val view = layoutInflater.inflate(R.layout.filter_dialog, null)
        val switchDuplicated = view.findViewById<Switch>(R.id.duplicated)
        val switchOrder = view.findViewById<Switch>(R.id.order)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
        builder.apply {
            setView(view)
            setTitle("Filtrar")
            setCancelable(true)
        }
        filterDialog = builder.create()
        filterDialog.setOnShowListener {
            switchDuplicated.isChecked = adapter.hideDuplicated
            switchOrder.isChecked = adapter.orderByName
        }
        view.findViewById<Button>(R.id.ok).setOnClickListener { view ->
            adapter.filter(switchDuplicated.isChecked, switchOrder.isChecked)
            filterDialog.dismiss()
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
        outState.putParcelableArrayList("originalDataSet", adapter.originalDataSet)
        outState.putParcelableArrayList("dataSet", adapter.dataSet)
        outState.putBoolean("hideDuplicated", adapter.hideDuplicated)
        outState.putBoolean("orderByName", adapter.orderByName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.originalDataSet = savedInstanceState.getParcelableArrayList<Fruit>("originalDataSet")!!
        adapter.dataSet = savedInstanceState.getParcelableArrayList<Fruit>("dataSet")!!
        adapter.hideDuplicated = savedInstanceState.getBoolean("hideDuplicated")!!
        adapter.orderByName = savedInstanceState.getBoolean("orderByName")!!

        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter -> {
            filterDialog.show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}