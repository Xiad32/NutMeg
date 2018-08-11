package com.example.nutmeg.DBUtils;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipes")
public class RecipesDesc {
    @PrimaryKey
    private  int id;
    private  String name;
    private  String image;
    private  int servings;


    public RecipesDesc(int id, String name, String image, int servings) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.servings=servings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
