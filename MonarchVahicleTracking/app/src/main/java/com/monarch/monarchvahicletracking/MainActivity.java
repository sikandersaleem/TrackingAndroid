package com.monarch.monarchvahicletracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private ProgressDialog mProgress;
    String email,uid,deviceid;
    String veh;
    public location location;
    TextView hemail,huid,total_devices;
    private String TAG = MainActivity.class.getSimpleName();
    String temp;
    String incmng;
    public double lat, lattt,loggg;
    public double logi;
    String asd;
    String[] data;
    String abc;
    String lastloc;
    GoogleMap googlemap;
    public  static  MainActivity ma;
    private PolylineOptions mPolylineOptions;
    boolean doubleBackToExitPressedOnce = false;
    LatLng sydney;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance();
        FirebaseInstanceId.getInstance().getToken();

        ma= this;

        //total_devices=(TextView)findViewById(R.id.total_devices);

        Bundle bundle = getIntent().getExtras();
        //if (bundle != null)
                incmng = bundle.getString("msg");
        asd = "users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Vehicles/" + incmng + "/lastlocation";
        //Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
        //check= getString(position);
        //check = bundle.getString("message");
        Firebase.setAndroidContext(this);
        if (Firebase.getDefaultConfig().isPersistenceEnabled() == false)
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase myFirebaseRef = new Firebase("https://monarchvahicletracking.firebaseio.com/");
        myFirebaseRef.keepSynced(true);

        mProgress =new ProgressDialog(this);
        mProgress.setMessage("Getting map ready for you...");
        mProgress.setCancelable(false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//            this.googleMap = googleMap;
        mapFragment.getMapAsync(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Toast.makeText(this,"check: retreving data from fire base.",Toast.LENGTH_SHORT).show();
            // Name, email address, and profile photo Url
            email = user.getEmail();

            if(incmng.equals(""))
            {
                mProgress.show();
                String amg="users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Vehicles";
                // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(amg);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            abc=String.valueOf(ds.getKey());
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Vehicles/" + abc + "/lastlocation");
                            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // veh = (String) dataSnapshot.getValue();
                                    // Toast.makeText(getApplicationContext(),veh,Toast.LENGTH_SHORT).show();
                                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp);
                                    location = dataSnapshot.getValue(location.class);
                                    lat = location.latitude;
                                    logi = location.longitude;
                                    lastloc = location.lastaddress;

                                    /*DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("location");
                                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // veh = (String) dataSnapshot.getValue();
                                            // Toast.makeText(getApplicationContext(),veh,Toast.LENGTH_SHORT).show();
                                            //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp);
                                            location = dataSnapshot.getValue(location.class);
                                            lat = location.latitude;
                                            logi = location.longitude;
                                            lastloc = location.lastaddress;

                                            //Toast.makeText(getApplicationContext(), "getting location data in data chnge class " + lat + " , " + logi, Toast.LENGTH_SHORT).show();
                                            //     Toast.makeText(getApplicationContext(), location.lastaddress, Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(getApplicationContext(), asd, Toast.LENGTH_SHORT).show();
                                            //initilizeMap();
                                            sydney = new LatLng(lat, logi);
                                            //googlemap.clear();
                                            mProgress.dismiss();

                                            googlemap.addMarker(new MarkerOptions().position(sydney)
                                                    .title(lastloc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                            googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5));

                                            googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                                            {
                                                public boolean onMarkerClick(Marker arg0) {

                                                    googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0.getPosition(), 16));
                                                    return  true;
                                                }

                                            });
                                            //updateCamera();

                                            //csvdata();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });*/
                                    //Toast.makeText(getApplicationContext(), "getting location data in data chnge class " + lat + " , " + logi, Toast.LENGTH_SHORT).show();
                                    //     Toast.makeText(getApplicationContext(), location.lastaddress, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(), asd, Toast.LENGTH_SHORT).show();
                                    //initilizeMap();
                                    sydney = new LatLng(lat, logi);
                                    //googlemap.clear();
                                    mProgress.dismiss();

                                    googlemap.addMarker(new MarkerOptions().position(sydney)
                                            .title(lastloc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5));

                                    googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                                    {
                                        public boolean onMarkerClick(Marker arg0) {

                                            googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0.getPosition(), 16));
                                            return  true;
                                        }

                                    });
                                    //updateCamera();

                                    //csvdata();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
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
            else {
                //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference(asd);
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                                location = dataSnapshot.getValue(location.class);
                                lat = location.latitude;
                                logi = location.longitude;
                                lastloc = location.lastaddress;

                                Toast.makeText(getApplicationContext(), "getting location data in data chnge class " + lat + " , " + logi, Toast.LENGTH_SHORT).show();
                                //     Toast.makeText(getApplicationContext(), location.lastaddress, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), asd, Toast.LENGTH_SHORT).show();
                                //initilizeMap();
                                sydney = new LatLng(lat, logi);
                                googlemap.clear();
                                googlemap.addMarker(new MarkerOptions().position(sydney)
                                        .title(lastloc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                updateCamera();

                                //csvdata();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
            //Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //uid = user.getUid();
        }

        //hemail.setText("newfile");
        //hemail.setText("hello");
        //header_uid.setText("User ID: " + uid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        hemail=(TextView)header.findViewById(R.id.header_email);
        //huid=(TextView)header.findViewById(R.id.header_uid);
        //huid.setText("User ID: " + uid);
        hemail.setText(email);

    }
    //private void initilizeMap() {

    //}

    public void csvdata()
    {
        DatabaseReference ref_data = FirebaseDatabase.getInstance().getReference("Devices/Device/history");
        mProgress.setMessage("Uploading CSV...");
        mProgress.show();
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(getAssets()
                    .open("traccarpositions.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            //String[] data;
            //ArrayList points = new ArrayList();
            //PolylineOptions lineOptions = new PolylineOptions();

            //int temp=1;
            while ((line = reader.readLine()) != null) {

                data = line.split(",");
                //Toast.makeText(getApplicationContext(),"data="+data[2]+" , "+data[3],Toast.LENGTH_SHORT).show();
                String a = data[0].toString().replaceAll("\"", "");
                a = a.replaceAll("\n", "");

                String b = data[2].toString().replaceAll("\"", "");
                b = b.replaceAll("\n", "");

                String c = data[3].toString().replaceAll("\"", "");
                c = c.replaceAll("\n", "");

                DatabaseReference ref3 = ref_data.push();
                //Map<String, Object> info = new HashMap<String, Object>();

                //ref_data.push(ref3.getKey(), info);
                ref_data.child(ref3.getKey()).child("time").setValue(a);
                ref_data.child(ref3.getKey()).child("latitude").setValue(Double.parseDouble(b));
                ref_data.child(ref3.getKey()).child("longitude").setValue(Double.parseDouble(c));


                /*lat = Double.parseDouble(a);
                logi = Double.parseDouble(b);
                sydney = new LatLng(lat, logi);*/

                //String ch =data[1].toString().replace("\"","");
                //Toast.makeText(getApplicationContext(),ch,Toast.LENGTH_SHORT).show();
                /*if(ch.equals("false"))
                {

                    googlemap.addMarker(new MarkerOptions().position(sydney)
                            .title(data[9].toString().replace("\"","")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    //Toast.makeText(getApplicationContext(),"temo called red",Toast.LENGTH_SHORT).show();
                    mPolylineOptions.color(Color.RED).width(10);
                    temp=1;
                }

                else {
                    if (temp==1)
                    {
                        googlemap.addMarker(new MarkerOptions().position(sydney)
                                .title(data[9].toString().replace("\"","")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        //Toast.makeText(getApplicationContext(),"temo called green",Toast.LENGTH_SHORT).show();
                        mPolylineOptions.color(Color.BLUE).width(10);
                        temp=0;
                    }

//                Float.parseFloat(dat)

                    //try {
                    //lat = new Double(data[2]);
//                    logi= Double.parseDouble(String.format("%.8f",data[3]));
                    //logi = new Double(data[3]);
                   // Toast.makeText(getApplicationContext(), "data=" + lat + " , " + logi, Toast.LENGTH_SHORT).show();
                    //LatLng position = new LatLng(lat, logi);

                    *//*updatePolyline();
                    updateCamera();
                    updateMarker();*//*

                }*/
//                MarkerOptions mp = new MarkerOptions().position(current)
//                        .title("Text");
//                Marker m = new Marker();
                /*points.add(position);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
                lineOptions.add(position);
                googlemap.addPolyline(lineOptions);*/
    
//                googlemap.addMarker(mp);
//                googlemap.moveCamera(CameraUpdateFactory.newLatLng(current));
//                googlemap.clear();


//                    initilizeMap();
               /* }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"exception",Toast.LENGTH_SHORT).show();
                }*/
            }
            mProgress.dismiss();
//            lineOptions.addAll(points);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void updatePolyline() {
        //googlemap.clear();
        googlemap.addPolyline(mPolylineOptions.add(sydney));
    }
    private void updateMarker() {
        //googlemap.addMarker(new MarkerOptions().position(sydney));
    }
    private void updateCamera() {
        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
    }
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        googlemap.clear();
        googlemap.getUiSettings().setZoomControlsEnabled(true);
        //sydney = new LatLng(lat,logi);
        initializeMap();
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            //geocoder.getFromLocation(lat, logi, 1); //1 num of possible location returned
//            address = geocoder.getFromLocation(lat, logi, 1).get(0).getAddressLine(0); //0 to obtain first possible address
//            city = geocoder.getFromLocation(lat, logi, 1).get(0).getLocality();
//            country = geocoder.getFromLocation(lat, logi, 1).get(0).getCountryName();
//            postalcode = geocoder.getFromLocation(lat, logi, 1).get(0).getPostalCode();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //create your custom title
       /* String title = "hjhg";// address +"-"+city+"-"+country+" ("+postalcode+") ";
        googlemap.addMarker(new MarkerOptions().position(sydney)
                .title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        googlemap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
    private void initializeMap() {
        mPolylineOptions = new PolylineOptions();
        //mPolylineOptions.color(Color.BLUE).width(10);
        //csvdata();
    }
       public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.refresh) {
            Intent intent = getIntent();
            intent.putExtra("msg","");
            finish();
            startActivity(intent);
            return true;
        }
        else if (id == R.id.exitmain) {
            finish();
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.devices) {
            //finish();
            startActivity(new Intent(MainActivity.this, deviceslist.class));

        } else if (id == R.id.geofence) {

            startActivity(new Intent(MainActivity.this, geofencelist.class));

        } else if (id == R.id.reports) {

            startActivity(new Intent(MainActivity.this, reportcriteria.class));

        } else if (id == R.id.notification) {

            startActivity(new Intent(MainActivity.this, notificationactivity.class));

        } else if (id == R.id.contactus) {

            startActivity(new Intent(MainActivity.this, contactus.class));

        } else if (id == R.id.singout) {
            mProgress.show();
            FirebaseAuth.getInstance().signOut();
            finish();
            mProgress.dismiss();
            startActivity(new Intent(MainActivity.this, LoginActivity.class)); //Go back to home page

        } else if (id == R.id.exit) {
            finish();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                System.exit(0);
                return true;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    doubleBackToExitPressedOnce=false;

                }
            }, 2000);
        return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
