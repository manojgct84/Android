package app.infogen.cs.com.retrofitandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import app.infogen.cs.com.retrofitandroid.retrofit.api.EndpointInterface;
import app.infogen.cs.com.retrofitandroid.retrofit.api.ClientApi;
import app.infogen.cs.com.retrofitandroid.retrofit.api.response.ResponseLocationResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_SHORT;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;


    private boolean mLocationPermissionGranted = false;
    private final static int requestPermission = 123;

    private boolean success = false;
    private boolean searchOnClick = false;

    private final static String TAG = MapsActivity.class.getSimpleName();

    private Button bntRestaurant;

    //Fuse Location Api
    FusedLocationProviderClient fusedLocationProviderClient;
    private double latitude;
    private double longitude;
    private int PROXIMITY_RADIUS = 1;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        bntRestaurant = (Button) findViewById(R.id.bntRestaurant);
        getLocationPermission();
        getSearchNearBy();

    }

    private void getLocationPermission() {

        Log.d(TAG, "Location Permission");
        String[] permission = {ACCESS_COARSE_LOCATION, ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                Log.d(TAG, "requestPermissions");
                ActivityCompat.requestPermissions(this, permission, requestPermission);
            }
        } else {
            Log.d(TAG, "requestPermissions");
            ActivityCompat.requestPermissions(this, permission, requestPermission);
        }
        Log.d(TAG, "Location Permission mLocationPermissionGranted " + mLocationPermissionGranted);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case requestPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    mLocationPermissionGranted = false;
                }
            }
        }
        if (mLocationPermissionGranted) {
            initMap();
        }
    }

    private void initMap() {
        Log.d(TAG, "Map Init");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getSearchNearBy() {
        searchOnClick = true;

        //googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build()

        bntRestaurant = findViewById(R.id.bntRestaurant);

        bntRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNearbyLocation("restaurant");
            }
        });
    }

    private void getNearbyLocation(String placeType) {
        Retrofit retrofit = ClientApi.getClinet();

        EndpointInterface serviceOperation = retrofit.create(EndpointInterface.class);
        Call<ResponseLocationResult> responseLocationResult = serviceOperation.getNearbyPlaces(placeType, latitude + "," + longitude, PROXIMITY_RADIUS);
        responseLocationResult.enqueue(new Callback<ResponseLocationResult>() {
            @Override
            public void onResponse(Call<ResponseLocationResult> call, Response<ResponseLocationResult> response) {
                try {
                    mMap.clear();
                    if (response.body().getResults().size() > 0) {
                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            double resultLatitude = response.body().getResults().get(i).getGeometry().getGoogleLocationVO().getLat();
                            double resultLongitude = response.body().getResults().get(i).getGeometry().getGoogleLocationVO().getLng();
                            Log.d(TAG, "Result  resultLatitude" + resultLatitude + " resultLongitude " + resultLongitude);
                        }
                    }

                } catch (Exception ex) {
                    Log.d(TAG, "Service call has failed" + ex.getStackTrace());
                }

            }

            @Override
            public void onFailure(Call<ResponseLocationResult> call, Throwable t) {
                Log.d(TAG, "Service call has failed");
                Toast.makeText(MapsActivity.this, "Service call has failed", LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", LENGTH_SHORT).show();
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined in a raw resource file.
            success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission not granted");
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            if (!searchOnClick) {
                Log.d(TAG, "On Map Ready");
                getDeviceLocation();
            }
            getSearchNearBy();
        }
    }

    private void getDeviceLocation() {

        Log.d(TAG, "Get Device Location");

        try {
            if (mLocationPermissionGranted) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                final Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Location Successful");
                            Location currentLocation = (Location) task.getResult();

                            if (currentLocation != null){
                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();
                            }else {
                                Log.d(TAG, "Not able to get the location");
                            }
                            Log.d(TAG, "Location Successful getDeviceLocation " + "latitude " + latitude + " longitude " + longitude);

                        } else {
                            Log.d(TAG, "Not able to get the location");
                            Toast.makeText(MapsActivity.this, "Unable to fetch the location", LENGTH_SHORT).show();
                        }
                    }
                });

            }

        } catch (SecurityException ex) {
            Log.e(TAG, "Permission Exception Security error :", ex);
        }
    }


}
