package com.example.android.bakingproject.Recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingproject.R;

/**
 * Created by Denis on 11/01/2018.
 */

public class RecipeStepsFragment extends Fragment {

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
