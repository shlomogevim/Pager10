package com.sg.pager10.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.pager10.adapters.PagerAdapter
import com.sg.pager10.R
import com.sg.pager10.adapters.PagerAdapterPost
import com.sg.pager10.databinding.ActivityMainBinding
import com.sg.pager10.model.Post
import com.sg.pager10.utilities.POST_REF
import com.sg.pager10.utilities.Utility
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    val util1 = Utility()
    val posts= ArrayList<Post>()
    lateinit var pagerAdapterPost:PagerAdapterPost




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val posts =downloadAllPost()
        pagerAdapterPost= PagerAdapterPost(posts)
        binding.viewPager.adapter=pagerAdapterPost

     /*   val images = listOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
        )
        val adapter = PagerAdapter(images)
        binding.viewPager.adapter = adapter*/

    }

    fun downloadAllPost() :ArrayList<Post>{
        posts.clear()

        FirebaseFirestore.getInstance().collection(POST_REF).addSnapshotListener { value, error ->
            if (value != null) {
                for (doc in value.documents) {
                    var  post = util1.retrivePostFromFirestore(doc)
                    posts.add(post)
                }
                pagerAdapterPost.notifyDataSetChanged()
            }
        }
        return posts
    }
}

/*   viewPager.beginFakeDrag()
         viewPager.fakeDragBy(-10f)
         viewPager.endFakeDrag()*/

