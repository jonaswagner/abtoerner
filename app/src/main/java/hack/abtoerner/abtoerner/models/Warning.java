package hack.abtoerner.abtoerner.models;

import java.util.List;

import se.walkercrou.places.Place;

public class Warning {
    private List<Place> places;
    double avgSentiment;

    public Warning(List<Place> places, double avgSentiment) {
        this.places = places;
        this.avgSentiment = avgSentiment;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public double getAvgSentiment() {
        return avgSentiment;
    }

    public void setAvgSentiment(double avgSentiment) {
        this.avgSentiment = avgSentiment;
    }
}
