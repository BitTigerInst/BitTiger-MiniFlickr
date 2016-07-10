package com.android.bittiger.janescookies.tinyflickr;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


/**
 * Created by xicheng on 16/6/13.
 */
public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = GalleryActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //this.getSupportActionBar().hide();
        //this.getSupportActionBar().show();
        setContentView(R.layout.activity_gallery);
        // test to fix searchview
        //handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {

        Log.d(TAG, "----------onNewIntent----------");
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        Log.d(TAG, "----------handleIntent----------");


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "---handleIntent----testsearchview---Received a new search query: " + query);

//            PreferenceManager.getDefaultSharedPreferences(this)
//                    .edit()
//                    .putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
//                    .commit();

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(UrlManager.PREF_SEARCH_QUERY, query)
                    .commit();

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.gallery_fragment);

            if(fragment!= null) {
                ( (GalleryFragment) fragment).refresh();
            }
        }


    }



}
