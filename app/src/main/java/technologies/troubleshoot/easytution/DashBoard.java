package technologies.troubleshoot.easytution;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String USER_EMAIL = "email";
    private static final String USER_IMAGE = "profile_pic";
    private static final String TAG_JOB_FEED_FRAGMENT = "JOB_FEED_FRAGMENT";

    String userType = "";
    private Bitmap bitmap;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("informme", 0);

        //see if the user is logged in shared memory
        //then redirect to dashboard
        if (!(getSharedPreferences("informme", 0).getBoolean("isLoggedIn", false))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {

            userType = preferences.getString(Config.SP_USERTYPE, "");

        }

        //Default fragment to be called on app start.
        getSupportFragmentManager().beginTransaction().add(R.id.content_news_feed, new JobFeedFragment(), TAG_JOB_FEED_FRAGMENT)
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

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.user_type_text_view_id)).setText(preferences.getString(Config.SP_USERTYPE, ""));

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email_text_view_id)).setText(preferences.getString(Config.SP_EMAIL, ""));

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.text_View_User_Name_id)).setText(preferences.getString(Config.SP_NAME, ""));

        if (userType.equals("teacher"))
            navigationView.getMenu().findItem(R.id.nav_new_post_id).setVisible(false);
        else
            navigationView.getMenu().findItem(R.id.nav_new_post_id).setVisible(true);

        navigationView.setNavigationItemSelectedListener(this);


        (navigationView.getHeaderView(0).findViewById(R.id.user_Image_View_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });


    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            JobFeedFragment jobFeedFragment = (JobFeedFragment) getSupportFragmentManager().findFragmentByTag(TAG_JOB_FEED_FRAGMENT);

            if (jobFeedFragment != null && jobFeedFragment.isVisible())
                super.onBackPressed();

            getSupportFragmentManager().beginTransaction().replace(R.id.content_news_feed, new JobFeedFragment(), TAG_JOB_FEED_FRAGMENT)
                    .commit();


        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newsFeed_id) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new JobFeedFragment(), TAG_JOB_FEED_FRAGMENT)
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

            preferences = getSharedPreferences("informme", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.putString(Config.SP_EMAIL, "");
            editor.putString(Config.SP_NAME, "");
            editor.putString(Config.SP_USERTYPE, "");
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_settings_id) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new SettingsFragment())
                    .commit();

        } else if (id == R.id.nav_phone_admin_id) {

            String phone = "01707703703";

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);

        } else if(id == R.id.nav_payment_id){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new PaymentFragment())
                    .commit();

        } else if(id == R.id.nav_about_id){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_news_feed, new AboutFragment())
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && null != data && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_Image_View_id)).setImageBitmap(bitmap);

                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /*try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                ImageView imgView = (ImageView) rootView.findViewById(R.id.id_card_image_view_id);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }*/

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DashBoard.this, "Please wait...", "uploading", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DashBoard.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DashBoard.this);
                String email = preferences.getString(Config.SP_EMAIL, "");

                HashMap<String, String> param = new HashMap<>();
                param.put(USER_IMAGE, image);
                param.put(USER_EMAIL, email);
                String result = rh.sendPostRequest(Config.UPDATE_USER_PROFILE_PIC_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }


}
