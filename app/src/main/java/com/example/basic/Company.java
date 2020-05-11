package com.example.basic;

public class Company {
    private String jobTitle;
    private String jobDescription;
    private String linkLogo;
    public String getLinkLogo() {
        return linkLogo;
    }

    public void setLinkLogo(String linkLogo) {
        this.linkLogo = linkLogo;
    }



    public Company(String jobTitle, String jobDescription,String linkLogo) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.linkLogo=linkLogo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        //jobDescription=jobDescription.substring(0,20);
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
       // jobDescription=jobDescription.substring(0,20);
        this.jobDescription = jobDescription;
    }

    public Company()
    {

    }

}
