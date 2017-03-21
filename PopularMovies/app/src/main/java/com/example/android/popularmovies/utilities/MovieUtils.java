package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.android.popularmovies.MainActivity;

/**
 * Created by jorgemendes on 04/02/17.
 */

public final class MovieUtils {

    public static final String YOUTUBE_BASE_SHORT_URL = "https://youtu.be/";
    public static final String YOUTUBE_APP_URL = "vnd.youtube://";

    private static final String TMDB_BASE_IMAGES_URL = "https://image.tmdb.org/t/p/";

    /**
     * This method returns the base url of poster image with the appropriate size based on the
     * number of movies displayed by row.
     *
     * @param context Context of application.
     * @return Base url for poster images.
     */
    public static String getPosterBaseUrl(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        double halfWidth = metrics.widthPixels / (double) MainActivity.LAYOUT_SPAN_COUNT;

        String imageW = "original";
        if (halfWidth <= 92)
            imageW = "w92";
        else if (halfWidth <= 154)
            imageW = "w154";
        else if (halfWidth <= 185)
            imageW = "w185";
        else if (halfWidth <= 342)
            imageW = "w342";
        else if (halfWidth <= 500)
            imageW = "w500";
        else if (halfWidth <= 780)
            imageW = "w780";

        return TMDB_BASE_IMAGES_URL + imageW;
    }

}
