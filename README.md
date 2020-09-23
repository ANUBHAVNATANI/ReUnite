# ReUnite

# Inspiration
27,356 children in Delhi went missing during 2015-2018. And 63,407 children went missing in 2016, according to data from the National Crime Records Bureau, which translates to an average of 174 every day. More disquietingly, 50% of the children who have gone missing until 2016 have stayed untraced. This is a big issue and most of this childern get caught up in trafficing or child labour.

# Solution
For solving this problem to some extent we have developed ReLice. A software solution for the problem of missing children.

  

The major problem is that there is a lot of workload on the authorities. They can’t be at every place. But the people are present almost everywhere. So the main aim of our project is to transfer this load on the authorities to the masses.

  

We are using machine learning algorithms along with a lot of Azure services. The users across the country can send images of suspected missing children which we compare to the database of reported missing children. If a match is found authorities are immediately informed.


[Logo of ReUnite](https://github.com/ANUBHAVNATANI/Relice/blob/flask-ml-server/static/logo.png)

# Tech Stack
1. Azure

2. Java Script

3. React

4. Android

5. Kotlin

6. Java

7. Python

8. Pytorch

9. Keras

10. Node Js

# Working



# Azure Services used

  

1. Azure Cognition Services - Face API

  

1. Used for extracting features from the image of the child

2. Used for comparing 2 images to detect a missing child

3. Extract FaceId from an image to ease comparisons and reduce image uploads

  

2. Azure Cosmos DB

  

1. Used to store details of a child reported(eg. Skin color, hair color etc.)

2. Used to store user details

  

3. Azure App Services

  

1. Used to Deploy the web app for reporting a missing child by the authorities

2. Used to Deploy the web app for serving the machine learning model

3. Used to Deploy the web app for the serving api calls the database.

  

  

## Steps:

  

Register a missing child -

  

1. Open link [http://relice-test.azurewebsites.net/admin/dashboard](http://relice-test.azurewebsites.net/admin/dashboard)

2. Enter user-id as ‘[admit@gmail.com](mailto:admit@gmail.com)’ and password as ‘admin’

3. Click on Register Complaint button on the left pane

4. Click on ‘Pick an image’ to open a file picker to select missing child’s image

5. Fill all the details of the missing

6. Click Submit

  

  

Check if a child is missing -

  

1. Download ReLice.apk file to an android device

2. Install the above downloaded file

3. Open the installed app ReLice

4. Login with userId ‘[test.email@gmail.com](mailto:test.email@gmail.com)’ and password ‘abcd1234’

5. A camera interface opens

6. Click the photo of a child that you suspect of being lost

7. Then press on Submit

8. The app extracts essential information from the image using Azure Congnition Services Face API

9. The app then uploads the image and initiates the check to detect if the child has been reported.

10. If found reported, the concerned authorities are notified immediately.

  

  

When a reported child is found the authorities are notified. It can be viewed in the Notifications sections on the website [http://relice-test.azurewebsites.net/admin/dashboard](http://relice-test.azurewebsites.net/admin/dashboard)

  

  

## The demo videos are present in the “Waitlisted_Demo_Videos” folder in the “Waitlisted” folder of the main repository.

  

[https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/ReactVideo.mp4](https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/ReactVideo.mp4)

  
  

Links to demo videos - [https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/Android_App_Demo_Video.mp4](https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/Android_App_Demo_Video.mp4)

  

[https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/ML_API_Demo_Video.mp4](https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/ML_API_Demo_Video.mp4)

  

[https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/API_Demo_Video.webm](https://github.com/KumarManas04/LNMHacks4-Submissions/blob/master/Waitlisted/Waitlisted_Demo_Videos/API_Demo_Video.webm)

  

  

# Edge Cases Covered

  

1. The project maintains a temporary database where queries are stored for 72 hours. It deals with the case in which a child’s photo is submitted for check before the child has been reported missing by the parents. When a child is reported missing his images are compared with the temporary database.

2. We have secured the data of the children by not storing the images of the child at all. We submit the image to the Face API of Azure and it generates a face id. Now the image can be accessed by our APIs only and not by anyone else. Even our API accesses the images for comparison via the face ids and not the actual images. This ensures total security of everyone’s data.

  

  

# Future Work

  

We can extend this to pets. We can easily use this software solution to find the missing pets. Then app would not require the police intervention and can easily works between user and consumers.

  

  

# ReliceAI!

  

  

ReliceAi is a python flask app **deployed on azure using app services** which serves the deep learning model as well as process other **Azure Cosmos DB** request.

  

  

# What it does

  

  

It provides routes to both **Azure Face API** and **FaceNet-MTCNN-blend**. We have used **Azure Face API** for face detection as well as face matching and we have used FaceNet-MTCNN-blend network for image conversion to embeddings and image similarity calculation. It also provieds routes for the file extraction from **Azure Cosmos DB**.

  

  

## Azure Face API

  

  

Azure face api is a part of **microsoft azure cognitive services** it provides face detection and face matching and many other featuers. We have used the face detection and face matching api from the cognitive services for our application.

  

  

## How the image searching works

  

  

Our model does not directly extract all the images but it first search of sutiable images based on the meta data from our user the meta data includes the age, gender, hair color, skin color, and other features. This decreases the search tree size exponentially and we get the response in very short time. Then we just match the user given image with extracted images from the database(**Azure Cosmos DB**).

  

  

## Access Model Files and API website

  

  

Model files are prototyped in jupyter notebook with different kids of deep learning frameworks like PyTorch, Keras and we have also used other types of scientific python packages.

  

  

[Relice API Link](https://reliceai.azurewebsites.net)

  

[Relice Notebook Link]([https://github.com/ANUBHAVNATANI/LNMHacks4-Submissions/blob/master/Waitlisted/DeeplearningAndAzure/ML_Models/SiameseNetwork.ipynb](https://github.com/ANUBHAVNATANI/LNMHacks4-Submissions/blob/master/Waitlisted/DeeplearningAndAzure/ML_Models/SiameseNetwork.ipynb))

  

  

# RELICE API DOCUMENTATION

Base url : [https://reliceapi.azurewebsites.net/](https://reliceapi.azurewebsites.net/)

  

## Use

This api is used to server the database queryies. Which we get from the ml server and the react server.

  

# Relice React Webapp

  

This web app is deployed on the **azure app service** link for the app is

Open link [http://relice-test.azurewebsites.net/admin/dashboard](http://relice-test.azurewebsites.net/admin/dashboard)

  

## Use

  

This web app is used by the police to register children and get statistics accordingly.


# What we learnt
We learnt advanced Machine learning and how to connect the big machine learning model to an android app. 

# What's Next
1. To Launch Android App on Playstore
2. To Improve Machine Learning Algorithm Accuracy


