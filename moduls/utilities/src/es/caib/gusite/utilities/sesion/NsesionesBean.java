package es.caib.gusite.utilities.sesion;

import java.util.Date;
import java.util.HashMap;

public class NsesionesBean
{
  private static Date fechaInicio = new Date();
  private static HashMap mapaSesionesContador = new HashMap();

  public static void reset()
  {
    mapaSesionesContador = new HashMap();
  }

  public static void sumaContador(String paramString)
  {
    Long localLong = (Long)mapaSesionesContador.get(paramString);
    if (localLong == null)
      localLong = new Long(0L);
    long l = localLong.longValue();
    l += 1L;
    mapaSesionesContador.put(paramString, new Long(l));
  }

  public static void restaContador(String paramString)
  {
    Long localLong = (Long)mapaSesionesContador.get(paramString);
    if (localLong == null)
      localLong = new Long(0L);
    long l = localLong.longValue();
    l -= 1L;
    if (l < 0L)
      l = 0L;
    mapaSesionesContador.put(paramString, new Long(l));
  }

  public static long getContador(String paramString)
  {
    Long localLong = (Long)mapaSesionesContador.get(paramString);
    if (localLong == null)
      localLong = new Long(0L);
    return localLong.longValue();
  }

  public static HashMap getMapaSesionesContador()
  {
    return mapaSesionesContador;
  }

  public static Date getFechaInicio()
  {
    return fechaInicio;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.sesion.NsesionesBean
 * JD-Core Version:    0.6.2
 */