package com.sulton.belibijak.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RecommendationsResponse(

	@field:SerializedName("total")
	val total: Any? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)

data class RecommendationsItem(

	@field:SerializedName("weighted_avg")
	val weightedAvg: Double? = null,

	@field:SerializedName("discount_price")
	val discountPrice: Double? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class BudgetPost(
	@field:SerializedName("budget")
	val budget: Double? = null
)
