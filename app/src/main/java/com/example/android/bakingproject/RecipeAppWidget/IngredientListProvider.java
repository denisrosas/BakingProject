package com.example.android.bakingproject.RecipeAppWidget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by denis.b on 19/02/2018.
 */

public class IngredientListProvider implements RemoteViewsService.RemoteViewsFactory {

        private ArrayList<String> ingredientItemList = new ArrayList<>();
        private Context context = null;
        private int appWidgetId;

        public IngredientListProvider(Context context, int appWidgetId, ArrayList<String> ingredientList) {
            this.context = context;
            this.appWidgetId =  appWidgetId;
            this.ingredientItemList = ingredientList;
        }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

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
//        final RemoteViews remoteView = new RemoteViews(
//                context.getPackageName(), R.layout.list_row);
//        ListItem listItem = listItemList.get(position);
//        remoteView.setTextViewText(R.id.heading, listItem.heading);
//        remoteView.setTextViewText(R.id.content, listItem.content);
//
//        return remoteView;
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
