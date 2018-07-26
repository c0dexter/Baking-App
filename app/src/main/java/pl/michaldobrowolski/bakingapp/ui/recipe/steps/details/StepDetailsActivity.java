package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.StepsActivity;
import pl.michaldobrowolski.bakingapp.utils.UtilityHelper;


public class StepDetailsActivity extends AppCompatActivity { //implements OnBackButtonClickedListener, OnNextButtonClickedListener
    // -------------------- Properties --------------------//
    final static String TAG = StepsActivity.class.getSimpleName();
    // Bundle keys
    private static final String MAIN_BUNDLE_KEY = "step_detail";
    private static final String BUNDLE_ARRAY_STEPS_KEY = "step_array_bundle_key";
    private static final String BUNDLE_STEP_ID_KEY = "step_position_bundle_key";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name_bundle_key";
    // Fields
    private int mCurrentStep;
    private String mRecipeName;
    private ArrayList<Step> mStepArrayList;
    private int mTotalStepsAmount;
    private int mStepId;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private StepDetailsFragment stepDetailsFragment;
    private UtilityHelper utilityHelper = new UtilityHelper();
    private boolean fragmentAdded;

    public TextView mStepCounterTv;
    public ImageButton backBtn;
    public ImageButton nextBtn;
    // ------------------ End Of Properties ------------------ //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_step_detail);

        // Mapping
        mStepCounterTv = findViewById(R.id.text_step_counter);
        backBtn = findViewById(R.id.button_previous_step);
        nextBtn = findViewById(R.id.button_next_step);
;

        // Get data from bundle
        getStepDetailsDataFromBundle(
                MAIN_BUNDLE_KEY,
                BUNDLE_ARRAY_STEPS_KEY,
                BUNDLE_STEP_ID_KEY,
                BUNDLE_RECIPE_NAME_KEY);

        // Check if saved instance state exist and set a fragmentAdded flag
        if (savedInstanceState != null) {
            fragmentAdded = savedInstanceState.getBoolean("fragment_added");
            mCurrentStep = savedInstanceState.getInt("clicked_step_position");
        }

        getDataForSpecificStep(mCurrentStep);
        addStepDetailsFragment(fragmentAdded);

        // Logic for UI elements
        setStepsCounter(mCurrentStep, mStepArrayList.size());
        showOrHideNavigationButtons();

        // Navigation buttons logic
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked(mCurrentStep + 1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClicked(mCurrentStep - 1);
            }
        });
    }

    public void getDataForSpecificStep(int position) {
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

        // Choose a method delivery data to fragment. For new view: "add", for existed view "replace"
        FragmentManager fm = getSupportFragmentManager();
        if (!fragmentExist) {
            fm.beginTransaction()
                    .add(R.id.step_details_container, stepDetailsFragment)
                    .commit();
            fragmentAdded = true;
        } else {
            fm.beginTransaction()
                    .replace(R.id.step_details_container, stepDetailsFragment)
                    .commit();
        }
    }

    private void getStepDetailsDataFromBundle(String mainBundleKey, String StepsArrayKey, String stepPositionClicked, String recipeNameKey) {
        Bundle stepDetailBundle = getIntent().getExtras();
        if (stepDetailBundle != null) {
            if (stepDetailBundle.containsKey(mainBundleKey)) {
                stepDetailBundle = stepDetailBundle.getBundle(mainBundleKey);
                mStepArrayList = Objects.requireNonNull(stepDetailBundle).getParcelableArrayList(StepsArrayKey);
                mCurrentStep = Objects.requireNonNull(stepDetailBundle).getInt(stepPositionClicked);
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
        stepDetailsBundle.putString("desc_bundle", utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", mDescription));
        stepDetailsBundle.putString("video_url_bundle", mVideoUrl);
        stepDetailsBundle.putString("thumbnail_url_bundle", mThumbnailUrl);

        return stepDetailsBundle;
    }

    public void onBackButtonClicked(int newPositionValue) {
        Toast.makeText(this, "Previous step", Toast.LENGTH_SHORT).show();
        mCurrentStep = newPositionValue;
        getDataForSpecificStep(mCurrentStep);
        addStepDetailsFragment(fragmentAdded);
        setStepsCounter(mCurrentStep, mStepArrayList.size());
        showOrHideNavigationButtons();
    }

    public void onNextButtonClicked(int newPositionValue) {
        Toast.makeText(this, "Next step", Toast.LENGTH_SHORT).show();
        mCurrentStep = newPositionValue;
        getDataForSpecificStep(mCurrentStep);
        addStepDetailsFragment(fragmentAdded);
        setStepsCounter(mCurrentStep, mStepArrayList.size());
        showOrHideNavigationButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stepDetailsFragment.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        stepDetailsFragment.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragment_added", fragmentAdded);
        outState.putInt("clicked_step_position", mCurrentStep);
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
}
