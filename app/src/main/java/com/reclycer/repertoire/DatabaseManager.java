package com.reclycer.repertoire;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyouse on 05/12/17.
 */

public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Repertoire.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);
            Log.i("DATABASE", "onCreate invoked");
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't create database", exception);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public void insertContact(Contact contact) {
        try {
            Dao<Contact, Integer> dao = getDao(Contact.class);
            dao.create(contact);
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't insert contact into Database", exception);
        }
    }

    public List<Contact> readContact() {
        try {
            Dao<Contact, Integer> dao = getDao(Contact.class);
            List<Contact> list_contact = dao.queryForAll();
            Log.i("DATABASE", "readContact invoked");
            return list_contact;
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't readContact", exception);
            return null;
        }
    }

    public Contact getContact(int id){
        try {
            Dao<Contact, Integer> dao = getDao(Contact.class);
            return dao.queryForId(id);
        }
        catch (Exception exception){
            Log.e("DATABASE", "Can't get contact", exception);
            return null;
        }
    }

    public void deleteContact(int id) {
        try {
            Dao<Contact, Integer> dao = getDao(Contact.class);
            dao.deleteById(id);
            Log.i("DATABASE", "deleteContact invoked");
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't delete contact", exception);
        }
    }

    public void updateContact(Contact contact){
        try {
            Dao<Contact, Integer> dao = getDao(Contact.class);
            dao.update(contact);
        }
        catch (Exception exception) {
            Log.e("DATABASE", "Can't update contact", exception);
        }
    }
}
