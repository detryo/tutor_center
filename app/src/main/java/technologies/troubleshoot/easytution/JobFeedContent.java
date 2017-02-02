package technologies.troubleshoot.easytution;

/**
 * Created by kaizer on 11/24/16.
 */

public class JobFeedContent {

    //All private variables of this class is declared here.
    private int imageResourceId;
    private String jobTitle, jobContent, salary;

    public JobFeedContent(int imageResourceId, String jobTitle, String  salary, String jobContent){

        this.imageResourceId = imageResourceId;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.jobContent = jobContent;


    }

    public JobFeedContent(int imageResourceId) {

        this.imageResourceId = imageResourceId;
    }

    public int getImageRecourseId(){

        return imageResourceId;

    }

    public String getJobTitle(){

        return jobTitle;

    }

    public String  getSalary(){

        return salary;

    }

    public String getJobContent(){

        return jobContent;

    }

}
