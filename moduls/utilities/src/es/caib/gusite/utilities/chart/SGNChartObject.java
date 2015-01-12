package es.caib.gusite.utilities.chart;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class SGNChartObject
{
  protected JFreeChart chart = null;
  protected int ancho = 355;
  protected int alto = 230;

  public void toFile(String paramString)
  {
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
      ChartUtilities.writeChartAsPNG(localFileOutputStream, this.chart, this.ancho, this.alto);
      localFileOutputStream.close();
    }
    catch (Exception localException)
    {
    }
  }

  public byte[] toByteArray()
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      ChartUtilities.writeChartAsPNG(localByteArrayOutputStream, this.chart, this.ancho, this.alto);
    }
    catch (Exception localException)
    {
    }
    return ((ByteArrayOutputStream)localByteArrayOutputStream).toByteArray();
  }

  protected void crearChartDefault()
  {
    DefaultKeyedValues localDefaultKeyedValues = new DefaultKeyedValues();
    localDefaultKeyedValues.addValue("No hay accesos", 0.0D);
    CategoryDataset localCategoryDataset = DatasetUtilities.createCategoryDataset("Accesos", localDefaultKeyedValues);
    this.chart = ChartFactory.createLineChart("", "", "", localCategoryDataset, PlotOrientation.VERTICAL, false, true, false);
    CategoryPlot localCategoryPlot = this.chart.getCategoryPlot();
    LineAndShapeRenderer localLineAndShapeRenderer = (LineAndShapeRenderer)localCategoryPlot.getRenderer();
    localLineAndShapeRenderer.setShapesVisible(true);
    localLineAndShapeRenderer.setSeriesPaint(0, new Color(106, 166, 227));
    localCategoryPlot.setDomainGridlinesVisible(true);
    this.chart.setBackgroundPaint(Color.white);
  }

  protected String formatTempo(String paramString)
  {
    String str = paramString;
    if (paramString.length() > 4)
      switch (Integer.parseInt(paramString.substring(4, 6)))
      {
      case 1:
        str = "ene";
        break;
      case 2:
        str = "feb";
        break;
      case 3:
        str = "mar";
        break;
      case 4:
        str = "abr";
        break;
      case 5:
        str = "may";
        break;
      case 6:
        str = "jun";
        break;
      case 7:
        str = "jul";
        break;
      case 8:
        str = "ago";
        break;
      case 9:
        str = "sep";
        break;
      case 10:
        str = "oct";
        break;
      case 11:
        str = "nov";
        break;
      case 12:
        str = "dic";
      }
    return str;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.chart.SGNChartObject
 * JD-Core Version:    0.6.2
 */