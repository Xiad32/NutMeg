package com.example.nutmeg;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipesActivity extends AppCompatActivity
            implements MessageDelayer.DelayerCallback
{
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        RecipesList recipiesList = new RecipesList();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.list_fragment, recipiesList).commit();

        getIdlingResource();
        MessageDelayer.processMessage("", this, mIdlingResource);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MessageDelayer.processMessage("", this, mIdlingResource);
    }

    @Override
    public void onDone(String text) {

    }



    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
            return mIdlingResource;
        }





}
