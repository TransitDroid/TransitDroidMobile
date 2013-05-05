package com.coboltforge.datasource;

import org.shipp.activity.PrincipalActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class helps to store/retrieve selected profile picture
 * @author Austin Takam
 * 
 */
public class PictureTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "TRANSITDROID.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "PATH";
    private PrincipalActivity activity;
    
    /**
     * 
     * @param activity
     */
    public PictureTDG(PrincipalActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
    /**
     * Create table if not exist
     */
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, BYTE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Insert picture path in database
     * @param path
     */
    public void insertIntoTable(String path){
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(BYTE) VALUES('" + path + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Delete all values in the database
     */
    public void deleteValues(){
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("DELETE FROM " + TABLE);
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered while deleting.", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Obtain picture path from database
     * @return picturePath
     */
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
