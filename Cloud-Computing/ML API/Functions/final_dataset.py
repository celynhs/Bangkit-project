# recommendingproducts id
def final_dataset(df, dataset, user_id, cbfmodel, clfmodel):
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
