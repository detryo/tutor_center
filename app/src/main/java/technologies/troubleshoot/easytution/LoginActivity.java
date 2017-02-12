package technologies.troubleshoot.easytution;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL = "http://tuition.troubleshoot-tech.com/login.php";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    Button loginBtn, studentSignUpBtn, teacherSignUpBtn;
    String email, password;
    EditText idEditText, passwordEditText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*//see if the user is logged in shared memory
        //then redirect to dashboard
        if (getSharedPreferences("informme", 0).getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(this, DashBoard.class);
            startActivity(intent);
        }*/

        //setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login);

        //Attaching all the widgets to their corresponding view id.
        loginBtn = (Button) findViewById(R.id.loginBtn_id);
        idEditText = (EditText) findViewById(R.id.usernameEditText_id);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText_id);
        studentSignUpBtn = (Button) findViewById(R.id.newAccountBtn_as_student_id);
        teacherSignUpBtn = (Button) findViewById(R.id.newAccountBtn_as_teacher_id);

        progressBar = (ProgressBar) findViewById(R.id.login_progress_view_id);

        progressBar.setVisibility(View.GONE);

    }

    private void userLogin() {
        email = idEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            SharedPreferences sp = getSharedPreferences("informme", 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);

                            openProfile();
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
                        builder1.setMessage("No Connection");
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
    }

    //This method saves user Email address on SharedPreference, for later use on other activity.
    private void saveUserEmail() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.SP_EMAIL, email);
        editor.apply();
    }

    //This method saves user Type (Student/Teacher)on SharedPreference, for later use on other activity/fragment.
    private void saveUserType(String userType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.KEY_USERTYPE, userType);
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

        }

    }

}
