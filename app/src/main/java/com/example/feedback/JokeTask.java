package com.example.feedback;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JokeTask extends AsyncTask<String, Void, String> {
    private final TextView tv;
    HttpURLConnection urlConnection;
    // TourUzbekistan (2020). Code is partially taken from Android Application from Seminars.
    JokeTask(TextView tv) {
        this.tv = tv;
    }

    @Override
    protected String doInBackground(String... args) {

        String joke = "";
        // Internet Chuck Norris database. Api. http://www.icndb.com/api/
        try {
            String urlString = "https://api.icndb.com/jokes/random";

            //connecting to provided url
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            // reading from api
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            //building string
            StringBuilder builder = new StringBuilder();
            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            // getting json result from api
            JSONObject topLevel = new JSONObject(builder.toString());

            //{ "type": "success", "value": { "id": , "joke": } }
            // taking value
            JSONObject main = topLevel.getJSONObject("value");
            // taking joke
            joke = main.getString("joke");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        // returning joke
        return joke;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //while loading show this text
        tv.setText("Please, wait ...");
    }
    @Override
    protected void onPostExecute(String joke) {
        super.onPostExecute(joke);
        //removing "&quot;" from result"
        tv.setText(joke.replace("&quot;", "\""));
    }
}