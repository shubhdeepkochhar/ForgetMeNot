package com.example.shubhdeepk.forgetmenot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by shkochh on 9/27/2016.
 */

public class ToDoDbHandler extends SQLiteOpenHelper{

    public ToDoDbHandler(Context context) {
        super(context, "ForgetMeNot.db", null, 1);
    }

    public static final String TABLE = "todoTable";
    public static final String COL_ID = "todoId";
    public static final String COL_NAME = "todoName";
    public static final String COL_DUE = "todoDue";
    public static final String COL_PRIORITY = "todoPriority";
    public static final String COL_STATUS = "todoStatus";
    public static final String COL_NOTES = "todoNotes";



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " ( " +
                "" + COL_ID +" integer PRIMARY KEY, " +
                "" + COL_NAME + " text, " +
                "" + COL_DUE + " text, " +
                "" + COL_PRIORITY + " text, " +
                "" + COL_STATUS + " text, " +
                "" + COL_NOTES + " text) ";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if EXISTS " + TABLE;
        db.execSQL(sql);


        onCreate(db);
    }

    public boolean insertToDo(String toDo, String dueDate, String priority, String status, String notes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, toDo);
        cv.put(COL_DUE,dueDate);
        cv.put(COL_PRIORITY,priority);
        cv.put(COL_STATUS,status);
        cv.put(COL_NOTES,notes);

        db.insert(TABLE, null, cv);
        return true;
    }

    public Integer getID (String toDo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+
                TABLE +" WHERE "+ COL_NAME + " = '" + toDo + "' ", null );
        res.moveToFirst();
        return res.getInt(res.getColumnIndex(COL_ID));
    }


    public ArrayList<String> getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "
                + TABLE, null );
        res.moveToFirst();

        ArrayList<String> toDos = new ArrayList<String>();

        while(res.isAfterLast() == false){
            toDos.add(res.getString(res.getColumnIndex(COL_NAME)));
            res.moveToNext();
        }

        return toDos;
    }

    public Integer delete (String toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, COL_ID +" = ?", new String[]{Integer.toString(getID(toDo))});
    }

    public boolean updateToDo(String oldToDo, String newToDo, String dueDate, String priority, String status, String notes){
        delete(oldToDo);
        insertToDo(newToDo, dueDate, priority, status, notes);
        return true;
    }

    public String getDueDate(String toDo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE + " WHERE " + COL_NAME + " = '" + toDo + "' ", null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COL_DUE));
    }

    public String getPriority(String toDo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+ TABLE +" WHERE "+ COL_NAME + " = '"+ toDo +"'", null );
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COL_PRIORITY));
    }

    public String getStatus(String toDo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+ TABLE +" WHERE "+ COL_NAME + " = '"+ toDo +"' ", null );
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COL_STATUS));
    }

    public String getNotes(String toDo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+ TABLE +" WHERE "+ COL_NAME + " = '"+ toDo +"'", null );
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COL_NOTES));
    }





}
