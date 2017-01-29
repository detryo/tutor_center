package technologies.troubleshoot.easytution;

/**
 * Created by kaizer on 11/24/16.
 */

public class NewsFeedContent {

    //All private variables of this class is declared here.
    private int imageResourceId;
    private String userName, timeAndDate, newsContent;

    public NewsFeedContent(int imageResourceId, String userName, String timeAndDate, String newsContent){

        this.imageResourceId = imageResourceId;
        this.userName = userName;
        this.timeAndDate = timeAndDate;
        this.newsContent = newsContent;


    }

    public NewsFeedContent(int imageResourceId) {

        this.imageResourceId = imageResourceId;
    }

    public int getImageRecourseId(){

        return imageResourceId;

    }

    public String getUserName(){

        return userName;

    }

    public String getTimeAndDate(){

        return timeAndDate;

    }

    public String getNewsContent(){

        return newsContent;

    }

}
