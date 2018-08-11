package com.example.nutmeg.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.nutmeg.DataStructures.RecipeEntry;
import com.example.nutmeg.IngredientsAdapter;
import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.DataStructures.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtils {
    private static String recipesURLString = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static ArrayList<RecipeEntry> getRecipies() throws IOException, JSONException {
        ArrayList<RecipeEntry> results = new ArrayList<>();
        String httpDownloadResult = getHttpResult(recipesURLString);
        JSONArray resultsJSON = new JSONArray(httpDownloadResult);
        results = parseJSON(resultsJSON);
        return results;
    }

    private static ArrayList<RecipeEntry> parseJSON(JSONArray resultsJSON) throws JSONException {
        ArrayList<RecipeEntry> results = new ArrayList<>();
        int id, servings;
        String imageURL, name;
        ArrayList<RecipeSteps> steps;
        ArrayList<RecipeIngredients> ingredients;
        int count = resultsJSON.length();
        for (int i=0; i < count; i++){
            JSONObject thisOne = new JSONObject();
            thisOne = resultsJSON.getJSONObject(i);
            id = (thisOne.has("id")) ?  thisOne.getInt("id") : -1;
            servings = (thisOne.has("servings")) ?  thisOne.getInt("servings") : -1;
            imageURL = (thisOne.has("imageURL")) ?  thisOne.getString("imageURL") : "none";
            name = (thisOne.has("name")) ?  thisOne.getString("name") : "none";
            steps
                    = (thisOne.has("steps")) ?  parseJSONSteps(thisOne.getJSONArray("steps")) : new ArrayList<RecipeSteps>();
            ingredients
                    = (thisOne.has("ingredients")) ?  parseJSONIngredients(thisOne.getJSONArray("ingredients")) : new ArrayList<RecipeIngredients>();

            RecipeEntry thisRecipe = new RecipeEntry(
                    id,
                    name,
                    steps,
                    ingredients,
                    imageURL,
                    servings);

            results.add(thisRecipe);
        }
        return results;
    }

    private static ArrayList<RecipeIngredients> parseJSONIngredients(JSONArray ingredients) throws JSONException {
        ArrayList<RecipeIngredients> results = new ArrayList<>();
        int count = ingredients.length();
        for (int i=0; i < count; i++){
            JSONObject thisOne = ingredients.getJSONObject(i);
            //TODO: do same checks as others
            //TODO: define defaults for all
            RecipeIngredients thisRecipeIngredients = new RecipeIngredients(
                    thisOne.getInt("quantity"),
                    thisOne.getString("measure"),
                    thisOne.getString("ingredient")
            );
            results.add(thisRecipeIngredients);
        }
        return results;

    }

    private static ArrayList<RecipeSteps> parseJSONSteps(JSONArray steps) throws JSONException {
        ArrayList<RecipeSteps> results = new ArrayList<>();
        int id;
        String shortDescription, description, videoURL, thumbnailURL;
        int count = steps.length();
        for (int i=0; i < count; i++){
            JSONObject thisOne = steps.getJSONObject(i);
            id = (thisOne.has("id")) ?  thisOne.getInt("id") : -1;
            shortDescription = (thisOne.has("shortDescription")) ?  thisOne.getString("shortDescription") : "";
            description = (thisOne.has("description")) ?  thisOne.getString("description") : "No Detailed Description";
            videoURL = (thisOne.has("videoURL")) ?  thisOne.getString("videoURL") : "No Video";
            thumbnailURL = (thisOne.has("thumbnailURL")) ?  thisOne.getString("thumbnailURL") : "No Thumbnail";

            RecipeSteps thisRecipeSteps = new RecipeSteps(
                    id,
                    0,                      //Will be populated later
                    shortDescription,
                    description,
                    videoURL,
                    thumbnailURL
            );
            results.add(thisRecipeSteps);
        }
        return results;
    }

    private static String getHttpResult(String urlString) throws IOException{

        //check urlString form
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //get String Response:
        //Create & Open HTTP connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext())
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        } finally {
            //Once complete or failed disconnect
            urlConnection.disconnect();
        }


    }


}
