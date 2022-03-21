package com.sg.pager10.model

import java.sql.Timestamp

data class Comment(
    val commntId:String="",
    val postId:String="",
    val text:String="",
    val userName:String="",
    val userId:String="",
    val timestamp: Timestamp

)
