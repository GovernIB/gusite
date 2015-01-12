package es.caib.gusite.utilities.system;

import es.caib.gusite.utilities.date.Fechas;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

public class MonitorThreadUtil
{
  private static Log log = LogFactory.getLog(MonitorThreadUtil.class);
  private static final int _tamano = 200;
  private static List listthreads = new ArrayList();

  public static String getActualThread()
  {
    String str = "";
    str = "" + ManagementFactory.getThreadMXBean().getThreadCount();
    return str;
  }

  public static void almacenarThread()
  {
    addValorThread(getActualThread());
  }

  public static List getListThread()
  {
    return listthreads;
  }

  public static String getLastThread()
  {
    if (listthreads.size() == 0)
      return "No registrado";
    LabelValueBean localLabelValueBean = (LabelValueBean)listthreads.get(0);
    return localLabelValueBean.getValue();
  }

  public static String listaToStringHeap()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = listthreads.iterator();
    while (localIterator.hasNext())
    {
      LabelValueBean localLabelValueBean = (LabelValueBean)localIterator.next();
      localStringBuilder.append(localLabelValueBean.getLabel()).append("=").append(localLabelValueBean.getValue()).append(" \n");
    }
    return localStringBuilder.toString();
  }

  private static void addValorThread(String paramString)
  {
    listthreads.add(0, new LabelValueBean(Fechas.formatFecha(new Date(), "dd-MM HH:mm"), paramString));
    if (listthreads.size() > 200)
      listthreads.remove(listthreads.size() - 1);
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.system.MonitorThreadUtil
 * JD-Core Version:    0.6.2
 */