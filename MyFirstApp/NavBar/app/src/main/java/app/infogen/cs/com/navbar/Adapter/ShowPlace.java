package app.infogen.cs.com.navbar.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/28/2017.
 */

public class ShowPlace {
    private String showPlace;
    private ArrayList<String> showTimings;

    public ShowPlace(String showPlace, ArrayList<String> showTimings) {
        this.showPlace = showPlace;
        this.showTimings = showTimings;
    }

    public String getShowPlace() {
        return showPlace;
    }

    public void setShowPlace(String showPlace) {
        this.showPlace = showPlace;
    }


    public ArrayList<String> getShowTimings() {
        return showTimings;
    }

    public void setShowTimings(ArrayList<String> showTimings) {
        this.showTimings = showTimings;
    }
}
