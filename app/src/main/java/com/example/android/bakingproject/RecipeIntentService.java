package com.example.android.bakingproject;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;

/**
 * Created by Denis on 27/01/2018.
 */

public class RecipeIntentService extends IntentService {

    private static final String ACTION_UPDATE = "com.example.android.bakingproject.action.UPDATE_WIDGETS";
    private static final String SHOW_PREVIOUS_RECIPE = "com.example.android.bakingproject.action.SHOW_PREVIOUS_RECIPE";
    private static final String SHOW_NEXT_RECIPE = "com.example.android.bakingproject.action.SHOW_NEXT_RECIPE";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent!=null){
            String intentAction = intent.getAction();

            if(Objects.equals(intentAction, ACTION_UPDATE)){
                handleActionUpdateWidgets();
                Log.i("denis","RecipeIntentService - onHandleIntent - ACTION_UPDATE");
            } else if (Objects.equals(intentAction, SHOW_PREVIOUS_RECIPE)){
                Log.i("denis","RecipeIntentService - onHandleIntent - SHOW_PREVIOUS_RECIPE");
                RecipeWidget.setPreviousRecipeWidget(this);
                handleActionUpdateWidgets();
            } else if (Objects.equals(intentAction, SHOW_NEXT_RECIPE)){
                Log.i("denis","RecipeIntentService - onHandleIntent - SHOW_NEXT_RECIPE");
                RecipeWidget.setNextRecipeWidget(this);
                handleActionUpdateWidgets();
            }
        }
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE);
        try{
            context.startService(intent);
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    private void handleActionUpdateWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ll_widget_container);
        RecipeWidget.updateAllRecipeWidgets(this, appWidgetManager, appWidgetIds);

    }
}
