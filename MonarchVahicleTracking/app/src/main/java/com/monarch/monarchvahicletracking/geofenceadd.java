package com.monarch.monarchvahicletracking;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class geofenceadd extends AppCompatActivity implements

        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
     OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private static final String TAG = geofenceadd.class.getSimpleName();

    private GoogleMap map;

    int check=0;
    Button radius,create;
    LinearLayout lil;
    EditText geoname;
    String type;
    private ProgressDialog pDialog;
    LatLng newlatlang;
    private PolylineOptions mPolylineOptions;
    private PolygonOptions mPolygonOptions;
    public int GEOFENCE_RADIUS = 500;
    private ArrayList<LatLng> arrayPoints;

    // private TextView textLat, textLong;

    private SupportMapFragment mapFragment;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, geofenceadd.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofenceadd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radius = (Button) findViewById(R.id.radius);
        create = (Button) findViewById(R.id.savegeo);
        geoname = (EditText) findViewById(R.id.geofencename);
        lil = (LinearLayout) findViewById(R.id.creategeo);

        radius.getBackground().setAlpha(128);
        radius.setVisibility(View.INVISIBLE);
        lil.getBackground().setAlpha(128);
        lil.setVisibility(View.INVISIBLE);
        create.getBackground().setAlpha(192);
        arrayPoints = new ArrayList<LatLng>();
        // initialize GoogleMaps
        initGMaps();
        radius.setOnClickListener(this);
        create.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving...");
        pDialog.setCancelable(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.startgeo: {
                if(check==2 || check==3)
                {
                    Toast.makeText(getApplicationContext(), "First clear map", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    check=1;
                    type = "circle";
                }
                return true;
            }
            case R.id.startpoly: {
                if(check==1 || check==3)
                {
                    Toast.makeText(getApplicationContext(), "First clear map", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    check=2;
                    type = "polyline";
                    startpolyline();
                }
                return true;
            }

            case R.id.startpolygon: {
                if(check==1 || check==2)
                {
                    Toast.makeText(getApplicationContext(), "First clear map", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    check=3;
                    type = "polygon";
                    startpolyline();
                }
                return true;
            }
            case R.id.clear: {
                check=0;
                radius.setVisibility(View.INVISIBLE);
                lil.setVisibility(View.INVISIBLE);
                map.clear();
                type = "";
                geoname.setText("");
                newlatlang = null;
                arrayPoints.clear();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void startpolyline() {
        Toast.makeText(getApplicationContext(),"Enter points to draw polyline geofence.",Toast.LENGTH_SHORT).show();

    }
    // Initialize GoogleMaps
    private void initGMaps(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapgeofence);
        mapFragment.getMapAsync(this);
    }

    // Callback called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnMapLongClickListener(this);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick("+latLng +")");

        if (check==0)
        {
            map.clear();
            radius.setVisibility(View.INVISIBLE);
            radius.setVisibility(View.INVISIBLE);
            arrayPoints.clear();
        }
        else if (check==1)
        {
            map.clear();
            radius.setVisibility(View.VISIBLE);
            lil.setVisibility(View.VISIBLE);
            markerForGeofence(latLng);
            newlatlang=latLng;
        }
        else if (check==2)
        {
            MarkerOptions marker=new MarkerOptions();
            newlatlang =latLng;
            marker.position(newlatlang);
            map.addMarker(marker);
            mPolylineOptions = new PolylineOptions();
            mPolylineOptions.color(Color.RED);
            mPolylineOptions.width(5);
            arrayPoints.add(newlatlang);
            mPolylineOptions.addAll(arrayPoints);
            map.addPolyline(mPolylineOptions);
            lil.setVisibility(View.VISIBLE);
        }
        else if (check==3)
        {
            MarkerOptions marker=new MarkerOptions();
            newlatlang =latLng;
            marker.position(newlatlang);
            map.addMarker(marker);
            arrayPoints.add(newlatlang);
            mPolygonOptions = new PolygonOptions();
            mPolygonOptions.strokeColor(Color.argb(50, 70,70,70));
            mPolygonOptions .fillColor( Color.argb(100, 150,150,150) );
            mPolygonOptions.addAll(arrayPoints);
            map.addPolygon(mPolygonOptions);
            lil.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onMapLongClick(LatLng ll) {
        if (check==2) {
            ll = arrayPoints.get(0);
            arrayPoints.add(ll);
            mPolylineOptions.addAll(arrayPoints);
            map.addPolyline(mPolylineOptions);
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClickListener: " + marker.getPosition() );
        return false;
    }

    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
           map.addMarker(markerOptions);
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( GEOFENCE_RADIUS );
        map.addCircle( circleOptions );
            //inputprompt(latLng);
    }
    // Start Geofence creation process
    public void inputprompt(final LatLng ilatLng){
        AlertDialog.Builder builder = new AlertDialog.Builder(geofenceadd.this);
        builder.setTitle("Enter Radius");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        //View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.textinputprompt, null);
        LayoutInflater layoutInflater = LayoutInflater.from(geofenceadd.this);
        View promptView = layoutInflater.inflate(R.layout.textinputprompt, null);
        // Set up the input
        final EditText input = (EditText) promptView.findViewById(R.id.inputforprm);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(promptView);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                GEOFENCE_RADIUS = Integer.parseInt(input.getText().toString());
                //Toast.makeText(getApplicationContext(),"Radius = "+GEOFENCE_RADIUS,Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(),"circle",Toast.LENGTH_SHORT).show();
                map.clear();
                ;
                map.addMarker(new MarkerOptions()
                        .position(ilatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .title(ilatLng.latitude + ", " + ilatLng.longitude));
                CircleOptions circleOptions = new CircleOptions()
                        .center(ilatLng)
                        .strokeColor(Color.argb(50, 70,70,70))
                        .fillColor( Color.argb(100, 150,150,150) )
                        .radius( GEOFENCE_RADIUS );
                map.addCircle( circleOptions );
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void onClick(View v) {
        if (v.getId()==R.id.radius)
        {
            inputprompt(newlatlang);
        }
        else if (v.getId()==R.id.savegeo)
        {
            if (newlatlang==null)
            {
                Snackbar snackbar = Snackbar
                        .make(v, "Geofence not saved, create geofence first.", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            else
            {
                pDialog.show();
                DatabaseReference ref_geofence = FirebaseDatabase.getInstance().getReference("Devices/Device/DeviceGeofence/");
                DatabaseReference newref = ref_geofence.push();
                if (check==1)
                {
                    arrayPoints.add(newlatlang);
                    ref_geofence.child(newref.getKey()).child("name").setValue(geoname.getText().toString());
                    ref_geofence.child(newref.getKey()).child("type").setValue(type);
                    ref_geofence.child(newref.getKey()).child("locations").setValue(arrayPoints);

                }
                else if (check==2)
                {
                    ref_geofence.child(newref.getKey()).child("name").setValue(geoname.getText().toString());
                    ref_geofence.child(newref.getKey()).child("type").setValue(type);
                    ref_geofence.child(newref.getKey()).child("locations").setValue(arrayPoints);
                }
                else if (check==3)
                {
                    ref_geofence.child(newref.getKey()).child("name").setValue(geoname.getText().toString());
                    ref_geofence.child(newref.getKey()).child("type").setValue(type);
                    ref_geofence.child(newref.getKey()).child("locations").setValue(arrayPoints);
                }

                Toast.makeText(getApplicationContext(), "Geofence saved succesfully.", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                this.finish();
            }
        }
    }
}