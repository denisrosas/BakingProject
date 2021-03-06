package com.example.android.bakingproject.Recipes;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingproject.MainActivity;
import com.example.android.bakingproject.R;
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
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

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

    static final String RECIPE_ID = "RECIPE_ID";
    static final String STEP_ID = "STEP_ID";
    static final String SEEK_TIME_PLAYER = "SEEK_TIME_PLAYER";

    long seekTimePlayer = C.TIME_UNSET;

    private OnChangeStepClickListener mCallBack;

    public interface OnChangeStepClickListener{
        void onPrevNextStepSelected(int recipeId, int newStepId);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        boolean noVideo = false;
        boolean noImage = false;

        if(savedInstanceState!=null){
            recipeStep = new RecipeStep(0, savedInstanceState.getString(STEP_SHORT_DESCRIPTION),
                    savedInstanceState.getString(STEP_DESCRIPTION),
                    savedInstanceState.getString(STEP_VIDEO_URL),
                    savedInstanceState.getString(STEP_THUMBNAIL_URL));
            recipeId = savedInstanceState.getInt(RECIPE_ID);
            stepId = savedInstanceState.getInt(STEP_ID);
            seekTimePlayer = savedInstanceState.getLong(SEEK_TIME_PLAYER);
        }

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        //The JSON received can have 2 fields (videourl and thumbnailurl). Both fields can have a
        // link to a video of the recipe. To make sure it's a link it should start with http or https
        //in case
        mPlayerView = rootView.findViewById(R.id.exoplayer_step_view);

        //if in TwoPaneMode and landscape, Exoplayer video height will be half of screen height
        if((MainActivity.mTwoPaneMode)&&(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)){
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mPlayerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)((size.y)/1.6) ));
        }

        //check if there is video in VideoURL and ThumbNailURL fields received from Json
        //if the url is of a video, start ExoPlayer. If not, hide the player view
        if((isUrl(recipeStep.getVideoURL()))&&(isUrlOfVideo(recipeStep.getVideoURL()))) {
            initializePlayer(Uri.parse(recipeStep.getVideoURL()));
        }
        else {
            noVideo = true;
            Log.i("denis","No Video Found in this step. Exoplayer View will be hidden.");
        }

        //check if the Thumbnail is of a Image. If is, set the ImageView
        if ((isUrl(recipeStep.getThumbnailURL()))&&(isUrlOfImage(recipeStep.getThumbnailURL()))){

            ImageView imageView = rootView.findViewById(R.id.iv_recipe_thumbnail);
            Picasso.with(container.getContext()).load(recipeStep.getThumbnailURL()).into(imageView);

        } else{
            noImage = true;
        }

        defineViewsVisibility(rootView, noVideo, noImage);
        return rootView;
    }

    //try to build a URL using the String. If there is no exception, we know it's a valid URL
    public static boolean isUrl(String videoThumbUrl) {
        URL url;

        Uri.Builder UriBuilder = Uri.parse(videoThumbUrl).buildUpon();
        Uri builtUri = UriBuilder.build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean isUrlOfVideo(String url) {

        url = url.toLowerCase();
        return (url.endsWith(".mp4")) ||
                (url.endsWith(".mpg")) ||
                (url.endsWith(".3gp"));
    }

    public static boolean isUrlOfImage(String url) {

        url = url.toLowerCase();
        return (url.endsWith(".jpg")) ||
                (url.endsWith(".gif")) ||
                (url.endsWith(".png"));

    }

    //Defines the Visibility of tv_step_description, button_previous_step and button_next_step

    private void defineViewsVisibility(View rootView, boolean noVideo, boolean noImage){
        //if in Landscape and not in two-Pane-mode, video will play in fullscreen
        // so there is no need to display the textview
        TextView textViewStepDescription = rootView.findViewById(R.id.tv_step_description);
        Button buttonPrevStep = rootView.findViewById(R.id.button_previous_step);
        Button buttonNextStep = rootView.findViewById(R.id.button_next_step);

        if(noImage) {
            ImageView imageView = rootView.findViewById(R.id.iv_recipe_thumbnail);
            imageView.setVisibility(View.GONE);
        }
        if(noVideo)
            mPlayerView.setVisibility(View.GONE);


        if ((getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) &&
                (!noVideo) && (!MainActivity.mTwoPaneMode)) {
            //all gone because video will be displayed in full screen
            textViewStepDescription.setVisibility(View.GONE);
            buttonPrevStep.setVisibility(View.GONE);
            buttonNextStep.setVisibility(View.GONE);
        } else {
            //if not in full screen, the textview will always be visible
            textViewStepDescription.setText(recipeStep.getDescription());

            if(MainActivity.mTwoPaneMode){
                //don't display navigation buttons in Two Pane Mode
                buttonPrevStep.setVisibility(View.INVISIBLE);
                buttonNextStep.setVisibility(View.INVISIBLE);
            } else {
                if (isFirstStep()) {
                    if (!isLastStep()) {
                        //only Next Step button will be displayed
                        buttonPrevStep.setVisibility(View.INVISIBLE);
                        buttonNextStep.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mCallBack.onPrevNextStepSelected(recipeId, (stepId + 1));
                            }
                        });
                    } else { //isFirstStep and isLastStep are both true -> There is only one step
                        buttonPrevStep.setVisibility(View.INVISIBLE);
                        buttonNextStep.setVisibility(View.INVISIBLE);
                    }

                } else if (isLastStep()) {
                    //only Prev Step button will be displayed
                    buttonPrevStep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCallBack.onPrevNextStepSelected(recipeId, (stepId - 1));
                        }
                    });
                    buttonNextStep.setVisibility(View.INVISIBLE);
                } else {
                    //both buttons will be displayed
                    buttonPrevStep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCallBack.onPrevNextStepSelected(recipeId, (stepId - 1));
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
        }
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
        if (Util.SDK_INT <= 23) {
            releasePlayer();
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
        if(mExoPlayer==null) {
            if((recipeStep.getVideoURL().startsWith("https://"))||(recipeStep.getVideoURL().startsWith("http://"))) {
                initializePlayer(Uri.parse(recipeStep.getVideoURL()));
            }
            else if ((recipeStep.getThumbnailURL().startsWith("https://"))||(recipeStep.getThumbnailURL().startsWith("http://"))) {
                initializePlayer(Uri.parse(recipeStep.getThumbnailURL()));
            }
        }
    }

    private void releasePlayer() {
        if(mExoPlayer!=null) {
            seekTimePlayer = mExoPlayer.getCurrentPosition();
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

            if ((getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)&&
                    (!MainActivity.mTwoPaneMode)){
                //set player to full screen
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            //if seekTimePlayer is not TimeUnset, means that activity left the foreground and returned
            if(seekTimePlayer!=C.TIME_UNSET)
                mExoPlayer.seekTo(seekTimePlayer);

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
        outState.putInt(RECIPE_ID, recipeId);
        outState.putInt(STEP_ID, stepId);
        outState.putLong(SEEK_TIME_PLAYER, seekTimePlayer);
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
