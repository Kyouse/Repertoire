package com.reclycer.repertoire.data

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by kyouse on 06/02/18.
 */
interface ContactService {

    data class ApiContact(val _id:String?, val first_name:String, val last_name: String, val phone_number:String, val email: String?, val gcm_token: String?) {

    }

    @GET("contact2/")
    fun getContactList() : Single<List<ApiContact>>

    @POST("contact/")
    @FormUrlEncoded
    fun createContact(@Field("first_name") first_name: String,
                      @Field("last_name") last_name: String,
                      @Field("phone_number") phone_number: String,
                      @Field("gcm_token") gcm_token : String?) : Single<ApiContact>

    @PUT("contact/{id}")
    @FormUrlEncoded
    fun updateContact(@Path("id") _id: String,
                      @Field("first_name") first_name: String,
                      @Field("last_name") last_name: String,
                      @Field("phone_number") phone_number: String,
                      @Field("gcm_token") gcm_token: String?) : Single<ApiContact>

    @DELETE("contact/{id}")
    fun deleteContact(@Path("id") _id: String): Completable

    data class ApiMessage(val to_id: String, val from_id: String, val body: String, val date: String?, val _id: String?)

    //TODO create api message
    @GET("messages/")
    fun getMessages() : Single<List<ApiMessage>>

    @POST("messages/")
    @FormUrlEncoded
    fun sendMessage(@Field("to_id") to_id: String,
                      @Field("from_id") from_id: String,
                      @Field("body") body : String,
                      @Field("date") date : String?) : Single<ApiMessage>


}