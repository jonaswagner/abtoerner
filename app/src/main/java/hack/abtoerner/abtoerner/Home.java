package hack.abtoerner.abtoerner;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import se.walkercrou.places.Place;

//2.9 rating
//https://www.google.ch/maps/place/Circle+Motor+Lodge/@40.495243,-74.2988127,17z/data=!4m12!1m4!2m3!1srestaurant!5m1!4e1!3m6!1s0x89c3ca3ba6a393f5:0xa613768d37bd16db!8m2!3d40.495243!4d-74.296771!9m1!1b1



public class Home extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String locationProvider = LocationManager.GPS_PROVIDER;

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;

    // Define a listener that responds to location updates
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        TextView editText = (TextView) findViewById(R.id.restaurantName);
        editText.setText("Yolo Swaggins \ud83d\ude01");
        editText.setEnabled(false);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar3);
        ratingBar.setMax(5);

        // check location permissions
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }




        long[] pattern = {0, 1000, 500};

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warnicon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setVibrate(pattern)
                        .setChannel("thisIsAChannelID");

        Intent resultIntent = new Intent(this, Settings.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 003;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("Toggle switch enabled!");
                } else {
                    System.out.println("Toggle switch disabled!");
                }
            }
        });

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new GpsLocationListener(this);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new GpsLocationListener(this);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        // The time it takes for your location listener to receive
        // the first location fix is often too long for users wait.
        // Make use of last known location
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        new Warner(this).execute(lastKnownLocation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(this, Settings.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateWithWarning(List<Place> places) {
        // Check if one place with a bad rating
        // is in proximity. If so then raise an
        // alarm by updating the UI

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        if (lastKnownLocation == null) {
            lastKnownLocation = new Location("null");
            lastKnownLocation.setLongitude(8.539203199999974d);
            lastKnownLocation.setLatitude(47.3781899d);
        }
        int nrOfLocations = places.size();
        double[] distanceArray = new double[nrOfLocations];

        // Calculate distance for all locations
        for(int i = 0;i<nrOfLocations;i++) {
            Location locationPlace = new Location("Place");
            locationPlace.setLatitude(places.get(i).getLatitude());
            locationPlace.setLongitude(places.get(i).getLongitude());
            distanceArray[i] = locationPlace.distanceTo(lastKnownLocation);

        }

        // Threshhold for raining an alarm
        double distanceThreshold = 100;
        double ratingAlarm = 5;

        for(int i = 0;i<nrOfLocations;i++) {
            if((distanceArray[i]<distanceThreshold) && (places.get(i).getRating()<ratingAlarm)){
                // Raise alarm
                // update name and rating in the UI
                String restaurantName = places.get(i).getName();
                TextView restaurantTextView = (TextView) findViewById(R.id.restaurantName);
                restaurantTextView.setText(restaurantName);
                RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar3);
                ratingBar.setRating((float) places.get(i).getRating());
            }

        }

        new TextAnalytics(this).execute(places.get(0).getPlaceId());
    }

    public void updateWithBuzzWords(List<String> buzzWords) {

        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (String element : buzzWords) {
            builder.append("Review " + i + ": \t");
            builder.append(element);
            builder.append(" " + "\n");
            i++;
        }

        String reviews = builder.toString();
        

    }
}
