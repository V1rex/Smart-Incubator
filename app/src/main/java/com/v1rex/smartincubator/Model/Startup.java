package com.v1rex.smartincubator.Model;

import java.io.Serializable;

public class Startup implements Serializable {

    private String mStartupName;
    private String mAssociate;
    private String mDescription;
    private String mWebsite;
    private String mPageFacebook;
    private String mDateOfIncubation;
    private String mJuridiqueSatatus;
    private String mCreationDate;
    private String mActivitySecteur;
    private String mNumberEmployees;
    private String mObjective;
    private String mFond;
    private String mChiffre;
    private String mNeed;
    private String mDomain;
    private String mUserId;

    public Startup(){

    }

    public Startup(String mStartupName, String mAssociate, String mDescription, String mWebsite, String mPageFacebook, String mDateOfIncubation, String mJuridiqueSatatus, String mCreationDate, String mNumberEmployees, String mObjective, String mFond, String mChiffre, String mUserId, String mNeed, String mDomain) {
        this.mStartupName = mStartupName;
        this.mAssociate = mAssociate;
        this.mDescription = mDescription;
        this.mWebsite = mWebsite;
        this.mPageFacebook = mPageFacebook;
        this.mDateOfIncubation = mDateOfIncubation;
        this.mJuridiqueSatatus = mJuridiqueSatatus;
        this.mCreationDate = mCreationDate;
        this.mNumberEmployees = mNumberEmployees;
        this.mObjective = mObjective;
        this.mFond = mFond;
        this.mChiffre = mChiffre;
        this.mDomain = mDomain;
        this.mNeed = mNeed;
        this.mUserId = mUserId;
    }


    public String getmStartupName() {
        return mStartupName;
    }

    public String getmAssociate() {
        return mAssociate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmWebsite() {
        return mWebsite;
    }

    public String getmPageFacebook() {
        return mPageFacebook;
    }

    public String getmDateOfIncubation() {
        return mDateOfIncubation;
    }

    public String getmJuridiqueSatatus() {
        return mJuridiqueSatatus;
    }

    public String getmCreationDate() {
        return mCreationDate;
    }

    public String getmActivitySecteur() {
        return mActivitySecteur;
    }

    public String getmNumberEmployees() {
        return mNumberEmployees;
    }

    public String getmObjective() {
        return mObjective;
    }

    public String getmFond() {
        return mFond;
    }

    public String getmChiffre() {
        return mChiffre;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmNeed() {
        return mNeed;
    }

    public void setmNeed(String mNeed) {
        this.mNeed = mNeed;
    }

    public String getmDomain() {
        return mDomain;
    }

    public void setmDomain(String mDomain) {
        this.mDomain = mDomain;
    }
}
