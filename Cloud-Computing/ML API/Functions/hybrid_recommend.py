def hybrid_recommend(df, dataset, user_id, cbfmodel, clfmodel, budget):
    all_recs = final_dataset(df, dataset, user_id, cbfmodel, clfmodel)

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
