package com.example.kotlininstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    lateinit var userArrayList:ArrayList<userModel>
    private lateinit var feedadapter: FeedRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        auth= Firebase.auth
        db = Firebase.firestore
        userArrayList= ArrayList<userModel>()

        recyclerView.layoutManager=LinearLayoutManager(this)
        feedadapter= FeedRecyclerAdapter(userArrayList)
        recyclerView.adapter=feedadapter
        getData()
    }

    fun getData(){


        db.collection("Posts").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null){

                Toast.makeText(this@FeedActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value!=null){
                    if (!value.isEmpty){

                      userArrayList.clear()
                        val documents = value.documents


                        for (doc in documents){

                            val yorum = doc.get("yorum") as String
                            val email= doc.get("kullanicimail") as String
                            val resimurl=doc.get("resimurl") as String


                            println(yorum)
                            val userModel=userModel(email,yorum,resimurl)
                            userArrayList.add(userModel)
                        }

                    }

                }
                feedadapter.notifyDataSetChanged()
            }



        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.postekle){
            val intent = Intent(this@FeedActivity,UploadActivity::class.java)
            startActivity(intent)
        }
        else if (item.itemId==R.id.cikis){
            auth.signOut()
            val intent = Intent(this@FeedActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}