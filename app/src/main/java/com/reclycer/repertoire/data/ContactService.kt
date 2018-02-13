package com.reclycer.repertoire.data

import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by kyouse on 06/02/18.
 */
interface ContactService {

    data class ApiContact(val _id:String?, val first_name:String, val last_name: String, val phone_number:String) {

    }

    @GET("contact/")
    fun getContactList() : Single<List<ApiContact>>

    @POST("contact/")
    @FormUrlEncoded
    fun createContact(@Field("first_name") first_name: String,
                      @Field("last_name") last_name: String,
                      @Field("phone_number") phone_number: String) : Single<ApiContact>

    @PUT("contact/{id}")
    @FormUrlEncoded
    fun updateContact(@Path("id") _id: String,
                      @Field("first_name") first_name: String,
                      @Field("last_name") last_name: String,
                      @Field("phone_number") phone_number: String) : Single<ApiContact>

}