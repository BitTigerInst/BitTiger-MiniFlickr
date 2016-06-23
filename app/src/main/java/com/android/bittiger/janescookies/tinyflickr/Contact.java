package com.android.bittiger.janescookies.tinyflickr;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xicheng on 16/6/19.
 */
public class Contact {
    private String name="default";
    private ImageView image=null;
    public static int NoItems = 0;
    private static final String TAG = "Contact" ;

    private static Contact instance = getInstance();

    public static Contact getInstance() {
        if (instance == null) {
            instance = new Contact();
        }

        return instance;
    }

    public String getName() {
        return name;
    }

    public ImageView getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

      // can not generate imageview object here !
    public void setImage() {
        this.image.setImageResource(R.drawable.dog);
    }


    // this is actually initialization for name
    public static List<Contact> generateSampleList(int samples){
        List<Contact> list = new ArrayList<>();
        //Log.d(TAG, "--------generateSampleList-----samples is " + samples + " ----NoItems---- is " + NoItems);

        for(int i= 0 ; i < samples; i++){  // dynamically initialize photoes
            //Contact contact = new Contact();
            Contact contact = getInstance();
            contact.setName("Name - " + i);
//            contact.setImage();//  only get null pointer

            list.add(contact);
        }
        //NoItems = NoItems + samples;
        return list;
    }


}
