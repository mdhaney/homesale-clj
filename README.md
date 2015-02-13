# homesale

Clojurescript example using Firebase

## Overview

While preparing for a home sale, there are many things you have to do - repairs, de-cluttering, improvements, etc.
My wife and I are in that position now, and we are finding there are many tradeoffs regarding the time+cost of repairs
and improvements vs. anticipated increase in value or ease of selling.

I was going to create a spreadsheet to track all this, but since I have been using Clojurescript with Firebase and have been
impressed with the speed and ease of development, I thought - why not see how quickly I can whip something up to help us track
all of this, and serve as a demo for others to use?

## Setup Firebase
If you want to follow along with your own Firebase, then follow these steps:

1. Create a free Firebase account if you don't have one.
2. Create a new Firebase from the dashboard (ex. my demo app has a firebase name of 'haneyhome')
3. Edit firebase.json in this project so that the "firebase" key specifies your firebase name.
4. Open the rules.json file in this project and copy all the text to the clipboard.
5. In Firebase Forge, select the "Security & Rules" tab.  Highlight all the rules and delete them, then paste the rules you copied in the previous step.  Save the changes.
6. In Firebase Forge, go to the Login & Auth tab and enable Email & Password authentication.  Then at the bottom of the page, add your email address and set a password (and any other users you want to give access).

## Development
My development workflow is based on Lighttable.  And yes, I know I can improve the Clojurescript build a lot using some of the new features, like CLJSJS modules, but those don't work with lein-light-nrepl yet, so for now I'm doing it the "old fashioned way" (feel free to change to fit your workflow)

1. Start a repl in the project dir
2. Connect to the repl from LightTable
3. Edit `resources\public\index.html` with the correct port for the LT browser repl.
4. In another terminal, run `lein httpd 3000` to start an http server
5. In yet another terminal, run `lein watch-all`.  This task publishes all the files to the /static folder and watches for changes to the HTML resources and Clojurescript.
6. Browse to localhost:3000/static

## Deployment

1. You will need the Firebase tools - `npm install -g firebase-tools`
2. From a terminal, run `lein dist`.  This will publish all the files to the /dist folder of the project.
3. Probably a good idea to check locally by loading localhost:3000/dist
4. Publish to Firebase by running `firebase deploy` (make sure you set the correct firebase name in firebase.json)
5. App should be available at `https://<firebasename>.firebaseapp.com`

## Changelog

1. This version simply shows a "Hello" message and deploys correctly to Firebase.
2. Added navigation



## License

Copyright © 2015 Michael Haney

Licensed under Eclipse Public License (see [LICENSE](LICENSE)).
