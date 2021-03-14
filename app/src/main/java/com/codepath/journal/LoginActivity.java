package com.codepath.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import Util.iClaimAPI;
import model.Bill;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button createAccountButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private AutoCompleteTextView emailAddress;
    private EditText password;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        emailAddress=findViewById(R.id.Email);
        password=findViewById(R.id.Password);
        progressBar=findViewById(R.id.login_progress);



        loginButton=findViewById(R.id.email_sign_in_button);
        createAccountButton=findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginEmailPasswordUser(emailAddress.getText().toString().trim(), password.getText().toString().trim());
            }
        });
    }
    private void loginEmailPasswordUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            assert user!=null;
                            String currentUserId=user.getUid();

                            collectionReference.whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            assert queryDocumentSnapshots!=null;
                                            if(!queryDocumentSnapshots.isEmpty()) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                    iClaimAPI billApi = iClaimAPI.getInstance();
                                                    billApi.setUsername(snapshot.getString("username"));
                                                    billApi.setUserId(snapshot.getString("userId"));

                                                    //Go to ListActivity
                                                    startActivity(new Intent(LoginActivity.this, PostBillActivity.class));
                                                }

                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });

        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_LONG).show();
        }
    }
}