package com.codekopf.hotstocks.service

import com.codekopf.hotstocks.HotStocksCrawler
import com.codekopf.hotstocks.model.Stock
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.Point
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom
import javax.imageio.ImageIO

/**
 * Selenium-based scraper that captures stock chart screenshots from FinViz.
 *
 * @param scrappingDate date in yyyy-MM-dd string format for which the scrapping is happening
 */
class FinvizScrapper(private val scrappingDate: String) {

    fun crawl(stocks: List<Stock>) {
        val options = FirefoxOptions().apply {
            // Use EAGER strategy to prevent the driver from hanging on slow trackers or ads
            setPageLoadStrategy(PageLoadStrategy.EAGER)

            // Disable Tracking Protection to prevent 'NetworkError' from crashing the Privacy Manager
            addPreference("privacy.trackingprotection.enabled", false)
            addPreference("dom.webnotifications.enabled", false)
            addPreference("geo.enabled", false)
            addPreference("media.navigator.enabled", false)

            // Stabilize connection timeout
            addPreference("network.http.connection-timeout", 20)
        }

        // Suppress Selenium logs
        java.util.logging.Logger.getLogger("org.openqa.selenium").level = java.util.logging.Level.OFF

        val driver = FirefoxDriver(options)

        try {
            // Set window size via the driver management API
            driver.manage().window().size = Dimension(1920, 1080)

            // Set a generous timeout to handle Finviz's heavy initial load
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20))
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))

            // Navigate to homepage
            driver.get("https://finviz.com/")

            // Critical: Wait for the browser to settle to avoid 'uninitialized' state errors
            letThreadSleep()

            try {
                // Try to dismiss the consent button if it appears
                val button = driver.findElement(By.className("Button__StyledButton-buoy__sc-a1qza5-0"))
                button.click()
            } catch (_: Exception) {
                // Ignore if not present or not clickable
            }

            // Set the cookie directly - Set preference cookie via JS
            (driver as JavascriptExecutor).executeScript(
                "document.cookie = 'chartsTheme=light; path=/; max-age=31536000';"
            )

            val wait = WebDriverWait(driver, Duration.ofSeconds(20))

            // Wait for the chart theme UI to be ready
            wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("[data-testid='chart-layout-theme']")
                )
            )

            for (stock in stocks) {
                try {
                    val ticker = stock.ticker

                    // Daily
                    driver.get("https://finviz.com/quote.ashx?t=$ticker&ty=c&ta=1&p=d&tas=0")
                    getGraph(driver, wait, ticker, GraphType.DAILY)

                    // Weekly
                    driver.get("https://finviz.com/quote.ashx?t=$ticker&ty=c&ta=1&p=w&tas=0")
                    getGraph(driver, wait, ticker, GraphType.WEEKLY)

                    // Monthly
                    driver.get("https://finviz.com/quote.ashx?t=$ticker&ty=c&ta=1&p=m&tas=0")
                    getGraph(driver, wait, ticker, GraphType.MONTHLY)

                    letThreadSleep()
                } catch (ex: Exception) {
                    System.err.println("Error while crawling ${stock.ticker}: ${ex.message}")
                }
            }
        } finally {
            // Ensure driver is closed even if an exception occurs
            driver.quit()
        }
    }

    private fun getGraph(driver: FirefoxDriver, wait: WebDriverWait, stockTicker: String, graphType: GraphType) {
        val webElements = wait.until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("interactive-chart"))
        )

        if (webElements.isEmpty()) return
        val webElement = webElements[0]
        letThreadSleep(1000) // Give chart time to render SVG

        val screenshotFile = driver.getScreenshotAs(OutputType.FILE)
        val fullImage = ImageIO.read(screenshotFile)

        // Get the location of element on the page
        val point = webElement.location

        // Get width and height of the element
        val webElementWidth = webElement.size.width
        val webElementHeight = webElement.size.height

        // Crop the entire page screenshot to get only element screenshot
        val elementScreenshot = captureSubImage(fullImage, point, 0, webElementWidth, webElementHeight)
        ImageIO.write(elementScreenshot, "png", screenshotFile)

        // Copy the element screenshot to disk
        val suffix = when (graphType) {
            GraphType.DAILY -> "-d"
            GraphType.WEEKLY -> "-w"
            GraphType.MONTHLY -> "-m"
        }
        val screenshotLocation = createNewStockImageGraphFile(stockTicker, suffix)
        screenshotFile.copyTo(screenshotLocation, overwrite = true)
    }

    private fun createNewStockImageGraphFile(stockTicker: String, graphImageSuffix: String): File {
        return File("${HotStocksCrawler.DOWNLOAD_FILE_PATH}$scrappingDate\\img\\$stockTicker$graphImageSuffix.png")
    }

    internal fun captureSubImage(
        fullImg: BufferedImage,
        point: Point,
        scrollPosition: Int,
        elementWidth: Int,
        elementHeight: Int,
    ): BufferedImage {
        return fullImg.getSubimage(point.x, point.y - scrollPosition, elementWidth, elementHeight)
    }

    /**
     * Helper method which will let the thread sleep for the given amount of time.
     *
     * @param sleepingTime The time in milliseconds that the thread will sleep.
     */
    private fun letThreadSleep(sleepingTime: Long) {
        try {
            Thread.sleep(sleepingTime)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Thread.currentThread().interrupt()
        }
    }

    /**
     * Helper method which will let the thread sleep for a random amount of time between 1 and 3 seconds.
     */
    private fun letThreadSleep() {
        val sleepingTime = ThreadLocalRandom.current().nextLong(1000, 3001)
        try {
            Thread.sleep(sleepingTime)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private enum class GraphType {
        DAILY,
        WEEKLY,
        MONTHLY,
    }
}
