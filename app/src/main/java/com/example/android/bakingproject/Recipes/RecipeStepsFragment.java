package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingproject.R;

import java.util.ArrayList;

public class RecipeStepsFragment extends Fragment {

    OnStepClickListener mCallBack;
    ArrayList<String> recipeStepNames = new ArrayList<>();
    private String recipeName;


    public RecipeStepsFragment(){  //empty constructor
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    public void setRecipeStepNames(ArrayList<String> recipeStepNames1){
        recipeStepNames = recipeStepNames1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (OnStepClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        TextView textViewRecipeName = rootView.findViewById(R.id.tv_recipe_name);
        textViewRecipeName.setText(recipeName);

        ListView listView = rootView.findViewById(R.id.lv_recipe_steps_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.textview_stepname, recipeStepNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mCallBack.onStepSelected(position);
            }
        });

        return rootView;
    }
}
