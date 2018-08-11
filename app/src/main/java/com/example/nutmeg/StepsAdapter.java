package com.example.nutmeg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nutmeg.DataStructures.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>
        implements View.OnClickListener {
    private onItemClickListener mListener;
    private Context mContext;
    private int mSelected;
    private List<RecipeSteps> mData;
    public interface onItemClickListener{
        void onStepClick(int position);
    }

    public StepsAdapter(Context applicationContext, List<RecipeSteps> data, int itemSelected, onItemClickListener clickListener)
    {
      mData = data;
      mContext = applicationContext;
      mSelected = itemSelected;
      mListener = clickListener;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) { holder.bind(position, mSelected); }

    @Override
    public int getItemViewType(int position) {return super.getItemViewType(position);}

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.step_item,parent, false);

        StepsViewHolder viewHolder = new StepsViewHolder(itemView);
        return viewHolder;
    }

    public void updateData(List<RecipeSteps> data, int selected){
        mData = data;
        mSelected = selected;
        this.notifyDataSetChanged();
    }

    public void updateSelected(int selected){
        mSelected = selected;
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        return 0;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        @BindView(R.id.stepNoTV)
        TextView stepNoTV;
        @BindView(R.id.shortDescriptionTV)
        TextView shortDescriptionTV;
        @BindView(R.id.step_itemV)
        ConstraintLayout stepItemV;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position, int selected){
            if ((selected - 1) == position) {
                setLayoutSelected(true);
            }
            else {
                setLayoutSelected(false);
            }
            stepNoTV.setText(String.format("%d. ", position+1));
            shortDescriptionTV.setText(mData.get(position).getShortDescription());
        }

        @Override
        public void onClick(View v) {
            mListener.onStepClick(getAdapterPosition());
        }

        private void setLayoutSelected(boolean selected){
            if (selected)
            {
                stepNoTV.setTextColor(ContextCompat.getColor(mContext, R.color.textColorSelected));
                shortDescriptionTV.setTextColor(ContextCompat.getColor(mContext, R.color.textColorSelected));
                stepItemV.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_box_selected));
            }
            else{

                stepNoTV.setTextColor(ContextCompat.getColor(mContext, R.color.textColorNotSelected));
                shortDescriptionTV.setTextColor(ContextCompat.getColor(mContext, R.color.textColorNotSelected));
                stepItemV.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_box));
            }

        }
    }

}
