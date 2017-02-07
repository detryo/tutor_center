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

public class TeacherEducationInfo extends Fragment {

    private static final String REGISTER_URL = "http://tuition.troubleshoot-tech.com/signup.php";
    public static final String LAST_LEVEL_OF_STUDY = "last_level_study";
    public static final String MAJOR = "major_group";
    public static final String ID_CARD = "id_card";
    public static final String CGPA = "cgpa";
    public static final String YEAR_OF_PASSING = "years_of_passing";
    public static final String CURRICULUM = "curriculam";
    public static final String FORM_DATE = "year_form";
    public static final String TO_DATE = "year_to";


    EditText lastLevelOfStudyEditText, majorEditText, cgpaEditText, yearOfPassingEditText, curriculumEditText, fromEditText, toEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_educational_info_layout, container, false);

        lastLevelOfStudyEditText = (EditText) rootView.findViewById(R.id.last_level_of_study_edit_view_id);
        majorEditText = (EditText) rootView.findViewById(R.id.major_group_edit_view_id);
        cgpaEditText = (EditText) rootView.findViewById(R.id.cgpa_edit_view_id);
        yearOfPassingEditText = (EditText) rootView.findViewById(R.id.years_of_passing_edit_view_id);
        curriculumEditText = (EditText) rootView.findViewById(R.id.curriculum_edit_view_id);
        fromEditText = (EditText) rootView.findViewById(R.id.from_edit_view_id);
        toEditText = (EditText) rootView.findViewById(R.id.to_edit_view_id);

        updateEducationInfo(lastLevelOfStudyEditText.getText().toString(), majorEditText.getText().toString(), cgpaEditText.getText().toString(), yearOfPassingEditText.getText().toString(), curriculumEditText.getText().toString(), fromEditText.getText().toString(), toEditText.getText().toString());

        return rootView;

    }

    private void updateEducationInfo(final String last_level_study, final String major_group, final String cgpa, final String year_of_passing, final String curriculum, final String from_year, final String to_year) {

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
                params.put(LAST_LEVEL_OF_STUDY, last_level_study);
                params.put(MAJOR, major_group);
                //params.put(ID_CARD, id_card);
                params.put(CGPA, cgpa);
                params.put(YEAR_OF_PASSING, year_of_passing);
                params.put(CURRICULUM, curriculum);
                params.put(FORM_DATE, from_year);
                params.put(TO_DATE, to_year);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
