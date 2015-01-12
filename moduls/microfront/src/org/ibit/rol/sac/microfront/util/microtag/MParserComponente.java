package org.ibit.rol.sac.microfront.util.microtag;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.micromodel.Componente;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.TraduccionComponente;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micropersistence.delegate.ComponenteDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;



/**
 * Parseo de 'componentes'. Esta clase contiene m�todos que parsean los tags especiales de los microsites.
 * Los tags son de la version 1.4
 * Devuelven trozos de c�digo HTML pertenecientes a la listados de elementos.
 * 
 * @author Indra
 */
public class MParserComponente extends MParserHTML {
	
	
	protected static Log log = LogFactory.getLog(MParserComponente.class);

	/**
	 * Constructor de la clase
	 * @param version
	 */
	public MParserComponente(String version) {
		super(version);
	}
	
	/**
	 * M�todo que devuelve un string preparado para insertar en un html.
	 * Ese string contiene el listado de los �ltimos elementos seg�n el 'id' del componente.
	 * Este m�todo es el que se usa para los tags de la version 1.4
	 * @param idmicrosite
	 * @param idcomponente
	 * @param idioma
	 * @return StringBuffer que contiene todo el 'html' necesario para montar el listado
	 */	
	public StringBuffer getHtmlElementosComponente(Long idmicrosite, String idcomponente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		try {
			ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate();
			Componente componente = compodel.obtenerComponente( new Long(Long.parseLong(idcomponente)) );
	        
    		//si es de clase noticia. 
    		if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) { 
    			retorno.append(getHtmlComponenteTNoticia3(idmicrosite,componente,idioma));
    		}
    		//si es de clase enlace. 
    		if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_LINK)) {
    			retorno.append(getHtmlComponenteTEnlace(idmicrosite,componente,idioma));        			
    		}
    		//si es de clase documento
    		if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
    			retorno.append(getHtmlComponenteTDocumento(idmicrosite,componente,idioma));
    		}
    		//si es de clase documento
    		if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_CONEXIO_EXTERNA)) {
    			retorno.append(getHtmlComponenteTExterno(idmicrosite,componente,idioma));
    		}
   

			if (idmicrosite.longValue()==0) retorno.append("");
	        
		} catch (Exception e) {
			log.error("[getHtmlElementosComponente]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		
		return retorno;
	}		
	
	/** 
	 * M�todo privado que devuelve un string para insertar en un HTML un documento externo
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer C�digo HTML
	 */
	private StringBuffer getHtmlComponenteTExterno(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();
		
		try {
	        String nombreformulario = "formnoticiasexternocomp"+componente.getId().longValue();
        	String nombre_elemento=((TraduccionComponente)componente.getTraduccion(idioma))!=null?((TraduccionComponente)componente.getTraduccion(idioma)).getTitulo():"[ empty ]";
        	
			//al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(configStyle3(idmicrosite, componente));
        	retorno.append("<div id=\"element").append(componente.getId()).append("\">");          	
        	retorno.append("<h2>").append(nombre_elemento).append("</h2>");
        	retorno.append("<form action=\"noticias.do\" name=\"").append(nombreformulario).append("\" id=\"").append(nombreformulario).append("\" >");
        	
        	retorno.append("<input type=\"hidden\" name=\"lang\" value=\"").append(idioma).append("\">");
        	retorno.append("<input type=\"hidden\" name=\"nameform\" value=\"").append(nombreformulario).append("\">");
        	
			TipoDelegate tD = DelegateUtil.getTipoDelegate();
			
			//preparamos el map a enviar
			Map<String, String> filtrado = new HashMap<String, String>();
			filtrado.put("lang", idioma);
			filtrado.put("nameform", nombreformulario);
			filtrado.put("idcomponente", "" + componente.getId().longValue());
			
			String resultadoHTML = tD.obtenerPegoteHTMLExterno(componente.getTipo().getId(),filtrado);
        	
			//retorno.append("<style>").append(resultado.get(Microfront.MAP_EXT_MCSTYLE)).append("</style>");
			retorno.append(resultadoHTML);
			//retorno.append("<script>").append(resultado.get(Microfront.MAP_EXT_MCJAVASCRIPT)).append("</script>");

        	retorno.append("</form></div>");
			if (idmicrosite.longValue()==0) retorno.append("");
	        
		} catch (Exception e) {
			log.error("[getHtmlComponenteTExterno]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		
		return retorno;
	}	
	
	/**
	 * M�todo privado que devuelve un string para insertar en un HTML una Noticia
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer C�digo Html
	 */
	private StringBuffer getHtmlComponenteTNoticia3(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();
		
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		try {
			
			
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
	    	noticiadel.init();noticiadel.setPagina(1);noticiadel.setTampagina(componente.getNumelementos().intValue());
	    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
	    	String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + idmicrosite + " and noti.tipo=" + componente.getTipo().getId().toString();
	    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
	    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
	    	noticiadel.setWhere(wherenoticias);
	    	noticiadel.setOrderby2(preparaOrden(componente.getOrdenacion().toString()));
        	List<?> listanoticias=noticiadel.listarNoticiasThin(idioma);
	        
        	String nombre_elemento=((TraduccionComponente)componente.getTraduccion(idioma))!=null?((TraduccionComponente)componente.getTraduccion(idioma)).getTitulo():"[ empty ]";
        	
			//al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(configStyle3(idmicrosite, componente));
        	retorno.append("<div id=\"element" + componente.getId() + "\">");          	
        	retorno.append("<h2>" + nombre_elemento + "</h2>");
        	
        	if (listanoticias.size()!=0) {
        		
        			retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
            		Iterator<?> iter = listanoticias.iterator();
            		int cont=0;
            		// Si SoloImagen, es mostra en forma de mosaic a dues columnes
            		String nCols = (componente.getSoloimagen().equals("S"))?"":"<tr>";
            		while (iter.hasNext()) {
            				java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("dd/MM/yyyy");
            				Noticia noti = (Noticia)iter.next();

            				retorno.append(((cont%2)==0)?"<tr class=\"par\">":nCols);
            				if (componente.getSoloimagen().equals("S")) {
            					//en el caso de que s�lo im�genes
            					retorno.append("<td style=\"width:1%\" >");
            					retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
    		        			if (noti.getImagen()!=null)
    		        				retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue()) + "\" alt=\"\" class=\"imagen\" />");
    		        			else {
	            					if (componente.getImagenbul()!=null) {
	            						retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RMICROSITE, idmicrosite.longValue(), componente.getImagenbul().getId().longValue()) + "\" alt=\"" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />&nbsp;");
	            					} else {
	            						retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" /> &nbsp;");
	            					}
    		        				retorno.append(((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo());
    		        			}
    		        			retorno.append("</a>");
    		        			retorno.append("</td><td>&nbsp;</td> \n");
    		        			
            				} else {
            					retorno.append("<td style=\"width:1%\" >");
    		        			if (noti.getImagen()!=null)
    		        				retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue()) + "\" width=\"48\" height=\"48\" alt=\"\" class=\"imagen\" width=\"266\" height=\"127\" />&nbsp;");
    		        			else{
	            					if (componente.getImagenbul()!=null) {
	            						retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RMICROSITE, idmicrosite.longValue(), componente.getImagenbul().getId().longValue()) + "\" alt=\"" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />&nbsp;");
	            					} else {
	            						retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" /> &nbsp;");
	            					}
    		        			}
    		        			retorno.append(noti.getFpublicacion()!=null?dia.format(noti.getFpublicacion()):"");
    		        			retorno.append("&nbsp;<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
    		        			retorno.append(((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a>");
    		        			String subTitol = ((TraduccionNoticia)noti.getTraduccion(idioma)).getSubtitulo();
    		        			retorno.append("<br/>" + (subTitol==null?"":subTitol) + "<br/>");
    		        			
    		        			retorno.append("</td> \n");
            				}
            				retorno.append(((cont%2)==0)?nCols:"</tr> \n");
            				cont++;
            		}
            		retorno.append("</table> \n");

					/*
					<div id="element58u">
					<h2>Not&iacute;cies</h2><!-- El class per a un enlla� intern es 'enlaceInterno' -->
					<table border="0" cellPadding="0" cellSpacing="0" id="element58u">
							<tr>
								<td><img width="26" src="imgs/noticies/news.gif" height="28" style="width: 26px; height: 28px" /> </td>
								<td vAlign="top"><img width="64" src="imgs/noticies/news.gif" height="67" style="width: 64px; height: 67px" class="imagen" /> 07/02/2007 - <span class="font">Computing</span> <span class="enllas">La Comunitat Aut&ograve;noma de les Illes Balears compleix en administraci&oacute; electr&ograve;nica</span><span class="enllas">asdf asdfa</span> <span class="enllas"></span></td>
							</tr>
					</table>
					 */            		
        	} else {
        		retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
        	}
        	retorno.append("</div>");
			if (idmicrosite.longValue()==0) retorno.append("");
	        
		} catch (Exception e) {
			log.error("[getHtmlComponenteTNoticia]: " + e.getMessage());
			retorno= new StringBuffer("");
		}		
		
		return retorno;
	}	
	
	/**
	 * M�todo privado que devuelve un string para insertar en un HTML un Enlace
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer C�digo HTML
	 */
	private StringBuffer getHtmlComponenteTEnlace(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		
		try {
			
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
	    	noticiadel.init();noticiadel.setPagina(1);noticiadel.setTampagina(componente.getNumelementos().intValue());
	    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
	    	String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + idmicrosite + " and noti.tipo=" + componente.getTipo().getId().toString();
	    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
	    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
	    	noticiadel.setWhere(wherenoticias);
	    	noticiadel.setOrderby2(preparaOrden(componente.getOrdenacion().toString()));
        	List<?> listanoticias=noticiadel.listarNoticiasThin(idioma);
	        
        	String nombre_elemento=((TraduccionComponente)componente.getTraduccion(idioma))!=null?((TraduccionComponente)componente.getTraduccion(idioma)).getTitulo():"[ empty ]";
        	
			//al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(configStyle3(idmicrosite, componente));
        	retorno.append("<div id=\"element" + componente.getId() + "\">");          	
        	retorno.append("<h2>" + nombre_elemento + "</h2>");
        	if (listanoticias.size()!=0) {

        			retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
            		Iterator<?> iter = listanoticias.iterator();
            		int cont=0;
            		// Si SoloImagen, es mostra en forma de mosaic a dues columnes
            		String nCols = (componente.getSoloimagen().equals("S"))?"":"<tr>";
            		while (iter.hasNext()) {
            				Noticia noti = (Noticia)iter.next();
            				retorno.append(((cont%2)==0)?"<tr class=\"par\">":nCols);
            				if (componente.getSoloimagen().equals("S") && (noti.getImagen()!=null)) {
            					//en el caso de que s�lo im�genes
            					retorno.append("<td style=\"width:1%\" >");
            					retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
    		        			if (noti.getImagen()!=null)
    		        				retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue()) + "\" alt=\"" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />");
    		        			else 
    		        				retorno.append(((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo());
    		        			retorno.append("</a>");
    		        			retorno.append("</td><td>&nbsp;</td> \n");
    		        			
            				} else {
            					retorno.append("<td style=\"width:1%\" >");
            					if (componente.getImagenbul()!=null) {
            						retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RMICROSITE, idmicrosite.longValue(), componente.getImagenbul().getId().longValue()) + "\" alt=\"\" class=\"imagen\" />");
            					} else {
            						retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" />");	
            					}
            					
    		        			boolean urlExt=((TraduccionNoticia)noti.getTraduccion(idioma)).getLaurl().startsWith("http:");
    		        			String target=(urlExt)?"_blank":"_self";
    		        			retorno.append("<a href=\"" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getLaurl() + "\" target=\""+target+"\">&nbsp;" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a>"); 		        			
    		        			
    		        			if (((TraduccionNoticia)noti.getTraduccion(idioma)).getSubtitulo()!=null){
    		        				retorno.append("<br/>" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getSubtitulo() );
    		        			}
    		        			if (((TraduccionNoticia)noti.getTraduccion(idioma)).getTexto()!=null){
    		        					retorno.append("<br/>" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTexto() );
    		        			}
    		        			retorno.append("</td> \n");
            				}
            				retorno.append(((cont%2)==0)?nCols:"</tr> \n");
            				cont++;
            		}
            		retorno.append("</table> \n");        	
        		   
        	} else {
        		retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
        	}
        	retorno.append("</div>");
			if (idmicrosite.longValue()==0) retorno.append("");
	        
		} catch (Exception e) {
			log.error("[getHtmlComponenteTEnlace]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		
		return retorno;
	}	
	
	/**
	 * M�todo privado que devuelve un string para insertar en un HTML un documento
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer C�digo HTML
	 */
	private StringBuffer getHtmlComponenteTDocumento(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		try {

			
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
	    	noticiadel.init();noticiadel.setPagina(1);noticiadel.setTampagina(componente.getNumelementos().intValue());
	    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
	    	String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + idmicrosite + " and noti.tipo=" + componente.getTipo().getId().toString();
	    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
	    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
	    	noticiadel.setWhere(wherenoticias);
	    	noticiadel.setOrderby2(preparaOrden(componente.getOrdenacion().toString()));
        	List<?> listanoticias=noticiadel.listarNoticiasThin(idioma);
	        
        	String nombre_elemento=((TraduccionComponente)componente.getTraduccion(idioma))!=null?((TraduccionComponente)componente.getTraduccion(idioma)).getTitulo():"[ empty ]";
        		
			//al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(configStyle3(idmicrosite, componente));
        	retorno.append("<div id=\"element" + componente.getId() + "\">");          	
        	retorno.append("<h2>" + nombre_elemento + "</h2>");
        	
        	if (listanoticias.size()!=0) {

        			retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
            		Iterator<?> iter = listanoticias.iterator();
            		int cont=0;
            		// Si SoloImagen, es mostra en forma de mosaic a dues columnes
            		String nCols = (componente.getSoloimagen().equals("S"))?"":"<tr>";
            		while (iter.hasNext()) {
            				
            				Noticia noti = (Noticia)iter.next();
            				retorno.append(((cont%2)==0)?"<tr class=\"par\">":nCols);
            				if (componente.getSoloimagen().equals("S") && (noti.getImagen()!=null)) {
            					//en el caso de que s�lo im�genes
            					retorno.append("<td style=\"width:1%\" >");
            					retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
    		        			if (noti.getImagen()!=null)
    		        				retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue()) + "\" alt=\"" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />");
    		        			else 
    		        				retorno.append(((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo());
    		        			retorno.append("</a>");
    		        			retorno.append("</td><td>&nbsp;</td> \n");
    		        			
            				} else {
            					retorno.append("<td style=\"width:1%\" >");
            					if (componente.getImagenbul()!=null) {
            						retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RMICROSITE, idmicrosite.longValue(), componente.getImagenbul().getId().longValue()) + "\" alt=\"\" class=\"imagen\" />");
            					} else {
            						retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" />");
            					}
    		        			retorno.append("<a href=\"elementodocumento.do?idsite=" + idmicrosite + "&cont=" + noti.getId() + "&lang=" + idioma + "\" target=\"_blank\">&nbsp;" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a>");
    		        			if (((TraduccionNoticia)noti.getTraduccion(idioma)).getTexto()!=null)
    		        				retorno.append("<br/>" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTexto() );
    		        			retorno.append("</td> \n");
            				}
            				retorno.append(((cont%2)==0)?nCols:"</tr> \n");
            				cont++;
            		}
            		retorno.append("</table> \n");           	
        		
        	} else {
        		retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
        	}
        	retorno.append("</div>");
			if (idmicrosite.longValue()==0) retorno.append("");
	        
		} catch (Exception e) {
			log.error("[getHtmlElementos]: " + e.getMessage());
			retorno= new StringBuffer("");
		}		
		
		return retorno;
	}	
	
	/**
	 * M�todo que crea un bloque completo del tag de html "<style>"
	 * @param idmicrosite
	 * @param componente
	 * @return StringBuffer con todo el bloque '<style>'
	 */
	private StringBuffer configStyle3(Long idmicrosite, Componente componente) {
		StringBuffer retorno = new StringBuffer();
		
    	//se monta todo en un estilo, luego simplemente hay que aplicarlo
		retorno.append("<style> " + "\n");
		//retorno.append("div#element" + componente.getId() + " h2 { padding:.5em 0 0 2em; border-top: #85bbe4 2px solid; background: url(imgs/titol/ico_blau.gif) #fff no-repeat left top; color: #00276c;  } \n");
		if (componente.getSoloimagen().equals("S")) {
			retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
			retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }   \n");
			
		} else {
			if (componente.getImagenbul()!=null) {
				retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
				retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }  \n");				
			} else {
				if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
					retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
					retorno.append("div#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
				} else {
					retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
					retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }  \n");					
				}
			}
		}
		if (componente.getFilas().equals("S")) {
			retorno.append("table#element" + componente.getId() + " tr.par { background-color:#EFEFEF; } \n");
		}
		
		retorno.append("table#element" + componente.getId() + " tr td span.font { font-weight: bold; font-style: italic } \n");
		retorno.append("table#element" + componente.getId() + " tr td span.enllas { margin-top: 0.2em; display: block; padding-left: 0.2em } \n");
		retorno.append("table#element" + componente.getId() + " tr td img { margin-right:0em; } \n");
		retorno.append("table#element" + componente.getId() + " tr td img.imagen { float:left; margin-right:.3em; padding-right:.2em; padding-bottom:.8em;} \n");
		retorno.append(" </style>" + "\n");
		
		return retorno;
	}		
	
	
	/* 
	<style>
	div#prueba h2 { padding-right: 0px; border-top: #85bbe4 2px solid; padding-left: 2em; background: url(../imgs/titol/ico_blau.gif) #fff no-repeat left top; padding-bottom: 0px; color: #00276c; padding-top: 0.5em }
	div#prueba ul { margin:0; padding:0; }
	div#prueba ul li { clear: left; margin-bottom: 1em; margin-left: 1em; list-style-type: none }
	div#prueba ul li img { float: left; margin-right: 0.8em }
	div#prueba ul li span.font { font-weight: bold; font-style: italic }
	div#prueba ul li span.enllas { margin-top: 0.3em; display: block; padding-left: 6em }
	a.enlaceinterno { padding-left: 2.5em; background: url(../imgs/noticies/enlace_interno.gif) no-repeat 0px 0.2em }
	a.enlaceexterno { padding-left: 2.5em; background: url(../imgs/noticies/enlace_externo.gif) no-repeat 0px 0.2em }
	a.docpdf { padding-left: 2.5em; background: url(../imgs/noticies/doc_pdf.gif) no-repeat 0px 0.2em }
	</style>	 
	<div id="noticies">
		<h2>Not�cies</h2>
		<!-- El class per a un enlla� intern es 'enlaceInterno' -->
		<ul>
			<li><img src="imgs/noticies/news.gif" alt="" />
				07/02/2007 - <span class="font">Computing</span>
				<span class="enllas">La Comunitat Aut�noma de les Illes Balears compleix en administraci� electr�nica</span>
				<span class="enllas"><a href="noticias/Noticia_20070207_CP.pdf" class="docPDF">Descarregar not�cia en format PDF de 352 kb</a></span>
			</li>
			<li><img src="imgs/noticies/news.gif" alt="" />
				31/01/2007 - <span class="font">Europa Press</span>
				<span class="enllas"><a href="http://www.europapress.es/noticia.aspx?cod=20070131121003&ch=239" class="enlaceExterno">El conseller d�Economia, Hisenda i Innovaci�, Llu�s Ramis de Ayreflor, presenta els �ltims avan�os en la implantaci� i desenvolupament del Pla d�Administraci� Digital de Balears</a></span>
				<span class="enllas"><a href="noticias/Noticia_20070131_EP.pdf" class="docPDF">Descarregar not�cia en format PDF de 67 kb</a></span>
			</li>
			<li><img src="imgs/noticies/news.gif" alt="" />
				18/01/2007 - <span class="font">Diario de Mallorca</span>
				<span class="enllas"><a href="http://www.diariodemallorca.es/secciones/noticia.jsp?pNumEjemplar=1415&pIdSeccion=903&pIdNoticia=236821&rand=1169120257342" class="enlaceExterno">El Govern telematitzar� els 80 procedimients administratius m�s demandats pels usuaris</a></span>
				<span class="enllas"><a href="noticias/Noticia_20070118_DMall.pdf" class="docPDF">Descarregar not�cia en format PDF de 79 kb</a></span>
			</li>
		</ul>
		
	</div>	 
*/

}
