package com.jonathanritchey.movies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieModel {
    public static List<MovieItem> ITEMS = new ArrayList<>();
    public static Map<String, MovieItem> ITEM_MAP = new HashMap<>();

    public static void addItem(MovieItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static class MovieItem {
        public String id, overview, posterPath, title, releaseDate, voteAverage, length;
        public MovieItem(String aId) {
            id = aId;
        }
        @Override
        public String toString() {
            return "overview:"+overview+" posterPath:"+posterPath+" title:"+title+
                    " releaseDate:"+releaseDate+" "+" voteAverage:"+voteAverage+" length:"+length;
        }
    }
}
