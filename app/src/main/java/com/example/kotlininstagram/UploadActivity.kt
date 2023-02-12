package com.example.kotlininstagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var permissionLauncher: ActivityResultLauncher<String>
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var storage : FirebaseStorage
    var secilengorsel:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        registerLauncher()
        storage = Firebase.storage
        auth=Firebase.auth
        firestore=Firebase.firestore


    }

    fun yukle(view:View){

        val uuid=UUID.randomUUID()
        val imagename="$uuid.jpg"
        val reference = storage.reference
        val imageReference=reference.child("images").child(imagename)
        if (secilengorsel!=null){
            imageReference.putFile(secilengorsel!!).addOnSuccessListener {

                val guncelResim=storage.reference.child("images").child(imagename)
                guncelResim.downloadUrl.addOnSuccessListener {
                    val indirilenResim=it.toString()





                    val user = hashMapOf(
                        "resimurl" to indirilenResim,
                        "kullanicimail" to auth.currentUser!!.email!!,
                        "yorum" to yorumText.text.toString(),
                        "tarih" to com.google.firebase.Timestamp.now()
                    )


                    firestore.collection("Posts").add(user).addOnSuccessListener {

                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }



                }



            }.addOnFailureListener {

                Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }
    fun resimsec(view:View){


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
           if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

               Snackbar.make(view,"Galeri icin izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("Izin ver"){

                   permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
               }.show()
           }else{

               permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
           }
        }else{
            //izin verildi
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }
    }

    fun registerLauncher(){

        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

            if (result.resultCode== RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult!=null){

                    secilengorsel=intentFromResult.data

                    secilengorsel?.let {
                        recyclerImageView.setImageURI(it)
                    }
                }
            }
        }

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->

            if (result){

                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //izin verilmedi
                Toast.makeText(this@UploadActivity, "Izin gerekli", Toast.LENGTH_LONG).show()

            }

        }
    }

}