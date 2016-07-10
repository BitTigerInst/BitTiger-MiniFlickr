package com.android.bittiger.janescookies.tinyflickr;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by xicheng on 16/7/7.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    // part of content uri which is defined in AndroidManifest.xml
    public static final String AUTHORITY = "com.android.bittiger.janescookies.tinyflickr" +
            ".SuggestionProvider";

    // suggestion mode which gives recent queries
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
