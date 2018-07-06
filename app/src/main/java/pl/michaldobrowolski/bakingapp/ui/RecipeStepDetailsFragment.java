package pl.michaldobrowolski.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

public class RecipeStepDetailsFragment extends Fragment {

    // -------------------- Properties --------------------//
    final static String TAG = RecipeStepDetailsFragment.class.getSimpleName();
    // Bundle keys
    private static final String MAIN_BUNDLE_KEY = "step_detail";
    private static final String BUNDLE_STEP_ID_KEY = "step_id";
    private static final String BUNDLE_STEP_FULL_DESC_KEY = "step_full_desc";
    private static final String BUNDLE_VIDEO_URL_KEY = "step_video_url";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "step_video_thumbnail_url";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name";
    private Context mContext;
    private Bundle stepDetailBundle;
    private int mStepId;
    private String mDescription;
    private String videoUrl;
    private String thumbnailURL;
    private String mRecipeName;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public RecipeStepDetailsFragment() {
    }

    // Fragment must have: Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // Fragment must have: onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set a root view
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        getStepDetailsDataFromBundle(MAIN_BUNDLE_KEY,
                BUNDLE_STEP_ID_KEY,
                BUNDLE_STEP_FULL_DESC_KEY,
                BUNDLE_VIDEO_URL_KEY,
                BUNDLE_VIDEO_THUMB_URL_KEY,
                BUNDLE_RECIPE_NAME_KEY);

        // Set a title on NavBar
        ((StepDetailActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName + "'s instructions");

        // Mapping views
        // TODO: Here should be method for add/replace specific fragment


        return rootView;
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
        stepDetailBundle = getArguments();
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




