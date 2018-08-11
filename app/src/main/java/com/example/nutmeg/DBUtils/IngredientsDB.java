package com.example.nutmeg.DBUtils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.nutmeg.DataStructures.RecipeSteps;
import com.example.nutmeg.RecipesList;

@Database(entities =  {IngredientEntry.class, RecipesDesc.class, RecipeSteps.class}, version = 1, exportSchema = false)
public abstract class IngredientsDB extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "ingredients_db";
    private static IngredientsDB sInstance;

    public static IngredientsDB getsInstance(Context context){
        if (sInstance == null)
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        IngredientsDB.class,
                        IngredientsDB.DATABASE_NAME)
                .build();
            }
        return sInstance;
    }

    public abstract IngredientsDao ingredientsDao();

    public abstract RecipesDescDao recipesDescDao();

    public abstract RecipeStepsDao recipeStepsDao();

}
