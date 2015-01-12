package es.caib.gusite.utilities.statusbar;

import java.util.Date;

public class StatusBar
{
  private String identificador;
  private Long totalitems;
  private Long actualitem;
  private String actualitemtext;
  private Date fechacreacion;
  private Long timeout;

  public Long getTotalitems()
  {
    return this.totalitems;
  }

  public void setTotalitems(Long paramLong)
  {
    this.totalitems = paramLong;
  }

  public Long getActualitem()
  {
    return this.actualitem;
  }

  public void setActualitem(Long paramLong)
  {
    this.actualitem = paramLong;
  }

  public String getActualitemtext()
  {
    return this.actualitemtext;
  }

  public void setActualitemtext(String paramString)
  {
    this.actualitemtext = paramString;
  }

  public String getIdentificador()
  {
    return this.identificador;
  }

  public void setIdentificador(String paramString)
  {
    this.identificador = paramString;
  }

  public String getPorcentaje()
  {
    if ((this.totalitems == null) || (this.totalitems.longValue() == 0L))
      return null;
    Long localLong = Long.valueOf(this.actualitem.longValue() * 100L / this.totalitems.longValue());
    return localLong.toString();
  }

  public Date getFechacreacion()
  {
    return this.fechacreacion;
  }

  public void setFechacreacion(Date paramDate)
  {
    this.fechacreacion = paramDate;
  }

  public Long getTimeout()
  {
    return this.timeout;
  }

  public void setTimeout(Long paramLong)
  {
    this.timeout = paramLong;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.statusbar.StatusBar
 * JD-Core Version:    0.6.2
 */