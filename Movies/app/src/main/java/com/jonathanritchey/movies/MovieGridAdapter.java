package com.jonathanritchey.movies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jonathanritchey.movies.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MovieGridAdapter extends ArrayAdapter<MovieModel.MovieItem> {
    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();
    private List<MovieModel.MovieItem> objects;

    public MovieGridAdapter(Context context, int textViewResourceId, List<MovieModel.MovieItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.movie_grid_cell_view, null);
        }
        MovieModel.MovieItem i = objects.get(position);
        if (i != null) {
//            TextView description = (TextView) v.findViewById(R.id.movie_cell_text_view);
//            if (description != null && i.overview != null) {
//                description.setText(i.overview);
//            }
            ImageView imageView = (ImageView) v.findViewById(R.id.movie_cell_image_view);
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("image.tmdb.org").appendPath("t").appendPath("p").appendPath("w185").appendPath(i.posterPath);
            String imagePath = builder.build().toString();
            try {
                imagePath = java.net.URLDecoder.decode(imagePath, "UTF-8");
            } catch ( UnsupportedEncodingException e ) {
                Log.e(LOG_TAG, "Exception "+e.toString());
            }
            Picasso.with(getContext()).load(imagePath).into(imageView);
        }
        return v;
    }
}
