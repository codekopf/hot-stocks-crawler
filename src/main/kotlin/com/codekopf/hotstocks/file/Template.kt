package com.codekopf.hotstocks.file

class Template(private val date: String) {

    private val html = StringBuilder()
    private var content: String = ""

    fun addContent(content: String) {
        this.content = content
    }

    fun build(): String {
        startHtmlPage()
        createHead()
        endHead()
        createBody()
        createHeader()
        startMain()
        html.append(content)
        endMain()
        endBody()
        endHtmlPage()
        return html.toString()
    }

    private fun startHtmlPage() {
        html.append("<!doctype html>")
        html.append("<html lang=\"en\">")
    }

    private fun endHtmlPage() {
        html.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4\" crossorigin=\"anonymous\"></script>")
        html.append("</html>")
    }

    private fun createHead() {
        html.append("<head>")
        html.append("<meta charset=\"utf-8\">")
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
        html.append("<meta name=\"description\" content=\"\">")
        html.append("<meta name=\"author\" content=\"\">")
        html.append("<title>Hot-stocks review for $date</title>")
        html.append("<link rel=\"canonical\" href=\"\">")
        html.append("<!-- Bootstrap core CSS -->")
        html.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x\" crossorigin=\"anonymous\">")
        html.append("<!-- Bootstrap core JS -->")
        html.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>")
        html.append("<!-- Favicons -->")
        html.append("<link rel=\"apple-touch-icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/apple-touch-icon.png\" sizes=\"180x180\">")
        html.append("<link rel=\"icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/favicon-32x32.png\" sizes=\"32x32\" type=\"image/png\">")
        html.append("<link rel=\"icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/favicon-16x16.png\" sizes=\"16x16\" type=\"image/png\">")
        html.append("<link rel=\"manifest\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/manifest.json\">")
        html.append("<link rel=\"mask-icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/safari-pinned-tab.svg\" color=\"#7952b3\">")
        html.append("<link rel=\"icon\" href=\"https://getbootstrap.com/docs/5.0/assets/img/favicons/favicon.ico\">")
    }

    private fun endHead() {
        html.append("</head>")
    }

    private fun createHeader() {
        html.append("<div class=\"container\">")
        html.append("<header class=\"d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom\">")
        html.append("<ul class=\"nav col-12 col-md-auto mb-2 justify-content-center mb-md-0\">")
        html.append("<li><a href=\"#\" class=\"nav-link px-2 link-secondary\">All</a></li>")
        html.append("<li><a href=\"#\" class=\"nav-link px-2 link-dark\">Purchased</a></li>")
        html.append("<li><a href=\"#\" class=\"nav-link px-2 link-dark\">Speculative</a></li>")
        html.append("<li><a href=\"#\" class=\"nav-link px-2 link-dark\">Dividend Aristocrat</a></li>")
        html.append("</ul>")
        html.append("</header>")
        html.append(DIV_END)

        html.append("<div class=\"container\">")
        html.append("<nav>")
        html.append("<div class=\"nav nav-tabs mb-3\" id=\"nav-tab\" role=\"tablist\">")
        html.append("<button class=\"nav-link active\" id=\"nav-all-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-all\" type=\"button\" role=\"tab\" aria-controls=\"nav-all\" aria-selected=\"true\">All</button>")
        html.append("<button class=\"nav-link\" id=\"nav-daily-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-daily\" type=\"button\" role=\"tab\" aria-controls=\"nav-daily\" aria-selected=\"false\">Daily</button>")
        html.append("<button class=\"nav-link\" id=\"nav-weekly-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-weekly\" type=\"button\" role=\"tab\" aria-controls=\"nav-weekly\" aria-selected=\"false\">Weekly</button>")
        html.append("<button class=\"nav-link\" id=\"nav-monthly-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#nav-monthly\" type=\"button\" role=\"tab\" aria-controls=\"nav-monthly\" aria-selected=\"false\">Monthly</button>")
        html.append(DIV_END)
        html.append("</nav>")
        html.append(DIV_END)
    }

    private fun createBody() {
        html.append("<body>")
    }

    private fun endBody() {
        html.append("</body>")
    }

    private fun startMain() {
        html.append("<main class =\"container\">")
    }

    private fun endMain() {
        html.append("</main>")
    }

    companion object {
        private const val DIV_END = "</div>"
    }
}
