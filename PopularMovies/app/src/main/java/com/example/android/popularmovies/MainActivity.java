package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MoviesPreferences;
import com.example.android.popularmovies.interfaces.ListItemClickListener;
import com.example.android.popularmovies.interfaces.RequestDelegate;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MoviesListType;
import com.example.android.popularmovies.network.MovieService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ListItemClickListener,
        RequestDelegate {

    public static final int LAYOUT_SPAN_COUNT = 2;

    public static final int ID_FAVOURITE_MOVIES_LOADER = 200;

    private static final String SAVE_MOVIE_SELECTION = "movie_selection";
    private static final String SAVE_MOVIES = "movies";

    private TextView mErrorLoadingTextView;
    private ProgressBar mProgressBar;

    private RecyclerView mMoviesList;
    private MovieAdapter mMoviesAdapter;

    private MoviesListType mSelectedListType;
    private Movie[] mMovies;
    private Cursor mFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorLoadingTextView = (TextView) findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, LAYOUT_SPAN_COUNT);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);
        mMoviesAdapter = new MovieAdapter(this, this);
        mMoviesList.setAdapter(mMoviesAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVE_MOVIES)) {
            mSelectedListType = MoviesPreferences.getSelectedMovieListType(this);
            loadMovies();
        } else {
            mSelectedListType = (MoviesListType) savedInstanceState.getSerializable(SAVE_MOVIE_SELECTION);
            configureMoviesList((Movie[]) savedInstanceState.getParcelableArray(SAVE_MOVIES));
        }

        getSupportLoaderManager().initLoader(ID_FAVOURITE_MOVIES_LOADER, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_MOVIE_SELECTION, mSelectedListType);
        outState.putParcelableArray(SAVE_MOVIES, mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        switch (mSelectedListType) {
            case MostPopular:
                MenuItem mostPopularItem = menu.findItem(R.id.most_popular_action);
                mostPopularItem.setChecked(true);
                break;
            case TopRated:
                MenuItem topRatedItem = menu.findItem(R.id.top_rated_action);
                topRatedItem.setChecked(true);
                break;
            case Favourites:
                MenuItem favouriteItem = menu.findItem(R.id.favourite_action);
                favouriteItem.setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_action:
                loadMovies();
                return true;
            case R.id.most_popular_action:
                if (!item.isChecked()) {
                    mSelectedListType = MoviesListType.MostPopular;
                    MoviesPreferences.setSelectedMovieListType(this, mSelectedListType);
                    loadMovies();
                }
                item.setChecked(true);
                return true;
            case R.id.top_rated_action:
                if (!item.isChecked()) {
                    mSelectedListType = MoviesListType.TopRated;
                    MoviesPreferences.setSelectedMovieListType(this, mSelectedListType);
                    loadMovies();
                }
                item.setChecked(true);
                return true;
            case R.id.favourite_action:
                if (!item.isChecked()) {
                    mSelectedListType = MoviesListType.Favourites;
                    MoviesPreferences.setSelectedMovieListType(this, mSelectedListType);
                    loadMovies();
                }
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        switch (mSelectedListType) {
            case MostPopular:
                if (netInfo != null && netInfo.isConnectedOrConnecting())
                    MovieService.getPopularMovies(this);
                else
                    showErrorMessage();
                break;
            case TopRated:
                if (netInfo != null && netInfo.isConnectedOrConnecting())
                    MovieService.getTopRatedMovies(this);
                else
                    showErrorMessage();
                break;
            case Favourites:
                getSupportLoaderManager().restartLoader(ID_FAVOURITE_MOVIES_LOADER, null, this);
                break;
        }
    }

    private void configureMoviesList(Movie[] movies) {
        if (movies != null && movies.length > 0) {
            mMovies = movies;
            showMoviesList();
            mMoviesAdapter.swapMovie(mMovies);
            mMoviesList.setAdapter(mMoviesAdapter);
        } else {
            showErrorMessage();
        }
    }

    private void showMoviesList() {
        mErrorLoadingTextView.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mErrorLoadingTextView.setText(getString(R.string.loading_error_message));
        mErrorLoadingTextView.setVisibility(View.VISIBLE);
    }

    private void showNoFavouritesMessage() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mErrorLoadingTextView.setText(getString(R.string.loading_no_favourites_message));
        mErrorLoadingTextView.setVisibility(View.VISIBLE);
    }

    //region Click Listener Callback
    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mSelectedListType == MoviesListType.Favourites) {
            if (mFavourites.getCount() > clickedItemIndex) {
                mFavourites.moveToPosition(clickedItemIndex);
                Movie movie = new Movie();
                movie.setId(mFavourites.getInt(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setPosterUrl(mFavourites.getString(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL)));
                movie.setOriginalTitle(mFavourites.getString(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setReleaseDate(mFavourites.getString(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setVoteAverage(mFavourites.getDouble(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVG)));
                movie.setOverview(mFavourites.getString(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));

                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
                intent.putExtra(DetailActivity.EXTRA_FAVOURITE, true);
                startActivity(intent);
            }
        } else if (mMovies != null && mMovies.length > clickedItemIndex) {
            boolean isFavourite = false;

            mFavourites.moveToFirst();
            for (int i = 0; i < mFavourites.getCount(); i++) {
                mFavourites.moveToPosition(i);
                if (mFavourites.getInt(mFavourites.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)) == mMovies[clickedItemIndex].getId()) {
                    isFavourite = true;
                    break;
                }
            }

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, mMovies[clickedItemIndex]);
            intent.putExtra(DetailActivity.EXTRA_FAVOURITE, isFavourite);
            startActivity(intent);
        }
    }
    //endregion

    //region Request Delegate
    @Override
    public void beforeRequest(MoviesListType type) {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorLoadingTextView.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestSuccess(MoviesListType type, Object data) {
        mProgressBar.setVisibility(View.INVISIBLE);

        List<Movie> list = (List<Movie>) data;
        Movie[] movies = list.toArray(new Movie[list.size()]);
        configureMoviesList(movies);
    }

    @Override
    public void onRequestError(MoviesListType type) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovies = null;
        showErrorMessage();
    }
    //endregion

    //region Loader Callback
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mSelectedListType == MoviesListType.Favourites) {
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorLoadingTextView.setVisibility(View.INVISIBLE);
            mMoviesList.setVisibility(View.INVISIBLE);
        }

        switch (id) {
            case ID_FAVOURITE_MOVIES_LOADER:
                return new CursorLoader(this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mSelectedListType == MoviesListType.Favourites) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (data != null && data.getCount() > 0) {
                mMoviesAdapter.swapCursor(data);
                mMoviesList.setAdapter(mMoviesAdapter);

                mMoviesList.setVisibility(View.VISIBLE);
            } else
                showNoFavouritesMessage();
        }
        mFavourites = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
        mMoviesList.setAdapter(mMoviesAdapter);
    }
    //endregion
}
