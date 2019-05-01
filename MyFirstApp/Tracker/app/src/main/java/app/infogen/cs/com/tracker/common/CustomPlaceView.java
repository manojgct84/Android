package app.infogen.cs.com.tracker.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import app.infogen.cs.com.tracker.R;

/**
 * Created by Dell on 12/11/2017.
 */

public class CustomPlaceView implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private final View mWindow;

    public CustomPlaceView(Context mContext) {
        this.mContext = mContext;
        this.mWindow = LayoutInflater.from(mContext).inflate(R.layout.show_place_info, null);
    }

    private void renderTextWindow(Marker maker, View window) {
        String title = maker.getTitle();
        TextView titleView = window.findViewById(R.id.title);

        if (title != null) {
            titleView.setText(title);
        }

        String snippet = maker.getSnippet();
        TextView placeDetails = window.findViewById(R.id.placeDetails);

        if (snippet != null) {
            placeDetails.setText(snippet);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderTextWindow(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderTextWindow(marker, mWindow);
        return mWindow;
    }
}
