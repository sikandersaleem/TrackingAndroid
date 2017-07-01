package com.monarch.monarchvahicletracking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class contactus extends AppCompatActivity {

    public ourcontact cu;
    //String getlocation,getphoneno,getemail;

    TextView tv15,tv2,tv5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv15 = (TextView) findViewById(R.id.phtext);
        tv2 = (TextView) findViewById(R.id.loctext);
        tv5 = (TextView) findViewById(R.id.textmail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ourcontact");
        ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                cu = dataSnapshot.getValue(ourcontact.class);
                /*getlocation=cu.location1;
                getphoneno=cu.phoneno;
                getemail=cu.mailus;*/

                tv15.setText(cu.phoneno);
                tv2.setText(cu.mailus);
                tv5.setText(cu.location1);
            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
