# **College Connect**

[![License Badge](https://img.shields.io/badge/license-Apache%202.0-blue)](https://github.com/sakshampruthi/CollegeConnect/blob/versionTwo/LICENSE)

College Connect is an Android application aimed to be a one stop shop for all the college needs irrespective of college and course.  
It aims to create a helpful plateform for all college students   
It uses Firebase and SQLite to store and display the information to user  
The app uses Material UI for an attractive interface (subject to improvement) which attracts user to stay on the app for more time


This idea was developed for Ideathon held in BVCOE where it was presented to be a college app for BVCOE  

Later it was converted to be a _app for every college_ and course in India

The app is primarily in Java and is tighly coupled as it was developed as a project to learn Android Development. If anyone wishes to contibute to migrate the application to MVVM architecture please contact the repo maintainers as we'd be more than happy to port to MVVM

<img src = "/Images/login.png" width="300" height="600"/>

<img src = "/Images/home_light.png" width="300" height="600"/><img src = "/Images/home_dark.png" width="300" height="600"/>

### Attendence Manager
The app has an integrated **Attendence Manager** which uses SQLite Databse to store and retrieve data and display it in a graphical manner which looks appealing and easy to look at 
  
<img src = "/Images/attendance_light.png" width="300" height="600"/>  <img src = "/Images/attendance_dark.png" width="300" height="600"/>

### Notes  
The Notes tab provides notes uploaded by students. The recycler view shows the name of the author  
with total no. of downloads   
#### _Upload Notes_  
The upload notes area provide for easy upload of codes by users. It takes the authors name to give credit to the uploader

The no. of downloads will encourage students to make more impressive notes and create a sense of competition

<img src = "/Images/notes_light.png" width="300" height="600"/>  <img src = "/Images/notes_dark.png" width="300" height="600"/>


### Timetable
This feature allows user to upload the image of timetable from memory. It stores the image in SQLite Database so that it works even without internet connectivity


### Tools
#### Room Locator  
The room locator tab implements the room locator build by the DSC BVP team. Currently it only works for BVCOE, New Delhi but we plan to expand it using the help of wide network of DSCs present all over India  
#### Covid-19 Tracker
This option loads a webview of a [Covid Tracker](https://www.covid19india.org/) built into the application for instant access
#### Upcomg Events
It shows the upcoming events around you and their details along with an option to register within th application
#### Project Collaboration
This tools help to procreate a plateform where different developers can come and work together on any projects. Just list your project and get collaborators

<img src = "/Images/tools_light.png" width="300" height="600"/> <img src = "/Images/tools_dark.png" width="300" height="600"/>



## Contributions Best Practices


### For first time Contributors

First time contributors can read [CONTRIBUTING.md](/CONTRIBUTING.md) file for help regarding creating issues and sending pull requests.

### Branch Policy

We have the following branches

 * **development** All development goes on in this branch. If you're making a contribution, you are supposed to make a pull request to _development_. PRs to development branch must pass a build check and a unit-test check on Circle CI.
 * **master** This contains shipped code. After significant features/bugfixes are accumulated on development, we make a version update and make a release.
 	
### Code practices

Please help us follow the best practices to make it easy for the reviewer as well as the contributor. We want to focus on the code quality more than on managing pull request ethics.

 * Single commit per pull request
 * Reference the issue numbers in the commit message. Follow the pattern Fixes #<issue number> <commit message>
 * Follow uniform design practices. The design language must be consistent throughout the app.
 * The pull request will not get merged until and unless the commits are squashed. In case there are multiple commits on the PR, the commit author needs to squash them and not the maintainers cherrypicking and merging squashes.
 * If the PR is related to any front end change, please attach relevant screenshots in the pull request description.

### Join the development

* Before you join development, please set up the project on your local machine, run it and go through the application completely. Press on any button you can find and see where it leads to. Explore. (Don't worry ... Nothing will happen to the app or to you due to the exploring :wink: You'll be more familiar with what is where and might even get some cool ideas on how to improve various aspects of the app.)
* If you would like to work on an issue, drop in a comment at the issue. If it is already assigned to someone, but there is no sign of any work being done, please free to drop in a comment so that the issue can be assigned to you if the previous assignee has dropped it entirely.

## License

This project is currently licensed under the Apache License Version 2.0. A copy of [LICENSE](LICENSE) should be present along with the source code.

## Maintainers and Developers
This repository is maintained by [Sajal Jain](https://github.com/sjain30) and [Saksham Pruthi](https://github.com/sakshampruthi)
