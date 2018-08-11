package com.example.nutmeg.DataStructures;

import com.example.nutmeg.DBUtils.IngredientEntry;

import java.io.Serializable;

public class RecipeIngredients implements Serializable{
    private float quantity;
    private String measure;
    private String ingredient;

    public RecipeIngredients(float quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public IngredientEntry toIngredientEntry(int recipeID) {
        return new IngredientEntry(recipeID, quantity, measure, ingredient);
    }

    //Usd to translate enums to actual text
//    public String measurements[] = {
//            "Cup",
//            "Tablespoon",
//            "Teaspoon",
//            "K",
//            "gram",
//            "ounce"
//    };

//    public enum MEASURE{
//        CUP,
//        TBLSP,
//        TSP,
//        K,
//        G,
//        OZ
//    }
}
