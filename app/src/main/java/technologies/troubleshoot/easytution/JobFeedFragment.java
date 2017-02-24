package technologies.troubleshoot.easytution;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class JobFeedFragment extends Fragment {

    public static String USER_NAME = "username";
    public static String USER_IMAGE = "image";


    public static final String JSON_ARRAY = "job_post";
    public static String STATUS_TIME = "date_time";
    public static String POST_ID = "id";
    public static String ADDITIONAL_INFO = "content";
    public static String DATE_TO_START = "date_to_start";
    public static String ADDRESS = "address";
    public static String SUBJECT = "subject";
    public static String TITLE = "title";
    public static String DAYS_IN_WEEK = "days_in_week";
    public static String PREFERRED_TEACHER_GENDER = "preferred_teacher_gender";
    public static String CLASS = "student_class";
    public static String PREFERRED_MEDIUM = "preferred_medium";
    public static String SALARY = "salary";

    private JobAdapter jobAdapter;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onStart() {
        super.onStart();
        getStatus();

    }

    public JobFeedFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dash_board_app_bar_items, menu);
    }


    private void getStatus() {

        FetchStatus status = new FetchStatus();
        status.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            Intent intent = new Intent(getContext(), DashBoard.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dash_board_job_feed_layout, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_down_to_refresh_job_feed_id);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getStatus();

            }
        });

        ((DashBoard) getActivity()).setActionBarTitle(getResources().getString(R.string.job_feed));

        ArrayList<JobFeedContent> job = new ArrayList<>();

        job.add(new JobFeedContent(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "));

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.divider);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.newsView_newsFeed_RecyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobAdapter = new JobAdapter(getActivity(), job);
        recyclerView.setAdapter(jobAdapter);

        recyclerView.addItemDecoration(new ItemDecorator(dividerDrawable));
        recyclerView.setVisibility(View.GONE);

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

                Uri builtUri = Uri.parse(Config.JOB_FEED_URL).buildUpon()
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

                    if (statusStr != null)
                        return statusValue(statusStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            return null;
        }

        private JobFeedContent[] statusValue(String response) throws JSONException {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);

            //Log.v("result", "value : " + result.length());

            JobFeedContent[] nfc = new JobFeedContent[result.length()];

            for (int i = 0; i < result.length(); i++) {

                JSONObject json = result.getJSONObject(i);

                nfc[i] = new JobFeedContent(json.getString(USER_IMAGE), json.optString(TITLE), json.optString(SALARY), json.optString(PREFERRED_MEDIUM), json.optString(CLASS), json.optString(DAYS_IN_WEEK), json.optString(DATE_TO_START), json.optString(PREFERRED_TEACHER_GENDER), json.optString(SUBJECT), json.optString(ADDRESS), json.optString(ADDITIONAL_INFO), json.optString(POST_ID), json.optString(USER_NAME), json.optString(STATUS_TIME));

            }

            return nfc;

        }

        @Override
        protected void onPostExecute(JobFeedContent[] result) {

            swipeRefreshLayout.setRefreshing(false);
            if (result != null) {
                recyclerView.setVisibility(View.VISIBLE);
                jobAdapter.itemUpdated(result);

            }
        }
    }

}



