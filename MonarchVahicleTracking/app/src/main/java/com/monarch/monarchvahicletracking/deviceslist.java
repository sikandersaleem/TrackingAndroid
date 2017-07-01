package com.monarch.monarchvahicletracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class deviceslist extends AppCompatActivity {

    String carnum="";
    String msg;
    TextView tv22;
    private String TAG = MainActivity.class.getSimpleName();
    private static String url = "https://acc.esajee.com/test.json";
    private ProgressDialog pDialog;
    private ListView lv;
    GPSTracker gps;
    LinearLayout ll;
    LocationManager lm;
    DatabaseReference loc = FirebaseDatabase.getInstance().getReference("location");
    ArrayList<HashMap<String, String>> contactList;
    List<String> devlist= new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv22 =(TextView) findViewById(android.R.id.text1);
        ll = (LinearLayout)findViewById(R.id.ll_main);
        //linearlayout = (LinearLayout)findViewById(R.id.ll_main);
        Toast.makeText(getApplicationContext(), "Devices list", Toast.LENGTH_SHORT).show();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        //final View child = getLayoutInflater().inflate(R.layout.entity_row, null);
        //linearlayout.addView(child);
        //linearlayout.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                msg = "" + position;
                gps = new GPSTracker(deviceslist.this);
                // check if GPS enabled
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                   // DatabaseReference reffortemp = FirebaseDatabase.getInstance().getReference("temp");

                    carnum = (String) ((TextView)view).getText();
                    Toast.makeText(getApplicationContext(),carnum,Toast.LENGTH_SHORT).show();
                    //reffortemp.setValue(text);
                    //Toast.makeText(getApplication(),"data from GPS"+latitude +" , "+longitude,Toast.LENGTH_SHORT).show();
                    location location1 = new location(latitude,longitude,"new location");
                    loc.setValue(location1);
                    Toast.makeText(getApplication(),"updated.",Toast.LENGTH_SHORT).show();
                    deviceslist.this.finish();
                    MainActivity.ma.finish();
                    Intent act=new Intent(deviceslist.this,MainActivity.class);
                    act.putExtra("msg",carnum);
                    startActivity(act);
                    //Intent goto_mainAntivity =new Intent(deviceslist.this,MainActivity.class);
                    ///goto_mainAntivity.putExtra("message",msg);
                   // startActivity(goto_mainAntivity);
                    // \n is for new line
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    //finish();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
        final FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        String amg="users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Vehicles";
       // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
        DatabaseReference ref = fdatabase.getReference(amg);
        //contactList = ref.getKey();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
            {
                devlist.add(String.valueOf(ds.getKey()));
            }

                lv.setAdapter(new ArrayAdapter<String>(deviceslist.this,
                        android.R.layout.simple_list_item_1, devlist));
                pDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
       /* lv.setAdapter(new ArrayAdapter<String>(deviceslist.this,
                R.layout.entity_row,R.id.textView22, devlist));*/

    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
