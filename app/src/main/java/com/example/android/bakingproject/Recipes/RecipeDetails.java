package com.example.android.bakingproject.Recipes;

import java.util.ArrayList;

/**
 * Created by Denis on 08/01/2018.
 */

public class RecipeDetails {

    private final int id;
    private final String name;
    private final ArrayList<Ingredient> ingredientList;
    private final ArrayList<RecipeStep> recipeSteps;
    private final int serving;
    private final String image;


    RecipeDetails(int id, String name, ArrayList<Ingredient> ingredientList, ArrayList<RecipeStep> recipeSteps, int serving, String image){

        this.id = id;
        this.name = name;
        this.ingredientList = ingredientList;
        this.recipeSteps = recipeSteps;
        this.serving = serving;
        this.image = image;
    }

    int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ArrayList<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public int getServing() {
        return serving;
    }

    public String getImage() {
        return image;
    }
}
