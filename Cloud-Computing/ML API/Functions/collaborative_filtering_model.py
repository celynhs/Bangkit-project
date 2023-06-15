# get recommendation from collaborative filtering
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
