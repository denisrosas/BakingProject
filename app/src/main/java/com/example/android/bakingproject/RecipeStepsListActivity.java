package com.example.android.bakingproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingproject.Recipes.RecipeStep;
import com.example.android.bakingproject.Recipes.RecipeStepsFragment;

import java.util.ArrayList;

public class RecipeStepsListActivity extends AppCompatActivity implements RecipeStepsFragment.OnStepClickListener {

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
        recipeStepsFragment.setRecipeStepNames(getStepListFromGlobal(recipeId));
        recipeStepsFragment.setActivityContext(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_list_container, recipeStepsFragment)
                .commit();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    private ArrayList<String> getStepListFromGlobal(int recipeId){
        ArrayList<RecipeStep> recipeSteps = MainActivity.globalRecipeDetailsList.get(recipeId).getRecipeSteps();
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

        if(MainActivity.mTwoPaneMode == true){
            //criar uma classe fragment para exibir o video no exoplayer e a descrição dos passos
            //criar uma instancia dessa classe
            // e substittuir o fragment antigo por esse. Exemplo abaixo:

//            BodyPartFragment newFragment = new BodyPartFragment();
//            newFragment.setImageIds(AndroidImageAssets.getHeads());
//            newFragment.setListIndex(listIndex);
//            // Replace the old head fragment with a new one
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.head_container, newFragment)
//                    .commit();

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
}
