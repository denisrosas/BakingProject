package com.example.android.bakingproject.RecipeAppWidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingproject.R;
import com.example.android.bakingproject.RecipeWidget;

import java.util.ArrayList;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;
import static com.example.android.bakingproject.RecipeIngredientsListActivity.returnReadableIngredientList;

/**
 * Created by denis.b on 19/02/2018.
 */

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new IngredientListProvider(this.getApplicationContext()));
    }
}

class IngredientListProvider implements RemoteViewsService.RemoteViewsFactory {

        private ArrayList<String> ingredientItemList = new ArrayList<>();
        private Context context = null;

        IngredientListProvider(Context context) {
            this.context = context;
        }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

        ingredientItemList = returnReadableIngredientList(globalRecipeDetailsList.
                get(RecipeWidget.getRecipeIdWidget(context)).
                getIngredientList());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        remoteView.setTextViewText(R.id.lv_widget_ingredient_list, ingredientItemList.get(position));
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
