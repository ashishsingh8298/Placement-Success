package com.example.basic;

public class Company {
    private String jobTitle;
    private String jobDescription;
    private String linkLogo;
    private String requiredSkills;
    private  String jobResponsibility;
    private String jobCtc;
    private String eligibleBranch;
    private String eligibilityCriteria;
    private String applyDateFrom;
    private  String applyDateTo;
    private String datetime;

    public Company(String jobTitle, String jobDescription, String linkLogo, String requiredSkills, String jobResponsibility, String jobCtc, String eligibleBranch, String eligibilityCriteria, String applyDateFrom, String applyDateTo,String datetime) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.linkLogo = linkLogo;
        this.requiredSkills = requiredSkills;
        this.jobResponsibility = jobResponsibility;
        this.jobCtc = jobCtc;
        this.eligibleBranch = eligibleBranch;
        this.eligibilityCriteria = eligibilityCriteria;
        this.applyDateFrom = applyDateFrom;
        this.applyDateTo = applyDateTo;
        this.datetime=datetime;
    }
    public String getDatetime(){return datetime;}

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getJobResponsibility() {
        return jobResponsibility;
    }

    public void setJobResponsibility(String jobResponsibility) {
        this.jobResponsibility = jobResponsibility;
    }

    public String getJobCtc() {
        return jobCtc;
    }

    public void setJobCtc(String jobCtc) {
        this.jobCtc = jobCtc;
    }

    public String getEligibleBranch() {
        return eligibleBranch;
    }

    public void setEligibleBranch(String eligibleBranch) {
        this.eligibleBranch = eligibleBranch;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public String getApplyDateFrom() {
        return applyDateFrom;
    }

    public void setApplyDateFrom(String applyDateFrom) {
        this.applyDateFrom = applyDateFrom;
    }

    public String getApplyDateTo() {
        return applyDateTo;
    }

    public void setApplyDateTo(String applyDateTo) {
        this.applyDateTo = applyDateTo;
    }


    public String getLinkLogo() {
        return linkLogo;
    }

    public void setLinkLogo(String linkLogo) {
        this.linkLogo = linkLogo;
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
