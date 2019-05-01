package app.infogen.cs.com.tracker.placeVO;

import android.location.Address;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Dell on 12/10/2017.
 */

public class PlaceVO {

    private String name;
    private String phoneNumber;


    private String id;
    private Uri webUri;
    private LatLng latLng;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWebUri() {
        return webUri;
    }

    public void setWebUri(Uri webUri) {
        this.webUri = webUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PlaceVO{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", webUri=" + webUri +
                ", latLng=" + latLng +
                ", address='" + address + '\'' +
                '}';
    }
}
