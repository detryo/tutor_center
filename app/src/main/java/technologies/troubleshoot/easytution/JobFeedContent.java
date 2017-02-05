package technologies.troubleshoot.easytution;

/**
 * Created by kaizer on 11/24/16.
 */

public class JobFeedContent {

    //All private variables of this class is declared here.
    private int imageResourceId;
    private String isActive,jobTitle, jobContent, salary, preferred_medium, classOfStudent, daysPerWeek, dateOfStart, tutorGenderPref, subject, location, userName, dateOfPost;

    public JobFeedContent(int imageResourceId, String jobTitle, String  salary, String preferred_medium, String classOfStudent, String daysPerWeek, String dateOfStart, String tutorGenderPref, String subject, String location, String jobContent, String userName, String dateOfPost){

        this.imageResourceId = imageResourceId;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.preferred_medium = preferred_medium;
        this.classOfStudent = classOfStudent;
        this.daysPerWeek = daysPerWeek;
        this.dateOfStart = dateOfStart;
        this.tutorGenderPref = tutorGenderPref;
        this.subject = subject;
        this.location = location;
        this.jobContent = jobContent;
        this.userName = userName;
        this.dateOfPost = dateOfPost;
        this.isActive = isActive;
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

    public String getPreferred_medium() {
        return preferred_medium;
    }

    public String getClassOfStudent() {
        return classOfStudent;
    }

    public String getDaysPerWeek() {
        return daysPerWeek;
    }

    public String getDateOfStart() {
        return dateOfStart;
    }

    public String getTutorGenderPref() {
        return tutorGenderPref;
    }

    public String getSubject() {
        return subject;
    }

    public String getLocation() {
        return location;
    }

    public String getUserName() {
        return userName;
    }

    public String getDateOfPost() {
        return dateOfPost;
    }

    public String getIsActive() {
        return isActive;
    }
}
