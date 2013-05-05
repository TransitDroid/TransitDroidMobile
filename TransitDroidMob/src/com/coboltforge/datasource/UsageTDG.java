package com.coboltforge.datasource;

import java.util.ArrayList;

import org.shipp.activity.PrincipalActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Class helps to store usage details in database
 * @author Austin Takam
 *
 */
public class UsageTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "TRANSITDROID.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "USAGE";
    private PrincipalActivity activity;
    
    /**
     * Constructor
     * @param activity
     */
    public UsageTDG(PrincipalActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
    /**
     * Create table if not exist
     */
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, USAGE TEXT);");
        mydb.close();
        }catch(Exception e){
        	Toast t = Toast.makeText(activity, "Error Creating DB", Toast.LENGTH_LONG);
    		t.setDuration(10000);
    		t.show();
        }
    }
    
    /**
     * Insert usage details in database
     * @param usage
     */
    public void insertIntoTable(String usage){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(USAGE) VALUES('" + usage + "')");
            mydb.close();
        }catch(Exception e){
        	Toast t = Toast.makeText(activity, "Error inserting in DB", Toast.LENGTH_LONG);
    		t.setDuration(10000);
    		t.show();
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
        	Toast t = Toast.makeText(activity, "Error deleting from DB.", Toast.LENGTH_LONG);
    		t.setDuration(10000);
    		t.show();
        }
    }
    
    /**
     * Get list of usage details
     * @return UsageList
     */
    public ArrayList<String> getUsageDetails(){
    	ArrayList<String> usage = new ArrayList<String>();
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            
            
            if(allrows.moveToFirst()){
                do{
                    usage.add(allrows.getString(1).trim());
                }
                while(allrows.moveToNext());
            }
            mydb.close();
         }catch(Exception e){
        	 Toast t = Toast.makeText(activity, "Error getting user from DB.", Toast.LENGTH_LONG);
     		t.setDuration(10000);
     		t.show();
        }
    	return usage;
    }
}
