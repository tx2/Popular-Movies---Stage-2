package com.example.android.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.model.MoviesListType;

/**
 * Created by jorgemendes on 21/03/17.
 */

public final class MoviesPreferences {

    private static final String PREF_MOVIE_LIST_TYPE = "list_type_selected";

    public static void setSelectedMovieListType(Context context, MoviesListType type) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        int listType;
        switch (type) {
            case MostPopular:
                listType = 0;
                break;
            case TopRated:
                listType = 1;
                break;
            case Favourites:
                listType = 2;
                break;

            default:
                listType = -1;
        }

        editor.putInt(PREF_MOVIE_LIST_TYPE, listType);
        editor.apply();
    }

    public static MoviesListType getSelectedMovieListType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int listType = sp.getInt(PREF_MOVIE_LIST_TYPE, -1);

        MoviesListType type;
        switch (listType) {
            case 0:
                type = MoviesListType.MostPopular;
                break;
            case 1:
                type = MoviesListType.TopRated;
                break;
            case 2:
                type = MoviesListType.Favourites;
                break;

            default:
                type = MoviesListType.MostPopular;
        }
        return type;
    }

}
