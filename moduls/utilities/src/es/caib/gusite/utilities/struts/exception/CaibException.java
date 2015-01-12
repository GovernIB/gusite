package es.caib.gusite.utilities.struts.exception;

import es.caib.gusite.utilities.cadenas.Cadenas;

public class CaibException extends Exception
{
  private String error;
  private String mensaje;
  private String username;
  private String servicio;
  private Exception eoriginal;

  public CaibException(Exception paramException)
  {
    this("ERROR", paramException.getMessage(), null, null, paramException);
  }

  public CaibException(String paramString)
  {
    this(paramString, null, null, null, null);
  }

  public CaibException(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, null, null, null);
  }

  public CaibException(String paramString, Exception paramException)
  {
    this(paramString, paramException.getMessage(), null, null, paramException);
  }

  public CaibException(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this(paramString1, paramString2, paramString3, paramString4, null);
  }

  public CaibException(String paramString1, String paramString2, String paramString3, String paramString4, Exception paramException)
  {
    this.error = paramString1;
    this.mensaje = paramString2;
    this.username = paramString3;
    this.servicio = paramString4;
    this.mensaje = paramException.getMessage();
    this.eoriginal = paramException;
  }

  public String getError()
  {
    return this.error;
  }

  public void setError(String paramString)
  {
    this.error = paramString;
  }

  public String getMensaje()
  {
    return this.mensaje;
  }

  public void setMensaje(String paramString)
  {
    this.mensaje = paramString;
  }

  public String getUsername()
  {
    return this.username;
  }

  public void setUsername(String paramString)
  {
    this.username = paramString;
  }

  public String getServicio()
  {
    return this.servicio;
  }

  public void setServicio(String paramString)
  {
    this.servicio = paramString;
  }

  public Exception getEoriginal()
  {
    return this.eoriginal;
  }

  public void setEoriginal(Exception paramException)
  {
    this.eoriginal = paramException;
  }

  public String getToStringToHtml()
  {
    return Cadenas.CRtoBR(toString());
  }

  public String toString()
  {
    String str = "";
    str = this.error + " " + "\n";
    if (this.eoriginal != null)
      str = str + this.eoriginal.getMessage() + "\n";
    else
      str = str + this.mensaje + " \n";
    str = str + ((this.username != null) && (!this.username.equals("")) ? " [Username: " + this.username + "] \n" : "");
    str = str + ((this.servicio != null) && (!this.servicio.equals("")) ? " [Servicio: " + this.servicio + "] \n" : "");
    return str;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.struts.exception.CaibException
 * JD-Core Version:    0.6.2
 */