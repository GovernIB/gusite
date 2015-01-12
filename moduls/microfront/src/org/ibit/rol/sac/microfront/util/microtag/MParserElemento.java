package org.ibit.rol.sac.microfront.util.microtag;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.Tipo;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;

/**
 * Parseo de 'elementos'. Esta clase contiene métodos que parsean los tags especiales de los microsites.
 * Los tags son de la version 1.0 y 1.3
 * Devuelven trozos de código HTML pertenecientes a la listados de elementos.
 * 
 * @author Indra
 *
 */
public class MParserElemento extends MParserHTML {

	protected static Log log = LogFactory.getLog(MParserElemento.class);
	
	public MParserElemento(String version) {
		super(version);
	}
	
	/**
	 * Método que devuelve un string preparado para insertar en un html.
	 * Ese string contiene el listado de los últimos elementos de clase 'noticias'.
	 * @param idmicrosite
	 * @param idioma
	 * @param numnoticias
	 * @return StringBuffer que contiene todo el 'html' necesario para montar noticias
	 */	
	public StringBuffer getHtmlNoticias(Long idmicrosite, String idioma, int numnoticias) {
		StringBuffer retorno = new StringBuffer();
		
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		String value = (String)rb.getObject("noticia.noticias");
		try {
	    	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
	    	noticiadel.init();noticiadel.setPagina(1);noticiadel.setTampagina(numnoticias);
	    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
	    	String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + idmicrosite;
	    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
	    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
	    	noticiadel.setWhere(wherenoticias);
	    	noticiadel.setOrderby2(" order by noti.fpublicacion desc");
        	List<?> listanoticias=noticiadel.listarNoticiasThin(idioma);
	        
        	retorno.append("<div id=\"noticies\">");
        	retorno.append("<h2>" + value + "</h2>");
        	if (listanoticias.size()!=0) {
        		retorno.append("<ul>");
        		Iterator<?> iter = listanoticias.iterator();
        		while (iter.hasNext()) {
        				java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("dd/MM/yyyy");
        				Noticia noti = (Noticia)iter.next();
        				//la añadimos si realmente es del tipo noticia
        				if (noti.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
	        				retorno.append("<li>");
	        				if (noti.getImagen()!=null)
	        					retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue()) + "\" width=\"48\" height=\"48\" alt=\"\" />");
	        				if (noti.getFpublicacion()!=null)
	        					retorno.append(dia.format(noti.getFpublicacion()) + " - <em>" + ((TraduccionTipo)noti.getTipo().getTraduccion(idioma)).getNombre() + "</em>");
	        				retorno.append("<br />");
	        				retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
	        				retorno.append(((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a></li>");
        				}
        		}
        		retorno.append("</ul>");
        	} else {
        		retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
        	}
        	retorno.append("</div>");
			if (idmicrosite.longValue()==0) retorno.append("");
	        
		} catch (Exception e) {
			log.error("[getHtmlNoticias]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		
		return retorno;
	}			
	
	
	/**
	 * Método que devuelve un string preparado para insertar en un html.
	 * Ese string contiene el listado de los últimos elementos según el 'tipo' que se pase.
	 * Este método es el que se usa para los tags de la version 1.3
	 * @param idmicrosite
	 * @param txttipo tipo de elemento
	 * @param idioma
	 * @param numnoticias numero de items que se desean mostrar
	 * @return StringBuffer que contiene todo el 'html' necesario para montar el listado
	 */	
	public StringBuffer getHtmlElementos(Long idmicrosite, String txttipo, String idioma, int numnoticias) {
		StringBuffer retorno = new StringBuffer();
		
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		try {
			TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
			Tipo tipo = tipodel.obtenerTipo( new Long(Long.parseLong(txttipo)));
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
	    	noticiadel.init();noticiadel.setPagina(1);noticiadel.setTampagina(numnoticias);
	    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
	    	String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + idmicrosite + " and noti.tipo=" + txttipo;
	    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
	    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
	    	noticiadel.setWhere(wherenoticias);
	    	noticiadel.setOrderby2(preparaOrden(tipo.getOrden()));
        	List<?> listanoticias=noticiadel.listarNoticiasThin(idioma);
	        
        	String nombre_elemento=((TraduccionTipo)tipo.getTraduccion(idioma))!=null?((TraduccionTipo)tipo.getTraduccion(idioma)).getNombre():"[ empty ]";
        	retorno.append("<div id=\"noticies\">");
        	retorno.append("<h2>" + nombre_elemento + "</h2>");
        	if (listanoticias.size()!=0) {
        		retorno.append("<ul>");
        		Iterator<?> iter = listanoticias.iterator();
        		while (iter.hasNext()) {
        				java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("dd/MM/yyyy");
        				Noticia noti = (Noticia)iter.next();

        				retorno.append("<li>");
        				
        				if (tipo.getTipoelemento().equals(Microfront.ELEM_NOTICIA)) { 
		        			if (noti.getImagen()!=null)
		        				retorno.append("<img src=\"" + MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue()) + "\" width=\"48\" height=\"48\" alt=\"\" />");
		        			retorno.append(noti.getFpublicacion()!=null?dia.format(noti.getFpublicacion()):"&nbsp;");
		        			retorno.append((((TraduccionNoticia)noti.getTraduccion(idioma)).getFuente()!=null)?" - <em>" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getFuente() + "</em>":"");
		        			retorno.append("<br />");
		        			retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
		        			retorno.append(((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a>");
        				}
        				if (tipo.getTipoelemento().equals(Microfront.ELEM_LINK)) {
        					retorno.append("<a href=\"" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getLaurl() + "\" target=\"_blank\">" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a>");        					
        				}
        				if (tipo.getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
        					retorno.append("<a href=\"elementodocumento.do?idsite=" + idmicrosite + "&cont=" + noti.getId() + "&lang=" + idioma + "\" target=\"_blank\">" + ((TraduccionNoticia)noti.getTraduccion(idioma)).getTitulo() + "</a>"); 
        				}
        				retorno.append("</li>");
        		}
        		retorno.append("</ul>");
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
	
}
