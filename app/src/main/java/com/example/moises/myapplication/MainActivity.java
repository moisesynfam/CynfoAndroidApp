package com.example.moises.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.altbeacon.beacon.MonitorNotifier;
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
    private InitialFragment initialFragment;
    private int REQUEST_ENABLE_BT =89;
    public static NotificationManager mNotificationManager;
    static public Context context;
    //public static ArrayList<Business> BusinessList;
    public static Map<String,Business> BusinessList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        initialFragment = InitialFragment.newInstance("","");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        context = this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        areaAdsFragments = new ArrayList<AreaAdsFragment>();
        //BusinessList =  new ArrayList<>();
        BusinessList = new HashMap<>();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,initialFragment)
                .commit();

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
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            }
        }



        database = Database.getDatabase();

        DatabaseReference myRef = database.getReference("businessTest");


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


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG,"On activity Request"+ requestCode);

        if (requestCode == REQUEST_ENABLE_BT) {

            if(resultCode == RESULT_CANCELED){
                Log.d(TAG,"On activity Result"+ resultCode);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Cynfo necesita el adaptador de bluetooth para funcionar, de lo" +
                        "contrario se cerrará ¿Desea activar el adaptador bluetooth?")
                        .setTitle("Error al encender bluetooth");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter != null) {
                            if (!mBluetoothAdapter.isEnabled()) {
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                            }
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        finishAndRemoveTask();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
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

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,initialFragment)
                        .commit();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {    }


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
