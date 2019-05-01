package app.infogen.cs.com.tracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.infogen.cs.com.tracker.autoComplete.PlaceAutocompleteAdapter;
import app.infogen.cs.com.tracker.common.CustomPlaceView;
import app.infogen.cs.com.tracker.placeVO.PlaceVO;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.PACKAGE_USAGE_STATS;
import static android.widget.Toast.LENGTH_LONG;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    //GOOGLE MAP VAR
    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15f;
    private Marker mMaker;

    //PERMISSION VAR
    private final static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final static int locationRequestCode = 123;

    private boolean mLocationPermissionGranted = true;

    //LOG VAR
    private String LOGCAT = "MapsActivity";


    //LOCATION VAR
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest mlocationRequest;
    private long UPDATE_INTERVAL = 59 * 1000 ;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationCallback mlocationCallBack;


    //Layour Var
    private AutoCompleteTextView searchLocation;
    private ImageView gpsMyLocation, imageplaceInfo;
    private boolean searchLocationClickFlag = false;
    private Button click;

    //AutoComplete
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient googleApiClient;
    private final static LatLngBounds LAT_LNG_BOUND = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    //Data Place
    PlaceVO placeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        searchLocation = (AutoCompleteTextView) findViewById(R.id.input_search);
        gpsMyLocation = (ImageView) findViewById(R.id.ic_gsp);
        imageplaceInfo = (ImageView) findViewById(R.id.place_info);
        getLocationPermission();
        click = (Button) findViewById(R.id.click);


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDeviceLocation();
    }


    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(mlocationCallBack);
    }


    private void inti() {
        Log.d(LOGCAT, "inti initializing the searchLocation");

        googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build();

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, LAT_LNG_BOUND, null);

        searchLocation.setAdapter(placeAutocompleteAdapter);

        //Call the Item Click Listener
        searchLocation.setOnItemClickListener(onItemClickListener);

        searchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //Location of the search
                    geoLocationAddress();
                }
                return false;
            }
        });

        gpsMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGCAT, "GSP icon clicked");
                searchLocationClickFlag = false;
                getDeviceLocation();
            }
        });

        imageplaceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mMaker.isInfoWindowShown()) {
                        mMaker.hideInfoWindow();
                    } else {
                        Log.d(LOGCAT, "onClick: place info: " + placeInfo.toString());
                        mMaker.showInfoWindow();
                    }
                } catch (NullPointerException ex) {
                    Log.d(LOGCAT, " Place info NullPointer Exception" + ex);
                }
            }
        });

//Tab View
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tabActivity = new Intent(MapsActivity.this, BasicActivity.class);
                startActivity(tabActivity);
            }
        });
        softKeyboardHide();

    }

    private void geoLocationAddress() {
        String searchText = searchLocation.getText().toString();

        Geocoder geoCoderLocation = new Geocoder(MapsActivity.this);

        List<Address> addressLst = new ArrayList<>();
        try {
            addressLst = geoCoderLocation.getFromLocationName(searchText, 1);
        } catch (IOException ex) {
            Log.e(LOGCAT, "Exception on the search Geo Lcoation", ex);
        }

        if (addressLst.size() > 0) {
            Address address = addressLst.get(0);
            Log.d(LOGCAT, "GeoCoder Address Location" + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, (address.getLocality() == null) ? address.getAddressLine(0) : address.getLocality());

        }

    }

    private void getDeviceLocation() {
        Log.d(LOGCAT, "getDeviceLocation");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Create the location request to start receiving updates
        mlocationRequest = new LocationRequest();
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FASTEST_INTERVAL);

        try {
            if (mLocationPermissionGranted) {

                mlocationCallBack = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult);
                    }
                };

                fusedLocationProviderClient.requestLocationUpdates(mlocationRequest, mlocationCallBack, Looper.myLooper());

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(LOGCAT, "Got the location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "MyLocation");
                        } else {
                            Log.d(LOGCAT, "Didnt get the location");
                            Toast.makeText(MapsActivity.this, "Didnt get the current location", LENGTH_LONG).show();
                        }
                    }
                });
            }

        } catch (SecurityException ex) {
            Log.e(LOGCAT, "Permission Exception Security error :", ex);
        }
    }

    private void onLocationChanged(LocationResult locationResult) {

        // New location has now been determined
        String msg = "New Updated Location: Latitude " + Double.toString(locationResult.getLastLocation().getLatitude()) + ", Longitude: " + Double.toString(locationResult.getLastLocation().getLongitude());

        Log.d(LOGCAT, msg);
        if (!searchLocationClickFlag) {
            moveCamera(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), 30f);
        }

    }

    private void moveCamera(LatLng latLng, float zoom, PlaceVO placeInfo) {
        Log.d(LOGCAT, "moveCamera : moving the cam to " + "latitude :" + latLng.latitude + " longitude :" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.setInfoWindowAdapter(new CustomPlaceView(MapsActivity.this));

        String place = "Address :" + placeInfo.getAddress() + "\n" +
                "Phone Number :" + placeInfo.getPhoneNumber() + "\n" +
                "WebSite : " + placeInfo.getWebUri();
        MarkerOptions option = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(place);
        mMaker = mMap.addMarker(option);

        softKeyboardHide();
    }

    //Blue dot move
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(LOGCAT, "moveCamera : moving the cam to " + "latitude :" + latLng.latitude + " longitude :" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void moveCamera(LatLng latLng, float zoom, String locationTitle) {
        Log.d(LOGCAT, "moveCamera : moving the cam to " + "latitude :" + latLng.latitude + " longitude :" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!locationTitle.equals("MyLocation")) {
            MarkerOptions makerOptions = new MarkerOptions().position(latLng).title(locationTitle);
            mMap.addMarker(makerOptions);
        } else {
            MarkerOptions makerOptions = new MarkerOptions().position(latLng).title("MyLocation");
            mMap.addMarker(makerOptions);
        }
        softKeyboardHide();
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Log.d(LOGCAT, "Getting the may ready inti Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            //Enables the blue dot in the map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //Enables the gps current location icon
            mMap.setMyLocationEnabled(true);
            //Disable the default gps current location icon
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //get the geoCoder Address on the search
            inti();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void getLocationPermission() {
        Log.d(LOGCAT, "getLocationPermission");
        String[] permission = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                Log.d(LOGCAT, "requestPermissions");
                ActivityCompat.requestPermissions(this, permission, locationRequestCode);
            }
        } else {
            Log.d(LOGCAT, "requestPermissions");
            ActivityCompat.requestPermissions(this, permission, locationRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(LOGCAT, "onRequestPermissionsResult");
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
        Log.d(LOGCAT, "mLocationPermissionGranted :" + mLocationPermissionGranted);
        if (mLocationPermissionGranted) {
            initMap();
        }
    }

    //To hide the keyboard after enter
    private void softKeyboardHide() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /*

    -------------------------Get the place for the search from the Auto Complete --------------------------
     */

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            softKeyboardHide();
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String palceId = item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, palceId);
            placeResult.setResultCallback(updatePlaceDetailsCallBaack);
            searchLocationClickFlag = true;
        }

        private ResultCallback<PlaceBuffer> updatePlaceDetailsCallBaack = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    places.release();
                    return;
                }

                placeInfo = new PlaceVO();

                try {
                    Place place = places.get(0);
                    placeInfo.setAddress(place.getAddress().toString());
                    placeInfo.setName(place.getName().toString());
                    placeInfo.setLatLng(place.getLatLng());

                } catch (NullPointerException ex) {
                    Log.d(LOGCAT, "Null Point Exception :" + ex);
                }

                places.release();
                moveCamera(placeInfo.getLatLng(), DEFAULT_ZOOM, placeInfo);
                // moveCamera(placeInfo.getLatLng(), DEFAULT_ZOOM, placeInfo.getName());
            }
        };
    };
}
