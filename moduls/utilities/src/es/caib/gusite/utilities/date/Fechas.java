package es.caib.gusite.utilities.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Fechas
{
  public static final String FORMATO_SERIALIZACION_FECHA = "yyyyMMddHHmmss";
  public static final String FORMATO_ESTANDAR_FECHA = "dd/MM/yyyy";
  public static final String FORMATO_ESTANDAR_FECHA_HORA = "dd/MM/yyyy HH:mm:ss";

  public static String formatFecha(Date paramDate, String paramString)
  {
    if (paramString == null)
      paramString = "dd/MM/yyyy";
    if (paramString.length() == 0)
      paramString = "dd/MM/yyyy";
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString);
    return localSimpleDateFormat.format(paramDate);
  }

  public static Date dateToFechaEstandar(String paramString)
    throws Exception
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    return localSimpleDateFormat.parse(paramString.trim());
  }

  public static String dateToString(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return localSimpleDateFormat.format(paramDate);
  }

  public static Date stringToDate(String paramString)
    throws Exception
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return localSimpleDateFormat.parse(paramString.trim());
  }

  public static Date stringToDate(String paramString1, String paramString2)
    throws Exception
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString2);
    return localSimpleDateFormat.parse(paramString1.trim());
  }

  public static GregorianCalendar siguienteMes(GregorianCalendar paramGregorianCalendar)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramGregorianCalendar.getTime());
    int i = localGregorianCalendar.get(2);
    int j = localGregorianCalendar.get(1);
    i++;
    if (i > 11)
    {
      i = 0;
      j++;
    }
    localGregorianCalendar.set(1, j);
    localGregorianCalendar.set(2, i);
    return localGregorianCalendar;
  }

  public static GregorianCalendar anteriorMes(GregorianCalendar paramGregorianCalendar)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramGregorianCalendar.getTime());
    int i = localGregorianCalendar.get(2);
    int j = localGregorianCalendar.get(1);
    i--;
    if (i < 0)
    {
      i = 11;
      j--;
    }
    localGregorianCalendar.set(1, j);
    localGregorianCalendar.set(2, i);
    return localGregorianCalendar;
  }

  public static int obtenerNumeroSemana(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    int i = localGregorianCalendar.get(3);
    return i;
  }

  public static Date obtenerPrimerDiaSemana(int paramInt1, int paramInt2)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.set(1, paramInt1);
    localGregorianCalendar.set(7, 2);
    localGregorianCalendar.set(3, paramInt2);
    localGregorianCalendar.set(11, 0);
    localGregorianCalendar.set(12, 0);
    localGregorianCalendar.set(13, 0);
    localGregorianCalendar.set(14, 0);
    return localGregorianCalendar.getTime();
  }

  public static Date obtenerUltimoDiaSemana(int paramInt1, int paramInt2)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.set(1, paramInt1);
    localGregorianCalendar.set(3, paramInt2);
    localGregorianCalendar.set(7, 1);
    localGregorianCalendar.set(11, 23);
    localGregorianCalendar.set(12, 59);
    localGregorianCalendar.set(13, 59);
    localGregorianCalendar.set(14, 999);
    Date localDate = localGregorianCalendar.getTime();
    return localDate;
  }

  public static Date obtenerPrimeraHora(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.set(11, 0);
    localGregorianCalendar.set(12, 0);
    localGregorianCalendar.set(13, 0);
    localGregorianCalendar.set(14, 0);
    return localGregorianCalendar.getTime();
  }

  public static Date obtenerUltimaHora(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.set(11, 23);
    localGregorianCalendar.set(12, 59);
    localGregorianCalendar.set(13, 59);
    localGregorianCalendar.set(14, 999);
    return localGregorianCalendar.getTime();
  }

  public static int obtenerAnyo(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    return localGregorianCalendar.get(1);
  }

  public static int obtenerMes(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    return localGregorianCalendar.get(2) + 1;
  }

  public static int obtenerDiaMes(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    return localGregorianCalendar.get(5);
  }

  public static int obtenerHora(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    return localGregorianCalendar.get(11);
  }

  public static int obtenerMinuto(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    return localGregorianCalendar.get(12);
  }

  public static int obtenerEdad(int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(new Date());
    return localGregorianCalendar.get(1) - paramInt;
  }

  public static int obtenerAnyoNacimiento(int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(new Date());
    return localGregorianCalendar.get(1) - paramInt;
  }

  public static int obtenerNumeroDiaSemana(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    int i = localGregorianCalendar.get(7);
    return i;
  }

  public static int obtenerNumeroDiasMes(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    if (localGregorianCalendar.isLeapYear(paramInt1))
      arrayOfInt[1] += 1;
    return arrayOfInt[(paramInt2 - 1)];
  }

  public static boolean vigente(Date paramDate1, Date paramDate2)
  {
    return vigente(paramDate1, paramDate2, new Date());
  }

  public static boolean vigente(Date paramDate1, Date paramDate2, Date paramDate3)
  {
    boolean bool = false;
    if (paramDate3 == null)
      return false;
    Calendar localCalendar = Calendar.getInstance();
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar();
    if (paramDate1 == null)
      localGregorianCalendar1 = new GregorianCalendar(1972, 1, 19);
    else
      localGregorianCalendar1.setTime(paramDate1);
    if (paramDate2 == null)
      localGregorianCalendar2 = new GregorianCalendar(localCalendar.get(1) + 1, localCalendar.get(2) + 1, localCalendar.get(5));
    else
      localGregorianCalendar2.setTime(paramDate2);
    if ((paramDate3.getTime() >= localGregorianCalendar1.getTime().getTime()) && (paramDate3.getTime() <= localGregorianCalendar2.getTime().getTime()))
      bool = true;
    return bool;
  }

  public static Date sumaMeses(Date paramDate, int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.add(2, paramInt);
    return localGregorianCalendar.getTime();
  }

  public static Date sumaDias(Date paramDate, int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.add(5, paramInt);
    return localGregorianCalendar.getTime();
  }

  public static Date sumaHoras(Date paramDate, int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.add(11, paramInt);
    return localGregorianCalendar.getTime();
  }

  public static Date sumaSegundos(Date paramDate, int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.add(13, paramInt);
    return localGregorianCalendar.getTime();
  }

  public static int diferenciaHoras(Date paramDate1, Date paramDate2)
  {
    long l = paramDate1.getTime() - paramDate2.getTime();
    int i = (int)(l / 1000L / 60L / 60L);
    return i;
  }

  public static int diferenciaDias(Date paramDate1, Date paramDate2)
  {
    long l = paramDate1.getTime() - paramDate2.getTime();
    l = Math.abs(l);
    int i = (int)(l / 1000L / 60L / 60L / 24L);
    return i;
  }

  public static int diferenciaDiasReal(Date paramDate1, Date paramDate2)
  {
    long l = paramDate1.getTime() - paramDate2.getTime();
    int i = (int)(l / 1000L / 60L / 60L / 24L);
    return i;
  }

  public static int diferenciaAnyos(Date paramDate1, Date paramDate2)
  {
    int i = 0;
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar(new Locale("es"));
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar1.setTime(paramDate1);
    localGregorianCalendar2.setTime(paramDate2);
    int j = localGregorianCalendar1.get(1);
    int k = localGregorianCalendar2.get(1);
    localGregorianCalendar1.set(1, 2000);
    localGregorianCalendar2.set(1, 2000);
    if (localGregorianCalendar1.getTimeInMillis() > localGregorianCalendar2.getTimeInMillis())
      i = -1;
    i += k - j;
    return i;
  }

  public static boolean esBisiesto(int paramInt)
  {
    return (paramInt % 400 == 0) || ((paramInt % 4 == 0) && (paramInt % 100 != 0));
  }

  public static Date setHora(Date paramDate, int paramInt1, int paramInt2, int paramInt3)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(paramDate);
    localGregorianCalendar.set(11, paramInt1);
    localGregorianCalendar.set(12, paramInt2);
    localGregorianCalendar.set(13, paramInt3);
    localGregorianCalendar.set(14, 0);
    return localGregorianCalendar.getTime();
  }

  public static Date setFecha(int paramInt1, int paramInt2, int paramInt3)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar.setTime(new Date());
    localGregorianCalendar.set(5, paramInt1);
    localGregorianCalendar.set(2, paramInt2 - 1);
    localGregorianCalendar.set(1, paramInt3);
    return localGregorianCalendar.getTime();
  }

  public static Boolean esDiaLaborable(Date paramDate)
  {
    int i = obtenerNumeroDiaSemana(paramDate);
    return Boolean.valueOf((i != 1) && (i != 7));
  }

  public static boolean sonIguales(Date paramDate1, Date paramDate2)
  {
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar1.setTime(paramDate1);
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar(new Locale("es"));
    localGregorianCalendar2.setTime(paramDate2);
    return (localGregorianCalendar1.get(1) == localGregorianCalendar2.get(1)) && (localGregorianCalendar1.get(2) == localGregorianCalendar2.get(2)) && (localGregorianCalendar1.get(5) == localGregorianCalendar2.get(5));
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.date.Fechas
 * JD-Core Version:    0.6.2
 */