import numpy as np
import pandas as pd

from tensorflow import keras
from keras.models import load_model
from pyspark.sql import Row
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
from pyspark.ml.recommendation import ALSModel
from pyspark.sql import SparkSession
from flask import Flask, request, jsonify

from forex_python.converter import CurrencyRates

import os

# Environment Variable
password = os.environ.get('PASSWORD')

# MySQL
import mysql.connector

connection = mysql.connector.connect(
    host='34.101.133.120',
    user='root',
    password=password or 'OIg.aV:Fg[[);@8Y',
    database='db_app'
)

if connection.is_connected():
    print('Connected to MySQL database')

# get recommendation from collaborative filtering
def collaborative_filtering_model(df,dataset, user_id,clfmodel):
  # Generate item recommendations for the specific user
  num_items = len(dataset['product_id'].unique())

  user_mapping = {user_id: i for i, user_id in enumerate(dataset['user_id'].unique())}
  item_mapping = {item_id: i for i, item_id in enumerate(dataset['product_id'].unique())}

  user_items = np.full((num_items,), user_mapping[user_id])
  item_ids = np.arange(num_items)

  # Predict ratings for the user-item combinations
  predictions = clfmodel.predict([user_items, item_ids]).flatten()

  # Create a DataFrame of predictions for the user
  recommendations_df = pd.DataFrame({'product_id': item_mapping.keys(), 'rating': predictions})

  # Sort the DataFrame by predicted rating in descending order
  recommendations_df = recommendations_df.sort_values('rating', ascending=False)

  # Select the top 10 recommendations
  top_10_recs = recommendations_df.nlargest(10, 'rating')
  clfrecs = top_10_recs[['product_id', 'rating']]
  clfrecs = clfrecs.rename(columns={'rating': 'clf_ratings'})
  clfinfo = pd.merge(clfrecs,df,on='product_id')
  clfinfo = clfinfo.drop_duplicates(subset='product_id', keep='first')
  clfinfo = clfinfo.drop(['user_id','add_to_cart_order','reordered','actual_price','ratings','brand'],axis=1) 
  # Identify the index of the column to move
  column_index = clfinfo.columns.get_loc('clf_ratings')

  # Create a list of column names in the desired order
  new_columns = list(clfinfo.columns[:column_index]) + list(clfinfo.columns[column_index+1:]) + ['clf_ratings']

  # Reorder the columns in the DataFrame
  clfinfo = clfinfo[new_columns]


  return clfinfo

# get recommendations from content based ratings
def content_based_model(df, dataset, user_id, cbfmodel):
  # Predict the ratings for all products in the dataset
  all_products_data = dataset[dataset['user_id'] != user_id]
  X_all = [all_products_data['category_id'], all_products_data[["discount_price", "reordered", "add_to_cart_order"]]]
  predicted_ratings = cbfmodel.predict(X_all).flatten()


  # Create a list of tuples containing product ID and predicted rating
  product_ratings = list(zip(all_products_data['product_id'], predicted_ratings))

  # Sort the product ratings by predicted rating in descending order
  product_ratings.sort(key=lambda x: x[1], reverse=True)

  # Get the top 10 recommendations without duplicate product IDs
  recommended_products = []
  recommended_product_ids = set()

  for product_id, _ in product_ratings:
      if product_id not in recommended_product_ids:
          recommended_products.append((product_id, _))
          recommended_product_ids.add(product_id)
      if len(recommended_products) >= 10:
          break

  # Filter the original dataset based on the recommended product IDs
  top_10_recommendations = all_products_data[all_products_data['product_id'].isin([p[0] for p in recommended_products])]

  # Remove duplicate product IDs from the recommendations
  top_10_recommendations = top_10_recommendations.drop_duplicates(subset='product_id')

  # Keep the top 10 recommendations (if there are more than 10 after removing duplicates)
  top_10_recommendations = top_10_recommendations.head(10)

  pred_rating = pd.DataFrame(product_ratings)
  pred_rating.columns =['product_id','cbf_ratings']
  pred_rating = pred_rating.drop_duplicates(subset='product_id', keep='first')
  pred_rating['cbf_ratings'] = pred_rating['cbf_ratings'] * (df['ratings'].max() - df['ratings'].min() ) + df['ratings'].min()
  pred_rating['cbf_ratings'] = pred_rating['cbf_ratings'].clip(lower=0.0, upper=5.0)
  cbfrecs = top_10_recommendations[['product_id', 'name','discount_price','category']]
  cbfinfo = pd.merge(cbfrecs, pred_rating, on='product_id')
  cbfinfo = cbfinfo.drop_duplicates(subset='product_id', keep='first')
  cbfinfo['discount_price'] = cbfinfo['discount_price'] * (df['discount_price'].max()- df['discount_price'].min() ) + df['discount_price'].min()

  return cbfinfo , pred_rating


# recommendingproducts id
def finaldataset(df,dataset,user_id,cbfmodel,clfmodel):
  # get final collaborative filtering top 10 result data
  clfinfo = collaborative_filtering_model(df,dataset, user_id,clfmodel)
  cbfinfo , pred_rating = content_based_model(df, dataset, user_id, cbfmodel)
  finalclf =  pd.merge(clfinfo, pred_rating, on='product_id', how='left')

  # get final content-based filtering top 10 result data
  product_ids = [x for x in cbfinfo['product_id']] 

  user_mapping = {user_id: i for i, user_id in enumerate(dataset['user_id'].unique())}
  item_mapping = {item_id: i for i, item_id in enumerate(dataset['product_id'].unique())}

  user_ids = np.full(len(product_ids), user_mapping[user_id])
  item_ids = np.array([item_mapping[product_id] for product_id in product_ids])
  predictions = clfmodel.predict([user_ids, item_ids]).flatten()
  user_product_ratings_df = pd.DataFrame({'product_id': product_ids,'clf_ratings': predictions})
  finalcbf =  pd.merge(cbfinfo, user_product_ratings_df, on='product_id', how='left')
  
  # combine the result
  combined = pd.concat([finalclf, finalcbf], axis=0)
  combined['cbf_ratings'] = combined['cbf_ratings'] * (df['ratings'].max() - df['ratings'].min() ) + df['ratings'].min()
  combined['cbf_ratings'] = combined['cbf_ratings'].clip(lower=0.0, upper=5.0)
  combined['clf_ratings'] = combined['clf_ratings'] * (df['ratings'].max() - df['ratings'].min() ) + df['ratings'].min()
  combined['clf_ratings'] = combined['clf_ratings'].clip(lower=0.0, upper=5.0)
  combined = combined.drop_duplicates(subset='product_id', keep='first')

  return combined

# def hybrid_recommend(df,dataset,user_id,cbfmodel,clfmodel,budget):
#     all_recs = finaldataset(df,dataset,user_id,cbfmodel,clfmodel)

#     # Calculate the average predicted rating for both recommendations
#     collaborative_avg_rating = np.mean(all_recs['clf_ratings'])
#     content_based_avg_rating = np.mean(all_recs['cbf_ratings'])

#     # Calculate the final recommendations based on weighted average ratings

#     print(all_recs)

#     all_recs['weighted_avg'] = (all_recs['clf_ratings'] * collaborative_avg_rating + all_recs['cbf_ratings']* content_based_avg_rating) / 2
#     hybrid_recommendations = all_recs[['product_id','weighted_avg','discount_price']].values.tolist()

#     # Sort the recommendations by rating in descending order
#     hybrid_recommendations.sort(key=lambda x: x[1], reverse=True)

#     # Filter the recommendations based on the user's budget
#     filtered_recommendations = []
#     total_cost = 0
#     for product, rating,price in hybrid_recommendations:
#         print(product, price)
#         product_cost = price
#         if total_cost + product_cost <= budget:
#             filtered_recommendations.append(product)
#             total_cost += product_cost
#         if len(filtered_recommendations) >= 10:
#             break

#     return filtered_recommendations, total_cost

def hybrid_recommend(df, dataset, user_id, cbfmodel, clfmodel, budget):
    all_recs = finaldataset(df, dataset, user_id, cbfmodel, clfmodel)

    # Calculate the average predicted rating for both recommendations
    collaborative_avg_rating = np.mean(all_recs['clf_ratings'])
    content_based_avg_rating = np.mean(all_recs['cbf_ratings'])

    # Calculate the final recommendations based on weighted average ratings
    all_recs['weighted_avg'] = (all_recs['clf_ratings'] * collaborative_avg_rating + all_recs['cbf_ratings'] * content_based_avg_rating) / 2

    hybrid_recommendations = all_recs[['product_id', 'name', 'discount_price', 'category', 'description', 'weighted_avg']].to_dict('records')

    # Sort the recommendations by rating in descending order
    hybrid_recommendations.sort(key=lambda x: x['weighted_avg'], reverse=True)

    # Filter the recommendations based on the user's budget
    filtered_recommendations = []
    total_cost = 0

    # Convert INR to IDR
    c = CurrencyRates()
    exchange_rate = c.get_rate('INR', 'IDR')

    for product in hybrid_recommendations:
        product_cost_inr = product['discount_price']
        product_cost_idr = product_cost_inr * exchange_rate

        if total_cost + product_cost_idr <= budget:
            # Update the discount price to IDR
            product['discount_price'] = product_cost_idr
            filtered_recommendations.append(product)
            total_cost += product_cost_idr
        if len(filtered_recommendations) >= 10:
            break

    output = {
        "recommendations": filtered_recommendations,
        "total": total_cost
    }

    return output


# Get the directory path of the current script (main.py)
current_dir = os.path.dirname(os.path.abspath(__file__))


app = Flask(__name__)


@app.route("/")
def hello_world():
    return jsonify({"about": "Hello World!"})

# Add an API endpoint for recommendations


@app.route('/recommendations', methods=['POST'])
def get_recommendations():
    # Load the dataset
    # Construct the absolute file path
    file_path = os.path.join(current_dir, 'Data', 'final_dataset.csv')

    # Read the CSV file into a DataFrame
    df = pd.read_csv(file_path)
    df = df.drop(["Unnamed: 0"], axis=1)

    # Load the models
    cbf_model_path = os.path.join(current_dir, 'Models', 'cbf_model.h5')
    loaded_cbf_model = load_model(cbf_model_path)
    clf_model_path = os.path.join(current_dir, 'Models', 'clf_model.h5')
    loaded_clf_model = load_model(clf_model_path)

    # Preprocess the dataset
    dataset = df.copy()
    scaler = MinMaxScaler()
    dataset[["actual_price", "discount_price", "ratings", "add_to_cart_order"]] = scaler.fit_transform(
        dataset[["actual_price", "discount_price", "ratings", "add_to_cart_order"]])
    dataset['category_id'] = LabelEncoder().fit_transform(dataset['category'])

    # Get recommendations using collaborative filtering and content-based models
    user_id = 1

    # Calculate the weighted average ratings and filter based on budget
    budget = request.json['budget']
    recommendations = hybrid_recommend(
        df, dataset, user_id, loaded_cbf_model, loaded_clf_model, budget)

    # Return the recommendations as a JSON response
    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(port=int(os.environ.get("PORT", 8080)),host='0.0.0.0',debug=True)