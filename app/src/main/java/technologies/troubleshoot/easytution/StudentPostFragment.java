package technologies.troubleshoot.easytution;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static technologies.troubleshoot.easytution.LoginActivity.KEY_EMAIL;

/**
 * Created by kaizer on 2/2/17.
 */

public class StudentPostFragment extends Fragment {

    public static final String DATA_URL = "http://tuition.troubleshoot-tech.com/post.php";
    /*public static final String JSON_ARRAY = "job_post";*/
    public static String DATE_TO_START = "date_to_start";
    public static String ADDITIONAL_INFO = "content";

    public static String TITLE = "title";
    public static String DAYS_IN_WEEK = "days_in_week";
    public static String PREFERRED_TEACHER_GENDER = "preferred_teacher_gender";
    public static String CLASS = "student_class";
    public static String PREFERRED_MEDIUM = "preferred_medium";
    public static String SALARY = "salary";

    CalendarView calendar;
    Spinner categorySpinner, classSpinner;
    ArrayList categoryList, classList;
    ArrayAdapter<String> categoryAdapter, classAdapter;

    public StudentPostFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.student_post_layout, container, false);

        calendar = (CalendarView) rootView.findViewById(R.id.calendar_id);
        calendar.setVisibility(View.GONE);
        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category_id);
        classSpinner = (Spinner) rootView.findViewById(R.id.spinner_class_id);

        /*postStatus("kaizer@gmail.com", "content", "1st Job Post", "1", "male", "A-level", "English", "5000", "2017-02-02");*/

        final TextView calenderText = (TextView) rootView.findViewById(R.id.calender_textView_id);

        calenderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calenderText.setVisibility(View.GONE);
                calendar.setVisibility(View.VISIBLE);

            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                month++;
                calenderText.setText("" + year + "-" + month + "-" + dayOfMonth);
                calenderText.setVisibility(View.VISIBLE);
                calendar.setVisibility(View.GONE);

            }
        });

        categoryList = new ArrayList();
        classList = new ArrayList();
        
        categoryList = getData("category.json");
        categoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long l) {

                if (position == 0) {
                    classList = getClassData("bmediumclass.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 1) {
                    classList = getClassData("emediumclass.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 2) {
                    classList = getClassData("bmediumclass.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 3) {
                    classList = getClassData("madrasa.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 4) {
                    classList = getClassData("uniadmission.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 5) {
                    classList = getClassData("it.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 6) {
                    classList = getClassData("testprep.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 7) {
                    classList = getClassData("languagelearning.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }
                else if (position == 8) {
                    classList = getClassData("project_assignment.json");
                    classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classList);
                    classSpinner.setAdapter(classAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    private void postStatus(final String email, final String content, final String title, final String days_in_week, final String preferred_teacher_gender, final String student_class, final String preferred_medium, final String salary, final String date_to_start) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Status Updated : Thug LiFe", Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Unable to Post", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(ADDITIONAL_INFO, content);
                params.put(TITLE, title);
                params.put(DAYS_IN_WEEK, days_in_week);
                params.put(PREFERRED_TEACHER_GENDER, preferred_teacher_gender);
                params.put(CLASS, student_class);
                params.put(PREFERRED_MEDIUM, preferred_medium);
                params.put(SALARY, salary);
                params.put(DATE_TO_START, date_to_start);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private ArrayList<String> getData(String fileName){
        JSONArray jsonArray=null;
        ArrayList<String> cList=new ArrayList<String>();
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray=new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    cList.add(jsonArray.getJSONObject(i).getString("medium"));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return cList;
    }

    private ArrayList<String> getClassData(String fileName){
        JSONArray jsonArray=null;
        ArrayList<String> cList=new ArrayList<String>();
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray=new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    cList.add(jsonArray.getJSONObject(i).getString("cname"));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return cList;
    }

}
