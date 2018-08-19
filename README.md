# Smart Incubator

![](https://image.ibb.co/nQT12d/Screenshot_20180528_212525.png)

An Android App coded in Java that use Firebase as Backend for helping startup to find Mentors. 

App features : 

  - Login / Register from firebase 
  - Logout from app in Firebase account
  - Use Firebase RecyclerView Adapter for show showing list of Mentors , Startups and Meetings.
  - Sending Meetings between Mentors and Startups by Firebase Realtime Database . 
  - Search in Mentors and Startups by need or speciality 
  - Settings page for Informations about the startups or mentors . 

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



# Screenshots!
![app icon](https://image.ibb.co/kMexay/Screenshot_20180528_212525.png)

![app icon](https://preview.ibb.co/etFQTJ/Screenshot_20180528_212559.png)

![app icon](https://image.ibb.co/fBBM2d/Screenshot_20180528_212603.png)

![app icon](https://preview.ibb.co/ktRcay/Screenshot_20180528_212532.png)

![app icon](https://preview.ibb.co/dZijNd/Screenshot_20180528_212540.png)

![app icon](https://preview.ibb.co/cRzr2d/Screenshot_20180528_212545.png)

License
----

MIT
