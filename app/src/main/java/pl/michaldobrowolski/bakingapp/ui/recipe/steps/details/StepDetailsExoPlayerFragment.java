package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

public class StepDetailsExoPlayerFragment extends Fragment {

    // -------------------- Properties --------------------//
    final static String TAG = StepDetailsDescFragment.class.getSimpleName();
    private static final String BUNDLE_VIDEO_URL_KEY = "step_video_url";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "step_video_thumbnail_url";

    private Context mContext;
    private String mVideoUrl;
    private String mThumbnailUrl;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public StepDetailsExoPlayerFragment() {
    }

    // Fragment must have: Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set a root view
        final View rootView = inflater.inflate(R.layout.fragment_step_detail_player_item, container, false); //fragment_step_detail

        // Get data from StepDetailActivity
        getDataFromBundle();

        // Verify and populate a correct Video URL by using thumb url (for consistence)
        switchThumbUrlToVideoUrl(mThumbnailUrl, mVideoUrl);

        // TODO: Here should be make some EXO PLAYER stuff

        return rootView;
    }


    private void switchThumbUrlToVideoUrl(String thumbUrl, String videoUrl) { // TODO: check this!!!
        if (thumbUrl != null && videoUrl == null) {
            videoUrl = thumbUrl;
        }
    }


    private void getDataFromBundle() {
        Bundle exoPlayerBundle = getArguments();
        mVideoUrl = Objects.requireNonNull(exoPlayerBundle).getString(BUNDLE_VIDEO_URL_KEY);
        mThumbnailUrl = Objects.requireNonNull(exoPlayerBundle).getString(BUNDLE_VIDEO_THUMB_URL_KEY);

    }
}
