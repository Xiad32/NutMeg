package com.example.nutmeg.DBUtils;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class IngredientEntry {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    private int recipeID;
    private float quantity;
    private String measure;
    private String ingredient;

    public IngredientEntry(int recipeID, float quantity, String measure, String ingredient) {
        this.recipeID = recipeID;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public IngredientEntry(int primaryKey, int RecipeID, float Quantity, String Measure, String Ingredient) {
        this.recipeID = RecipeID;
        this.quantity = Quantity;
        this.measure = Measure;
        this.ingredient = Ingredient;
        this.primaryKey = primaryKey;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }



    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }







}
