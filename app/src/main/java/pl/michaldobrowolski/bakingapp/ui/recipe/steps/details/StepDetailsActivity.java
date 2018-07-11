package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.StepsActivity;

public class StepDetailsActivity extends AppCompatActivity {
    final static String TAG = StepsActivity.class.getSimpleName();
    private static final String MAIN_BUNDLE_KEY = "step_detail";
    private static final String BUNDLE_STEP_ID_KEY = "step_id";
    private static final String BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY = "total_steps";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name";
    private static final String BUNDLE_STEP_FULL_DESC_KEY = "step_full_desc";
    private static final String BUNDLE_VIDEO_URL_KEY = "step_video_url";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "step_video_thumbnail_url";
    // Properties
    private int mStepId;
    private int mTotalStepsAmount;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mRecipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_step_detail);

        // Get data from bundle
        getStepDetailsDataFromBundle(MAIN_BUNDLE_KEY,
                BUNDLE_STEP_ID_KEY,
                BUNDLE_STEP_FULL_DESC_KEY,
                BUNDLE_VIDEO_URL_KEY,
                BUNDLE_VIDEO_THUMB_URL_KEY,
                BUNDLE_RECIPE_NAME_KEY,
                BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY);

        addStepDetailsFragment();
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

        // Deliver data to specific fragments
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.step_details_container, stepDetailsFragment)
                .add(R.id.step_full_desc_container, descriptionFragment)
                .add(R.id.exo_player_container, exoPlayerFragment)
                .commit();
    }


    private void getStepDetailsDataFromBundle(String mainBundleKey, String stepIdKey, String stepFullDescKey,
                                              String stepVideoUrlKey, String stepVideoThumbUrlKey, String recipeNameKey, String totalStepsAmountKey) {
        Bundle stepDetailBundle = getIntent().getExtras();
        if (stepDetailBundle != null) {
            if (stepDetailBundle.containsKey(mainBundleKey)) {
                stepDetailBundle = stepDetailBundle.getBundle(mainBundleKey);
                mStepId = Objects.requireNonNull(stepDetailBundle).getInt(stepIdKey);
                mDescription = stepDetailBundle.getString(stepFullDescKey);
                mVideoUrl = stepDetailBundle.getString(stepVideoUrlKey);
                mThumbnailUrl = stepDetailBundle.getString(stepVideoThumbUrlKey);
                mRecipeName = stepDetailBundle.getString(recipeNameKey);
                mTotalStepsAmount = stepDetailBundle.getInt(totalStepsAmountKey);
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


}
