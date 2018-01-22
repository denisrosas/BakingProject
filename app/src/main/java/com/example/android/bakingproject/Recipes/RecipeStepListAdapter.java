package com.example.android.bakingproject.Recipes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Denis on 11/01/2018.
 */

public class RecipeStepListAdapter extends RecyclerView.Adapter <RecipeStepListAdapter.StepViewHolder> {

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        public StepViewHolder(View itemView) {
            super(itemView);
        }
    }
}
