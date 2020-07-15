package com.example.myapplication;

import java.io.Serializable;

public class GenreRepository implements Serializable {

    private String name;
    private int image;

    public GenreRepository(String name, int image) {

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
