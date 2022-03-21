package com.sg.pager10.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.pager10.R
import com.sg.pager10.adapters.CommentAdapter
import com.sg.pager10.databinding.ActivityPostDetailsBinding
import com.sg.pager10.interfaces.CommentsOptionClickListener
import com.sg.pager10.model.Comment
import com.sg.pager10.model.Post
import com.sg.pager10.utilities.*
import kotlinx.coroutines.*
import java.util.ArrayList

class PostDetailsActivity : AppCompatActivity(), CommentsOptionClickListener {
    lateinit var binding: com.sg.pager10.databinding.ActivityPostDetailsBinding
    private var currentUser: FirebaseUser? = null
     var util = Utility()
    var textViewArray = ArrayList<TextView>()

    lateinit var commentsRV: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    val comments = ArrayList<Comment>()
    var currentPostNum = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        createTextViewArray()
        setContentView(binding.root)

 //   util.logi("PostDetailActivity 115     currentUser=$currentUser")

      currentPostNum = intent.getIntExtra(DETAIL_POST_EXSTRA, 0)

           create_commentsRv()
           operateButtoms()
           createComments()
           findCurrentPost()
        // coroutineStyle()
    }

    private fun operateButtoms() {
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))
        }
        binding.profileImageComment.setOnClickListener {
            addComment()
            //  finish()
        }
    }

    override fun onStart() {
        super.onStart()
                   util.logi("PostDetails 121 in OnStart")
          val pref=getSharedPreferences(SHAR_PREF, Context.MODE_PRIVATE)
          val existUser= pref.getString(CURRENT_USER_EXIST,"none").toString()
              util.logi("PostDetails 122 in OnStart          existUse1=$existUser")
        if (existUser== EXIST){
            currentUser = FirebaseAuth.getInstance().currentUser
            binding.nameCurrentUserName.setText(currentUser!!.displayName.toString())
        }
    }
    /*  override fun onStart() {
           super.onStart()
           if (FirebaseAuth.getInstance().currentUser != null) {
               val intent = Intent(this, MainActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(intent)
               finish()
           }
       }*/
    private fun createComments() {
        FirebaseFirestore.getInstance().collection(COMMENT_REF).document(currentPostNum.toString())
            .collection(COMMENT_LIST).addSnapshotListener { value, error ->
                if (value != null) {
                    comments.clear()
                    for (doc in value.documents) {
                        val comment = util.retriveCommentFromFirestore(doc)
                        comments.add(comment)
                    }
                    //    util.logi("PostDetailsActivity 111        comments.size=${comments.size} ")
                    commentAdapter.notifyDataSetChanged()
                }

            }

    }

    private fun addComment() {
        val commentText = binding.postCommentText.text.toString()
        if (commentText == "") {
            util.toasti(this, " היי , קודם תכתוב משהו בהערה ואחר כך תלחץ ...")
        } else {
            binding.postCommentText.text.clear()
            hideKeyboard()
            FirebaseFirestore.getInstance().collection(POST_REF).document(currentPostNum.toString())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val post = util.retrivePostFromFirestore(task.result)
                        util.createComment(post, commentText)
                    }
                }
        }

    }

    private fun findCurrentPost() {
        FirebaseFirestore.getInstance().collection(POST_REF).document(currentPostNum.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val post = util.retrivePostFromFirestore(task.result)
                    drawHeadline(post)
                }
            }
    }


    private fun drawHeadline(post: Post) {
        val num = post.postNum
        val st = "   פוסט מספר: " + "$num   "
        binding.postNumber.text = st
        for (ind in 0 until post.postText.size) {
            textViewArray[ind].visibility = View.VISIBLE
            textViewArray[ind].text = post.postText[ind]
        }
    }

    private fun create_commentsRv() {
        commentsRV = binding.rvPost
        commentAdapter = CommentAdapter(comments, this)
        val layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        commentsRV.layoutManager = layoutManger
        commentsRV.adapter = commentAdapter
    }


    private fun createTextViewArray() {
        with(binding) {
            textViewArray = arrayListOf(
                tvPost1,
                tvPost2,
                tvPost3,
                tvPost4,
                tvPost5,
                tvPost6,
                tvPost7,
                tvPost8,
                tvPost9
            )
        }

    }

    private fun hideKeyboard() {
        val inputeManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputeManager.isAcceptingText) {
            inputeManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun optionMenuClicked(comment: Comment) {

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.option_menu, null)
        val deleteBtn = dialogView.findViewById<Button>(R.id.optionDelelBtn)
        val editBtn = dialogView.findViewById<Button>(R.id.optionEditBtn)
        builder.setView(dialogView)
            .setNegativeButton("Cancel") { _, _ -> }
        val ad = builder.show()
        deleteBtn.setOnClickListener {
            util.deleteComment(comment)
            finish()
        }
        editBtn.setOnClickListener {
            val intent = Intent(this, UpdateCommentActivity::class.java)
            intent.putExtra(COMMENT_POST_ID, comment.postId)
            intent.putExtra(COMMENT_ID, comment.commntId)
            intent.putExtra(COMMENT_TEXT, comment.text)
            startActivity(intent)
            finish()
        }
    }


}
/*     deleteBtn.setOnClickListener {
            val thoughtRef = FirebaseFirestore.getInstance()
                .collection(THOUGHT_REF).document(thoughtDocumentID)

            val commentRef = FirebaseFirestore.getInstance()
                .collection(THOUGHT_REF).document(thoughtDocumentID)
                .collection(COMMENTS_REF).document(comment.documentID)

            FirebaseFirestore.getInstance().runTransaction { transaction ->
                val thought = transaction.get(thoughtRef)
                val numComments = thought.getLong(NUM_COMMENTS)?.minus(1)
                transaction.update(thoughtRef, NUM_COMMENTS, numComments)
                transaction.delete(commentRef)
            }.addOnSuccessListener {
                ad.dismiss()
            }.addOnFailureListener {
                Log.d(TAG, "cannot create transaction because : ${it.localizedMessage}")
            }
        }

        editBtn.setOnClickListener {
            val intent = Intent(this, UpdateCommentActivity::class.java)
            intent.putExtra(THOUGHT_DOC_ID_EXTRA, thoughtDocumentID)
            intent.putExtra(COMMENT_DOC_ID_EXTRA, comment.documentID)
            intent.putExtra(COMMENT_TEXT_EXSTRA, comment.commentTxt)
            ad.dismiss()
            startActivity(intent)
        }
    }*/


/*  private fun createDemiArray() {
        for (index in 1..32) {
            val comment = Comment("This is message from popi$index", "Popi$index", "${index * 41}")
            comments.add(comment)
            commentAdapter.notifyDataSetChanged()
        }
    }*/


/*private  fun findCurrentPost2(numPost:Int,callback: (Post) -> Unit) {
    FirebaseFirestore.getInstance().collection(POST_REF).document(numPost.toString()).get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                // util.logi("Postdetail 101                 currentPostNum=$currentPostNum")
                val post=util.retrivePostFromFirestore(task.result)
                callback(post)
            }
        }
}

 private fun coroutineStyle(currentPostNum:Int) {
          val numPost=currentPostNum
         val coroutinScope = CoroutineScope(Dispatchers.Main)
         coroutinScope.launch {
           findCurrentPost2(numPost) { post -> drawLayout(post) }                     //  Its work
            }
    }

*/


private suspend fun findCurrentPost1(postNum: Int) {
    val coroutinScope = CoroutineScope(Dispatchers.Main)
    //      util.logi1("PostDetailActivity 101            contex1=${coroutineContext}")
    coroutinScope.launch {

        val postDeffer = coroutinScope.async(Dispatchers.IO) {
            getValueFromFireStore(postNum)
        }

        val postExist = postDeffer.await()
        postExist.addOnCompleteListener { task ->
            // var post=util.retrivePostFromFirestore(task.result)
        }
    }

}

/*private val coroutinScope = CoroutineScope(Dispatchers.Main)
    coroutinScope.launch {
        val originalDeffer = coroutinScope.async(Dispatchers.IO) { getOriginalBitmap() }
        val originalBitmap = originalDeffer.await()
        val afterFilterDiffer=coroutinScope.async(Dispatchers.Default) {Filter.apply(originalBitmap)}
        val afterFilter=afterFilterDiffer.await()
        loadImg(afterFilter)
    }
}*/

private suspend fun getValueFromFireStore(postNum: Int) =
    FirebaseFirestore.getInstance().collection(POST_REF).document(postNum.toString()).get()
/*  private fun drawLayout1() {
    util.logi("PostDetailActivity 111 cuurentPost=$currentPose")
    val num=currentPose.postNum
    val st= "   פוסט מספר: $num   "
    binding.postNumber.text=st
    drawPost(currentPose)
}*/

/*class PostDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostDetailsBinding
    private  var firbaseUser: FirebaseUser? =null
    var currentPose=Post()
    var posts= ArrayList<Post>()
    var util=Utility()
    var textViewArray=ArrayList<TextView>()

    lateinit var recyclerView: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    val comments = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostDetailsBinding.inflate(layoutInflater)
        createTextViewArray()
        setContentView(binding.root)

        firbaseUser = FirebaseAuth.getInstance().currentUser!!
     //   firbaseUser=null
        if (firbaseUser==null){
            binding.currentUserName.setText("ברוך הבא משתמש אנונימי")
        }else{
            val s1= firbaseUser!!.displayName
            val s12= ":"
            val s2="כרגע מככב כאן"
            val s3="$s1 $s12 $s2"
            binding.currentUserName.setText(s3)
        }


        val currentPostNum=intent.getIntExtra(DETAIL_POST_EXSTRA,0)
         findCurrentPost(currentPostNum)



        commentsRv()
      //  createDemiArray()
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.signInBtn.setOnClickListener {
          //  startActivity(Intent(this,SignInActivity::class.java))
            startActivity(Intent(this, SignInActivity::class.java))
        }
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this,AccountSettingActivity::class.java))
        }
        binding.addCommentLayout.setOnClickListener {


        }

    }

    private fun createDemiArray() {
        for (index  in 1..32){
            val comment=Comment("This is message from popi$index","Popi$index","${index*41}" )
            comments.add(comment)
            commentAdapter.notifyDataSetChanged()
        }

    }

    private fun commentsRv() {
        recyclerView = binding.rvPost
      commentAdapter = CommentAdapter(comments)
        val layoutManger = LinearLayoutManager(this)
        layoutManger.reverseLayout = true
        recyclerView.layoutManager = layoutManger
       recyclerView.adapter = commentAdapter

    }

    private fun createTextViewArray() {
        with (binding){
            textViewArray= arrayListOf(tvPost1,tvPost2,tvPost3,tvPost4,tvPost5,tvPost6,tvPost7,tvPost8,tvPost9)
        }

    }

    private fun findCurrentPost(currentPostNum: Int) {
        FirebaseFirestore.getInstance().collection(POST_REF).document(currentPostNum.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    // util.logi("Postdetail 101                 currentPostNum=$currentPostNum")
                    val post=util.retrivePostFromFirestore(task.result)
                    drawLayout(post)

                }
            }
    }

    private fun drawLayout(post: Post) {
        val num=post.postNum
        val st="   פוסט מספר: "+"$num   "
       binding.postNumber.text=st
        drawPost(post)
    }

    private fun drawPost(post: Post) {
        for (ind in 0 until post.postText.size) {
            textViewArray[ind].visibility=View.VISIBLE
            textViewArray[ind].text=post.postText[ind]
        }

        }

    }*/


