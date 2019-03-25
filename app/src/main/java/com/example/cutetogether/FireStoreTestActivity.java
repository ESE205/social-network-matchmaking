package com.example.cutetogether;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FireStoreTestActivity extends AppCompatActivity {

    private Button mButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private static final String TAG = "FireStoreTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_store_test);

        mButton = findViewById(R.id.fire_test_btn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireTest4();
            }
        });
    }

    private void fireTest4() {
        Map<String, Object> docData = new HashMap<>();
        String id1 = "test" + Math.random();

        for(int i = 0; i < 500; i++){

            String id2 = "test" + Math.random();
            String status = "none";
            Map<String, Object> nestedData = new HashMap<>();
            nestedData.put("status", status);
            nestedData.put("name", id2);
            ArrayList<String> names = new ArrayList<>();
            names.add("a");
            names.add("b");
            nestedData.put("endorsers", names);
            docData.put(id2, nestedData);
        }



        db.collection("match").document(id1)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
    }

    private void fireTest3() {
        String id1 = "test" + Math.random();
        String id2 = "test" + Math.random();
        String id3 = "test" + Math.random();

        String id4 = "chad";
        String id5 = "Mary";

        String status = "none";

        Map<String, Object> docData = new HashMap<>();

        Map<String, Object> nestedData = new HashMap<>();

        nestedData.put("status", status);
        nestedData.put("name", id5);
        ArrayList<String> names = new ArrayList<>();
        names.add("a");
        names.add("b");
        nestedData.put("endorsers", names);
        docData.put(id5, nestedData);



        db.collection("match").document(id4)
                .update("Mary.endorsers", FieldValue.arrayUnion("c"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

    }

    private void fireTest2() {
        db.collection("match").document("chad").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void fireTest() {
        String id1 = "test" + Math.random();
        String id2 = "test" + Math.random();
        String id3 = "test" + Math.random();
        
        String id4 = "chad";
        String id5 = "Mary";
        
        String status = "none";
        
        Map<String, Object> docData = new HashMap<>();

        Map<String, Object> nestedData = new HashMap<>();
        
        nestedData.put("status", status);
        nestedData.put("name", id5);
        ArrayList<String> names = new ArrayList<>();
        names.add("a");
        names.add("b");
        nestedData.put("endorsers", names);
        docData.put(id1, nestedData);


        
        

        db.collection("match").document(id4)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
        
    }
}
