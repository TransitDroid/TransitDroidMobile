package com.coboltforge.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coboltforge.slidemenuexample.MainActivity;

public class PictureTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "IMAGESDB.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "PATH";
    private MainActivity activity;
    
    public PictureTDG(MainActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
 // CREATE TABLE IF NOT EXISTS 
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, BYTE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void insertIntoTable(String path){
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(BYTE) VALUES('" + path + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION DELETES VALUES FROM THE DATABASE ACCORDING TO THE CONDITION
    public void deleteValues(){
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("DELETE FROM " + TABLE);
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered while deleting.", Toast.LENGTH_LONG);
        }
    }
    
    public String getImagePth(){
    	String path = null;
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            
            
            if(allrows.moveToFirst()){
                do{
                    path = allrows.getString(1);
                }
                while(allrows.moveToNext());
            }
            mydb.close();
         }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        }
    	return path;
    }
}
