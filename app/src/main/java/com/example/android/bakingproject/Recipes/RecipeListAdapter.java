package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingproject.R;
import com.example.android.bakingproject.RecipeStepsListActivity;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter <RecipeViewHolder> {

    private final ArrayList<String> recipes_names;
    private Context activityContext;
    private static final String RECIPE_ID = "RECIPE_ID";

    public RecipeListAdapter(ArrayList<String> recipes_names, Context activityContext) {
        this.recipes_names = recipes_names;
        this.activityContext = activityContext;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipelist_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {

        holder.button.setTag(Integer.toString(position));
        holder.button.setText(recipes_names.get(position));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityContext, RecipeStepsListActivity.class);
                intent.putExtra(RECIPE_ID, position);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activityContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Log.i("denis", "getItemCount(): "+recipes_names.size());
        return recipes_names.size();
    }

}
