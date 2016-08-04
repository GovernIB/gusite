package es.caib.gusite.microback.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.extractor.taw.TawResultBean;
import es.caib.gusite.extractor.tidy.TidyResultBean;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.utils.microtag.MicrositeParser;
import es.caib.gusite.microback.utils.w3c.Testeador;
import es.caib.gusite.micromodel.Accesibilidad;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.utilities.statusbar.StatusBar;
import es.caib.gusite.utilities.statusbar.StatusBarFactory;

/**
 * Clase que define el proceso de validación W3C
 * 
 * @author Indra
 *
 */
public class ProcesoW3C {

	protected static Log log = LogFactory.getLog(ProcesoW3C.class);
	
	private StatusBar statusbar = null;
	
	//delegados a utilizar
	protected AccesibilidadDelegate _accDel = null;
	protected IdiomaDelegate _bdIdi = null;
	protected ContenidoDelegate _bdCon = null;
	protected NoticiaDelegate _bdNot = null;
	protected AgendaDelegate _bdAge = null;
	
	//lista de idiomas
	protected List<?> lista = null;
	
	//microsite
	protected Long idmicrosite;
	
	//servidor en el que se está ejecutando
	protected String _servidor="localhost";
	protected String _puerto="80";
	protected String _protocolo="http://";
	
	public ProcesoW3C(HttpServletRequest request) {
		idmicrosite = ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
		StringBuffer urlbuf = request.getRequestURL();
		if (urlbuf==null) urlbuf = new StringBuffer(request.getHeader("referer"));
		if (urlbuf==null) urlbuf = new StringBuffer("http://localhost");
		int posSeparadorProtocol=urlbuf.indexOf("//");
		_protocolo = (urlbuf.substring(0, posSeparadorProtocol+2)).toString();
		_servidor = request.getServerName();
		_puerto = "" + request.getServerPort();
		statusbar = new StatusBar();
		statusbar.setIdentificador("statusDUMMY");
		_accDel = DelegateUtil.getAccesibilidadDelegate();
		_bdIdi = DelegateUtil.getIdiomaDelegate();
		_bdCon = DelegateUtil.getContenidoDelegate();
		_bdAge = DelegateUtil.getAgendaDelegate();
		_bdNot = DelegateUtil.getNoticiasDelegate();
		
		try {
			lista = (List<?>)_bdIdi.listarLenguajes();
		} catch (DelegateException e) {
			log.error("No se ha podido recuperar la lista de idiomas");
			lista = new ArrayList<Object>();
		}
	}
	
	/**
	 * Constructor al que se le pasa una barra de estado. <br/>
	 * El estado de la barra de estado se irá actualizando durante el proceso.<br/>
	 * @param idstatusbar String identifcador de un <em>StatusBar</em>
	 * @param idsite Long identificador del microsite
	 * @param remotehost String con el nombre de la máquina que ejecuta el servlet
	 */
	public ProcesoW3C(String idstatusbar, Long idsite, String remotehost, String port, String protocol) {
		idmicrosite = idsite;
		_servidor=remotehost;
		_puerto = port;
		_protocolo = protocol;
		/*
	    servidor = "http://"+ Microback._HOSTCAIB;
    	String value = System.getProperty("es.indra.caib.rolsac.oficina");
        if ((value == null) || value.equals("N"))
        	servidor = "localhost";
		*/
		
		if (idstatusbar!=null) statusbar = StatusBarFactory.obtenerStatusBar(idstatusbar);
		else {
			statusbar = new StatusBar();
			statusbar.setIdentificador("statusDUMMY");
		}
		_accDel = DelegateUtil.getAccesibilidadDelegate();
		_bdIdi = DelegateUtil.getIdiomaDelegate();
		_bdCon = DelegateUtil.getContenidoDelegate();
		_bdAge = DelegateUtil.getAgendaDelegate();
		_bdNot = DelegateUtil.getNoticiasDelegate();
		
		try {
			lista = (List<?>)_bdIdi.listarLenguajes();
		} catch (DelegateException e) {
			log.error("No se ha podido recuperar la lista de idiomas");
			lista = new ArrayList<Object>();
		}
		
	}

	
	/**
	 * Proceso que repasa la accesibilidad de un microsite compelto.<br/>
	 * 
	 */
	public void testearMicrosite() {
		try {
			//recoger todo: contenidos, noticias, agendas, ...
			List<?> contenidos = _bdCon.listarAllContenidos(idmicrosite.toString());
			_bdAge.init(idmicrosite); _bdAge.setPagina(1); _bdAge.setTampagina(Microback.MAX_INTEGER);
			List<?> agendas = _bdAge.listarAgendas();
			_bdNot.init(idmicrosite); _bdNot.setPagina(1); _bdNot.setTampagina(Microback.MAX_INTEGER);
			List<?> noticias = _bdNot.listarNoticias();

			//establecemos el total de la barra de estado
			statusbar.setTotalitems(new Long( contenidos.size() + agendas.size() + noticias.size() ));
			
			long cont = 0;
			Iterator<?> iter = contenidos.iterator();
			while (iter.hasNext()) {
				Contenido contenido = (Contenido)iter.next();
				testeoW3C(contenido);
				cont++;
				statusbar.setActualitem(new Long(cont));
			}
			
			
			iter = agendas.iterator();
			while (iter.hasNext()) {
				Agenda agenda = (Agenda)iter.next();
				testeoW3C(agenda);
				cont++;
				statusbar.setActualitem(new Long(cont));
			}
			
			iter = noticias.iterator();
			while (iter.hasNext()) {
				Noticia noticia = (Noticia)iter.next();
				testeoW3C(noticia);
				cont++;
				statusbar.setActualitem(new Long(cont));
			}

			
			statusbar.setActualitem(statusbar.getTotalitems());
		} catch (Exception e) {
			StatusBarFactory.removerStatusBar(statusbar.getIdentificador());
		}
	}
	
	
	/**
	 * Testea la accesibilidad de un <em>contenido</em>.<br/>
	 * Retorna un string con los errores o warnings detectados.<br/>
	 * Además guarda los registros correspondientes.<br/>
	 * En caso de no haber ningún error ni warning, retorna <em>null</em>.<br/>
	 * @param conte Bean Contenido
	 * @return String con el resultado serializado.
	 */
	public String testeoW3C(Contenido conte) {
		String retorno = "";
		Accesibilidad accse = new Accesibilidad();
		try {
			TidyResultBean resultado;
			TawResultBean resultadotaw;
			
			for (int i = 0; i < lista.size(); i++) {
				String idi = (String)lista.get(i);
				if (idi.equals("null")) break;
				accse = _accDel.obtenerAccesibilidad(Catalogo.SRVC_MICRO_CONTENIDOS, conte.getId(), idi);
				if (accse==null) accse = new Accesibilidad();
				TraduccionContenido tracon = (TraduccionContenido)conte.getTraduccion(idi);
				if ((tracon!=null) && (tracon.getTexto()!=null) && (tracon.getTexto().length()>0)) {
					
					// ******************** Para evitar los tags de los componentes.  ******************************** //
					MicrositeParser microparser = new MicrositeParser("2",tracon.getTexto(), idmicrosite, idi, 2);
					microparser.doParser2Comentario(idi);
					String html2analizar = microparser.getHtmlParsed().toString();
					
					//Analizamos con el Tidy
					resultado = Testeador.testeoPegoteHTML(html2analizar);
					
					//Analizamos con el Taw. Se monta una url preparada para sera analizada por el taw.
					//ejemplo: sacmicrofront/taw.do?ttr=CNTSP&idioma=ca&id=1658&idsite=109
					String urlTest = _protocolo + _servidor + ":" + _puerto + "/sacmicrofront/taw.do?ttr=CNTSP&idioma=" + idi + "&id=" + conte.getId() + "&idsite=" + idmicrosite;
					resultadotaw = Testeador.testeoTaw(urlTest);
					
					
					if ((resultado.getErrores()>0) || (resultado.getWarnings()>0) || (resultadotaw.getErrores()>0) || (resultadotaw.getWarnings()>0)) {
						accse.setIdioma(idi);
						accse.setServicio(Catalogo.SRVC_MICRO_CONTENIDOS);
						accse.setIditem(conte.getId());
						accse.setCodmicro(idmicrosite);
						accse.setMedida(Accesibilidad.MES_SINMEDIA);
						
						if (resultadotaw.getErrores()>0) accse.setTawresultado(Accesibilidad.RES_ERROR);
						else if (resultadotaw.getWarnings()>0) accse.setTawresultado(Accesibilidad.RES_WARN);
						else accse.setTawresultado(Accesibilidad.RES_OK);
						if ((resultadotaw.getErrores()>0) || (resultadotaw.getWarnings()>0)) {
							accse.setTawmensaje(resultadotaw.getMensaje());
							retorno +="\n\n[T.A.W (Idioma:" + idi + ")]\n" + resultadotaw.getMensaje().toString();
							
						}

						if (resultado.getErrores()>0)  accse.setResultado(Accesibilidad.RES_ERROR);
						else if (resultado.getWarnings()>0)  accse.setResultado(Accesibilidad.RES_WARN);
						else accse.setResultado(Accesibilidad.RES_OK);
						if ((resultado.getErrores()>0) || (resultado.getWarnings()>0)) {
							accse.setMensaje(resultado.getMensajes().toString());
							retorno +="\n\n[XHTML (Idioma:" + idi + ")] Hi ha errors en el format del codi HTML."; // + resultado.getMensajes().toString();
						}						
						
						if ((accse.getTawmensaje()!=null) && (accse.getTawmensaje().length()>2000)) accse.setTawmensaje(accse.getTawmensaje().substring(0, 1995)+"...");
						if ((accse.getMensaje()!=null) && (accse.getMensaje().length()>2000)) accse.setMensaje(accse.getMensaje().substring(0, 1995)+"...");
						_accDel.grabarAccesibilidad(accse);
					} else {
						if (accse.getId()!=null) _accDel.borrarAccesibilidad(accse.getId());
					}
					

				} else {
					if (accse.getId()!=null) _accDel.borrarAccesibilidad(accse.getId());
				}
			}
		} catch (DelegateException e) {
			log.warn("No se ha podido analizar la accesibilidad del contenido: " + conte.getId() + " Error: " + e.getMessage());
		}
		
		if (retorno.length()>0) return retorno;
		else return null;
	}
	
	
	/**
	 * Testea la accesibilidad de una <em>agenda</em>.<br/>
	 * Retorna un string con los errores o warnings detectados.<br/>
	 * Además guarda los registros correspondientes.<br/>
	 * En caso de no haber ningún error ni warning, retorna <em>null</em>.<br/>
	 * @param agenda Bean Agenda
	 * @return String con el resultado serializado.
	 */
	public String testeoW3C(Agenda agenda) {
		String retorno = "";
		Accesibilidad accse = new Accesibilidad();
		try {
			TidyResultBean resultado;
			TawResultBean resultadotaw;
			
			for (int i = 0; i < lista.size(); i++) {
				String idi = (String)lista.get(i);
				if (idi.equals("null")) break;
				accse = _accDel.obtenerAccesibilidad(Catalogo.SRVC_MICRO_EVENTOS, agenda.getId(), idi);
				if (accse==null) accse = new Accesibilidad();
				TraduccionAgenda traage = (TraduccionAgenda)agenda.getTraduccion(idi);
				if ((traage!=null) && (traage.getDescripcion()!=null) && (traage.getDescripcion().length()>0)) {
					//Analizamos con el Tidy
					resultado = Testeador.testeoPegoteHTML(traage.getDescripcion());
					
					//Analizamos con el Taw. Se monta una url preparada para sera analizada por el taw.
					//ejemplo: sacmicrofront/taw.do?ttr=CNTSP&idioma=ca&id=1658&idsite=109
					resultadotaw = Testeador.testeoTaw( _protocolo + _servidor + ":" + _puerto + "/sacmicrofront/taw.do?ttr=GND00&idioma=" + idi + "&id=" + agenda.getId() + "&idsite=" + idmicrosite);


					if ((resultado.getErrores()>0) || (resultado.getWarnings()>0) || (resultadotaw.getErrores()>0) || (resultadotaw.getWarnings()>0)) {
						accse.setIdioma(idi);
						accse.setServicio(Catalogo.SRVC_MICRO_EVENTOS);
						accse.setIditem(agenda.getId());
						accse.setCodmicro(idmicrosite);
						accse.setMedida(Accesibilidad.MES_SINMEDIA);
						
						if (resultadotaw.getErrores()>0) accse.setTawresultado(Accesibilidad.RES_ERROR);
						else if (resultadotaw.getWarnings()>0) accse.setTawresultado(Accesibilidad.RES_WARN);
						else accse.setTawresultado(Accesibilidad.RES_OK);
						if ((resultadotaw.getErrores()>0) || (resultadotaw.getWarnings()>0)) {
							accse.setTawmensaje(resultadotaw.getMensaje());
							retorno +="\n\n[T.A.W (Idioma:" + idi + ")]\n" + resultadotaw.getMensaje().toString();
						}
						
						if (resultado.getErrores()>0)  accse.setResultado(Accesibilidad.RES_ERROR);
						else if (resultado.getWarnings()>0)  accse.setResultado(Accesibilidad.RES_WARN);
						else accse.setResultado(Accesibilidad.RES_OK);
						if ((resultado.getErrores()>0) || (resultado.getWarnings()>0)) {
							accse.setMensaje(resultado.getMensajes().toString());
							retorno +="\n\n[XHTML (Idioma:" + idi + ")] Hi ha errors en el format del codi HTML."; // + resultado.getMensajes().toString();
						}						
						
						if ((accse.getTawmensaje()!=null) && (accse.getTawmensaje().length()>2000)) accse.setTawmensaje(accse.getTawmensaje().substring(0, 1995)+"...");
						if ((accse.getMensaje()!=null) && (accse.getMensaje().length()>2000)) accse.setMensaje(accse.getMensaje().substring(0, 1995)+"...");

						_accDel.grabarAccesibilidad(accse);
					} else {
						if (accse.getId()!=null) _accDel.borrarAccesibilidad(accse.getId());
					}
				} else {
					if (accse.getId()!=null) _accDel.borrarAccesibilidad(accse.getId());
				}
			}
		} catch (DelegateException e) {
			log.warn("No se ha podido analizar la accesibilidad del evento: " + agenda.getId() + " Error: " + e.getMessage());
		}
		
		if (retorno.length()>0) return retorno;
		else return null;
	}
	
	
	
	/**
	 * Testea la accesibilidad de una <em>noticia</em>.<br/>
	 * Retorna un string con los errores o warnings detectados.<br/>
	 * Además guarda los registros correspondientes.<br/>
	 * En caso de no haber ningún error ni warning, retorna <em>null</em>.<br/>
	 * @param noticia Bean Noticia
	 * @return String con el resultado serializado.
	 */
	public String testeoW3C(Noticia noticia) {
		String retorno = "";
		Accesibilidad accse = new Accesibilidad();
		try {
			TidyResultBean resultado;
			TawResultBean resultadotaw;
			
			for (int i = 0; i < lista.size(); i++) {
				String idi = (String)lista.get(i);
				if (idi.equals("null")) break;
				accse = _accDel.obtenerAccesibilidad(Catalogo.SRVC_MICRO_ELEMENTOS, noticia.getId(), idi);
				if (accse==null) accse = new Accesibilidad();
				TraduccionNoticia tranot = (TraduccionNoticia)noticia.getTraduccion(idi);
				if ((tranot!=null) && (tranot.getTexto()!=null) && (tranot.getTexto().length()>0)) {
				
					//Analizamos con el Tidy
					resultado = Testeador.testeoPegoteHTML(tranot.getTexto());
					
					//Analizamos con el Taw. Se monta una url preparada para sera analizada por el taw.
					//ejemplo: sacmicrofront/taw.do?ttr=CNTSP&idioma=ca&id=1658&idsite=109
					resultadotaw = Testeador.testeoTaw( _protocolo + _servidor + ":" + _puerto + "/sacmicrofront/taw.do?ttr=NTCS0&idioma=" + idi + "&id=" + noticia.getId() + "&idsite=" + idmicrosite);


					if ((resultado.getErrores()>0) || (resultado.getWarnings()>0) || (resultadotaw.getErrores()>0) || (resultadotaw.getWarnings()>0)) {
						accse.setIdioma(idi);
						accse.setServicio(Catalogo.SRVC_MICRO_ELEMENTOS);
						accse.setIditem(noticia.getId());
						accse.setCodmicro(idmicrosite);
						accse.setMedida(Accesibilidad.MES_SINMEDIA);
						
						if (resultadotaw.getErrores()>0) accse.setTawresultado(Accesibilidad.RES_ERROR);
						else if (resultadotaw.getWarnings()>0) accse.setTawresultado(Accesibilidad.RES_WARN);
						else accse.setTawresultado(Accesibilidad.RES_OK);
						if ((resultadotaw.getErrores()>0) || (resultadotaw.getWarnings()>0)) {
							accse.setTawmensaje(resultadotaw.getMensaje());
							retorno +="\n\n[T.A.W (Idioma:" + idi + ")]\n" + resultadotaw.getMensaje().toString();
						}
						
						if (resultado.getErrores()>0)  accse.setResultado(Accesibilidad.RES_ERROR);
						else if (resultado.getWarnings()>0)  accse.setResultado(Accesibilidad.RES_WARN);
						else accse.setResultado(Accesibilidad.RES_OK);
						if ((resultado.getErrores()>0) || (resultado.getWarnings()>0)) {
							accse.setMensaje(resultado.getMensajes().toString());
							retorno +="\n\n[XHTML (Idioma:" + idi + ")] Hi ha errors en el format del codi HTML."; // + resultado.getMensajes().toString();
						}						

						
						if ((accse.getTawmensaje()!=null) && (accse.getTawmensaje().length()>2000)) accse.setTawmensaje(accse.getTawmensaje().substring(0, 1995)+"...");
						if ((accse.getMensaje()!=null) && (accse.getMensaje().length()>2000)) accse.setMensaje(accse.getMensaje().substring(0, 1995)+"...");
						_accDel.grabarAccesibilidad(accse);
					} else {
						if (accse.getId()!=null) _accDel.borrarAccesibilidad(accse.getId());
					}
				} else {
					if (accse.getId()!=null) _accDel.borrarAccesibilidad(accse.getId());
				}
			}
		} catch (DelegateException e) {
			log.warn("No se ha podido analizar la accesibilidad de la noticia: " + noticia.getId() + " Error: " + e.getMessage());
		}
		
		if (retorno.length()>0) return retorno;
		else return null;
	}
	
}
