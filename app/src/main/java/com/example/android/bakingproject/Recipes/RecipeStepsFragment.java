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
import android.widget.TextView;

import com.example.android.bakingproject.R;

import java.util.ArrayList;

public class RecipeStepsFragment extends Fragment {

    ArrayList<String> recipeStepNames = new ArrayList<>();
    private String recipeName;


    public RecipeStepsFragment(){  //empty constructor
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeStepNames(ArrayList<String> recipeStepNames1){
        recipeStepNames = recipeStepNames1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        TextView textViewRecipeName = rootView.findViewById(R.id.tv_recipe_name);
        textViewRecipeName.setText(recipeName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_recipe_steps_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecipeStepListAdapter(recipeStepNames));

        return rootView;
    }
}
