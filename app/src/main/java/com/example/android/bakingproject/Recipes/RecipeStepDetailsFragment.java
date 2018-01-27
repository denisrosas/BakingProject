package com.example.android.bakingproject.Recipes;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingproject.R;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailsFragment extends Fragment implements ExoPlayer.EventListener{

    private RecipeStep recipeStep;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mExoPlayer;

    static final String STEP_SHORT_DESCRIPTION = "STEP_SHORT_DESCRIPTION";
    static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";
    static final String STEP_VIDEO_URL = "STEP_VIDEO_URL";
    static final String STEP_THUMBNAIL_URL = "STEP_THUMBNAIL_URL";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            recipeStep = new RecipeStep(0, savedInstanceState.getString(STEP_SHORT_DESCRIPTION),
                    savedInstanceState.getString(STEP_DESCRIPTION),
                    savedInstanceState.getString(STEP_VIDEO_URL),
                    savedInstanceState.getString(STEP_THUMBNAIL_URL));
        }

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        if(recipeStep.getVideoURL().startsWith("https://")) {
            mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);
            initializePlayer(Uri.parse(recipeStep.getVideoURL()));
        }
        else if (recipeStep.getThumbnailURL().startsWith("https://")){
            mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);
            initializePlayer(Uri.parse(recipeStep.getThumbnailURL()));
        } else {
            mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);
            mPlayerView.setVisibility(View.GONE);
            Log.i("denis","deu false nos dois!!");
        }

        TextView textViewStepDescription = rootView.findViewById(R.id.tv_step_description);
        textViewStepDescription.setText(recipeStep.getDescription());

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void releasePlayer() {
        if(mExoPlayer!=null) {
            //mPlayerView
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this.getActivity(), "BakingProject");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    public void setRecipeStep(RecipeStep recipeStep) {
        this.recipeStep = recipeStep;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STEP_SHORT_DESCRIPTION, recipeStep.getShortDescription());
        outState.putString(STEP_DESCRIPTION, recipeStep.getDescription());
        outState.putString(STEP_VIDEO_URL, recipeStep.getVideoURL());
        outState.putString(STEP_THUMBNAIL_URL, recipeStep.getThumbnailURL());
    }



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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
