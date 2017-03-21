package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.interfaces.ListItemClickListener;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by jorgemendes on 05/02/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final ListItemClickListener mOnClickListener;

    private Context mContext;
    private Movie[] mMovies;
    private Cursor mCursor;

    /**
     * Constructor for MovieAdapter that accepts a list of movies to display and the specification
     * for the ListItemClickListener.
     *
     * @param context Context of adapter.
     * @param mOnClickListener Listener for list item clicks.
     */
    public MovieAdapter(@NonNull Context context, ListItemClickListener mOnClickListener) {
        this.mContext = context;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (this.mMovies != null)
            holder.bind(this.mMovies[position]);
        else if (this.mCursor != null) {
            this.mCursor.moveToPosition(position);
            holder.bind(this.mCursor);
        }
    }

    @Override
    public int getItemCount() {
        return this.mMovies != null ? this.mMovies.length : (this.mCursor != null ? mCursor.getCount() : 0);
    }

    /**
     * Swaps the movie array used by the MovieAdapter for its movie data. When this method is called,
     * we assume we have a completely new set of data, so we call notifyDataSetChanged to tell
     * the RecyclerView to update.
     *
     * @param movies the new movies array to use as MoviesAdapter's data source
     */
    public void swapMovie(Movie[] movies) {
        this.mCursor = null;
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    /**
     * Swaps the cursor used by the MovieAdapter for its movie data. When this method is called,
     * we assume we have a completely new set of data, so we call notifyDataSetChanged to tell
     * the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as ForecastAdapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        this.mMovies = null;
        this.mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView moviePosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            this.moviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);

            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            this.moviePosterImageView.setImageDrawable(null);
            Picasso.with(mContext)
                    .load(MovieUtils.getPosterBaseUrl(mContext) + movie.getPosterUrl())
                    .into(this.moviePosterImageView);
        }

        public void bind(Cursor cursor) {
            String posterUrl = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL));

            this.moviePosterImageView.setImageDrawable(null);
            Picasso.with(mContext)
                    .load(MovieUtils.getPosterBaseUrl(mContext) + posterUrl)
                    .into(this.moviePosterImageView);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
