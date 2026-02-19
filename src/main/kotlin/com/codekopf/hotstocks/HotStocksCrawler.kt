package com.codekopf.hotstocks

import com.codekopf.hotstocks.file.Template
import com.codekopf.hotstocks.model.CrawlingSource
import com.codekopf.hotstocks.model.Stock
import com.codekopf.hotstocks.service.FinvizScrapper
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object HotStocksCrawler {

    const val DOWNLOAD_FILE_PATH = "C:\\hotstocks\\"

    private const val SEMICOLON = ';'
    private const val SCRAPER = "SCRAPER"
    private const val TICKER = "TICKER"
    private const val NAME = "NAME"
    private const val IMAGE = "IMAGE"
    private const val NOTE = "NOTE"
    private val HEADER = arrayOf(SCRAPER, TICKER, NAME, IMAGE, NOTE)

    private const val HTML_BR = "<br>"
    private const val DIV_END = "</div>"
    private const val H2_TITLE_PORTFOLIO_STOCKS = "<h2>Portfolio stocks!</h2>"
    private const val H2_TITLE_OWNED_STOCKS = "<h2>Owned stocks!</h2>"
    private const val H2_TITLE_SPECULATIVE_STOCKS = "<h2>Speculative stocks!</h2>"
    private const val IMG_START_WITH_SOURCE = "<img src=\"img"
}

fun main() {
    val today = LocalDate.now()
    val scrappingDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(today)

    // Init WebDriver
    System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver\\geckodriver.exe")

    val portfolio = parseStocks(File("C:\\DEV\\hot-stocks-crawler\\src\\main\\resources\\portfolio.csv").readText())
    val owned = parseStocks(File("C:\\DEV\\hot-stocks-crawler\\src\\main\\resources\\owned.csv").readText())
    val watchlist = parseStocks(File("C:\\DEV\\hot-stocks-crawler\\src\\main\\resources\\watchlist.csv").readText())

    val content = StringBuilder()

    content.append("<div class=\"tab-content\" id=\"nav-tabContent\">")

    // All
    content.append("<div class=\"tab-pane fade show active\" id=\"nav-all\" role=\"tabpanel\" aria-labelledby=\"nav-all-tab\">")
    content.append("<h1>All Stocks!</h1>")
    content.append(H2_TITLE_PORTFOLIO_STOCKS)
    portfolio.forEach { addStockTitleToContent(content, it) }
    content.append(H2_TITLE_OWNED_STOCKS)
    owned.forEach { addStockTitleToContent(content, it) }
    content.append(H2_TITLE_SPECULATIVE_STOCKS)
    watchlist.forEach { addStockTitleToContent(content, it) }
    content.append(DIV_END)

    // Daily
    content.append("<div class=\"tab-pane fade\" id=\"nav-daily\" role=\"tabpanel\" aria-labelledby=\"nav-daily-tab\">")
    content.append("<h1>Daily</h1>")
    content.append(H2_TITLE_PORTFOLIO_STOCKS)
    portfolio.forEach { appendStockChart(content, it, "-d") }
    content.append(H2_TITLE_OWNED_STOCKS)
    owned.forEach { appendStockChart(content, it, "-d") }
    content.append(H2_TITLE_SPECULATIVE_STOCKS)
    watchlist.forEach { appendStockChart(content, it, "-d") }
    content.append(DIV_END)

    // Weekly
    content.append("<div class=\"tab-pane fade\" id=\"nav-weekly\" role=\"tabpanel\" aria-labelledby=\"nav-weekly-tab\">")
    content.append("<h1>Weekly</h1>")
    content.append(H2_TITLE_PORTFOLIO_STOCKS)
    portfolio.forEach { appendStockChart(content, it, "-w") }
    content.append(H2_TITLE_OWNED_STOCKS)
    owned.forEach { appendStockChart(content, it, "-w") }
    content.append(H2_TITLE_SPECULATIVE_STOCKS)
    watchlist.forEach { appendStockChart(content, it, "-w") }
    content.append(DIV_END)

    // Monthly
    content.append("<div class=\"tab-pane fade\" id=\"nav-monthly\" role=\"tabpanel\" aria-labelledby=\"nav-monthly-tab\">")
    content.append("<h1>Monthly</h1>")
    content.append(H2_TITLE_PORTFOLIO_STOCKS)
    portfolio.forEach { appendStockChart(content, it, "-m") }
    content.append(H2_TITLE_OWNED_STOCKS)
    owned.forEach { appendStockChart(content, it, "-m") }
    content.append(H2_TITLE_SPECULATIVE_STOCKS)
    watchlist.forEach { appendStockChart(content, it, "-m") }
    content.append(DIV_END)

    // Tab ends
    content.append(DIV_END)

    val outputDir = File("${HotStocksCrawler.DOWNLOAD_FILE_PATH}$scrappingDate")
    outputDir.mkdirs()
    val outputFile = File(outputDir, "index.html")
    BufferedWriter(FileWriter(outputFile)).use { writer ->
        val html = Template(scrappingDate)
        html.addContent(content.toString())
        writer.write(html.build())
    }

    val finvizScrapper = FinvizScrapper(scrappingDate)
    finvizScrapper.crawl(portfolio)
    finvizScrapper.crawl(owned)
    finvizScrapper.crawl(watchlist)
}

private const val HTML_BR = "<br>"
private const val DIV_END = "</div>"
private const val H2_TITLE_PORTFOLIO_STOCKS = "<h2>Portfolio stocks!</h2>"
private const val H2_TITLE_OWNED_STOCKS = "<h2>Owned stocks!</h2>"
private const val H2_TITLE_SPECULATIVE_STOCKS = "<h2>Speculative stocks!</h2>"
private const val IMG_START_WITH_SOURCE = "<img src=\"img"

private fun parseStocks(fileContent: String): List<Stock> {
    return try {
        val format = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setHeader(*arrayOf("SCRAPER", "TICKER", "NAME", "IMAGE", "NOTE"))
            .setSkipHeaderRecord(true)
            .build()
        CSVParser(StringReader(fileContent), format).use { csvParser ->
            csvParser.records
                .map { record ->
                    Stock(
                        crawlingSource = CrawlingSource.valueOf(record.get("SCRAPER").trim()),
                        ticker = record.get("TICKER").trim(),
                        name = record.get("NAME").trim(),
                        image = record.get("IMAGE").trim(),
                    )
                }
                .sortedBy { it.name }
        }
    } catch (e: Exception) {
        emptyList()
    }
}

private fun addStockTitleToContent(content: StringBuilder, stock: Stock) {
    content.append("<img src=\"").append(stock.image).append("\">").append(stock.name)
    content.append(HTML_BR)
    content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.ticker).append("-d.png").append("\">")
    content.append(HTML_BR).append(HTML_BR)
    content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.ticker).append("-w.png").append("\">")
    content.append(HTML_BR).append(HTML_BR)
    content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.ticker).append("-m.png").append("\">")
    content.append(HTML_BR).append(HTML_BR)
}

private fun appendStockChart(content: StringBuilder, stock: Stock, suffix: String) {
    content.append("<img src=\"").append(stock.image).append("\">").append(stock.name)
    content.append(HTML_BR)
    content.append(IMG_START_WITH_SOURCE).append("\\").append(stock.ticker).append(suffix).append(".png").append("\">")
    content.append(HTML_BR).append(HTML_BR)
}
