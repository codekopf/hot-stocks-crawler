package com.codekopf.hotstocks.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codekopf.hotstocks.HotStocksCrawler;
import com.codekopf.hotstocks.model.StockTitle;

public class FinvizScrapper {

    private final String scrappingDate;

    /**
     * @param scrappingDate - scrappingDate in yyyy-mm-dd string format for which the scrapping is happening
     */
    public FinvizScrapper(final String scrappingDate) {
        this.scrappingDate = scrappingDate;
    }

    public void crawl(List<StockTitle> stockTitles) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-width=1920");
        options.addArguments("-height=1080");

        FirefoxDriver driver = new FirefoxDriver(options);
        driver.get("https://finviz.com/");

        letThreadSleep(1000);

        WebElement button = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/button[3]"));
        button.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("qc-cmp2-container")));
        driver.executeScript("return arguments[0].remove();", menu);

        try {
            for (StockTitle stockTitle : stockTitles) {
                try {
                    String stockTicker = stockTitle.getTicker();
                    driver.get("https://finviz.com/quote.ashx?t=" + stockTicker + "&ty=c&ta=1&p=d&tas=0");
                    getGraph(driver, wait, stockTicker, GraphType.DAILY);
                    driver.get("https://finviz.com/quote.ashx?t=" + stockTicker + "&ty=c&ta=1&p=w&tas=0");
                    getGraph(driver, wait, stockTicker, GraphType.WEEKLY);
                    driver.get("https://finviz.com/quote.ashx?t=" + stockTicker + "&ty=c&ta=1&p=m&tas=0");
                    getGraph(driver, wait, stockTicker, GraphType.MONTHLY);
                } catch (Throwable t) {
                    System.out.println("Error while crawling " + stockTitle.getTicker() + ": " + t.getMessage());
                }
            }
        } finally {
            driver.quit();
        }

    }

    private void getGraph(FirefoxDriver driver, WebDriverWait wait, String stockTicker, GraphType graphType) throws IOException { // TODO replace FirefoxDrive to interface

        var webElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("interactive-chart")));

        if (webElements.isEmpty()) {
            return;
        }
        var webElement = webElements.get(0);
        // Long scrollYPos = (Long) driver.executeScript("arguments[0].scrollIntoView(true); return window.scrollY;", webElement);
        letThreadSleep(500);

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

    // An enum that is used to determine the type of graph that is being scraped.
    private enum GraphType {
        DAILY,
        WEEKLY,
        MONTHLY
    }

}
