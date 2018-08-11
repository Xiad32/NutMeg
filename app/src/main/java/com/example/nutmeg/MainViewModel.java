package com.example.nutmeg;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.nutmeg.DBUtils.IngredientEntry;
import com.example.nutmeg.DBUtils.IngredientsDB;
import com.example.nutmeg.DBUtils.RecipesDesc;
import com.example.nutmeg.DataStructures.RecipeIngredients;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<RecipesDesc>> recipes;
    private LiveData<List<RecipeIngredients>> ingredients;

    public MainViewModel(@NonNull Application application){
        super(application);
        IngredientsDB ingredientsDB =
                IngredientsDB.getsInstance(this.getApplication());
        recipes = ingredientsDB.recipesDescDao().getAllRecipes();
        ingredients = ingredientsDB.ingredientsDao().getAllIngredients();
    }

    public LiveData<List<RecipeIngredients>> getIngredients() {
        return ingredients;
    }

    public LiveData<List<RecipesDesc>> getRecipes() {
        return recipes;
    }


}
