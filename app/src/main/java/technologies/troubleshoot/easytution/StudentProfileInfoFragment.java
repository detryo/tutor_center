package technologies.troubleshoot.easytution;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kaizer on 2/9/17.
 */

public class StudentProfileInfoFragment extends Fragment{

    public static final String USER_EMAIL = "email";
    public static final String REF_PERSON_NAME = "ref_person_name";
    public static final String CONTACT_NUMBER = "ref_phone_no";
    public static final String RELATION = "ref_person_relation";
    public static final String ID_CARD = "id_card";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    EditText refPersonNameEditText, contactNumberEditText, relationEditText;

    Button editStudentInfoBtn, saveStudentInfoBtn;

    String email;

    ImageView idCardStudentImageView;

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

        idCardStudentImageView = (ImageView) rootView.findViewById(R.id.student_id_card_image_view_id);

        idCardStudentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

            }
        });

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_STUDENT_INFO_URL,
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

            Picasso.with(getContext()).load(json.getString(ID_CARD)).into(idCardStudentImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set the username -- that is fetched from database
        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserName)).setText(name);*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && null != data && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                idCardStudentImageView.setImageBitmap(bitmap);
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
                String result = rh.sendPostRequest(Config.UPDATE_STUDENT_ID_CARD_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

}
