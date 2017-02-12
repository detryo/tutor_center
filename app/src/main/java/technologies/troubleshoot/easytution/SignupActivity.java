package technologies.troubleshoot.easytution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by kaizer on 11/21/16.
 * Database work done by xubi on 12/12/16.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    /*
     * All Variables are declared here.
     */
    private static final String REGISTER_URL = "http://tuition.troubleshoot-tech.com/signup.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_USERGENDER = "gender";
    public static final String KEY_INSTITUTE = "institute";
    public static final String KEY_PHONE = "phone";

    Button nextBtn;
    EditText userEditText, emailEditText, passwordEditText, reEnteredPassEditText, phoneNumber,instituteName;
    RequestQueue requestQueue;
    Spinner gender;
    ArrayAdapter spinnerAdapter;
    String user_gender, user_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        //Attaching all widgets to their corresponding viewID;
        nextBtn = (Button) findViewById(R.id.nextToCategoryBtn_id);

        userEditText = (EditText) findViewById(R.id.Signup_Username_EditText_id);
        emailEditText = (EditText) findViewById(R.id.Signup_Email_EditText_id);
        passwordEditText = (EditText) findViewById(R.id.Signup_Password_EditText_id);
        reEnteredPassEditText = (EditText) findViewById(R.id.Signup_Password_ReEnter_EditText_id);
        phoneNumber = (EditText) findViewById(R.id.Signup_Phone_EditText_id);
        instituteName = (EditText) findViewById(R.id.Signup_Institute_EditText_id);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_type = preferences.getString(KEY_USERTYPE, "");

        gender = (Spinner) findViewById(R.id.Signup_UserType_Spinner_id);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.gender_type, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(spinnerAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user_gender = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        nextBtn.setOnClickListener(this);
    }

    private void registerUser(final String username, final String password, final String email, final String institute, final String phone, final String user_type, final String gender) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("success")) {
                            SharedPreferences sp = getSharedPreferences("informme", 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            openProfile(email);
                        } else {/*
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);*/
                            Toast.makeText(SignupActivity.this, "Invalid email ID already exists", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_INSTITUTE, institute);
                params.put(KEY_PHONE, phone);
                params.put(KEY_USERTYPE, user_type);
                params.put(KEY_USERGENDER, gender);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onClick(View v) {
        if (v == nextBtn) {

            String username = userEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String rePass = reEnteredPassEditText.getText().toString().trim();
            String institute = instituteName.getText().toString().trim();
            String phone = phoneNumber.getText().toString().trim();

            //condition for validation error message;
            if (username.trim().equals("")){
                userEditText.setError("username required!");
            }
            else if(!isValidEmaillId(email.toString().trim())){
                //Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                emailEditText.setError("Invalid Email Address");
            }
            else if(email.trim().equals("")){
                emailEditText.setError("email is missing!");
                //Toast.makeText(SignupActivity.this, "email is missing!", Toast.LENGTH_SHORT).show();
            }
            else if(password.trim().equals("") || password.length()<5){
                passwordEditText.setError("password length should be greater than 4 or can't be blank");
                //Toast.makeText(SignupActivity.this, "password length should be greater than 4 or can't be blank", Toast.LENGTH_SHORT).show();
            }
            else if (rePass.trim().equals("")){
                reEnteredPassEditText.setError("can't be blank");
                //Toast.makeText(SignupActivity.this, "can't be blank", Toast.LENGTH_SHORT).show();
            }
            else if(!password.equals(rePass)){
                reEnteredPassEditText.setError("password doesn't matched");
                //Toast.makeText(SignupActivity.this, "Password Doesn't Match", Toast.LENGTH_LONG).show();
            }
            else
                registerUser(username, password, email, institute, phone, user_type, user_gender);
        }

    }

    //function for email validation;
    private boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void openProfile(String email) {

        Intent intent = new Intent(this, DashBoard.class);
        saveUserEmail(email);
        startActivity(intent);
    }

    //This method saves user Email address on SharedPreference, for later use on other activity.
    private void saveUserEmail(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.SP_EMAIL, email);
        editor.apply();
    }

}