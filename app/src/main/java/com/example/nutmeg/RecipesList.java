package com.example.nutmeg;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nutmeg.DBUtils.IngredientsDB;
import com.example.nutmeg.DBUtils.RecipesDesc;
import com.example.nutmeg.DataStructures.RecipeEntry;
import com.example.nutmeg.DataStructures.RecipeSteps;
import com.example.nutmeg.Utils.AppExecutors;
import com.example.nutmeg.Utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RecipesList extends Fragment
        implements RecipesAdapter.ClickListener
{
    //Public Constants:
    public static String RECIPE_KEY = "recipeKey";

    //private static ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private static ArrayList<RecipeEntry> mData;
    private RecipesAdapter mAdapter;
    private IngredientsDB ingredientsDB;
    private boolean tablet;
    private GridLayoutManager gridLayoutManager;



    @Nullable
    @BindView(R.id.recipiesRV) RecyclerView listView;
    @Nullable
    @BindView(R.id.recipies_grid_RV) RecyclerView listGridView;
    @BindView(R.id.recipiesPB) ProgressBar progressBar;

    public RecipesList() {
        // Required empty public constructor
    }





    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mData == null){
            GetData getData = new GetData();
            getData.execute();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onStart();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipies_list, container, false);
        ButterKnife.bind(this, view);
        if (listGridView != null)
            tablet = true;

        mData = new ArrayList<>();
        mAdapter = new RecipesAdapter(getContext(), mData, this);

        ingredientsDB = IngredientsDB.getsInstance(getActivity().getApplicationContext());

        //DONE: AsyncTask to download and parse the available recipes:

        //DONE: populate a RecyclerView with an image and a onClickListener
        if (!tablet) {
            linearLayoutManager = new LinearLayoutManager(getContext());
            listView.setLayoutManager(linearLayoutManager);
            listView.setAdapter(mAdapter);
        }
        else {//tablet
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            gridLayoutManager = new GridLayoutManager(getContext(), 3);
            listGridView.setLayoutManager(gridLayoutManager);
            listGridView.setAdapter(mAdapter);

        }


        GetData getData = new GetData();
        getData.execute();


        return view;
    }



    @Override
    public void onRecipeClick(int position) {
        //TODO: launch the activity.
        Intent launchActivity = new Intent(getActivity(), MainActivity.class);
        launchActivity.putExtra(RECIPE_KEY, mData.get(position).getId());
        startActivity(launchActivity);
    }

    public class GetData extends AsyncTask<Void, Void, ArrayList<RecipeEntry> > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show the progress bar while downloading
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(ArrayList<RecipeEntry> recipesArrayList) {
            super.onPostExecute(recipesArrayList);
            if(recipesArrayList != null) //use the last stored data
            {
                mData = recipesArrayList;
                mAdapter.updateData(mData);
                storeIngredientsinDB();
            }
            progressBar.setVisibility(View.INVISIBLE);
            //TESTING:

        }

        @Override
        protected ArrayList<RecipeEntry> doInBackground(Void... voids) {
            try {
                return NetworkUtils.getRecipies();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void storeIngredientsinDB() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //Reset Database entries
                ingredientsDB.ingredientsDao().deleteAllIngredient();
                ingredientsDB.recipesDescDao().deleteAllRecipes();
                ingredientsDB.recipeStepsDao().deleteAllSteps();
                //Add all ingredients for all recipes (the filter is based on the recipe ID)
                if(mData == null)
                    return;
                for(int i =0; i<mData.size(); i++){

                    //Add all recipes:
                        ingredientsDB.recipesDescDao().insetRecipe(
                                new RecipesDesc(
                                        mData.get(i).getId(),
                                        mData.get(i).getName(),
                                        mData.get(i).getImageURL(),
                                        mData.get(i).getServings() ));

                    //Ingredients:
                    for (int j=0; j<mData.get(i).getIngredients().size(); j++)
                        ingredientsDB.ingredientsDao().insetIngredient(
                                mData.get(i).getIngredients().get(j).toIngredientEntry(
                                        mData.get(i).getId() ) );

                    //Steps:
                    for (int j=0; j<mData.get(i).getSteps().size(); j++) {
                        //Before stuffing it into the DB update the reciepe reference
                        RecipeSteps thisOne = mData.get(i).getSteps().get(j);
                        thisOne.setRecipeID(mData.get(i).getId());
                        ingredientsDB.recipeStepsDao().insetStep(thisOne);
                    }
            }
        };});

    }
}
