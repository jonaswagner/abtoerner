package hack.abtoerner.abtoerner;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsLocationListener implements LocationListener {

    private Activity activity;

    public GpsLocationListener(Activity activity) {
        this.activity = activity;
    }

    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        new Warner(activity).execute(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }
}
