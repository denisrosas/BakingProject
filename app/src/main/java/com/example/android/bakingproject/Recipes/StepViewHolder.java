package com.example.android.bakingproject.Recipes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class StepViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.button_recipe_step)
    Button stepButton;

    StepViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
        stepButton = itemView.findViewById(R.id.button_recipe_step);
    }
}
