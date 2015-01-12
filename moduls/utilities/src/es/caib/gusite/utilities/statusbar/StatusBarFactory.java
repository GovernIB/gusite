package es.caib.gusite.utilities.statusbar;

import java.util.Date;
import java.util.Hashtable;

public class StatusBarFactory
{
  private static Hashtable hsstatusbar = new Hashtable();
  private static final Long DEFAUL_TIMEOUT = new Long(36000L);

  public static StatusBar getStatusBar(Long paramLong1, Long paramLong2)
  {
    StatusBar localStatusBar = new StatusBar();
    String str = "" + paramLong1 + paramLong2 + new Date().getTime();
    localStatusBar.setFechacreacion(new Date());
    localStatusBar.setTimeout(paramLong2);
    localStatusBar.setIdentificador(str);
    localStatusBar.setTotalitems(paramLong1);
    localStatusBar.setActualitem(new Long(0L));
    hsstatusbar.put(str, localStatusBar);
    return localStatusBar;
  }

  public static StatusBar getStatusBar(Long paramLong)
  {
    return getStatusBar(paramLong, DEFAUL_TIMEOUT);
  }

  public static StatusBar getStatusBar()
  {
    return getStatusBar(new Long(10000L), DEFAUL_TIMEOUT);
  }

  public static StatusBar obtenerStatusBar(String paramString)
  {
    if (!hsstatusbar.containsKey(paramString))
      return null;
    return (StatusBar)hsstatusbar.get(paramString);
  }

  public static void removerStatusBar(String paramString)
  {
    if ((paramString != null) && (hsstatusbar.containsKey(paramString)))
      hsstatusbar.remove(paramString);
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.statusbar.StatusBarFactory
 * JD-Core Version:    0.6.2
 */