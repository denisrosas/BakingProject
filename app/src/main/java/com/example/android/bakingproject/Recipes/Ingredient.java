package com.example.android.bakingproject.Recipes;

/**
 * Created by Denis on 08/01/2018.
 */

public class Ingredient {
    private final float quantity;
    private final String measure;
    private final String ingredientName;

    public Ingredient(float quantity, String measure, String ingredientName) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
    }


}
