package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.bakingproject.Network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RecipesFromJson extends AsyncTaskLoader<ArrayList<RecipeDetails>> {

    private Context activityContext;

    //list of JSON root items
    private final static String JSON_RECIPE_ID = "id";
    private final static String JSON_RECIPE_NAME = "name";
    private final static String JSON_RECIPE_INGREDIENTS = "ingredients";
    private final static String JSON_RECIPE_STEPS = "steps";
    private final static String JSON_RECIPE_SERVINGS = "servings";
    private final static String JSON_RECIPE_IMAGE = "image";

    //list of JSON Ingredient Items
    private final static String JSON_INGREDIENT_QUANTITY = "quantity";
    private final static String JSON_INGREDIENT_MEASURE = "measure";
    private final static String JSON_INGREDIENT_INGREDIENT_NAME = "ingredient";

    //list of JSON Step Items
    private final static String JSON_STEP_ID = "id";
    private final static String JSON_STEP_SHORTDESCR = "shortDescription";
    private final static String JSON_STEP_DESCRIPTION = "description";
    private final static String JSON_STEP_VIDEO_URL = "videoURL";
    private final static String JSON_STEP_THUMBNAIL_URL = "thumbnailURL";

    public RecipesFromJson(Context context) {
        super(context);
        activityContext = context;
    }

    @Override
    public ArrayList<RecipeDetails> loadInBackground() {

        //build the
        URL url = NetworkUtils.buildUrlRecipesList();

        Log.i("denis", "url: "+url.toString());

        String jsonToParse = "";

        try {
            jsonToParse = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseJsonToArrayList(jsonToParse);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private ArrayList<RecipeDetails> parseJsonToArrayList(String jsonRecipes){

        JSONObject jsonObject;
        JSONArray jsonArray;
        ArrayList<RecipeDetails> recipeDetailsList = new ArrayList<RecipeDetails>();

        try {
            jsonArray = new JSONArray(jsonRecipes);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        if(jsonArray.length()>0){

            for(int index=0; index<jsonArray.length(); index++){
                try {
                    jsonObject = jsonArray.getJSONObject(index);

                    RecipeDetails recipeDetails = new RecipeDetails(
                            jsonObject.getInt(JSON_RECIPE_ID),
                            jsonObject.getString(JSON_RECIPE_NAME),
                            getIngredientsListFromJson(jsonObject.getJSONArray(JSON_RECIPE_INGREDIENTS)),
                            getRecipeStepsListFromJson(jsonObject.getJSONArray(JSON_RECIPE_STEPS)),
                            jsonObject.getInt(JSON_RECIPE_SERVINGS),
                            jsonObject.getString(JSON_RECIPE_IMAGE));

                    recipeDetailsList.add(recipeDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return recipeDetailsList;
    }

    private ArrayList<Ingredient> getIngredientsListFromJson(JSONArray jsonArray){

        JSONObject jsonObject;
        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

        for (int i=0; i<jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);
                Ingredient ingredient = new Ingredient(
                        (float)jsonObject.getDouble(JSON_INGREDIENT_QUANTITY),
                        jsonObject.getString(JSON_INGREDIENT_MEASURE),
                        jsonObject.getString(JSON_INGREDIENT_INGREDIENT_NAME)
                );

                ingredientList.add(ingredient);

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return ingredientList;
    }

    private ArrayList<RecipeStep> getRecipeStepsListFromJson(JSONArray jsonArray){

        JSONObject jsonObject;
        ArrayList<RecipeStep> recipeStepList = new ArrayList<RecipeStep>();

        for (int i=0; i<jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);
                RecipeStep recipeStep = new RecipeStep(
                        jsonObject.getInt(JSON_STEP_ID),
                        jsonObject.getString(JSON_STEP_SHORTDESCR),
                        jsonObject.getString(JSON_STEP_DESCRIPTION),
                        jsonObject.getString(JSON_STEP_VIDEO_URL),
                        jsonObject.getString(JSON_STEP_THUMBNAIL_URL)
                );

                recipeStepList.add(recipeStep);

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return recipeStepList;
    }
}
