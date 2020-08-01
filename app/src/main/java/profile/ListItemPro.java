package profile;

public class ListItemPro {

    public ListItemPro(String title, String applicantname, String gender, String paynumber) {
        this.title = title;
        this.applicantname = applicantname;
        this.gender = gender;
        this.paynumber = paynumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApplicantname() {
        return applicantname;
    }

    public void setApplicantname(String applicantname) {
        this.applicantname = applicantname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return paynumber;
    }

    public void setDob(String paynumber) {
        this.paynumber = paynumber;
    }

    String title,applicantname,gender,paynumber;
}
