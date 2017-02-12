package technologies.troubleshoot.easytution;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaizer on 2/12/17.
 */

public class SettingsFragment extends Fragment {

    private static final String UPDATE_URL = "http://tuition.troubleshoot-tech.com/settingsinfo.php";
    public static final String USER_EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";


    EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText, phoneNumberEditText;

    Button editPasswordBtn, savePasswordBtn, editPhoneNumberBtn, savePhoneNumberBtn;

    String email, currentPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_layout, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString(Config.SP_EMAIL, "");

        currentPasswordEditText = (EditText) rootView.findViewById(R.id.previous_password_id);
        newPasswordEditText = (EditText) rootView.findViewById(R.id.new_passowrd_id);
        confirmPasswordEditText = (EditText) rootView.findViewById(R.id.confirm_passwordid);
        phoneNumberEditText = (EditText) rootView.findViewById(R.id.user_phone_number_edit_text_id);

        editPasswordBtn = (Button) rootView.findViewById(R.id.password_edit_btn_id);
        savePasswordBtn = (Button) rootView.findViewById(R.id.password_save_btn_id);
        editPhoneNumberBtn = (Button) rootView.findViewById(R.id.phone_number_edit_btn_id);
        savePhoneNumberBtn = (Button) rootView.findViewById(R.id.phone_number_save_btn_id);

        fetchSettingsInfo();
        setEditTextEnableOrDisableForPassword(false);
        setEditTextEnableOrDisableForPhoneNumber(false);

        editPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisableForPassword(true);

            }
        });

        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPassword.equals(currentPasswordEditText.getText().toString()) && newPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()) && newPasswordEditText.getText().toString().length() >= 6 && confirmPasswordEditText.getText().toString().length() >= 6) {

                    updateSettingsInfo(email, newPasswordEditText.getText().toString(), phoneNumberEditText.getText().toString());

                } else {

                    currentPasswordEditText.setError("Password miss match/min 6 length");
                    newPasswordEditText.setError("Password miss match/min 6 length");
                    confirmPasswordEditText.setError("Password miss match/min 6 length");

                }

                setEditTextEnableOrDisableForPassword(false);

            }
        });

        editPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisableForPhoneNumber(true);

            }
        });

        savePhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSettingsInfo(email, currentPasswordEditText.getText().toString(), phoneNumberEditText.getText().toString());
                setEditTextEnableOrDisableForPhoneNumber(false);

            }
        });



        return rootView;

    }

    private void updateSettingsInfo(final String email, final String password, final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(USER_EMAIL, email);
                params.put(PASSWORD, password);
                params.put(PHONE, phone);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void fetchSettingsInfo(){

        String url = Config.FETCH_SETTINGS_INFO_URL + email;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(), "Connection error", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void showJSON(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject json = result.getJSONObject(0);

            currentPassword = json.getString(PASSWORD);
            phoneNumberEditText.setText(json.getString(PHONE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setEditTextEnableOrDisableForPassword(boolean state){

        currentPasswordEditText.setEnabled(state);
        newPasswordEditText.setEnabled(state);
        confirmPasswordEditText.setEnabled(state);

    }

    private void setEditTextEnableOrDisableForPhoneNumber(boolean state){

        confirmPasswordEditText.setEnabled(state);
        phoneNumberEditText.setEnabled(state);

    }

}
