package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import pl.michaldobrowolski.bakingapp.R;

public class StepDetailsDescFragment extends Fragment {

    // -------------------- Properties --------------------//
    final static String TAG = StepDetailsDescFragment.class.getSimpleName();
    // Bundle keys: Video
    private static final String BUNDLE_VIDEO_URL_KEY = "video_url_bundle";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "thumbnail_url_bundle";

    private static final String BUNDLE_STEP_ID_KEY = "step_id_bundle_key";
    private static final String BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY = "total_steps_bundle_key";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name_bundle_key";
    private static final String BUNDLE_DESCRIPTION_KEY = "desc_bundle";
    // Fields
    private Context mContext;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mDefaultStepImage;
    private long mMediaLength;
    private String mDescription;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public StepDetailsDescFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_step_detail_video_and_desc_vertical, container, false); //fragment_step_detail_vertical

        // Get data from StepDetailActivity
        getDataFromBundle();

        // Mapping views
        TextView fullDescTv = rootView.findViewById(R.id.text_step_full_desc);
        fullDescTv.setText(mDescription);

        return rootView;
    }

    private void getDataFromBundle() {
        Bundle stepDetailBundle = getArguments();
        if (stepDetailBundle != null && stepDetailBundle.containsKey(BUNDLE_DESCRIPTION_KEY)) {
            mDescription = stepDetailBundle.getString(BUNDLE_DESCRIPTION_KEY);
        } else {
            Log.i(TAG, "Incorrect bundle key!");
        }
    }

}




