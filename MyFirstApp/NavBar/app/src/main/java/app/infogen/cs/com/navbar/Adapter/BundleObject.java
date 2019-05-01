package app.infogen.cs.com.navbar.Adapter;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dell on 11/19/2017.
 */

public class BundleObject {

    private String name;
    private Integer movieImage;
    private String details;
    private String cert;
    private ArrayList<ShowPlace> showPlace;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(Integer movieImage) {
        this.movieImage = movieImage;
    }

    public ArrayList<ShowPlace> getShowPlace() {
        return showPlace;
    }

    public void setShowPlace(ArrayList<ShowPlace> showPlace) {
        this.showPlace = showPlace;
    }
}
