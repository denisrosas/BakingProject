package com.example.android.bakingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingproject.Recipes.IngredientsFragment;
import com.example.android.bakingproject.Recipes.RecipeStep;
import com.example.android.bakingproject.Recipes.RecipeStepDetailsFragment;
import com.example.android.bakingproject.Recipes.RecipeStepListAdapter;
import com.example.android.bakingproject.Recipes.RecipeStepsFragment;

import java.util.ArrayList;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;
import static com.example.android.bakingproject.RecipeIngredientsListActivity.returnReadableIngredientList;

public class RecipeStepsListActivity extends AppCompatActivity implements RecipeStepListAdapter.OnStepClickListener, RecipeStepDetailsFragment.OnChangeStepClickListener {

    static final String RECIPE_ID = "RECIPE_ID";
    static final String STEP_ID = "STEP_ID";

    int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);

        Intent i = getIntent();
        recipeId = i.getIntExtra(RECIPE_ID, 0);

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        recipeStepsFragment.setRecipeName(globalRecipeDetailsList.get(recipeId).getName());
        recipeStepsFragment.setRecipeStepNames(getStepListFromGlobal(recipeId));

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_list_container, recipeStepsFragment)
                .commit();

        if(MainActivity.mTwoPaneMode){
            ArrayList<String> readableIngredientList = returnReadableIngredientList(globalRecipeDetailsList.
                    get(recipeId).getIngredientList());

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredientsList(readableIngredientList);

            fragmentManager.beginTransaction()
                    .add(R.id.steps_details_container, ingredientsFragment)
                    .commit();
        }

    }

    private ArrayList<String> getStepListFromGlobal(int recipeId){
        ArrayList<RecipeStep> recipeSteps = globalRecipeDetailsList.get(recipeId).getRecipeSteps();
        ArrayList<String> recipeStepList = new ArrayList<>();

        //Ingredients list will be the first item.
        recipeStepList.add(getString(R.string.ingredient_list));

        for(RecipeStep recipeStep : recipeSteps){
            recipeStepList.add(recipeStep.getShortDescription());
        }

        return recipeStepList;
    }

    @Override
    public void onStepSelected(int position) {

        if(MainActivity.mTwoPaneMode){

            if(position==0){

                ArrayList<String> readableIngredientList = returnReadableIngredientList(globalRecipeDetailsList.
                        get(recipeId).getIngredientList());

                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setIngredientsList(readableIngredientList);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_details_container, ingredientsFragment)
                        .commit();
            } else {

                int stepId = position-1; //the position 0 is the ingredient list. position 1 == step 0

                RecipeStepDetailsFragment stepDetailsFragment = new RecipeStepDetailsFragment();
                stepDetailsFragment.setRecipeStep(globalRecipeDetailsList.get(recipeId).getRecipeSteps().get(stepId));
                stepDetailsFragment.setRecipeStepIds(recipeId, stepId);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_details_container, stepDetailsFragment)
                        .commit();

            }

        } else {
            //TODO - se position for 0, carregar a acitivy de ingredientes. se 1 ou mais, carregar o RecipeStepDetailActivity
            if(position==0){
                Intent intent = new Intent(this, RecipeIngredientsListActivity.class);
                intent.putExtra(RECIPE_ID, (recipeId));
                startActivity(intent);
            } else {
                //Toast.makeText(this, "posicao: " + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, RecipeStepDetailActivity.class);
                intent.putExtra(STEP_ID, (position-1));
                intent.putExtra(RECIPE_ID, (recipeId));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onPrevNextStepSelected(int recipeId, int newStepId) {
        if(MainActivity.mTwoPaneMode){
            RecipeStepDetailsFragment stepDetailsFragment = new RecipeStepDetailsFragment();
            stepDetailsFragment.setRecipeStep(globalRecipeDetailsList.get(recipeId).getRecipeSteps().get(newStepId));
            stepDetailsFragment.setRecipeStepIds(recipeId, newStepId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_details_container, stepDetailsFragment)
                    .commit();
        }
    }
}
