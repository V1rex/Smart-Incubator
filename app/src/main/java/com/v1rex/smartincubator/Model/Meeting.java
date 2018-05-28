package com.v1rex.smartincubator.Model;

import java.sql.Time;
import java.util.Date;

public class Meeting {

    private String mUserIdSent;

    private String mUserIdReceived;

    private String mPlace;

    private String mDate;

    private String mType;

    private String accepte;

    public Meeting(String mUserIdSent, String mUserIdReceived, String mPlace, String mDate, String accepte, String mType) {
        this.mUserIdSent = mUserIdSent;
        this.mUserIdReceived = mUserIdReceived;
        this.mPlace = mPlace;
        this.mDate = mDate;
        this.accepte = accepte;
        this.mType = mType;

    }

    public Meeting(){

    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmUserIdSent() {
        return mUserIdSent;
    }

    public void setmUserIdSent(String mUserIdSent) {
        this.mUserIdSent = mUserIdSent;
    }

    public String getmUserIdReceived() {
        return mUserIdReceived;
    }

    public void setmUserIdReceived(String mUserIdReceived) {
        this.mUserIdReceived = mUserIdReceived;
    }

    public String getmPlace() {
        return mPlace;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getAccepte() {
        return accepte;
    }

    public void setAccepte(String accepte) {
        this.accepte = accepte;
    }
}
