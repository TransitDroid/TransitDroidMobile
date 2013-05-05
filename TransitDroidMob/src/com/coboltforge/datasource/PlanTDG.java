package com.coboltforge.datasource;

import org.shipp.activity.PrincipalActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coboltforge.domain.Plan;

/**
 * This class helps keep information about the specific user's plan
 * @author Austin Takam
 *
 */
public class PlanTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "TRANSITDROID.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "PLAN";
    private PrincipalActivity activity;
    
    /**
     * 
     * @param activity
     */
    public PlanTDG(PrincipalActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
    /**
     * Create table if not exist
     */
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, PLAN_ID TEXT, TYPE TEXT, EXP TEXT, PASS_CODE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Insert plan details in database
     * @param plan
     */
    public void insertIntoTable(Plan plan){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(PLAN_ID, TYPE, EXP, PASS_CODE) VALUES('" + plan.getId() + "', " + "'" + plan.getType() + "', " + "'" + plan.getExp() + "', " + "'" + plan.getPassCode() + "')");
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
            mydb.execSQL("DELETE FROM " + TABLE );
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered while deleting.", Toast.LENGTH_LONG);
        }
    }
    
    /**
     * Obtain a plan object from the database
     * @param pid
     * @return planObject
     */
    public Plan getPlan(String pid){
    	Plan plan = null;
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            String id, exp, type, code;
            
            if(allrows.moveToFirst()){
                do{
                    id = allrows.getString(1);
                    type = allrows.getString(2);
                    exp = allrows.getString(3);
                    code = allrows.getString(4);
                }
                while(allrows.moveToNext());
                plan = new Plan(id, type, exp, code, activity);
            }
            mydb.close();
         }catch(Exception e){
        	 e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        	 plan = new Plan(pid, "", "", "PASSCODE", activity);
         }
    	return plan;
    }
}
