package com.v1rex.smartincubator.Model;

import java.io.Serializable;

public class Mentor implements Serializable {
    private String mLastName ;
    private String mFirstName;
    private String mCity;
    private String mSpeciality;
    private String mAccountType;
    private String mUserId;

    public Mentor(String mCity, String mSpeciality, String mLastName , String mFirstName, String mUserId ) {
        this.mCity = mCity;
        this.mSpeciality = mSpeciality;
        this.mUserId = mUserId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public Mentor(){

    }

    public String getmCity() {
        return mCity;
    }

    public String getmSpeciality() {
        return mSpeciality;
    }

    public String getmAccountType() {
        return mAccountType;
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
