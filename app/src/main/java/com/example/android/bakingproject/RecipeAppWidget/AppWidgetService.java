package com.example.android.bakingproject.RecipeAppWidget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;
import static com.example.android.bakingproject.RecipeIngredientsListActivity.returnReadableIngredientList;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new IngredientListProvider(this.getApplicationContext(), appWidgetId,
                returnReadableIngredientList(globalRecipeDetailsList.get(0).getIngredientList())));
    }
}
