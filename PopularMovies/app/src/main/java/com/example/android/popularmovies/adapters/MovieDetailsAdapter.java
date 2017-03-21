package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ListItemMovieDetailBinding;
import com.example.android.popularmovies.databinding.ListItemMovieReviewBinding;
import com.example.android.popularmovies.databinding.ListItemMovieVideoBinding;
import com.example.android.popularmovies.databinding.ListItemTitleSeparatorBinding;
import com.example.android.popularmovies.interfaces.DetailItemClickListener;
import com.example.android.popularmovies.interfaces.ListItemClickListener;
import com.example.android.popularmovies.model.DetailItemType;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.utilities.DateUtils;
import com.example.android.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by jorgemendes on 21/03/2017.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.MovieDetailsViewHolder> {

    private static final int VIEW_TYPE_MOVIE_DETAIL = 0;
    private static final int VIEW_TYPE_SEPARATOR_TITLE = 1;
    private static final int VIEW_TYPE_VIDEO = 2;
    private static final int VIEW_TYPE_REVIEW = 3;

    private final DetailItemClickListener mOnClickListener;

    private Context mContext;
    private Movie mMovie;
    private boolean mIsFavourite;
    private Video[] mVideos;
    private Review[] mReviews;

    public MovieDetailsAdapter(Context mContext, DetailItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        this.mContext = mContext;
    }

    @Override
    public MovieDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_MOVIE_DETAIL:
                layoutId = R.layout.list_item_movie_detail;
                break;
            case VIEW_TYPE_SEPARATOR_TITLE:
                layoutId = R.layout.list_item_title_separator;
                break;
            case VIEW_TYPE_VIDEO:
                layoutId = R.layout.list_item_movie_video;
                break;
            case VIEW_TYPE_REVIEW:
                layoutId = R.layout.list_item_movie_review;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        return new MovieDetailsAdapter.MovieDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieDetailsViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_MOVIE_DETAIL:
                holder.bind(viewType, mMovie);
                break;
            case VIEW_TYPE_SEPARATOR_TITLE:
                if (position == 1)
                    holder.bind(viewType, mContext.getString(R.string.trailers_title));
                else
                    holder.bind(viewType, mContext.getString(R.string.reviews_title));
                break;
            case VIEW_TYPE_VIDEO:
                holder.bind(viewType, mVideos[position - 2]);
                break;
            case VIEW_TYPE_REVIEW:
                holder.bind(viewType, mReviews[position - 3 - mVideos.length]);
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if (this.mMovie != null) {
            count = 3;
            if (this.mVideos != null) count += this.mVideos.length;
            if (this.mReviews != null) count += this.mReviews.length;
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (position == 0) {
            viewType = VIEW_TYPE_MOVIE_DETAIL;
        } else if (position == 1 || (mVideos != null && mVideos.length == position - 2)) {
            viewType = VIEW_TYPE_SEPARATOR_TITLE;
        } else if (mVideos != null && mVideos.length > position - 2) {
            viewType = VIEW_TYPE_VIDEO;
        } else if (mReviews != null && mReviews.length > (position - 3 - (mVideos != null ? mVideos.length : 0))) {
            viewType = VIEW_TYPE_REVIEW;
        }

        return viewType;
    }

    public void swapData(Movie movie, boolean isFavourite, Video[] videos, Review[] reviews) {
        mMovie = movie;
        mIsFavourite = isFavourite;
        mVideos = videos;
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class MovieDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListItemMovieDetailBinding mDetailsBinding;
        private ListItemTitleSeparatorBinding mTitleBinding;
        private ListItemMovieVideoBinding mVideoBinding;
        private ListItemMovieReviewBinding mReviewBonding;

        public MovieDetailsViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());

            if (binding instanceof ListItemMovieDetailBinding)
                mDetailsBinding = (ListItemMovieDetailBinding) binding;
            else if (binding instanceof  ListItemTitleSeparatorBinding)
                mTitleBinding = (ListItemTitleSeparatorBinding) binding;
            else if (binding instanceof ListItemMovieVideoBinding)
                mVideoBinding = (ListItemMovieVideoBinding) binding;
            else if (binding instanceof ListItemMovieReviewBinding)
                mReviewBonding = (ListItemMovieReviewBinding) binding;

            binding.getRoot().setOnClickListener(this);
        }

        public void bind(int viewType, Object data) {
            switch (viewType) {
                case VIEW_TYPE_MOVIE_DETAIL:
                    Movie movie = (Movie) data;
                    bindDetail(movie);
                    break;
                case VIEW_TYPE_SEPARATOR_TITLE:
                    String title = (String) data;
                    mTitleBinding.tvTitle.setText(title);
                    break;
                case VIEW_TYPE_VIDEO:
                    Video video = (Video) data;
                    mVideoBinding.tvTrailerTitle.setText(video.getName());
                    break;
                case VIEW_TYPE_REVIEW:
                    Review review = (Review) data;
                    mReviewBonding.tvUsername.setText(review.getAuthor());
                    mReviewBonding.tvReview.setText(review.getContent());
                    break;

                default:
                    throw new IllegalArgumentException("Invalid view type, value of " + viewType);
            }
        }

        private void bindDetail(Movie movie) {
            Picasso.with(mContext)
                    .load(MovieUtils.getPosterBaseUrl(mContext) + movie.getPosterUrl())
                    .into(mDetailsBinding.ivMoviePoster);
            mDetailsBinding.tvMovieTitle.setText(movie.getOriginalTitle());
            mDetailsBinding.tvMovieYear.setText(DateUtils.getYearFromDate(movie.getReleaseDate()));
            mDetailsBinding.tvMovieRate.setText(movie.getVoteAverage() + "/10");
            mDetailsBinding.tvMovieSynopsis.setText(movie.getOverview());
            mDetailsBinding.bFavourite.setText(mContext.getText(mIsFavourite ? R.string.favourite_button_off : R.string.favourite_button_on));
            mDetailsBinding.bFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onDetailItemClick(DetailItemType.Favourite, 0);
                }
            });
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            int viewType = MovieDetailsAdapter.this.getItemViewType(clickedPosition);

            if (viewType == VIEW_TYPE_VIDEO) {
                mOnClickListener.onDetailItemClick(DetailItemType.Video, clickedPosition - 2);
            }
        }
    }
}
