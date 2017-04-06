package technologies.troubleshoot.easytution;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {


    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    Button loginBtn, studentSignUpBtn, teacherSignUpBtn, backBtn, sendPassword;
    String email, password;
    EditText idEditText, passwordEditText;
    ProgressBar progressBar;
    TextView forgotPassword, emailResponse;
    TextInputLayout passwordHolder;
    LinearLayout signupBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //Attaching all the widgets to their corresponding view id.
        loginBtn = (Button) findViewById(R.id.loginBtn_id);
        backBtn = (Button) findViewById(R.id.back_id);
        sendPassword = (Button) findViewById(R.id.send_password_id);

        idEditText = (EditText) findViewById(R.id.usernameEditText_id);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText_id);
        studentSignUpBtn = (Button) findViewById(R.id.newAccountBtn_student_id);
        teacherSignUpBtn = (Button) findViewById(R.id.newAccountBtn_teacher_id);

        forgotPassword = (TextView) findViewById(R.id.forgot_password_text_view_id);
        emailResponse = (TextView) findViewById(R.id.email_response_id);

        passwordHolder = (TextInputLayout) findViewById(R.id.password_holder_id);

        progressBar = (ProgressBar) findViewById(R.id.login_progress_view_id);

        signupBtns = (LinearLayout) findViewById(R.id.signupBtn_linear_layout_id);

        progressBar.setVisibility(View.GONE);
        backBtn.setVisibility(View.GONE);
        sendPassword.setVisibility(View.GONE);
        emailResponse.setVisibility(View.GONE);
    }

    private void userLogin() {
        email = idEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {

                            userDetail();

                            SharedPreferences sp = getSharedPreferences("informme", 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            openProfile();
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);

                            finish();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage("Invalid Username or Password");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                        /*builder1.setMessage("No Connection");*/
                        builder1.setMessage("Error : " + error);
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_EMAIL, email);
                map.put(KEY_PASSWORD, password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void openProfile() {

        Intent intent = new Intent(this, DashBoard.class);
        saveUserEmail();
        startActivity(intent);
        android.os.SystemClock.sleep(1000);
    }

    //This method saves user Email address on SharedPreference, for later use on other activity.
    private void saveUserEmail() {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);*/
        SharedPreferences preferences = getSharedPreferences("informme", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.SP_EMAIL, email);
        editor.apply();
    }

    //This method saves user Type (Student/Teacher)on SharedPreference, for later use on other activity/fragment.
    private void saveUserType(String userType) {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);*/
        SharedPreferences preferences = getSharedPreferences("informme", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.SP_USERTYPE, userType);
        editor.apply();

    }

    private void saveUserName(String userName) {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);*/
        SharedPreferences preferences = getSharedPreferences("informme", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.SP_NAME, userName);
        editor.apply();

    }

    public void btnClicked(View view) {

        if (view == studentSignUpBtn) {

            //click listener for (create new Account) Button. clicking this Button makes intent for SignupActivity.
            saveUserType("student");
            Intent newAccountIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(newAccountIntent);

        } else if (view == teacherSignUpBtn) {

            //click listener for (create new Account) Button. clicking this Button makes intent for SignupActivity.
            saveUserType("teacher");
            Intent newAccountIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(newAccountIntent);

        } else if (view == loginBtn) {

            loginBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            userLogin();

        } else if (view == forgotPassword){

            /*forgotPassword.setVisibility(View.GONE);
            passwordHolder.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
            backBtn.setVisibility(View.VISIBLE);
            sendPassword.setVisibility(View.VISIBLE);
            idEditText.setHint("Enter Email");
            signupBtns.setVisibility(View.GONE);*/

            String url = "http://tutorcenter-bd.com/password/reset";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (view == backBtn){

            forgotPassword.setVisibility(View.VISIBLE);
            passwordHolder.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.GONE);
            sendPassword.setVisibility(View.GONE);
            idEditText.setHint("Email");
            signupBtns.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.GONE);
            idEditText.setVisibility(View.VISIBLE);
            emailResponse.setVisibility(View.GONE);

        } else if (view == sendPassword){

            sendPassword.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            recoverUserPassword();

        }

    }

    private void showJSON(String response) throws IOException {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject json = result.getJSONObject(0);
            saveUserType(json.getString(Config.KEY_USERTYPE));
            saveUserName(json.getString(Config.KEY_NAME));
            saveUserEmail();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void userDetail(){

        String url = Config.DATA_URL + email;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    showJSON(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                        builder1.setMessage("No Internet Connection");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void recoverUserPassword() {
        email = idEditText.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RECOVER_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("success")) {

                            idEditText.setVisibility(View.GONE);
                            emailResponse.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        } else {

                            progressBar.setVisibility(View.GONE);
                            sendPassword.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage("Invalid email ID");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        sendPassword.setVisibility(View.VISIBLE);

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                        builder1.setMessage("No Internet Connection");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                /*params.put(KEY_EMAIL, idEditText.getText().toString().trim());*/
                params.put(KEY_EMAIL, email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
