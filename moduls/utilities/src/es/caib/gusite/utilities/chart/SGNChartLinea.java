package es.caib.gusite.utilities.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class SGNChartLinea extends SGNChartObject
{
  private String _nombre = "default";

  public SGNChartLinea(String paramString, ArrayList paramArrayList)
  {
    this._nombre = paramString;
    Iterator localIterator = paramArrayList.iterator();
    if (paramArrayList.size() == 0)
      crearChartDefault();
    else
      crearChartDefaultLinea(paramArrayList);
  }

  private void crearChartDefaultLinea(ArrayList paramArrayList)
  {
    DefaultKeyedValues localDefaultKeyedValues = new DefaultKeyedValues();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      DatosBean localDatosBean = (DatosBean)localIterator.next();
      localDefaultKeyedValues.addValue(localDatosBean.getDato(), Integer.parseInt(localDatosBean.getValor()));
    }
    CategoryDataset localCategoryDataset = DatasetUtilities.createCategoryDataset(this._nombre, localDefaultKeyedValues);
    this.chart = ChartFactory.createLineChart("", "", "", localCategoryDataset, PlotOrientation.VERTICAL, false, true, true);
    CategoryPlot localCategoryPlot = this.chart.getCategoryPlot();
    LineAndShapeRenderer localLineAndShapeRenderer = (LineAndShapeRenderer)localCategoryPlot.getRenderer();
    localLineAndShapeRenderer.setSeriesPaint(0, new Color(106, 166, 227));
    localCategoryPlot.setDomainGridlinesVisible(true);
    this.chart.setBackgroundPaint(Color.white);
  }

  private void crearChartMeses(ArrayList paramArrayList)
  {
    DefaultKeyedValues localDefaultKeyedValues = new DefaultKeyedValues();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      DatosBean localDatosBean = (DatosBean)localIterator.next();
      localDefaultKeyedValues.addValue(formatTempo(localDatosBean.getDato()), Integer.parseInt(localDatosBean.getValor()));
    }
    CategoryDataset localCategoryDataset = DatasetUtilities.createCategoryDataset("Accesos", localDefaultKeyedValues);
    this.chart = ChartFactory.createLineChart("", "", "", localCategoryDataset, PlotOrientation.VERTICAL, false, true, false);
    CategoryPlot localCategoryPlot = this.chart.getCategoryPlot();
    LineAndShapeRenderer localLineAndShapeRenderer = (LineAndShapeRenderer)localCategoryPlot.getRenderer();
    localLineAndShapeRenderer.setShapesVisible(true);
    localLineAndShapeRenderer.setSeriesPaint(0, new Color(106, 166, 227));
    localCategoryPlot.setDomainGridlinesVisible(true);
    this.chart.setBackgroundPaint(Color.white);
  }

  private void crearChartAnyos(ArrayList paramArrayList)
  {
    DefaultKeyedValues localDefaultKeyedValues = new DefaultKeyedValues();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      DatosBean localDatosBean = (DatosBean)localIterator.next();
      localDefaultKeyedValues.addValue(localDatosBean.getDato(), Integer.parseInt(localDatosBean.getValor()));
    }
    CategoryDataset localCategoryDataset = DatasetUtilities.createCategoryDataset("Accesos", localDefaultKeyedValues);
    this.chart = ChartFactory.createLineChart("", "", "", localCategoryDataset, PlotOrientation.VERTICAL, false, true, true);
    CategoryPlot localCategoryPlot = this.chart.getCategoryPlot();
    LineAndShapeRenderer localLineAndShapeRenderer = (LineAndShapeRenderer)localCategoryPlot.getRenderer();
    localLineAndShapeRenderer.setShapesVisible(true);
    localLineAndShapeRenderer.setSeriesPaint(0, new Color(106, 166, 227));
    localCategoryPlot.setDomainGridlinesVisible(true);
    this.chart.setBackgroundPaint(Color.white);
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.chart.SGNChartLinea
 * JD-Core Version:    0.6.2
 */