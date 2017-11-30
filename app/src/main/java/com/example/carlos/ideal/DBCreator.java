package com.example.carlos.ideal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by carlos on 15/11/2017.
 */

public class DBCreator extends SQLiteOpenHelper {

    private String sqlCreate = "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT," +
            "email TEXT," +
            "password TEXT," +
            "photo BLOB," +
            "description TEXT);";

    private String ideaQuery = "CREATE TABLE idea (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title TEXT," +
            "short_description TEXT," +
            "full_description TEXT," +
            "tag TEXT," +
            "votes INTEGER," +
            "owner INTEGER," +
            "FOREIGN KEY(owner) REFERENCES user(id));";

    private String contQuery =  "CREATE TABLE contributors (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "contributor INTEGER," +
            "idea INTEGER," +
            "FOREIGN KEY(contributor) REFERENCES user(id)," +
            "FOREIGN KEY(idea) REFERENCES idea(id));";

    private String reqQuery =  "CREATE TABLE requesters (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "requester INTEGER," +
            "idea INTEGER," +
            "FOREIGN KEY(requester) REFERENCES user(id)," +
            "FOREIGN KEY(idea) REFERENCES idea(id));";

    private String votesQuery =  "CREATE TABLE votes (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "voter INTEGER," +
            "idea INTEGER," +
            "value INTEGER," +
            "FOREIGN KEY(voter) REFERENCES user(id)," +
            "FOREIGN KEY(idea) REFERENCES idea(id));";

    private String commentQuery =  "CREATE TABLE comment (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user INTEGER," +
            "idea INTEGER," +
            "description TEXT," +
            "FOREIGN KEY(user) REFERENCES user(id)," +
            "FOREIGN KEY(idea) REFERENCES idea(id));";

    private String createRootUser = "INSERT INTO user(name,email,password,description)" +
            "VALUES(\"root\",\"root@mail.com\",\"password\",\"description\")";


    public DBCreator(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
        db.execSQL(ideaQuery);
        db.execSQL(contQuery);
        db.execSQL(reqQuery);
        db.execSQL(votesQuery);
        db.execSQL(createRootUser);
        db.execSQL(commentQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sqlDropTable1 = "DROP TABLE IF EXISTS user;";
        db.execSQL(sqlDropTable1);
        String sqlDropTable2 = "DROP TABLE IF EXISTS idea;";
        db.execSQL(sqlDropTable2);
        String sqlDropTable3 = "DROP TABLE IF EXISTS contributors;";
        db.execSQL(sqlDropTable3);
        String sqlDropTable4 = "DROP TABLE IF EXISTS votes;";
        db.execSQL(sqlDropTable4);
        String sqlDropTable5 = "DROP TABLE IF EXISTS requesters;";
        db.execSQL(sqlDropTable5);
        String sqlDropTable6 = "DROP TABLE IF EXISTS comment;";
        db.execSQL(sqlDropTable6);

        db.execSQL(sqlCreate);
        db.execSQL(ideaQuery);
        db.execSQL(contQuery);
        db.execSQL(reqQuery);
        db.execSQL(votesQuery);
        db.execSQL(commentQuery);
        db.execSQL(createRootUser);
    }
}
