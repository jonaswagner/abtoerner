package hack.abtoerner.abtoerner;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.Review;


public class TextAnalytics extends AsyncTask<String, Void, List<String>> {

// ***********************************************
// *** Update or verify the following values. ***
// **********************************************

    // Replace the accessKey string value with your valid access key.
    static String accessKey = "ee3a7a8d27f14d3daed8dc0d8028e37b";

// Replace or verify the region.

// You must use the same region in your REST API call as you used to obtain your access keys.
// For example, if you obtained your access keys from the westus region, replace
// "westcentralus" in the URI below with "westus".

    // NOTE: Free trial access keys are generated in the westcentralus region, so if you are using
// a free trial access key, you should not need to change this region.
//    static String host = "https://westus.api.cognitive.microsoft.com";
    static String host = "https://westcentralus.api.cognitive.microsoft.com/text/analytic";

//    static String path = "/keyPhrases";
    static String path = "/v2.0/keyPhrases";

    private GooglePlaces placesClient = new GooglePlaces("AIzaSyAwCJUpWwAHBhT7jPSP7X14Cy2c91Fye50");

    WeakReference<Activity> activity;

    public TextAnalytics(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public static String GetKeyPhrases (Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host+path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        String placeId = strings[0];
        Documents documents = new Documents ();

        //TODO jwa fake some warning reviews
        Place place = placesClient.getPlaceById("ChIJWf_iMqigmkcRRJAVYGb7Hwo");
        int i = 0;
        for (Review review : place.getReviews()) {
            documents.add("" +i, review.getLanguage(), review.getText());
            i++;
        }

        List<String> buzzWords = new ArrayList<String>();
        try {
            String response = GetKeyPhrases (documents);
            JSONArray jsonArray = new JSONArray(prettify(response));

            for(int index = 0;index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                buzzWords.add((String) jsonObject.get("keyPhrases"));
            }

        }
        catch (Exception e) {
            System.out.println (e);
        }
        return buzzWords;
    }

    protected void onPostExecute(List<String> buzzWords) {
        Home homeActivity = (Home) activity.get();
        homeActivity.updateWithBuzzWords(buzzWords);
    }
}