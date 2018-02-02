package com.example.android.bakingproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingproject.IndlingResource.SimpleIdlingResource;
import com.example.android.bakingproject.Network.NetworkUtils;
import com.example.android.bakingproject.Recipes.RecipeDetails;
import com.example.android.bakingproject.Recipes.RecipeListAdapter;
import com.example.android.bakingproject.Recipes.RecipesFromJson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<RecipeDetails>> {

    private static final int LOADER_PARSE_RECIPES_FROM_JSON = 100;
    private static final int GRIDLAYOUT_TABLET_COLUMNS = 3;
    public static ArrayList<RecipeDetails> globalRecipeDetailsList;

    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecycleView;

    public static boolean mTwoPaneMode = false;

    // The Idling Resource which will be null in production.
    @Nullable
    @VisibleForTesting
    private SimpleIdlingResource mIdlingResource = new SimpleIdlingResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //will be initialized only if testing
        if(mIdlingResource!=null) {
            mIdlingResource.setIdleState(false);
            Log.i("denis", "essa mesangem deve aparecer somente em testes do espresso");
        }

        ButterKnife.bind(this);

        //check if device should display in Two-Pane-Mode
        mTwoPaneMode = findViewById(R.id.rl_mainactivity_tablet) != null;

        startLoaderTask(LOADER_PARSE_RECIPES_FROM_JSON);
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
        Log.i("denis", "mTwoPaneMode: "+ mTwoPaneMode);

        if(loader.getId()==LOADER_PARSE_RECIPES_FROM_JSON)
            globalRecipeDetailsList = recipeList;

        if(recipeList.size()==0){
            Toast.makeText(this,getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return;
        }

        mRecycleView = findViewById(R.id.rv_recipes_list);
        if(mTwoPaneMode)
            layoutManager = new GridLayoutManager(this, GRIDLAYOUT_TABLET_COLUMNS);
        else
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);

        mRecycleView.setAdapter(new RecipeListAdapter(getRecipeNameList(), this));

        //if test is running, mIdlingResource will be null
        if(mIdlingResource!=null){
            mIdlingResource.setIdleState(true);
        }
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

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

 }
