package samples

import org.junit._
import Assert._
import de.stereotypez.reportly.Reportly
import plotly.Bar
import plotly.layout.Layout

@Test
class ReportlyTest {

  @Test
  def testOK(): Unit = {

    assertNotNull({

      val labels = Seq("Banana", "Banano", "Grapefruit")
      val valuesA = labels.map(_ => new java.util.Random().nextGaussian())
      val valuesB = labels.map(_ => 0.5 + new java.util.Random().nextGaussian())

      Reportly()
        .add("### Some highly informative plots")
        .add(
          Seq(Bar(labels, valuesA).withName("A"), Bar(labels, valuesB).withName("B")),
          Layout().withTitle("Test"))
        .render("test")
    })
  }

}


