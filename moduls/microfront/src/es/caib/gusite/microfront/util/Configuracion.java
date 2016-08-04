package es.caib.gusite.microfront.util;

import java.util.Hashtable;


/**
 * 
 * Clase estatica que lee del web.xml algunos parametros de configuraciones.
 * Ejemplo: para leer una propiedad 
 *        Configuracion.getPropiedad("dirlog");
 *        
 * @author Indra
 */

public class Configuracion {
  
  private static Hashtable<String, String> propiedades = new Hashtable<String, String>();


  static {

    try {
    
        javax.naming.Context ctx = new javax.naming.InitialContext();
        javax.naming.Context env = (javax.naming.Context) ctx.lookup("java:comp/env");
    
        propiedades.put("mail_host",(String) env.lookup("mail_host"));
        propiedades.put("mail_port",(String) env.lookup("mail_port"));
        propiedades.put("mail_usr",(String) env.lookup("mail_usr"));
        propiedades.put("mail_pwd",(String) env.lookup("mail_pwd"));
        propiedades.put("mail_from",(String) env.lookup("mail_from"));

    } catch (Exception e) {
       System.out.println("ERROR:MICROSITES:configuracion : " + e.getMessage());
    }
  }

  private Configuracion() { }
  
  public static String getPropiedad(String nombre) throws Exception {

    String valor = (String) propiedades.get(nombre);
    if (valor == null)
      throw new Exception("Configuracion: lectura erronea en el parametro: " + nombre);

    return valor;
  }
}
