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
    private static final String BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY = "total_steps";

    private Bundle stepDetailBundle;
    private int mStepId;
    private int mTotalStepsAmount;
    private int mCurrentStep; // Use this to navigation and displaying step# on the details scree
    private String mDescription;
    private String videoUrl;
    private String thumbnailURL;  // COMPLETED: make a method for transfer thumbnailURL to videoUrl
    private String mRecipeName;
    private TextView mStepCounterTv;
    private ImageButton backBtn;
    private ImageButton nextBtn;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_step_detail);
        
        backBtn = findViewById(R.id.button_previous_step);
        nextBtn = findViewById(R.id.button_next_step);
        
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StepDetailsActivity.this, "BACK button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StepDetailsActivity.this, "NEXT button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Get data from bundle
        getStepDetailsDataFromBundle(MAIN_BUNDLE_KEY,
                BUNDLE_STEP_ID_KEY,
                BUNDLE_STEP_FULL_DESC_KEY,
                BUNDLE_VIDEO_URL_KEY,
                BUNDLE_VIDEO_THUMB_URL_KEY,
                BUNDLE_RECIPE_NAME_KEY,
                BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY);

        addStepDetailsFragment();
        setStepsCounter(mStepId, mTotalStepsAmount);
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
     * @param totalStepsAmountKey  - key for getting a total amount of steps
     **/
    private void getStepDetailsDataFromBundle(String mainBundleKey, String stepIdKey, String stepFullDescKey,
                                              String stepVideoUrlKey, String stepVideoThumbUrlKey, String recipeNameKey, String totalStepsAmountKey) {
        stepDetailBundle = getIntent().getExtras();
        if (stepDetailBundle != null) {
            if (stepDetailBundle.containsKey(mainBundleKey)) {
                stepDetailBundle = stepDetailBundle.getBundle(mainBundleKey);
                mStepId = Objects.requireNonNull(stepDetailBundle).getInt(stepIdKey);
                mDescription = stepDetailBundle.getString(stepFullDescKey);
                videoUrl = stepDetailBundle.getString(stepVideoUrlKey);
                thumbnailURL = stepDetailBundle.getString(stepVideoThumbUrlKey);
                mRecipeName = stepDetailBundle.getString(recipeNameKey);
                mTotalStepsAmount = stepDetailBundle.getInt(totalStepsAmountKey);
            } else {
                Log.i(TAG, "Cannot get object from bundle. Incorrect bundle key string.");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }

    }

    /**
     * This method transfer URL form thumbnail to the videoUrl because this is the same link
     * and clip could be played without issues no matter which link will be used. Transferring data
     * is makeing only for makeing code more consistent.
     * @param thumbUrl - URL of thumbnail which comes from bundle
     * @param videoUrl - URL of video clip which will be used
     */
    private void switchThumbUrlToVideoUrl(String thumbUrl, String videoUrl){
        if(thumbUrl != null && videoUrl == null ){
            videoUrl = thumbnailURL;
        }

    }

    private void setStepsCounter(int stepId, int totalStepsAmount){
        mStepCounterTv = findViewById(R.id.text_step_counter);
        mCurrentStep = stepId;

        // Add +1 to the real values, because of UX
        int currentStep = stepId + 1;
        int lastStepNumber = totalStepsAmount;

        String counterValue = currentStep + "/" + lastStepNumber;
        mStepCounterTv.setText(counterValue);

    }

}
