package com.codekopf.hotstocks.file;

public class Template {

    private static final String DIV_END = "</div>";

    StringBuilder html;
    String content;
    String date;
    
    public Template(final String date) {
        this.html = new StringBuilder();
        this.content = "";
        this.date = date;
    }
    
    private void startHTMLPage() {
        this.html.append("<!doctype html>");
        this.html.append("<html lang=\"en\">");
    }

    private void endHTMLPage() {
        this.html.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4\" crossorigin=\"anonymous\"></script>");
        this.html.append("</html>");
    }

    private void createHEAD() {
        this.html.append("<head>");
        this.html.append("<meta charset=\"utf-8\">");
        this.html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        this.html.append("<meta name=\"description\" content=\"\">"); // TODO: Fix
        this.html.append("<meta name=\"author\" content=\"\">"); // TODO: Fix
        this.html.append("<title>Hot-stocks review for ").append(this.date).append("</title>");
        this.html.append("<link rel=\"canonical\" href=\"\">"); // TODO: Fix
        this.html.append("<!-- Bootstrap core CSS -->");
        this.html.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x\" crossorigin=\"anonymous\">");
        this.html.append("<!-- Bootstrap core JS -->");
        this.html.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>");
        this.html.append("<!-- Favicons -->");
        // TODO: Fix
        this.html.append("<link rel=\"apple-touch-icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/apple-touch-icon.png\" sizes=\"180x180\">");
        this.html.append("<link rel=\"icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/favicon-32x32.png\" sizes=\"32x32\" type=\"image/png\">");
        this.html.append("<link rel=\"icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/favicon-16x16.png\" sizes=\"16x16\" type=\"image/png\">");
        this.html.append("<link rel=\"manifest\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/manifest.json\">");
        this.html.append("<link rel=\"mask-icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/safari-pinned-tab.svg\" color=\"#7952b3\">");
        this.html.append("<link rel=\"icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/favicon.ico\">");
    }

    private void endHEAD() {
        this.html.append("</head>");
    }

    private void createHEADER() {
        this.html.append("<div class=\"container\">");
        this.html.append("<header class=\"d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom\">");
        this.html.append("<ul class=\"nav col-12 col-md-auto mb-2 justify-content-center mb-md-0\">");
        this.html.append("<li><a href=\"#\" class=\"nav-link px-2 link-secondary\">All</a></li>");
        this.html.append("<li><a href=\"#\" class=\"nav-link px-2 link-dark\">Purchased</a></li>");
        this.html.append("<li><a href=\"#\" class=\"nav-link px-2 link-dark\">Speculative</a></li>");
        this.html.append("<li><a href=\"#\" class=\"nav-link px-2 link-dark\">Dividend Aristocrat</a></li>");
        this.html.append("</ul>");
        this.html.append("</header>");
        this.html.append(DIV_END);

        this.html.append("<div class=\"container\">");
        this.html.append("<nav>");
        this.html.append("<div class=\"nav nav-tabs mb-3\" id=\"nav-tab\" role=\"tablist\">");
        this.html.append("<button class=\"nav-link active\" id=\"nav-all-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-all\" type=\"button\" role=\"tab\" aria-controls=\"nav-all\" aria-selected=\"true\">All</button>");
        this.html.append("<button class=\"nav-link\" id=\"nav-daily-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-daily\" type=\"button\" role=\"tab\" aria-controls=\"nav-daily\" aria-selected=\"false\">Daily</button>");
        this.html.append("<button class=\"nav-link\" id=\"nav-weekly-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-weekly\" type=\"button\" role=\"tab\" aria-controls=\"nav-weekly\" aria-selected=\"false\">Weekly</button>");
        this.html.append("<button class=\"nav-link\" id=\"nav-weekly-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-monthly\" type=\"button\" role=\"tab\" aria-controls=\"nav-monthly\" aria-selected=\"false\">Monthly</button>");
        this.html.append(DIV_END);
        this.html.append("</nav>");
        this.html.append(DIV_END);
    }

    private void createBODY() {
        this.html.append("<body>");
    }

    private void endBODY() {
        this.html.append("</body>");
    }

    private void startMAIN() {
        this.html.append("<main class =\"container\">");
    }

    private void endMAIN() {
        this.html.append("</main>");
    }

    /**
     * Set content of the body. Content needs to be in HTML format.
     * Content will be placed between <body></body> tags in HTML.
     * @param content - HTML content
     */
    public void setContent(final String content) {
        this.content = content;
    }

    public String build() {
        startHTMLPage();
        createHEAD();
        endHEAD();
        createBODY();
        createHEADER();
        startMAIN();
        this.html.append(this.content);
        endMAIN();
        endBODY();
        endHTMLPage();
        return this.html.toString();
    }

}
