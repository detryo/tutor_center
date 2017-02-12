package technologies.troubleshoot.easytution;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String userType;

    @Override
    protected void onStart() {
        super.onStart();

        //see if the user is logged in shared memory
        //then redirect to dashboard
        if (!(getSharedPreferences("informme", 0).getBoolean("isLoggedIn", false))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userType = preferences.getString(Config.SP_USERTYPE, "");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserDetail();

        //Default fragment to be called on app start.
        getSupportFragmentManager().beginTransaction().add(R.id.content_news_feed, new JobFeedFragment())
                .commit();

        setContentView(R.layout.activity_nav_drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void getUserDetail() {

        FetchUserDetail fetchUserDetail = new FetchUserDetail();
        fetchUserDetail.execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newsFeed_id) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new JobFeedFragment())
                    .commit();
        } else if (id == R.id.nav_new_post_id) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new StudentPostFragment())
                    .commit();

        } else if (id == R.id.nav_profile_id) {

                if (userType.equals("student")) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_news_feed, new StudentProfileInfoFragment())
                            .commit();

                } else if (userType.equals("teacher")) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_news_feed, new TeacherProfileFragment())
                            .commit();

                }


        } else if (id == R.id.nav_log_out_id) {

            SharedPreferences sp = getSharedPreferences("informme", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_settings_id) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new SettingsFragment())
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class FetchUserDetail extends AsyncTask<Void, Void, Void>{

        public FetchUserDetail() {
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String email;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            email = preferences.getString(Config.SP_EMAIL, "");

            String url = Config.DATA_URL+email;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showJSON(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DashBoard.this, "No Internet Connection", Toast.LENGTH_LONG).show();


                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            return null;
        }

        private void showJSON(String response){
            String name="";

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                JSONObject json = result.getJSONObject(0);
                name = json.getString(Config.KEY_NAME);
                userType = json.getString(Config.KEY_USERTYPE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //set the username -- that is fetched from database
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserName)).setText(name);

            if (userType.equals("teacher"))
                navigationView.getMenu().findItem(R.id.nav_new_post_id).setVisible(false);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DashBoard.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Config.SP_USERTYPE, userType);
            editor.apply();

        }

    }

}
