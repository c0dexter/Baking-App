package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.StepsActivity;
import pl.michaldobrowolski.bakingapp.utils.UtilityHelper;

import static pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsFragment.OnBackButtonClickedListener;
import static pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsFragment.OnNextButtonClickedListener;

public class StepDetailsActivity extends AppCompatActivity implements OnBackButtonClickedListener, OnNextButtonClickedListener {
    // -------------------- Properties --------------------//
    final static String TAG = StepsActivity.class.getSimpleName();
    // Bundle keys
    private static final String MAIN_BUNDLE_KEY = "step_detail";
    private static final String BUNDLE_ARRAY_STEPS_KEY = "step_array_bundle_key";
    private static final String BUNDLE_STEP_ID_KEY = "step_position_bundle_key";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name_bundle_key";
    // Fields
    private int mClickedStepPosition;
    private String mRecipeName;
    private ArrayList<Step> mStepArrayList;
    private int mTotalStepsAmount;
    private int mStepId;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private StepDetailsFragment stepDetailsFragment;
    private StepDetailsDescFragment descriptionFragment;
    private StepDetailsExoPlayerFragment exoPlayerFragment;
    private UtilityHelper utilityHelper = new UtilityHelper();
    private boolean fragmentAdded;
    private boolean mTwoPane;
    // ------------------ End Of Properties ------------------ //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_step_detail);

        if(findViewById(R.id.steps_activity_tablet_layout) != null) {
            mTwoPane = true;

            // Hide elements for tablet view
            ImageButton buttonNext = (ImageButton) findViewById(R.id.button_next_step);
            buttonNext.setVisibility(View.GONE);
            ImageButton buttonPrevious = (ImageButton) findViewById(R.id.button_previous_step);
            buttonPrevious.setVisibility(View.GONE);
            TextView stepCounter = (TextView) findViewById(R.id.text_step_counter);
            stepCounter.setVisibility(View.GONE);

        } else {
            mTwoPane = false;
        }

        // Get data from bundle
        getStepDetailsDataFromBundle(
                MAIN_BUNDLE_KEY,
                BUNDLE_ARRAY_STEPS_KEY,
                BUNDLE_STEP_ID_KEY,
                BUNDLE_RECIPE_NAME_KEY);

        // Check if saved instance state exist and set a fragmentAdded flag
        if(savedInstanceState != null){
            fragmentAdded = savedInstanceState.getBoolean("fragment_added");
            mClickedStepPosition = savedInstanceState.getInt("clicked_step_position");
        }

        getDataForSpecificStep(mClickedStepPosition);
        addStepDetailsFragment(fragmentAdded);
    }

    private void getDataForSpecificStep(int position) {
        mTotalStepsAmount = mStepArrayList.size();
        mDescription = mStepArrayList.get(position).getmDescription();
        mVideoUrl = mStepArrayList.get(position).getmVideoURL();
        mThumbnailUrl = mStepArrayList.get(position).getThumbnailURL();
        mStepId = position; // Getting position instead of step ID to avoiding bug in numeration
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private void addStepDetailsFragment(boolean fragmentExist) {
        // Main step details fragment
        stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(makeStepDetailsBundle());

        // Full description fragment
        descriptionFragment = new StepDetailsDescFragment();
        descriptionFragment.setArguments(makeFullDescBundle());

        // ExoPlayer Fragment
        exoPlayerFragment = new StepDetailsExoPlayerFragment();
        exoPlayerFragment.setArguments(makeExoPlayerBundle());

        // Choose a method delivery data to fragment. For new view: "add", for existed view "replace"
        FragmentManager fm = getSupportFragmentManager();
        if(!fragmentExist){
            fm.beginTransaction()
                    .add(R.id.step_details_container, stepDetailsFragment)
                    .add(R.id.step_full_desc_container, descriptionFragment)
                    .add(R.id.exo_player_container, exoPlayerFragment)
                    .commit();
            fragmentAdded = true;
        } else {
            fm.beginTransaction()
                    .replace(R.id.step_details_container, stepDetailsFragment)
                    .replace(R.id.step_full_desc_container, descriptionFragment)
                    .replace(R.id.exo_player_container, exoPlayerFragment)
                    .commit();
        }
    }

    private void getStepDetailsDataFromBundle(String mainBundleKey, String StepsArrayKey, String stepPositionClicked, String recipeNameKey) {
        Bundle stepDetailBundle = getIntent().getExtras();
        if (stepDetailBundle != null) {
            if (stepDetailBundle.containsKey(mainBundleKey)) {
                stepDetailBundle = stepDetailBundle.getBundle(mainBundleKey);
                mStepArrayList = Objects.requireNonNull(stepDetailBundle).getParcelableArrayList(StepsArrayKey);
                mClickedStepPosition = Objects.requireNonNull(stepDetailBundle).getInt(stepPositionClicked);
                mRecipeName = stepDetailBundle.getString(recipeNameKey);
            } else {
                Log.i(TAG, "Cannot get object from bundle. Incorrect bundle key string.");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }
    }

    private Bundle makeStepDetailsBundle() {
        Bundle stepDetailsBundle = new Bundle();
        stepDetailsBundle.putInt("step_id_bundle_key", mStepId);
        stepDetailsBundle.putInt("total_steps_bundle_key", mTotalStepsAmount);
        stepDetailsBundle.putString("recipe_name_bundle_key", mRecipeName);
        return stepDetailsBundle;
    }

    private Bundle makeFullDescBundle() {
        Bundle fullDescBundle = new Bundle();
        fullDescBundle.putString("desc_bundle", utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)",mDescription));
        return fullDescBundle;
    }

    private Bundle makeExoPlayerBundle() {
        Bundle videoBundle = new Bundle();
        videoBundle.putString("video_url_bundle", mVideoUrl);
        videoBundle.putString("thumbnail_url_bundle", mThumbnailUrl);
        return videoBundle;
    }

    @Override
    public void onBackButtonClicked(int newPositionValue) {
        Toast.makeText(this, "Previous step", Toast.LENGTH_SHORT).show();
        mClickedStepPosition = newPositionValue;
        getDataForSpecificStep(mClickedStepPosition);
        addStepDetailsFragment(fragmentAdded);
    }

    @Override
    public void onNextButtonClicked(int newPositionValue) {
        Toast.makeText(this, "Next step" , Toast.LENGTH_SHORT).show();
        mClickedStepPosition = newPositionValue;
        getDataForSpecificStep(mClickedStepPosition);
        addStepDetailsFragment(fragmentAdded);
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayerFragment.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragment_added", fragmentAdded);
        outState.putInt("clicked_step_position", mClickedStepPosition);
    }
}
