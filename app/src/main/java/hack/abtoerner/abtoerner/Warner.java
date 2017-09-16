package hack.abtoerner.abtoerner;

import android.location.Location;
import android.os.AsyncTask;

import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

public class Warner extends AsyncTask<Location, Void, Double> {

    public static final int placesRadiusInMeters = 500;
    private GooglePlaces placesClient = new GooglePlaces("AIzaSyAwCJUpWwAHBhT7jPSP7X14Cy2c91Fye50");

    @Override
    protected Double doInBackground(Location... location) {

        return getAvgRatingSentiment(location[0]);
    }

    private double getAvgRatingSentiment(Location location) {
        if (location == null)
            return 0;

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

        return sentiment;
    }

}
