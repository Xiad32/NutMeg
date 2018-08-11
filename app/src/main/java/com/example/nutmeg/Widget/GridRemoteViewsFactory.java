package com.example.nutmeg.Widget;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.nutmeg.DBUtils.IngredientsDB;
import com.example.nutmeg.DBUtils.RecipesDesc;
import com.example.nutmeg.DataStructures.RecipeEntry;
import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.MainActivity;
import com.example.nutmeg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private static DataDbHelper dbHelper;
    private static SQLiteDatabase mDB;

    Cursor ingredients;


    @Override
    public void onCreate() {
//        ingredientsDB = IngredientsDB.getsInstance(mContext);
    }

    public GridRemoteViewsFactory(Context context){

        mContext = context;
    }

    @Override
    public int getCount() {
        if (ingredients == null)
            return 0;
        return ingredients.getCount();
    }

    @Override
    public void onDataSetChanged() {
        //DONE: if I couldnt pull this off, call a function that gets all that is in a database

        int recipeID = -1;
        dbHelper = new DataDbHelper(mContext);
        mDB = dbHelper.getReadableDatabase();

        int location = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(MainActivity.SP_RECIPE_INDICATOR, 0);
        //get RecipieID at this location:
        Cursor recipies = mDB.query("recipes", null, null, null, null, null, null);
        if((recipies != null || recipies.getCount() > 0) && recipies.moveToPosition(location)) {
                recipeID = recipies.getInt(recipies.getColumnIndex("id"));
        }

        recipies.close();
        //Get the ingredients:
        if (recipeID != -1)
        {
            ingredients = mDB.query("ingredients",
                null,
                "recipeID = ?",
                new String[] {String.valueOf(recipeID)},
                null,
                null,
                null);
//            if (ingredients != null)
//                ingredients.close();
        }



    }


    @Override
    public RemoteViews getViewAt(int position) {

        if (ingredients == null || (ingredients.getCount() == 0) )
            return null;
        ingredients.moveToPosition(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);

        //Populate the view
        views.setTextViewText(R.id.quanitityTV, String.valueOf(ingredients.getFloat(ingredients.getColumnIndex("quantity"))));
        views.setTextViewText(R.id.measureTV, ingredients.getString(ingredients.getColumnIndex("measure")));
        views.setTextViewText(R.id.ingredientsTV, ingredients.getString(ingredients.getColumnIndex("ingredient")));


        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public void onDestroy() {
            ingredients.close();
    }
}
