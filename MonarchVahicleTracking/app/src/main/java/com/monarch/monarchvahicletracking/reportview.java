package com.monarch.monarchvahicletracking;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class reportview extends AppCompatActivity implements OnMapReadyCallback {

    String receivedtype,amg;
    public history hist;
    LatLng view,sydney;
    public double lat, lattt,loggg;
    public double logi;
    int p=0,i;
    private PolylineOptions mPolylineOptions;
    private ProgressDialog mProgress;
    public  location2 loc2;
    private ArrayList<LatLng> arrayPoints;
    GoogleMap googlemap;Handler handler;
    ArrayList<location2> history;
    //double[] laat,lang;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportview);

        Bundle bundle = getIntent().getExtras();
        receivedtype = bundle.getString("type");
        history=new ArrayList<>(100);
        arrayPoints = new ArrayList<LatLng>();
        handler = new Handler();

        mProgress =new ProgressDialog(this);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);



        //laat= new ArrayList<double>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report ("+receivedtype+")");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
//            this.googleMap = googleMap;
        mapFragment.getMapAsync(this);


        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();

        //contactList = ref.getKey();

        if (receivedtype.equals("Play Back")) {
/*            history.add(0,new location2(33.61289555555555,73.12729777777777,"2017-04-11 14:48:31.0"));
            history.add(1,new location2(33.61302888888889,73.12778666666667,"2017-04-11 14:48:47.0"));
            history.add(2,new location2(33.61291111111111,73.12786222222222,"2017-04-11 14:48:47.0"));
            history.add(3,new location2(33.61201777777778,73.12864,"2017-04-11 14:48:47.0"));
            history.add(4,new location2(33.61238,73.12958666666667,"2017-04-11 14:48:47.0"));
            history.add(5,new location2(33.61301777777778,73.13089777777778,"2017-04-11 14:48:47.0"));*/
        if (p<100){
            mProgress.show();
            final FirebaseDatabase firedata = FirebaseDatabase.getInstance();
            amg="Devices/Device/history";
            DatabaseReference ref = firedata.getReference(amg);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        String amd="Devices/Device/history/"+String.valueOf(ds.getKey());

                        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(amd);
                        //Toast.makeText(getApplicationContext(), amd, Toast.LENGTH_SHORT).show();
                        ref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                hist = dataSnapshot.getValue(history.class);
                                //lat=Double.parseDouble(hist.latitude.replace("\"",""));
                                //logi=Double.parseDouble(hist.longitude.replace("\"",""));
                                lat=hist.latitude;
                                logi=hist.longitude;

                                mProgress.dismiss();
                                history.add(p,new location2(hist.latitude,hist.longitude,hist.time));
                                //Toast.makeText(getApplicationContext(), p+" , ", Toast.LENGTH_SHORT).show();
                                p++;
                                if (p==100)
                                {
                                    i=0;
                                    new Timer().scheduleAtFixedRate(new TimerTask() {
                                        @Override
                                        public void run() {
                                                loc2 =history.get(i);
                                            // Toast.makeText(getApplicationContext(),loc2.latitude+ " , " +loc2.longitude,Toast.LENGTH_SHORT).show();
                                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("location4");
                                            dbref.child("latitude").setValue(loc2.latitude);
                                            dbref.child("longitude").setValue(loc2.longitude);
                                            DatabaseReference dbref1 = FirebaseDatabase.getInstance().getReference("location4");
                                            dbref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    loc2 = dataSnapshot.getValue(location2.class);
                                                    lattt = loc2.latitude;
                                                    loggg = loc2.longitude;
                                                    //lastloc = location.lastaddress;
                                                    sydney = new LatLng(lattt, loggg);
                                                    googlemap.clear();
                                                    googlemap.addMarker(new MarkerOptions().position(sydney)
                                                            .title(loc2.time).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                                    googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

                                                    mPolylineOptions = new PolylineOptions();
                                                    mPolylineOptions.color(Color.RED);
                                                    mPolylineOptions.width(5);
                                                    arrayPoints.add(sydney);
                                                    mPolylineOptions.addAll(arrayPoints);
                                                    googlemap.addPolyline(mPolylineOptions);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });

                                            if (i==p)
                                            {
                                                //Toast.makeText(getApplicationContext(),"Ended",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                i++;
                                            }

                                        }
                                    }, 0, 2500);

                                }
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


        }

    }}
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
