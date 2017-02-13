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

public class TeacherEducationInfo extends Fragment {


    public static final String USER_EMAIL = "email";

    public static final String SSC_YEAR_OF_PASSING = "last_level_study";
    public static final String SSC_GRUOP = "major_group";
    public static final String SSC_GPA = "cgpa";

    public static final String HSC_YEAR_OF_PASSING = "last_level_study";
    public static final String HSC_GRUOP = "major_group";
    public static final String HSC_GPA = "cgpa";

    public static final String UNDER_GRAD_YEAR_OF_PASSING = "last_level_study";
    public static final String UNDER_GRAD_MAJOR = "major_group";
    public static final String UNDER_GRAD_CGPA = "cgpa";

    public static final String GRAD_YEAR_OF_PASSING = "last_level_study";
    public static final String GRAD_MAJOR= "major_group";
    public static final String GRAD_CGPA = "cgpa";

    public static final String ID_CARD = "id_card";
    public static final String FROM_DATE = "year_from";
    public static final String TO_DATE = "year_to";


    EditText sscYearOfPassingEditText, sscGroupEditText, sscGpaEditText, fromEditText, toEditText, hscYearOfPassingEditText, hscGroupEditText, hscGpaEditText, underGradYearOfPassingEditText, underGradMajorEditText, underGradCgpaEditText, gradYearOfPassingEditText, gradMajorEditText, gradCgpaEditText;

    Button editEducationInfoBtn, saveEducationInfoBtn;

    String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_educational_info_layout, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString(Config.SP_EMAIL, "");

        sscYearOfPassingEditText = (EditText) rootView.findViewById(R.id.ssc_years_of_passing_edit_view_id);
        sscGroupEditText = (EditText) rootView.findViewById(R.id.ssc_major_group_edit_view_id);
        sscGpaEditText = (EditText) rootView.findViewById(R.id.ssc_gpa_edit_view_id);

        hscYearOfPassingEditText = (EditText) rootView.findViewById(R.id.hsc_years_of_passing_edit_view_id);
        hscGroupEditText = (EditText) rootView.findViewById(R.id.hsc_major_group_edit_view_id);
        hscGpaEditText = (EditText) rootView.findViewById(R.id.hsc_gpa_edit_view_id);

        underGradYearOfPassingEditText = (EditText) rootView.findViewById(R.id.under_grad_years_of_passing_edit_view_id);
        underGradMajorEditText = (EditText) rootView.findViewById(R.id.under_grad_major_group_edit_view_id);
        underGradCgpaEditText = (EditText) rootView.findViewById(R.id.under_grad_gpa_edit_view_id);

        gradYearOfPassingEditText = (EditText) rootView.findViewById(R.id.grad_years_of_passing_edit_view_id);
        gradMajorEditText = (EditText) rootView.findViewById(R.id.grad_major_group_edit_view_id);
        gradCgpaEditText = (EditText) rootView.findViewById(R.id.grad_gpa_edit_view_id);

        fromEditText = (EditText) rootView.findViewById(R.id.from_edit_view_id);
        toEditText = (EditText) rootView.findViewById(R.id.to_edit_view_id);

        editEducationInfoBtn = (Button) rootView.findViewById(R.id.edit_education_info_btn_id);
        saveEducationInfoBtn = (Button) rootView.findViewById(R.id.save_education_info_btn_id);

        fetchEducationInfo();
        setEditTextEnableOrDisable(false);

        editEducationInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisable(true);

            }
        });

        saveEducationInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateEducationInfo(email, sscYearOfPassingEditText.getText().toString(), sscGroupEditText.getText().toString(), sscGpaEditText.getText().toString(), hscYearOfPassingEditText.getText().toString(), hscGroupEditText.getText().toString(), hscGpaEditText.getText().toString(), underGradYearOfPassingEditText.getText().toString(), underGradMajorEditText.getText().toString(), underGradCgpaEditText.getText().toString(), gradYearOfPassingEditText.getText().toString(), gradMajorEditText.getText().toString(), gradCgpaEditText.getText().toString(), fromEditText.getText().toString(), toEditText.getText().toString());

                setEditTextEnableOrDisable(false);

            }
        });


        return rootView;

    }

    private void updateEducationInfo(final String email, final String sscYearOfPassing, final String sscGroup, final String sscGpa, final String hscYearOfPassing, final String hscGroup, final String hscGpa, final String underGradYearOfPassing, final String underGradMajor, final String underGradCgpa, final String gradYearOfPassing, final String gradMajor, final String gradCgpa, final String from_year, final String to_year) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_TEACHER_EDUCATION_INFO,
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(USER_EMAIL, email);

                params.put(SSC_YEAR_OF_PASSING, sscYearOfPassing);
                params.put(SSC_GRUOP, sscGroup);
                params.put(SSC_GPA, sscGpa);

                params.put(HSC_YEAR_OF_PASSING, hscYearOfPassing);
                params.put(HSC_GRUOP, hscGroup);
                params.put(HSC_GPA, hscGpa);

                params.put(UNDER_GRAD_YEAR_OF_PASSING, underGradYearOfPassing);
                params.put(UNDER_GRAD_MAJOR, underGradMajor);
                params.put(UNDER_GRAD_CGPA, underGradCgpa);

                params.put(GRAD_YEAR_OF_PASSING, gradYearOfPassing);
                params.put(GRAD_MAJOR, gradMajor);
                params.put(GRAD_CGPA, gradCgpa);

                //params.put(ID_CARD, id_card);
                params.put(FROM_DATE, from_year);
                params.put(TO_DATE, to_year);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void fetchEducationInfo(){

        String url = Config.FETCH_EDUCATION_INFO_URL + email;
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

            sscGroupEditText.setText(json.getString(SSC_GRUOP));
            sscGpaEditText.setText(json.getString(SSC_GPA));
            fromEditText.setText(json.getString(FROM_DATE));
            toEditText.setText(json.getString(TO_DATE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setEditTextEnableOrDisable(boolean state){

        sscYearOfPassingEditText.setEnabled(state);
        sscGroupEditText.setEnabled(state);
        sscGpaEditText.setEnabled(state);

        hscYearOfPassingEditText.setEnabled(state);
        hscGroupEditText.setEnabled(state);
        hscGpaEditText.setEnabled(state);

        underGradYearOfPassingEditText.setEnabled(state);
        underGradMajorEditText.setEnabled(state);
        underGradCgpaEditText.setEnabled(state);

        gradYearOfPassingEditText.setEnabled(state);
        gradMajorEditText.setEnabled(state);
        gradCgpaEditText.setEnabled(state);

        fromEditText.setEnabled(state);
        toEditText.setEnabled(state);

    }

}
