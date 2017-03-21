package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jorgemendes on 20/03/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_URL + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVG + " REAL, " +
                "UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
