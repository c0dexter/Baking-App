package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.StepsActivity;
import pl.michaldobrowolski.bakingapp.utils.UtilityHelper;


public class StepDetailsActivity extends AppCompatActivity {
    // Bundle keys
    public static final String MAIN_BUNDLE_KEY = "step_detail";
    public static final String BUNDLE_ARRAY_STEPS_KEY = "step_array_bundle_key";
    public static final String BUNDLE_STEP_ID_KEY = "step_position_bundle_key";
    public static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name_bundle_key";
    public static final String BUNDLE_TABLET_TEST_KEY = "tablet_test";
    // -------------------- Properties --------------------//
    final static String TAG = StepsActivity.class.getSimpleName();
    // UI elements
    public TextView mStepCounterTv;
    public ImageButton backBtn;
    public ImageButton nextBtn;
    public LinearLayout navSection;
    // Fields
    private int mCurrentStep;
    private String mRecipeName;
    private ArrayList<Step> mStepArrayList;
    private int mTotalStepsAmount;
    private int mStepId;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private StepDetailsFragment mStepDetailsFragment;
    private UtilityHelper utilityHelper = new UtilityHelper();
    private boolean fragmentAdded;
    private FragmentManager fm;
    private boolean navigationClicked = false;
    private boolean isTest;
    // ------------------ End Of Properties ------------------ //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_step_detail);

        // Get data from bundle
        getStepDetailsDataFromBundle(
                MAIN_BUNDLE_KEY,
                BUNDLE_ARRAY_STEPS_KEY,
                BUNDLE_STEP_ID_KEY,
                BUNDLE_RECIPE_NAME_KEY,
                BUNDLE_TABLET_TEST_KEY);

        // Mapping
        mStepDetailsFragment = new StepDetailsFragment();
        fm = getSupportFragmentManager();
        mStepCounterTv = findViewById(R.id.text_step_counter);
        backBtn = findViewById(R.id.button_previous_step);
        nextBtn = findViewById(R.id.button_next_step);
        navSection = findViewById(R.id.navigation_section_linear_layout);
        if (!isTest) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                navSection.setVisibility(View.GONE);
            }
        }

        // Set title on the ActionBar
        setTitle(mRecipeName + "'s instructions");

        // Check if saved instance state exist and set a fragmentAdded flag
        if (savedInstanceState != null) {
            fragmentAdded = savedInstanceState.getBoolean("fragment_added");
            mCurrentStep = savedInstanceState.getInt("clicked_step_position");
            if (!navigationClicked) {
                mStepDetailsFragment = (StepDetailsFragment) fm.getFragment(savedInstanceState, "step_details_fragment");
            }
            isTest = savedInstanceState.getBoolean("is_test");
        }

        getDataForSpecificStep(mCurrentStep);
        addStepDetailsFragment(fragmentAdded);

        // Logic for UI elements
        setStepsCounter(mCurrentStep, mStepArrayList.size());
        showOrHideNavigationButtons();

        // Navigation buttons logic
        nextBtn.setOnClickListener(v -> onNextButtonClicked(mCurrentStep + 1));

        backBtn.setOnClickListener(v -> onBackButtonClicked(mCurrentStep - 1));
    }

    public void getDataForSpecificStep(int position) {
        mTotalStepsAmount = mStepArrayList.size();
        mDescription = utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", mStepArrayList.get(position).getmDescription());
        mVideoUrl = mStepArrayList.get(position).getmVideoURL();
        mThumbnailUrl = mStepArrayList.get(position).getThumbnailURL();
        mStepId = position; // Getting position instead of step ID to avoiding bug in numeration
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addStepDetailsFragment(boolean fragmentExist) {
        // Main step details fragment
        mStepDetailsFragment.setArguments(makeStepDetailsBundle());
        // Choose a method delivery data to fragment. For new view: "add", for existed view "replace"
        if (!fragmentExist) {
            fm.beginTransaction()
                    .add(R.id.step_details_container, mStepDetailsFragment)
                    .commit();
            fragmentAdded = true;
        } else {
            fm.beginTransaction()
                    .replace(R.id.step_details_container, mStepDetailsFragment)
                    .commit();
        }
    }

    private void getStepDetailsDataFromBundle(String mainBundleKey, String StepsArrayKey, String stepPositionClicked, String recipeNameKey, String tabletTestKey) {
        Bundle stepDetailBundle = getIntent().getExtras();
        if (stepDetailBundle != null) {
            if (stepDetailBundle.containsKey(mainBundleKey)) {
                stepDetailBundle = stepDetailBundle.getBundle(mainBundleKey);
                mStepArrayList = stepDetailBundle.getParcelableArrayList(StepsArrayKey);
                mCurrentStep = stepDetailBundle.getInt(stepPositionClicked);
                mRecipeName = stepDetailBundle.getString(recipeNameKey);
                if (stepDetailBundle.containsKey(tabletTestKey)) {
                    isTest = stepDetailBundle.getBoolean(tabletTestKey);
                }
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
        stepDetailsBundle.putString("desc_bundle", utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", mDescription));
        stepDetailsBundle.putString("video_url_bundle", mVideoUrl);
        stepDetailsBundle.putString("thumbnail_url_bundle", mThumbnailUrl);

        return stepDetailsBundle;
    }

    public void onBackButtonClicked(int newPositionValue) {
        navigationClicked = true;
        mCurrentStep = newPositionValue;
        getDataForSpecificStep(mCurrentStep);
        mStepDetailsFragment = new StepDetailsFragment();
        addStepDetailsFragment(fragmentAdded);
        setStepsCounter(mCurrentStep, mStepArrayList.size());
        showOrHideNavigationButtons();
    }

    public void onNextButtonClicked(int newPositionValue) {
        navigationClicked = true;
        mCurrentStep = newPositionValue;
        getDataForSpecificStep(mCurrentStep);
        mStepDetailsFragment = new StepDetailsFragment();
        addStepDetailsFragment(fragmentAdded);
        setStepsCounter(mCurrentStep, mStepArrayList.size());
        showOrHideNavigationButtons();
    }

    private void setStepsCounter(int stepId, int totalStepsAmount) {
        mCurrentStep = stepId;
        int currentStep = stepId + 1; // Add +1 to the real values, because of UX

        String counterValue = currentStep + "/" + totalStepsAmount;
        mStepCounterTv.setText(counterValue);
    }

    private void showOrHideNavigationButtons() {
        // Last step verification
        if (mCurrentStep + 1 >= mTotalStepsAmount) {
            nextBtn.setVisibility(View.INVISIBLE);
        } else {
            nextBtn.setVisibility(View.VISIBLE);
        }

        // First step verification
        if (mCurrentStep - 1 < 0) {
            backBtn.setVisibility(View.INVISIBLE);
        } else {
            backBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fm.putFragment(outState, "step_details_fragment", mStepDetailsFragment);
        outState.putBoolean("fragment_added", fragmentAdded);
        outState.putInt("clicked_step_position", mCurrentStep);
        outState.putBoolean("is_test", isTest);
    }
}
