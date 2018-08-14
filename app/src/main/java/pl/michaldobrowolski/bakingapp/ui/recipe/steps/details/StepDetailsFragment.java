package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    private static final String BUNDLE_STEP_ID_KEY = "step_id_bundle_key";
    private static final String BUNDLE_VIDEO_URL_KEY = "video_url_bundle";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "thumbnail_url_bundle";
    private static final String BUNDLE_TWO_PANE_FLAG_KEY = "two_pane_key";
    private static final String SAVED_INSTANCE_SELECTED_POSITION = "exo_position";

    TextView fullDescTv;
    // Fields
    private Context mContext;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private ImageView mDefaultStepImage;
    public long mLastPlayerPositionTime;
    private UtilityHelper utilityHelper;
    private Bundle stepDetailBundle;
    private boolean mTwoPane;
    private boolean mPlaybackReady = true;
    private int mCurrentWindow;
    public int mStepId;
    // ------------------ End Of Properties ------------------ //


    public Context getmContext() {
        return mContext;
    }

    // Fragment must have: an empty constructor
    public StepDetailsFragment() {
    }

    // Fragment must have: Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(mVideoUrl);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer(mVideoUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
        super.onDestroy();
    }

    // Fragment must have: onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mLastPlayerPositionTime = savedInstanceState.getLong(SAVED_INSTANCE_SELECTED_POSITION);
            mPlaybackReady = savedInstanceState.getBoolean("exo_ready");
            Log.i(TAG, "RESTORED BUNDLE TIME: " + mLastPlayerPositionTime);
        }

        // Initialize utility helper, function removeRedundantCharactersFromText() will be used
        utilityHelper = new UtilityHelper();

        // Get data from StepDetailActivity
        getDataFromBundle();

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
        mStepId = stepDetailBundle.getInt(BUNDLE_STEP_ID_KEY);
        mVideoUrl = stepDetailBundle != null ? stepDetailBundle.getString(BUNDLE_VIDEO_URL_KEY) : null;
        mThumbnailUrl = stepDetailBundle != null ? stepDetailBundle.getString(BUNDLE_VIDEO_THUMB_URL_KEY) : null;
        mTwoPane = stepDetailBundle != null && stepDetailBundle.getBoolean(BUNDLE_TWO_PANE_FLAG_KEY);
        switchThumbUrlToVideoUrl(mThumbnailUrl);
    }

    private void switchThumbUrlToVideoUrl(String thumbUrl) {
        if (!thumbUrl.equals("") && thumbUrl != null) {
            mVideoUrl = thumbUrl;
        }
    }

    private void showOrHideExoPlayer(PlayerView exoPlayerView, ImageView defaultImage) {
        if (mVideoUrl.equals("") || mVideoUrl == null) {
            exoPlayerView.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
            defaultImage.setImageResource(R.drawable.defaultstepimage);
        } else {
            exoPlayerView.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            loadVideoURL(mVideoUrl);
        }
    }

    private void loadVideoURL(String url) {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
        try {
            mVideoUrl = url;
            initializePlayer(mVideoUrl);
        } catch (Exception e) {
            Log.e("StepDetailFragment", " -- EXOPLAYER ERROR --" + e.toString());
        }
    }

    void initializePlayer(String videoUrl) {   //boolean resumeOnly
        Uri videoURI = Uri.parse(videoUrl);

        if (mExoPlayer == null) {

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);        // Handling full screen mode
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);                     // Handling full screen mode

            MediaSource mediaSource = buildMediaSourceForPlayer(videoURI);
            mExoPlayer.prepare(mediaSource);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.seekTo(mCurrentWindow, mLastPlayerPositionTime);
            Log.i(TAG, "Player time to set: " + mLastPlayerPositionTime);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private MediaSource buildMediaSourceForPlayer(Uri mediaUri) {
        String userAgent = Util.getUserAgent(mContext, "BakingApp");
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent)).
                createMediaSource(mediaUri);
    }

    public void loadData(Step step) {
        mVideoUrl = step.getmVideoURL();
        mDescription = utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", step.getmDescription());
        fullDescTv.setText(mDescription);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            mPlaybackReady = mExoPlayer.getPlayWhenReady();
            mLastPlayerPositionTime = mExoPlayer.getCurrentPosition();
        }
        outState.putLong(SAVED_INSTANCE_SELECTED_POSITION, mLastPlayerPositionTime);
        outState.putBoolean("exo_ready", mPlaybackReady);
    }

    // Release player
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlaybackReady = mExoPlayer.getPlayWhenReady();
            mLastPlayerPositionTime = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
