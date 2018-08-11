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
public interface RecipesDescDao {

    @Query("SELECT * FROM recipes")
    LiveData<List<RecipesDesc>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :recipeID")
    LiveData<List<RecipesDesc>> getThisRecipe(int recipeID);

    @Query("SELECT COUNT(*) FROM recipes")
    int getDataCount();

    @Insert
    void insetRecipe(RecipesDesc recipesDesc);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipesDesc recipesDesc);

    @Delete
    void deleteRecipe(RecipesDesc recipesDesc);

    @Query("DELETE FROM recipes")
    void deleteAllRecipes();
}
