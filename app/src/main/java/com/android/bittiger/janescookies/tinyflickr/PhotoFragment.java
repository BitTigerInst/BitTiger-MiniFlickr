package com.android.bittiger.janescookies.tinyflickr;


import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {
    // tag for logcat
    public static final String TAG = PhotoFragment.class.getSimpleName();

    private ProgressBar mProgressBar;
    private TextView mDescText;
    private ImageView mPhoto;

    private GalleryItem mItem;
    private RequestQueue mRq;
    private DownloadManager mDownloadManager;

    private boolean mLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,
                container,
                false);

        mItem = (GalleryItem) getActivity().getIntent().getSerializableExtra("item");

        mDownloadManager = (DownloadManager) getActivity().getSystemService(
                getActivity().DOWNLOAD_SERVICE);
        mRq = Volley.newRequestQueue(getActivity());

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mDescText = (TextView) view.findViewById(R.id.desc_text);

        mPhoto = (ImageView) view.findViewById(R.id.photo);
        Glide.with(this).load(mItem.getUrl()).thumbnail(0.5f).into(mPhoto);

        // download original single photo
        LinearLayout downloadView = (LinearLayout) view.findViewById(R.id.download);
        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPhoto();
                Toast.makeText(getActivity(), "Start downloading", Toast.LENGTH_LONG).show();
            }
        });

        // open url link for Flickr official app
        LinearLayout openView = (LinearLayout) view.findViewById(R.id.open);
        openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });

        // load original photo
        startLoading();
        return view;
    }

    // function for downloading original photo when download button is pressed
    private void downloadPhoto() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mItem.getUrl()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("SharkFeed Download");
        request.setDescription(mItem.getUrl());
        mDownloadManager.enqueue(request);
    }

    // function for opening Flickr official app when open button is pressed
    private void openApp () {
        String url = UrlManager.getInstance().getFlickrUrl(mItem.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    // function for loading single photo description by Volley
    private void startLoading() {
        mLoading = true;
        mProgressBar.setVisibility(View.VISIBLE);
        String url =  UrlManager.getInstance().getPhotoInfoUrl(mItem.getId());
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject photo = response.getJSONObject("photo");
                            JSONObject descObj = photo.getJSONObject("description");
                            String desc = descObj.getString("_content");
                            mDescText.setText(desc);
                        } catch (JSONException e) {
                            if(e != null) {
                                Toast.makeText(getActivity(), e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        mProgressBar.setVisibility(View.GONE);
                        mLoading = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                    }
                }
        );

        request.setTag(TAG);
        mRq.add(request);
    }

    // cancel downloading request when fragment is stopped
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
}
