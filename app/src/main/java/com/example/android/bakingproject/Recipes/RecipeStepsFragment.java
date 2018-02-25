package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsFragment extends Fragment {

    static final String RECIPE_NAME = "RECIPE_NAME";
    static final String IMAGE_PATH = "IMAGE_PATH";
    static final String RECIPE_STEP_NAMES = "RECIPE_STEP_NAMES";
    static final String RECYCLERVIEW_SCROL_STATE = "RECYCLERVIEW_SCROL_STATE";

    ArrayList<String> recipeStepNames = new ArrayList<>();
    private String recipeName;
    private String imagePath;

    @BindView(R.id.rv_recipe_steps_list)
    RecyclerView recyclerView;

    @BindView(R.id.iv_recipe_image)
    ImageView recipeImageView;


    public RecipeStepsFragment(){  //empty constructor
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeStepNames(ArrayList<String> recipeStepNames1){
        recipeStepNames = recipeStepNames1;
    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            recipeName = savedInstanceState.getString(RECIPE_NAME);
            imagePath = savedInstanceState.getString(IMAGE_PATH);
            recipeStepNames = savedInstanceState.getStringArrayList(RECIPE_STEP_NAMES);
        }

        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        ButterKnife.bind(this, rootView);

        //
        if(RecipeStepDetailsFragment.isUrl(imagePath)&&(RecipeStepDetailsFragment.isUrlOfImage(imagePath))) {
            Picasso.with(container.getContext()).load(imagePath).into(recipeImageView);
        } else{
            recipeImageView.setVisibility(View.GONE);
        }

        TextView textViewRecipeName = rootView.findViewById(R.id.tv_recipe_name);
        textViewRecipeName.setText(recipeName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = rootView.findViewById(R.id.rv_recipe_steps_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecipeStepListAdapter(recipeStepNames));

        if(savedInstanceState!=null)
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLERVIEW_SCROL_STATE));

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_NAME, recipeName);
        outState.putString(IMAGE_PATH, imagePath);
        outState.putStringArrayList(RECIPE_STEP_NAMES, recipeStepNames);
        outState.putParcelable(RECYCLERVIEW_SCROL_STATE, recyclerView.getLayoutManager().onSaveInstanceState());

    }
}
