package es.caib.gusite.microback.utils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Clase CorreoEngineService. Implementa CorreoEngine. Se utiliza para inicializar y enviar correos.
 * @author Indra
 *
 */
public class CorreoService {

	protected static Log log = LogFactory.getLog(CorreoService.class);
	
	  ArrayList<String> listadestinatarios= new ArrayList<String>();
	  HashMap<String,String> informe = new HashMap();
	  
	  String mail_subject = "";
	  String mail_charset = "utf-8";

	  boolean mail_isHTMLFormat=false;
	  StringBuffer mail_mensaje=new StringBuffer("");
	    
	  public CorreoService(){}
	  
	  
	public void initCorreo(String subject, boolean isHTMLFormat, StringBuffer body) {
	    this.mail_subject=subject;
	    this.mail_mensaje=body;
	    this.mail_isHTMLFormat = isHTMLFormat;
	    if (isHTMLFormat) 
	      this.mail_mensaje= montarHTML(body);    
	  }

	public void initCorreo(String destinatario, String subject, boolean isHTMLFormat, StringBuffer body)  {
		this.listadestinatarios.clear();
	    this.listadestinatarios.add(destinatario);
	    this.mail_subject=subject;
	    this.mail_mensaje=body;
	    this.mail_isHTMLFormat= isHTMLFormat;
	    if (isHTMLFormat)
	      this.mail_mensaje= montarHTML(body);
	  }
	  
	public void newDestinatario(String destinatario) {
	    listadestinatarios.add(destinatario);
	  }

	  public boolean enviarCorreo() {
		boolean tmpok=true;
	    //recorrer la lista de destinatarios y enviar
	    for (int i = 0; i < listadestinatarios.size(); i++) {
	      if (!send((String)listadestinatarios.get(i))) {	    	  
	    	  log.error("Error enviando correo a destinatario: " + (String)listadestinatarios.get(i) );
	    	  tmpok=false;
	      }
	    }
	    return tmpok;

	  }
	  
	  public void clearInforme(){
			this.informe.clear();
	  }
	  
	  public HashMap<String, String> getInforme(){
		  return this.informe;
	  }
	  
	  /**
	   * Método para enviar a un destinatario
	   * @param destinatario
	   * @return boolean true si se ha enviao, false si ha habido un error
	   */
	  private boolean send(String destinatario) { 
		  
		  try {
			  
	          InitialContext ctx = new InitialContext();
	          
	          Session sesion = (Session) ctx.lookup("java:/es.caib.gusite.mail");
	          MimeMessage mensaje = new MimeMessage(sesion);
	          if (mail_isHTMLFormat)
	        	  mensaje.setContent(mail_mensaje.toString(),"text/html; charset=" + mail_charset);
	          else
	        	  mensaje.setContent(mail_mensaje.toString(),"text/plain; charset=" + mail_charset);
	          
	          mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
	          mensaje.setSubject(mail_subject);
			  	         
              Transport.send(mensaje);
              this.informe.put(destinatario, "ok");
              
		  } catch (Exception mex){
			  this.informe.put(destinatario, mex.getMessage());
     	      log.error(mex.getMessage());
     	      return false; 
	      } 			  
			  
		  return true; 
	  }
	  
	  /**
	   * Metodo privado para montar HTML
	   * @param body
	   * @return StringBuffer Código HTML
	   */
	  private StringBuffer montarHTML(StringBuffer body) {
	    StringBuffer retorno = new StringBuffer();
	    StringBuffer cabecera = new StringBuffer();
	    StringBuffer pie = new StringBuffer();
	    
	    cabecera.append("<html><head><title>MICROSITES</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
	    cabecera.append("<style type=\"text/css\">");
	    cabecera.append("<!--");
	    cabecera.append(".tableborde {");
	    cabecera.append("border: 1px solid #3E2D2A;");
	    cabecera.append("font-family: Verdana, Arial, Helvetica, sans-serif;");
	    cabecera.append("font-size: 80%;");
	    cabecera.append("color: #000000;");
	    cabecera.append("}");
	    cabecera.append(".tablecabecera {");
	    cabecera.append("font-weight: bold;");
	    cabecera.append("color: #FFFFFF;");
	    cabecera.append("background-color: #7C95AD;");
	    cabecera.append("}");
	    cabecera.append("a {");
	    cabecera.append("color: #33716F;");
	    cabecera.append("text-decoration: underline;");
	    cabecera.append("}");
	    cabecera.append("-->");
	    cabecera.append("</style>");
	    cabecera.append("</head>");
	    cabecera.append("<body>");
	    cabecera.append("<table width='98%'  border='0' cellpadding='2' cellspacing='2' class='tableborde'><tr><td class='tablecabecera'><div align='center'>CAIB</div></td></tr><tr><td>");

	    pie.append("</td></tr><tr><td><div align='center'>Correu automàtic</div></td></tr></table></body></html>");
	     
	    retorno.append(cabecera);
	    retorno.append(body);
	    retorno.append(pie);
	    
	    return retorno;
	  }
	
}

