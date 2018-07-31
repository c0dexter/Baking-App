package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.utils.UtilityHelper;

public class StepDetailsFragment extends Fragment {
    final static String TAG = StepDetailsFragment.class.getSimpleName();

    // -------------------- Properties --------------------//
    // Bundle keys
    private static final String BUNDLE_DESCRIPTION_KEY = "desc_bundle";
    private static final String BUNDLE_VIDEO_URL_KEY = "video_url_bundle";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "thumbnail_url_bundle";
    private static final String BUNDLE_TWO_PANE_FLAG_KEY = "two_pane_key";
    private static final String SAVED_INSTANCE_SELECTED_POSITION = "exo_position";

    TextView fullDescTv;
    StepDetailsActivity stepDetailsActivity;
    // Fields
    private Context mContext;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mDefaultStepImage;
    private long mMediaLength;
    private UtilityHelper utilityHelper;
    private Bundle stepDetailBundle;
    private boolean mTwoPane;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public StepDetailsFragment() {
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

        if (savedInstanceState != null) {
            mMediaLength = savedInstanceState.getLong(SAVED_INSTANCE_SELECTED_POSITION, mMediaLength);
        }

        // Initialize utility helper, function removeRedundantCharactersFromText() will be used
        utilityHelper = new UtilityHelper();
        stepDetailsActivity = new StepDetailsActivity();

        // Get data from StepDetailActivity
        getDataFromBundle();

        // Verify and populate a correct Video URL by using thumb url (for consistence)
        switchThumbUrlToVideoUrl(mThumbnailUrl);

        // Set a root view
        View rootView = checkScreenOrientationAndSetRootView(inflater, container);

        // Mapping views
        mPlayerView = rootView.findViewById(R.id.playerView);
        mDefaultStepImage = rootView.findViewById(R.id.defaultStepImage);

        // ExoPlayer
        showOrHideExoPlayer(mPlayerView, mDefaultStepImage);

        return rootView;
    }

    private View checkScreenOrientationAndSetRootView(LayoutInflater inflater, ViewGroup container) {
        View rootView;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !mTwoPane) {

            // Play a video clip on the full screen, hide redundant elements in a full screen mode
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
            rootView = getActivity().getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN;
            rootView.setSystemUiVisibility(uiOptions);
            rootView = inflater.inflate(R.layout.fragment_step_detail_video_and_desc_horizontal, container, false);

        } else {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
            rootView = inflater.inflate(R.layout.fragment_step_detail_video_and_desc_vertical, container, false);

            fullDescTv = rootView.findViewById(R.id.text_step_full_desc);
            fullDescTv.setText(mDescription);
        }
        return rootView;
    }

    private void getDataFromBundle() {
        stepDetailBundle = getArguments();
        mDescription = utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", stepDetailBundle != null ? stepDetailBundle.getString(BUNDLE_DESCRIPTION_KEY) : null);
        mVideoUrl = stepDetailBundle != null ? stepDetailBundle.getString(BUNDLE_VIDEO_URL_KEY) : null;
        mThumbnailUrl = stepDetailBundle != null ? stepDetailBundle.getString(BUNDLE_VIDEO_THUMB_URL_KEY) : null;
        mTwoPane = stepDetailBundle != null && stepDetailBundle.getBoolean(BUNDLE_TWO_PANE_FLAG_KEY);
    }

    private void switchThumbUrlToVideoUrl(String thumbUrl) {
        if (!thumbUrl.equals("") && thumbUrl != null) {
            mVideoUrl = thumbUrl;
        }
    }

    private void showOrHideExoPlayer(SimpleExoPlayerView exoPlayerView, ImageView defaultImage) {
        if (mVideoUrl.equals("") || mVideoUrl == null) {
            exoPlayerView.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
            defaultImage.setImageResource(R.drawable.defaultstepimage);
        } else {
            exoPlayerView.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            initializePlayer(Uri.parse(mVideoUrl));
        }
    }

    void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the SimpleExoPlayer

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);        // Handling full screen mode
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);                     // Handling full screen mode
            mPlayerView.setPlayer(mExoPlayer);
            loadVideo(mediaUri);
        }
    }

    private void loadVideo(Uri mediaUri) {
        MediaSource mediaSource = getMediaSourceForPlayer(mediaUri);
        if (mMediaLength != C.TIME_UNSET) mExoPlayer.seekTo(mMediaLength);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private MediaSource getMediaSourceForPlayer(Uri mediaUri) {
        // Prepare MediaSource
        String userAgent = Util.getUserAgent(mContext, "BakingApp");
        return new ExtractorMediaSource(
                mediaUri,
                new DefaultDataSourceFactory(mContext, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
    }

    public void loadData(Step step) {
        mVideoUrl = step.getmVideoURL();
        mDescription = utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", step.getmDescription());
        fullDescTv.setText(mDescription);
        showOrHideExoPlayer(mPlayerView, mDefaultStepImage);
        loadVideo(Uri.parse(mVideoUrl));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mMediaLength = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null) {
            initializePlayer(Uri.parse(mVideoUrl));
        }
    }

    @Override
    public void onDestroy() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("exo_position", mMediaLength);
    }
}
