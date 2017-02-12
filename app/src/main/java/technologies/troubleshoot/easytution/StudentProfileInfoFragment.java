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
 * Created by kaizer on 2/9/17.
 */

public class StudentProfileInfoFragment extends Fragment{

    private static final String SAVE_STUDENT_INFO_URL = "http://tuition.troubleshoot-tech.com/studentInfo.php";

    public static final String USER_EMAIL = "email";
    public static final String REF_PERSON_NAME = "ref_person_name";
    public static final String CONTACT_NUMBER = "ref_phone_no";
    public static final String RELATION = "ref_person_relation";

    EditText refPersonNameEditText, contactNumberEditText, relationEditText;

    Button editStudentInfoBtn, saveStudentInfoBtn;

    String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.student_profile_info, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString(Config.SP_EMAIL, "");

        refPersonNameEditText = (EditText) rootView.findViewById(R.id.ref_person_name_edit_view_student_id);
        contactNumberEditText = (EditText) rootView.findViewById(R.id.contact_number_edit_view_student_id);
        relationEditText = (EditText) rootView.findViewById(R.id.relation_edit_view_student_id);

        editStudentInfoBtn = (Button) rootView.findViewById(R.id.edit_student_info_btn_id);
        saveStudentInfoBtn = (Button) rootView.findViewById(R.id.save_student_info_btn_id);

        fetchStudentInfo();
        setEditTextEnableOrDisable(false);

        editStudentInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisable(true);

            }
        });

        saveStudentInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePersonalInfo(email, refPersonNameEditText.getText().toString(), contactNumberEditText.getText().toString(), relationEditText.getText().toString());

                setEditTextEnableOrDisable(false);

            }
        });

        return rootView;

    }

    private void setEditTextEnableOrDisable(boolean state){

        refPersonNameEditText.setEnabled(state);
        contactNumberEditText.setEnabled(state);
        relationEditText.setEnabled(state);

    }

    private void updatePersonalInfo(final String email, final String ref_person_name, final String ref_person_number, final String ref_person_relation) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SAVE_STUDENT_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Successfully Updated");
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Update Failed");
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
                Map<String, String> params = new HashMap<String, String>();
                params.put(USER_EMAIL, email);
                params.put(REF_PERSON_NAME, ref_person_name);
                params.put(CONTACT_NUMBER, ref_person_number);
                params.put(RELATION, ref_person_relation);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void fetchStudentInfo(){

        String url = Config.FETCH_STUDENT_INFO_URL + email;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Connection error");
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

            refPersonNameEditText.setText(json.getString(REF_PERSON_NAME));
            contactNumberEditText.setText(json.getString(CONTACT_NUMBER));
            relationEditText.setText(json.getString(RELATION));

            /*Log.v("Result", " " + additionalNumber);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set the username -- that is fetched from database
        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserName)).setText(name);*/

    }

}
