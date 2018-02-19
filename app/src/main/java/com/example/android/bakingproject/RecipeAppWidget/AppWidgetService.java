package com.example.android.bakingproject.RecipeAppWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new IngredientListProvider(this.getApplicationContext(), intent));
        //return null;
    }
}
