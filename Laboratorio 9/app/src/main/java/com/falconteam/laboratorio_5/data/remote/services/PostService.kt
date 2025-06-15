package com.falconteam.laboratorio_5.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class GetAllPostWithCommentsResponse(
    val _id: String,
    val tittle: String,
    val description: String,
    val author: String,
    val messages: List<Message>
)

data class Message(
    val _id: String,
    val comment: String,
    val author: String
)

data class AddPostRequest(
    val tittle: String,
    val description: String
)

data class AddPostResponse(
    val status: String,
    val message: String
)

data class AddCommentRequest(
    val postId: String,
    val comment: String
)

data class AddCommentResponse(
    val status: String,
    val message: String
)

data class DeletePostRequest(
    val postId: String
)

data class DeletePostResponse(
    val status: String,
    val message: String
)

interface PostService {
    @GET("post/getall")
    suspend fun getAllPostsWithComments(
        @Header("Authorization") token: String
    ): Response<List<GetAllPostWithCommentsResponse>>

    @POST("post/add")
    suspend fun addPost(
        @Header("Authorization") token: String,
        @Body addPostRequest: AddPostRequest
    ): Response<AddPostResponse>

    @POST("post/addcomment")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Body addCommentRequest: AddCommentRequest
    ): Response<AddCommentResponse>

    @POST("post/removepost")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Body deletePostRequest: DeletePostRequest
    ): Response<DeletePostResponse>
}