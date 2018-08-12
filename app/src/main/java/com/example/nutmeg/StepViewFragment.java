package com.example.nutmeg;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutmeg.DBUtils.IngredientsDB;
import com.example.nutmeg.DBUtils.RecipesDesc;
import com.example.nutmeg.DataStructures.RecipeEntry;
import com.example.nutmeg.DataStructures.RecipeSteps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStepViewFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean fullscreen = false;
    private static int stepIndicator = 1;
    private static int noOfSteps;
    private static RecipesDesc thisRecipe;
    private IngredientsDB ingredientsDB;
    private List<RecipeSteps> recipeSteps;
    private static boolean tablet;
    private List<RecipeSteps> recipeStock;
    private static String PLAYER_PLAY_STATE = "player_state";
    private static String PLAYER_POSITION = "player_position";
    private static long recoveredPlayerPosition = 0;
    private static boolean recoveredPlayerState = false;

    private SimpleExoPlayer mPlayer;

    @BindView(R.id.stepSEPV)
    SimpleExoPlayerView mPlayerView;
    @Nullable @BindView(R.id.stepDescriptionTV)
    TextView stepDescriptionTV;

    @Nullable @BindView(R.id.previousArrowIV)
    ImageView previousArrowIV;
    @Nullable @BindView(R.id.previousTV)
    TextView previousTV;

    @Nullable @BindView(R.id.nextArrowIV)
    ImageView nextArrowIV;
    @Nullable @BindView(R.id.nextTV)
    TextView nextTV;


    private OnStepViewFragmentInteractionListener mListener;

    public StepViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepViewFragment newInstance(String param1, String param2) {
        StepViewFragment fragment = new StepViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("StepViewFragment", "onDestroyView: "+"lifecycle");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("StepViewFragment", "onDestroy: "+"lifecycle");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayer!=null)
            mPlayer.stop();
        Log.d("StepViewFragment", "onPause: "+"lifecycle");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("StepViewFragment", "onStop: "+"lifecycle");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("StepViewFragment", "onCreate: "+"lifecycle");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Recover player status if any:
        Log.d("StepViewFragment", "onCreateView: "+"lifecycle");
        if (savedInstanceState != null) {
            recoveredPlayerState = savedInstanceState.getBoolean(PLAYER_PLAY_STATE, false);
            recoveredPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION, 0);
        }


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step_view, container, false);
        if (view.findViewById(R.id.stepDescriptionTV) == null)
            fullscreen = true;
        else
            fullscreen = false;
        if(view.findViewById(R.id.previousArrowIV) == null)
            tablet = true;
        else
            tablet = false;

        ingredientsDB = IngredientsDB.getsInstance(getContext());
        int recipeIdToView = getActivity().getIntent().getIntExtra(RecipesList.RECIPE_KEY, 0);
//        LiveData<List<RecipesDesc>> recipesDesc = ingredientsDB.recipesDescDao().getThisRecipe(recipeIdToView);
        LiveData<List<RecipeSteps>> recipeSteps = ingredientsDB.recipeStepsDao().getStepsFor(recipeIdToView);
        recipeSteps.observe(this, new Observer<List<RecipeSteps>>() {
            @Override
            public void onChanged(@Nullable List<RecipeSteps> recipeSteps) {
                if (recipeSteps.size() > 0) {
                    noOfSteps = recipeSteps.size();
                    if (stepIndicator > noOfSteps)
                        stepIndicator = noOfSteps;
                    if (noOfSteps != 0 & !fullscreen) {
                        stepDescriptionTV.setText(recipeSteps.get(stepIndicator - 1).getDescription());
                    } else {
                        Log.i(TAG, "onCreateView: " + "No Steps to view");
                    }
                    if (!fullscreen)
                        setNavigationUI();
                    instantiatePlayer(recipeSteps.get(stepIndicator - 1).getVideoURL());
                    recipeStock = recipeSteps;
                }

            }
        });


        ButterKnife.bind(this, view);


        return view;
    }

    private void instantiatePlayer(String videoURL) {
        if (mPlayer == null) {
            // Setup the Player.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);

            // Setup the ExoPlayer.EventListener
            MediaSource mediaSource = getMediaSource(videoURL);


            mPlayer.prepare(mediaSource);
            //Set State:
            mPlayer.setPlayWhenReady(recoveredPlayerState);
            mPlayer.seekTo(recoveredPlayerPosition);
        }
    }

    @NonNull
    private MediaSource getMediaSource(String videoURL) {
        // Prepare the MediaSource.
        Uri videoURI = Uri.parse(videoURL);
        return new ExtractorMediaSource(videoURI,
                new DefaultDataSourceFactory(
                    getContext(),
                    Util.getUserAgent(getContext(), getActivity().getPackageName())),
                new DefaultExtractorsFactory(),
                null,
                null);
    }

    private void freePlayer(){
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void setNavigationUI() {
        if(previousArrowIV == null ||
                previousTV == null ||
                nextArrowIV == null ||
                nextTV == null )
            return;

        if (stepIndicator == 1) //No previous Navigation
        {
            previousTV.setTextColor(getContext().getResources().getColor(R.color.colorButtonInactive));
            previousTV.setVisibility(View.INVISIBLE);
            previousTV.setOnClickListener(null);
            previousArrowIV.setVisibility(View.INVISIBLE);
            previousArrowIV.setOnClickListener(null);
        }
        else {
            if (!previousTV.hasOnClickListeners()) {
                previousTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stepIndicator > 0)
                            stepIndicator--;
                        onStepIndicatorChange();
                    }
                });
            }
            if (!previousArrowIV.hasOnClickListeners()) {
                previousArrowIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stepIndicator > 0)
                            stepIndicator--;
                        onStepIndicatorChange();
                    }
                });

            }
            previousArrowIV.setVisibility(View.VISIBLE);
            previousTV.setVisibility(View.VISIBLE);
            previousTV.setTextColor(getContext().getResources().getColor(R.color.colorButtonActive) );
        }

        if (stepIndicator == noOfSteps) //No more steps to go to
        {
            nextTV.setTextColor(getContext().getResources().getColor(R.color.colorButtonInactive));
            nextArrowIV.setVisibility(View.INVISIBLE);
            nextArrowIV.setOnClickListener(null);
            nextTV.setVisibility(View.INVISIBLE);
            nextTV.setOnClickListener(null);

        }
        else{
            if (!nextTV.hasOnClickListeners()) {
                nextTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stepIndicator < noOfSteps) {
                            stepIndicator++;
                            setNavigationUI();
                            mListener.onStepViewFragmentInteraction(stepIndicator);
                            onStepIndicatorChange();
                        }
                    }
                });
            }
            if (!nextArrowIV.hasOnClickListeners()) {
                nextArrowIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stepIndicator < noOfSteps) {
                            stepIndicator++;
                            setNavigationUI();
                            mListener.onStepViewFragmentInteraction(stepIndicator);
                            onStepIndicatorChange();
                        }
                    }
                });
            }
            nextArrowIV.setVisibility(View.VISIBLE);
            nextTV.setVisibility(View.VISIBLE);
            nextTV.setTextColor(getContext().getResources().getColor(R.color.colorButtonActive) );
        }
    }

    private void onStepIndicatorChange() {
        if (!tablet) {
            setNavigationUI();
            if (stepDescriptionTV != null)
                stepDescriptionTV.setText(recipeStock.get(stepIndicator - 1).getDescription());

        }
        if (mListener != null) {
            mListener.onStepViewFragmentInteraction(stepIndicator);
        }
        mPlayer.stop();
        mPlayer.prepare(getMediaSource(recipeStock.get(stepIndicator - 1).getVideoURL()));
        mPlayer.setPlayWhenReady(false);
    }

    public void updateStep(int position){
        stepIndicator = position + 1;
        mPlayer.setPlayWhenReady(true);
        onStepIndicatorChange();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepViewFragmentInteractionListener) {
            mListener = (OnStepViewFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.d("StepViewFragment", "onAttach: "+"lifecycle");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("StepViewFragment", "onStart: "+"lifecycle");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("StepViewFragment", "onResume: "+"lifecycle");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        freePlayer();
        Log.d("StepViewFragment", "onDetach: "+"lifecycle"+"lifecycle");
        //mListener = null;
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
    public interface OnStepViewFragmentInteractionListener {
        void onStepViewFragmentInteraction(int position);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAYER_PLAY_STATE, mPlayer.getPlayWhenReady());
        outState.putLong(PLAYER_POSITION, mPlayer.getCurrentPosition());
        Log.d("StepViewFragment", "onSaveInstanceState: "+"lifecycle");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPlayer != null){
            mPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAYER_PLAY_STATE, false));
            //mPlayer.seekTo(savedInstanceState.getLong(PLAYER_POSITION, 0));
        }
        Log.d("StepViewFragment", "onActivityCreated: "+"lifecycle");


    }
}
