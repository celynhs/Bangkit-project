from pyspark.sql import SparkSession
from pyspark.sql import Row
import numpy as np
import pandas as pd

# Create a SparkSession without a specific application name
spark = SparkSession.builder.getOrCreate()

# Set configuration to allow multiple contexts
spark.conf.set("spark.driver.allowMultipleContexts", "true")


def collaborative_filtering_model(df, user_id, clfmodel):
    # Get top 10 recommendations for all users (personalized for each user)
    usersRecs = clfmodel.recommendForAllUsers(10)

    # Get recommendations for a specified user
    user_recommendations = usersRecs.filter(
        usersRecs.user_id == user_id).select("recommendations").collect()[0][0]

    # reformat the item recommendations for user
    # convert recommendations to a pd dataframe
    clfrecs = pd.DataFrame(user_recommendations)
    # give column names to dataframe
    clfrecs.columns = ['product_id', 'pred_ratings']
    # merge with df to get product info
    clfinfo = pd.merge(clfrecs, df, on='product_id')
    # drop rows with same product id
    clfinfo = clfinfo.drop_duplicates(subset='product_id', keep='first')
    # drop irrelevant columns
    clfinfo = clfinfo.drop(
        ['user_id', 'add_to_cart_order', 'reordered', 'actual_price', 'ratings'], axis=1)

    # Move columns to the correct order
    column_index = clfinfo.columns.get_loc('pred_ratings')
    new_columns = list(clfinfo.columns[:column_index]) + \
        list(clfinfo.columns[column_index+1:]) + ['pred_ratings']
    clfinfo = clfinfo[new_columns]

    return clfinfo

# get recommendations from content based ratings


def content_based_model(df, dataset, user_id, cbfmodel):
    # Predict the ratings for all products in the dataset
    all_products_data = dataset[dataset['user_id'] != user_id]
    X_all = [all_products_data['category_id'], all_products_data[[
        "discount_price", "reordered", "add_to_cart_order"]]]
    predicted_ratings = cbfmodel.predict(X_all).flatten()

    # Create a list of tuples containing product ID and predicted rating
    product_ratings = list(
        zip(all_products_data['product_id'], predicted_ratings))

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
    top_10_recommendations = all_products_data[all_products_data['product_id'].isin(
        [p[0] for p in recommended_products])]

    # Remove duplicate product IDs from the recommendations
    top_10_recommendations = top_10_recommendations.drop_duplicates(
        subset='product_id')

    # Keep the top 10 recommendations (if there are more than 10 after removing duplicates)
    top_10_recommendations = top_10_recommendations.head(10)

    pred_rating = pd.DataFrame(product_ratings)
    pred_rating.columns = ['product_id', 'cbf_ratings']
    pred_rating = pred_rating.drop_duplicates(
        subset='product_id', keep='first')
    pred_rating['cbf_ratings'] = pred_rating['cbf_ratings'] * \
        (df['ratings'].max() - df['ratings'].min()) + df['ratings'].min()
    pred_rating['cbf_ratings'] = pred_rating['cbf_ratings'].clip(
        lower=0.0, upper=5.0)
    cbfrecs = top_10_recommendations[[
        'product_id', 'name', 'discount_price', 'category']]
    cbfinfo = pd.merge(cbfrecs, pred_rating, on='product_id')
    cbfinfo = cbfinfo.drop_duplicates(subset='product_id', keep='first')
    cbfinfo['discount_price'] = cbfinfo['discount_price'] * \
        (df['discount_price'].max() - df['discount_price'].min()) + \
        df['discount_price'].min()

    return cbfinfo, pred_rating


# recommendingproducts id
def finaldataset(df, dataset, user_id, cbfmodel, clfmodel):
    # get final collaborative filtering top 10 result data
    clfinfo = collaborative_filtering_model(df, user_id, clfmodel)
    cbfinfo, pred_rating = content_based_model(df, dataset, user_id, cbfmodel)
    finalclf = pd.merge(clfinfo, pred_rating, on='product_id', how='left')

    # get final content-based filtering top 10 result data
    # product id of top 10 items
    product_ids = [x for x in cbfinfo['product_id']]
    data = [Row(user_id=user_id, product_id=product_id)
            for product_id in product_ids]
    dfs = spark.createDataFrame(data)
    predicted_ratings = clfmodel.transform(dfs)
    predicted_ratings_pd = predicted_ratings.toPandas()
    cbf_prod_pred = predicted_ratings_pd[['product_id', 'prediction']]
    finalcbf = pd.merge(cbfinfo, cbf_prod_pred, on='product_id', how='left')
    finalcbf.rename(columns={'prediction': 'clf_ratings'}, inplace=True)

    # combine the result
    combined = pd.concat([finalclf, finalcbf], axis=0)

    return combined


def hybrid_recommend(df, dataset, user_id, cbfmodel, clfmodel, budget):
    all_recs = finaldataset(df, dataset, user_id, cbfmodel, clfmodel)

    # Calculate the average predicted rating for both recommendations
    collaborative_avg_rating = np.mean(all_recs['clf_ratings'])
    content_based_avg_rating = np.mean(all_recs['cbf_ratings'])

    # Calculate the final recommendations based on weighted average ratings

    all_recs['weighted_avg'] = (all_recs['clf_ratings'] * collaborative_avg_rating +
                                all_recs['cbf_ratings'] * content_based_avg_rating) / 2
    hybrid_recommendations = all_recs[[
        'product_id', 'weighted_avg', 'discount_price']].values.tolist()

    # Sort the recommendations by rating in descending order
    hybrid_recommendations.sort(key=lambda x: x[1], reverse=True)

    # Filter the recommendations based on the user's budget
    filtered_recommendations = []
    total_cost = 0
    for product, rating, price in hybrid_recommendations:
        product_cost = price
        if total_cost + product_cost <= budget:
            filtered_recommendations.append(product)
            total_cost += product_cost
        if len(filtered_recommendations) >= 10:
            break

    return filtered_recommendations, total_cost
