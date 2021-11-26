# AEA3: Second working version of app
This is a simple working version of the app that is being developed.
It is centered around a database of known stars, to which the user can add fictional ones.

**[NEW:]** This version adds the ability to view details of each star by clicking on its name, and a settings section
## Structure
Inside the repository you will find:
* Readme.md
  * this file
* SolerArtiguesMeridia_StarNavigator/
  * the whole Android Studio project structure
* SolerArtiguesMeridia_StarNavigator.apk
  * the .apk is copied in the repository root to make it more easy to find
## Usage
You can either try the application in your device:
1. Copy the .apk to an Android device
2. Open it from the Android system
   1. Android will ask for permission to install unknown apps if not already given
3. Open the app once installed

...or in Android Studio:
1. Open the folder as a project and run it
## App description
This app is in English, but also almost completely translated to Spanish and Catalan.
This app consists of several screens or views described below:
### Login
![Login view screenshot](../AEA2/screenshots/login.png)

This screen simulates a login and may later be used as a real one.
At the moment, you can type any user and password and click the button.
The next screen will then greet you with the name you have entered.
### Main view
In this part you can navigate between the main functionalities of the app via a bottom menu. It gives access to the following three views by clicking the corresponding icon in the menu:
#### Home
![Home view screenshot](../AEA2/screenshots/home.png)

This is the less developed part of the app. Currently it only shows the total amount of entries and has buttons to delete them or reload defaults.
#### List
![List view screenshot](../AEA2/screenshots/list.png)

This view shows a scrollable list of all the stars currently present in the database with their color. **[NEW]:** If you click on the name of a star it will display the next screen.
#### **[NEW]:** Info
![Info view screenshot](./screenshots/info.png)

A simple document showing information of the selected star. It currently displays the name, spectral class and coordinates in both cubic and spherical systems
#### Add (form)
![Form view screenshot](../AEA2/screenshots/form.png)

A form to add new stars to the database. The user can create any star as long as they don't repeat names and fill all the fields. The user can choose the coordinate system between spherical (the most common system in star charts) or cubic (a more absolute system which specifies three coordinates in space). The main 
[spectral type](https://en.wikipedia.org/wiki/Stellar_classification#Spectral_types "read about spectral types (NOTE: only the classic ones + L are used in the app)") of the star can be set via a scrollbar, which will change colors accordingly.
#### **[NEW]:** Settings
![Settings view screenshot](./screenshots/settings.png)

The app has now a settings section which allow to select one of the three languages from a radio group (various mutually excluding options) and to set the decimal precision of the numbers shown in the Info view. There are also buttons to restore all the configuration and to log out, since the app will not log you out at startup anymore since this version.
## Technology used
* This project is being made in Android Studio and coded in Java
* The navigation is achieved by a combination of activities+intents and fragments
  * The login screen is an activity and the rest of the app is another activity
  * The second activity contains three fragments in which the main screens of the app are shown
* The values are persistently stored via a SQLite database (following the standard Anrdoid MVC and using DBHelper-derived classes)
* A totally custom class, Star, is used to create and manage the objects, including calls to the database
* Dialogs are used to confirm actions and Toasts display afterwards
* **[NEW]:** Shared preferences to allow setting a language different from that of the system and adjust the decimal precision of numbers
* **[NEW]:** A new custom class has been added to manage shared preferences and auxiliar methods

![Dialog screenshot](../AEA2/screenshots/dialog.png)
## ToDo
This app is a work in progress. This version has added the following features
* **[NEW]:** Detailed information about stars, accessed by clicking them in the list
* **[NEW]:** Persistent data to prevent continuous login
The below list are still to be done
* A more personal style for the forms and buttons
* Real user and password associations, ability to create and delete users
* Ability to export custom data
* Loading data of stars from an online scientific database
* Firebase chat

...and maybe more
## Credits
This was made by Meridia Soler Artigues "Arianensis" as a class project
