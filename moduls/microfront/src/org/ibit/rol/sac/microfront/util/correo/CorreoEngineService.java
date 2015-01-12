package org.ibit.rol.sac.microfront.util.correo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Clase CorreoEngineService. Implementa CorreoEngine. Se utiliza para inicializar y enviar correos.
 * @author Indra
 *
 */
public class CorreoEngineService implements CorreoEngine {

	protected static Log log = LogFactory.getLog(CorreoEngineService.class);
	
	  ArrayList<String> listadestinatarios= new ArrayList<String>();
	  String mail_subject = "";
	  String mail_from = "";
	  String mail_charset = "iso-8859-1";
	  boolean mail_isHTMLFormat=false;
	  StringBuffer mail_mensaje=new StringBuffer("");
	  ArrayList<InputStream> mail_file = new ArrayList<InputStream>();
	  ArrayList<String> mail_fileName = new ArrayList<String>();
	    
	  public CorreoEngineService(){}
	  
	  
	public void initCorreo(String subject, boolean isHTMLFormat, StringBuffer body) {
	    this.mail_subject=subject;
	    this.mail_isHTMLFormat= isHTMLFormat;
	    this.mail_mensaje=body;
	    if (isHTMLFormat) 
	      this.mail_mensaje= montarHTML(body);    
	  }

	public void initCorreo(String destinatario, String subject, boolean isHTMLFormat, StringBuffer body)  {
	    this.listadestinatarios.add(destinatario);
	    this.mail_subject=subject;
	    this.mail_isHTMLFormat= isHTMLFormat;
	    this.mail_mensaje=body;    
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
	  /**
	   * M�todo para enviar a un destinatario
	   * @param destinatario
	   * @return boolean true si se ha enviao, false si ha habido un error
	   */
	  private boolean send(String destinatario) { 
		  
		  try {
			  
	          InitialContext ctx = new InitialContext();
	          Session sesion = (Session) ctx.lookup("java:/es.caib.gusite.mail");
	          MimeMessage mensaje = new MimeMessage(sesion);
	          mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
	          mensaje.setSubject(mail_subject);
	          
	          MimeBodyPart messageBodyPart = new MimeBodyPart();
	          messageBodyPart.setText(mail_mensaje.toString());
	          Multipart multipart = new MimeMultipart();
	          multipart.addBodyPart(messageBodyPart);
	          boolean hayfile= false;
	          File file = null;
	          
	          if (!mail_file.isEmpty() && mail_file.get(0).available()>0 && mail_fileName.get(0) != ""){
	        	  file = new File(System.getProperty("java.io.tmpdir")+file.separator + mail_fileName.get(0));        	 
	        	  hayfile=true;
	        	  for(int i = 0;i<mail_file.size();i++){
		        	  messageBodyPart = new MimeBodyPart();
		        	  InputStream is = mail_file.get(i);
		        	 
		        	  FileOutputStream fileOs = new FileOutputStream(file);
		        	  int a;
		        	  while((a = is.read()) != -1){
		        		  fileOs.write(a);
		        	  }
		        	  mail_file.get(i).close();
		        	  fileOs.close();
		        	  DataSource ds = new FileDataSource(System.getProperty("java.io.tmpdir")+file.separator + mail_fileName.get(i));
		        	  
		        	  messageBodyPart.setDataHandler(new DataHandler(ds));
		        	  messageBodyPart.setFileName(mail_fileName.get(i));
		        	  multipart.addBodyPart(messageBodyPart);
		          }
	          }
	          mensaje.setContent(multipart);
              Transport.send(mensaje);
              if (hayfile) file.delete();
              
		  } catch (Exception mex){ 
	         	      log.error(mex.getMessage());
	         	      return false; 
	      } 			  
			  
		  return true; 
	  }
	  
	  /**
	   * Metodo privado para montar HTML
	   * @param body
	   * @return StringBuffer C�digo HTML
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

	    pie.append("</td></tr><tr><td><div align='center'>Correu autom�tic</div></td></tr></table></body></html>");
	     
	    retorno.append(cabecera);
	    retorno.append(body);
	    retorno.append(pie);
	    
	    return retorno;
	  }

	public void setFile(InputStream is, String fileName) {		
		mail_file.add(is);
		mail_fileName.add(fileName);
	}
	
}
