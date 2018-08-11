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
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
    // Fields
    private Context mContext;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mDefaultStepImage;
    private long mLastPlayerPositionTime;
    private UtilityHelper utilityHelper;
    private Bundle stepDetailBundle;
    private boolean mTwoPane;
    private MediaSource mMediaSource;
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

    private void showOrHideExoPlayer(SimpleExoPlayerView exoPlayerView, ImageView defaultImage) {
        if (mVideoUrl.equals("") || mVideoUrl == null) {
            exoPlayerView.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
            defaultImage.setImageResource(R.drawable.defaultstepimage);
        } else {
            exoPlayerView.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            loadVideoURL(mVideoUrl); // TODO: check this
        }
    }

    private void loadVideoURL(String url) {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
        }
        try {
            mVideoUrl = url;
            initializePlayer(mVideoUrl, false);
        } catch (Exception e) {
            Log.e("StepDetailFragment", " -- EXOPLAYER ERROR --" + e.toString());
        }
    }

    void initializePlayer(String videoUrl, boolean resumeOnly) {
        Uri videoURI = Uri.parse(videoUrl);

        if ((mExoPlayer != null && resumeOnly) == true) {
            return;
        }

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);

        mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);        // Handling full screen mode
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);                     // Handling full screen mode

        mMediaSource = getMediaSourceForPlayer(videoURI);
        mExoPlayer.prepare(mMediaSource);
        //
        // mExoPlayer.setPlayWhenReady(true);

        mExoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_BUFFERING:
                        break;
                    case ExoPlayer.STATE_ENDED:
                        mExoPlayer.seekTo(0);
                        break;
                    case ExoPlayer.STATE_IDLE:
                        break;
                    case ExoPlayer.STATE_READY:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });

        if (mLastPlayerPositionTime != 0) {
            mExoPlayer.seekTo(mLastPlayerPositionTime);
        } else {
            mExoPlayer.seekTo(0);
        }
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
    }


    public long getCurrentPlayerPosition() {
        if (mExoPlayer != null)
            return mExoPlayer.getCurrentPosition();
        else
            return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
//            return;
//        }
//        if (mVideoUrl == null || mVideoUrl.length() == 0) return;
        initializePlayer(mVideoUrl, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLastPlayerPositionTime = getCurrentPlayerPosition();
        if (mExoPlayer != null) {
//            if (Util.SDK_INT <= 23) {
//                releasePlayer();
//            }
            releasePlayer();
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("exo_position", mLastPlayerPositionTime);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLastPlayerPositionTime = savedInstanceState.getLong(SAVED_INSTANCE_SELECTED_POSITION, mLastPlayerPositionTime);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    // Release player
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
