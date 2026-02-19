package com.codekopf.hotstocks.model

data class Stock(
    val crawlingSource: CrawlingSource,
    val ticker: String,
    val name: String,
    val image: String,
)
