package com.example.nutmeg.DBUtils;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.DataStructures.RecipeSteps;

import java.util.List;

@Dao
public interface RecipeStepsDao {

    @Query("SELECT * FROM steps")
    LiveData<List<RecipeSteps>> getAllSteps();

    @Query("SELECT * FROM steps WHERE recipeID = :recipeID")
    LiveData<List<RecipeSteps>> getStepsFor(int recipeID);

    @Query("SELECT COUNT(*) FROM steps WHERE recipeID = :recipeID")
    int getDataCount(int recipeID);

    @Insert
    void insetStep(RecipeSteps recipeSteps);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(RecipeSteps recipeSteps);

    @Delete
    void deleteStep(RecipeSteps recipeSteps);

    @Query("DELETE FROM steps")
    void deleteAllSteps();
}
