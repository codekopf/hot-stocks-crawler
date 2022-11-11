# Hot Stocks Crawler

This scrapper does not scrap any information from the website. It scraps only visual graph from the FinViz website.

The aim of this scrapper is to provide quick overview on situation of the stocks. They can be either owned stocks or the one I am interested in.

## Installation

On Windows, you must place `geckodriver.exe` file located in `resource\geckodriver` folder into the `C:\drivers\geckodriver\geckodriver.exe` folder. Run the main method in `HotStocksApplication.java` and hope for the best.

## Importing project to IDE

### IntelliJ IDEA
If you import this project to IntelliJ IDE, use `New > Project from Version Control`. This is Maven project and import helps you to set up project faster.

## TODO
+ draw a high level graph of scrapper architecture
+ Add a logo to every stock? 
  + Logo source https://s3-symbol-logo.tradingview.com/amc-entertainment-holdings--big.svg
  + Logo source https://www.tradingview.com/symbols/NYSE-AMC/
+ Add ETFs I have - ETF support ?
+ Multithreading ? parallel - from book - multithreading - 4 threads -> However it might not be "polite"
+ Gecko drivers download from official source, not this repo
+ Check fix me and todos
+ Add image of how it looks like
+ Finish adding icons to stock titles
+ Add alphabetical list of stock tickers as links to individual company graphs on the top
