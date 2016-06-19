package com.android.bittiger.janescookies.tinyflickr;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;

/**
 * Created by xicheng on 16/6/13.
 */
public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = GalleryActivity.class.getSimpleName();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_gallery);

    }


    @Override
    protected void onNewIntent(Intent intent) {

        Log.d(TAG, "----------onNewIntent----------");
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        Log.d(TAG, "----------handleIntent----------");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_gallery);




    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu,  inflater);

        //MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        //MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchManager searchManager =
                (SearchManager) GalleryActivity.this.getSystemService(Context.SEARCH_SERVICE);

        //SearchView mSearchView = (SearchView) findViewById(R.id.search_bar);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();

        if (mSearchView != null) {
            // error info
            mSearchView.setSearchableInfo(searchManager.
                    getSearchableInfo(getComponentName()));
        }

        //mSearchView.setIconifiedByDefault(false);
        //mSearchView.onActionViewExpanded();


//        if (searchItem != null) {
//            mSearchView = (SearchView) searchItem.getActionView();
//        }
//
//
//        if (mSearchView != null) {
//            // error info
//            mSearchView.setSearchableInfo(searchManager.
//                    getSearchableInfo(GalleryActivity.this.getComponentName()));
//        }



        // TODO: search suggestion

        return true;
        //return super.onCreateOptionsMenu(menu);
    }*/


}
