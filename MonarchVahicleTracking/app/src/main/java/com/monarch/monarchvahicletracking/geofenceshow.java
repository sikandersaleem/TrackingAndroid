package com.monarch.monarchvahicletracking;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class geofenceshow extends AppCompatActivity implements OnMapReadyCallback {

    String keyget="";
    String name,type;
    public locations locforgeo;
    public geofenceget geoget;
    public LatLng latlang;
    private PolylineOptions mPolylineOptions;
    private PolygonOptions mPolygonOptions;
    ArrayList<LatLng> arrayPoints;
    public ArrayList<locations> arrlocations;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofenceshow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        keyget = bundle.getString("keyvalue");
        //Toast.makeText(getApplicationContext(),keyget,Toast.LENGTH_SHORT).show();

        initGMaps();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Devices/Device/DeviceGeofence/"+keyget+"/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //name = dataSnapshot.getValue();
                geoget= dataSnapshot.getValue(geofenceget.class);
                //String value = (String) dataSnapshot.getValue();
                //Toast.makeText(getApplicationContext(),"inmethod"+dataSnapshot.getValue()+value,Toast.LENGTH_SHORT).show();
                name=geoget.name;
                type=geoget.type;
                arrlocations = geoget.locations;
                arrayPoints = new ArrayList<LatLng>(geoget.locations.size());
                for (int i=0;i<arrlocations.size();i++)
                {
                    locforgeo=arrlocations.get(i);
                    //Toast.makeText(getApplicationContext(),locforgeo.latitude+","+locforgeo.longitude,Toast.LENGTH_SHORT).show();
                    latlang=new LatLng(locforgeo.latitude,locforgeo.longitude);
                    //Toast.makeText(getApplicationContext(),locforgeo.latitude+" , "+locforgeo.longitude,Toast.LENGTH_SHORT).show();
                    arrayPoints.add(latlang);
                }
                //Toast.makeText(getApplicationContext(),arrlocations.toString(),Toast.LENGTH_SHORT).show();
                if (type.equals("circle"))
                {
                    String title = latlang.latitude + ", " + latlang.longitude;
                    // Define marker options
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latlang)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title(title);
                    map.addMarker(markerOptions);
                    CircleOptions circleOptions = new CircleOptions()
                            .center(latlang)
                            .strokeColor(Color.argb(50, 70,70,70))
                            .fillColor( Color.argb(100, 150,150,150) )
                            .radius(500 );
                    map.addCircle( circleOptions );
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 16));
                }
                else if (type.equals("polyline"))
                {
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.RED);
                    mPolylineOptions.width(5);
                    mPolylineOptions.addAll(arrayPoints);
                    map.addPolyline(mPolylineOptions);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 16));
                }
                else if (type.equals("polygon"))
                {
                    mPolygonOptions = new PolygonOptions();
                    mPolygonOptions.strokeColor(Color.argb(50, 70,70,70));
                    mPolygonOptions .fillColor( Color.argb(100, 150,150,150) );
                    mPolygonOptions.addAll(arrayPoints);
                    map.addPolygon(mPolygonOptions);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 16));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

       private void initGMaps(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapgeofenceshow);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.clear();
    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
