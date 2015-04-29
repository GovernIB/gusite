package es.caib.gusite.front.microtag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 * Método HTML para parsear un HTML
 * 
 * @author Indra
 * 
 *         (esta clase recibe un objeto Microsite y genera codigo html) TODO
 *         cambiar los getXXXX() por generaXXXX() TODO esta clase parece una
 *         duplicacion de microback.MParserHTML
 * 
 */
public class MParserHTML {

	public MParserHTML() {
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el trozo de html que muestra la imagen de la campaña.
	 * 
	 * @param microsite
	 * @param idioma
	 * @return StringBuffer con el códig HTML
	 */
	public StringBuffer getHtmlCampanya(Microsite microsite, String idioma) {
		StringBuffer retorno = new StringBuffer();

		if ((microsite.getTraduccion(idioma) != null) && (microsite.getImagenCampanya() != null)) {
			String Titulo = ((TraduccionMicrosite) microsite.getTraduccion(idioma)).getTitulo();
			String SubtituloCampanya = ((TraduccionMicrosite) microsite.getTraduccion(idioma)).getSubtitulocampanya();
			String TituloCampanya = ((TraduccionMicrosite) microsite.getTraduccion(idioma)).getTitulocampanya();
			String UrlCampanya = microsite.getUrlcampanya();

			if (Titulo == null) {
				Titulo = "";
			}
			if (SubtituloCampanya == null) {
				SubtituloCampanya = "";
			}
			if (TituloCampanya == null) {
				TituloCampanya = "";
			}

			retorno.append("<div id=\"enllasDest\" style=\"background:url("
					+ MicroURI.uriImg(Microfront.RMICROSITE, microsite.getId().longValue(), microsite.getImagenCampanya().getId().longValue())
					+ ") no-repeat #fff;\" >");
			retorno.append("<h2 class=\"invisible\">Destaquem</h2>");
			if (UrlCampanya != null) {
				retorno.append("<a id=\"enllas\" href=\"" + UrlCampanya + "\" title=\"" + SubtituloCampanya + "\">");
				retorno.append("<span class=\"frase1\">" + TituloCampanya + "</span>");
				retorno.append("<br />");
				retorno.append("<span class=\"frase2\">" + SubtituloCampanya + "</span>");
				retorno.append("</a></div>");
			} else {
				retorno.append("<div id=\"enllas\" >");
				retorno.append("<span class=\"frase1\">" + TituloCampanya + "</span>");
				retorno.append("<br />");
				retorno.append("<span class=\"frase2\">" + SubtituloCampanya + "</span>");
				retorno.append("</div></div>");
			}
			retorno.append("<div id=\"enllasDestPeu\"></div>");
		}
		return retorno;
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el trozo del selector
	 * 
	 * @param nombre
	 * @param valores
	 * @param tipo
	 * @param lineas
	 * @param obligatorio
	 * @return StringBuffer Código HTML
	 */
	public StringBuffer getTagSelect(String nombre, ArrayList<?> valores, String tipo, int lineas, int obligatorio) {
		StringBuffer retorno = new StringBuffer();
		/*
		 * <select name="6"> <select name="6" size="4" multiple> <option
		 * value="a">a</option> </select>
		 */
		retorno.append("<select name=\"" + ((obligatorio == 1) ? Microfront.VCAMPO_REQUERIDO : "") + nombre);
		retorno.append("\" id=\"" + nombre + "\"");
		if (tipo.equals(Contacto.RTYPE_SELECTORMULTIPLE)) {
			if (lineas <= 1) {
				retorno.append(" size=\"5\""); // por defecto 5
			} else {
				retorno.append(" size=\"" + valores.size() + "\"");
			}
			retorno.append(" multiple");
		}
		retorno.append(">");

		Iterator<?> iter = valores.iterator();
		while (iter.hasNext()) {
			String opcion = (String) iter.next();
			retorno.append("<option value=\"" + opcion + "\">" + opcion + "</option>");
		}

		retorno.append("</select> " + ((obligatorio == 1) ? "*" : ""));
		return retorno;
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el trozo del TextArea
	 * 
	 * @param nombre
	 * @param columnas
	 * @param lineas
	 * @param obligatorio
	 * @return StringBuffer
	 */
	public StringBuffer getTagTextarea(String nombre, int columnas, int lineas, int obligatorio) {
		StringBuffer retorno = new StringBuffer();
		// <textarea name="5" cols="34" rows="5"></textarea>
		retorno.append("<textarea name=\"" + ((obligatorio == 1) ? Microfront.VCAMPO_REQUERIDO : "") + nombre);
		retorno.append("\" id=\"" + nombre + "\" cols=\"" + columnas + "\" rows=\"" + lineas + "\"></textarea> " + ((obligatorio == 1) ? "*" : ""));
		return retorno;
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el trozo del tag de texto
	 * 
	 * @param nombre
	 * @param longitudtexto
	 * @param obligatorio
	 * @return StringBuffer
	 */
	public StringBuffer getTagText(String nombre, int longitudtexto, int obligatorio) {
		StringBuffer retorno = new StringBuffer();
		// <input name="4" id="4" type="text" />
		retorno.append("<input name=\"" + ((obligatorio == 1) ? Microfront.VCAMPO_REQUERIDO : "") + nombre);
		retorno.append("\" id=\"" + nombre);
		retorno.append("\" type=\"text\" size=\"" + (longitudtexto + 2) + "\" maxlength=\"" + longitudtexto + "\" /> "
				+ ((obligatorio == 1) ? "*" : ""));
		return retorno;
	}

	/**
	 * Método que dado un tag html devuelve el valor de un atributo.
	 * 
	 * @param cadena
	 *            en la que hay que buscar el atributo
	 * @param atributo
	 *            en cuestión
	 * @return String con el valor del atributo solicitado
	 */
	public String getAtributoTag(String cadena, String atributo) {
		String retorno = "-1";

		cadena = cadena.toLowerCase();
		String str[] = null;
		String strpropertyid[] = null;

		StringTokenizer st = new StringTokenizer(cadena, " ");
		int n = st.countTokens();
		if (n > 2) {
			str = new String[n];
			for (int i = 0; i < n; i++) {
				str[i] = st.nextToken();
				int pos_propertyid = str[i].indexOf(atributo);
				if (pos_propertyid != -1) {
					StringTokenizer stprop = new StringTokenizer(str[i], "=");
					int nprop = stprop.countTokens();
					strpropertyid = new String[nprop];
					strpropertyid[0] = stprop.nextToken();
					strpropertyid[1] = stprop.nextToken();

					if (nprop == 2) { // al segundo hay que comprobar si tiene
										// comillas y quitarselas
						if (strpropertyid[1].indexOf("\"") != -1) {
							retorno = strpropertyid[1].substring(1, strpropertyid[1].length() - 1);
						} else {
							retorno = strpropertyid[1];
						}
						break;
					}

				}
			}
		}

		return retorno;
	}

	public String getAtributoTag2(String cadena, String atributo, String idioma) {
		String retorno = "-1";
		String str[] = null; // cadena=cadena.toLowerCase();

		StringTokenizer st = new StringTokenizer(cadena, " ");
		int n = st.countTokens();
		if (n > 2) {
			str = new String[n];
			for (int i = 0; i < n; i++) {
				str[i] = st.nextToken();
				int pos_propertyid = str[i].indexOf(atributo);
				if (pos_propertyid != -1) {
					retorno = str[i];
					break;
				}
			}
		}
		// sacamos el titulo , se puede optimizar
		String retorno2 = "";
		int longi = cadena.indexOf("<b>");
		int longi2 = cadena.indexOf("</b>");
		retorno2 = cadena.substring(longi + 3, (longi2));
		retorno = retorno.substring(0, (retorno.length() - 1));
		retorno = "<a href=" + (retorno.substring(13)) + "&language=" + idioma + "\">" + retorno2 + "</a>";

		return retorno;
	}

	/**
	 * String con el tag css. MCR v1.1
	 * 
	 * @param idsite
	 * @param idcss
	 * @param idcsspatron
	 * @return String
	 * @deprecated Comprobar que ya no se usa y eliminar
	 */
	public String tagCSS(Long idsite, Long idcss, String idcsspatron) {
		// <link href="css/estilos.css" rel="stylesheet" type="text/css" />
		String retorno = "";

		// TODO: esto construye HTML. Mejor que retorne una lista de estilos y
		// las urls se contruyan en la plantilla.
		// Por ahora, para que funcione, añadimos /sites/resources/

		if ((idsite != null) && (idcss != null)) {
			retorno = "<link href=\"/sites/" + MicroURI.uriImg(Microfront.RMICROSITE, idsite.longValue(), idcss.longValue())
					+ "\" rel=\"stylesheet\" type=\"text/css\" />";
			retorno += "<link href=\"/sites/resources/css/estils_print.css\" rel=\"stylesheet\" type=\"text/css\" media=\"print\" />";
		} else {
			retorno = "<link href=\"/sites/resources/css/estils.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />";
			retorno += "<link href=\"/sites/resources/css/estils_print.css\" rel=\"stylesheet\" type=\"text/css\" media=\"print\" />";
			if (idcsspatron.equals("A")) {
				retorno += "<link href=\"/sites/resources/css/estils_blau.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />";
			}
			if (idcsspatron.equals("R")) {
				retorno += "<link href=\"/sites/resources/css/estils_roig.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />";
			}
			if (idcsspatron.equals("V")) {
				retorno += "<link href=\"/sites/resources/css/estils_verd.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />";
			}
			if (idcsspatron.equals("G")) {
				retorno += "<link href=\"/sites/resources/css/estils_groc.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />";
			}
			if (idcsspatron.equals("M")) {
				retorno += "<link href=\"/sites/resources/css/estils_morat.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />";
			}
		}

		return retorno;
	}

	/**
	 * método que devuelve un numero aleatorio. Utiliza el
	 * Microfront.contadorbanner que es un Long genérico a la aplicacion. Es una
	 * forma de garantizar que si se piden dos banners a la vez no se da el
	 * mismo.
	 * 
	 * @param maximo
	 * @return int
	 */
	public int getNumeroaleatorio(int maximo) {
		Random rand = new Random();
		Microfront.contadorbanner = new Long(Microfront.contadorbanner.longValue() + 1);

		Long operando1 = new Long(Microfront.contadorbanner.longValue() + rand.nextInt(maximo));

		Long operando2 = new Long(maximo);
		int resto = operando1.intValue() % operando2.intValue();

		int retorno = 0;
		retorno = resto + 1;
		return retorno;
	}

	protected String preparaOrden(String ordenacion) {
		String sqlOrder = "";
		if (ordenacion.equals("0")) {
			sqlOrder = " order by noti.orden ";
		}
		if (ordenacion.equals("1")) {
			sqlOrder = " order by noti.fpublicacion ";
		}
		if (ordenacion.equals("2")) {
			sqlOrder = " order by noti.fpublicacion desc";
		}
		if (ordenacion.equals("3")) {
			sqlOrder = " order by trad.titulo ";
		}
		return sqlOrder;
	}

}
