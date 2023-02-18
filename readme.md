# Hot Stocks Crawler

This scrapper does not scrap any information from the website. It scraps only visual graph from the FinViz website.

The aim of this scrapper is to provide quick overview on situation of the stocks. They can be either owned stocks or the one I am interested in.

## Installation

On Windows, you must place `geckodriver.exe` file located in `resource\geckodriver` folder into the `C:\drivers\geckodriver\geckodriver.exe` folder. Run the main method in `HotStocksApplication.java` and hope for the best.

## Importing project to IDE

### IntelliJ IDEA
If you import this project to IntelliJ IDE, use `New > Project from Version Control`. This is Maven project and import helps you to set up project faster.

## TODO - Crawler
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
+ Use AJAX to sort by cheapest stock at current day
+ Past ownership - I do not own X anymore
+ List of companies by me which can not be scrapped by info at FINVIZ:
  + Airbus
  + CEZ a.s.
  + Deutsche Lufthansa AG - https://www.boersen-zeitung.de/aktie/kurse/Deutsche-Lufthansa-AG-DE0008232125
  + Erste Group Bank AG - https://www.google.com/finance/quote/EBS:VIE?sa=X&window=5Y ; https://www.boersen-zeitung.de/unternehmen/kurse/AT0000652011
  + Kofola CeskoSlovensko as
  + Moneta Money Bank as
  + RWE AG - https://www.boersen-zeitung.de/aktie/kurse/RWE-AG-DE0007037129
  + Schibsted ASA
  + Stock Spirits Group PLC
  + Thyssenkrupp AG - ??????????????????
  + Volkswagen AG - https://www.boersen-zeitung.de/aktie/kurse/Volkswagen-AG-DE0007664039
  + Orsted
+ FINVIZ does not show all the stocks I have
  + B4B; 
  + Metro AG; 
  + Siemens Gamesa Renewable Energy SA - not at FINVIZ
  + Nestlé S.A.; - Swiss company, not at FINVIZ;

## TODO - Stocks
+ Check https://finviz.com/screener.ashx?v=111&f=ind_reitspecialty REIT section for interesting
