package com.example.android.bakingproject.RecipeAppWidget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingproject.R;

import java.util.ArrayList;

import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;
import static com.example.android.bakingproject.RecipeIngredientsListActivity.returnReadableIngredientList;

/**
 * Created by denis.b on 19/02/2018.
 */

public class AppWidgetService extends RemoteViewsService {

    static final String RECIPE_ID = "RECIPE_ID";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int recipe_id = intent.getIntExtra(RECIPE_ID, 0);
        return (new IngredientListProvider(this.getApplicationContext(), recipe_id));
    }
}

class IngredientListProvider implements RemoteViewsService.RemoteViewsFactory {

    private final int recipe_id;
    private ArrayList<String> ingredientItemList = new ArrayList<>();
        private Context context = null;

        IngredientListProvider(Context context, int recipe_id) {
            this.context = context;
            this.recipe_id = recipe_id;
        }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

        Log.i("denis", "IngredientListProvider - onDataSetChanged() - recipeID: "+RecipeWidget.getRecipeIdWidget(context));

        ingredientItemList = returnReadableIngredientList(globalRecipeDetailsList.
                get(recipe_id).getIngredientList());
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
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);
        remoteView.setTextViewText(R.id.tv_single_ingredient_listview, ingredientItemList.get(position));
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
