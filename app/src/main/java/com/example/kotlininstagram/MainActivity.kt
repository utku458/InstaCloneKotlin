package com.example.kotlininstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= Firebase.auth
       val currentUser = auth.currentUser

        if (currentUser!=null){
            val intent = Intent(this@MainActivity,FeedActivity::class.java)
            startActivity(intent)
            finish()

        }



    }





    fun kayitol(view:View){


        val email =emailText.text.toString()
        val parola =parolaText.text.toString()

        if (email.equals("") || parola.equals("")){
            Toast.makeText(this, "Kullanici adi ve Parola bos birakilmaz", Toast.LENGTH_LONG).show()
        }else{

            auth.createUserWithEmailAndPassword(email,parola).addOnSuccessListener {
                val intent = Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {

                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }



    fun girisyap(view:View){

        val email:String =emailText.text.toString()
        val parola:String =parolaText.text.toString()
        if (email.equals("") || parola.equals("")){
            Toast.makeText(this, "Kullanici adi ve Parola bos birakilmaz", Toast.LENGTH_LONG).show()
        }else{

            auth.signInWithEmailAndPassword(email,parola).addOnSuccessListener {
                val intent = Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}