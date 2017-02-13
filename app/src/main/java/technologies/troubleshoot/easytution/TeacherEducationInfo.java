package technologies.troubleshoot.easytution;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_OK;


/**
 * Created by kaizer on 2/6/17.
 */

public class TeacherEducationInfo extends Fragment {

    public static final String USER_EMAIL = "email";

    public static final String SSC_YEAR_OF_PASSING = "ssc_year_of_passing";
    public static final String SSC_GRUOP = "ssc_group";
    public static final String SSC_GPA = "ssc_gpa";

    public static final String HSC_YEAR_OF_PASSING = "hsc_year_of_passing";
    public static final String HSC_GRUOP = "hsc_group";
    public static final String HSC_GPA = "hsc_gpa";

    public static final String UNDER_GRAD_YEAR_OF_PASSING = "under_grad_year_of_passing";
    public static final String UNDER_GRAD_MAJOR = "under_grad_major";
    public static final String UNDER_GRAD_CGPA = "under_grad_cgpa";

    public static final String GRAD_YEAR_OF_PASSING = "grad_year_of_passing";
    public static final String GRAD_MAJOR = "grad_major";
    public static final String GRAD_CGPA = "grad_cgpa";

    public static final String ID_CARD = "id_card";
    private static final int PICK_IMAGE_REQUEST = 1;

    String imgDecodableString;

    EditText sscYearOfPassingEditText, sscGroupEditText, sscGpaEditText, hscYearOfPassingEditText, hscGroupEditText, hscGpaEditText, underGradYearOfPassingEditText, underGradMajorEditText, underGradCgpaEditText, gradYearOfPassingEditText, gradMajorEditText, gradCgpaEditText;

    Button editEducationInfoBtn, saveEducationInfoBtn;

    ImageView idCardImageView;

    String email, idCardUrl;

    View rootView;

    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.teacher_educational_info_layout, container, false);

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

        editEducationInfoBtn = (Button) rootView.findViewById(R.id.edit_education_info_btn_id);
        saveEducationInfoBtn = (Button) rootView.findViewById(R.id.save_education_info_btn_id);

        idCardImageView = (ImageView) rootView.findViewById(R.id.id_card_image_view_id);

        idCardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });

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

                updateEducationInfo(email, sscYearOfPassingEditText.getText().toString(), sscGroupEditText.getText().toString(), sscGpaEditText.getText().toString(), hscYearOfPassingEditText.getText().toString(), hscGroupEditText.getText().toString(), hscGpaEditText.getText().toString(), underGradYearOfPassingEditText.getText().toString(), underGradMajorEditText.getText().toString(), underGradCgpaEditText.getText().toString(), gradYearOfPassingEditText.getText().toString(), gradMajorEditText.getText().toString(), gradCgpaEditText.getText().toString());

                setEditTextEnableOrDisable(false);

            }
        });


        return rootView;

    }

    private void updateEducationInfo(final String email, final String sscYearOfPassing, final String sscGroup, final String sscGpa, final String hscYearOfPassing, final String hscGroup, final String hscGpa, final String underGradYearOfPassing, final String underGradMajor, final String underGradCgpa, final String gradYearOfPassing, final String gradMajor, final String gradCgpa) {

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
                Map<String, String> params = new HashMap<>();
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
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void fetchEducationInfo() {

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

    private void showJSON(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject json = result.getJSONObject(0);

            sscYearOfPassingEditText.setText(json.getString(SSC_YEAR_OF_PASSING));
            sscGroupEditText.setText(json.getString(SSC_GRUOP));
            sscGpaEditText.setText(json.getString(SSC_GPA));

            hscYearOfPassingEditText.setText(json.getString(HSC_YEAR_OF_PASSING));
            hscGroupEditText.setText(json.getString(HSC_GRUOP));
            hscGpaEditText.setText(json.getString(HSC_GPA));

            underGradYearOfPassingEditText.setText(json.getString(UNDER_GRAD_YEAR_OF_PASSING));
            underGradMajorEditText.setText(json.getString(UNDER_GRAD_MAJOR));
            underGradCgpaEditText.setText(json.getString(UNDER_GRAD_CGPA));

            gradYearOfPassingEditText.setText(json.getString(GRAD_YEAR_OF_PASSING));
            gradMajorEditText.setText(json.getString(GRAD_MAJOR));
            gradCgpaEditText.setText(json.getString(GRAD_CGPA));

            Picasso.with(getContext()).load(json.getString(ID_CARD)).into(idCardImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setEditTextEnableOrDisable(boolean state) {

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && null != data && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                idCardImageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

        /*try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                ImageView imgView = (ImageView) rootView.findViewById(R.id.id_card_image_view_id);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }*/

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(){
        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Void,Void,String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Please wait...","uploading",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<>();
                param.put(ID_CARD,image);
                param.put(USER_EMAIL, email);
                String result = rh.sendPostRequest(Config.UPDATE_TEACHER_ID_CARD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

}
