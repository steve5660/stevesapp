package com.mycompany.stevesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException; //changed code below to throw SQLException rather than Exception
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Michelle on 21/01/2015.
 */
public class MyDatabaseManager {

    //the Activity or Application that is creating an object from this class:
    Context context;            //see constructor for MyDatabaseManager below

    //Class attributes
    private SQLiteDatabase db; //My database is to be an object called 'db'.
    private final String DB_NAME = "message_db";
    private final int DB_VERSION = 1;

        //table and column names
        private final String TABLE_NAME = "messages";
        private final String ROW_ID = "id";
        private final String MESSAGE = "message";

    //TO DO - WRITE CONSTRUCTOR METHODS FOR THE MANAGER CLASS - See next:
    public MyDatabaseManager(Context context){
        this.context = context;
        //create or open database
        MyOpenHelper helper = new MyOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void addRow(String rowValue){ //can take more parameters if more columns
                                        //ROW_ID will update automatically (auto increment)
        //VALUES are passed to the SQLiteDatabase insert method as a 'ContentValues' object, so:
        ContentValues values = new ContentValues();

        values.put(MESSAGE, rowValue); //one 'put' method for each column required here.

        //insert this ContentValues object into the table
        try{
            db.insert(TABLE_NAME, null, values); //null signifies null values not allowed.
        }
        catch(SQLException e){
            Log.e("Add Row Error", e.toString()); //print error message to log
            e.printStackTrace();                    //print stack trace to log
        }
    }

    //similar to last method but for update:
    public void updateRow(long rowID, String rowValue){
        ContentValues values = new ContentValues();
        values.put(MESSAGE, rowValue);

        try{
            db.update(TABLE_NAME, values, ROW_ID + "=" + rowID, null);
        }
        catch(SQLException e){
            Log.e("Add Row Error", e.toString()); //print error message to log
            e.printStackTrace();                    //print stack trace to log
        }
    }

    // To do: public Array
    public ArrayList<Object> getRow(long rowID){
        ArrayList<Object> row = new ArrayList<Object>();
        Cursor cursor;
                        //NEED A DO-WHILE BLOCK IN THE TRY BLOCK !!!!!!!!
        try{
            cursor = db.query(TABLE_NAME,
                    new String[]{ROW_ID, MESSAGE},
                    ROW_ID + "=" + rowID,
                    null,null,null,null,null);  //CHECK!!! This has 8 parameters.

            cursor.moveToFirst();
            if(!cursor.isAfterLast()){
                do {
                    row.add(cursor.getLong(0));
                    row.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (SQLException e){
            Log.e("Add Row Error", e.toString()); //print error message to log
            e.printStackTrace();
        }
        return row;
    }

    public ArrayList<ArrayList<Object>> getAllRows(){
        ArrayList<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
        Cursor cursor;

        try{
            cursor = db.query(TABLE_NAME,
                    new String[]{ROW_ID, MESSAGE},
                    null,null,null,null,null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()){ //if it is, then no rows have been returned
                do{
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));

                    rows.add(dataList);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch(SQLException e){
            Log.e("Add Row Error", e.toString()); //print error message to log
            e.printStackTrace();                    //print stack trace to log
        }
        return rows;
    }



    //OPEN HELPER (For creating and upgrading the database and table structures) internal class:
    private class MyOpenHelper extends SQLiteOpenHelper{  //declared static in NotePad example

        //TO DO - override constructor and other methods in (abstract) parent class. - DONE.
        //this is the constructor:
        public MyOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION); //context is info about app environment. null means use default 'CursorFactory'
                                                        //(Data from the db comes in the form of a Cursor.
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //
            String createTableSQL = "create table "+TABLE_NAME+" ("+ROW_ID+" integer primary key autoincrement not null, "+MESSAGE+" text);";
            db.execSQL(createTableSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            //Only needs completing if database structure changes.
        }
    }
}
