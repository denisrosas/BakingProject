package com.example.android.bakingproject.Recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.bakingproject.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {

    private static final String INGREDIENTS_LIST = "INGREDIENTS_LIST";
    private static final String LIST_VIEW_BUNDLE = "LIST_VIEW_BUNDLE";

    ArrayList<String> ingredientsList;

    @BindView(R.id.lv_ingredients_list)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState!=null)
            ingredientsList = savedInstanceState.getStringArrayList(INGREDIENTS_LIST);

        View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        ButterKnife.bind(this, rootView);

        listView = rootView.findViewById(R.id.lv_ingredients_list);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.textview_ingredient, ingredientsList);
        listView.setAdapter(arrayAdapter);

        if(savedInstanceState!=null)
            listView.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_VIEW_BUNDLE));

        return rootView;
    }

    public void setIngredientsList(ArrayList<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(INGREDIENTS_LIST, ingredientsList);
        outState.putParcelable(LIST_VIEW_BUNDLE, listView.onSaveInstanceState());
    }
}
