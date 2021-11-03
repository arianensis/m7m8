# MP07/08 Mobile Applications and Interfaces
## AEA2+6: First version of full app
This is a simple working version of the app that is being developed.
It is centered around a database of known stars, to which the user can add fictional ones.
### Structure
Inside the repository you will find:
* Readme.md
  * this file
* SolerArtiguesMeridia_StarNavigator/
  * the whole Android Studio project structure
* SolerArtiguesMeridia_StarNavigator.apk
  * the .apk is copied in the repository root to make it more easy to find
### Usage
You can either try the application in your device:
1. Copy the .apk to an Android device
2. Open it from the Android system
   1. Android will ask for permission to install unknown apps if not already given
3. Open the app once installed

...or in Android Studio:
1. Open the folder as a project and run it
### App description
This app is in English, but also almost completely translated to Spanish and Catalan.
This app consists of several screens or views described below:
## Login
![Login view screenshot][INSERT IMAGE]
This screen simulates a login and may later be used as a real one.
At the moment, you can type any user and password and click the button.
## Main view
In this part you can navigate between the main functionalities of the app via a bottom menu. It gives access to the following three views by clicking the corresponding icon in the menu:
# Home
![Home view screenshot][INSERT IMAGE]
This is the less developed part of the app. Currently it only shows the total amount of entries and has buttons to delete them or reload defaults.
# List
![List view screenshot][INSERT IMAGE]
This view shows a scrollable list of all the stars currently present in the database with their color. In the future, it will show information of each star.
# Add (form)
![Form view screenshot][INSERT IMAGE]
A form to add new stars to the database. The user can create any star as long as they don't repeat names and fill all the fields. The user can choose the coordinate system between spherical (the most common system in star charts) or cubic (a more absolute system which specifies three coordinates in space). The main 
[spectral type](https://en.wikipedia.org/wiki/Stellar_classification#Spectral_types "read about spectral types (NOTE: only the classic ones + L are used in the app)") of the star can be set via a scrollbar, which will change colors accordingly.
### Credits
This was made by Meridia Soler Artigues "Arianensis" as a class project