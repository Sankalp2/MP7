package edu.illinois.cs.cs125.lab12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /** number of possible plants*/
    private final int randNum = 50;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);
        startAPICall();
        setContentView(R.layout.activity_main);
        Button getImage = (Button) findViewById(R.id.getImage);
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startAPICall();
            }
        });

    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Update the text
     * @param response JSON from API
     * @throws JSONException in case things don't work out
     */
    void updateText(final JSONObject response) throws JSONException {
        String name = response.getString("name").toString();
        TextView text = (TextView) findViewById(R.id.TextBox);
        String output = "Name: " + name + "\nRotation Period: " + response.getString("rotation_period").toString()
                + "\nOrbital Period: " + response.getString("orbital_period").toString() + "\nDiameter: " +
                response.getString("diameter").toString() + "\nClimate: " + response.getString("climate").toString() +
                "\nGravity: " + response.getString("gravity").toString() + "\nPopulation: " +
                response.getString("population").toString() + "\n\n";

                text.setText(output);
    }

    /**
     * Make a call to the API.
     */
    void startAPICall() {

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://swapi.co/api/planets/" + (int) (Math.random() * randNum) + "/",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                updateText(response);
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
