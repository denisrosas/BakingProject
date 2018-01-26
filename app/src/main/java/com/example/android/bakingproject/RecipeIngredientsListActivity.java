package com.example.android.bakingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingproject.Recipes.Ingredient;
import com.example.android.bakingproject.Recipes.IngredientsFragment;

import java.util.ArrayList;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;

public class RecipeIngredientsListActivity extends AppCompatActivity {

    static final String RECIPE_ID = "RECIPE_ID";
    ArrayList<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients_list);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra(RECIPE_ID, 0);

        ArrayList<String> readableIngredientList = returnReadableIngredientList(globalRecipeDetailsList.
                get(recipeId).getIngredientList());

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setIngredientsList(readableIngredientList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_list_container, ingredientsFragment)
                .commit();

    }

    public static ArrayList<String> returnReadableIngredientList(ArrayList<Ingredient> ingredientList) {

        String quantity;
        String medida;
        String ingredientName;
        ArrayList<String> readableIngredientList = new ArrayList<>();

        for (Ingredient ingredient : ingredientList){

            if(ingredient.getQuantity() == 0.5)
                quantity = "Half";
            else
                quantity = Float.toString(ingredient.getQuantity());

            readableIngredientList.add(quantity+" "+ingredient.getMeasure()+" of "+ingredient.getIngredientName());
        }
        return readableIngredientList;
    }
}
