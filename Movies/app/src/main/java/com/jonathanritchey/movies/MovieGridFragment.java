package com.jonathanritchey.movies;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.jonathanritchey.movies.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieGridFragment extends Fragment {

    // --------------------------------------------------------------
    // INSERT YOUR API KEY FROM https://www.themoviedb.org/documentation/api
    // HERE
    private static final String API_KEY = null;
    // --------------------------------------------------------------

    private static String mSortCriteria = "popular";
    public GridView mGridView;
    public MovieGridAdapter mMovieGridAdapter;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sMovieCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private static boolean VERBOSE_LOGGING = true;
    private static String LOG_TAG = MovieModel.class.getSimpleName();


    private static Callbacks sMovieCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public MovieGridFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String previousCriteria = new String(mSortCriteria);
        switch(item.getItemId()) {
            case R.id.action_sort_by_popularity:
                mSortCriteria = "popular";
                break;
            case R.id.action_sort_by_rating:
                mSortCriteria = "top_rated";
                break;
        }
        if ( !mSortCriteria.equals(previousCriteria) ) {
            fetchData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.movie_grid_view, container, false);
        mGridView = (GridView)v.findViewById(R.id.movie_grid);
        mMovieGridAdapter = new MovieGridAdapter(getActivity(),R.id.movie_cell_text_view,MovieModel.ITEMS);
        mGridView.setAdapter(mMovieGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallbacks.onItemSelected(MovieModel.ITEMS.get(position).id);
            }
        });
        mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sMovieCallbacks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void refreshGridView() {
        int gridViewEntrySize = getResources().getDimensionPixelSize(R.dimen.grid_view_entry_size_width);
        int gridViewSpacing = getResources().getDimensionPixelSize(R.dimen.grid_view_spacing);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        int numColumns = (width - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);
        float widthDp = width / displaymetrics.density;
        float heightDp = height / displaymetrics.density;
        float shortestLengthDp = widthDp < heightDp ? widthDp : heightDp;
        boolean isTablet = shortestLengthDp >= 600;
        if ( !isTablet ) {
            mGridView.setNumColumns(numColumns);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshGridView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshGridView();
    }

    private void setActivatedPosition(int position) {
        boolean invalidPosition = position == GridView.INVALID_POSITION;
        mGridView.setItemChecked(invalidPosition ? mActivatedPosition : position,
                                 invalidPosition ? false : true);
        mActivatedPosition = position;
    }

    public interface Callbacks {
        void onItemSelected(String id);
    }

    public class FetchDataTask extends AsyncTask<String, Void, ArrayList<MovieModel.MovieItem>> {
        @Override
        protected void onPostExecute(ArrayList<MovieModel.MovieItem> movieItems) {
            super.onPostExecute(movieItems);
            if ( movieItems != null ) {
                MovieModel.clear();
                for ( MovieModel.MovieItem entry : movieItems ) {
                    MovieModel.addItem(entry);
                }
                mMovieGridAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected ArrayList<MovieModel.MovieItem> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString = null;
            try {
                Uri.Builder builder = new Uri.Builder();
                // http://api.themoviedb.org/3/movie/popular?api_key=

                builder.scheme("http").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(params[0]);
                builder.appendQueryParameter("api_key", API_KEY);
                URL url = new URL(builder.build().toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                try {
                    // Will throw IOException if server responds with 401.
                    urlConnection.getResponseCode();
                } catch (IOException e) {
                    // Will return 401, because now connection has the correct internal state.
                    int responsecode = urlConnection.getResponseCode();
                }

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) buffer.append(line + "\n");
                if (buffer.length() == 0) return null;
                movieJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if ( urlConnection != null ) urlConnection.disconnect();
                if ( reader != null ) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
                if ( VERBOSE_LOGGING ) Log.v(LOG_TAG, "Forecast JSON String "+movieJsonString);
                ArrayList<MovieModel.MovieItem> movieItems;
                try {
                    movieItems = getMovieDataFromJSON(movieJsonString);
                    return movieItems;
                } catch (JSONException e) {
                    Log.e(LOG_TAG,"Exception "+e);
                }
            }
            return null;
        }
    }

    private ArrayList<MovieModel.MovieItem> getMovieDataFromJSON(String jsonStr)
            throws JSONException {
        /*
        "adult":false,
         "backdrop_path":"/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg",
         "genre_ids":[
            53,
            28,
            12
         ],
         "id":76341,
         "original_language":"en",
         "original_title":"Mad Max: Fury Road",
         "overview":"An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.",
         "release_date":"2015-05-15",
         "poster_path":"/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
         "popularity":51.220824,
         "title":"Mad Max: Fury Road",
         "video":false,
         "vote_average":7.8,
         "vote_count":1836
         */
        final String RESULTS_ARRAY_KEY = "results";
        final String ADULT_BOOL_KEY = "adult";
        final String POSTER_PATH_STRING_KEY = "poster_path";
        final String OVERVIEW_STRING_KEY = "overview";
        final String RELEASE_DATE_STRING_KEY = "release_date";
        final String TITLE_STRING_KEY = "title";
        final String VOTE_AVERAGE_STRING_KEY = "vote_average";
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray movieArray = jsonObject.getJSONArray(RESULTS_ARRAY_KEY);
        String[] resultStrs = new String[movieArray.length()];
        ArrayList<MovieModel.MovieItem> movieItems = new ArrayList<>();
        for(int i = 0; i < resultStrs.length; i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);
            boolean adult = movieObject.getBoolean(ADULT_BOOL_KEY);
            if ( !adult ) {
                String id = ""+movieItems.size();
                MovieModel.MovieItem movieItem = new MovieModel.MovieItem(id);
                movieItem.overview = movieObject.getString(OVERVIEW_STRING_KEY);
                String posterPath = movieObject.getString(POSTER_PATH_STRING_KEY);
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("image.tmdb.org").appendPath("t").appendPath("p").appendPath("w185").appendPath(posterPath);
                posterPath = builder.build().toString();
                try {
                    posterPath = java.net.URLDecoder.decode(posterPath, "UTF-8");
                } catch ( UnsupportedEncodingException e ) {
                    Log.e(LOG_TAG, "Exception "+e.toString());
                }
                movieItem.posterPath = posterPath;
                movieItem.releaseDate = movieObject.getString(RELEASE_DATE_STRING_KEY);
                movieItem.title = movieObject.getString(TITLE_STRING_KEY);
                movieItem.voteAverage = movieObject.getString(VOTE_AVERAGE_STRING_KEY);
                movieItems.add(movieItem);
            }
        }
        for (String s : resultStrs) {
            if ( VERBOSE_LOGGING ) Log.v(LOG_TAG, "Movie entry: " + s);
        }
        return movieItems;
    }

    public void fetchData() {
        if ( API_KEY == null ) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.replace_api_key, Toast.LENGTH_LONG).show();
        } else {
            FetchDataTask fetchDataTask = new FetchDataTask();
            fetchDataTask.execute(mSortCriteria);
        }
    }
}
