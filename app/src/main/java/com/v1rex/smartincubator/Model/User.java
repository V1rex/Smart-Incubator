package com.v1rex.smartincubator.Model;

public class User {
    private String mLastName ;
    private String mFirstName;
    private String mEmail;
    private String mSexe;
    private String mAccountType;
    private String mAge;
    private String mUserId;


    public User(String mLastName, String mFirstName, String mEmail,  String mSexe, String mAge,String mAccountType, String mUserId) {
        this.mLastName = mLastName;
        this.mFirstName = mFirstName;
        this.mEmail = mEmail;
        this.mSexe = mSexe;
        this.mAge = mAge;
        this.mAccountType = mAccountType;
        this.mUserId = mUserId;
    }

    public User() {
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

    public String getmAccountType() {
        return mAccountType;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmSexe() {
        return mSexe;
    }

    public String getmAge() {
        return mAge;
    }
}
