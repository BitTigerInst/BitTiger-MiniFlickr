package com.android.bittiger.janescookies.tinyflickr;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    private RequestQueue mRq;


    private GalleryAdapter mAdapter;
    private ArrayList<GalleryItem> mItems;

    private static final int COLUMN_NUM = 3;
    private static final int ITEM_PER_PAGE = 100;

    private boolean mHasMore = true;
    private boolean mLoading = false;

    //26.1
    private GridView mGridView;

    //27.4
//    private ThumbnailDownloader<ImageView> mThumbnailThread;


    public GalleryFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);

        //setRetainInstance(true);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mRq = Volley.newRequestQueue(getActivity());
//        RequestQueue queue = Volley.newRequestQueue(getActivity());



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItem = mLayoutManager.getItemCount();
                Log.d(TAG, "--------onScrolled-----totalItem is " + totalItem);

                int lastItemPos = mLayoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "--------onScrolled-----lastItemPos is " + lastItemPos);

                if (mHasMore && !mLoading && totalItem -1 != lastItemPos) {
                    startLoading();
                }
            }
        });


        mLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUM);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GalleryAdapter(getActivity(), new ArrayList<GalleryItem>());
//        Log.d("Contact", "--------onCreateView----- generate 100 items for Adapter");
//
//        mAdapter = new GalleryAdapter(getActivity(), Contact.generateSampleList(100));

        mRecyclerView.setAdapter(mAdapter);



        mCustomSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mCustomSwipeRefreshLayout.setOnRefreshListener(
                new CustomSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );



        startLoading();
        return view;
    }

    public void refresh() {
        mAdapter.clear();
        startLoading();
    }

    private void startLoading() {
        Log.d(TAG, "startLoading");
        mLoading = true;

        int totalItem = mLayoutManager.getItemCount();
        final int page = totalItem / ITEM_PER_PAGE + 1;

//        String query = PreferenceManager.
//                getDefaultSharedPreferences(getActivity()).
//                getString(FlickrFetchr.PREF_SEARCH_QUERY, null);

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(UrlManager.PREF_SEARCH_QUERY, null);

        Log.d(TAG, "startLoading--testsearchview-----query is----"+ query);

        String url = UrlManager.getInstance().getItemUrl(query, page);
//        String url = FlickrFetchr.getInstance().getItemUrl(query , page);

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResonse " + response);

                List<GalleryItem> result = new ArrayList<>();

                try {
                    JSONObject photos = response.getJSONObject("photos");

                    if (photos.getInt("pages") == page) {
                        mHasMore = false;
                    }

                    JSONArray photoArr = photos.getJSONArray("photo");
                    for ( int i = 0; i < photoArr.length(); i++) {
                        JSONObject itemObj = photoArr.getJSONObject(i);
                        GalleryItem item = new GalleryItem(
                                itemObj.getString("id"),
                                itemObj.getString("secret"),
                                itemObj.getString("server"),
                                itemObj.getString("farm")
                        );

                        result.add(item);
                    }
                } catch (JSONException e) {

                }
                mAdapter.addAll(result);
                mAdapter.notifyDataSetChanged();
                mLoading = false;
                mCustomSwipeRefreshLayout.refreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        request.setTag(TAG);
        mRq.add(request);

        /*   -------------------------------------------------
        //imagerequest
//        final ImageView mImageView;
        final String url = "http://i.imgur.com/7spzG.png";

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.d(TAG, "ImageRequest onResonse Bitmap " + bitmap);

                        List<Contact> result = new ArrayList<>();
                        Log.d("Contact", "--------ImageRequest, onResponse----- generate 3 items for Adapter");
                        ArrayList<Contact> newlist = (ArrayList) Contact.generateSampleList(3);

                        for (int i = 0; i < newlist.size(); i++) {

                            Contact item = newlist.get(i);


                            //temp test for public image, later change back to private
                            item.image = (ImageView) v.findViewById(R.id.networkimage);

//                            item.getImage().setImageResource(R.drawable.me);
                            item.getImage().setImageBitmap(bitmap);
//                            item.getImage().setImageResource(R.drawable.me);

                            ImageLoader mImageLoader;

                            mImageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
                            mImageLoader.get(url, ImageLoader.getImageListener(item.getImage(),
                                    R.drawable.me, 0));


                            mAdapter.add(item);

                            if (item.getImage() == null) {
                                Log.d(TAG, "ImageRequest onResonse  :  -----------item.getImage() == null " );
                            }else {
                                Log.d(TAG, "ImageRequest onResonse  :  -----------item.getImage() != null " );
                            }
                        }
//                        mAdapter.addAll(newlist);

                        mAdapter.notifyDataSetChanged();
                        //mLoading = false;
                        mCustomSwipeRefreshLayout.refreshComplete();

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
//                        mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(request); //getActivity() extends Context
*/


    }

    private void stopLoading() {
        if (mRq != null) {
            mRq.cancelAll(TAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoading();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu);

//        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) searchItem.getActionView();

        if (mSearchView != null) {
            Log.d("onCreateOptionsMenu", "---testsearchview---- mSearchView is not null  -----------");

            // search suggestion
            mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    String suggestion = getSuggestion(position);

                    if(mSearchView !=null && suggestion !=null) {
                        mSearchView.setQuery(suggestion, true);
                    }
                    return true;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    String suggestion = getSuggestion(position);

                    if(mSearchView !=null && suggestion !=null) {
                        mSearchView.setQuery(suggestion, true);
                    }

                    return true;
                }

                private String getSuggestion(int posiiton) {
                    String suggest = null;

                    if(mSearchView !=null) {
                        Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(posiiton);
                        suggest = cursor.getString(
                                cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                    }
                    return suggest;
                }
            });
        }

        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
        mSearchView.setSearchableInfo(searchInfo);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selectionHandled = false;

        switch (item.getItemId()) {
            case R.id.menu_item_search:
                Log.d("onOptionsItemSelected", "---testsearchview---- menu search request -----------");

                getActivity().onSearchRequested();
                selectionHandled = true;
                break;
            case R.id.menu_item_move:
                if (mRecyclerView != null) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
                selectionHandled = true;
                break;
            case R.id.menu_item_clear:
                Log.d("onOptionsItemSelected", "------- menu clear request -----------");

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                        SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
                suggestions.clearHistory();

                if (mSearchView != null) {
                    mSearchView.setQuery("", false);
                    mSearchView.setIconified(false);
                }

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_SEARCH_QUERY, null)
                        .commit();

                refresh();
                selectionHandled = true;
                break;

            default:
                selectionHandled = super.onOptionsItemSelected(item);
                break;
        }
        return selectionHandled;
    }
}
