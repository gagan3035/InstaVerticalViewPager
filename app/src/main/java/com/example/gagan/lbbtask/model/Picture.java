package com.example.gagan.lbbtask.model;

import java.io.Serializable;

/**
 * Created by Gagan on 5/28/2016.
 */
public class Picture implements Serializable {
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    private String URL;


}
