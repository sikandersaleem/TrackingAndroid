package com.monarch.monarchvahicletracking;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class reportview2 extends AppCompatActivity implements OnMapReadyCallback {

    String receivedtype;
    public history hist;
    LatLng view,sydney;
    Thread splashTread;
    public double lat, lattt,loggg;
    public double logi;
    int p=0,i;
    public  location2 loc2;
    GoogleMap googlemap;Handler handler;
    //List<Double> laat, lang;
    double[] laat,lang;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportview);

        Bundle bundle = getIntent().getExtras();
        receivedtype = bundle.getString("type");
        handler = new Handler();
        laat=new double[100];
        lang=new double[100];
        //laat= new ArrayList<double>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report ("+receivedtype+")");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
//            this.googleMap = googleMap;
        mapFragment.getMapAsync(this);

        if (receivedtype.equals("Play Back")){

            final FirebaseDatabase firedata = FirebaseDatabase.getInstance();
            String amg="Devices/Device/history";
            // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
            DatabaseReference ref = firedata.getReference(amg);
            //contactList = ref.getKey();

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        String amd="Devices/Device/history/"+String.valueOf(ds.getKey());

                        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(amd);
                        //Toast.makeText(getApplicationContext(), amd, Toast.LENGTH_SHORT).show();
                           ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                hist = dataSnapshot.getValue(history.class);
                                //lat=Double.parseDouble(hist.latitude.replace("\"",""));
                                //logi=Double.parseDouble(hist.longitude.replace("\"",""));
                                lat=hist.latitude;
                                logi=hist.longitude;
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("location2");
                                dbref.child("time").setValue(hist.time);
                                dbref.child("latitude").setValue(lat);
                                dbref.child("longitude").setValue(logi);
                                DatabaseReference dbref1 = FirebaseDatabase.getInstance().getReference("location2");
                                dbref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        // veh = (String) dataSnapshot.getValue();
                                                        // Toast.makeText(getApplicationContext(),veh,Toast.LENGTH_SHORT).show();
                                                        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp);
                                        loc2 = dataSnapshot.getValue(location2.class);
                                        lattt = loc2.latitude;
                                        loggg = loc2.longitude;
                                                        //lastloc = location.lastaddress;
                                        sydney = new LatLng(lattt, loggg);
                                        //googlemap.clear();
                                        googlemap.addMarker(new MarkerOptions().position(sydney)
                                                .title(loc2.time).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

                                                        // yourMethod();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
           // upload();

        }

    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void upload(){


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        googlemap.clear();
        googlemap.getUiSettings().setZoomControlsEnabled(true);
        /*view = new LatLng(14.3154, 94.5644);
        googlemap.addMarker(new MarkerOptions().position(view)
                .title("check").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(view, 16));*/

    }
    /*public void onMapReady(GoogleMap googleMap) {


        //nitializeMap();
//
    }*/
}
