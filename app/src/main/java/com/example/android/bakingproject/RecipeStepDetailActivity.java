package com.example.android.bakingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingproject.Recipes.RecipeStep;

public class RecipeStepDetailActivity extends AppCompatActivity {

    static final String RECIPE_ID = "RECIPE_ID";
    static final String STEP_ID = "STEP_ID";
    RecipeStep recipeStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent intent = getIntent();
        int recipeID = intent.getIntExtra(RECIPE_ID, 0);
        int stepId = intent.getIntExtra(STEP_ID, 0);

        recipeStep = MainActivity.globalRecipeDetailsList.get(recipeID).getRecipeSteps().get(stepId);

        Log.i("denis", "recipe Description: "+recipeStep.getDescription()+"\n videoURL: "+recipeStep.getVideoURL());
    }
}
