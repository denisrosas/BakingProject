package com.example.android.bakingproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.android.bakingproject.Recipes.RecipeStepDetailsFragment;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailsFragment.OnChangeStepClickListener{

    static final String RECIPE_ID = "RECIPE_ID";
    static final String STEP_ID = "STEP_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if in landscape mode, video must play in Full Screen. So both the activity and
        // Exoplayer will be in FullScreen
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_recipe_step_detail);

        if(getActionBar()!=null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra(RECIPE_ID, 0);
        int stepId = intent.getIntExtra(STEP_ID, 0);

        RecipeStepDetailsFragment stepDetailsFragment = new RecipeStepDetailsFragment();
        stepDetailsFragment.setRecipeStep(globalRecipeDetailsList.get(recipeId).getRecipeSteps().get(stepId));
        stepDetailsFragment.setRecipeStepIds(recipeId, stepId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.steps_details_container, stepDetailsFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrevNextStepSelected(int recipeId, int newStepId) {

        RecipeStepDetailsFragment stepDetailsFragment = new RecipeStepDetailsFragment();
        stepDetailsFragment.setRecipeStep(globalRecipeDetailsList.get(recipeId).getRecipeSteps().get(newStepId));
        stepDetailsFragment.setRecipeStepIds(recipeId, newStepId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.steps_details_container, stepDetailsFragment)
                .commit();
    }

}
