package technologies.troubleshoot.easytution;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kaizer on 11/24/16.
 */

public class NewsAdapter extends ArrayAdapter<NewsFeedContent> {

    public NewsAdapter(Activity context, ArrayList<NewsFeedContent> news) {

        super(context, 0, news);
    }

    ImageView userImage;
    TextView userName;
    TextView dateAndTime;
    TextView newsContent;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;


        if (listItemView == null) {


            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_news_layout, parent, false);



        }

        // Finding  all the View in activity_news_layout.xml with the ID and
        // assigning them to the corresponding view objects
        userImage = (ImageView) listItemView.findViewById(R.id.user_imageView_id);
        userName = (TextView) listItemView.findViewById(R.id.news_username_textView_id);
        dateAndTime = (TextView) listItemView.findViewById(R.id.news_date_time_textView_id);
        newsContent = (TextView) listItemView.findViewById(R.id.news_content_textView_id);

        //gets item position of the current news.
        NewsFeedContent currentNews = getItem(position);

        //Corresponding views are populated
        userImage.setImageResource(currentNews.getImageRecourseId());

        if (userName != null && dateAndTime != null && newsContent != null) {

            //Corresponding views are populated
            userName.setText(currentNews.getUserName());
            dateAndTime.setText(currentNews.getTimeAndDate());
            newsContent.setText(currentNews.getNewsContent());
        }
        
        return listItemView;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
