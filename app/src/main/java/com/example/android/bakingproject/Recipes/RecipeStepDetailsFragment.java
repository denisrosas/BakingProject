package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingproject.MainActivity;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class RecipeStepDetailsFragment extends Fragment implements ExoPlayer.EventListener{

    private RecipeStep recipeStep;
    private int recipeId;
    private int stepId;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mExoPlayer;

    static final String STEP_SHORT_DESCRIPTION = "STEP_SHORT_DESCRIPTION";
    static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";
    static final String STEP_VIDEO_URL = "STEP_VIDEO_URL";
    static final String STEP_THUMBNAIL_URL = "STEP_THUMBNAIL_URL";

    private OnChangeStepClickListener mCallBack;

    public interface OnChangeStepClickListener{
        void onPrevNextStepSelected(int recipeId, int newStepId);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        boolean noVideo = false;

        if(savedInstanceState!=null){
            recipeStep = new RecipeStep(0, savedInstanceState.getString(STEP_SHORT_DESCRIPTION),
                    savedInstanceState.getString(STEP_DESCRIPTION),
                    savedInstanceState.getString(STEP_VIDEO_URL),
                    savedInstanceState.getString(STEP_THUMBNAIL_URL));
        }

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        //The JSON received can have 2 fields (videourl and thumbnailurl). Both fields can have a
        // link to a video of the recipe. To make sure it's a link it should start with http or https

        if((recipeStep.getVideoURL().startsWith("https://"))||(recipeStep.getVideoURL().startsWith("http://"))) {
            mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);
            initializePlayer(Uri.parse(recipeStep.getVideoURL()));
        }
        else if ((recipeStep.getThumbnailURL().startsWith("https://"))||(recipeStep.getThumbnailURL().startsWith("http://"))){
            mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);
            initializePlayer(Uri.parse(recipeStep.getThumbnailURL()));
        }
        else {
            noVideo = true;
            mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);
            mPlayerView.setVisibility(View.GONE);
            Log.i("denis","No Video Found in this step. Exoplayer View will be hidden.");
        }

        //if in Landscape, video will play in fullscreen so there is no need to display the textview
        TextView textViewStepDescription = rootView.findViewById(R.id.tv_step_description);
        Button buttonPrevStep = rootView.findViewById(R.id.button_previous_step);
        Button buttonNextStep = rootView.findViewById(R.id.button_next_step);

        if ((getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)&&(!noVideo)){
            textViewStepDescription.setVisibility(View.GONE);
            buttonPrevStep.setVisibility(View.GONE);
            buttonNextStep.setVisibility(View.GONE);
        } else {
            textViewStepDescription.setText(recipeStep.getDescription());

            if(isFirstStep()){
                buttonPrevStep.setVisibility(View.INVISIBLE);

                Log.i("denis", "RecipeStepDetailsFragment - onCreateView() - It's the first step");
                if(!isLastStep()) {
                    buttonNextStep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCallBack.onPrevNextStepSelected(recipeId, (stepId + 1));
                        }
                    });
                } else { //will enter here only if there is one single step in the recipe
                    buttonNextStep.setVisibility(View.INVISIBLE);
                }

            } else if (isLastStep()){

                Log.i("denis", "RecipeStepDetailsFragment - onCreateView() - It's the last step");
                buttonPrevStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallBack.onPrevNextStepSelected(recipeId, (stepId-1));
                    }
                });

                buttonNextStep.setVisibility(View.INVISIBLE);
            } else {

                Log.i("denis", "RecipeStepDetailsFragment - onCreateView() - Not first nor last");
                buttonPrevStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallBack.onPrevNextStepSelected(recipeId, (stepId-1));
                    }
                });

                buttonNextStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallBack.onPrevNextStepSelected(recipeId, (stepId + 1));
                    }
                });
            }
        }

        return rootView;
    }

    private boolean isLastStep() {
        return (stepId + 1) == MainActivity.globalRecipeDetailsList.get(recipeId).getRecipeSteps().size();
    }

    private boolean isFirstStep() {
        return stepId == 0;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (OnChangeStepClickListener) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void releasePlayer() {
        if(mExoPlayer!=null) {
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

            if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }

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

    public void setRecipeStepIds(int recipeId, int stepId) {
        this.recipeId = recipeId;
        this.stepId = stepId;
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
