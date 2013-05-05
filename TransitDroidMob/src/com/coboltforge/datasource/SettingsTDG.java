package com.coboltforge.datasource;

import org.shipp.activity.PrincipalActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.coboltforge.domain.Setting;

/**
 * Class helps to store settings details in database
 * @author Austin Takam
 *
 */
public class SettingsTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "TRANSITDROID.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "SETTING";
    private static PrincipalActivity activity;
    
    /**
     * Constructor
     * @param activity
     */
    public SettingsTDG(PrincipalActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
    /**
     * Create table if not exist
     */
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, OPUS_THEME TEXT, SHOW_PICTURE TEXT, DISPLAY_NAME TEXT, USERNAME TEXT, PASSWORD TEXT, DISCLAIMER TEXT);");
        mydb.close();
        }catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Add setting details into the database
     * @param set
     */
    public void setSettings(Setting set){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            String connected = "no";
            if (set.isConnected()){
            	connected = "yes";
            }
            mydb.execSQL("INSERT INTO " + TABLE + "(OPUS_THEME, SHOW_PICTURE, DISPLAY_NAME, USERNAME, PASSWORD, DISCLAIMER) VALUES('" + set.getOpus_theme() + "', '" + connected + "', '" + set.getDisplayName() + "', '" + set.getUsername() + "', '" + set.getPassword() + "', '" + set.getDisclaimer() + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Delete all values from database
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
     * obtain recent settings stored in database
     * @return Setting
     */
    public static Setting getSetting(){
    	Setting set = null;
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            String theme, connected;
            boolean isConnected = false;
            String un, pass, dis, claim;
            
            if(allrows.moveToFirst()){
                do{
                    theme = allrows.getString(1);
                    connected = allrows.getString(2);
                    dis = allrows.getString(3);
                    un = allrows.getString(4);
                    pass = allrows.getString(5);
                    claim = allrows.getString(6);
                }
                while(allrows.moveToNext());
                
                if (connected != null && connected.equalsIgnoreCase("yes")){
                	isConnected = true;
                }
                set = new Setting(theme, isConnected, un, pass, dis, activity, claim);
            }
            else {
            	set = new Setting("Original", false, "", "", "", activity, "No");
            }
            mydb.close();
         }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        	 //user = User.getInstance(activity);
        }
    	return set;
    }
}
