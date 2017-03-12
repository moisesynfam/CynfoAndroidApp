package com.example.moises.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.moises.myapplication.Model.Business;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Semaphore;

import static org.altbeacon.beacon.BeaconParser.ALTBEACON_LAYOUT;


public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private final String TAG = "MoisesBeacons";
    final Region region = new Region(TAG, Identifier.parse("5fd85e4c-8bd1-11e6-ae22-56b6b6499611"), null, null);
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BeaconManager beaconManager;
    private ArrayList<AreaAdsFragment> areaAdsFragments;
    private int bestSignal = -1000;
    private int lastAreaVisited = 1232445;
    private int signalCounter = 0;

    public static ArrayList<Business> BusinessList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        areaAdsFragments = new ArrayList<AreaAdsFragment>();
        BusinessList =  new ArrayList<>();

        // Permission To locate beacons ///
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This App needs location Access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok,null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
                String s = "this is a test";


            }

        }

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("business");


        /*
        Business business = new Business(2,"Apple");
        Area area = new Area(2,"music");
        Advertisement ad = new Advertisement("Title","lol","http://webneel.com/daily/sites/default/files/images/project/creative-advertisement%20(13).jpg");
        area.ads.add(ad);
        business.areas.add(area);
        myRef.child(String.valueOf(business.id_Major)).setValue(business);
        */
        myRef.keepSynced(true);


         myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            synchronized public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot single: dataSnapshot.getChildren() ) {

                    Business b = single.getValue(Business.class);
                    AreaAdsFragment areaAdsFragment = new AreaAdsFragment();
                    areaAdsFragment.FragmentBusiness = b;
                    areaAdsFragments.add(areaAdsFragment);

                    if(b!=null){
                        Log.d("CYNFO INITIAL READ ID " + dataSnapshot.getKey(), "Name "+ b.name);
                        BusinessList.add(b);
                    }
                }
                CallFragment();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Business newBussiness = dataSnapshot.getValue(Business.class);
                //Log.d("CYNFO NEW ID " + dataSnapshot.getKey(), "Name "+ newBussiness.name);

                BusinessList.add(newBussiness);
//
//                AreaAdsFragment areaAdsFragment = new AreaAdsFragment();
//                areaAdsFragment.FragmentBusiness = newBussiness;
//                areaAdsFragments.add(areaAdsFragment);
//
//
//
//                Log.d("CYNFO ADDED ID " + dataSnapshot.getKey(), "Name " + newBussiness.name);
//                CallFragment();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Business b = dataSnapshot.getValue(Business.class);

                Log.d("CYNFO MODIFICATION", "Object modified: Name " + b.name);

                Log.d("CYNFO MODIFICATION","Is empty: "+areaAdsFragments.isEmpty());
                if (!areaAdsFragments.isEmpty()) {
                    for (int i = 0; i < areaAdsFragments.size(); i++) {

                        Log.d("CYNFO MODIFICATION","Condition: "+areaAdsFragments.get(i).FragmentBusiness.name +" "+ b.id_Major);
                        if (areaAdsFragments.get(i).FragmentBusiness.id_Major == b.id_Major) {


                            areaAdsFragments.get(i).BusinessChange(b);
                            Log.d("CYNFO MODIFICATION", "Object " + b.name+"modified correctly");
                            break;
                        }
                    }


                }
                for (Business savedBussines : BusinessList) {

                        Log.d("CYNFO MODIFICATION", "Modification: Checking - "+ savedBussines.name);

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Business b = dataSnapshot.getValue(Business.class);

                if (!BusinessList.isEmpty()) {
                    for (int i = 0; i < BusinessList.size(); i++) {
                        if (BusinessList.get(i).id_Major == b.id_Major) {
                            Log.d("CYNFO Modify ID ", "Name " + b.name);
                            BusinessList.remove(i);
                            break;
                        }
                    }


                }
                for (Business savedBussines : BusinessList) {

                    Log.d("CYNFO check modified", "Name "+ savedBussines.name);

                }


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




        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(ALTBEACON_LAYOUT));
        beaconManager.bind(this);
//        AreaAdsFragment fragment1 = AreaAdsFragment.newInstance(0,1);
//        AreaAdsFragment fragment2 = AreaAdsFragment.newInstance(1,1);
//        areaAdsFragments.add(fragment1);
//        areaAdsFragments.add(fragment2);






    }

    public void CallFragment(){

        if(areaAdsFragments!=null){
            for (AreaAdsFragment a : areaAdsFragments){
                Log.d("CYNFO FRAGMENT", "CHANGE "+a.FragmentBusiness.name);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,areaAdsFragments.get(0))
                    .commit();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onBeaconServiceConnect() {


        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size()>0){

                    Beacon activeBeacon = beacons.iterator().next();

                    Log.i(TAG,"BEACON FOUND!");
                    Log.i(TAG,"MAC:"+activeBeacon.getBluetoothAddress());
                    Log.i(TAG,"MAJOR:"+activeBeacon.getId2().toInt());
                    Log.i(TAG,"MINOR:"+activeBeacon.getId3().toInt());
                    Log.i(TAG,"RSSI:"+activeBeacon.getRssi());

                    if(signalCounter>=3){
                        Log.i(TAG,"RESTARTING COUNTER - RESETTING SIGNAL VALUE");
                        bestSignal = -100;
                        signalCounter = 0;
                    }


                   /* if(activeBeacon.getRssi()>bestSignal && activeBeacon.getRssi()>-90&&lastAreaVisited!=activeBeacon.getId3().toInt()){
                        Log.i(TAG,"INSIDE");
                        bestSignal = activeBeacon.getRssi();
                        lastAreaVisited = activeBeacon.getId3().toInt();
                        Log.i(TAG,"Las visited Area RENEW:"+lastAreaVisited);
                        if(!areaAdsFragments.isEmpty()){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container,areaAdsFragments.get(activeBeacon.getId3().toInt()))
                                    .commit();
                        }
                    }
                    */
                    signalCounter++;



                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        }catch (RemoteException e){
            Log.e(TAG,e.getMessage());
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
}
