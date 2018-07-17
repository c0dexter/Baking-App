package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class StepDetailsExoPlayerFragment extends Fragment {

    // -------------------- Properties --------------------//
    final static String TAG = StepDetailsDescFragment.class.getSimpleName();
    private static final String BUNDLE_VIDEO_URL_KEY = "video_url_bundle";
    private static final String BUNDLE_VIDEO_THUMB_URL_KEY = "thumbnail_url_bundle";

    private Context mContext;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mDefaultStepImage;
    private long media_length;
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
        final View rootView = inflater.inflate(R.layout.fragment_step_detail_player_item, container, false);

        // Mapping views
        mPlayerView = rootView.findViewById(R.id.playerView);
        mDefaultStepImage = rootView.findViewById(R.id.defaultStepImage);

        // Get data from StepDetailActivity
        getDataFromBundle();

        // Verify and populate a correct Video URL by using thumb url (for consistence)
        switchThumbUrlToVideoUrl(mThumbnailUrl);

        // TODO: Here should be make some EXO PLAYER stuff
        showOrHideExoPlayer(mPlayerView, mDefaultStepImage);

        return rootView;
    }

    private void switchThumbUrlToVideoUrl(String thumbUrl) {
        if (!thumbUrl.equals("")) {
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

    private void getDataFromBundle() {
        Bundle exoPlayerBundle = getArguments();
        if (exoPlayerBundle.containsKey(BUNDLE_VIDEO_URL_KEY) && (exoPlayerBundle.containsKey(BUNDLE_VIDEO_THUMB_URL_KEY))) {
            mVideoUrl = Objects.requireNonNull(exoPlayerBundle).getString(BUNDLE_VIDEO_URL_KEY);
            mThumbnailUrl = Objects.requireNonNull(exoPlayerBundle).getString(BUNDLE_VIDEO_THUMB_URL_KEY);
        } else {
            Log.i(TAG, "ERROR, bundle key/keys aren't mach");
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the SimpleExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);        // Handling full screen mode
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);                     // Handling full screen mode
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare MediaSource
            String userAgent = Util.getUserAgent(mContext, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    mediaUri,
                    new DefaultDataSourceFactory(mContext, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            media_length = mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.seekTo(media_length);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
}
