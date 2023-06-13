# Bangkit capstone project - ML (Recommender System Model)


### Summary
To optimize user's experience when using our service (BeBi app), we made a recommender system model to offer the best grocery products bundle based on the user's budget and preferences. Looking at users' historical transactions and product features, our recommender system will generate the top 10 items with the best price, quality, and ratings for the user. The model will also consider other users evaluations and ratings on each product to improve its recommendations for all users. By utilizing this model, we can give personalized recommendations for each user and assist them in making better purchase decisions when grocery shopping.

### ML Model Details
To create the recommender system we create 2 models, which are collaborative filtering recommender system (NCF model) and content based filtering recommender system. The neural collaborative filtering model consider user-product interaction as inputs and utilize neural network to generate predicted 'ratings'/ similarity score where the higher the rating , the more likely user will buy that product. The content based filtering model consider and analyze each product features to generate personalized recommendations for users based on their preference. In our case we will use neural network to create the content based filtering model, where it predicts user ratings on certain products based on the inputted features. After getting the top 10 recommendations from each model, we will combine the result from both models to generate the final top 10 recommendations based on the weighted average of the predicted ratings from both models. In the final result, we also ensure that the total cost of all items recommended will not exceed the budget that the user inputted.  

This repository mainly consists of 2 files:
1. `BeBi_(Beli_Bijak).ipynb` , which contain the whole process of developing the hybrid recommender system model
3. `Deployment_for_CC_BeBi_(Beli_Bijak).ipynb` , which contain the process of deploying the model and getting the results from the hybrid model

## 1. BeBi_(Beli_Bijak).ipynb
### A. Data Exploratory 
1. Load all the datasets required from the MyDrive folder
2. Read the csv files (dataset) as pandas dataframe
3. Explore and analyze the data : check the shapes of each dataset, count the null values, get amount of unique values, check datatypes of each column values 

### B. Data Preparation (preprocessing)
1. Drop null values and duplicate values
2. Rename columns so the column names of each dataset is synchronized
3. Drop irrelevant columns
4. Set index (product id) for each product
5. Merge datasets (combine dataset to get all the necessary informations)
6. Drop rows with product id > 1000 for data efficiency
7. Shuffle datasets 
8. Add average ratings column which specify the average rating from all users on each product
9. Add image link column 
10. Save the clean dataset (to create database for cloud computing)

### C. Modelling Process
1. Load the clean dataset and preprocess the data (scale numerical values and split the data to train and test sets)
2. Make the collaborative filtering recommender system model. for this we create a neural collaborative filtering model with 3 dense layers (64,32,16) and relu activation function. We also add flattened embedding layers for user and product to the model. After compiling the model with Adam optimizer and mean_squared_error loss function, we train the model with 10 epochs and 256 batch size settings. At the end of this section, we try to get the prediction from the model to generate the predicted ratings(similarity score) from certain user-product combinations.
3. Make the content based filtering recommender system model. for this we create a neural network model with 3 dense layer with 64 layers and relu activation function. We use the price, reordered, add_to_cart_order features and also add flattened category embeddings for the input layer. For the output layer, we add 1 dense layer with linear activation function to get the predicted ratings based on the inputs. After compiling the model with Adam optimizer and mean_squared_error loss function, we train the model with 10 epochs and 32 batch size settings. At the end of this section, we try to get prediction from the model to generate the predicted ratings from a specific user to all the available products.

### D. Evaluation
1. Neural Collaborative Filtering Model : we calculate RMSE of the test set prediction to evaluate the performance of the model. From this , we get **0.0252** RMSE value which can be considered good for value in 0 to 1 range.  
2. Content Based Filtering Model : we calculate MSE and MAE of the test set prediction to evaluate the performance of the model. From this , we get **0.0247** MSE value and **0.1113** MAE value which can be considered good for value in 0 to 1 range.        

## 2. Deployment_for_CC_BeBi_(Beli_Bijak).ipynb.ipynb
### Functions
1. collaborative_filtering_model : to get the result from NCF model and process the result to generate top 10 recommendations data which contain the product id, product name, discount price, category, and its predicted ratings from a specific user   
2. content_based_model : to get the result from content based filtering model and process the result to generate top 10 recommendations data which contain the product id, product name, discount price, category, and its predicted ratings from a specific user
3. final_dataset : to combine the top 10 recommendations from both models. this function will generate a dataframe of the top 20 product ids along with its predicted ratings from collaborative and content based model (clf_ratings and cbf_ratings). 
4. hybrid_recommend : process the combined dataframe generated from final_dataset to get the final recommendations based on the weighted average predicted ratings with total cost below the budget parameter.
5. get_recommendations: to give the final recommendations to user based on the user id and budget. this function is added as an API endpoint for recommendations.
