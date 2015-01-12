package es.caib.gusite.utilities.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class SGNChartPastel extends SGNChartObject
{
  public SGNChartPastel(ArrayList paramArrayList, int paramInt1, int paramInt2)
  {
    Iterator localIterator = paramArrayList.iterator();
    if (paramArrayList.size() == 0)
    {
      crearChartDefault();
    }
    else
    {
      localIterator.hasNext();
      DatosBean localDatosBean = (DatosBean)localIterator.next();
      crearChartPastel(paramArrayList);
    }
    this.alto = paramInt2;
    this.ancho = paramInt1;
  }

  public SGNChartPastel(ArrayList paramArrayList)
  {
    this(paramArrayList, 386, 250);
  }

  private void crearChartPastel(ArrayList paramArrayList)
  {
    DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      DatosBean localDatosBean = (DatosBean)localIterator.next();
      localDefaultPieDataset.setValue(localDatosBean.getDato(), Integer.parseInt(localDatosBean.getValor()));
    }
    this.chart = ChartFactory.createPieChart("", localDefaultPieDataset, false, true, false);
    this.chart.setBackgroundPaint(Color.white);
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.chart.SGNChartPastel
 * JD-Core Version:    0.6.2
 */