package com.reclycer.repertoire.data


import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by kyouse on 15/11/16.
 */

@DatabaseTable(tableName = "T_Contact")
class Contact {

    @DatabaseField(columnName = "idContact", generatedId = true)
    var idContact: Int = 0
    @DatabaseField
    var lastName: String? = null
    @DatabaseField
    var firstName: String? = null
    @DatabaseField
    var phoneNumber: String? = null
    @DatabaseField
    var photoPath: String? = null
    @DatabaseField
    private var isPhotoFromApp: Boolean? = null
    @DatabaseField(columnName = COLUMN_NAME_SYNC_ID)
    var sync_id: String? = null
    @DatabaseField
    var to_delete: Boolean? = false
    @DatabaseField
    var email: String? = null
    @DatabaseField
    var gcmToken: String? = null
    @DatabaseField
    var isCurrent: Boolean? = false



    constructor(nom: String, prenom: String, numero: String?) {
        this.lastName = nom
        this.firstName = prenom
        this.phoneNumber = numero
        //this.email = email
    }

    constructor() {}

    fun getisPhotoFromApp(): Boolean? {
        return isPhotoFromApp
    }

    fun setisPhotoFromApp(isPhotoFromApp: Boolean?) {
        this.isPhotoFromApp = isPhotoFromApp
    }

    companion object {
        const val COLUMN_NAME_SYNC_ID="SYNC_ID"
    }
}
