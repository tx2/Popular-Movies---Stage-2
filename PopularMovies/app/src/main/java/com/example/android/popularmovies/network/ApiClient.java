package com.example.android.popularmovies.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorgemendes on 20/03/17.
 */

public class ApiClient {

    public static final String API_KEY_PARAM = "api_key";
    public static final String ID_PARAM = "id";
    public static final String APPEND_TO_RESPONSE_PARAM = "append_to_response";

    public static final String TMDB_TOP_RATED_URL = "movie/top_rated";
    public static final String TMDB_POPULAR_URL = "movie/popular";
    public static final String TMDB_MOVIE_DETAIL_URL = "movie/{" + ID_PARAM + "}";

    public static final String TMDB_API_KEY = "YOUR_API_KEY";
    public static final String TMDB_APPEND_FIELDS = "videos,reviews";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
