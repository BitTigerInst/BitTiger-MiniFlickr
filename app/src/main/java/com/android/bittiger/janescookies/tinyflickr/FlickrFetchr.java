package com.android.bittiger.janescookies.tinyflickr;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xicheng on 16/6/27.
 */
public class FlickrFetchr {

    public static final String API_KEY = "b8cbad529685c20682f8c6d88ad200fd" ;
    // must public
    public static final String PREF_SEARCH_QUERY = "searchQuery";

    /*----------- private -------------------------*/
    private static final String TAG = FlickrFetchr.class.getSimpleName();

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/" ;
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent" ;
    private static final String METHOD_SEARCH = "flickr.photos.search" ;
    private static final String PARAM_EXTRAS = "extras" ;
    private static final String PARAM_TEXT = "query" ;
    private static final String PAGE = "page";
    private static final String EXTRA_SMALL_URL = "url_s" ;

    private static final String XML_PHOTO = "photo" ;

    // Singleton, make sure only max. 1 instance exist, even for multi-thread
    private static volatile FlickrFetchr instance = null;

    public void FlickrFetcher() {

    }

    public static FlickrFetchr getInstance() {
        if (instance == null) {
            synchronized (FlickrFetchr.class) {
                if (instance == null ) {
                    instance = new FlickrFetchr();
                }
            }
        }
        return instance;
    }

    public static String getItemUrl(String query, int page) {
        String url;
        if (query !=null) {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_SEARCH)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("text", query)
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        } else {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .build().toString();
        }
        return url;
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //URLConnection to HttpURLConnection

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // GET request ;  if for POST , use getOutputStream()
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK ) {
                return null ;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            while ( (bytesRead = in.read(buffer)) > 0 ) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }


    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }



}
