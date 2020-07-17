package com.riwesta.whattowatch;

import java.io.Serializable;


public class MoviesRepository implements Serializable {

    private String id;
    private String name;
    private String year;
    private String rate;
    private String logo;
    private String image;
    private String imdb;

    public MoviesRepository(String id, String name, String year, String rate, String logo,
                            String image, String imdb) {

        this.id = id;
        this.name = name;
        this.year = year;
        this.rate = rate;
        this.logo = logo;
        this.image = image;
        this.imdb = imdb;
    }

    public String getId() {

        return id;
    }


    public String getName() {

        return name;
    }


    public String getRate() {

        return rate;
    }

    public String getYear() {

        return year;
    }

    public String getLogo() {

        return logo;
    }

    public String getImage() {

        return image;
    }

    public String getImdb() {

        return imdb;
    }
}