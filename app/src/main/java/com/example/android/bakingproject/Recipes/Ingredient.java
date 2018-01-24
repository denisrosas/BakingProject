package com.example.android.bakingproject.Recipes;

public class Ingredient {
    private final float quantity;
    private final String measure;
    private final String ingredientName;

    public Ingredient(float quantity, String measure, String ingredientName) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
