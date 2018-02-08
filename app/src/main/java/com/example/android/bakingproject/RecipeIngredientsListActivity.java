package com.example.android.bakingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.bakingproject.Recipes.Ingredient;
import com.example.android.bakingproject.Recipes.IngredientsFragment;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;

public class RecipeIngredientsListActivity extends AppCompatActivity {

    static final String RECIPE_ID = "RECIPE_ID";
    ArrayList<Ingredient> ingredientList;
    int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients_list);

        if(getActionBar()!=null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(savedInstanceState==null) {
            recipeId = intent.getIntExtra(RECIPE_ID, 0);
            ArrayList<String> readableIngredientList = returnReadableIngredientList(globalRecipeDetailsList.
                    get(recipeId).getIngredientList());

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredientsList(readableIngredientList);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_list_container, ingredientsFragment)
                    .commit();
        } else{
            recipeId = savedInstanceState.getInt(RECIPE_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<String> returnReadableIngredientList(ArrayList<Ingredient> ingredientList) {

        String finalQuantity;
        String finalMeasure;
        ArrayList<String> readableIngredientList = new ArrayList<>();

        int ingredNum = 1;

        for (Ingredient ingredient : ingredientList){

            if(ingredient.getQuantity() == 0.5){
                finalQuantity = "Half";
            }
            //else if quantity float has an integer value, we don't need to show decimal case
            else if(ingredient.getQuantity()%1==0) {
                finalQuantity = String.format(Locale.getDefault(),"%.0f", ingredient.getQuantity());
            } else{
                finalQuantity = Float.toString(ingredient.getQuantity());
            }

            finalMeasure = ingredient.getMeasure().
                    replace("CUP","Cup(s)").
                    replace("K", "Kg").
                    replace("G", "Gram(s)").
                    replace("OZ", "Ounce(s)").
                    replace("TSP", "tea spoon(s)").
                    replace("TBLSP", "table spoon(s)").
                    replace("UNIT", "Unit");

            readableIngredientList.add(ingredNum+") - "+finalQuantity+" "+finalMeasure+" of "+
                    ingredient.getIngredientName());

            ingredNum++;
        }
        return readableIngredientList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID, recipeId);
    }
}
