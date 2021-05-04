package com.codepath.iClaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import Util.iClaimAPI;

public class DashboardActivity extends AppCompatActivity {
    private String TAG = "DashboardActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");


    private String userName;
    private String balance;
    private TextView userNameTextView;
    private TextView amountTextView;


    private Button buttonBill;
    private Button buttonClaim;

    private Test2Speech t2s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        userNameTextView = findViewById(R.id.username_textView);
        amountTextView = findViewById(R.id.amounttextView);

        if(iClaimAPI.getInstance() != null)
        {
            userName=iClaimAPI.getInstance().getUsername();
            userNameTextView.setText(userName);

            balance = iClaimAPI.getInstance().getBalance().toString();
            amountTextView.setText(balance);

        }


        buttonBill = findViewById(R.id.buttonbill);
        buttonBill.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if(user != null && firebaseAuth != null) {
                                                  startActivity(new Intent(DashboardActivity.this, BillListActivity.class));
                                              }
                                          }
                                      }
                );
        buttonClaim = findViewById(R.id.buttonclaim);
        buttonClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(DashboardActivity.this, PostBillActivity.class));
                }
            }
        });

        t2s = new Test2Speech(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.signout:
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                }
            case R.id.action_speak_ms:
                if (user != null && firebaseAuth != null) {
                    t2s.speakBalance(this);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(iClaimAPI.getInstance() != null)
        {
            userName=iClaimAPI.getInstance().getUsername();
            userNameTextView.setText(userName);

            balance = iClaimAPI.getInstance().getBalance().toString();
            amountTextView.setText(balance);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        t2s.done();
    }
}