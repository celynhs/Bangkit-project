package com.sulton.belibijak.data.remote

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("post_code")
	val postCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("budget")
	val budget: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class SignIn(
	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null,
)

data class ResponseReg(
	@field:SerializedName("message")
	val  message: String? = null
)

data class SignUp(
	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = "user",

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("post_code")
	val postCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("budget")
	val budget: String? = null,

)

