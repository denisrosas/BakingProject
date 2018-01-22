package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.bakingproject.R;
import com.example.android.bakingproject.RecipeStepsListActivity;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter <RecipeListAdapter.RecipeViewHolder> {

    private final ArrayList<String> recipes_names;
    private Context activityContext;
    static final String RECIPE_ID = "RECIPE_ID";

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

        Log.i("denis","onBindViewHolder() - passou aqui. position: "+position);

        holder.button.setTag(Integer.toString(position));
        holder.button.setText(recipes_names.get(position));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(activityContext, "chamar a activity RecipeDetails", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activityContext, RecipeStepsListActivity.class);
                activityContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("denis", "getItemCount(): "+recipes_names.size());
        return recipes_names.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        Button button = null;

        RecipeViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.recipe_button);
        }
    }

}
