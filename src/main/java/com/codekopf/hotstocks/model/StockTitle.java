package com.codekopf.hotstocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockTitle {

    private CrawlingSource crawlingSource;
    private String ticker;
    private String name;
    private String image;

    public static StockTitle of(final CrawlingSource crawlingSource, final String ticker, final String name, final String image) {
        return new StockTitle(crawlingSource, ticker, name, image);
    }
}
