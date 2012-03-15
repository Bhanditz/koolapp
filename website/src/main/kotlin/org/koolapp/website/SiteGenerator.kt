package org.koolapp.website

import kotlin.io.*

import java.io.File
import org.pegdown.Extensions
import org.pegdown.PegDownProcessor
import org.pegdown.LinkRenderer

class SiteGenerator(val sourceDir: File, val outputDir: File) : Runnable {
    public var markdownProcessor: PegDownProcessor = PegDownProcessor(Extensions.ALL)
    public var linkRendered: LinkRenderer = LinkRenderer()

    override fun run() {
        println("Generating the site to $outputDir")

        sourceDir.recurse {
            if (it.isFile()) {
                var relativePath = sourceDir.relativePath(it)
                println("Processing ${relativePath}")
                var output: String? = null
                if (it.extension == "md") {
                    val text = it.readText()
                    output = markdownProcessor.markdownToHtml(text, linkRendered) ?: ""
                    relativePath = relativePath.trimTrailing(it.extension) + "html"
                } else if (it.extension == "html") {
                    output = it.readText()
                }
                val outFile = File(outputDir, relativePath)
                outFile.directory.mkdirs()
                if (output != null) {
                    val text = layout(relativePath, it, output.sure())
                    outFile.writeText(text)
                } else {
                    it.copyTo(outFile)
                }
            }
        }
    }

    /**
     * Applies a layout to the given file
     */
    fun layout(uri: String, file: File, text: String): String {
        val title = "Kotlin"
        return """
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>$title</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="apple-touch-icon" href="images/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="72x72" href="images/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="114x114" href="images/apple-touch-icon-114x114.png">
  </head>

  <body>

    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">KoolApp</a>
          <div class="nav-collapse">
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="versions/snapshot/apidocs/index.html">API</a></li>
              <li><a href="https://github.com/koolapp/koolapp">Source</a></li>
              <li><a href="http://twitter.com/#!/koolapp">@koolapp</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container">
$text
      <hr>

      <footer>
        <p>&copy; original authors 2012</p>
      </footer>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap-transition.js"></script>
    <script src="js/bootstrap-alert.js"></script>
    <script src="js/bootstrap-modal.js"></script>
    <script src="js/bootstrap-dropdown.js"></script>
    <script src="js/bootstrap-scrollspy.js"></script>
    <script src="js/bootstrap-tab.js"></script>
    <script src="js/bootstrap-tooltip.js"></script>
    <script src="js/bootstrap-popover.js"></script>
    <script src="js/bootstrap-button.js"></script>
    <script src="js/bootstrap-collapse.js"></script>
    <script src="js/bootstrap-carousel.js"></script>
    <script src="js/bootstrap-typeahead.js"></script>

  </body>
</html>
"""
    }
}