package com.example.nutmeg.DataStructures;

import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.DataStructures.RecipeSteps;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeEntry implements Serializable {
    private  int id;
    private  String name;
    private ArrayList<RecipeSteps> steps;
    private  ArrayList<RecipeIngredients> ingredients;
    private  String image;
    private  int servings;

    public RecipeEntry(int Id, String Name, ArrayList<RecipeSteps> Steps, ArrayList<RecipeIngredients> Ingredients, String ImageURL, int Servings) {
        id = Id;
        name = Name;
        steps = Steps;
        ingredients = Ingredients;
        image = ImageURL;
        servings = Servings;
    }

    //public RecipeEntry(){};

    public static void setId(int id) {
        id = id;
    }

    public  void setImage(String image) {
        image = image;
    }

    public  void setIngredients(ArrayList<RecipeIngredients> ingredients) {
        ingredients = ingredients;
    }

    public  void setName(String name) {
        name = name;
    }

    public  void setServings(int servings) {
        servings = servings;
    }

    public  void setSteps(ArrayList<RecipeSteps> steps) {
        steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public ArrayList<RecipeIngredients> getIngredients() {
        return ingredients;
    }

    public ArrayList<RecipeSteps> getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }

    public String getImageURL() {
        return image;
    }

    public String getName() {
        return name;
    }

}
