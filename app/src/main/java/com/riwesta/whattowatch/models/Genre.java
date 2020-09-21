package com.riwesta.whattowatch.models;

import java.io.Serializable;

public class Genre implements Serializable {

    private String name;
    private int image;

    public Genre(String name, int image) {

        this.name = name;
        this.image = image;
    }


    public String getName() {

        return name;
    }

    public int getImage() {

        return image;
    }
}
