package com.android.bittiger.janescookies.tinyflickr;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;

import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xicheng on 16/6/16.
 */
public class GalleryFragment extends Fragment {

    private SearchView mSearchView ;
    private static final String TAG = "GalleryFragment" ;

    private RecyclerView mRecyclerView;
    private CustomSwipeRefreshLayout mCustomSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;

    private GalleryAdapter mAdapter;
    private ArrayList<GalleryItem> mItems;

    private static final int COLUMN_NUM = 3;
    private static final int ITEM_PER_PAGE = 100;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItem = mLayoutManager.getItemCount();
                Log.d(TAG, "--------onScrolled-----totalItem is " + totalItem);


                int lastItemPos = mLayoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "--------onScrolled-----lastItemPos is " + lastItemPos);
//                if (mHasMore && !mLoading && totalItem -1 != lastItemPos) {
//                    startLoading();
//                }

                if (totalItem - 1 <= lastItemPos) {
                    List<Contact> result = new ArrayList<>();
                    ArrayList<Contact> newlist = (ArrayList) Contact.generateSampleList(3);

                    for (int i = 0; i < newlist.size(); i++) {

                        //Contact item = new Contact();
                        Contact item = newlist.get(i);
                        result.add(item);
                    }
                    mAdapter.addAll(result);
                }

                mAdapter.notifyDataSetChanged();

                mCustomSwipeRefreshLayout.refreshComplete();
            }
        });

        mLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUM);
        mRecyclerView.setLayoutManager(mLayoutManager);


//        mAdapter = new GalleryAdapter(getActivity(), new ArrayList<GalleryItem>());
        mAdapter = new GalleryAdapter(getActivity(), Contact.generateSampleList(100));

        mRecyclerView.setAdapter(mAdapter);



        mCustomSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mCustomSwipeRefreshLayout.setOnRefreshListener(
                new CustomSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        refresh();
                    }
                }
        );

//        startLoading();
        return view;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu);

//        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) searchItem.getActionView();
//
//        if (mSearchView != null) {
//            // error info
//        }

        // TODO: search suggestion

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_move:
                if (mRecyclerView != null) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
                return true;
            case R.id.menu_item_clear:
                if (mSearchView != null) {
                    mSearchView.setQuery("", false);
                    mSearchView.setIconified(false);
                }

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_SEARCH_QUERY, null)
                        .commit();

                refresh();
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
