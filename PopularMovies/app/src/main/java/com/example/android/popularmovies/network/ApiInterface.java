package com.example.android.popularmovies.network;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieDetailsResponse;
import com.example.android.popularmovies.model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jorgemendes on 20/03/17.
 */

public interface ApiInterface {

    @GET(ApiClient.TMDB_TOP_RATED_URL)
    Call<Response<Movie>> getTopRatedMovies(@Query(ApiClient.API_KEY_PARAM) String apiKey);

    @GET(ApiClient.TMDB_POPULAR_URL)
    Call<Response<Movie>> getPopularMovies(@Query(ApiClient.API_KEY_PARAM) String apiKey);

    @GET(ApiClient.TMDB_MOVIE_DETAIL_URL)
    Call<MovieDetailsResponse> getMovieDetails(@Path(ApiClient.ID_PARAM) int id,
                                               @Query(ApiClient.API_KEY_PARAM) String apiKey,
                                               @Query(ApiClient.APPEND_TO_RESPONSE_PARAM) String appendFields);

}
