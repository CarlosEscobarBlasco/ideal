package com.example.carlos.ideal;

import android.content.Context;

/**
 * Created by carlos on 23/11/2017.
 */

public class AppController {

    private static AppController instance=null;
    private int user_id;

    public static AppController getInstance(){
        if(instance==null){
            instance = new AppController();
            return instance;
        }else {
            return instance;
        }
    }

    private AppController() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
