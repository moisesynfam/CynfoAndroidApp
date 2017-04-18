package com.example.moises.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.moises.myapplication.Data.Database;
import com.example.moises.myapplication.Model.Advertisement;
import com.example.moises.myapplication.Model.Area;
import com.example.moises.myapplication.Model.Business;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import static org.altbeacon.beacon.BeaconParser.ALTBEACON_LAYOUT;


public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private final String TAG = "MoisesBeacons";
    final Region region = new Region(TAG, Identifier.parse("5fd85e4c-8bd1-11e6-ae22-56b6b6499611"), null, null);
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BeaconManager beaconManager;
    private ArrayList<AreaAdsFragment> areaAdsFragments;
    private int bestSignal = -1000;
    private int lastAreaVisited = -1;
    private int signalCounter = 0;
    private FirebaseDatabase database;
    private int lastBussinesVisited = -1;
    private int lastBussinesVisitedID = -1;

    public static NotificationManager mNotificationManager;
    static public Context context;
    //public static ArrayList<Business> BusinessList;
    public static Map<String,Business> BusinessList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        context = this;

        areaAdsFragments = new ArrayList<AreaAdsFragment>();
        //BusinessList =  new ArrayList<>();
        BusinessList = new HashMap<>();

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




        database = Database.getDatabase();

        DatabaseReference myRef = database.getReference("businessTest");



//        Business business = new Business(1,"Forever 21","http://cdn.girabsas.com/122015/1467228434092.jpg");
//        Area area = new Area(1,"Women","http://picture-cdn.wheretoget.it/egqn1d-i.jpg");
//        Area area2 = new Area(2,"Men","https://quemepongoblog.files.wordpress.com/2014/07/03_-banner_forever21_man_.jpg?w=750");
//        Advertisement ad = new Advertisement(0,"Line 0","lol","http://thebestfashionblog.com/wp-content/uploads/2014/02/Forever-21-Womens-Sweaters-2014-5.jpg",4);
//        Advertisement ad2 = new Advertisement(1,"Line 1","lol","http://www.thefashionisto.com/wp-content/uploads/2015/01/Plaid-Bomber-Jacket.jpg",4);
//        area.ads.put("eewdwqeq",ad);
//        area2.ads.put("eewdeq",ad2);
//        business.areas.put("weqsdmc",area);
//        business.areas.put("weqewefsdmc",area2);
//        myRef.child(String.valueOf(business.id_Major)).setValue(business);

        myRef.keepSynced(true);



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

               Business newBussiness = dataSnapshot.getValue(Business.class);
                Log.d("CYNFO NEW ID " + dataSnapshot.getKey(), "Name "+ newBussiness.name);


                AreaAdsFragment areaAdsFragment = new AreaAdsFragment();
                areaAdsFragment.FragmentBusiness = newBussiness;
                areaAdsFragments.add(areaAdsFragment);

                Log.d("CYNFO ADDED ID " + dataSnapshot.getKey(), "Name " + newBussiness.name);




            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final Business b = dataSnapshot.getValue(Business.class);

                Log.d("CYNFO MODIFICATION", "Object modified: Name " + b.name);

                Log.d("CYNFO MODIFICATION","Is empty: "+areaAdsFragments.isEmpty());
                if (!areaAdsFragments.isEmpty()) {
                    for (int i = 0; i < areaAdsFragments.size(); i++) {

                        Log.d("CYNFO MODIFICATION","Condition: "+areaAdsFragments.get(i).FragmentBusiness.name +" "+ b.id_Major);
                        if (areaAdsFragments.get(i).FragmentBusiness.id_Major == b.id_Major) {
                            final int ii = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    areaAdsFragments.get(ii).BusinessChange(b);
                                }
                            });

                            Log.d("CYNFO MODIFICATION", "Object " + b.name+"modified correctly");
                            break;
                        }
                    }


                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Business b = dataSnapshot.getValue(Business.class);

                if (!areaAdsFragments.isEmpty()) {
                    for (int i = 0; i < areaAdsFragments.size(); i++) {
                        if (areaAdsFragments.get(i).FragmentBusiness.id_Major == b.id_Major) {
                            Log.d("CYNFO Modify ID ", "Name " + b.name);
                            //areaAdsFragments.getremove(i);
                            break;
                        }
                    }


                }

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

    public void CallFragment(int bussinessID){

        if(areaAdsFragments!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,areaAdsFragments.get(bussinessID))
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

                    final Beacon activeBeacon = beacons.iterator().next();

//                    Log.i(TAG,"BEACON FOUND!");
//                    Log.i(TAG,"MAC:"+activeBeacon.getBluetoothAddress());
//                    Log.i(TAG,"MAJOR:"+activeBeacon.getId2().toInt());
//                    Log.i(TAG,"MINOR:"+activeBeacon.getId3().toInt());
//                    Log.i(TAG,"RSSI:"+activeBeacon.getRssi());

                    if(signalCounter>=3){
                        Log.i(TAG,"RESTARTING COUNTER - RESETTING SIGNAL VALUE");
                        bestSignal = -100;
                        signalCounter = 0;
                    }


                   if(activeBeacon.getRssi()>bestSignal && activeBeacon.getRssi()>-90){
                        Log.i(TAG,"INSIDE");
                        bestSignal = activeBeacon.getRssi();


                       if(lastBussinesVisited != activeBeacon.getId2().toInt()&&!areaAdsFragments.isEmpty()){

                           for (int i=0 ; i<areaAdsFragments.size();i++ ){
                               if (areaAdsFragments.get(i).FragmentBusiness.id_Major == activeBeacon.getId2().toInt()){

                                   areaAdsFragments.get(i).presentArea =activeBeacon.getId3().toInt();
                                   CallFragment(i);

                                   lastAreaVisited = activeBeacon.getId3().toInt();
                                   lastBussinesVisited = activeBeacon.getId2().toInt();
                                   lastBussinesVisitedID = i;
                                   break;


                               }


                           }

                        }

                       if(lastAreaVisited!= activeBeacon.getId3().toInt()) {

                           Log.i("Cynfo","Last visited Area Different:"+activeBeacon.getId3().toInt());

                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   areaAdsFragments.get(lastBussinesVisitedID).ChangeAreaDataSet(activeBeacon.getId3().toInt());
                               }
                           });


                           Log.i(TAG,"Las visited Area RENEW:"+lastAreaVisited);
                           lastAreaVisited = activeBeacon.getId3().toInt();

                       }
                    }

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
