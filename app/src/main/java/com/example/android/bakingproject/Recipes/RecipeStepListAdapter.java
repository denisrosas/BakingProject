package com.example.android.bakingproject.Recipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingproject.R;

import java.util.ArrayList;

public class RecipeStepListAdapter extends RecyclerView.Adapter<StepViewHolder> {

    private final ArrayList<String> stepShortDescription;
    private OnStepClickListener mCallBack;

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    RecipeStepListAdapter(ArrayList<String> stepShortDescription) {
        this.stepShortDescription = stepShortDescription;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.stepname_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {

        holder.stepButton.setText(stepShortDescription.get(position));
        holder.stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.onStepSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepShortDescription.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mCallBack = (OnStepClickListener) recyclerView.getContext();
    }
}
