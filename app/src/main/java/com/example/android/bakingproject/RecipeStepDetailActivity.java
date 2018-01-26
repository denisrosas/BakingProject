package com.example.android.bakingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingproject.Recipes.RecipeStepDetailsFragment;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;

public class RecipeStepDetailActivity extends AppCompatActivity {

    static final String RECIPE_ID = "RECIPE_ID";
    static final String STEP_ID = "STEP_ID";
    static final String STEP_SHORT_DESCRIPTION = "STEP_SHORT_DESCRIPTION";
    static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";
    static final String STEP_VIDEO_URL = "STEP_VIDEO_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra(RECIPE_ID, 0);
        int stepId = intent.getIntExtra(STEP_ID, 0);

        RecipeStepDetailsFragment stepDetailsFragment = new RecipeStepDetailsFragment();
        stepDetailsFragment.setRecipeStep(globalRecipeDetailsList.get(recipeId).getRecipeSteps().get(stepId));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.steps_details_container, stepDetailsFragment)
                .commit();
    }

}
