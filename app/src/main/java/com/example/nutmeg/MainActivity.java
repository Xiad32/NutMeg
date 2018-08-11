package com.example.nutmeg;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutmeg.DBUtils.IngredientEntry;
import com.example.nutmeg.DBUtils.IngredientsDB;
import com.example.nutmeg.DBUtils.RecipesDesc;
import com.example.nutmeg.DataStructures.RecipeEntry;
import com.example.nutmeg.DataStructures.RecipeIngredients;
import com.example.nutmeg.DataStructures.RecipeSteps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity
        implements StepViewFragment.OnStepViewFragmentInteractionListener,
                    DetailsFragment.OnDetailsFragmentInteractionListener
    {

        public static final String SP_NAME = "shared_ref_name";
        /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private RecipesDesc recipeDescToView;
    private ArrayList<Fragment> fragments;
    private IngredientsDB ingredientsDB;
    public static final String SP_RECIPE_INDICATOR = "recipe_indicator";
    private static boolean portrait;
    private DetailsFragment detailsFragment;
    private StepViewFragment stepViewFragment;


    @Nullable @BindView(R.id.recipieNameTV)
    TextView recipieNameTV;
    @Nullable @BindView(R.id.recipieIV)
    ImageView recipieIV;
    @Nullable @BindView(R.id.servesTV)
    TextView servesTV;



        /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int stepIndicator = 1;
    private static boolean landscape = false;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (findViewById(R.id.tabs) == null)
            landscape = true;
        else
            landscape = false;

        if(findViewById(R.id.fullscreen_video_fragment) != null)
            portrait = true;
        else
            portrait = false;

        if (!landscape) {

            ingredientsDB = IngredientsDB.getsInstance(getApplicationContext());


            Toolbar toolbar = (Toolbar) findViewById(R.id.recipie_item);
            setSupportActionBar(toolbar);

            //Get Data to view from calling Intent:
            int recipeIdToView = getIntent().getIntExtra(RecipesList.RECIPE_KEY, 0);
            LiveData<List<RecipesDesc>> allRecipes = ingredientsDB.recipesDescDao().getAllRecipes();
            final LiveData<List<RecipesDesc>> recipeToView = ingredientsDB.recipesDescDao().getThisRecipe(recipeIdToView);
            recipeToView.observe(this, new Observer<List<RecipesDesc>>() {
                @Override
                public void onChanged(@Nullable List<RecipesDesc> recipesDescs) {
                    if (recipesDescs.size() > 0) {
                        recipieNameTV.setText(recipesDescs.get(0).getName());

                        Picasso.get()
                                .load(recipesDescs.get(0).getImage())
                                .placeholder(getApplicationContext().getResources().getDrawable(R.drawable.emptyiconcrop))
                                .error(getApplicationContext().getResources().getDrawable(R.drawable.recipe_default_thumbnail))
                                .into(recipieIV);
                        servesTV.setText(String.valueOf(recipesDescs.get(0).getServings()));
                    }
                }
            });

            //Instantiate the Fragments:
            fragments = new ArrayList<>();
            fragments.add(new DetailsFragment());
            fragments.add(new StepViewFragment());

            // Create the adapter that will return a fragment.
            // Instantiate adapter and add fragments
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
        else if (portrait){ //landscape phone
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.fullscreen_video_fragment, new StepViewFragment()).commit();
        }
        else{ //tablet

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ingredientsDB = IngredientsDB.getsInstance(getApplicationContext());
            Toolbar toolbar = (Toolbar) findViewById(R.id.recipie_item);
            setSupportActionBar(toolbar);
            //Get Data to view from calling Intent:
            int recipeIdToView = getIntent().getIntExtra(RecipesList.RECIPE_KEY, 0);
            LiveData<List<RecipesDesc>> allRecipes = ingredientsDB.recipesDescDao().getAllRecipes();
            final LiveData<List<RecipesDesc>> recipeToView = ingredientsDB.recipesDescDao().getThisRecipe(recipeIdToView);
            recipeToView.observe(this, new Observer<List<RecipesDesc>>() {
                @Override
                public void onChanged(@Nullable List<RecipesDesc> recipesDescs) {
                    recipieNameTV.setText(recipesDescs.get(0).getName());
                    Picasso.get()
                            .load(recipesDescs.get(0).getImage())
                            .placeholder(getApplicationContext().getResources().getDrawable(R.drawable.emptyiconcrop))
                            .error(getApplicationContext().getResources().getDrawable(R.drawable.emptyiconcrop))
                            .into(recipieIV);
                    servesTV.setText(String.valueOf(recipesDescs.get(0).getServings()));
                }
            });

            stepViewFragment = new StepViewFragment();
            detailsFragment= new DetailsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.step_view_fragment_tablet, stepViewFragment).commit();
            fragmentManager.beginTransaction().add(R.id.details_fragment_tablet, detailsFragment).commit();



        }


    }


    @Override
    public void onStepViewFragmentInteraction(int position) {
        stepIndicator = position;
        if (!landscape || portrait)
            ((DetailsFragment) fragments.get(0)).onStepChange(position);
        else
            detailsFragment.onStepChange(position);
    }

    @Override
    public void onDetailsFragmentInteraction(int position) {
        stepIndicator = position;
        if(!landscape || portrait)
            ( (StepViewFragment) fragments.get(1)).updateStep(position);
        else
            stepViewFragment.updateStep(position);


    }


        /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] titles;
        private ArrayList<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> Fragments) {

            super(fm);
            titles = getApplicationContext().getResources().getStringArray(R.array.fragmentTitles);
            fragments = Fragments;
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
