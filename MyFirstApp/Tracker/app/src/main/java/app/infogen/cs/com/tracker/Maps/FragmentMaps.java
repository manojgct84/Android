package app.infogen.cs.com.tracker.Maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

import app.infogen.cs.com.tracker.HybridFragment;
import app.infogen.cs.com.tracker.R;

/**
 * Created by Dell on 12/16/2017.
 */

//This class is having issue with the getSupportFragmentManager().

public class FragmentMaps extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    //  private final View mapLayout;

    private Context mContext;

    private String TAG = FragmentMaps.class.getSimpleName();

    private boolean mLocationPermissionGranted = false;


    //PERMISSION VAR
    private final static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final static int locationRequestCode = 123;

    public FragmentMaps(FragmentActivity activity) {
        this.mContext = activity;
        //    mapLayout = LayoutInflater.from(mContext).inflate(R.layout.map_tab_layout, null);
    }

    public void initTab() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Log.d(TAG, "Getting the may ready inti Map");
        getLocationPermission();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;


        if (mLocationPermissionGranted) {
            //getDeviceLocation();
            //Enables the blue dot in the map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            try {
                // Customise the styling of the base map using a JSON object defined in a raw resource file.
                boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
            }


            //Enables the gps current location icon
            mMap.setMyLocationEnabled(true);
            //Disable the default gps current location icon
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //Google Api Map Style
            //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            //get the geoCoder Address on the search
            // inti();
        }
    }

    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission");
        String[] permission = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(mContext, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(mContext, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                Log.d(TAG, "requestPermissions");
                ActivityCompat.requestPermissions(this, permission, locationRequestCode);
            }
        } else {
            Log.d(TAG, "requestPermissions");
            ActivityCompat.requestPermissions(this, permission, locationRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case locationRequestCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;

                } else {
                    mLocationPermissionGranted = false;
                }
            }

        }
        Log.d(TAG, "mLocationPermissionGranted :" + mLocationPermissionGranted);
        if (mLocationPermissionGranted) {
            initMap();
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_tab_layout);

        mapFragment.getMapAsync(this);
    }

}
