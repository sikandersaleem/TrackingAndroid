package com.monarch.monarchvahicletracking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class reportcriteria extends AppCompatActivity implements View.OnClickListener,  AdapterView.OnItemSelectedListener {

    Calendar c;
    int startyear,startmonth,startday;
    int endyear,endmonth,endday;

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private Button enter;
    private String datafromspinn,typefromspinn;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private Spinner devicespin,typespin;
    List<String> devlist= new ArrayList<String>();
    List<String> typelist= new ArrayList<String>();
    ArrayAdapter<String> dataAdapter, typeAdapter;

    private SimpleDateFormat dateFormatter;
    Calendar startdate = Calendar.getInstance();
    Calendar enddate = Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportcriteria);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        c = Calendar.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fromDateEtxt = (EditText) findViewById(R.id.getstartdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt = (EditText) findViewById(R.id.getenddate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        devicespin =(Spinner) findViewById(R.id.selectdevices);
        devicespin.requestFocus();
        typespin =(Spinner) findViewById(R.id.selecttype);

        enter = (Button) findViewById(R.id.getdata);

        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);
        enter.setOnClickListener(this);
        devicespin.setOnItemSelectedListener(this);
        devicespin.setPrompt("Select Devices");
        typespin.setOnItemSelectedListener(this);
        typespin.setPrompt("Select Type");

        fromDateEtxt.setText(dateFormatter.format(c.getTime()));
        toDateEtxt.setText(dateFormatter.format(c.getTime()));

        typelist = new ArrayList<String>();
        typelist.add("Play Back");
        typelist.add("Route View");

        /*startyear = c.get(Calendar.YEAR);
        startmonth = c.get(Calendar.MONTH);
        startday = c.get(Calendar.DAY_OF_MONTH);
        endyear = c.get(Calendar.YEAR);
        endmonth = c.get(Calendar.MONTH);
        endday = c.get(Calendar.DAY_OF_MONTH);*/


        final FirebaseDatabase firedata = FirebaseDatabase.getInstance();
        String amg="users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Vehicles";
        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
        DatabaseReference ref = firedata.getReference(amg);
        //contactList = ref.getKey();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    devlist.add(String.valueOf(ds.getKey()));

                }
                dataAdapter = new ArrayAdapter<String>(reportcriteria.this,android.R.layout.simple_spinner_item, devlist);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                devicespin.setAdapter(dataAdapter);

                typeAdapter = new ArrayAdapter<String>(reportcriteria.this,android.R.layout.simple_spinner_item, typelist);
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typespin.setAdapter(typeAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                startdate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(startdate.getTime()));
            }

        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                enddate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(enddate.getTime()));
            }

        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.getstartdate) {
            fromDatePickerDialog.show();
        } else if(v.getId() == R.id.getenddate) {
            toDatePickerDialog.show();
        } else if(v.getId() == R.id.getdata){
            Toast.makeText(getApplicationContext(),"You select " + datafromspinn + " from "+ dateFormatter.format(startdate.getTime())+" to "+dateFormatter.format(enddate.getTime()),Toast.LENGTH_SHORT).show();
            Intent go=new Intent(reportcriteria.this, reportview.class);
            go.putExtra("type",typefromspinn);

            startActivity(go);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.selectdevices)
        {
            datafromspinn = parent.getItemAtPosition(position).toString();
        }
        else if(spinner.getId() == R.id.selecttype)
        {
            typefromspinn = parent.getItemAtPosition(position).toString();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
