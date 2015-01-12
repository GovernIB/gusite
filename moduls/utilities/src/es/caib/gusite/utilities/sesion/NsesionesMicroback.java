package es.caib.gusite.utilities.sesion;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class NsesionesMicroback
  implements HttpSessionListener
{
  public static final String KEY_APP_MICROBACK = "Microback";

  public void sessionCreated(HttpSessionEvent paramHttpSessionEvent)
  {
    NsesionesBean.sumaContador("Microback");
  }

  public void sessionDestroyed(HttpSessionEvent paramHttpSessionEvent)
  {
    NsesionesBean.restaContador("Microback");
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.sesion.NsesionesMicroback
 * JD-Core Version:    0.6.2
 */