package com.reclycer.repertoire;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by kyouse on 15/11/16.
 */

@DatabaseTable( tableName = "T_Contact")
public class Contact{

    @DatabaseField( columnName = "idContact", generatedId = true)
    private int idContact;
    @DatabaseField
    private String nom;
    @DatabaseField
    private String prenom;
    @DatabaseField
    private String numero;
    @DatabaseField
    private String photoPath;
    @DatabaseField
    private Boolean isPhotoFromApp;



    public Contact(String nom, String prenom, String numero){
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
    }

    public Contact(){
    }

    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Boolean getisPhotoFromApp() {
        return isPhotoFromApp;
    }

    public void setisPhotoFromApp(Boolean isPhotoFromApp) {
        this.isPhotoFromApp = isPhotoFromApp;
    }
}
