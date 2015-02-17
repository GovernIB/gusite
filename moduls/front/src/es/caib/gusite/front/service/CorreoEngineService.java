package es.caib.gusite.front.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
 * Clase CorreoEngineService. Se utiliza para inicializar y enviar correos.
 * 
 * @author Indra
 * 
 */
public class CorreoEngineService {

	protected static Log log = LogFactory.getLog(CorreoEngineService.class);

	private List<String> listadestinatarios = new ArrayList<String>();
	private String mail_subject = "";
	private StringBuffer mail_mensaje = new StringBuffer("");
	private List<InputStream> mail_file = new ArrayList<InputStream>();
	private List<String> mail_fileName = new ArrayList<String>();

	public CorreoEngineService() {
	}

	public void initCorreo(String subject, boolean isHTMLFormat,
			StringBuffer body) {
		this.mail_subject = subject;
		this.mail_mensaje = body;
		if (isHTMLFormat) {
			this.mail_mensaje = this.montarHTML(body);
		}
	}

	public void initCorreo(String destinatario, String subject,
			boolean isHTMLFormat, StringBuffer body) {
		this.listadestinatarios.add(destinatario);
		this.mail_subject = subject;
		this.mail_mensaje = body;
		if (isHTMLFormat) {
			this.mail_mensaje = this.montarHTML(body);
		}
	}

	public void newDestinatario(String destinatario) {
		this.listadestinatarios.add(destinatario);
	}

	/**
	 * Comprueba que la propiedad de sistema es.caib.gusite.email.debugMode sea
	 * igual a [S|s].
	 * 
	 * @return
	 */
	private static boolean isDebugMode() {

		String debugModeString = System
				.getProperty("es.caib.gusite.email.debugMode");
		boolean debugMode = ("S".equals(debugModeString) || "s"
				.equals(debugModeString));

		return debugMode;

	}

	/**
	 * Devuelve la propiedad de sistema es.caib.gusite.email.debugMail
	 * 
	 * @return
	 */
	private static String getDebugMail() {

		return System.getProperty("es.caib.gusite.email.debugMail");

	}

	public boolean enviarCorreo() {
		boolean tmpok = true;

		if (isDebugMode()) {
			String destinatario = getDebugMail();
			if (destinatario == null) {
				log.error("Mail configurado en modo debug, pero falta DebugMail");
				tmpok = false;
			} else {
				if (!this.send(destinatario)) {
					log.error("Error enviando correo a destinatario: "
							+ destinatario);
					tmpok = false;
				}
			}
		} else {
			// recorrer la lista de destinatarios y enviar
			for (int i = 0; i < this.listadestinatarios.size(); i++) {
				if (!this.send(this.listadestinatarios.get(i))) {
					log.error("Error enviando correo a destinatario: "
							+ this.listadestinatarios.get(i));
					tmpok = false;
				}
			}
		}
		return tmpok;

	}

	/**
	 * Método para enviar a un destinatario
	 * 
	 * @param destinatario
	 * @return boolean true si se ha enviao, false si ha habido un error
	 */
	private boolean send(String destinatario) {

		try {

			InitialContext ctx = new InitialContext();
			Session sesion = (Session) ctx.lookup("java:/es.caib.gusite.mail");
			MimeMessage mensaje = new MimeMessage(sesion);
			mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(
					destinatario));
			mensaje.setSubject(this.mail_subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(this.mail_mensaje.toString());
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			boolean hayfile = false;
			File file = null;

			if (!this.mail_file.isEmpty()
					&& this.mail_file.get(0).available() > 0
					&& this.mail_fileName.get(0) != "") {
				file = new File(System.getProperty("java.io.tmpdir")
						+ file.separator + this.mail_fileName.get(0));
				hayfile = true;
				for (int i = 0; i < this.mail_file.size(); i++) {
					messageBodyPart = new MimeBodyPart();
					InputStream is = this.mail_file.get(i);

					FileOutputStream fileOs = new FileOutputStream(file);
					int a;
					while ((a = is.read()) != -1) {
						fileOs.write(a);
					}
					this.mail_file.get(i).close();
					fileOs.close();
					DataSource ds = new FileDataSource(
							System.getProperty("java.io.tmpdir")
									+ file.separator
									+ this.mail_fileName.get(i));

					messageBodyPart.setDataHandler(new DataHandler(ds));
					messageBodyPart.setFileName(this.mail_fileName.get(i));
					multipart.addBodyPart(messageBodyPart);
				}
			}
			mensaje.setContent(multipart);
			Transport.send(mensaje);
			if (hayfile) {
				file.delete();
			}

		} catch (Exception mex) {
			log.error(mex.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Metodo privado para montar HTML
	 * 
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

		pie.append("</td></tr><tr><td><div align='center'>Correu automátic</div></td></tr></table></body></html>");

		retorno.append(cabecera);
		retorno.append(body);
		retorno.append(pie);

		return retorno;
	}

	public void setFile(InputStream is, String fileName) {
		this.mail_file.add(is);
		this.mail_fileName.add(fileName);
	}

}
