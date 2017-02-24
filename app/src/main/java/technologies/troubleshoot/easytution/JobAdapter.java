package technologies.troubleshoot.easytution;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaizer on 11/24/16.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobAdapterHolder> {

    public static final String USER_EMAIL = "email";
    public static final String USER_NAME = "username";
    public static final String POST_ID = "post_id";

    private LayoutInflater inflater;
    private ArrayList<JobFeedContent> job;
    private Activity context;

    public JobAdapter(Activity context, ArrayList<JobFeedContent> job) {

        inflater = LayoutInflater.from(context);
        this.job = job;
        this.context = context;

    }

    @Override
    public JobAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JobAdapterHolder(inflater.inflate(R.layout.job_feed_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(JobAdapterHolder holder, int position) {

        //gets item position of the current news.
        JobFeedContent item = job.get(position);

        holder.detailInfo.setVisibility(View.GONE);

        //Corresponding views are populated
        if(!item.getImageRecourseId().equals(" "))
        Picasso.with(context).load(item.getImageRecourseId()).into(holder.userImage);

        holder.userName.setText(item.getUserName());
        holder.statusTime.setText(item.getStatusTime());
        holder.titleTextView.setText(item.getJobTitle());
        holder.salaryTextView.setText(item.getSalary());
        holder.preferred_medium.setText((item.getPreferred_medium()));
        holder.classOfStudent.setText(item.getClassOfStudent());
        holder.daysPerWeek.setText(item.getDaysPerWeek());
        holder.dateOfStart.setText(item.getDateOfStart());
        holder.tutorGenderPref.setText(item.getTutorGenderPref());
        holder.subject.setText(item.getSubject());
        holder.location.setText(item.getLocation());
        holder.additionalInfo.setText(item.getJobContent());
        holder.setPostId(item.getPostId());

    }

    @Override
    public int getItemCount() {
        return job.size();
    }

    public void itemUpdated(JobFeedContent[] items) {

        job.clear();
        for (int i = 0; i < items.length; i++)
            job.add(items[i]);

        notifyDataSetChanged();

    }

    class JobAdapterHolder extends RecyclerView.ViewHolder {

        private View basicInfo;
        private View detailInfo;
        private ImageView userImage, clickIcon;
        private TextView titleTextView, salaryTextView, preferred_medium, classOfStudent, daysPerWeek, dateOfStart, tutorGenderPref, subject, location, additionalInfo, userName, statusTime;
        private Button interestBtn;
        private String postId;

        public JobAdapterHolder(View listItemView) {
            super(listItemView);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String userType = preferences.getString(Config.SP_USERTYPE, "");

            // Finding  all the View in activity_news_layout.xml with the ID and
            // assigning them to the corresponding view objects
            userImage = (ImageView) listItemView.findViewById(R.id.user_pro_pic_id);
            userName = (TextView) listItemView.findViewById(R.id.post_user_name_job_feed_id);
            statusTime = (TextView) listItemView.findViewById(R.id.date_of_job_feed_post_id);
            titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view_id);
            salaryTextView = (TextView) listItemView.findViewById(R.id.salary_text_view_id);
            clickIcon = (ImageView) listItemView.findViewById(R.id.clickButton_id);

            basicInfo = listItemView.findViewById(R.id.job_feed_content_basic_layout_id);
            detailInfo = listItemView.findViewById(R.id.job_feed_content_detail_layout_id);

            preferred_medium = (TextView) listItemView.findViewById(R.id.preferred_medium_job_feed_id);
            classOfStudent = (TextView) listItemView.findViewById(R.id.class_of_student_job_feed_id);
            daysPerWeek = (TextView) listItemView.findViewById(R.id.days_per_week_job_feed_id);
            dateOfStart = (TextView) listItemView.findViewById(R.id.date_job_feed_id);
            tutorGenderPref = (TextView) listItemView.findViewById((R.id.gender_preference_job_feed_id));
            subject = (TextView) listItemView.findViewById(R.id.subject_jod_feed_id);
            location = (TextView) listItemView.findViewById(R.id.location_job_feed_id);
            additionalInfo = (TextView) listItemView.findViewById(R.id.additional_info_job_feed_id);
            interestBtn = (Button) listItemView.findViewById(R.id.interest_btn_id);

            if (userType.equals("student")) {

                interestBtn.setVisibility(View.GONE);

            } else if (userType.equals("teacher")) {

                interestBtn.setVisibility(View.VISIBLE);

            }

            interestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    email = preferences.getString(Config.SP_EMAIL, "");

                    clickIcon.setVisibility(View.VISIBLE);
                    detailInfo.setVisibility(View.GONE);
                    showInterest(email, postId);

                }



            });

            detailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickIcon.setVisibility(View.VISIBLE);
                    detailInfo.setVisibility(View.GONE);


                }
            });

            basicInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickIcon.setVisibility(View.GONE);
                    detailInfo.setVisibility(View.VISIBLE);

                }
            });

        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        private void showInterest(final String email, final String post_id) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.INTERESTED_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.trim().equals("success")) {
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.custom_alert_dialog_layout_news_feed);

                                Button dialogButton = (Button) dialog.findViewById(R.id.dialog_btn_id);
                                // if button is clicked, close the custom_alert_dialog_layout_news_feed dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("You have already applied on this job");
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

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, " " + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(USER_EMAIL, email);
                    params.put(POST_ID, post_id);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }
    }
}
