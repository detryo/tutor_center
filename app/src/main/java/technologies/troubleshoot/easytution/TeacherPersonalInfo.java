package technologies.troubleshoot.easytution;

import android.app.Dialog;
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
import android.widget.TextView;

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
 * Created by kaizer on 2/6/17.
 */

public class TeacherPersonalInfo extends Fragment {




    public static final String ADDITIONAL_NUMBER = "additional_number";
    public static final String USER_EMAIL = "email";
    public static final String DETAIL_ADDRESS = "detail_address";
    public static final String N_ID = "nid_passport_no";
    public static final String FB_ID = "fb_id";
    public static final String LINKED_ID = "linkedin_id";
    public static final String FATHER_NAME = "father_name";
    public static final String MOTHER_NAME = "mother_name";
    public static final String FATHER_NUMBER = "father_number";
    public static final String MOTHER_NUMBER = "mother_number";
    public static final String REF_PERSON_NAME = "ref_person_name";
    public static final String CONTACT_NUMBER = "ref_person_number";
    public static final String RELATION = "ref_person_relation";

    EditText additionalNumberEditText, detailAddressEditText, nidNoEditText, fbIdEditText, linkedIdEditText, fatherNameEditText, motherNameEditText, fatherNumberEditText, motherNumberEditText, refPersonNameEditText, contactNumberEditText, relationEditText;

    Button editPersonalInfoBtn, savePersonalInfoBtn;

    String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_personal_info_layout, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString(Config.SP_EMAIL, "");

        additionalNumberEditText = (EditText) rootView.findViewById(R.id.additional_number_edit_view_id);
        detailAddressEditText = (EditText) rootView.findViewById(R.id.detail_address_edit_view_id);
        nidNoEditText = (EditText) rootView.findViewById(R.id.nid_edit_view_id);
        fbIdEditText = (EditText) rootView.findViewById(R.id.fb_edit_view_id);
        linkedIdEditText = (EditText) rootView.findViewById(R.id.linked_edit_view_id);
        fatherNameEditText = (EditText) rootView.findViewById(R.id.father_name_edit_view_id);
        motherNameEditText = (EditText) rootView.findViewById(R.id.mother_name_edit_view_id);
        fatherNumberEditText = (EditText) rootView.findViewById(R.id.father_number_edit_view_id);
        motherNumberEditText = (EditText) rootView.findViewById(R.id.mother_number_edit_view_id);
        refPersonNameEditText = (EditText) rootView.findViewById(R.id.ref_person_name_edit_view_id);
        contactNumberEditText = (EditText) rootView.findViewById(R.id.contact_number_edit_view_id);
        relationEditText = (EditText) rootView.findViewById(R.id.relation_edit_view_id);

        editPersonalInfoBtn = (Button) rootView.findViewById(R.id.edit_personal_info_btn_id);
        savePersonalInfoBtn = (Button) rootView.findViewById(R.id.save_personal_info_btn_id);

        savePersonalInfoBtn.setVisibility(View.GONE);

        fetchPersonalInfo();
        setEditTextEnableOrDisable(false);

        editPersonalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisable(true);
                editPersonalInfoBtn.setVisibility(View.GONE);
                savePersonalInfoBtn.setVisibility(View.VISIBLE);

            }
        });

        savePersonalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePersonalInfo(email, additionalNumberEditText.getText().toString(), detailAddressEditText.getText().toString(), nidNoEditText.getText().toString(), fbIdEditText.getText().toString(), linkedIdEditText.getText().toString(), fatherNameEditText.getText().toString(), motherNameEditText.getText().toString(), fatherNumberEditText.getText().toString(), motherNumberEditText.getText().toString(), refPersonNameEditText.getText().toString(), contactNumberEditText.getText().toString(), relationEditText.getText().toString());

                setEditTextEnableOrDisable(false);
                editPersonalInfoBtn.setVisibility(View.VISIBLE);
                savePersonalInfoBtn.setVisibility(View.GONE);

            }
        });

        return rootView;

    }

    private void setEditTextEnableOrDisable(boolean state){

        additionalNumberEditText.setEnabled(state);
        detailAddressEditText.setEnabled(state);
        nidNoEditText.setEnabled(state);
        fbIdEditText.setEnabled(state);
        linkedIdEditText.setEnabled(state);
        fatherNameEditText.setEnabled(state);
        motherNameEditText.setEnabled(state);
        fatherNumberEditText.setEnabled(state);
        motherNumberEditText.setEnabled(state);
        refPersonNameEditText.setEnabled(state);
        contactNumberEditText.setEnabled(state);
        relationEditText.setEnabled(state);

    }

    private void updatePersonalInfo(final String email, final String additional_number, final String detail_address, final String nid_no, final String fb_id, final String linkedin_id, final String father_name, final String mother_name, final String father_number, final String mother_number, final String ref_person_name, final String ref_person_number, final String ref_person_relation) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_PERSONAL_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.custom_alert_dialog_layout_updated);

                        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_updated_btn_id);
                        // if button is clicked, close the custom_alert_dialog_layout_news_feed dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

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
                params.put(ADDITIONAL_NUMBER, additional_number);
                params.put(DETAIL_ADDRESS, detail_address);
                params.put(N_ID, nid_no);
                params.put(FB_ID, fb_id);
                params.put(LINKED_ID, linkedin_id);
                params.put(FATHER_NAME, father_name);
                params.put(MOTHER_NAME, mother_name);
                params.put(FATHER_NUMBER, father_number);
                params.put(MOTHER_NUMBER, mother_number);
                params.put(REF_PERSON_NAME, ref_person_name);
                params.put(CONTACT_NUMBER, ref_person_number);
                params.put(RELATION, ref_person_relation);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void fetchPersonalInfo(){

        String url = Config.FETCH_PERSONAL_INFO_URL + email;
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

            additionalNumberEditText.setText(json.getString(ADDITIONAL_NUMBER));
            detailAddressEditText.setText(json.getString(DETAIL_ADDRESS));
            nidNoEditText.setText(json.getString(N_ID));
            fbIdEditText.setText(json.getString(FB_ID));
            linkedIdEditText.setText(json.getString(LINKED_ID));
            fatherNameEditText.setText(json.getString(FATHER_NAME));
            motherNameEditText.setText(json.getString(MOTHER_NAME));
            fatherNumberEditText.setText(json.getString(FATHER_NUMBER));
            motherNumberEditText.setText(json.getString(FATHER_NUMBER));
            refPersonNameEditText.setText(json.getString(REF_PERSON_NAME));
            contactNumberEditText.setText(json.getString(CONTACT_NUMBER));
            relationEditText.setText(json.getString(RELATION));

            /*Log.v("Result", " " + additionalNumber);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
