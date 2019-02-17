# Smart Incubator
![App Slogon](https://i.imgur.com/CE32aob.png)

## Table of contents
* [General info](#general-info)
* [Screenshots](#screenshots)
* [Technologies](#technologies)
* [Features](#features)
* [Setup](#setup)
* [Code examples in the app](#code-examples-in-the-app)

## General info
Smart incubator is an app that allows startups to find people that will help them grow . Those people are called in the app "Mentors" and every mentor have a specialty wich make him able to search about startups that needs his speciality and contact them by sending messages or meetings in the app.

## Screenshots
![Screenshot 1](https://i.imgur.com/7DCO3r8.png)

## Technologies
* This apps uses firebase as backend service for the app .


## Features
* Getting a list of mentors and startups .
* Searching in a list of mentors (by : specialty) and startups (by : need) . 
* Sending messages between the users (mentors and startups) in the app . 
* Sending meetings invitations between the users (mentors and startups) in the app .  


## Setup
1. Create an new firebase project 
2. Add just the google-services.json to /app folder
3. Enable email authentication method in your firebase project
4. Change Realtime Database rules with this : 

```sh
{
  "rules": {
    ".read": true,
    ".write": "auth != null"
  }
}
```

## Code examples in the app 
This app show many codes example in kotlin on :
* Login / Register from firebase auth .  
* Use Firebase RecyclerView Adapter for show showing list of Mentors, Startups, Meetings and Messages .
* Sending Meetings between Mentors and Startups by Firebase Realtime Database . 
* Sending Messages between users by Firebase Realtime Database . 
* Retrieving an ArrayList of object from firebase realtime database , searching in them and returning a list on RecyclerView .
* Changing informations in firebase realtime database (example : settings pages) . 


# Setup firebase:
1. Create an new firebase project 
2. Add just the google-services.json to /app folder
3. Enable email authentication method in your firebase project
4. Change Realtime Database rules with this : 

```sh
{
  "rules": {
    ".read": true,
    ".write": "auth != null"
  }
}
```

License
----

MIT
