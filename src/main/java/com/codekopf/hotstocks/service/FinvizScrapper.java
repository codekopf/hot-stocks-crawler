package com.codekopf.hotstocks.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codekopf.hotstocks.HotStocksCrawler;
import com.codekopf.hotstocks.model.Stock;

public class FinvizScrapper {

    private final String scrappingDate;

    /**
     * @param scrappingDate - scrappingDate in yyyy-mm-dd string format for which the scrapping is happening
     */
    public FinvizScrapper(final String scrappingDate) {
        this.scrappingDate = scrappingDate;
    }

    public void crawl(final List<Stock> stocks) {
        FirefoxOptions options = new FirefoxOptions();

        // Use EAGER strategy to prevent the driver from hanging on slow trackers or ads
        // This proceeds as soon as the DOM is ready.
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        // Disable Tracking Protection to prevent 'NetworkError' from crashing the Privacy Manager
        options.addPreference("privacy.trackingprotection.enabled", false);
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("geo.enabled", false);
        options.addPreference("media.navigator.enabled", false);

        // Stabilize connection timeout
        options.addPreference("network.http.connection-timeout", 20);

        // Suppress Selenium logs
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);

        FirefoxDriver driver = new FirefoxDriver(options);

        try {
            // Set window size via the driver management API instead of unrecognized flags
            driver.manage().window().setSize(new Dimension(1920, 1080));

            // Set a generous timeout to handle Finviz's heavy initial load
            driver.manage().timeouts().pageLoadTimeout(20L, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);

            // Navigate to homepage
            driver.get("https://finviz.com/");

            // Critical: Wait for the browser to settle to avoid 'uninitialized' state errors
            letThreadSleep();

            try {
                // Try to dismiss the consent button if it appears
                WebElement button = driver.findElement(By.className("Button__StyledButton-buoy__sc-a1qza5-0"));
                button.click();
            } catch (Exception e) {
                // Ignore if not present or not clickable
            }

            // Set the cookie directly - Set preference cookie via JS
            ((JavascriptExecutor) driver).executeScript(
                    "document.cookie = 'chartsTheme=light; path=/; max-age=31536000';"
            );

            WebDriverWait wait = new WebDriverWait(driver, 20);

            // Wait for the chart theme UI to be ready
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("[data-testid='chart-layout-theme']")
            ));

            for (var stock : stocks) {
                try {
                    var ticker = stock.getTicker();

                    // Daily Navigate to individual quotes
                    driver.get("https://finviz.com/quote.ashx?t=" + ticker + "&ty=c&ta=1&p=d&tas=0");
                    getGraph(driver, wait, ticker, GraphType.DAILY);

                    // letThreadSleep();

                    // Weekly
                    driver.get("https://finviz.com/quote.ashx?t=" + ticker + "&ty=c&ta=1&p=w&tas=0");
                    getGraph(driver, wait, ticker, GraphType.WEEKLY);

                    // letThreadSleep();

                    // Monthly
                    driver.get("https://finviz.com/quote.ashx?t=" + ticker + "&ty=c&ta=1&p=m&tas=0");
                    getGraph(driver, wait, ticker, GraphType.MONTHLY);

                    letThreadSleep();

                } catch (Exception ex) {
                    System.err.println("Error while crawling " + stock.getTicker() + ": " + ex.getMessage());
                }
            }
        } finally {
            // Ensure driver is closed even if an exception occurs
            driver.quit();
        }

    }

    private void getGraph(FirefoxDriver driver, WebDriverWait wait, String stockTicker, GraphType graphType) throws IOException { // TODO replace FirefoxDrive to interface

        var webElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("interactive-chart")));

        if (webElements.isEmpty()) {
            return;
        }
        var webElement = webElements.get(0);
        letThreadSleep(1000); // Give chart time to render SVG

        var screenshotFile = driver.getScreenshotAs(OutputType.FILE);
        var fullImage = ImageIO.read(screenshotFile);

        // Get the location of element on the page
        var point = webElement.getLocation();

        // Get width and height of the element
        var webElementWidth = webElement.getSize().getWidth();
        var webElementHeight = webElement.getSize().getHeight();

        // Crop the entire page screenshot to get only element screenshot
        // scrollYPos.intValue()
        var elementScreenshot = captureSubImage(fullImage, point, 0, webElementWidth, webElementHeight);
        ImageIO.write(elementScreenshot, "png", screenshotFile);

        // Copy the element screenshot to disk
        File screenshotLocation;
        switch (graphType) {
            case DAILY:
                screenshotLocation = createNewStockImageGraphFile(stockTicker, "-d");
                break;
            case WEEKLY:
                screenshotLocation = createNewStockImageGraphFile(stockTicker, "-w");
                break;
            case MONTHLY:
                screenshotLocation = createNewStockImageGraphFile(stockTicker, "-m");
                break;
            default:
                throw new IllegalArgumentException("Unknown graph type!");
        }
        FileUtils.copyFile(screenshotFile, screenshotLocation);
    }

    private File createNewStockImageGraphFile(String stockTicker, String graphImageSuffix) {
        return new File(HotStocksCrawler.DOWNLOAD_FILE_PATH + scrappingDate + "\\img\\" + stockTicker + graphImageSuffix + ".png");
    }

    protected BufferedImage captureSubImage(BufferedImage fullImg, Point point, int scrollPosition, int elementWidth, int elementHeight) {
        return fullImg.getSubimage(point.getX(), point.getY() - scrollPosition, elementWidth, elementHeight);
    }

    /**
     * Helper method which will let the thread upon which is called sleep for the given amount of time.
     *
     * @param sleepingTime The time in milliseconds that the thread will sleep.
     */
    private void letThreadSleep(int sleepingTime) {
        try {
            Thread.sleep(sleepingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Helper method which will let the thread upon which is called sleep for a
     * random amount of time between 2 and 7 seconds.
     */
    private void letThreadSleep() {
        // Random time between 1000ms and 3000ms
        int sleepingTime = ThreadLocalRandom.current().nextInt(1000, 3001);
        try {
            Thread.sleep(sleepingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    // An enum that is used to determine the type of graph that is being scraped.
    private enum GraphType {
        DAILY,
        WEEKLY,
        MONTHLY
    }

}
