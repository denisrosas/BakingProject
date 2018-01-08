package com.example.android.bakingproject.Recipes;

public class RecipeStep {

    private final int id;
    private final String shortDescription;
    private final String description;
    private final String videoURL;
    private final String thumbnailURL;

    public RecipeStep(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }


}
