package com.example.nutmeg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutmeg.DataStructures.RecipeEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ReipesViewHolder> implements View.OnClickListener{
    private Context mContext;
    private ArrayList<RecipeEntry> mData;
    private ClickListener mClickListener;
    public interface ClickListener{
        void onRecipeClick(int position);
    }

    public RecipesAdapter(Context applicationContext, ArrayList<RecipeEntry> data, ClickListener clickListener) {
        mContext = applicationContext;
        mData = data;
        mClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ReipesViewHolder holder, int position) {
        holder.bind(holder.getAdapterPosition());
    }
    @Override
    public ReipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recipe_item,parent, false);

        ReipesViewHolder viewHolder = new ReipesViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        return 0;
    }

    public void updateData(ArrayList<RecipeEntry> newData){
        mData = newData;
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
    }

    public class ReipesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener  {
        @BindView(R.id.recipieIV)     ImageView recipeImage;
        @BindView(R.id.recipieNameTV) TextView recipeName;
        @BindView(R.id.servesTV) TextView servesTV;

        public ReipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind (int position){
            //TODO: look into lading
            Picasso.get()
                    .load(mData.get(position).getImageURL())
                    .placeholder(mContext.getResources().getDrawable(R.drawable.emptyiconcrop))
                    .error(mContext.getResources().getDrawable(R.drawable.recipe_default_thumbnail))
                    .fit()
                    .into(recipeImage);
            recipeName.setText(mData.get(position).getName());
            servesTV.setText(String.valueOf(mData.get(position).getServings()));
        }

        @Override
        public void onClick(View v) {
            mClickListener.onRecipeClick(getAdapterPosition());
        }
    }
}
