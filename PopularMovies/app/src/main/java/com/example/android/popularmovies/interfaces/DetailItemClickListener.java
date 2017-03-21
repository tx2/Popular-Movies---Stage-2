package com.example.android.popularmovies.interfaces;

import com.example.android.popularmovies.model.DetailItemType;

/**
 * Created by jorgemendes on 21/03/2017.
 */

public interface DetailItemClickListener {
    void onDetailItemClick(DetailItemType type, int referenceIndex);
}
