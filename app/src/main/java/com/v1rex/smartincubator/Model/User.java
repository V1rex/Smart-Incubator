package com.v1rex.smartincubator.Model;

public class User {
    private String mLastName ;
    private String mFirstName;
    private String mEmail;
    private String mUserId;


    public User(String mLastName, String mFirstName, String mEmail, String mUserId) {
        this.mLastName = mLastName;
        this.mFirstName = mFirstName;
        this.mEmail = mEmail;
        this.mUserId = mUserId;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmUserId() {
        return mUserId;
    }
}
