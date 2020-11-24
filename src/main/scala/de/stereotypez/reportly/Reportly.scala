package de.stereotypez.reportly

import org.apache.commons.text.StringEscapeUtils
import plotly._
import plotly.layout._

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

/**
 * @author ${user.name}
 */

object Reportly {
  def apply() = new Reportly()
}

class Reportly() {

  trait Renderable {
    def render(divId: String = java.util.UUID.randomUUID().toString): String
  }

  class MarkdownContainer(md: String) extends Renderable {
    override def render(divId: String = java.util.UUID.randomUUID().toString): String = {
      s"<div id='$divId' class='markdown-body'></div><script>(function () {document.getElementById('$divId').innerHTML = marked('${StringEscapeUtils.escapeEcmaScript(md)}')})();</script>"
    }
  }

  class TraceContainer(traces: Seq[plotly.Trace], layout: Layout, config: Config) extends Renderable {
    override def render(divId: String = java.util.UUID.randomUUID().toString): String = {
      s"<div id='$divId'></div><script>${Plotly.jsSnippet(divId, traces, layout, config)}</script>"
    }
  }

  private val elements = ArrayBuffer[Renderable]()

  private def markedMinJs: String = {
    Using(Source.fromResource("marked.min.js")) { file =>
      file.getLines.mkString
    }.getOrElse("")
  }

  private def markdownCSS = {
    Using(Source.fromResource("github-markdown.min.css")) { file =>
      file.getLines.mkString
    }.getOrElse("")
  }

  def add(traces: Seq[plotly.Trace], layout: Layout, config: Config = Config()): Reportly = {
    elements.append(new TraceContainer(traces, layout, config))
    this
  }


  def add(markdown: String): Reportly = {
    elements.append(new MarkdownContainer(markdown))
    this
  }

  def render(title: String): String = {
    s"""<!DOCTYPE html>
       |<html>
       |<head>
       |<title>$title</title>
       |<script>${Plotly.plotlyMinJs}</script>
       |<script>${markedMinJs}</script>
       |<style>${markdownCSS}</style>
       |</head>
       |<body>
       |${elements.toSeq.map(_.render()).mkString("\n")}
       |</body>
       |</html>
       """.stripMargin
  }
}