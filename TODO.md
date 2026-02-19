# Hot Stocks Crawler - TODO List

This document tracks all planned features, improvements, and known issues for the Hot Stocks Crawler project.

## Table of Contents

- [High Priority](#high-priority)
- [Code Quality & Refactoring](#code-quality--refactoring)
- [Features - Crawler](#features---crawler)
- [Features - UI/UX](#features---uiux)
- [Features - Data & Analysis](#features---data--analysis)
- [Infrastructure & DevOps](#infrastructure--devops)
- [Documentation](#documentation)
- [Non-FinViz Stocks](#non-finviz-stocks)
- [Research & Investigation](#research--investigation)

---

## High Priority

- [ ] Main function should not throw IOException - redesign the scrapper so it does not fail and kill the whole application
- [ ] Handle empty stock lists gracefully - What if ownedStocks or watchlist is empty?
- [ ] Fix empty catch block in `parseStocks()` method
- [ ] Do not ignore `file.mkdirs()` return value - handle directory creation failures

---

## Code Quality & Refactoring

### Exception Handling
- [ ] Implement proper exception handling strategy across the application
- [ ] Create custom exceptions for different error scenarios
- [ ] Add logging instead of printing stack traces

### Architecture
- [ ] Draw a high level graph/diagram of scrapper architecture
- [ ] Replace `FirefoxDriver` with WebDriver interface in `FinvizScrapper`
- [ ] Extract HTML generation into a dedicated service class
- [ ] Implement Builder pattern for HTML content generation
- [ ] Consider using template engine (Thymeleaf/Freemarker) instead of StringBuilder

### Code Improvements
- [ ] Review and fix all FIXME comments in codebase
- [ ] Review and fix all TODO comments in codebase
- [ ] Add unit tests for core functionality
- [ ] Add integration tests for scraping logic
- [ ] Implement dependency injection properly

---

## Features - Crawler

### Performance
- [ ] Implement multithreading/parallel processing for faster scraping (4 threads suggested)
  - Note: Consider being "polite" to FinViz servers - implement rate limiting
- [ ] Measure and log time statistics for crawler performance
- [ ] Implement caching for already scraped images within same day

### Driver Management
- [ ] Download GeckoDriver from official source automatically instead of bundling in repo
- [ ] Add support for ChromeDriver as alternative
- [ ] Implement headless browser mode option
- [ ] Make driver path configurable via properties file

### Scraping Enhancements
- [ ] Add retry logic for failed scraping attempts
- [ ] Implement circuit breaker pattern for handling FinViz unavailability
- [ ] Add proxy support for scraping
- [ ] Handle page load timeouts more gracefully

---

## Features - UI/UX

### Visual Improvements
- [ ] Add company logos to every stock entry
  - Logo source: `https://s3-symbol-logo.tradingview.com/{company}--big.svg`
  - Alternative: `https://www.tradingview.com/symbols/{EXCHANGE}-{TICKER}/`
- [ ] Finish adding icons to stock titles
- [ ] Add image/screenshot of how the generated report looks like to README

### Navigation
- [ ] Add alphabetical list of stock tickers as links to individual company graphs at the top
- [ ] Add links on company names to FinViz pages of individual stocks
- [ ] Add icons and names into daily, weekly and monthly tabs

### Interactivity
- [ ] Use AJAX to sort stocks by cheapest at current day
- [ ] Add search/filter functionality
- [ ] Add dark mode toggle for generated HTML

---

## Features - Data & Analysis

### Stock Categories
- [ ] Add ETF support for owned ETFs
- [ ] Add "Past ownership" indicator - stocks no longer owned
  - Examples: Pure Storage (bought and sold), USX (owned in past)
- [ ] Add industry/sector categorization (Consumer, Tech, etc.)

### Analysis Features
- [ ] Generate report of stocks that can be purchased right now
- [ ] Identify stocks below their 60-day moving average
- [ ] Identify stocks below their 200-day moving average
- [ ] Add price alerts functionality
- [ ] Export data to CSV/Excel

---

## Infrastructure & DevOps

### Configuration
- [ ] Externalize configuration to `application.properties` or `application.yml`
- [ ] Make download path configurable
- [ ] Make CSV file paths configurable
- [ ] Add environment-specific configurations (dev, prod)

### Build & Deployment
- [ ] Create Docker image for containerized deployment
- [ ] Add CI/CD pipeline (GitHub Actions)
- [ ] Create scheduled job runner (cron) for automated daily scraping
- [ ] Add health check endpoint

### Logging & Monitoring
- [ ] Implement proper logging with SLF4J/Logback
- [ ] Add log levels (DEBUG, INFO, WARN, ERROR)
- [ ] Create execution summary report after each run

---

## Documentation

- [ ] Add architecture diagram
- [ ] Add screenshots of generated output
- [ ] Document CSV file format in detail
- [ ] Add troubleshooting guide
- [ ] Add contribution guidelines
- [ ] Create changelog (CHANGELOG.md)

---

## Non-FinViz Stocks

The following stocks are not available on FinViz and need alternative data sources:

### European Stocks - Need Alternative Scraper
| Company | Exchange | Alternative Source |
|---------|----------|-------------------|
| Airbus | EU | TBD |
| CEZ a.s. | Prague | TBD |
| Deutsche Lufthansa AG | Frankfurt | [boersen-zeitung.de](https://www.boersen-zeitung.de/aktie/kurse/Deutsche-Lufthansa-AG-DE0008232125) |
| Erste Group Bank AG | Vienna | [Google Finance](https://www.google.com/finance/quote/EBS:VIE) |
| Kofola CeskoSlovensko as | Prague | TBD |
| Moneta Money Bank as | Prague | TBD |
| RWE AG | Frankfurt | [boersen-zeitung.de](https://www.boersen-zeitung.de/aktie/kurse/RWE-AG-DE0007037129) |
| Schibsted ASA | Oslo | TBD |
| Thyssenkrupp AG | Frankfurt | TBD |
| Volkswagen AG | Frankfurt | [boersen-zeitung.de](https://www.boersen-zeitung.de/aktie/kurse/Volkswagen-AG-DE0007664039) |
| Orsted | Copenhagen | TBD |

### Stocks Not Listed on FinViz
| Company | Reason |
|---------|--------|
| B4B | Not on FinViz |
| Metro AG | European exchange |
| Siemens Gamesa Renewable Energy SA | Not on FinViz |
| Nestle S.A. | Swiss company |

### Action Items
- [ ] Implement scraper for German stocks (boersen-zeitung.de)
- [ ] Implement scraper for Austrian stocks
- [ ] Implement scraper for Czech stocks (Prague Stock Exchange)
- [ ] Implement Google Finance scraper as fallback
- [ ] Create unified interface for multiple data sources

---

## Research & Investigation

- [ ] Check REIT specialty stocks at [FinViz REIT Screener](https://finviz.com/screener.ashx?v=111&f=ind_reitspecialty) for interesting opportunities
- [ ] Research legal implications of web scraping FinViz
- [ ] Investigate FinViz API availability (paid subscription)
- [ ] Explore Yahoo Finance API as alternative data source
- [ ] Research TradingView widgets for embedding charts

---

## Completed

*Move completed items here with completion date*

- [x] Basic scraping functionality - FinViz daily/weekly/monthly charts
- [x] HTML report generation with Bootstrap tabs
- [x] Multiple stock list support (portfolio, owned, watchlist)
- [x] Stock sorting by name

---

## Notes

### Scraping Etiquette
When implementing multithreading, remember to:
- Add random delays between requests
- Respect robots.txt
- Implement rate limiting
- Consider FinViz's terms of service

### Version Compatibility
- Current GeckoDriver bundled may not work with latest Firefox
- Selenium 3.7.1 is outdated - consider upgrading to Selenium 4.x
- Spring Boot 2.1.1 is outdated - consider upgrading to 2.7.x or 3.x
