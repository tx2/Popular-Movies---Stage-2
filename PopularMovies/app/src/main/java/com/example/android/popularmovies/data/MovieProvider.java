package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by jorgemendes on 20/03/17.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                cursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            case CODE_MOVIE_WITH_ID:
                cursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                                new String[] { uri.getLastPathSegment() },
                                null,
                                null,
                                sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);

                if (id > 0)
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int moviesDeleted = 0;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                moviesDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
                break;
            case CODE_MOVIE_WITH_ID:
                moviesDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[] {uri.getLastPathSegment()});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return moviesDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented...");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Not implemented...");
    }
}
