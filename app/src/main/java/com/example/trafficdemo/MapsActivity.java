package com.example.trafficdemo;




import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
        public String MyTag = "TrafficApp";
        public int N = 0;
        public int W = 1;
        public int S = 2;
        public int E = 3;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_maps);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);


        }


        public void AskPermissions(Activity thisActivity, String permission, int permissioncode ) {
                // Here, thisActivity is the current activity
                if(ContextCompat.checkSelfPermission(thisActivity,
                        permission)
                        != PackageManager.PERMISSION_GRANTED)

                {
                        // Permission is not granted
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                                permission)) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                        } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(thisActivity,
                                        new String[]{permission},
                                        permissioncode);
                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                        }
                } else

                {
                        // Permission has already been granted
                }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
                switch (requestCode) {
                        case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        Log.d(MyTag, "WritePermissionGranted");
                                        // permission was granted, yay! Do the
                                        // contacts-related task you need to do.
                                } else {
                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                }
                                return;
                        }
                        case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        Log.d(MyTag, "ReadPermissionGranted");
                                        // permission was granted, yay! Do the
                                        // contacts-related task you need to do.
                                } else {
                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                }
                                return;
                        }

                        // other 'case' lines to check for other
                        // permissions this app might request.
                }
        }

        /** Anirudh's code **/

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Hope Farm and move the camera
                LatLng hopefarm = new LatLng(12.9837667, 77.7523578);
                mMap.addMarker(new MarkerOptions().position(hopefarm).title("Marker in Hopefarm"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(hopefarm));
                mMap.setMaxZoomPreference((float) 15.5);
                mMap.setMinZoomPreference((float)15.5);
                mMap.setTrafficEnabled(true);
                AskPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraUpdateFactory.()));
                //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                //getScreenShot(rootView);
                //CaptureMapScreen(mMap);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        public void onMapLoaded() {
                                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                        public void onSnapshotReady(Bitmap bitmap) {
                                                /* Write image to disk */
                                                FileOutputStream out = null;
                                                try {
                                                        String filename = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Mymap.png";
                                                        out = new FileOutputStream(filename);
                                                } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                }
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                                AnalyzeBitMap(bitmap);
                                        }
                                });
                        }
                });
        }

        /******************* Image Processing Code *****/


        public float[] AnalyzeBitMap(Bitmap bitmap) {

                int xStart[] ={808, 618, 262, 255, 437, 606, 971, 966};
                int yStart[] ={180, 149, 338, 492, 683, 750, 604, 408};
                int xEnd[] ={741, 560, 505, 417, 466, 648, 710, 771};
                int yEnd[] ={354, 306, 350, 532, 574, 380, 603, 610};
                float trafficlevel[] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                float green[] = {35.0f, 25.0f, 40.0f, 20.0f};

                int NI = 0;
                int NO = 1;
                int WI = 2;
                int WO = 3;
                int SI = 4;
                int SO = 5;
                int EI = 6;
                int EO = 7;
                int road;

                for (road = 0; road <1; road++)
                        // trafficlevel[road] = AnalyzeRoad(bitmap, xStart[road], xEnd[road], yStart[road], yEnd[road]);
                        trafficlevel[road] = AnalyzeRoad(bitmap, xStart[road], 0, yStart[road],0);
                // Add logic to convert trafficlevel to compute green values
                // More logic needed

                green[N] = (trafficlevel[NI] -
                        ((trafficlevel[SO] + trafficlevel[WO] + trafficlevel[EO])/3));
                return (green);
        }

        public double colourScale(Bitmap bitmap, int xd1, int yd1, int xd2, int yd2){
                int pixeld1 = bitmap.getPixel(xd1,yd1);
                int pixeld2= bitmap.getPixel(xd2,yd2);
                int greenValueni1 = Color.green(pixeld1);
                int greenValueni2 = Color.green(pixeld2);
                float colourScaled = 0.0f;
                colourScaled = (((255-greenValueni1)/255.0f) + ((255-greenValueni2)/255.0f))/2 ;
                Log.d(MyTag, "The color scale generated is:" + colourScaled);
                return colourScaled;
        }

        public float AnalyzeRoad(Bitmap bitmap, int xs, int xe, int ys, int ye) {
                int xni1 = 572;  //638
                int yni1 = 830;  //417
                int xni2 = 756;  //638
                int yni2 = 366;
                //
                int xno1 = 590;  //638
                int yno1 = 716;  //417
                int xno2 = 708;  //638
                int yno2 = 429;
                //
                int xwi1 = 487;  //638
                int ywi1 = 836;  //417
                int xwi2 = 150;  //638
                int ywi2 = 715;
                //
                int xwo1 = 514;  //638
                int ywo1 = 865;  //417
                int xwo2 = 145;  //638
                int ywo2 = 735;
                //
                int xsi1 = 530;  //638
                int ysi1 = 887;  //417
                int xsi2 = 420;  //638
                int ysi2 = 1445;
                //
                int xso1 = 544;  //638
                int yso1 = 909;  //417
                int xso2 = 458;  //638
                int yso2 = 1347;
                //
                int xei1 = 579;  //638
                int yei1 = 877;  //417
                int xei2 = 1076;  //638
                int yei2 = 986;
                //
                int xeo1 = 674;  //638
                int yeo1 = 869;  //417
                int xeo2 = 1034;  //638
                int yeo2 = 971;
                //
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();

                double northinCC = colourScale(bitmap, xni1, yni1, xni2, yni2);
                double northoutCC = colourScale(bitmap, xno1, yno1, xno2, yno2);
                double southinCC = colourScale(bitmap, xsi1, ysi1, xsi2, ysi2);
                double southoutCC = colourScale(bitmap, xso1, yso1, xso2, yso2);
                double westinCC = colourScale(bitmap, xwi1, ywi1, xwi2, ywi2);
                double westoutCC = colourScale(bitmap, xwo1, ywo1, xwo2, ywo2);
                double eastinCC = colourScale(bitmap, xei1, yei1, xei2, yei2);
                double eastoutCC = colourScale(bitmap, xeo1, yeo1, xeo2, yeo2);
                Log.d(MyTag, "northin: " + northinCC + " northout: " + northoutCC + "southin: " + southinCC );
                Log.d(MyTag, "southout: " + southoutCC + "westin: " + westinCC + "westout: " + westoutCC );
                Log.d(MyTag, "eastin: " + eastinCC + "eastout: " + eastoutCC );
                double northvalue = northinCC - ((southoutCC+westoutCC+eastoutCC)/3);
                double southvalue = southinCC - ((northoutCC+westoutCC+eastoutCC)/3);
                double eastvalue = eastinCC - ((southoutCC+westoutCC+northoutCC)/3);
                double westvalue = westinCC - ((southoutCC+northoutCC+eastoutCC)/3);
                Log.d(MyTag, "North is: " + northvalue + " South is: " + southvalue + " East is: " + eastvalue + " West is: " + westvalue);
                double total = northvalue+eastvalue+southvalue+westvalue;
                double southtiming = (southvalue/total)*240;
                double northtiming = (northvalue/total)*240;
                double westtiming = (westvalue/total)*240;
                double easttiming = (eastvalue/total)*240;
                Log.d(MyTag, "south timing is " + southtiming + " north timing is " + northtiming + "west timing is " + westtiming + "east timing is " + easttiming);
                /* Log.d(MyTag, "width = " + width + " height = " + height);
                int pixelni1 = bitmap.getPixel(xni1,yni1);
                int pixelni2= bitmap.getPixel(xni2,yni2);
                int greenValueni1 = Color.green(pixelni1);
                int greenValueni2 = Color.green(pixelni2);
                float colourScaleni = 0.0f;
                colourScaleni = (((255-greenValueni1)/255.0f) + ((255-greenValueni2)/255.0f))/2 ;
                Log.d(MyTag, "The color scale generated is:" + colourScaleni);
                //
                int pixelno1 = bitmap.getPixel(xno1,yno1);
                int pixelno2= bitmap.getPixel(xno2,yno2);
                int greenValueno1 = Color.green(pixelno1);
                int greenValueno2 = Color.green(pixelno2);
                float colourScaleno = 0.0f;
                colourScaleno = (((255-greenValueno1)/255.0f) + ((255-greenValueno2)/255.0f))/2 ;
                Log.d(MyTag, "The color scale generated is:" + colourScaleni);
                //
                int pixelwi1 = bitmap.getPixel(xwi1,ywi1);
                int pixelwi2= bitmap.getPixel(xni2,yni2);
                int greenValuewi1 = Color.green(pixelni1);
                int greenValuewi2 = Color.green(pixelni2);
                float colourScalewi = 0.0f;
                colourScaleni = (((255-greenValueni1)/255.0f) + ((255-greenValueni2)/255.0f))/2 ;
                Log.d(MyTag, "The color scale generated is:" + colourScaleni); */
                return 0.0f;
        }

}





