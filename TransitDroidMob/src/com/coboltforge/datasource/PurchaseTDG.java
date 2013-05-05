package com.coboltforge.datasource;

import java.util.ArrayList;

import org.shipp.activity.PrincipalActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Class to help store purchase details in the database
 * @author Austin Takam
 *
 */
public class PurchaseTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "TRANSITDROID.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "PURCHASES";
    private PrincipalActivity activity;
    
    /**
     * Constructor
     * @param activity
     */
    public PurchaseTDG(PrincipalActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
    /**
     * Create table if not exist
     */
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, PURCHASE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Insert purchase details in database
     * @param purchase
     */
    public void insertIntoTable(String purchase){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(PURCHASE) VALUES('" + purchase + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Delete values from the database
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
     * Return list of purchases stored in the database
     * @return PurchaseList
     */
    public ArrayList<String> getPurchaseDetails(){
    	ArrayList<String> purchase = new ArrayList<String>();
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            
            
            if(allrows.moveToFirst()){
                do{
                    purchase.add(allrows.getString(1).trim());
                }
                while(allrows.moveToNext());
            }
            mydb.close();
         }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        }
    	return purchase;
    }
}
