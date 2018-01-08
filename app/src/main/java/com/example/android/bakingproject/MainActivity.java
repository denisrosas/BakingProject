package com.example.android.bakingproject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingproject.Network.NetworkUtils;
import com.example.android.bakingproject.Recipes.RecipeDetails;
import com.example.android.bakingproject.Recipes.RecipesFromJson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<RecipeDetails>> {

    private static final int LOADER_PARSE_RECIPES_FROM_JSON = 100;
    public static ArrayList<RecipeDetails> globalRecipeDetailsList;

    @BindView(R.id.textView1)TextView textView;
    //@BindView(R.id.button_increase) Button button;

    int valor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        startLoaderTask(LOADER_PARSE_RECIPES_FROM_JSON);
    }

    @OnClick(R.id.button_increase)
    public void increaseNumber(Button localButton){
        valor++;
        localButton.setText("Novo valor "+valor);
        Log.i("denis","ta passando aqui");
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
    public void onLoadFinished(Loader<ArrayList<RecipeDetails>> loader, ArrayList<RecipeDetails> data) {

        if(loader.getId()==LOADER_PARSE_RECIPES_FROM_JSON)
            globalRecipeDetailsList = data;

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipeDetails>> loader) {
        //do nothing
    }

}
