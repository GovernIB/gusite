package es.caib.gusite.utilities.system;

import es.caib.gusite.utilities.date.Fechas;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

public class MonitorHeapUtil
{
  private static Log log = LogFactory.getLog(MonitorHeapUtil.class);
  private static final int _tamano = 200;
  private static List listheapsize = new ArrayList();

  public static String getActualHeap()
  {
    String str = "";
    str = "" + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024L;
    return str;
  }

  public static void almacenarHeap()
  {
    addValorHeap(getActualHeap());
  }

  public static List getListHeapSize()
  {
    return listheapsize;
  }

  public static String getLastValueHeap()
  {
    if (listheapsize.size() == 0)
      return "No registrado";
    LabelValueBean localLabelValueBean = (LabelValueBean)listheapsize.get(0);
    return localLabelValueBean.getValue();
  }

  public static String listaToStringHeap()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = listheapsize.iterator();
    while (localIterator.hasNext())
    {
      LabelValueBean localLabelValueBean = (LabelValueBean)localIterator.next();
      localStringBuilder.append(localLabelValueBean.getLabel()).append("=").append(localLabelValueBean.getValue()).append(" \n");
    }
    return localStringBuilder.toString();
  }

  private static void addValorHeap(String paramString)
  {
    listheapsize.add(0, new LabelValueBean(Fechas.formatFecha(new Date(), "dd-MM HH:mm"), paramString));
    if (listheapsize.size() > 200)
      listheapsize.remove(listheapsize.size() - 1);
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.system.MonitorHeapUtil
 * JD-Core Version:    0.6.2
 */