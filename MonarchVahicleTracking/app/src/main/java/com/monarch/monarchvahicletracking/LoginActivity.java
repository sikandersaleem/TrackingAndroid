package com.monarch.monarchvahicletracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    String carnum="";
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgress;
    String uemail, uid, deviceid;
    String token;
    TelephonyManager mngr;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mProgress =new ProgressDialog(this);
        mProgress.setMessage("Signing in...");
        mProgress.setCancelable(false);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    finish();
                    mProgress.dismiss();
                    Intent myintent= new Intent(LoginActivity.this, MainActivity.class);
                    myintent.putExtra("msg",carnum);
                    startActivity(myintent);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
                    mEmailSignInButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String email = mEmailView.getText().toString();
                            String password = mPasswordView.getText().toString();
                            mProgress.show();
                            signIn(email,password);
                /*
                mProgress.dismiss();
                finish();
                Intent myintent= new Intent(LoginActivity.this, MainActivity.class);
                myintent.putExtra("message",message);
                startActivity(myintent);*/
                        }
                    });
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void dataupload(){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        //deviceid = mngr.getDeviceId().toString();
        token = FirebaseInstanceId.getInstance().getToken();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_data = database.getReference("users");
        //deviceregistrationid d_r_id=new deviceregistrationid(token);
        //userdevices u_devices=new userdevices(deviceid, d_r_id);

        //userid u_id=new userid(uemail,u_devices);

        //List<String> deviceid=new ArrayList<String>();
        //deviceid.add(token);
        //HashMap<Objects> devices=new HashMap();


        //List<String> devices1= new ArrayList<String>();
        //HashMap<String, HashMap<String, Objects>> userr=new HashMap();
        //userr.put(uemail,devices);
       // devices1.add(token);
        //HashMap<String,Object> uid= new HashMap<String, Object>();

        //user user1=new user(uid,u_id);
        //ref_data.child("user").setValue(uid);
        ref_data.child(uid).child("email").setValue(uemail);
        ref_data.child(uid).child("Devices").child(deviceid).setValue(token);

        //Toast.makeText(getApplicationContext()," Users data Updated on firebase",Toast.LENGTH_SHORT).show();
    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn(final String email, String password) {
        Log.d(TAG, "signIn:" + email);
      /*  if (!validateForm()) {
            return;
        }*/

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            //mEmailView.setText("");
                            mPasswordView.setText("");
                        }
                        else
                        {
                            dataupload();
                            finish();
                            mProgress.dismiss();
                            Intent myintent= new Intent(LoginActivity.this, MainActivity.class);
                            myintent.putExtra("msg",carnum);
                            //Toast.makeText(getApplicationContext(),"sending carnum",Toast.LENGTH_SHORT).show();
                            startActivity(myintent);
                        }
                    }
                });
    }
}

