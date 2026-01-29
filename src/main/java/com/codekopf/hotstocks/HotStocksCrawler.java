package com.codekopf.hotstocks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.codekopf.hotstocks.file.Template;
import com.codekopf.hotstocks.model.CrawlingSource;
import com.codekopf.hotstocks.model.Stock;
import com.codekopf.hotstocks.service.FinvizScrapper;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.FileUtils;

public class HotStocksCrawler {

    public static final String DOWNLOAD_FILE_PATH = "C:\\hotstocks\\";

    private static final char SEMICOLON = ';';
    private static final String SCRAPER = "SCRAPER";
    private static final String TICKER = "TICKER";
    private static final String NAME = "NAME";
    private static final String IMAGE = "IMAGE";
    private static final String NOTE = "NOTE";
    private static final String[] HEADER = {
        SCRAPER,
        TICKER,
        NAME,
        IMAGE,
        NOTE
    };

    private static final String HTML_BR = "<br>";
    private static final String DIV_END = "</div>";
    private static final String H2_TITLE_PORTFOLIO_STOCKS = "<h2>Portfolio stocks!</h2>";
    private static final String H2_TITLE_OWNED_STOCKS = "<h2>Owned stocks!</h2>";
    private static final String H2_TITLE_SPECULATIVE_STOCKS = "<h2>Speculative stocks!</h2>";
    private static final String IMG_START_WITH_SOURCE = "<img src=\"img";


    public static void main(String[] args) throws IOException { // TODO abuday - main function should not throw IOException - redesign the scrapper so it does not  fail and kill the whole application

        LocalDate today = LocalDate.now().atStartOfDay().toLocalDate();
        String scrappingDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(today);

        // Init WebDriver
        System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver\\geckodriver.exe");

        val portfolio = parseStocks(FileUtils.readFileToString(new File("C:\\DEV\\hot-stocks-crawler\\src\\main\\resources\\portfolio.csv")));

        val owned = parseStocks(FileUtils.readFileToString(new File("C:\\DEV\\hot-stocks-crawler\\src\\main\\resources\\owned.csv")));

        val watchlist = parseStocks(FileUtils.readFileToString(new File("C:\\DEV\\hot-stocks-crawler\\src\\main\\resources\\watchlist.csv")));

        // TODO abuday - What if ownedStocks or watchlist should be empty list or throw exception
        //  How to handle exception? What to do if there is empty list?

        StringBuilder content = new StringBuilder();

        content.append("<div class=\"tab-content\" id=\"nav-tabContent\">");



        // All
        content.append("<div class=\"tab-pane fade show active\" id=\"nav-all\" role=\"tabpanel\" aria-labelledby=\"nav-all-tab\">");

        content.append("<h1>All Stocks!</h1>");

        content.append(H2_TITLE_PORTFOLIO_STOCKS);

        for (val stock : portfolio) {
            addStockTitleToContent(content, stock);
        }

        content.append(H2_TITLE_OWNED_STOCKS);

        for (val stock : owned) {
            addStockTitleToContent(content, stock);
        }

        content.append(H2_TITLE_SPECULATIVE_STOCKS);

        for (val speculative : watchlist) {
            addStockTitleToContent(content, speculative);
        }

        content.append(DIV_END);





        // Daily
        content.append("<div class=\"tab-pane fade\" id=\"nav-daily\" role=\"tabpanel\" aria-labelledby=\"nav-daily-tab\">");

        content.append("<h1>Daily</h1>");

        content.append(H2_TITLE_PORTFOLIO_STOCKS);

        for (val stock : portfolio) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-d.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(H2_TITLE_OWNED_STOCKS);

        for (val stock : owned) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-d.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(H2_TITLE_SPECULATIVE_STOCKS);

        for (val stock : watchlist) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-d.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(DIV_END);





        // Weekly
        content.append("<div class=\"tab-pane fade\" id=\"nav-weekly\" role=\"tabpanel\" aria-labelledby=\"nav-weekly-tab\">");

        content.append("<h1>Weekly</h1>");

        content.append(H2_TITLE_PORTFOLIO_STOCKS);

        for (val stock : portfolio) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-w.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(H2_TITLE_OWNED_STOCKS);

        for (val stock : owned) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-w.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(H2_TITLE_SPECULATIVE_STOCKS);

        for (val stock : watchlist) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-w.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(DIV_END);




        // Monthly
        content.append("<div class=\"tab-pane fade\" id=\"nav-monthly\" role=\"tabpanel\" aria-labelledby=\"nav-monthly-tab\">");

        content.append("<h1>Monthly</h1>");

        content.append(H2_TITLE_PORTFOLIO_STOCKS);

        for (val stock : portfolio) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-m.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(H2_TITLE_OWNED_STOCKS);

        for (val stock : owned) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-m.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(H2_TITLE_SPECULATIVE_STOCKS);

        for (val stock : watchlist) {
            content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
            content.append(HTML_BR);
            content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-m.png").append("\">");
            content.append(HTML_BR).append(HTML_BR);
        }

        content.append(DIV_END);

        // Tab ends
        content.append(DIV_END);

        File file = new File(DOWNLOAD_FILE_PATH + scrappingDate);
        file.mkdirs(); // TODO abuday - do not ignore this
        file = new File(DOWNLOAD_FILE_PATH + scrappingDate + "\\index.html");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        Template html = new Template(scrappingDate);
        html.addContent(content.toString());
        bufferedWriter.write(html.build());
        bufferedWriter.close();

        // tabs - https://getbootstrap.com/docs/5.0/components/navs-tabs/#tabs

        // view-source:https://getbootstrap.com/docs/5.0/examples/headers/

//        <div class="container">
//    <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
//      <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
//        <svg class="bi me-2" width="40" height="32"><use xlink:href="#bootstrap"/></svg>
//        <span class="fs-4">Simple header</span>
//      </a>
//
//      <ul class="nav nav-pills">
//        <li class="nav-item"><a href="#" class="nav-link active" aria-current="page">Home</a></li>
//        <li class="nav-item"><a href="#" class="nav-link">Features</a></li>
//        <li class="nav-item"><a href="#" class="nav-link">Pricing</a></li>
//        <li class="nav-item"><a href="#" class="nav-link">FAQs</a></li>
//        <li class="nav-item"><a href="#" class="nav-link">About</a></li>
//      </ul>
//    </header>
//  </div>

//
//    <!-- Custom styles for this template -->
//    <link href="headers.css" rel="stylesheet">
//
//      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
//        <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
//          <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"/></svg>
//        </a>
//
//        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
//          <li><a href="#" class="nav-link px-2 text-secondary">Home</a></li>
//          <li><a href="#" class="nav-link px-2 text-white">Features</a></li>
//          <li><a href="#" class="nav-link px-2 text-white">Pricing</a></li>
//          <li><a href="#" class="nav-link px-2 text-white">FAQs</a></li>
//          <li><a href="#" class="nav-link px-2 text-white">About</a></li>
//        </ul>
//
//        <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
//          <input type="search" class="form-control form-control-dark" placeholder="Search..." aria-label="Search">
//        </form>
//
//        <div class="text-end">
//          <button type="button" class="btn btn-outline-light me-2">Login</button>
//          <button type="button" class="btn btn-warning">Sign-up</button>
//        </div>
//      </div>

        // TODO measure time stats how fast the the crawler goes

        FinvizScrapper finvizScrapper = new FinvizScrapper(scrappingDate);
        finvizScrapper.crawl(portfolio);
        finvizScrapper.crawl(owned);
        finvizScrapper.crawl(watchlist);
    }

    private static List<Stock> parseStocks(String fileContent) {
        try (val csvParser = new CSVParser(new StringReader(fileContent), CSVFormat.DEFAULT.withDelimiter(SEMICOLON).withHeader(HEADER).withSkipHeaderRecord())) {
            return csvParser.getRecords()
                .stream()
                .map(csvRecord -> Stock.of(
                    CrawlingSource.valueOf(csvRecord.get(SCRAPER).trim()),
                    csvRecord.get(TICKER).trim(),
                    csvRecord.get(NAME).trim(),
                    csvRecord.get(IMAGE).trim()
                    )
                )
                .sorted(Comparator.comparing(Stock::getName))
                .collect(Collectors.toList());
        } catch (IOException e) {
            // TODO abuday - empty catch block
        }
        return Collections.emptyList();
    }

    private static void addStockTitleToContent(final StringBuilder content, final Stock stock) {
        content.append("<img src=\"").append(stock.getImage()).append("\">").append(stock.getName());
        content.append(HTML_BR);
        content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-d.png").append("\">");
        content.append(HTML_BR).append(HTML_BR);
        content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-w.png").append("\">");
        content.append(HTML_BR).append(HTML_BR);
        content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.getTicker()).append("-m.png").append("\">");
        content.append(HTML_BR).append(HTML_BR);
    }

}
