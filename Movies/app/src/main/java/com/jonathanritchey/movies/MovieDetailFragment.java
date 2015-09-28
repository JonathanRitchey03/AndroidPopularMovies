package com.jonathanritchey.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonathanritchey.movies.model.MovieModel;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private MovieModel.MovieItem mItem;
    public MovieDetailFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = com.jonathanritchey.movies.model.MovieModel.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail_overview)).setText(mItem.overview);
            ImageView imageView = ((ImageView) rootView.findViewById(R.id.movie_detail_imageview));
            Picasso.with(getActivity().getApplicationContext()).load(mItem.posterPath).into(imageView);
            ((TextView) rootView.findViewById(R.id.movie_detail_title)).setText(mItem.title);
            String voteAveragePrefix = getActivity().getString(R.string.vote_average_prefix);
            String voteAverageSuffix = getActivity().getString(R.string.vote_average_suffix);
            ((TextView) rootView.findViewById(R.id.movie_detail_vote_average)).setText(voteAveragePrefix+mItem.voteAverage+voteAverageSuffix);
            String releaseDatePrefx = getActivity().getString(R.string.release_date_prefix);
            ((TextView) rootView.findViewById(R.id.movie_detail_release_date)).setText(releaseDatePrefx+mItem.releaseDate);
        }
        return rootView;
    }
}
