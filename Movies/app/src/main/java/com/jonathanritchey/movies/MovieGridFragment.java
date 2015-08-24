package com.jonathanritchey.movies;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieGridFragment extends Fragment {

    public GridView mGridView;
    public ArrayAdapter<String> mArrayAdapter;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    public interface Callbacks {
        public void onItemSelected(String id);
    }
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };
    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // TODO: replace with a real list adapter.
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
//                getActivity(),
//                android.R.layout.simple_list_item_activated_1,
//                android.R.id.text1,
//                DummyContent.ITEMS));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.movie_grid, container, false);
        mGridView = (GridView)v.findViewById(R.id.movie_grid);
        String [] strings = new String[] {
                "Item 1",
                "Item 2",
                "Item 3",
        };
        ArrayList<String> fakeData = new ArrayList<>(Arrays.asList(strings));
        mArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.movie_cell_view,
                R.id.movie_cell_view_description,
                fakeData);
        mGridView.setAdapter(mArrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String forecast = mForecastAdapter.getItem(position);
//                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
//                intent.putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
//            }
//        });
        return v;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        View v = inflater.inflate(R.layout.movie_grid, container, false);
//        mGridView = (GridView)v.findViewById(R.id.gridView);
//        // Restore the previously serialized activated item position.
//        if (savedInstanceState != null
//                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
//            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

//    @Override
//    public void onListItemClick(ListView listView, View view, int position, long id) {
//        super.onListItemClick(listView, view, position, id);
//
//        // Notify the active callbacks interface (the activity, if the
//        // fragment is attached to one) that an item has been selected.
//        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
//        // When setting CHOICE_MODE_SINGLE, ListView will automatically
//        // give items the 'activated' state when touched.
//        getListView().setChoiceMode(activateOnItemClick
//                ? ListView.CHOICE_MODE_SINGLE
//                : ListView.CHOICE_MODE_NONE);
    }

//    private void setActivatedPosition(int position) {
//        if (position == ListView.INVALID_POSITION) {
//            getListView().setItemChecked(mActivatedPosition, false);
//        } else {
//            getListView().setItemChecked(position, true);
//        }
//
//        mActivatedPosition = position;
//    }
}
