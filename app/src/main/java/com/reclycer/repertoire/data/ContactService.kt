package com.reclycer.repertoire.data

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by kyouse on 06/02/18.
 */
interface ContactService {

    data class ApiContact(val _id:String, val first_name:String, val last_name: String, val phone_number:String)
    @GET("contact/")
    fun getContactList() : Single<List<ApiContact>>
}