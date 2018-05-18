package com.v1rex.smartincubator.Model;

public class User {
    private String mLastName ;
    private String mFirstName;
    private String mEmail;
    private String mCity;
    private String mSpeciality;
    private String mAccountType;

    public User(String mLastName, String mFirstName, String mEmail, String mCity, String mSpeciality, String mAccountType) {
        this.mLastName = mLastName;
        this.mFirstName = mFirstName;
        this.mEmail = mEmail;
        this.mCity = mCity;
        this.mSpeciality = mSpeciality;
        this.mAccountType = mAccountType;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmSpeciality() {
        return mSpeciality;
    }

    public void setmSpeciality(String mSpeciality) {
        this.mSpeciality = mSpeciality;
    }

    public String getmAccountType() {
        return mAccountType;
    }

    public void setmAccountType(String mAccountType) {
        this.mAccountType = mAccountType;
    }
}
