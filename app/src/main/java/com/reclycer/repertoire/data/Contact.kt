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
    var nom: String? = null
    @DatabaseField
    var prenom: String? = null
    @DatabaseField
    var numero: String? = null
    @DatabaseField
    var photoPath: String? = null
    @DatabaseField
    private var isPhotoFromApp: Boolean? = null


    constructor(nom: String, prenom: String, numero: String) {
        this.nom = nom
        this.prenom = prenom
        this.numero = numero
    }

    constructor() {}

    fun getisPhotoFromApp(): Boolean? {
        return isPhotoFromApp
    }

    fun setisPhotoFromApp(isPhotoFromApp: Boolean?) {
        this.isPhotoFromApp = isPhotoFromApp
    }
}