package com.sg.pager10.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sg.pager10.R
import com.sg.pager10.model.Post

class PagerAdapterPost(val posts:ArrayList<Post>): RecyclerView.Adapter<PagerAdapterPost.PagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view_page_pager,parent,false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
      holder.bindImage(posts[position])

    }

    override fun getItemCount()=posts.size

    inner class PagerViewHolder( itemView: View): RecyclerView.ViewHolder(itemView){
        val postImage = itemView?.findViewById<ImageView>(R.id.pagerImage)

        fun bindImage(post:Post){
           postImage.load(post.imageUri)
           // postImage.setImageResource(imageRes)
        }

    }

}
/*

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentImage=images[position]
        holder.itemView.ivImage.setImageResource(currentImage)
    }

    override fun getItemCount()=images.size

    inner class ViewPagerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }*/