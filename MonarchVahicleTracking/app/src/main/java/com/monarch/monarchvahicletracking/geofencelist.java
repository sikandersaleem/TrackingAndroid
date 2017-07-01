package com.monarch.monarchvahicletracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class geofencelist extends AppCompatActivity {

    private ProgressDialog pDialog;
    String nd,listget;
    geofenceget geofenceget;
    int i=0;
    Object value;
    private ListView lvforgeo;
    HashMap<String,String> hmforgeo;
    List<String> geolist= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencelist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hmforgeo = new HashMap<String, String>();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        lvforgeo = (ListView) findViewById(R.id.listforgeofence);
        lvforgeo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Toast.makeText(getApplicationContext(),(String) ((TextView)view).getText(),Toast.LENGTH_SHORT).show();
                //listget = hmforgeo.get((String) ((TextView)view).getText());
                //Toast.makeText(getApplicationContext(),(String) ((TextView)view).getText(),Toast.LENGTH_SHORT).show();
                if (hmforgeo.containsKey((String) ((TextView)view).getText())) {
                   value = hmforgeo.get((String) ((TextView)view).getText());
                }

                Intent geoshow=new Intent(geofencelist.this,geofenceshow.class);
                geoshow.putExtra("keyvalue",value.toString());
                startActivity(geoshow);

            }
        });

        String amg="Devices/Device/DeviceGeofence/";
        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(amg);
        //contactList = ref.getKey();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    nd = String.valueOf(ds.getKey());
                    //Toast.makeText(getApplicationContext(),ds.getValue().toString(),Toast.LENGTH_SHORT).show();

                    geofenceget = ds.getValue(geofenceget.class);
                    hmforgeo.put(geofenceget.name,ds.getKey());
                    geolist.add(geofenceget.name);

                }
                lvforgeo.setAdapter(new ArrayAdapter<String>(geofencelist.this,
                        android.R.layout.simple_list_item_1, geolist));
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addfeogence) {
            startActivity(new Intent(geofencelist.this,geofenceadd.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
