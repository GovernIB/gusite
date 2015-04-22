package es.caib.gusite.microback.action.edita;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.gusite.microback.utils.Cadenas;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.contenidoForm;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.microback.process.ProcesoW3C;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorException;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micromodel.Accesibilidad;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionContenidoPK;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;

/**
 * Action que edita los contenidos de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/contenidoEdita" <BR> 
 *  name="contenidoForm" <BR> 
 *  input="/contenidosAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleContenido.jsp" <BR>
 *  forward name="info" path="/infoContenido.jsp"
 *  
 *  @author Indra
 */
public class contenidosEditaAction extends BaseAction 
{
	
	protected static Log log = LogFactory.getLog(contenidosEditaAction.class);


	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
    	
    	contenidoForm contenidoForm = (contenidoForm) form;
    	Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	Contenido contenido = null;
    	
    	//Metemos en el request el CSS que utilizará el tinymce
    	request.setAttribute("MVS_css_tiny",tagCSS(micrositeBean.getEstiloCSS(),micrositeBean.getEstiloCSSPatron()));
    	
 		try {
			if 	((String) request.getParameter("accion") != null) {

				//Volcamos los datos del formulario al Bean de contenido
				contenido = setFormtoBean(request, contenidoForm, micrositeBean);

				if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir"))) {
					//Traducimos el Contenido
					traducir (request, contenidoForm);
				}

				else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar"))) {

					//Guardamos y reordenamos el árbol de menús
					List<String> eliminar = new ArrayList<String>();
					for (String lang : contenido.getTraducciones().keySet()) {
						TraduccionContenido trad = contenido.getTraducciones().get(lang);
						if (trad.getTitulo().equals("") && trad.getUri().equals("")) {
							//TODO: ¿qué se intentaba hacer aquí? no funciona si se rellena sólo el texto 
							eliminar.add(lang);
						} else if (trad.getUri().equals("")) {
							trad.setUri(Cadenas.string2uri(trad.getTitulo()));
						}
						if (trad.getId() == null) {
							TraduccionContenidoPK tradId = new TraduccionContenidoPK();
							tradId.setCodigoContenido(contenido.getId());
							tradId.setCodigoIdioma(lang);
							trad.setId(tradId);
						}
					}
					for (String key : eliminar) {
						contenido.getTraducciones().remove(key);
					}

					contenidoDelegate.grabarContenido(contenido);
					micrositeDelegate.grabarUltimoIdcontenido(micrositeBean,contenido.getId());

					//Pasamos el testeo W3C
					if (!pasaTesteoW3C(request, contenido)) {
						return mapping.findForward("info");
					}

					//Añaadimos mensajes de Información
					setMensajesInfo(request, contenidoForm);

					contenidoForm.set("orden", contenido.getOrden());
					contenidoForm.set("id", contenido.getId());

					} else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.borrar"))) {

					//Borramos y reordenamos el árbol de menús
					contenidoDelegate.borrarContenido(contenido.getId());
					menuDelegate.Reordena(contenido.getOrden(), 'b', micrositeBean.getId());

					//request.getSession().removeAttribute("contenidoForm");
					//request.removeAttribute("contenidoForm");
					contenidoForm.resetForm(mapping, request);
					String idmenu = "" + contenido.getMenu().getId();
					request.setAttribute("menu", idmenu );
					request.setAttribute("migapan", contenidoDelegate.migapan(idmenu, null));
					setMensajesInfo(request, contenidoForm);

					return mapping.findForward("detalle");
				}

			} else {
				//Volcamos los datos del Bean de contenido al formulario
				contenido = setBeantoForm(request, contenidoForm, micrositeBean);
	    	}

			request.setAttribute("listaDocs", contenidoDelegate.listarDocumentos(micrositeBean.getId().toString(), "" + contenido.getId()));
			request.getSession().setAttribute("migapan", contenidoDelegate.migapan("" + contenido.getMenu().getId(), contenido.getId()));
			//vrs: anyadido para saber migapan de la url
			request.setAttribute("MVS_HS_URL_migapan", hashMigaPan(contenido));
			request.setAttribute("contenidoForm", contenidoForm);
			//Refresco de parámetro MVS de menú
			Base.menuRefresh(request);

			return mapping.findForward("detalle");

		} catch (Exception e) {
			addMessageError(request, "peticion.error");
 			return mapping.findForward("info");
 		}
    }
 
    /**
     * Método que vuelca los datos del formulario de usuario al Bean de Contenido
     * @author Indra
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private Contenido setFormtoBean (HttpServletRequest request, contenidoForm contenidoForm, Microsite micrositeBean) throws Exception  {	 

    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
    	ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
    	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
    	Contenido contenido = new Contenido();
    	
       //Dar de alta un nuevo contenido
	   if (contenidoForm.get("id") == null) {
	
		   		Menu menuContenido=menuDelegate.obtenerMenu((Long)contenidoForm.get("idMenu")); // menu donde irá el contenido nuevo
	    		
	    		contenido.setOrden(menuContenido.getOrden()+1);
	    		menuDelegate.Reordena(menuContenido.getOrden(), 'a', micrositeBean.getId());
	    		request.getSession().setAttribute("cuenta_"+micrositeBean.getId(), 
	    										   new Integer(Integer.parseInt(""+request.getSession().getAttribute("cuenta_"+micrositeBean.getId()))+1));
				contenido.setMenu(new Menu());
	    		
		   //Al modificar un contenido
		} else if (contenidoForm.get("id") != null) {
				
		    	contenido = contenidoDelegate.obtenerContenido((Long)contenidoForm.get("id"));
		    	
				if (contenidoDelegate.checkSite(contenido.getId(), micrositeBean.getId())) {
					request.setAttribute("mensajes", "si");
					addMessageError(request, "info.seguridad");
					throw new Exception();
				}
				contenido.setOrden(((Integer)contenidoForm.get("orden")).intValue());	
	
		} else {
				request.setAttribute("mensajes", "si");
				addMessageError(request, "info.noposible");
	        	throw new Exception();
	    }
	  	
    	contenido.getMenu().setId((Long)contenidoForm.get("idMenu"));
    	contenido.setFcaducidad(contenidoForm.getFcaducidad());
    	contenido.setFpublicacion(contenidoForm.getFpublicacion());
    	contenido.setVisible(""+contenidoForm.get("visible"));
     	
    	//si es una modificación solo se cambian los textos
    	if (contenidoForm.get("id") == null) {
    		VOUtils.populate(contenido, contenidoForm);  // form --> bean
    	} else {
    		List<TraduccionContenido> llista = (List<TraduccionContenido>) contenidoForm.get("traducciones");
    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
    		
    		for (int i=0; i<llista.size(); i++)
    		{
    			if (contenido.getTraducciones().containsKey(((Idioma)langs.get(i)).getLang()))
    			{
    				contenido.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTexto(llista.get(i).getTexto());
    				contenido.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());
    				contenido.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxbeta(llista.get(i).getTxbeta());
    				contenido.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrl(llista.get(i).getUrl());
    				contenido.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUri(llista.get(i).getUri());
    				
    			} else {
    				TraduccionContenido traduccio = new TraduccionContenido();
//    				traduccio.setContenido(contenido);
//    				traduccio.setIdioma((Idioma)langs.get(i));
    				traduccio.setTexto(llista.get(i).getTexto());
    				traduccio.setTitulo(llista.get(i).getTitulo());
    				traduccio.setTxbeta(llista.get(i).getTxbeta());
    				traduccio.setUrl(llista.get(i).getUrl());
    				traduccio.setUri(llista.get(i).getUri());
    		
    				contenido.getTraducciones().put(((Idioma)langs.get(i)).getLang(), traduccio);
    			}
    		}
    	}

     	return contenido;
    }
    
    /**
     * Método que vuelca los datos del Bean de contenido al formulario de usuario
     * @author Indra
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private Contenido setBeantoForm (HttpServletRequest request, contenidoForm contenidoForm, Microsite micrositeBean) throws Exception  {	 

    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
    	ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
    	Contenido contenido = new Contenido();
    	
    	if (request.getParameter("id") != null) {
   		
        	contenido = contenidoDelegate.obtenerContenido(new Long(request.getParameter("id")));
        	// Comprobamos si el contenido pertenece al site a través de su menu 
        	if (menuDelegate.checkSite(micrositeBean.getId(),contenido.getMenu().getId())) {
        		request.setAttribute("mensajes", "si");
        		addMessageError(request, "info.seguridad");
            	throw new Exception(); 
            }

        	revisaAccesibilidad(request, contenido.getId());
        	contenidoForm.setFcaducidad(contenido.getFcaducidad());
            contenidoForm.setFpublicacion(contenido.getFpublicacion());
            contenidoForm.set("visible",contenido.getVisible());
            contenidoForm.set("orden", new Integer(contenido.getOrden()));
           	contenidoForm.set("idMenu",contenido.getMenu().getId());

            VOUtils.describe(contenidoForm, contenido);  // bean --> form    	
    	
    	} else throw new Exception();
    	return contenido;

    }    
    
    
    /**
     * Método que traduce un formulario de Contenido
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinámico enviado por usuario
     * @throws Exception
     * @author Indra
     * @throws TraductorException 
     */
    private void traducir (HttpServletRequest request, contenidoForm contenidoForm) throws TraductorException  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getAttribute("traductor");
    		String idiomaOrigen = "ca";

            TraduccionContenido contenidoOrigen = (TraduccionContenido) contenidoForm.get("traducciones", 0);
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");

            Iterator<?> itTradFichas = ((ArrayList<?>) contenidoForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
            
            while (itLang.hasNext()){

            	String idiomaDesti = itLang.next();
            	TraduccionContenido contenidoDesti = (TraduccionContenido) itTradFichas.next();
	
			   	if (contenidoDesti == null) {
			   		micrositeBean.setTraduccion(idiomaDesti, new TraduccionContenido());
			   		contenidoDesti = (TraduccionContenido) micrositeBean.getTraduccion(idiomaDesti);
			   	}
            	
            	
            	//Comprobamos que el idioma Destino está configurado en el Microsite si no está no se traduce
            	if (idiomasMicro.contains(idiomaDesti)) {

	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(contenidoOrigen, contenidoDesti)) {
	            			request.setAttribute("mensajes", "traduccioCorrecte");
	            		}
	            		else {
	            			request.setAttribute("mensajes", "traduccioIncorrecte");
	            			break;
	            		}
	            	}
            	}
            }
            
			if (request.getAttribute("mensajes").equals("traduccioCorrecte")) addMessage(request, "mensa.traduccion.confirmacion");
			 else addMessageError(request, "mensa.traduccion.error");
           
			log.info("Traducción Contenido - Id: " + (Long) contenidoForm.get("id"));
    }    
    
    
    /**
     * Método que revisa la accesibilidad para la edición de un Contenido
     * @param request			petición de usuario
     * @param idContenido		id del contenido a revisar
     * @throws Exception
     * @author Indra
     */
    private void revisaAccesibilidad (HttpServletRequest request, Long idContenido) throws Exception  {	 
    	
    	// Si el contenido tiene errores de accesibilidad,cargamos una lista con los mismos para recorrerla en el jsp
    	AccesibilidadDelegate accDel = DelegateUtil.getAccesibilidadDelegate();
    	IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
		Iterator<?> it = ididel.listarLenguajes().iterator();	
		int i=0;
    	while (it.hasNext()) {
    		String idi = (String)it.next();
    		Accesibilidad acce = accDel.obtenerAccesibilidad(Catalogo.SRVC_MICRO_CONTENIDOS, idContenido, idi);
    		if (acce!=null) request.setAttribute("MVS_w3c_"+i,acce);
    		i++;
    	}
    	
    	if (accDel.existeAccesibilidadContenido(idContenido))  request.setAttribute("accesibilidad","SI");
    	
    }    
    
	//MCR v1.1
    /*
     * El tag de hoja de estilos por defecto es una copia del bueno, pero quitandole algunes cosetes.
     * El objetivo de esto es que se visualice correctamente en el tinymce mientras estamos editando. 
     */
    
	public String tagCSS(Archivo archivocss, String idcsspatron) {
		String retorno="";
		if (archivocss!=null)
			retorno="/sacmicrofront/css/tiny_estils.css,archivo.do?id=" + archivocss.getId().longValue();
		else 
		{
			retorno="/sacmicrofront/css/tiny_estils.css";		
		    if (idcsspatron.equals("A"))
		    	  retorno+=",/sacmicrofront/v4/css/estils_blau.css";
		    if (idcsspatron.equals("R"))
		    	  retorno+=",/sacmicrofront/v4/css/estils_roig.css";
		    if (idcsspatron.equals("V"))
		    	  retorno+=",/sacmicrofront/v4/css/estils_verd.css";
		    if (idcsspatron.equals("G"))
		    	  retorno+=",/sacmicrofront/v4/css/estils_groc.css";
		    if (idcsspatron.equals("M"))
		    	  retorno+=",/sacmicrofront/v4/css/estils_morat.css";
		}
	    
		return retorno;
	}	    
	
	public Hashtable<String,String> hashMigaPan(Contenido conte) {
		Hashtable<String,String> hashidioma = new Hashtable<String,String>();
		try {
			IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
			Iterator<?> it = ididel.listarLenguajes().iterator();	
			int i=0;
	    	while (it.hasNext()) {
	    		String idi = (String)it.next();
	    		if ( conte.getTraduccion(idi)!=null )
	    			hashidioma.put(""+i,Base.obtenerMigaContenidoFromURI( ((TraduccionContenido)conte.getTraduccion(idi)).getUrl() ));
	    		i++;
	    	}
		} catch (Exception e) {
			log.warn("[AVISO] [hashMigaPan] " + e.getMessage());
		}
		return hashidioma;
	}
	
    /**
     * Método que establece en formulario los mensajes de información según la operación solicitada
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinámico enviado por usuario
     * @throws Exception
     * @author Indra
     */
    private void setMensajesInfo (HttpServletRequest request, contenidoForm contenidoForm) throws Exception  {	
    	
    	//Tratamos el contenido del mensaje informativo según la operación que se realiza
		//Modificación
		if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar"))
			     & contenidoForm.get("id") != null)
    		
			 addMessageWithDate(request, "mensa.modifconte");
		//Creación
		 else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar"))
			     & contenidoForm.get("id") == null)
			 addMessageWithDate(request, "mensa.nuevoconte");
		 //Eliminación
		 else 	addMessageAlert(request, "mensa.listacontborradas");
    	
    }	
    
    /**
     * Método que testea W3C el formulario de usuario
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinómico enviado por usuario
     * @throws Exception
     * @author Indra
     */
    private boolean pasaTesteoW3C (HttpServletRequest request, Contenido contenido) throws Exception  {	
    	
        String resultadoW3C = new ProcesoW3C(request).testeoW3C(contenido);
    	if (resultadoW3C != null) { request.setAttribute("SVS_otrainfo", "S'han detectat problemes de accesibilitat que ha de solucionar. Editi el contingut i accedeixi a la llista detallada d'errors: " + resultadoW3C);   
									request.setAttribute("W3C", "si");
    								return false;
    							  
    	} else return true;
					  
    }	    
    

}

