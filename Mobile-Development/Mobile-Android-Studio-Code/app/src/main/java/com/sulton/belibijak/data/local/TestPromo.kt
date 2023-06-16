package com.sulton.belibijak.data.local

import com.sulton.belibijak.data.remote.RecommendationsResponse
import com.sulton.belibijak.data.slideData

object TestPromo {
    val carouselHome = listOf(
        slideData("Fresh Product","Always provide best quality ingredients","https://drive.google.com/uc?export=view&id=1KH779VrqcsAKzynF-Zx2UIIDQtT7_3u8"),
        slideData("Fresh Product","Always provide best quality ingredients","https://drive.google.com/uc?export=view&id=1KH779VrqcsAKzynF-Zx2UIIDQtT7_3u8"),
        slideData("Fresh Product","Always provide best quality ingredients","https://drive.google.com/uc?export=view&id=1KH779VrqcsAKzynF-Zx2UIIDQtT7_3u8")

    )


}
data class DataRekomend(
    val title: String? = null,
    val imageUrl : String? = null,
    var recomended : RecommendationsResponse? = null
)

