package com.example.kotlininstagram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class FeedRecyclerAdapter(var userlist:ArrayList<userModel>):RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    class PostHolder(var view: View) : RecyclerView.ViewHolder(view){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.view.recyclerCommentText.text = userlist.get(position).yorum
        holder.view.recyclerKullaniciAdiText.text=userlist.get(position).email
        Picasso.get().load(userlist.get(position).downloadUrl).into(holder.view.recyclerImageView)
    }

    override fun getItemCount(): Int {
       return userlist.size

    }
}