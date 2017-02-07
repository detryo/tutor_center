package technologies.troubleshoot.easytution;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaizer on 2/6/17.
 */

public class TeacherPersonalInfo extends Fragment {

    private static final String REGISTER_URL = "http://tuition.troubleshoot-tech.com/signup.php";
    public static final String ADDITIONAL_NUMBER = "last_level_study";
    public static final String DETAIL_ADDRESS = "major_group";
    public static final String LINKED_ID = "id_card";
    public static final String FATHER_NAME = "cgpa";
    public static final String MOTHER_NAME = "years_of_passing";
    public static final String FATHER_NUMBER = "curriculam";
    public static final String MOTHER_NUMBER = "year_form";
    public static final String REF_PERSON_NAME = "year_to";
    public static final String CONTACT_NUMBER = "year_to";
    public static final String RELATION = "year_to";

    EditText additionalNumberEditText, detailAddressEditText, nidNoEditText, fbIdEditText, linkedId, fatherNameEditText, motherNameEditText, fatherNumberEditText, motherNumberEditText, refPersonNameEditText, contactNumberEditText, relationEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_personal_info_layout, container, false);

        additionalNumberEditText = (EditText) rootView.findViewById(R.id.additional_number_edit_view_id);
        detailAddressEditText = (EditText) rootView.findViewById(R.id.detail_address_edit_view_id);
        nidNoEditText = (EditText) rootView.findViewById(R.id.nid_edit_view_id);
        fbIdEditText = (EditText) rootView.findViewById(R.id.fb_edit_view_id);
        linkedId = (EditText) rootView.findViewById(R.id.linked_edit_view_id);
        fatherNameEditText = (EditText) rootView.findViewById(R.id.father_name_edit_view_id);
        motherNameEditText = (EditText) rootView.findViewById(R.id.mother_name_edit_view_id);
        fatherNumberEditText = (EditText) rootView.findViewById(R.id.father_number_edit_view_id);
        motherNumberEditText = (EditText) rootView.findViewById(R.id.mother_number_edit_view_id);
        refPersonNameEditText = (EditText) rootView.findViewById(R.id.ref_person_name_edit_view_id);
        contactNumberEditText = (EditText) rootView.findViewById(R.id.contact_number_edit_view_id);
        relationEditText = (EditText) rootView.findViewById(R.id.relation_edit_view_id);

        return rootView;

    }

    private void updateEducationInfo(final String last_level_study, final String major_group, final String cgpa, final String year_of_passing, final String curriculum, final String from_year, final String to_year, final String id_card) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ADDITIONAL_NUMBER, last_level_study);
                params.put(DETAIL_ADDRESS, major_group);
                params.put(LINKED_ID, id_card);
                params.put(FATHER_NAME, cgpa);
                params.put(MOTHER_NAME, year_of_passing);
                params.put(FATHER_NUMBER, curriculum);
                params.put(MOTHER_NUMBER, from_year);
                params.put(REF_PERSON_NAME, to_year);
                params.put(CONTACT_NUMBER, to_year);
                params.put(RELATION, to_year);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
