package com.sg.pager10.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sg.pager10.R

class PagerAdapter(val images:List<Int>): RecyclerView.Adapter<PagerAdapter.PagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view_page_pager,parent,false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
      holder.bindImage(images[position])

    }

    override fun getItemCount()=images.size

    inner class PagerViewHolder( itemView: View): RecyclerView.ViewHolder(itemView){
        val postImage = itemView?.findViewById<ImageView>(R.id.pagerImage)

        fun bindImage(imageRes:Int){
            postImage.setImageResource(imageRes)
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