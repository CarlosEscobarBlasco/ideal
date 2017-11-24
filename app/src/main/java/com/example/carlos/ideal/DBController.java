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

//    public long creteUser(String name, String email, String password, String description, Image image){
//        ContentValues values = new ContentValues();
//        values.put("name",name);
//        values.put("email",name);
//        values.put("password",name);
//        values.put("description",name);
//        return db.insert("user",null,values);
//    }

    public ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM user",null);
        while (cursor.moveToNext()){
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            String description = cursor.getString(5);
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

    public ArrayList<Idea> getIdeasFromTag(String tag){
        ArrayList<Idea> ideas = new ArrayList<>();
        String query = "SELECT * FROM idea WHERE tag =\""+ tag+"\"";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            int id = Integer.parseInt(cursor.getString(0));
            String title = cursor.getString(1);
            String short_description = cursor.getString(2);
            String full_description = cursor.getString(3);
//            String tag = cursor.getString(4);
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

    public int voteIdea(int value, int idea_id, int user_id) {
        try {
            String selectQuery ="SELECT id FROM votes WHERE voter ="+user_id+" AND idea="+idea_id;
            Cursor cursor = db.rawQuery(selectQuery,null);
            int id=0;
            if (cursor.moveToFirst())
            {
                do{
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                }while (cursor.moveToNext());
            }

            String query;

            if (id>0){
                query = "UPDATE votes SET value = value +"+value+" WHERE id = "+id;
                Log.d("#####if",query);

            }else{
                query = "INSERT INTO votes (voter, idea, value) VALUES("+ user_id+","+idea_id+","+value+")";
                Log.d("#####else",query);
            }

            String query2 = "UPDATE idea SET votes = votes +"+value+" WHERE id = "+idea_id;

            db.execSQL(query);
            db.execSQL(query2);
        }catch (Exception ex){
            return -1;
        }
        return 0;
    }

    private int getUserVotesOfTheIdea(int idea_id, int user_id){
        String query ="SELECT value FROM votes WHERE voter ="+user_id+" AND idea="+idea_id;
        Cursor cursor = db.rawQuery(query,null);

        int value=0;
        if (cursor.moveToFirst())
        {
            do{
                value = cursor.getInt(cursor.getColumnIndex("value"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("---",value+"");
        return value;
    }

    public boolean canUserUpVote(int idea_id, int user_id){
        return getUserVotesOfTheIdea(idea_id,user_id)<1;
    }

    public boolean canUserDownVote(int idea_id, int user_id){
        return getUserVotesOfTheIdea(idea_id,user_id)>-1;
    }

    public void deleteIdea(int idea_id){
        String query = "DELETE FROM idea WHERE id = "+idea_id;
        db.execSQL(query);
    }

    public long createUser(String name, String email, String password, String description, Image image) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        try {
            return db.insert("user", null, values);
        } catch (Exception e) {

        }
        return 0;
    }

    public long updateUser(int id, ContentValues value) {
        try {
            return db.update("user", value, "id = ?", new String[]{id+""});
        } catch (Exception e) {
            return -1;
        }
    }

    public User getUser(String name) {
        String query = "SELECT * FROM user WHERE name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        if (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(0));
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            String description = cursor.getString(5);
            User user = new User(id, name, password, email, description);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public int getIdOfUser(String name, String password){
        String query ="SELECT id FROM user WHERE name = ? AND password= ?";
        Cursor cursor = db.rawQuery(query,new String[]{name,password});
        int id=0;
        if (cursor.moveToFirst())
        {
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return id;
    }

    public User getUser(String name, String pwd) {

        User user = getUser(name);
        if (user == null) {
            return null;
        }
        if (user.getPassword() == null) {
            return null;
        }
        if (user.getPassword().equals(pwd)) {
            return user;
        }
        return null;
    }

    public User getUser(int id) {
        String query = "SELECT * FROM user WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"" + id});
        if (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            String description = cursor.getString(5);
            User user = new User(id, name, password, email, description);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public User getUserFromMail(String email) {
        String query ="SELECT * FROM user WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            String password = cursor.getString(3);
            String description = cursor.getString(5);
            User u = new User(id, name, password, email, description);
            cursor.close();
            return u;
        }
        cursor.close();
        return null;
    }

}
