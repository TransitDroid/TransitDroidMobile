package com.coboltforge.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coboltforge.domain.Plan;
import com.coboltforge.domain.User;
import com.coboltforge.slidemenuexample.MainActivity;

public class UserTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "USERSDB.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "USER";
    private MainActivity activity;
    
    public UserTDG(MainActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
 // CREATE TABLE IF NOT EXISTS 
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, TRANSIT_ID TEXT, USERNAME TEXT, PASSWORD TEXT, NAME TEXT, PLAN_ID TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void initialInsertIntoTable(User user){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(TRANSIT_ID, USERNAME, PASSWORD, NAME, PLAN_ID) VALUES('" + user.getId() + "', '" + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getName() + "', '" + user.getPlan().getId() + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    /*public void updateUsername(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET USERNAME = '" + user.getUsername() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updateName(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET NAME = '" + user.getName() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updatePassword(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET PASSWORD = '" + user.getPassword() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updatePlan(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET PLAN_ID = '" + user.getPlan().getId() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }*/
    
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
    
    public void setUser(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            String id, username, password, name, planId;
            
            if(allrows.moveToFirst()){
                do{
                    id = allrows.getString(1);
                    username = allrows.getString(2);
                    password = allrows.getString(3);
                    name = allrows.getString(4);
                    planId = allrows.getString(5);
                }
                while(allrows.moveToNext());
                mydb.close();
                
                Plan plan = (new PlanTDG(activity)).getPlan(planId);
                user = new User(name, id, plan, username, password);
            }
         }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        	 //user = User.getInstance(activity);
        	 e.printStackTrace();
         }
    }
}
