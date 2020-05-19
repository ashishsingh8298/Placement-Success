package com.example.basic;

public class Comment {
    private String userId;
    private String Comment;
    private String CompanyId;
    private String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public Comment()
    {

    }

    public String getUserId() {
        return userId;
    }


    public Comment(String userId, String comment,String CompanyId,String Date) {
        this.userId = userId;
        this.Comment=comment;
        this.CompanyId=CompanyId;
        this.Date=Date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment){
        this.Comment= comment;
    }
}
