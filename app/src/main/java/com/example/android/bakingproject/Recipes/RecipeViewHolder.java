package com.example.android.bakingproject.Recipes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class RecipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.recipe_button)
    Button button;

    RecipeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
        button = itemView.findViewById(R.id.recipe_button);
    }

}
