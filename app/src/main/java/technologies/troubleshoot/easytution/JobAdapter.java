package technologies.troubleshoot.easytution;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        //Corresponding views are populated
        holder.userImage.setImageResource(item.getImageRecourseId());
        holder.titleTextView.setText(item.getJobTitle());
        holder.salaryTextView.setText(item.getSalary());

        //holder.jobContent.setText(item.getJobContent());

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

        private View container;
        private ImageView userImage;
        private TextView titleTextView, salaryTextView, jobContent;

        public JobAdapterHolder(View listItemView) {
            super(listItemView);

            // Finding  all the View in activity_news_layout.xml with the ID and
            // assigning them to the corresponding view objects
            userImage = (ImageView) listItemView.findViewById(R.id.user_pro_pic_id);
            titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view_id);
            salaryTextView = (TextView) listItemView.findViewById(R.id.salary_text_view_id);


        }
    }
}
