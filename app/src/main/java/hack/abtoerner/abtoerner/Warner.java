package hack.abtoerner.abtoerner;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;

public class Warner extends AsyncTask<Location, Void, List<Place>> {

    // reference to the activity that called the async task
    WeakReference<Activity> activity;

    public static final int placesRadiusInMeters = 500;
    private GooglePlaces placesClient = new GooglePlaces("AIzaSyAwCJUpWwAHBhT7jPSP7X14Cy2c91Fye50");

    public Warner(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    @Override
    protected List<Place> doInBackground(Location... location) {
        return getWarning(location[0]);
    }

    private List<Place> getWarning(Location location) {
        // on first startup, the last known location might be null
        // thus we just wait until we get a real location
        if (location == null)
            return new ArrayList<Place>();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // get nearest open place
        List<Place> places = placesClient.getNearbyPlacesRankedByDistance(
                latitude,
                longitude,
                Param.name("opennow").value(true));

        return places;
    }

    protected void onPostExecute(List<Place> places) {
        Home homeActivity = (Home) activity.get();
        homeActivity.updateWithWarning(places);
    }

}
