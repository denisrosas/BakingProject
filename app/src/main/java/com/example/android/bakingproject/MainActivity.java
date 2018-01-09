package com.example.android.bakingproject;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.example.android.bakingproject.Network.NetworkUtils;
import com.example.android.bakingproject.Recipes.RecipeDetails;
import com.example.android.bakingproject.Recipes.RecipeListAdapter;
import com.example.android.bakingproject.Recipes.RecipesFromJson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<RecipeDetails>> {

    private static final int LOADER_PARSE_RECIPES_FROM_JSON = 100;
    public static ArrayList<RecipeDetails> globalRecipeDetailsList;
    RecyclerView mRecycleView;
    public static boolean isTablet = false;

    private static final int GRIDLAYOUT_TABLET_COLUMNS = 3;
    private static final int GRIDLAYOUT_PHONE_COLUMNS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isDeviceATablet();

        startLoaderTask(LOADER_PARSE_RECIPES_FROM_JSON);
    }

    private void isDeviceATablet(){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x<=600)
            isTablet=true;
        else
            isTablet=false;
    }

    private void startLoaderTask(int loaderId){

        if(NetworkUtils.isNetworkConnected(this)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<ArrayList<RecipeDetails>> loaderTaskRecipes = loaderManager.getLoader(loaderId);

            //if there is already a LoaderTask, restart it
            if (loaderTaskRecipes == null) {
                loaderManager.initLoader(loaderId, null, this);
            } else {
                loaderManager.restartLoader(loaderId, null, this);
            }
        }
    }

    @Override
    public Loader<ArrayList<RecipeDetails>> onCreateLoader(int loaderId, Bundle bundle) {

        if(loaderId == LOADER_PARSE_RECIPES_FROM_JSON){
            return new RecipesFromJson(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<RecipeDetails>> loader, ArrayList<RecipeDetails> recipeList) {

        //GridLayoutManager layoutManager;
        LinearLayoutManager layoutManager;

        Log.i("denis","onLoadFinished(). Tamanho da recipeList: "+recipeList.size());
        Log.i("denis", "isTablet: "+isTablet);

        if(loader.getId()==LOADER_PARSE_RECIPES_FROM_JSON)
            globalRecipeDetailsList = recipeList;

        if(recipeList.size()==0){
            Toast.makeText(this,getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return;
        }

        mRecycleView = (RecyclerView) findViewById(R.id.rv_recipes_list);
        if(isTablet)
            //layoutManager = new GridLayoutManager(this, GRIDLAYOUT_TABLET_COLUMNS);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        else
            //layoutManager = new GridLayoutManager(this, GRIDLAYOUT_PHONE_COLUMNS);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);

        mRecycleView.setAdapter(new RecipeListAdapter(getRecipeNameList(), this));
    }

    private ArrayList<String> getRecipeNameList() {
        ArrayList<String> recipeNames = new ArrayList<>();
        for(RecipeDetails recipeDetails: globalRecipeDetailsList){
            recipeNames.add(recipeDetails.getName());
        }
        return recipeNames;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipeDetails>> loader) {
        //do nothing
    }

}
