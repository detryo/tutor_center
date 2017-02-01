package technologies.troubleshoot.easytution;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JobFeed extends Fragment {

    public static final String DATA_URL = "http://tuition.troubleshoot-tech.com/newsfeed.php";
    public static final String JSON_ARRAY = "users";
    public static String USER_NAME = "username";
    public static String STATUS_TIME = "date_time";
    public static String STATUS = "content";
    public static String IMAGE = "name";

    private JobAdapter jobAdapter;

    @Override
    public void onStart() {
        super.onStart();
        //getStatus();
    }

    public JobFeed() {

    }

    private void getStatus() {

        FetchStatus status = new FetchStatus();
        status.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //TextView textView;
        View rootView = inflater.inflate(R.layout.dash_board_job_feed_layout, container, false);

        ArrayList<JobFeedContent> job = new ArrayList<>();

        job.add(new JobFeedContent(R.mipmap.ic_launcher, "GED CENTER", 5000, "This is to inform you all that" +
                " please insert all necessary information for verification from your profile section. :-)"));

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.newsView_newsFeed_RecyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobAdapter = new JobAdapter(getActivity(), job);
        recyclerView.setAdapter(jobAdapter);

        return rootView;
    }


    class FetchStatus extends AsyncTask<String, Void, JobFeedContent[]> {


        public FetchStatus() {
            super();
        }

        @Override
        protected JobFeedContent[] doInBackground(String... params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String statusStr = null;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                Uri builtUri = Uri.parse(DATA_URL).buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                statusStr = buffer.toString();

            } catch (IOException e) {
                Log.e("ForecastFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }

                try {
                    return statusValue(statusStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            return null;
        }

        private JobFeedContent[] statusValue(String response) throws JSONException {

//            Log.v("Response", "value : " + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);

//            Log.v("result", "value : " + result.length());

            JobFeedContent[] nfc = new JobFeedContent[result.length()];

            for (int i = 0; i < result.length(); i++) {

                JSONObject json = result.getJSONObject(i);

                nfc[i] = new JobFeedContent(R.mipmap.ic_launcher, json.optString(USER_NAME), json.optInt(STATUS_TIME), json.optString(STATUS));

            }

            return nfc;

        }

        @Override
        protected void onPostExecute(JobFeedContent[] result) {

            /*if (result != null) {
                for(int i = 0; i < result.length; i++)
                jobAdapter.add(result[i]);

            }*/
        }
    }


}



