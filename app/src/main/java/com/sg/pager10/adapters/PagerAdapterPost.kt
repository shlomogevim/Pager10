package com.sg.pager10.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sg.pager10.R
import com.sg.pager10.drawingPost.DrawPost
import com.sg.pager10.model.Post
import com.sg.pager10.utilities.Utility
import java.util.*
import kotlin.collections.ArrayList

class PagerAdapterPost( context:Context,val posts:ArrayList<Post>): RecyclerView.Adapter<PagerAdapterPost.PagerViewHolder>(){
   // val layout: ConstraintLayout = (context as Activity).findViewById(R.id.itemLayout)

 val drawPost= DrawPost(context)
  //  val postGenerator=PostGenerator(context)
    val util=Utility()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view_page_pager,parent,false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
      holder.bindImage(posts[position])
    }




    override fun getItemCount()=posts.size





    /*public int getItemPosition(Object object) {
    return POSITION_NONE;
}*/

    inner class PagerViewHolder( itemView: View): RecyclerView.ViewHolder(itemView){
        //val postImage = itemView?.findViewById<ImageView>(R.id.pagerImage)



       /* fun bindImage(post:Post){
            drawPost.drawPost(post,layout)
        }*/

        fun bindImage(post:Post){
            val layout = itemView?.findViewById<ConstraintLayout>(R.id.itemLayout)

                  drawPost.drawPost(post,layout)
        }
    }
}






/*   fun bindImage(post:Post){
  //  postImage.load(post.imageUri)
//         util.logi("pageAdapterPost 112  post=$post")
drawPost.drawPost(post,layout)
   //postGenerator.drawPost(post,layout)
    }*/

/*  fun bindImage(post:Post){
        // postImage.setImageResource(imageRes)
      }*/
/*
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentImage=images[position]
        holder.itemView.ivImage.setImageResource(currentImage)
    }

    override fun getItemCount()=images.size

    inner class ViewPagerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }*/