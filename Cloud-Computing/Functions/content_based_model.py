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
