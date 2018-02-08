package com.example.android.bakingproject.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingproject.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {


    //private static final String TMDB_BASE_URL = "http://go.udacity.com/";
    private static final String TMDB_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    //private final static String BAKING_APP_JSON = "android-baking-app-json";
    private final static String BAKING_APP_JSON = "topher/2017/May/59121517_baking/baking.json";

    private final static int CONNECTION_TIMEOUT = 10000;

    private NetworkUtils() {}

    public static URL buildUrlRecipesList() {

        URL url;

        Uri.Builder UriBuilder = Uri.parse(TMDB_BASE_URL).buildUpon();
        Uri builtUri = UriBuilder.appendPath(BAKING_APP_JSON)
            .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    //this method receives an URL and returns a String with the downloaded content
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String jsonReturn = "";
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT); //connection is closed after 7 seconds

        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if(!scanner.hasNext()){
                return null;
            }

            while(scanner.hasNext()){
                jsonReturn = jsonReturn.concat(scanner.next());
            }
            return jsonReturn;

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }

    //ONLY call this method on the MAIN THREAD
    public static boolean isNetworkConnected(Context activityContext) {
        ConnectivityManager cm = (ConnectivityManager) activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean returnBool = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!returnBool) {
            Toast.makeText(activityContext, activityContext.getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            Log.i("NetworkUtils", "isNetworkConnected() - Network connection check Failed. No Internet Connection");
        }
        return returnBool;
    }


}
