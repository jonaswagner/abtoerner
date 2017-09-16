package hack.abtoerner.abtoerner;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import hack.abtoerner.abtoerner.models.Warning;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

public class Warner extends AsyncTask<Location, Void, Warning> {

    // reference to the activity that called the async task
    WeakReference<Activity> activity;

    public static final int placesRadiusInMeters = 500;
    private GooglePlaces placesClient = new GooglePlaces("AIzaSyAwCJUpWwAHBhT7jPSP7X14Cy2c91Fye50");

    public Warner(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    @Override
    protected Warning doInBackground(Location... location) {
        return getWarning(location[0]);
    }

    private Warning getWarning(Location location) {
        if (location == null)
            return new Warning(null, 0);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // get nearby places
        List<Place> places = placesClient.getNearbyPlaces(
                latitude,
                longitude,
                placesRadiusInMeters,
                20);

        // sum all ratings
        double ratingSum = 0;
        int numberOfRatings = 0;
        while (numberOfRatings < places.size()) {
            Place place = places.get(numberOfRatings);
            double rating = place.getRating();

            ratingSum += rating;
            numberOfRatings++;
        }

        // get average rating sentiment
        double sentiment = ratingSum / numberOfRatings;

        Warning warning = new Warning(places, sentiment);
        return warning;
    }

    protected void onPostExecute(Warning warning) {
        Home homeActivity = (Home)activity.get();
        homeActivity.updateWithWarning(warning);
    }

}
