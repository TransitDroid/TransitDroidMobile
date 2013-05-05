package com.coboltforge.datasource;

import org.shipp.activity.PrincipalActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.coboltforge.domain.User;

/**
 * Class helps to store usage details in database
 * @author Austin Takam
 *
 */
public class UserTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "TRANSITDROID.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "USERS";
    private PrincipalActivity activity;
    
    /**
     * Constructor
     * @param activity
     */
    public UserTDG(PrincipalActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
    /**
     * Create table if not exist
     */
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, NAME TEXT, PID TEXT, USERNAME TEXT, PASSWORD TEXT, KEY1 TEXT, KEY2 TEXT, KEY3 TEXT, KEY4 TEXT, PASSCODE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Insert usage details in database
     * @param usage
     */
    public void insertIntoTable(User user){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(NAME, PID, USERNAME, PASSWORD, KEY1, KEY2, KEY3, KEY4, PASSCODE) VALUES('" + user.getName() + "', '" + user.getId() + "', '" + user.getUsername() + "', '" + user.getPassword() + "', '" + new String(user.getKey1()) + "', '" + new String(user.getKey2()) + "', '" + new String(user.getKey3()) + "', '" + new String(user.getKey4()) + "', '" + new String(user.getPassCode()) + "')");
            mydb.close();
        }catch(Exception e){
            Toast.makeText(activity, "Error in inserting into table", Toast.LENGTH_LONG);
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
     * Get list of usage details
     * @return UsageList
     */
    public User getUser(){
    	User user = null;
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            String name, id, password, username;
            byte[] key1, key2, key3, key4, passCode;
            if(allrows.moveToFirst()){
                do{
                    name = allrows.getString(1).trim();
                    id = allrows.getString(2).trim();
                    username = allrows.getString(3).trim();
                    password = allrows.getString(4).trim();
                    key1 = allrows.getString(5).trim().getBytes();
                    key2 = allrows.getString(6).trim().getBytes();
                    key3 = allrows.getString(7).trim().getBytes();
                    key4 = allrows.getString(8).trim().getBytes();
                    passCode = allrows.getString(9).trim().getBytes();
                    user = new User(name, id, username, password, key1, key2, key3, key4, passCode);
                }
                while(allrows.moveToNext());
            }
            mydb.close();
         }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        }
    	return user;
    }
}
