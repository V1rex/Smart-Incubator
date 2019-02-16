package com.v1rex.smartincubator.Model

class Startup{

    var mStartupName: String = ""
    var mAssociate: String = ""
    var mDescription: String = ""
    var  mWebsite: String = ""
    var mPageFacebook: String = ""
    var mDateOfIncubation: String = ""
    var mJuridiqueSatatus: String = ""
    var  mCreationDate: String = ""
    var  mNumberEmployees: String = ""
    var mObjective: String = ""
    var  mFond: String = ""
    var mChiffre: String = ""
    var mUserId: String = ""
    var mNeed: String = ""
    var  mDomain: String = ""
    var mPhotoProfile : String = ""

    constructor (startupName: String , associate: String , description: String, website: String , pageFacebook: String , dateOfIncubation: String , juridiqueSatatus: String , creationDate: String ,numberEmployees: String , objective: String, fond: String , chiffre: String , userId: String , need: String , domain: String ){
        mStartupName = startupName
        mAssociate = associate
        mDescription = description
        mWebsite = website
        mPageFacebook = pageFacebook
        mDateOfIncubation = dateOfIncubation
        mJuridiqueSatatus = juridiqueSatatus
        mCreationDate = creationDate
        mNumberEmployees = numberEmployees
        mObjective = objective
        mFond = fond
        mChiffre = chiffre
        mUserId = userId
        mNeed = need
        mDomain = domain
    }

    constructor(){

    }

    constructor (startupName: String , description: String, userId: String , need: String , domain: String , profilePhotoUrl : String  ){
        mStartupName = startupName
        mDescription = description
        mUserId = userId
        mNeed = need
        mDomain = domain
        mPhotoProfile = profilePhotoUrl
    }


}
