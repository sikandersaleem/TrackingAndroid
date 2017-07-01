package com.monarch.monarchvahicletracking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notificationactivity extends AppCompatActivity {

    private ListView list;
    public notificationreceived noti_rev;
    private ProgressDialog pDialog;
    public String notiid, getdate,gettime,getmsg;
    String isread;
    TextView datee,mesg;

    List<String> notilist= new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationactivity);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        datee = (TextView) findViewById(R.id.dateView) ;
        mesg = (TextView) findViewById(R.id.messageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.notifylist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                notiid = (String) ((TextView)view).getText();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/"+notiid);
                ref.addValueEventListener(new ValueEventListener() {

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        noti_rev = dataSnapshot.getValue(notificationreceived.class);
                        gettime=noti_rev.Time;
                        getdate=noti_rev.Date;
                        getmsg=noti_rev.Message;
                        isread=noti_rev.Read;
                       //Toast.makeText(getApplicationContext(),"get "+gettime,Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(notificationactivity.this);
                        builder1.setTitle(notiid);
                        builder1.setMessage(getmsg+System.getProperty("line.separator")+System.getProperty("line.separator")+Html.fromHtml("<b>"+getdate+"</b>")+System.getProperty("line.separator")+"at "+gettime);
                        builder1.setCancelable(false);
                        builder1.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        if(!isFinishing())
                        {
                            alert11.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        });

        final FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        String amg="users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications";
        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
        DatabaseReference ref = fdatabase.getReference(amg);
        //contactList = ref.getKey();
        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    notilist.add(String.valueOf(ds.getKey()));
                }
                list.setAdapter(new ArrayAdapter<String>(notificationactivity.this,
                        android.R.layout.simple_list_item_1, notilist));
                pDialog.dismiss();
            }

            @Override
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
