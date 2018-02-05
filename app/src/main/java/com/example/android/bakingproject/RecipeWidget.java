package com.example.android.bakingproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static final String RECIPE_ID = "RECIPE_ID";
    static final String CURRENT_RECIPE_WIDGET_ID = "CURRENT_RECIPE_WIDGET_ID";
    private static final String SHOW_PREVIOUS_RECIPE = "com.example.android.bakingproject.action.SHOW_PREVIOUS_RECIPE";
    private static final String SHOW_NEXT_RECIPE = "com.example.android.bakingproject.action.SHOW_NEXT_RECIPE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        //set Pending Intent Previous Recipe
        Intent intentPrevious = new Intent(context, RecipeIntentService.class);
        intentPrevious.setAction(SHOW_PREVIOUS_RECIPE);
        PendingIntent pendingIntentPrev = PendingIntent.getService(context, 0, intentPrevious, 0);
        views.setOnClickPendingIntent(R.id.tv_previous_recipe, pendingIntentPrev);

        //set Pending Intent Next Recipe
        Intent intentNext = new Intent(context, RecipeIntentService.class);
        intentNext.setAction(SHOW_NEXT_RECIPE);
        PendingIntent pendingIntentNext = PendingIntent.getService(context, 0, intentNext, 0);
        views.setOnClickPendingIntent(R.id.tv_next_recipe, pendingIntentNext);

        int recipe_id = getRecipeIdWidget(context);
        Log.i("denis", "RecipeWidget - updateAppWidget() - RecipeId: "+recipe_id);
        String recipe_name = MainActivity.globalRecipeDetailsList.get(recipe_id).getName();
        Intent intentIngredientsList;

        if (MainActivity.mTwoPaneMode){
            intentIngredientsList = new Intent(context, RecipeStepsListActivity.class);
        } else {
            intentIngredientsList = new Intent(context, RecipeIngredientsListActivity.class);
        }

        intentIngredientsList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentIngredientsList.putExtra(RECIPE_ID, recipe_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intentIngredientsList, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_recipe_name_widget, pendingIntent);
        views.setTextViewText(R.id.tv_recipe_name_widget, recipe_name);

        views.setOnClickPendingIntent(R.id.tv_ingredients_list_widget, pendingIntent);
        views.setTextViewText(R.id.tv_ingredients_list_widget, getIngredientList(recipe_id));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ll_widget_container);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private static String getIngredientList(int recipeId) {

        String compiledIngredientsList = "";
        ArrayList<String> ingredientsList = RecipeIngredientsListActivity.returnReadableIngredientList(MainActivity.globalRecipeDetailsList.get(recipeId).getIngredientList());

        for(String ingredient : ingredientsList){
            compiledIngredientsList = compiledIngredientsList.concat(ingredient+"\n");
        }

        return compiledIngredientsList;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeIntentService.startActionUpdateWidget(context);
    }

    public static void updateAllRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static int getRecipeIdWidget(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CURRENT_RECIPE_WIDGET_ID, 0);
    }

    public static void setNextRecipeWidget(Context context){
        int current_recipe_widget_id = getRecipeIdWidget(context) + 1;

        if(current_recipe_widget_id >= MainActivity.globalRecipeDetailsList.size())
            current_recipe_widget_id = 0;

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CURRENT_RECIPE_WIDGET_ID, current_recipe_widget_id).apply();

    }

    public static void setPreviousRecipeWidget(Context context){
        int current_recipe_widget_id = getRecipeIdWidget(context) - 1;

        if(current_recipe_widget_id < 0 )
            current_recipe_widget_id = MainActivity.globalRecipeDetailsList.size()-1;

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CURRENT_RECIPE_WIDGET_ID, current_recipe_widget_id).apply();

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

