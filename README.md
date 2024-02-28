# HelpGenic

A healthcare application which connects Patients with Doctors and Donors

## Features

- All features(use cases) are mentioned in the above git repository in the file named 'UseCases.pdf' , lying in the documentation folder.

## Supported Versions

- API 30

## Instructions to Run this Project

- Download android studio IDE, from the link https://developer.android.com/studio?gclid=Cj0KCQiAvqGcBhCJARIsAFQ5ke4LryVS6FfNCF7C2QRtnPQbW8wLvzxoNRQWjNuWTfXYzAwr8jjeEpAaAs7FEALw_wcB&gclsrc=aw.ds

- Open the project HelpGenic(residing in the 'code' folder above).

- Download Emulator pixel 4xl from device manager. Now you are all setup , you can now run the appliation on your emulator.

- Run by pressing the Green(triangle) run button on the top bar.

- If you wish to run app in your mobile phone then simply connect your android phone to your laptop/pc and enable the developer options and afterwards debug mode on your phone , now your phone will be shown on the android studio , in the device options. Select your phone and then press the run button and you are good to go now OR
  you can just download the apk from the android studio and run it on your mobile phone after installing it.

- To download the apk , go to Build tab in android studio and here click , build apk , it would get build after a while after then you can download it from this location:
  HelpGenic/app/build/intermediates/apk/debug

# Issues

- The app is rather slow as we have use azure database , so the app is developing connection with the database everytime it needs it , so because of this connection developing time , the app works slow. In future I have a plan to change this project by using firebase instead of azure , so it will work smoothly then.

- 1 or 2 bugs , like I remember the page for patient view , where all the doctors list is shown with which the patient has done appointments uptill now , there is a bug in this page that if a patient has done 2 appointments with the same doctor , it will be shown twice . So i think the code is fine but there is some mistake in the sql query , which actually get all such doctors for you.

- The second bug I remeber , that if you do multiple appointments on the same day , so there is a bug while displaying them , it shows only one upcoming appointment instead of two (something like that) , so this bug too is not because of the code , its because of the sql query which fetches this data.

- I am sure that when I will integrate firebase with it , all such issues would be resolved.
