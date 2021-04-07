package com.codepath.iClaim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.SystemParameterOrBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Util.iClaimAPI;
import model.Bill;

public class PostBillActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView addbillsPhotoButton;
    private EditText titleEditText;
    private EditText billsEditText;
    private TextView currentUserTextView;
    private static final String TAG= "PostBillActivity";
    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private Uri imageUri;
    private ImageView imageView;

    private TextView detectedTextView;

    // Connection to Firestore

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference=db.collection("iClaim");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_bill);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        storageReference= FirebaseStorage.getInstance().getReference();

        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.post_progressBar);
        titleEditText=findViewById(R.id.post_title_et);
        billsEditText =findViewById(R.id.post_description_et);
        currentUserTextView=findViewById(R.id.post_username_textview);

        imageView=findViewById(R.id.post_imageView);
        saveButton=findViewById(R.id.post_save_iClaim_button);
        saveButton.setOnClickListener(this);
        addbillsPhotoButton =findViewById(R.id.postCameraButton);
        addbillsPhotoButton.setOnClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);

        detectedTextView = findViewById(R.id.detectedtextview);

        if(iClaimAPI.getInstance() != null)
        {
            currentUserId=iClaimAPI.getInstance().getUserId();
            currentUserName=iClaimAPI.getInstance().getUsername();
            currentUserTextView.setText(currentUserName);
        }
        authStateListener=new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {

                }
                else{}

            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.post_save_iClaim_button:
                //save bills
                saveBill();
                adjustBalance();
                break;

            case R.id.postCameraButton:
                //get image from gallery or phone
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
                break;
        }

    }

    private void saveBill() {
        final String title=titleEditText.getText().toString().trim();
        final String thoughts= billsEditText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);


        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) &&imageUri!=null)
        {
            final StorageReference filePath=storageReference.child("bills_image_").child("my_image"+ Timestamp.now().getNanoseconds());

            filePath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl=uri.toString();
                                    // Todo: create a iclaim object
                                    Bill bill =new Bill();
                                    bill.setTitle(title);
                                    bill.setThought(thoughts);
                                    bill.setImageUrl(imageUrl);
                                    bill.setTimeAdded(new Timestamp(new Date()));
                                    bill.setUserName(currentUserName);
                                    bill.setUserId(currentUserId);

                                    //Todo: invoke our collectionReference
                                    collectionReference.add(bill)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                      @Override
                                                                      public void onSuccess(DocumentReference documentReference) {
                                                                          progressBar.setVisibility(View.INVISIBLE);
                                                                          startActivity(new Intent(PostBillActivity.this, BillListActivity.class));
                                                                          finish();
                                                                      }
                                                                  })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "on Failure"+e.getMessage());

                                                }
                                            });
                                                    //Todo: save iclaim instance

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

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                imageUri=data.getData();
                imageView.setImageURI(imageUri);
                //for text recognition
                FirebaseVisionActivity activity = new FirebaseVisionActivity(this, detectedTextView);
                activity.firebaseVisionImage(imageUri);
            }
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(firebaseAuth!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //adjust balance in firestore after bill is saved in storage
    private void adjustBalance(){

        String docRef = iClaimAPI.getInstance().getRef();
        DocumentReference userRef =  db.collection("Users").document(docRef);


        String text = detectedTextView.getText().toString();
        Double amt;
        Double balance;

        Pattern p = Pattern.compile("\\d+(((\\.)(\\d{0,2})){0,1})");
        Matcher m = p.matcher(text);

        if (m.find()) {
            amt = Double.valueOf(m.group());
            balance = iClaimAPI.getInstance().getBalance();
            if(balance > amt ){

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(userRef);
                        //System.out.println("testing: "+ snapshot);

                        DecimalFormat df = new DecimalFormat("####0.00");
                        df.setRoundingMode(RoundingMode.UP);
                        Double newbalance = Double.valueOf(df.format((balance - amt)));

                        transaction.update(userRef, "balance", newbalance);
                        iClaimAPI.getInstance().setBalance(newbalance);

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Transaction success!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Transaction failure.", e);
                            }
                        });

            }
            else{

                Log.d("adjustbalance failed: ",amt.toString());
                Toast.makeText(this, "Amount exceeds limit. Needs Review!!",Toast.LENGTH_LONG).show();

            }

        }
    }
}

