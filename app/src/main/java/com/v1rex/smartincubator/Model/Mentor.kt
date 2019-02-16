package com.v1rex.smartincubator.Model

class Mentor  {
    var mCity : String = ""
    var mSpeciality : String = ""
    var mLastName : String = ""
    var mFirstName : String = ""
    var mEmail : String = ""
    var mPhoneNumber : String = ""
    var mUserId : String = ""
    var mProfilePhoto : String = ""

    constructor(city: String , speciality: String ,lastName: String, firstName: String ,email: String , phoneNumber: String , userId: String ){
        mCity = city
        mSpeciality = speciality
        mLastName = lastName
        mFirstName = firstName
        mEmail = email
        mPhoneNumber = phoneNumber
        mUserId = userId
    }

    constructor(){

    }

    constructor(city: String , speciality: String ,lastName: String, firstName: String ,email: String , phoneNumber: String , userId: String , photoProfileUrl : String ){
        mCity = city
        mSpeciality = speciality
        mLastName = lastName
        mFirstName = firstName
        mEmail = email
        mPhoneNumber = phoneNumber
        mUserId = userId
        mProfilePhoto = photoProfileUrl
    }


}
