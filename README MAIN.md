# BeBi : Grocery Shopping Manager App for Sustainable Lifestyle

# Table of Contents

0. [Table of Contents](#table)
1. [About BeBi](#about)
   1. [Background](#background)
   2. [Goal & Aim](#goal)
   3. [Plans & Realization](#plans)
   4. [Repository & Branch](#repo)
   5. [Google Cloud Tools and Monthly Pricing](#pricing)
   6. [Bibliography](#bibliography)
   7. [Contributing Developers](#dev)

<br>
<a name="about"></a>

# About BeBi

**BeBi** is a mobile app that provides assitance on grocery shopping. BeBi offers various grocery bundles with the best price and quality for our users based on their preferences and budget. The main feature of our app is the monthly bundle feature which provides users with personalized top grocery products recommendations so they can stock up on their favorite/usual monthly groceries without going over budget. Overall, BeBi is a useful app that can help user in making better purchasing decisions when grocery shopping and getting good deals on all the grocery products they need within budget. By using our app, user can save time and money while developing a more sustainable lifestyle by reducing wasteful purchases.           

<br>

<a name="background"></a>

## 1. Background

Many people spend a lot of time browsing to find good deals on products and often end up wasting money due to over buying or making wrong purchases. This becomes a great issue during grocery shopping since grocery products are perishable. We came up with this project to solve these problems by making an app that helps manage users’ grocery shopping activities. Considering that people do grocery shopping routinely and food waste is a huge concern these days, we believe that our app can be useful to save users’ money and time while promoting a sustainable lifestyle by reducing waste. 

We also consider that for busy people like working moms , grocery shopping might be a necessary but tedious task to be done. Therefore, we offer monthly grocery bundles which consists of top grocery products personalized for each users based on their budget and preferences. By using this app, our user can save time and easily acquire their monthly groceries within budget.         

<br>

<a name="goal"></a>

## 2. Goal & Aim

- This app aims to help customers in their monthly grocery stock-up by acquiring all the grocery products they need with the best price and ratings within a set budget. 
- BeBi has a built-in recommender system which generate/offers personalized grocery product bundles for users based on their preferences and historical purchases.     

<br>

<a name="downloadandhost"></a>


## 3. Plans & Realization

The development plan of this project can be seen in the Gantt Chart in the picture provided or [for more information can click here to view](https://bit.ly/C22FV01-ProjectSchedule)
<img src="https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator/blob/ml-development/image/gann_chart.png" width="950" />

As the development has finished, these are the points conducted from the plan & realization:

1. First week

   a. Project planning (break down tasks and discussed how we should complete this project)
   
   b. User research and analysis (surveys to optimize user's experience) 
   
   c. Creating MVP (figma) for the UI design.
   
   d. Data gathering & Data Exploration/Analysis

2. Second week

   a. Data preparation/preprocessing
   
   b. ML model development (create 2 recommender system models, data training, and test/evaluate the model)
   
   c. Continue with designing the UI for the mobile app 
   
   d. Create database API. 

3. Third week

   a. Combining 2 recommender systems & process the result of both to create a hybrid recommender system which generates top products recommendations 
   
   b. Deploying the ML model & Create APIs 
  
4. Fourth week

   a. API testing
  
   b. Integrating API with Mobile Application

5. Fifth week 

   a. Application Integration
  
   b. Make project brief and documentation
  
   c. Prepare go-to-market proposal and project presentation.

<br>

<a name="repo"></a>

## 4. Repository & Branch

The **BeBi Repository** consisted of:

- **Cloud-Computing folder** (cc api development)

  The folder contain routes/ directory contains files that define the routes for different endpoints in your application. For example, the index.js file may define routes for the main application, while users.js may define routes for user-related operations like login, registration, etc. The controllers/ directory contains files that define the controllers for handling the logic of each route. The controllers interact with the models to retrieve or manipulate data and send appropriate responses to the client. The models/ directory contains files that define the data models for your application. These models typically represent the entities in your system and provide methods for interacting with the underlying data store (e.g., a database). The views/ directory is where you can store your application's view templates. These templates are typically rendered by the controllers and sent as responses to the client. The app.js file is the main entry point of your application. It's where you initialize the Express app, set up middleware, configure routes, and start the server.

- **Mobile-Development folder** (mobile app development)

  The folder contains ... (SISA PUNYA MD)
  

- **Machine-Learning folder** (ml model development)

  The folder has python notebook file which contain the code for developing the hybrid recommender system from collaborative filtering and content based filtering models. It has a .py file which contain the code for  ML model deployment (API endpoint for giving the recommendations). It also contain 2 model files (.h5 format) that has been downloaded from the python notebook, clean dataset of products and transactions that is used for the training, and requirement.txt which contain all the packages and its required version to run the model.    

- **Main Branch files**
  This repository mainly consist of this 2 files: 
  1. `BeBi_(Beli_Bijak).ipynb` , which contain the whole process of developing the hybrid recommender system model
  2. `Deployment_for_CC_BeBi_(Beli_Bijak).ipynb` , which contain the process of deploying the model and getting the results from the hybrid model

<br>

<a name="pricing"></a>

## 5. Google Cloud Tools and Monthly Pricing

Below are the tools used for deployment, and its detail of monthly pricing.


| Name             | Detail                                                       | Type                              | Monthly Pricing Est. |
| ---------------- | ------------------------------------------------------------ | --------------------------------- | -------------------- |
| Google Cloud SQL | Database-related functions to create, read, update, delete data | db-standard-1. 10 GiB SSD storage | $104                 |
| Google Cloud Run | Handle the request-related  from users                       | 0.25 GiB Memory, per 10k requests | $20                   |


<br>

<a name="bibliography"></a>

## 6. Bibliography

### A. Framework, Library, and external repository/API used:

- [Flask](https://flask.palletsprojects.com/en/2.1.x/) & [Flask Restful](https://flask-restful.readthedocs.io/en/latest/)
- [TensorFlow](https://tensorflow.org/)
- [Pandas](https://pandas.pydata.org/docs/)
- [NumPy](https://numpy.org/doc/stable/)
- [Scikit-Learn](https://scikit-learn.org/stable/)
- [Node.js](https://nodejs.org/en/docs)
- [Express](https://devdocs.io/express/)
- [Cloud Run](https://cloud.google.com/run/docs)
- [Cloud SQL](https://cloud.google.com/sql/docs)
- sisa punya md

<br>

<a name="dev"></a>

## 7. Contributing developers

**Bangkit 2023 | Product Based-Capstone| C23-PS069 Team Members**:

- M151DSX0106 – Arya Sujiwakusuma – Brawijaya University - [Active]
- M151DSY0104 – Jocelin Helsa – Brawijaya University - [Active]
- M097DKX3881 – Guardian Theo Andritya – STIKI Malang - [Active]
- C151DSY1611 – Ina Khoirunisa – Brawijaya University - [Active]
- C166DKY4154 – Maharani Swastika – Diponegoro University - [Active]
- A097DSX2101 – Achmad Sulton – STIKI Malang - [Active]

