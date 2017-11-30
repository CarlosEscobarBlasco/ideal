package com.example.carlos.ideal;

/**
 * Created by carlos on 29/11/2017.
 */

public class Comment {
    private int id;
    private int owner_id;
    private int idea_id;
    private String description;

    public Comment(int id, int owner_id, int idea_id, String description) {
        this.id = id;
        this.owner_id = owner_id;
        this.idea_id = idea_id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public int getIdea_id() {
        return idea_id;
    }

    public String getDescription() {
        return description;
    }
}
