package com.example.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jorgemendes on 20/03/17.
 */

public class MovieDetailsResponse {

    @SerializedName("videos")
    private Response<Video> videos;

    @SerializedName("reviews")
    private Response<Review> reviews;

    public Response<Video> getVideos() {
        return videos;
    }

    public void setVideos(Response<Video> videos) {
        this.videos = videos;
    }

    public Response<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Response<Review> reviews) {
        this.reviews = reviews;
    }

}
