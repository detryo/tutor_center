package technologies.troubleshoot.easytution;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kaizer on 11/24/16.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobAdapterHolder> {

    private LayoutInflater inflater;
    private ArrayList<JobFeedContent> job;

    public JobAdapter(Activity context, ArrayList<JobFeedContent> job) {

        inflater = LayoutInflater.from(context);
        this.job = job;

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
        holder.userImage.setImageResource(item.getImageRecourseId());
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
        private TextView titleTextView, salaryTextView, preferred_medium, classOfStudent, daysPerWeek, dateOfStart, tutorGenderPref, subject, location, additionalInfo;
        private Button interestBtn;

        public JobAdapterHolder(View listItemView) {
            super(listItemView);

            // Finding  all the View in activity_news_layout.xml with the ID and
            // assigning them to the corresponding view objects
            userImage = (ImageView) listItemView.findViewById(R.id.user_pro_pic_id);
            titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view_id);
            salaryTextView = (TextView) listItemView.findViewById(R.id.salary_text_view_id);
            clickIcon = (ImageView) listItemView.findViewById(R.id.clickButton_id);

            basicInfo = listItemView.findViewById(R.id.job_feed_content_basic_layout_id);
            detailInfo = listItemView.findViewById(R.id.job_feed_content_detail_layout_id);

            preferred_medium = (TextView) listItemView.findViewById(R.id.preferred_medium_job_feed_id);
            classOfStudent = (TextView) listItemView.findViewById(R.id.class_of_student_job_feed_id);
            daysPerWeek = (TextView) listItemView.findViewById(R.id.days_per_week_job_feed_id);
            dateOfStart = (TextView) listItemView.findViewById(R.id.date_of_job_feed_post_id);
            tutorGenderPref = (TextView) listItemView.findViewById((R.id.gender_preference_job_feed_id));
            subject = (TextView) listItemView.findViewById(R.id.subject_jod_feed_id);
            location = (TextView) listItemView.findViewById(R.id.location_job_feed_id);
            additionalInfo = (TextView) listItemView.findViewById(R.id.additional_info_job_feed_id);
            interestBtn = (Button) listItemView.findViewById(R.id.interest_btn_id);

            interestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickIcon.setVisibility(View.VISIBLE);
                    detailInfo.setVisibility(View.GONE);

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
    }
}
