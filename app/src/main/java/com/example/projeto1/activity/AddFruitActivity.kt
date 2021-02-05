package com.example.projeto1.activity

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto1.databinding.ActivityAddFruitBinding
import com.example.projeto1.model.Fruit
import java.net.URLDecoder


class AddFruitActivity : AppCompatActivity() {
    companion object{
        const val GALLERY_IMAGE_REQUEST_CODE = 0
        const val PERMISSION_CODE_READ = 1
    }

    private var imageUri: Uri? = null
    private lateinit var binding: ActivityAddFruitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFruitBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar.toolbar)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adicionar Fruta"

        binding.selectBt.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE_READ)
                } else pickImage()
            }
        }
        binding.addBt.setOnClickListener {
            val name = binding.nameTxt.text.toString()
            val description = binding.descriptionTxt.text.toString()
            var erroMsg : String? = null;

            if(name.isNullOrEmpty()) erroMsg = "Digite um nome para a fruta.";
            else if(description.isNullOrEmpty()) erroMsg = "Digite uma descrição para a fruta.";
            else if(imageUri == null) erroMsg = "Selecione uma imagem para a fruta.";

            if(erroMsg != null) {
                Toast.makeText(this, erroMsg, Toast.LENGTH_LONG).show()
            }else {
                val fruit = Fruit(name, description, imageUri!!)
                val intent = Intent()
                intent.putExtra(FruitDetailActivity.EXTRA, fruit)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        binding.cancelBt.setOnClickListener {
            finish()
        }
    }

    private fun pickImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, GALLERY_IMAGE_REQUEST_CODE)
    }

    private fun getRealUri(currentUri: Uri): Uri {
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = contentResolver.query(currentUri, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
        val filePath = cursor.getString(columnIndex)
        cursor.close()
        return Uri.parse(URLDecoder.decode(filePath, "UTF-8"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==PERMISSION_CODE_READ){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) pickImage()
        }
    }

    private fun updateImage(){
        binding.image.setImageURI(imageUri)
        binding.image.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.image.requestLayout()
        if(binding.selectTxt.parent != null) (binding.selectTxt.parent as ViewGroup).removeView(binding.selectTxt);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            imageUri = getRealUri(data!!.data!!);
            updateImage();
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(imageUri != null) outState.putParcelable("imagePath", imageUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        imageUri = savedInstanceState.getParcelable("imagePath")
        Log.d("STATE", imageUri!!.path)
        if(imageUri != null) updateImage()
    }

}