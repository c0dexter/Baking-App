package pl.michaldobrowolski.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

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
    private String mDescription;
    private String videoUrl;
    private String thumbnailURL;
    private String mRecipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_step_detail);

        stepDetailBundle = getIntent().getExtras();
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
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(stepDetailBundle);


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.steps_detail_container, stepDetailsFragment)
                .commit();
    }


}
