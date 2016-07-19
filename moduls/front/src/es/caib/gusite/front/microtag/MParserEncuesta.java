package es.caib.gusite.front.microtag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.utilities.auth.CertsPrincipal;

/**
 * Parseo de 'encuestas'. Esta clase contiene métodos que parsean los tags
 * especiales de los microsites. Los tags son de la version 2.0 Devuelven trozos
 * de código HTML pertenecientes a las encuestas.
 * 
 * @author vroca
 * 
 */

public class MParserEncuesta extends MParserHTML {

	protected static Log log = LogFactory.getLog(MParserEncuesta.class);

	public MParserEncuesta() {
		super();
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene una encuesta.
	 * 
	 * @param idmicrosite
	 * @param idencuesta
	 * @param idioma
	 * @return StringBuffer con el pegote de html
	 */
	public StringBuffer getHtmlEncuesta(HttpServletRequest request, Long idmicrosite, String idencuesta, String idioma) {
		StringBuffer retorno = new StringBuffer();

		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		try {
			// u91856 29/06/2011 Respostes que venen donades fixes
			Map param = (request == null) ? null : (HashMap) request.getSession().getAttribute(Microfront.ENCPARAM);
			if (param == null) {
				param = new HashMap();
			} else {
				request.getSession().removeAttribute(Microfront.ENCPARAM);
			}

			String obligatoriedad = "";
			EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			Long idcont = new Long(Long.parseLong(idencuesta));
			Encuesta encuesta = encuestadel.obtenerEncuesta(idcont);

			// u91856 02/03/2012 Salvador Antich: Identificacion del usuario que
			// contesta segun configuracion de la encuesta
			if (encuesta.getIdentificacion().equals("S")) {
				try {
					CertsPrincipal principal = null;
					principal = CertsPrincipal.getCurrent();
					// principal = (CertsPrincipal) request.getUserPrincipal();
					String identificacio = rb.getString("encuesta.identificacion").replaceAll("\\{1\\}", principal.getFullName());
					retorno.append(identificacio);
				} catch (Exception e) {
					log.error("Error en la identificación del usuario en la encuesta: " + idencuesta + " ---> " + e);
					throw new Exception("Error en la identificación del usuario");
				}
			} else {
				// retorno.append(rb.getString("encuesta.anonima"));
			}
			retorno.append("<p></p><p></p>");

			if ((encuesta.getVisible().equals("S")) && (Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad()))) {

				retorno.append("<form name=\"encuesta\" action=\"envioencuesta.do\" method=\"post\" id=\"encuesta\">\n");
				retorno.append("<input type=\"hidden\" name=\"idsite\" value=\"" + idmicrosite + "\">\n");
				retorno.append("<input type=\"hidden\" name=\"lang\" value=\"" + idioma + "\">\n");
				retorno.append("<input type=\"hidden\" name=\"cont\" value=\"" + idencuesta + "\">\n");
				retorno.append("<input type=\"hidden\" name=\"enccomp\" value=\"yes\">\n");

				StringBuffer scriptValidar = new StringBuffer();
				scriptValidar.append("<script>\n function comptaChecks(pregunta){ \n");
				scriptValidar.append("	 var num=0; \n");
				scriptValidar.append("	 for (i=0; i<eval('document.encuesta.' + pregunta + '.length'); i++){ \n");
				scriptValidar.append("	 	if (eval('document.encuesta.' + pregunta + '[i].checked')==true) \n");
				scriptValidar.append("	 		num++; \n");
				scriptValidar.append("	 } \n");
				scriptValidar.append("	 return num;  \n } \n \n");

				scriptValidar.append("function marcaCheck(idResp, idChk){ \n");
				scriptValidar.append("	 if (eval('document.encuesta.T' + idResp + '_' + idChk).value.length==0) { \n");
				scriptValidar.append("	 	document.getElementById(idResp + '_' + idChk).checked = false;\n");
				scriptValidar.append("	 }else{ \n");
				scriptValidar.append("	 	document.getElementById(idResp + '_' + idChk).checked = true;\n");
				scriptValidar.append("   } \n} \n \n");

				scriptValidar.append("function getChecked(radioObj) {\n");
				scriptValidar.append("	 if(!radioObj) return false;\n");
				scriptValidar.append("	 var radioLength = radioObj.length;\n");
				scriptValidar.append("	 if(radioLength == undefined){\n");
				scriptValidar.append("   	if(radioObj.checked) return true;\n");
				scriptValidar.append("		else false; }\n");
				scriptValidar.append("	 for(var i = 0; i < radioLength; i++) {\n");
				scriptValidar.append("		if(radioObj[i].checked) return true;\n");
				scriptValidar.append("	 }\n");
				scriptValidar.append("	 return false;\n");
				scriptValidar.append("} \n \n");

				scriptValidar.append("function validaencuesta(){\n var txtError = \"\"; \n");

				Iterator iter = encuesta.getPreguntas().iterator();
				while (iter.hasNext()) { // Preguntas de una encuesta

					Pregunta pregunta = (Pregunta) iter.next();
					pregunta.setIdi(idioma);

					obligatoriedad = "";
					if ((pregunta.getVisible().equals("S")) && (pregunta.getVisiblecmp().equals("S"))) {
						Integer max = (pregunta.getMaxContestadas() != null) ? pregunta.getMaxContestadas() : new Integer(0);
						Integer min = (pregunta.getMinContestadas() != null) ? pregunta.getMinContestadas() : new Integer(0);
						if ("S".equals(pregunta.getMultiresp())) {
							if (min != 0 && max != 0) {
								// obligatoriedad = new String
								// (rb.getSting("clau").getBytes("ISO-8859-1"),"UTF-8");
								obligatoriedad = rb.getString("encuesta.respcont.minmax");
								obligatoriedad = obligatoriedad.replaceAll("\\{1\\}", min.toString()).replaceAll("\\{2\\}", max.toString());

								scriptValidar.append("if (comptaChecks(\"C" + pregunta.getId() + "\") > " + max + " || comptaChecks(\"C"
										+ pregunta.getId() + "\") < " + min + ") ");
								scriptValidar.append(" txtError = txtError + \"\\n"
										+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
												: "Error en traduccion" + "\"") + "\";\n");
							} else if (min != 0) {
								obligatoriedad = rb.getString("encuesta.respcont.min");
								obligatoriedad = obligatoriedad.replaceAll("\\{1\\}", min.toString());

								scriptValidar.append("if (comptaChecks(\"C" + pregunta.getId() + "\") < " + min + ") ");
								scriptValidar.append(" txtError = txtError + \"\\n"
										+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
												: "Error en traduccion" + "\"") + "\";\n");
							} else if (max != 0) {
								obligatoriedad = rb.getString("encuesta.respcont.max");
								obligatoriedad = obligatoriedad.replaceAll("\\{1\\}", max.toString());

								scriptValidar.append("if (comptaChecks(\"C" + pregunta.getId() + "\") > " + max + ") ");
								scriptValidar.append(" txtError = txtError + \"\\n"
										+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
												: "Error en traduccion" + "\"") + "\";\n");
							}
						} else { // Resposta simple
							if (min == 1) {
								obligatoriedad = rb.getString("encuesta.obligatoria");

								scriptValidar.append("	if (!getChecked(document.encuesta.R" + pregunta.getId() + ")) \n");
								scriptValidar.append(" txtError = txtError + \"\\n"
										+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
												: "Error en traduccion" + "\"") + "\";\n");
							}
						}
						retorno.append("<div id=\"enquestaPreguntaCom\">\n");
						retorno.append("<h3>"
								+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo() + " " + obligatoriedad
										: "&nbsp;") + "</h3>\n");

						if (pregunta.getImagen() != null) {
							retorno.append("<p><img src=\"archivopub.do?ctrl=MCRST" + idmicrosite + "ZI" + pregunta.getImagen().getId() + "&id="
									+ pregunta.getImagen().getId() + "\" alt=\"\" /></p>\n");
						}

						retorno.append("<ul>\n");

						String scriptRadioDisabled = "";
						String scriptTxtDisabled = "";
						String checked = "";
						String disabledTxt = "";
						String disabledChk = "";
						String disabledRadio = "";

						Iterator iter2 = pregunta.getRespuestas().iterator();
						while (iter2.hasNext()) {
							checked = "";

							disabledTxt = "";
							disabledChk = "";

							Respuesta respuesta = (Respuesta) iter2.next();
							respuesta.setIdi(idioma);
							retorno.append("<li>\n");
							retorno.append("<label>\n");

							if (param.containsKey(respuesta.getId())) {
								checked = " checked='checked' ";
								disabledTxt = " readonly ";
								disabledChk = " disabled ";
								disabledRadio = " disabled ";
							}

							String compId = respuesta.getId() + "_" + pregunta.getId();

							if (pregunta.getMultiresp().equals("S")) {
								if (respuesta.getTipo().equals("I")) {
									disabledChk = " disabled ";
								}

								retorno.append("<input type=\"checkbox\" id=\"" + compId + "\" name=\"C" + pregunta.getId() + "\" value=\""
										+ respuesta.getId() + "\"" + disabledChk + checked + ">");
							} else {
								scriptRadioDisabled += "document.getElementById(\"" + compId + "\").disabled=true;\n";

								retorno.append("<input type=\"radio\"id=\"" + compId + "\"name=\"R" + pregunta.getId() + "\" value=\""
										+ respuesta.getId() + "\"" + checked + ">");
							}

							retorno.append(((respuesta.getTraduce() != null) ? ((TraduccionRespuesta) respuesta.getTraduce()).getTitulo() : "&nbsp;"));
							retorno.append("</label>\n");

							if (respuesta.getTipo().equals("I")) {
								scriptTxtDisabled += "document.encuesta.T" + respuesta.getId() + "_" + pregunta.getId() + ".readonly=true;\n";
								// retorno.append("<input type=\"text\" name=\"T"
								// + respuesta.getId() + "_" + pregunta.getId()
								// + "\"" + readonly + ">");
								String onKeyUp = "onKeyUp=\"marcaCheck('" + respuesta.getId() + "','" + pregunta.getId() + "');\"";
								retorno.append("<p></p><TEXTAREA COLS=\"120\" ROWS=\"2\" " + onKeyUp + " name=\"T" + respuesta.getId() + "_"
										+ pregunta.getId() + "\"" + disabledTxt + "></TEXTAREA>");
								if (param.get(respuesta.getId()) != null) {
									retorno.append("<script>document.getElementById(\"encuesta\").T" + respuesta.getId() + "_" + pregunta.getId()
											+ ".value='" + (String) param.get(respuesta.getId()) + "';</script>");
								}
							}
							retorno.append("</li>\n");
						}
						if (!"".equals(disabledRadio)) {
							retorno.append("<script>\n" + scriptRadioDisabled + "\n" + scriptTxtDisabled + "</script>\n\n");
						}

						retorno.append("</ul>\n");
						retorno.append("</div>\n");
					}
				}
				scriptValidar.append("; \n if (txtError == \"\") { \n document.encuesta.submit(); \n } else { \n alert(\""
						+ rb.getString("encuesta.condiciones") + "\" + txtError); } }\n</script>\n");

				retorno.append("<div id=\"botoneraCom\"><label><input type=\"button\" onClick = \"validaencuesta();\" name=\"btnanar\" value=\""
						+ rb.getString("encuesta.enviar") + "\" /></label></div>\n");
				retorno.append("</form>\n");
				if (encuesta.getMostrar().equalsIgnoreCase("S")) {
					retorno.append("<p style=\"text-align:center;\"><a href=\"envioencuesta.do?" + Microfront.PIDSITE + "=" + idmicrosite + "&"
							+ Microfront.PCONT + "=" + idencuesta + "&" + Microfront.PLANG + "=" + idioma + "&" + Microfront.PVIEW + "=yes\">"
							+ rb.getString("encuesta.verresultados") + "</a></p>\n");
				}

				retorno.append(scriptValidar);
			} else {
				// encuesta no visible o caducada
				retorno.append("<div id=\"enquestaPreguntaCom\">\n");
				retorno.append("<h3>" + ((encuesta.getTraduce() != null) ? ((TraduccionEncuesta) encuesta.getTraduce()).getTitulo() : "&nbsp;")
						+ "</h3>\n");
				retorno.append("<p style=\"text-align:center;\">" + rb.getString("encuesta.caducada") + "</p>\n");
				retorno.append("<p style=\"text-align:center;\"><a href=\"envioencuesta.do?" + Microfront.PIDSITE + "=" + idmicrosite + "&"
						+ Microfront.PCONT + "=" + idencuesta + "&" + Microfront.PLANG + "=" + idioma + "&" + Microfront.PVIEW + "=yes\">"
						+ rb.getString("encuesta.verresultados") + "</a></p>\n");

			}

		} catch (Exception e) {
			log.error("[getHtmlEncuesta]: " + e.getMessage());
			retorno = new StringBuffer("");
		}

		return retorno;
	}

}
