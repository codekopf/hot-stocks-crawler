[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=bugs)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=codekopf_hot-stocks-crawler&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=codekopf_hot-stocks-crawler)  

# Hot Stocks Crawler

A web scraper that captures visual stock graphs from the FinViz website for quick portfolio overview and analysis.

> **Note:** This scraper does not extract any data from the website. It only captures visual chart images for stocks you own or are interested in.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Minimum Requirements](#minimum-requirements)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
  - [GeckoDriver Setup](#geckodriver-setup)
  - [Firefox Browser](#firefox-browser)
- [Build and Deploy](#build-and-deploy)
  - [Building the Project](#building-the-project)
  - [Running the Application](#running-the-application)
  - [Output](#output)
- [Configuration](#configuration)
  - [Stock Lists](#stock-lists)
  - [CSV Format](#csv-format)
- [IDE Setup](#ide-setup)
  - [IntelliJ IDEA](#intellij-idea)
- [Project Structure](#project-structure)
- [License](#license)

## Overview

The aim of this scraper is to provide a quick visual overview of stock situations. It generates an HTML report with daily, weekly, and monthly charts for:

- **Portfolio stocks** - Stocks you currently hold in larger quantities
- **Owned stocks** - Stocks you own in smaller quantities
- **Watchlist stocks** - Stocks you are interested in monitoring

## Features

- Automated scraping of stock charts from FinViz
- Daily, weekly, and monthly chart views
- Generated HTML report with Bootstrap-based tabbed interface
- Support for multiple stock lists (portfolio, owned, watchlist)
- Alphabetically sorted stock listings
- Company logos integration

## Minimum Requirements

| Requirement | Version |
|-------------|---------|
| Java JDK | 11 or higher |
| Maven | 3.6+ |
| Firefox Browser | Latest stable |
| GeckoDriver | Compatible with Firefox version |
| Operating System | Windows 10/11 |
| RAM | 4GB minimum (8GB recommended) |
| Disk Space | 500MB for application + space for scraped images |

## Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11 | Core programming language |
| Spring Boot | 2.1.1.RELEASE | Application framework (parent POM) |
| Selenium WebDriver | 3.7.1 | Browser automation for web scraping |
| GeckoDriver | - | Firefox WebDriver implementation |
| JSoup | 1.11.3 | HTML parsing |
| Apache Commons IO | 1.3.2 | File operations utilities |
| Apache Commons CSV | 1.4 | CSV file parsing |
| Google Guava | 27.0.1-jre | Utility library |
| Lombok | 1.18.22 | Boilerplate code reduction |
| JavaMail | 1.4.7 | Email functionality |
| Bootstrap | 5.0 | Frontend UI framework (generated HTML) |

## Installation

### GeckoDriver Setup

1. Download GeckoDriver from the [official releases page](https://github.com/mozilla/geckodriver/releases)
2. Or use the bundled version located in `resource\geckodriver` folder
3. Place `geckodriver.exe` in `C:\drivers\geckodriver\geckodriver.exe`

### Firefox Browser

Ensure Firefox browser is installed on your system. Download from [mozilla.org](https://www.mozilla.org/firefox/).

## Build and Deploy

### Building the Project

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/hot-stocks-crawler.git
   cd hot-stocks-crawler
   ```

2. **Build with Maven:**
   ```bash
   mvn clean package
   ```
   This creates a fat JAR with all dependencies at `target/lunchtime-0.0.1-SNAPSHOT.jar`

3. **Build without running tests:**
   ```bash
   mvn clean package -DskipTests
   ```

### Running the Application

**Option 1: Run from IDE**
- Open project in IntelliJ IDEA
- Run `HotStocksCrawler.java` main method

**Option 2: Run compiled JAR**
```bash
java -jar target/lunchtime-0.0.1-SNAPSHOT.jar
```

**Option 3: Run with Maven**
```bash
mvn exec:java -Dexec.mainClass="com.codekopf.hotstocks.HotStocksCrawler"
```

### Output

The scraper generates output in the following location:
```
C:\hotstocks\{YYYY-MM-DD}\
    index.html          # Main HTML report
    img\                # Directory with scraped chart images
        {TICKER}-d.png  # Daily chart
        {TICKER}-w.png  # Weekly chart
        {TICKER}-m.png  # Monthly chart
```

Open `index.html` in a browser to view the generated report.

## Configuration

### Stock Lists

Stock lists are defined in CSV files located in `src/main/resources/`:

| File | Purpose |
|------|---------|
| `portfolio.csv` | Main portfolio holdings |
| `owned.csv` | Smaller/secondary holdings |
| `watchlist.csv` | Stocks you're monitoring |
| `watchlist-not-on-finviz.csv` | European/non-US stocks not available on FinViz |

### CSV Format

Each CSV file uses semicolon (`;`) as delimiter with the following columns:

```csv
SCRAPER;TICKER;NAME;IMAGE;NOTE
FINVIZ;AAPL;Apple Inc.;https://logo-url.com/aapl.svg;Tech giant
```

| Column | Description |
|--------|-------------|
| SCRAPER | Source identifier (currently `FINVIZ`) |
| TICKER | Stock ticker symbol |
| NAME | Company name |
| IMAGE | URL to company logo |
| NOTE | Optional notes |

## IDE Setup

### IntelliJ IDEA

1. Select `File > New > Project from Version Control`
2. Enter the repository URL
3. IntelliJ will automatically detect Maven and configure the project
4. Enable annotation processing for Lombok:
   - Go to `Settings > Build, Execution, Deployment > Compiler > Annotation Processors`
   - Check "Enable annotation processing"
5. Install Lombok plugin if prompted

## Project Structure

```
hot-stocks-crawler/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/codekopf/hotstocks/
│       │       ├── HotStocksCrawler.java    # Main entry point
│       │       ├── file/
│       │       │   └── Template.java        # HTML template generator
│       │       ├── model/
│       │       │   ├── Stock.java           # Stock data model
│       │       │   └── CrawlingSource.java  # Source enum
│       │       └── service/
│       │           └── FinvizScrapper.java  # FinViz scraping logic
│       └── resources/
│           ├── portfolio.csv
│           ├── owned.csv
│           ├── watchlist.csv
│           ├── watchlist-not-on-finviz.csv
│           └── geckodriver/
│               └── geckodriver.exe
├── pom.xml
├── readme.md
├── TODO.md
└── LICENSE
```

## License

This project is licensed under the terms specified in the [LICENSE](LICENSE) file.
