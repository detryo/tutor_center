package technologies.troubleshoot.easytution;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;
import static technologies.troubleshoot.easytution.LoginActivity.KEY_EMAIL;


/**
 * Created by kaizer on 1/1/17.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {

    private static final String REGISTER_URL = "http://tuition.troubleshoot-tech.com/post.php";
    private static final String KEY_CONTENT= "content";

    public ProfileFragment() {
        super();
    }

    EditText postBarEditText;
    Button postBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final String email;

        //TextView textView;
        View rootView = inflater.inflate(R.layout.dash_board_post_bar, container, false);

        postBarEditText = (EditText) rootView.findViewById(R.id.postBar_editText_id);
        postBtn = (Button) rootView.findViewById(R.id.postBtn_id);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        email = preferences.getString(KEY_EMAIL, "");

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String content = postBarEditText.getText().toString();
                postStatus(email, content);
            }
        });

        return rootView;

    }

    private void postStatus(final String email, final String content) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
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
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_CONTENT, content);
                params.put(KEY_EMAIL, email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
