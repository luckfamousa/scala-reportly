package samples

import java.io.PrintWriter

import org.junit._
import Assert._
import de.stereotypez.reportly.Reportly
import plotly.element.{Color, Fill, Marker}
import plotly.{Bar, Scatter}
import plotly.layout.Layout

import scala.util.Using

@Test
class ReportlyTest {

  @Test
  def testOK(): Unit = {

    assertNotNull({

      val labels = Seq("Palta", "Arango", "Avocado")
      val columns = Seq("Alfa","Bravo","Charlie","Delta","Echo","Foxtrot","Golf","Hotel","India","Juliett","Kilo","Lima","Mike")
      val table =
        s"| ${columns.mkString(" | ")} |" +:
          s"| ${columns.map(_ => " ---:").mkString(" | ")} |" +:
          (1 to 5).map(_ => s"| ${columns.map(_ => new java.util.Random().nextGaussian()).map(d => f"$d%1.2f").mkString(" | ")} |").toSeq

      val html = Reportly()
        .add("### This is a scientific report")
        .add(">Avocado (Persea americana) is a tropical or subtropical fruit native from South America,  \nwhich has been referred to as the most nutritious of all fruits.")
        .add("*Source:* [ScienceDirect](https://www.sciencedirect.com/topics/agricultural-and-biological-sciences/persea-americana)")
        .add(
          Seq(
            Bar(labels, labels.map(_ => java.lang.Math.abs(new java.util.Random().nextGaussian()))).withName("Hass").withMarker(Marker().withColor(Color.RGBA(74, 67, 0,0.8)).withWidth(1)),
            Bar(labels, labels.map(_ => java.lang.Math.abs(0.5 + new java.util.Random().nextGaussian()))).withName("Nabal").withMarker(Marker().withColor(Color.RGBA(107,142,35,0.8)).withWidth(1))
          ),
          Layout().withTitle("Names"),
        )
        .add(
          Seq(
            Scatter((1 until 20).toSeq, (1 until 20).map(_ => new java.util.Random().nextGaussian())).withName("A").withFill(Fill.ToZeroY),
            Scatter((1 until 20).toSeq, (1 until 20).map(_ => new java.util.Random().nextGaussian())).withName("B").withFill(Fill.ToNextY)
          ),
          Layout().withTitle("Some Random Numbers")
        )
        .add("""**Avocado production – 2017:**
                |1. 🇲🇽 Mexico [2.01 million of tonnes]
                |2. 🇩🇴 Dominican Republic [0.64 million of tonnes]
                |3. 🇵🇪 Perú [0.47 million of tonnes]
                |4. 🇮🇩 Indonesia [0.36 million of tonnes]
             """.stripMargin)
        .add("![Avocado with cross section](https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Avocado_with_cross_section_edit.jpg/640px-Avocado_with_cross_section_edit.jpg)  \n*By Muhammad Mahdi Karim - Own work, GFDL 1.2, https://commons.wikimedia.org/w/index.php?curid=11601447*")
        .add("&nbsp;\n")
        .add(table.mkString("\n"))
        .render("Avocado")

      Using(new PrintWriter("/tmp/Avocado.html")) { writer =>
        writer.write(html)
      }
    })
  }

}


