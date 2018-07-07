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
    private static final String BUNDLE_STEP_FULL_DESC_KEY = "step_full_desc";
    private static final String BUNDLE_VIDEO_URL_KEY = "step_video_url";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "step_video_thumbnail_url";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name";

    private Bundle stepDetailBundle;
    private int mStepId;
    private int totalStepsAmount;
    private int currentStep; // Use this to navigation and displaying step# on the details screen
    private String mDescription;
    private String videoUrl;
    private String thumbnailURL;  // TODO: make a method for transfer thumbnailURL to videoUrl
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
                BUNDLE_RECIPE_NAME_KEY);

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
        StepDetailsDescFragment descriptionFragment = new StepDetailsDescFragment();
        descriptionFragment.setDescription(mDescription);
        descriptionFragment.setRecipeName(mRecipeName);

        // COMPLETED: here add fragment for exo player
        StepDetailsExoPlayerFragment stepDetailsExoPlayerFragment = new StepDetailsExoPlayerFragment();
        stepDetailsExoPlayerFragment.setVideoUrl(videoUrl);
        stepDetailsExoPlayerFragment.setThumbnailURL(thumbnailURL);


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.step_full_desc_container, descriptionFragment)
                .add(R.id.exo_player_container, stepDetailsExoPlayerFragment)
                .commit();
    }

    /**
     * @param mainBundleKey        - main key for extracting bundle
     * @param stepIdKey            - key for getting an Id of step
     * @param stepFullDescKey      - key for getting a full description of step
     * @param stepVideoUrlKey      - key for getting a video url of step
     * @param stepVideoThumbUrlKey - key for getting a thumbnail url of video url of step
     * @param recipeNameKey        - key for getting name of recipe which contains specific step
     **/
    private void getStepDetailsDataFromBundle(String mainBundleKey, String stepIdKey, String stepFullDescKey,
                                              String stepVideoUrlKey, String stepVideoThumbUrlKey, String recipeNameKey) {
        stepDetailBundle = getIntent().getExtras();
        if (stepDetailBundle != null) {
            if (stepDetailBundle.containsKey(mainBundleKey)) {
                stepDetailBundle = stepDetailBundle.getBundle(mainBundleKey);
                mStepId = Objects.requireNonNull(stepDetailBundle).getInt(stepIdKey);
                mDescription = stepDetailBundle.getString(stepFullDescKey);
                videoUrl = stepDetailBundle.getString(stepVideoUrlKey);
                thumbnailURL = stepDetailBundle.getString(stepVideoThumbUrlKey);
                mRecipeName = stepDetailBundle.getString(recipeNameKey);
            } else {
                Log.i(TAG, "Cannot get object from bundle. Incorrect bundle key string.");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }

    }

}
