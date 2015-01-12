package es.caib.gusite.utilities.cadenas;

import java.util.StringTokenizer;

public class Cadenas
{
  public static String CRtoBR(String paramString)
  {
    String str = "";
    if (paramString == null)
      paramString = "";
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "\n");
    if (localStringTokenizer.countTokens() > 0)
      for (str = str + localStringTokenizer.nextToken(); localStringTokenizer.hasMoreTokens(); str = str + "<br/>" + localStringTokenizer.nextToken());
    return str;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.cadenas.Cadenas
 * JD-Core Version:    0.6.2
 */