package com.example.nutmeg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.DataStructures.RecipeSteps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends BaseAdapter {
    Context mContext;
    List<RecipeIngredients> mData;
    LayoutInflater mInflater;

    @BindView(R.id.quanitityTV)
    TextView quantityTV;
    @BindView(R.id.ingredientsTV)
    TextView ingredientsTV;
    @BindView(R.id.measureTV)
    TextView measureTV;

    public IngredientsAdapter(Context c, List<RecipeIngredients> Data) {
        mContext = c;
        mData = Data;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mData.size();

    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mInflater.inflate(R.layout.ingredient_item, parent, false);
        ButterKnife.bind(this, view);
        quantityTV.setText(String.valueOf(mData.get(position).getQuantity()));
        ingredientsTV.setText(mData.get(position).getIngredient());
        measureTV.setText(mData.get(position).getMeasure());
        return view;
    }


}

