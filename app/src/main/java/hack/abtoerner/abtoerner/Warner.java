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
import se.walkercrou.places.Review;

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
        if (location == null) {
            //if null set location of zurich mainstation
            location = new Location("null");
            location.setLongitude(8.539203199999974d);
            location.setLatitude(47.3781899d);
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // get nearest open place
        List<Place> places = placesClient.getNearbyPlacesRankedByDistance(
                latitude,
                longitude,
                Param.name("opennow").value(true),Param.name("types").value("restaurant"));

        Place places2 = placesClient.getPlaceById("ChIJWf_iMqigmkcRRJAVYGb7Hwo");
        List<Review> review = places2.getReviews();

        return places;
    }

    protected void onPostExecute(List<Place> places) {
        Home homeActivity = (Home) activity.get();
        homeActivity.updateWithWarning(places);
    }

}
