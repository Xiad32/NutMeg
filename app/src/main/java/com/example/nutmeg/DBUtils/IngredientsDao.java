package com.example.nutmeg.DBUtils;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.nutmeg.DataStructures.RecipeIngredients;
import java.util.List;

@Dao
public interface IngredientsDao {

    @Query("SELECT * FROM ingredients")
    LiveData<List<RecipeIngredients>> getAllIngredients();

    @Query("SELECT * FROM ingredients WHERE recipeID = :recipeID")
    LiveData<List<RecipeIngredients>> getIngredientsFor(int recipeID);

    @Query("SELECT COUNT(*) FROM ingredients WHERE recipeID = :recipeID")
    int getDataCount(int recipeID);

    @Insert
    void insetIngredient(IngredientEntry recipeIngredient);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(IngredientEntry recipeIngredient);

    @Delete
    void deleteIngredient(IngredientEntry recipeIngredient);

    @Query("DELETE FROM ingredients")
    void deleteAllIngredient();
}
