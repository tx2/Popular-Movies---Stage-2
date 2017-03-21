package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jorgemendes on 20/03/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_POSTER_URL = "poster";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVG = "vote";
    }
}
