package com.android.bittiger.janescookies.tinyflickr;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xicheng on 16/6/19.
 */
public class Contact {
    private String name="default";
    private ImageView image=null;
    static int NoItems = 0;

    public Contact() {
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
    public static List<Contact> generateSampleList(){
        List<Contact> list = new ArrayList<>();
        for(int i= 0; i < 100; i++){
            Contact contact = new Contact();
            contact.setName("Name - " + i);
//            contact.setImage();//  only get null pointer

            list.add(contact);
        }

        return list;
    }


}
