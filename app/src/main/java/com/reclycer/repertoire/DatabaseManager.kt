package com.reclycer.repertoire

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

import com.reclycer.repertoire.data.Contact

/**
 * Created by kyouse on 05/12/17.
 */

class DatabaseManager(context: Context, databaseName: String = SYNC_DATABASE) : OrmLiteSqliteOpenHelper(context, databaseName, null, DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable<Contact>(connectionSource, Contact::class.java)
            Log.i("DATABASE", "onCreate invoked")
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't create database", exception)
        }
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {

    }

    fun insertContact(contact: Contact) {
        try {
            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            dao.create(contact)
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't insert contact into Database", exception)
        }

    }

    fun readContact(): List<Contact>? {
        try {
            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            val list_contact = dao.queryForAll()
            Log.i("DATABASE", "readContact invoked")
            return list_contact
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't readContact", exception)
            return null
        }

    }

    fun getContact(id: Int): Contact? {
        try {
            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            return dao.queryForId(id)
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't get contact", exception)
            return null
        }
    }

    fun deleteContact(id: Int) {
        try {
            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            dao.deleteById(id)
            Log.i("DATABASE", "deleteContact invoked")
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't delete contact", exception)
        }

    }

    fun updateContact(contact: Contact) {
        try {
            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            dao.update(contact)
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't update contact", exception)
        }
    }


    fun getContact(sync_id: String): Contact? {
        try {

            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            val queryBuilder = dao.queryBuilder()
            queryBuilder.where().eq(Contact.COLUMN_NAME_SYNC_ID, sync_id)
            return queryBuilder.queryForFirst()
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't get contact", exception)
            return null
        }
    }

    companion object {

        private val DATABASE_NAME = "Repertoire.db"
        private val DATABASE_VERSION = 1
        val SYNC_DATABASE = "synchronised_contacts.db"

    }

    fun markToDelete(id: Int) {
        try {
            val dao = getDao<Dao<Contact, Int>, Contact>(Contact::class.java)
            val contact = dao.queryForId(id)
            contact.to_delete = true
            dao.update(contact)
            Log.i("DATABASE", "markToDelete invoked")
        } catch (exception: Exception) {
            Log.e("DATABASE", "Can't delete contact", exception)
        }
    }
}
