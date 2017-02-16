package technologies.troubleshoot.easytution;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Element;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaizer on 2/2/17.
 */

public class StudentPostFragment extends Fragment {

    /*public static final String JSON_ARRAY = "job_post";*/
    public static String DATE_TO_START = "date_to_start";
    public static String ADDITIONAL_INFO = "content";
    public static String KEY_EMAIL = "email";

    public static String TITLE = "title";
    public static String DAYS_IN_WEEK = "days_in_week";
    public static String PREFERRED_TEACHER_GENDER = "preferred_teacher_gender";
    public static String CLASS = "student_class";
    public static String PREFERRED_MEDIUM = "preferred_medium";
    public static String SUBJECT = "subject";
    public static String ADDRESS = "address";
    public static String SALARY = "salary";

    CalendarView calendar;
    Spinner categorySpinner, classSpinner, tutorGenderSpinner, numOfDaysSpinner;
    ArrayList categoryListSpinner, classListSpinner;
    ArrayAdapter<String> categoryAdapter, classAdapter, numOfDaysAdapter, tutorGenderAdapter;
    String title, numOfDays, tutorGender, category, courses, subjects, dateToStart, salary, additionalInfo, address;
    EditText titleEditText, subjectsEditText, salaryEditText, additionalInfoEditText, addressEditText;
    LinearLayout calenderView;
    TextView calenderText;
    Button postBtn;
    ProgressBar progressBar;

    public StudentPostFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.student_post_layout, container, false);

        titleEditText = (EditText) rootView.findViewById(R.id.post_title_id);
        subjectsEditText = (EditText) rootView.findViewById(R.id.job_post_subject_id);
        salaryEditText = (EditText) rootView.findViewById(R.id.job_post_salary_id);
        additionalInfoEditText = (EditText) rootView.findViewById(R.id.job_post_additional_info_id);
        addressEditText = (EditText) rootView.findViewById(R.id.job_address_id);

        categorySpinner = (Spinner) rootView.findViewById(R.id.spinner_category_id);
        classSpinner = (Spinner) rootView.findViewById(R.id.spinner_class_id);
        numOfDaysSpinner = (Spinner) rootView.findViewById(R.id.numOfDays_job_post_id);
        tutorGenderSpinner = (Spinner) rootView.findViewById(R.id.tutor_gender_job_post_id);

        calenderView = (LinearLayout) rootView.findViewById(R.id.calender_view_id);
        calenderText = (TextView) rootView.findViewById(R.id.calender_textView_id);
        calendar = (CalendarView) rootView.findViewById(R.id.calendar_id);

        postBtn = (Button) rootView.findViewById(R.id.job_post_btn_id);

        progressBar = (ProgressBar) rootView.findViewById(R.id.student_post_progress_bar_id);

        progressBar.setVisibility(View.GONE);

        calendar.setVisibility(View.GONE);

        String[] numOfdaysArray = {"Num of days", "1 day", "2 days", "3 days", "4 days", "5 days", "6 days", "7 days"};
        String[] tutorGenderArray = {"Tutor gender", "Any", "Male", "Female"};

        String[] classArray = {"Class/Courses"," Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7", "Class 8", "Class 9", "class SSC / O-level", "class HSC / A=level"};



        numOfDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    numOfDays = (String) parent.getItemAtPosition(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tutorGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    tutorGender = (String) parent.getItemAtPosition(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    courses = (String) parent.getItemAtPosition(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calenderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calenderView.setVisibility(View.GONE);
                calendar.setVisibility(View.VISIBLE);

            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                month++;
                dateToStart = "" + dayOfMonth + "-" + month + "-" + year;
                calenderText.setText(dateToStart);
                calenderView.setVisibility(View.VISIBLE);
                calendar.setVisibility(View.GONE);

            }
        });

        categoryListSpinner = new ArrayList();
        classListSpinner = new ArrayList();

        categoryListSpinner = getData("category.json");

        numOfDaysAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, numOfdaysArray);
        categoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, categoryListSpinner);

        tutorGenderAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, tutorGenderArray);

        classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classArray);


        categorySpinner.setAdapter(categoryAdapter);
        numOfDaysSpinner.setAdapter(numOfDaysAdapter);
        tutorGenderSpinner.setAdapter(tutorGenderAdapter);
        classSpinner.setAdapter(classAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long l) {

                if (position != 0) {

                    categorySpinnerItemSelected(position);
                    category = (String) adapterView.getItemAtPosition(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                postBtn.setVisibility(View.GONE);

                title = titleEditText.getText().toString();
                subjects = subjectsEditText.getText().toString();
                salary = salaryEditText.getText().toString();
                address = addressEditText.getText().toString();
                additionalInfo = additionalInfoEditText.getText().toString();

                if (title.trim().equals("")) {

                    titleEditText.setError("Post Title Required!!");
                    progressBar.setVisibility(View.GONE);
                    postBtn.setVisibility(View.VISIBLE);

                } else if (subjects.trim().equals("")) {

                    subjectsEditText.setError("Subject Name Required!!");
                    progressBar.setVisibility(View.GONE);
                    postBtn.setVisibility(View.VISIBLE);

                } else if (salary.trim().equals("")) {

                    salaryEditText.setError("Salary Required!!");
                    progressBar.setVisibility(View.GONE);
                    postBtn.setVisibility(View.VISIBLE);

                } else if (address.trim().equals("")) {

                    addressEditText.setError("Address Required!!");
                    progressBar.setVisibility(View.GONE);
                    postBtn.setVisibility(View.VISIBLE);

                }

                String email;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                email = preferences.getString(Config.SP_EMAIL, "");

                postStatus(email, title, numOfDays, tutorGender, category, courses, subjects, dateToStart, salary, address, additionalInfo);

                progressBar.setVisibility(View.GONE);
                postBtn.setVisibility(View.VISIBLE);

                /*Log.v("Fields", " Title : " + title  + " Days : " + numOfDays + " TGender : " + tutorGender + " Category : " + category + " Courses : " + courses + " Sub : " + subjects + " Date : " + dateToStart + " Salary : " + salary + " address : "+ additionalInfo);*/

            }
        });

        return rootView;

    }

    private void categorySpinnerItemSelected(int position) {

        if (position == 1) {
            classListSpinner = getClassData("bmediumclass.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 2) {
            classListSpinner = getClassData("emediumclass.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 3) {
            classListSpinner = getClassData("bmediumclass.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 4) {
            classListSpinner = getClassData("madrasa.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 5) {
            classListSpinner = getClassData("uniadmission.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 6) {
            classListSpinner = getClassData("it.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 7) {
            classListSpinner = getClassData("testprep.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 8) {
            classListSpinner = getClassData("languagelearning.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        } else if (position == 9) {
            classListSpinner = getClassData("project_assignment.json");
            classAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item_text, classListSpinner);
            classSpinner.setAdapter(classAdapter);
        }

    }

    private void postStatus(final String email, final String title, final String days_in_week, final String preferred_teacher_gender, final String preferred_medium, final String student_class, final String studentSubject, final String date_to_start, final String salary, final String address, final String content) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custom_alert_dialog_layout_underreview
                        );

                        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_btn_on_post_id);
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
                        builder1.setMessage("Unable to post, please give all required fields");
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
                params.put(KEY_EMAIL, email);
                params.put(TITLE, title);
                params.put(DAYS_IN_WEEK, days_in_week);
                params.put(PREFERRED_TEACHER_GENDER, preferred_teacher_gender);
                params.put(PREFERRED_MEDIUM, preferred_medium);
                params.put(CLASS, student_class);
                params.put(SUBJECT, studentSubject);
                params.put(DATE_TO_START, date_to_start);
                params.put(SALARY, salary);
                params.put(ADDRESS, address);
                params.put(ADDITIONAL_INFO, content);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private ArrayList<String> getData(String fileName) {
        JSONArray jsonArray = null;
        ArrayList<String> cList = new ArrayList<String>();
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray = new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    cList.add(jsonArray.getJSONObject(i).getString("medium"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return cList;
    }

    private ArrayList<String> getClassData(String fileName) {
        JSONArray jsonArray = null;
        ArrayList<String> cList = new ArrayList<String>();
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray = new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    cList.add(jsonArray.getJSONObject(i).getString("cname"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return cList;
    }

}
