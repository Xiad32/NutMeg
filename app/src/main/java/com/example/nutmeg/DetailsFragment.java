package com.example.nutmeg;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nutmeg.DBUtils.IngredientEntry;
import com.example.nutmeg.DBUtils.IngredientsDB;
import com.example.nutmeg.DBUtils.RecipesDesc;
import com.example.nutmeg.DataStructures.RecipeEntry;
import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.DataStructures.RecipeSteps;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment
implements StepsAdapter.onItemClickListener{

    @BindView(R.id.ingredientsLV)
    ListView ingredientsLV;
    @BindView(R.id.stepsRV)
    RecyclerView stepsRV;

    private static int stepIndicator = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDetailsFragmentInteractionListener mListener;
    private IngredientsAdapter mAdapter;
    private StepsAdapter mStepsAdapter;
    //private List<RecipeSteps> mData;
    private final Fragment thisFragment = this;


    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details, container, false);
        IngredientsDB ingredientsDB = IngredientsDB.getsInstance(getContext());

        Intent callerIntent = getActivity().getIntent();
        final int recipeIdToView = callerIntent.getIntExtra(RecipesList.RECIPE_KEY, 0);

        LiveData<List<RecipeSteps>> mData = ingredientsDB.recipeStepsDao().getStepsFor(recipeIdToView);
        LiveData<List<RecipeIngredients>> ingredientsToView= ingredientsDB.ingredientsDao().getIngredientsFor(recipeIdToView);

        ButterKnife.bind(this, view);
        //Populate interface

        //Populate Ingredients List
        ingredientsToView.observe(this, new Observer<List<RecipeIngredients>>() {
            @Override
            public void onChanged(@Nullable List<RecipeIngredients> recipeIngredients) {
                mAdapter = new IngredientsAdapter(getContext(), recipeIngredients);
                ingredientsLV.setAdapter(mAdapter);
                ingredientsLV.setDivider(null);
                ingredientsLV.setDividerHeight(0);
            }
        });

        //Populate Steps Links:
        mStepsAdapter = new StepsAdapter(getContext(), null, stepIndicator, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        stepsRV.setLayoutManager(linearLayout);
        stepsRV.setAdapter(mStepsAdapter);
        mData.observe(this, new Observer<List<RecipeSteps>>() {
            @Override
            public void onChanged(@Nullable List<RecipeSteps> recipeSteps) {
                mStepsAdapter.updateData(recipeSteps, stepIndicator);
                mStepsAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsFragmentInteractionListener) {
            mListener = (OnDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onStepChange(int position){
        stepIndicator = position +1;
        //mData. //TODO:need fix this!
        //stepsRV.setAdapter(mStepsAdapter);
        mStepsAdapter.updateSelected(position);
        //mStepsAdapter.updateData(mData, stepIndicator);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStepClick(int position) {
        //DONE: highlight the current step
        onStepChange(position);
        //DONE: send feedback to instructions fragment through Main activity
        mListener.onDetailsFragmentInteraction(position);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDetailsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDetailsFragmentInteraction(int position);
    }


}
