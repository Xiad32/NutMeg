package com.example.nutmeg;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.nutmeg.Widget.DataDbHelper;

import butterknife.BindView;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsProvider extends AppWidgetProvider{

    @BindView(R.id.ingredients_listGV)
    GridView ingredients_listGV;
    @BindView(R.id.launchRecipe)
    Button launchRecipe;
    @BindView(R.id.widgetArrowPrev)
    ImageView widgetArrowPrev;
    @BindView(R.id.widgetArrowNext)
    ImageView widgetArrowNext;
    @BindView(R.id.widget_recipe_TV)
    TextView widgetRecipeTV;

    private static final String PrevActionClick = "previousArrowClicked";
    private static final String NextActionClick = "nextArrowClicked";

    private int location = 0;
    private static DataDbHelper dbHelper;
    private SQLiteDatabase mDB;
    private boolean arrowPrev; //true prev, false next

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_provider);
        dbHelper = new DataDbHelper(context);
        mDB = dbHelper.getReadableDatabase();
        location = PreferenceManager.getDefaultSharedPreferences(context).getInt(MainActivity.SP_RECIPE_INDICATOR, 0);
        Cursor recipies = mDB.query("recipes", null, null, null, null, null, null);
        if (recipies.getCount() <= 0) return;        //Dont try anything crazy now!
        if (recipies.getCount() < location)
            location = recipies.getCount()-1;

        Intent launchIntent = new Intent(context, MainActivity.class);
        //DONE: need to add extras for the recipe to view.
        if (recipies.moveToPosition(location)) {
            launchIntent.putExtra(RecipesList.RECIPE_KEY, recipies.getInt(recipies.getColumnIndex("id")));
        }
        else {
            launchIntent.putExtra(RecipesList.RECIPE_KEY, -1); //-1 indicates first entry //TODO: reflect in main activity
        }
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.launchRecipe, pendingIntent);

        Intent remoteServiceIntent = new Intent(context, WidgetGridService.class);
        views.setRemoteAdapter(R.id.ingredients_listGV, remoteServiceIntent);


        recipies.moveToPosition(location);
        views.setTextViewText(R.id.widget_recipe_TV, recipies.getString(recipies.getColumnIndex("name")));
        if(location == 0) {
            arrowPrev = true;
            setArrowInactive(arrowPrev, views);
            setArrowActive(!arrowPrev, views, context);
        }
        else if (location == recipies.getCount() - 1)
        {
            arrowPrev = false;
            setArrowInactive(arrowPrev, views);
            setArrowActive(!arrowPrev, views,context);
        }
        else{
            setArrowActive(arrowPrev, views, context);
            setArrowActive(!arrowPrev, views, context);
        }
        // Construct the RemoteViews object


        //DONE: set the previous/next to invisible based on recipe

        //DONE:set the recipe name based on the selected recipe

        //DONE: handle empty list


        // Instruct the widget manager to update the widget
//        recipies.close();
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private void setArrowInactive(boolean arrowPrev, RemoteViews views) {
        if(arrowPrev)
            views.setViewVisibility(R.id.widgetArrowPrev, View.INVISIBLE);
        else
            views.setViewVisibility(R.id.widgetArrowNext, View.INVISIBLE);
    }
    private void setArrowActive(boolean arrowPrev, RemoteViews views, Context context) {
        if(arrowPrev){
            views.setViewVisibility(R.id.widgetArrowPrev, View.VISIBLE);
            //DONE: create a pending intent to decrement location
            Intent onClickIntent = new Intent(context, getClass());
            onClickIntent.setAction(PrevActionClick);
            PendingIntent onClickPendingIntent =
                    PendingIntent.getBroadcast(context, 0, onClickIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetArrowPrev, onClickPendingIntent);
        }
        else{
            views.setViewVisibility(R.id.widgetArrowNext, View.VISIBLE);
            //DONE: create a pending intent to decrement location
            Intent onClickIntent = new Intent(context, getClass());
            onClickIntent.setAction(NextActionClick);
            PendingIntent onClickPendingIntent =
                    PendingIntent.getBroadcast(context, 0, onClickIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetArrowNext, onClickPendingIntent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
        String intentAction = intent.getAction();
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_provider);
        location = PreferenceManager.getDefaultSharedPreferences(context).getInt(MainActivity.SP_RECIPE_INDICATOR, 0);
        if(PrevActionClick.equals(intentAction))
        {
            location--;
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(MainActivity.SP_RECIPE_INDICATOR, location).apply();
            setArrowActive(true,views, context);
        }
        if(NextActionClick.equals(intentAction))
        {
            location++;
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(MainActivity.SP_RECIPE_INDICATOR, location).apply();
            setArrowActive(false,views, context);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, IngredientsProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.ingredients_listGV);

        onUpdate( context, appWidgetManager, ids);
        super.onReceive(context, intent);
    }
}

