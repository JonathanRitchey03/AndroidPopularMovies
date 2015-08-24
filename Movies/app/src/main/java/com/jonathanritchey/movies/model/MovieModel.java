package com.jonathanritchey.movies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MovieModel {

    /**
     * An array of sample (dummy) items.
     */
    public static List<MovieItem> ITEMS = new ArrayList<MovieItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, MovieItem> ITEM_MAP = new HashMap<String, MovieItem>();

    static {
        // Add 3 sample items.
        addItem(new MovieItem("1", "Item 1"));
        addItem(new MovieItem("2", "Item 2"));
        addItem(new MovieItem("3", "Item 3"));
    }

    private static void addItem(MovieItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
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
