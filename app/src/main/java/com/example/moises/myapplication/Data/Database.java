package com.example.moises.myapplication.Data;

import android.util.Log;

import com.example.moises.myapplication.Model.Business;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Moises on 3/11/2017.
 */

public class Database {

    public static ArrayList<Business> BusinessList;

    private FirebaseDatabase database;
    public DatabaseReference myRef;

    public Database(){
        BusinessList =  new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        myRef = database.getReference("business");
        myRef.keepSynced(true);




        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Business b = dataSnapshot.getValue(Business.class);
                Log.d("CYNFO NEW ID " + dataSnapshot.getKey(), "Name "+ b.name);

                for (Business bus:BusinessList) {
                    if(bus.id_Major != b.id_Major){

                    }

                }
                if(b!=null ){
                    BusinessList.add(b);
                    Log.d("CYNFO NEW ID " + dataSnapshot.getKey(), "Name "+ b.name);

                }






            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Business b = dataSnapshot.getValue(Business.class);

                Log.d("CYNFO Modify ID " + s, "Name "+ b.name);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Business b = dataSnapshot.getValue(Business.class);

                Log.d("CYNFO Removed", "Name "+ b.name);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Business b = dataSnapshot.getValue(Business.class);

                Log.d("CYNFO MovedID " + s, "Name "+ b.name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })  ;
        // Read from the database



    }

}
