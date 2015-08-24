package com.jonathanritchey.movies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieModel {

    private static boolean VERBOSE_LOGGING = true;
    private static String LOG_TAG = MovieModel.class.getSimpleName();

    public static List<MovieItem> ITEMS = new ArrayList<MovieItem>();
    public static Map<String, MovieItem> ITEM_MAP = new HashMap<String, MovieItem>();

    public static void addItem(MovieItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clear() {
//        ITEMS = new ArrayList<MovieItem>();
//        ITEM_MAP = new HashMap<String,MovieItem>();
    }

    public static class MovieItem {
        public String id;
        public String content;

        public MovieItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
