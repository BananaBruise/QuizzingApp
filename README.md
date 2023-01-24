# QuizzingApp

## Trying the app
Use this [invite link](https://appdistribution.firebase.dev/i/425ee29108df6e66) to grab the latest build of this app

## Summary
Using Android Studio and Java, we (Abhinav Bhashyam and Han Tu) have created an interactive quizzing app.

## Backend Setup
This project was written for Firebase. [Firebase](https://firebase.google.com/) is a platform for creating and delivering cross-platform applications. Our project uses the Firestore Database and Authentication services.

To link this project to a new/your own Firebase backend, please follow [this guide](https://firebase.google.com/docs/android/setup#add-config-file) to create a Firebase project and install the proper connection file (e.g. `google-services.json`). For this project, place the Firebase connection file here: `QuizzingApp/app/google-services.json`

## Testing the Project using Firebase
We distribute app builds using the App Distribution service in Firebase. The basic steps are:

1. Create a build (.apk)
2. Upload build to App Distribution
3. Invite any groups/individual tester(s)
4. Distribute app

Steps to build an APK can be found [here](https://developer.android.com/studio/run/build-for-release), assuming you're using Android Studio. App Distribution tutorial [here](https://firebase.google.com/docs/app-distribution/android/distribute-console).

## Application Workflow
This is a step-by-step walkthrough of how to use this app.

### Part 1: Authentication
To start, teachers and students must sign up.<br/><br/>
<ins>What you'll see when authenticating:</ins>

<img src="https://user-images.githubusercontent.com/84284109/214068624-26cc382d-482e-490b-914f-e8ecdb3da6f0.png" width="200" /> <img src="https://user-images.githubusercontent.com/84284109/214068616-eb073217-fbbe-41d6-bb5f-9ad14720b52d.png" width="200" /> <img src="https://user-images.githubusercontent.com/84284109/214068622-3c97a9c5-f4a4-468e-91ce-9815a3677472.png" width="200" />

### Part 2: Sync (Pairing)
Then, the user who has signed up as a student will be prompted to enter the teacher's sync code.<br/>
The teacher's code will be visible after the teacher has been registered to the app.<br/><br/>

Teacher's view           |Student's view
:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/84284109/214070732-10341176-41fc-4af8-9eef-2269609baeba.png" width="200" /> |  <img src="https://user-images.githubusercontent.com/84284109/214073503-e3037689-f64a-4bc3-91f7-0a0c319f34a3.png" width="200" />

### Part 3: Post-Sync
After the student enters the teacher's code, they'll be notified that a quiz is about to begin.<br/><br/>
<ins>What the student will see after syncing:</ins>

<img src="https://user-images.githubusercontent.com/84284109/214076993-75236317-c260-48ee-b57b-4251763bd341.png" width="200" />

Upon successfully syncing, the student and teacher's workflow diverges. The teacher must make some questions (see [Part 4](#part-4-teacher)) before students can answer them at their own pace (see [Part 5](#part-5-student)).

If teacher has not created any questions, the student will see a blank screen with notification saying `You don't have any questions yet!`

### Part 4: Teacher
#### Making Questions
Before the student begins their quiz, the teacher should make sure that they have published questions for the student to answer.<br/><br/>

Add question using plus (+) button |Create question
:---------------------------------:|:----------------:
<img src="https://user-images.githubusercontent.com/84284109/214079240-60ac2028-9184-4d15-a835-8acbdd69d2fd.png" width="200" />|<img src="https://user-images.githubusercontent.com/84284109/214079817-a89df8eb-6f3c-4add-865b-769001d2e228.png" width="200" />

<ins>Afterwards, teacher should see all the questions they've made:</ins>

<img src="https://user-images.githubusercontent.com/84284109/214081017-3bc49231-97f5-4feb-a373-9e86d87507e0.png" width="200" />

### Part 5: Student
#### Interacting with Questions
After the teacher has posted some questions, the student can take an adaptive quiz with those questions.<br/>
There is a 3 minute timer for each question and a progress bar at the top of each flashcard indicating how far the student is through their quiz.<br/><br/>
<ins>Some questions (flashcards):</ins>

<img src="https://user-images.githubusercontent.com/84284109/214081790-db1d6f7d-001e-443a-9a2a-cd6189b42649.png" width="200" /> <img src="https://user-images.githubusercontent.com/84284109/214081799-0ff3ae4e-b3e2-4e61-8997-7c46c5a05c36.png" width="200" />

#### Post Quiz
After the student has finished their quiz, they will be able to see which questions they got wrong by clicking on them from the list.

<img src="https://user-images.githubusercontent.com/84284109/214083134-f0b5a609-4b67-47af-bf2c-a5db2f42dd01.png" width="200" /> <img src="https://user-images.githubusercontent.com/84284109/214083125-d960a47c-bc5f-4e8c-9509-606c7374ae39.png" width="200" />

#### Retry
The student will have the option to retake their quiz. Upon retake, questions will be reordered based on the following criteria, in decreasing order of importance:<br/><br/>
(1) incorrect questions are displayed earlier than correct ones, <br/>
(2) questions that take you longer to answer are displayed earlier than questions answered quicker, <br/>
(3) questions with a higher difficulty rating are displayed earlier than questions with lower difficulty ratings.<br/><br/>

In this sense, the quiz is "adaptive."<br/><br/>
Each time you take the quiz, your questions will be reordered such that questions you are less proficient at will appear earlier than questions you are more proficient at.<br/><br/>
So, after every iteration through the quiz, question order will be adjusted to help you improve on your weaknessess and maintain your strengths!

### Extra: How questions are reordered
We reorder questions by mainting a max-Heap structure of Question objects, where Questions with highest "priority" are stored at the "top" of the Heap.<br/><br/>
Every time new questions are posted or a student retakes a quiz, we heapify our list of Questions, and then heap sort our list such that highest priority questions (as determined by the criteria stated in [Retry](#retry)) appear first in the student's quiz.<br/><br/>
All the methods needed to heap sort are defined in our QuestionPriorityQueue class.
  
## Citations:
(Manager for card stack display/animation): https://github.com/yuyakaido/CardStackView<br/>
(Scalable size text unit): https://github.com/intuit/ssp, 
                           https://github.com/intuit/sdp