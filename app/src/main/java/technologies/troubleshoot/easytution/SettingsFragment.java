package technologies.troubleshoot.easytution;

import android.app.Dialog;
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
import android.widget.TextView;
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
 * Created by kaizer on 2/12/17.
 */

public class SettingsFragment extends Fragment {


    public static final String USER_EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    private static final String USER_IMAGE = "profile_pic";

    private static final int PICK_IMAGE_REQUEST = 1;

    Bitmap bitmap;


    EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText, phoneNumberEditText;

    Button editPasswordBtn, savePasswordBtn, editPhoneNumberBtn, savePhoneNumberBtn;

    String email, currentPassword;

    ImageView profileImage;

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

        savePasswordBtn.setVisibility(View.GONE);
        savePhoneNumberBtn.setVisibility(View.GONE);

        profileImage = (ImageView) rootView.findViewById(R.id.profile_pic_id);

        fetchSettingsInfo();
        setEditTextEnableOrDisableForPassword(false);
        setEditTextEnableOrDisableForPhoneNumber(false);

        editPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisableForPassword(true);
                editPasswordBtn.setVisibility(View.GONE);
                savePasswordBtn.setVisibility(View.VISIBLE);

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
                editPasswordBtn.setVisibility(View.VISIBLE);
                savePasswordBtn.setVisibility(View.GONE);
            }
        });

        editPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextEnableOrDisableForPhoneNumber(true);
                editPhoneNumberBtn.setVisibility(View.GONE);
                savePhoneNumberBtn.setVisibility(View.VISIBLE);

            }
        });

        savePhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSettingsInfo(email, currentPasswordEditText.getText().toString(), phoneNumberEditText.getText().toString());
                setEditTextEnableOrDisableForPhoneNumber(false);
                editPhoneNumberBtn.setVisibility(View.VISIBLE);
                savePhoneNumberBtn.setVisibility(View.GONE);

            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

            }
        });



        return rootView;

    }

    private void updateSettingsInfo(final String email, final String password, final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_URL,
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

            if(!json.getString(USER_IMAGE).equals(""))
                Picasso.with(getContext()).load(json.getString(USER_IMAGE)).into(profileImage);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && null != data && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
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
                param.put(USER_IMAGE,image);
                param.put(USER_EMAIL, email);
                String result = rh.sendPostRequest(Config.UPDATE_USER_PROFILE_PIC_URL, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

}
