package com.example.carlos.ideal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.util.Log;

import java.util.ArrayList;


public class DBController {
    private SQLiteDatabase db;
    private static DBController instance=null;

    public static DBController getInstance(Context context){
        if(instance==null){
            instance = new DBController(context);
            return instance;
        }else {
            return instance;
        }
    }

    private DBController(Context context) {
        DBCreator creator = new DBCreator(context, "database.db", null, 3);
        db = creator.getWritableDatabase();
    }

    public long creteUser(String name, String email, String password, String description, Image image){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("email",name);
        values.put("password",name);
        values.put("description",name);
        return db.insert("user",null,values);
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM user",null);
        while (cursor.moveToNext()){
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            String description = cursor.getString(5);
            Log.d("MyApp",name);
            users.add(new User(id, name,password,email,description));
        }
        cursor.close();
        return users;
    }

    public long createIdea(String title, String short_description, String tag, String full_description, int owner_id){
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("short_description",short_description);
        values.put("full_description",full_description);
        values.put("tag",tag);
        values.put("votes",0);
        values.put("owner",owner_id);
        return db.insert("idea",null,values);
    }

    public ArrayList<Idea> getIdeasFromUser(int user_id){
        ArrayList<Idea> ideas = new ArrayList<>();
        String query = "SELECT * FROM idea WHERE owner ="+ user_id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            int id = Integer.parseInt(cursor.getString(0));
            String title = cursor.getString(1);
            String short_description = cursor.getString(2);
            String full_description = cursor.getString(3);
            String tag = cursor.getString(4);
            int votes = Integer.parseInt(cursor.getString(5));
            int owner_id = Integer.parseInt(cursor.getString(6));
            ideas.add(new Idea(id, title,short_description,full_description,tag,votes,owner_id));
        }
        cursor.close();
        return ideas;
    }


    public Idea getIdeaInformation(int idea_id) {
        String query ="SELECT * FROM idea WHERE id ="+idea_id;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToNext();
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String short_description = cursor.getString(2);
        String full_description = cursor.getString(3);
        String tag = cursor.getString(4);
        int votes = Integer.parseInt(cursor.getString(5));
        int owner_id = Integer.parseInt(cursor.getString(6));
        cursor.close();
        return new Idea(id,title,short_description,full_description,tag,votes,owner_id);
    }

    public int updateIdea(int ideaId, String title, String shortDescription, String tag, String fullDescription) {
        try {
            String query = "UPDATE idea SET title =\"" + title +
                    "\",short_description=\"" + shortDescription +
                    "\",full_description=\"" + fullDescription +
                    "\",tag=\"" + tag +
                    "\" WHERE id=" + ideaId;

            db.execSQL(query);
        }catch (Exception ex){
            return -1;
        }
        return 0;

    }
}
