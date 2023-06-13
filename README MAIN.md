# BeBi : Grocery Shopping Manager App for Sustainable Lifestyle

# Table of Contents

0. [Table of Contents](#table)
1. [About BeBi](#about)
   1. [Background](#background)
   2. [Goal & Aim](#goal)
   3. [How to Download and Host](#downloadandhost)
      1. [Run BeBi recommender system on local](#local)
      2. [Deploy on Google Cloud Platform](#gcp)
      3. [Deploy on Heroku](#heroku)
   4. [Plans & Realization](#plans)
   5. [Repository & Branch](#repo)
   6. [Google Cloud Tools and Monthly Pricing](#pricing)
   7. [Bibliography](#bibliography)
   8. [Contributing Developers](#dev)

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

## 3. How to Download and Host

This section contains the steps to run StarWord Web on local device and environment, then how to deploy it to Google Cloud Platform or Heroku. Those three steps will be explained with several short codes below.

<br>

<a name="local"></a>

### A. Run StarWord Web on Local

#### 	1. Clone this repository

```
https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator.git
```

#### 	2. Go to the main branch and install virtualenv

```
pip install virtualenv
```

#### 	3. Create and name a virtual environment

```
python -m venv <name of environment>
```

#### 	4. Activate the environment

```
<name of environment>\Scripts\activate
```

#### 	5. Install all required packages for this Web

```
pip install -r requirements.txt
```

#### 	6. Add debug mode to keep reloading server (only for development)

```
if __name__ == "__main__":
  app.run(debug=True)
```

#### 	7. Run the Web server

```
python run.py
```

Now, your own StarWord website is ready to serve!



<br>

<a name="gcp"></a>

### B. Deploy on Google Cloud Platform

#### 	1. Create a project in GCP and enable billing for this project

#### 	2. Enable the Cloud Run Admin API. Open the GCP Console then on the left in the sidebar menu select > APIs &

#### 		Services > Library

#### 	3. Activate Cloud Shell then clone this repository

```
https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator.git
```

#### 	4. Go to the main branch and create Dockerfile below

```
# Use the official lightweight Python image.
# https://hub.docker.com/_/python
FROM python:3.8

# Allow statements and log messages to immediately appear in the Knative logs
ENV PYTHONUNBUFFERED True

# Copy local code to the container image.
ENV APP_HOME /app
WORKDIR $APP_HOME
COPY . ./

# Install production dependencies.
RUN pip install -r requirements.txt

# Run the web service on container startup. Here we use the gunicorn
# webserver, with one worker process and 8 threads.
# For environments with multiple CPU cores, increase the number of workers
# to be equal to the cores available.
# Timeout is set to 0 to disable the timeouts of the workers to allow Cloud Run to handle instance scaling.
CMD exec gunicorn --bind :$PORT --workers 1 --threads 8 --timeout 0 run:app
```

#### 	5. Open the Editor Console then on the bottom click Cloud Code

#### 	6. Deploy to Cloud Run

<br>

<a name="heroku"></a>

### C. Deploy on Heroku

#### 	1. Create an account on Heroku

#### 	2. Install the Heroku CLI

#### 	3. Clone this repository

```
https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator.git
```

#### 	4. Go to the main branch

#### 	5. Login to the Heroku CLI

```
$ heroku login -i
```

#### 	6. Create Heroku apps from the CLI

```
$ heroku create <name of apps>
```

#### 	7. Create a Procfile file

```
web: gunicorn run:app
```

#### 	8. Initialize a Git repository in a new or existing directory

```
$ git init
$ heroku git:remote -a <name of apps>
```

#### 	9. Commit the code to the repository and deploy it to Heroku using Git

```
$ git add .
$ git commit -am "make it better"
$ git push heroku master
```

<br>

<a name="plans"></a>
## 4. Plans & Realization

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

   a. Combining 2 recommender system & process the result of both to create a hybrid recommender system which generate top products recommendations 
   
   b. Deploying the ML model & Create APIs 
   
   c. 
  
4. Fourth week

   a. API testing
  
   b. Mobile Application Development
  
   c. 
  

5. Fifth week 

   a. Application Integration
  
   b. Make project brief and documentations
  
   c. Prepare go-to market proposal and project presentation.

<br>

<a name="repo"></a>

## 5. Repository & Branch

The **BeBi Repository** is divided into **3 branches** (including master). Below is the explanation:

- **API Development Branch** (cc-development)

  The cc-development branch is the branch for back-end (dedicated for ONLY API) development. It is written in Python by using the flask and flask restful framework.

  See the full documentation and the routings configuration, as well as their input and output [here](https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator/tree/cc-development). 

- **Machine Learning Development Branch** (ml-development)

  The ML branch is the branch for machine learning development. It is written in Python, and it uses most basic libraries such as Pandas, NumPy, Sci-Kit Learn, and TensorFlow. Full documentation of the Machine Learning project with steps to make the model is clearly explained in the readme.md file of the Machine Learning development branch, along with the pipeline testing of each model.

  See the full documentation and the model development process from scratch [here](https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator/tree/ml-development).

- **Main Branch** (main)

  The Main branch is used as the integration branch of CC-development and ML-development. The plan is to use the Flask framework to build and integrate the website and machine learning model as a whole website. In the main branch lies the front-end website which only uses monolithic Flask to consume the API made from the cc-development branch. 

<br>

<a name="pricing"></a>

## 6. Google Cloud Tools and Monthly Pricing

Below are the tools used for deployment, and its detail of monthly pricing.

| Name              | Detail                                                       | Type                              | Monthly Pricing Est. |
| ----------------- | ------------------------------------------------------------ | --------------------------------- | -------------------- |
| Google Cloud SQL  | Database-related functions to create, read, update, delete data | db-standard-1. 10 GiB SSD storage | $42                  |
| Google App Engine | Hosting the website                                          | F2                                | $133                 |
| Google Cloud Run  | Handle the request-related  from users                       | 0.25 GiB Memory, per 10k requests | $5                   |



<br>

<a name="bibliography"></a>

## 7. Bibliography

### A. Framework, Library, and external repository/API used:

- [Flask](https://flask.palletsprojects.com/en/2.1.x/) & [Flask Restful](https://flask-restful.readthedocs.io/en/latest/)
- [TensorFlow](https://tensorflow.org/)
- [Pandas](https://pandas.pydata.org/docs/)
- [NumPy](https://numpy.org/doc/stable/)
- [Scikit-Learn](https://scikit-learn.org/stable/)
- []

<br>

<a name="dev"></a>

## 8. Contributing developers

**Bangkit 2023 | Product Based-Capstone| C23-PS069 Team Members**:

- M151DSX0106 – Arya Sujiwakusuma – Brawijaya University - [Active]
- M151DSY0104 – Jocelin Helsa – Brawijaya University - [Active]
- M097DKX3881 – Guardian Theo Andritya – STIKI Malang - [Active]
- C151DSY1611 – Ina Khoirunisa – Brawijaya University - [Active]
- C166DKY4154 – Maharani Swastika – Diponegoro University - [Active]
- A097DSX2101 – Achmad Sulton – STIKI Malang - [Active]

