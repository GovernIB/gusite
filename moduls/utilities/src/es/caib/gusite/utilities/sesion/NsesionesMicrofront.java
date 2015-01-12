package es.caib.gusite.utilities.sesion;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class NsesionesMicrofront
  implements HttpSessionListener
{
  public static final String KEY_APP_MICROFRONT = "Microfront";

  public void sessionCreated(HttpSessionEvent paramHttpSessionEvent)
  {
    NsesionesBean.sumaContador("Microfront");
  }

  public void sessionDestroyed(HttpSessionEvent paramHttpSessionEvent)
  {
    NsesionesBean.restaContador("Microfront");
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.sesion.NsesionesMicrofront
 * JD-Core Version:    0.6.2
 */