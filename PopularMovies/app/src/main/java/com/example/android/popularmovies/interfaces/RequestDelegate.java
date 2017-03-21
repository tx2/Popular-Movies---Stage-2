package com.example.android.popularmovies.interfaces;

import com.example.android.popularmovies.model.MoviesListType;

/**
 * Created by jorgemendes on 19/03/17.
 */

public interface RequestDelegate {
    void beforeRequest(MoviesListType type);
    void onRequestSuccess(MoviesListType type, Object data);
    void onRequestError(MoviesListType type);
}
