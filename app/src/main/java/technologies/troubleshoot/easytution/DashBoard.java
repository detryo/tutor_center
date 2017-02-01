package technologies.troubleshoot.easytution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import static technologies.troubleshoot.easytution.LoginActivity.KEY_EMAIL;


public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserName();
        String email;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_news_feed, new JobFeed())
                .commit();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        email = preferences.getString(KEY_EMAIL, "");

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

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewEmail)).setText(email);

        /*// Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        NewsFeedTabAdapter adapter = new NewsFeedTabAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
*/
    }

    private void getUserName() {

        FetchUserName fetchUserName = new FetchUserName();
        fetchUserName.execute();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board_app_bar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newsFeed_id) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_news_feed, new JobFeed())
                    .commit();
        } else if (id == R.id.nav_category_id) {

        } else if (id == R.id.nav_new_post_id) {

        } else if (id == R.id.nav_settings_id) {

        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onLogout(View view) {
        SharedPreferences sp = getSharedPreferences("informme", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    class FetchUserName extends AsyncTask<Void, Void, Void>{

        public FetchUserName() {
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String email;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            email = preferences.getString(KEY_EMAIL, "");
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
                            Toast.makeText(DashBoard.this, "Stop using this app", Toast.LENGTH_LONG).show();
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //set the username -- that is fetched from database
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserName)).setText(name);

        }

    }


}
