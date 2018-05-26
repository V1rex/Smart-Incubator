package com.v1rex.smartincubator.Model;

import java.io.Serializable;

public class Mentor  {
    private String mLastName ;
    private String mFirstName;
    private String mCity;
    private String mSpeciality;
    private String mEmail;
    private String mPhoneNumber;
    private String mUserId;

    public Mentor(String mCity, String mSpeciality, String mLastName , String mFirstName,String mEmail, String mPhoneNumber, String mUserId ) {
        this.mCity = mCity;
        this.mSpeciality = mSpeciality;
        this.mUserId = mUserId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mEmail = mEmail;
        this.mPhoneNumber = mPhoneNumber;
    }

    public Mentor(){

    }


    public String getmEmail() {
        return mEmail;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmSpeciality() {
        return mSpeciality;
    }


    public String getmUserId() {
        return mUserId;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmFirstName() {
        return mFirstName;
    }
}
