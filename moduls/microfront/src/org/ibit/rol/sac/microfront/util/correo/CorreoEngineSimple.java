package org.ibit.rol.sac.microfront.util.correo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.util.Configuracion;


public class CorreoEngineSimple implements CorreoEngine{

	protected static Log log = LogFactory.getLog(CorreoEngineSimple.class);
	
	  ArrayList<String> listadestinatarios= new ArrayList<String>();
	  String mail_subject = "";
	  String mail_from = "";
	  String mail_to = "";
	  String mail_cc = "";
	  String mail_bcc = "";
	  String mail_host = "";
	  String mail_port ="";
	  String mail_charset = "iso-8859-1";
	  String mail_usr = "";
	  String mail_pwd = "";
	  boolean mail_isHTMLFormat=false;
	  StringBuffer mail_mensaje=new StringBuffer("");

	    
	  public CorreoEngineSimple(){}
	  
	  public void initCorreo(String subject, boolean isHTMLFormat, StringBuffer body)
	  {
	    this.mail_subject=subject;
	    this.mail_isHTMLFormat= isHTMLFormat;
	    this.mail_mensaje=body;
	    if (isHTMLFormat) 
	      this.mail_mensaje= montarHTML(body);    
	    try {
	      this.mail_host=Configuracion.getPropiedad("mail_host");
	      this.mail_port=Configuracion.getPropiedad("mail_port");
	      this.mail_usr=Configuracion.getPropiedad("mail_usr");
	      this.mail_pwd=Configuracion.getPropiedad("mail_pwd");
	      this.mail_from=Configuracion.getPropiedad("mail_from");
	    } catch (Exception e) {
	    }
	  }

	  public void initCorreo(String destinatario, String subject, boolean isHTMLFormat, StringBuffer body){
	    this.listadestinatarios.add(destinatario);
	    this.mail_subject=subject;
	    this.mail_isHTMLFormat= isHTMLFormat;
	    this.mail_mensaje=body;    
	    if (isHTMLFormat) 
	      this.mail_mensaje= montarHTML(body);
	    try {
	      this.mail_host=Configuracion.getPropiedad("mail_host");
	      this.mail_port=Configuracion.getPropiedad("mail_port");
	      this.mail_usr=Configuracion.getPropiedad("mail_usr");
	      this.mail_pwd=Configuracion.getPropiedad("mail_pwd");
	      this.mail_from=Configuracion.getPropiedad("mail_from");
	    } catch (Exception e) {
	    }
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
	  
	  /**
	   * Mï¿½todo privado para enviar correo.
	   * @param destinatario
	   * @return boolean true si se ha enviao, false si ha habido un error
	   */
	  private boolean send(String destinatario) { 

		   Multipart multipart = new MimeMultipart();
		   Properties properties = new Properties(); 
		   properties.put("mail.smtp.host", mail_host);
		   properties.put( "mail.smtp.port",mail_port);
		   properties.put( "mail.smtp.auth", "true" );
		   
		   Session session = Session.getDefaultInstance(properties, null); 
		   try { 
		      MimeMessage msg = new MimeMessage(session); 
		      msg.setFrom(new InternetAddress(mail_from)); 
		      msg.setRecipients(Message.RecipientType.TO, destinatario); 
		      msg.setRecipients(Message.RecipientType.CC, mail_cc); 
		      msg.setRecipients(Message.RecipientType.BCC, mail_bcc); 
		      msg.setSubject(mail_subject); 
		      msg.setSentDate(new Date()); 
		
		      // BODY 
		      MimeBodyPart mbp = new MimeBodyPart(); 
		      if(mail_isHTMLFormat){ 
		         mbp.setContent(mail_mensaje.toString(), "text/html"); 
		      } 
		      else{ 
		         mbp.setText(mail_mensaje.toString()); 
		      } 
		
		      multipart.addBodyPart(mbp); 
		      msg.setContent(multipart); 
		
		      Transport transporte = session.getTransport("smtp");
		      transporte.connect(mail_host, mail_usr, mail_pwd);
		      transporte.sendMessage(msg,msg.getAllRecipients());
		      //esta es la otra alternativa
		      //Transport.send(msg); 
		   } 
	   catch (Exception mex){ 
	      return false; 
	   } 
	   return true; 
	  }
	  
	  /**
	   * Método público para montarHTML
	   * @param body
	   * @return StringBuffer Código Html
	   */
	  private StringBuffer montarHTML(StringBuffer body) {
	    StringBuffer retorno = new StringBuffer();
	    StringBuffer cabecera = new StringBuffer();
	    StringBuffer pie = new StringBuffer();
	    
	    cabecera.append("<html><head><title>MICROSITES</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
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

	    pie.append("</td></tr><tr><td><div align='center'>Correu automï¿½tic</div></td></tr></table></body></html>");
	    	    
	    retorno.append(cabecera);
	    retorno.append(body);
	    retorno.append(pie);
	    
	    return retorno;
	  }
	
}