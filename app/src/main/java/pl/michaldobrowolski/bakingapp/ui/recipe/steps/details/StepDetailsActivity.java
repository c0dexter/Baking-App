package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.StepsActivity;

public class StepDetailsActivity extends AppCompatActivity implements StepDetailsFragment.OnBackButtonClickedListener, StepDetailsFragment.OnNextButtonClickedListener {
    final static String TAG = StepsActivity.class.getSimpleName();
    private static final String MAIN_BUNDLE_KEY = "step_detail";
    private static final String BUNDLE_ARRAY_STEPS_KEY = "step_array_bundle_key";
    private static final String BUNDLE_STEP_ID_KEY = "step_position_bundle_key";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name_bundle_key";
    // Properties
    private int mClickedStepPosition;
    private int mAdjustedStepPosition;
    private String mRecipeName;
    private ArrayList<Step> mStepArrayList;
    private int mTotalStepsAmount;
    private int mStepId;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private FragmentManager fragmentManager;

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
                BUNDLE_RECIPE_NAME_KEY);

        getDataForSpecificStep(mClickedStepPosition);
        addStepDetailsFragment();
    }

    private void getDataForSpecificStep(int position) {
        mTotalStepsAmount = mStepArrayList.size();
        mDescription = mStepArrayList.get(position).getmDescription();
        mVideoUrl = mStepArrayList.get(position).getmVideoURL();
        mThumbnailUrl = mStepArrayList.get(position).getThumbnailURL();
        mStepId = mStepArrayList.get(position).getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private void addStepDetailsFragment() {
        // Main step details fragment
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(makeStepDetailsBundle());

        // Full description fragment
        StepDetailsDescFragment descriptionFragment = new StepDetailsDescFragment();
        descriptionFragment.setArguments(makeFullDescBundle());

        // ExoPlayer Fragment
        StepDetailsExoPlayerFragment exoPlayerFragment = new StepDetailsExoPlayerFragment();
        exoPlayerFragment.setArguments(makeExoPlayerBundle());

        // Choose a method delivery data to fragment. For new view: "add", for existed view "replace"
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    // Deliver data to specific fragments
                    .add(R.id.step_details_container, stepDetailsFragment)
                    .add(R.id.step_full_desc_container, descriptionFragment)
                    .add(R.id.exo_player_container, exoPlayerFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
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
        fullDescBundle.putString("desc_bundle", mDescription);
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
        Toast.makeText(this, "BACK clicked, position: " + newPositionValue, Toast.LENGTH_SHORT).show();
        mAdjustedStepPosition = newPositionValue;
        getDataForSpecificStep(mAdjustedStepPosition);
        addStepDetailsFragment();
    }

    @Override
    public void onNextButtonClicked(int newPositionValue) {
        Toast.makeText(this, "NEXT clicked, position: " + newPositionValue, Toast.LENGTH_SHORT).show();
        mAdjustedStepPosition = newPositionValue;
        getDataForSpecificStep(mAdjustedStepPosition);
        addStepDetailsFragment();
    }


}
