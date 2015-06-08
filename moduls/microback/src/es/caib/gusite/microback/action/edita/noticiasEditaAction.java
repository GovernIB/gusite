package es.caib.gusite.microback.action.edita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.gusite.microback.utils.Cadenas;
import es.caib.gusite.micromodel.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.noticiaForm;
import es.caib.gusite.microback.process.ProcesoW3C;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * Action que edita las noticias (elementos de un listado) de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/noticiaEdita" <BR> 
 *  name="noticiaForm" <BR> 
 *  input="/noticiasAcc.do"   <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleNoticia.jsp"
 *  
 *  @author Indra
 */
public class noticiasEditaAction extends BaseAction 
{
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */
	
	protected static Log log = LogFactory.getLog(noticiasEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	try {
    	
    	NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();	
    	TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
    	Noticia noticiaBean = null;
    	noticiaForm noticiaForm = (noticiaForm) form;
    	Microsite micrositeBean= (Microsite)request.getSession().getAttribute("MVS_microsite");

        if(request.getSession().getAttribute("MVS_idtipo")!=null) 
        	request.setAttribute("MVS_tipolistado", tipoDelegate.obtenerTipo( new Long(""+request.getSession().getAttribute("MVS_idtipo")) ) );    	
    	
        if (request.getParameter("accion") != null) {
        
	    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null
	    	|| (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar")))){
	
		        noticiaBean = setFormtoBean(request, noticiaForm, micrositeBean);

                List<String> eliminar = new ArrayList<String>();
                for (String lang : noticiaBean.getTraducciones().keySet()) {
                	TraduccionNoticia trad = noticiaBean.getTraducciones().get(lang);
                    if (trad.getTitulo().equals("") && trad.getUri().equals("")) {
						//TODO: ¿qué se intentaba hacer aquí? no funciona si se rellena sólo el texto 
                    	/* NPE!! cuando trad.getId es null
                        eliminar.add(trad.getId().getCodigoIdioma());
                        */
                        eliminar.add(lang);
                    } else if (trad.getUri().equals("")) {
                        trad.setUri(Cadenas.string2uri(trad.getTitulo()));
                    }
					if (trad.getId() == null) {
						TraduccionNoticiaPK tradId = new TraduccionNoticiaPK();
						tradId.setCodigoNoticia(noticiaBean.getId());
						tradId.setCodigoIdioma(lang);
						trad.setId(tradId);
					}
                }
                for (String key : eliminar) {
                    noticiaBean.getTraducciones().remove(key);
                }

		       	noticiaDelegate.grabarNoticia(noticiaBean);
		
		       	String resultadoW3C = new ProcesoW3C(request).testeoW3C(noticiaBean);
		       	if (resultadoW3C!=null) { request.setAttribute("SVS_otrainfo", "S'han detectat problemes de accesibilitat: " + resultadoW3C);
		       							  return mapping.findForward("info");
		       	}
		       	
		       	if (noticiaForm.get("id") == null){
		       		noticiaForm.set("id",noticiaBean.getId());
		       		addMessageWithDate(request, "mensa.nuevanoticia");
		       	} else addMessageWithDate(request, "mensa.modifnoticia"); 
		       	
		       	//Los cambios aplicados en el Bean los cargamos en el formulario
		       	setBeantoForm(request,noticiaForm, (Long)noticiaForm.get("id"), micrositeBean);
		       	
		       	request.getSession().removeAttribute("MVS_noticia");
	       	
	        } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir"))) {
	        	noticiaBean = setFormtoBean(request, noticiaForm, micrositeBean);
	        	traducir(request, noticiaForm);
	        	request.getSession().setAttribute("MVS_noticia", noticiaBean);
	        	/* esto hacía que se recargue lo ya traducido!
	        	//setBeantoForm(request,noticiaForm, (Long)noticiaForm.get("id"), micrositeBean);
	        	 */
		    }else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.crear"))) {
		    	  noticiaForm.resetForm(mapping, request);
		    	  noticiaForm.set("idTipo", new Long(""+request.getSession().getAttribute("MVS_idtipo")));
		    	  noticiaForm.set("fpublicacion", new SimpleDateFormat("dd/MM/yyyy 00:00").format(new java.util.Date()));
		    	  request.setAttribute("noticiaForm",noticiaForm);
		    	  request.getSession().removeAttribute("MVS_noticia");
	        } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.borrar"))) {
	        	Long id = (Long)noticiaForm.get("id");
	        	NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
	            bdNoticia.borrarNoticia(id);
	            noticiaForm.resetForm(mapping, request);
	            addMessageAlert(request, "noticia.borrar.sinid");
	        }        
       } else setBeantoForm(request,noticiaForm, new Long(""+request.getParameter("id")), micrositeBean);
        
       request.setAttribute("tiposCombo", tipoDelegate.listarCombo(micrositeBean.getId())); 
       return mapping.findForward("detalle");

    	} catch (Exception e) {
            addMessageError(request, "peticion.error");
            return mapping.findForward("info");    		
    	}
    }

    /**
     * Método que vuelca los datos del formulario en el Bean de Noticia
	 * @param request		petición del usuario
	 * @param noticiaForm	formulario dinámico enviado por el usuario
	 * @param micrositeBean bean de Microsite
	 * @return Noticia
	 * @throws Exception
	 */
	private Noticia setFormtoBean (HttpServletRequest request, noticiaForm noticiaForm, Microsite micrositeBean) throws Exception  {

		NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
		IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
		TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
		Noticia noticiaBean = null;
    	Tipo tipo = null;
    	Noticia noticiaBeanSession = (Noticia) request.getSession().getAttribute("MVS_noticia");

        if (noticiaBeanSession != null) {
    		noticiaBean = noticiaBeanSession;
    		tipo = noticiaBean.getTipo();
    	} else if (noticiaForm.get("id") == null) {  
    		noticiaBean = new Noticia(); 
    		tipo = tipoDelegate.obtenerTipo((Long) noticiaForm.get("idTipo"));
        } else {
        	noticiaBean = noticiaDelegate.obtenerNoticia((Long) noticiaForm.get("id"));
	    	if (noticiaBean.getIdmicrosite().longValue() != micrositeBean.getId().longValue()) {
                throw new Exception();
            }
	    	tipo = noticiaBean.getTipo();
        }
    	tipo.setId((Long) noticiaForm.get("idTipo"));
    	noticiaBean.setTipo(tipo);
    	
    	noticiaBean.setIdmicrosite(micrositeBean.getId());
       	noticiaBean.setFcaducidad(noticiaForm.getFcaducidad());
       	noticiaBean.setFpublicacion(noticiaForm.getFpublicacion());
       	noticiaBean.setVisible("" + noticiaForm.get("visible"));
       	noticiaBean.setVisibleweb("" + noticiaForm.get("visibleweb"));
       	if (noticiaForm.get("orden") != null) {
            noticiaBean.setOrden(new Integer("" + noticiaForm.get("orden")));
        } else {
            noticiaBean.setOrden(null);
        }

        // Tratamos la imagen de la noticia
       	FormFile imagen = (FormFile) noticiaForm.get("imagen");
        if (archivoValido(imagen)) {
            noticiaBean.setImagen(populateArchivo(noticiaBean.getImagen(), imagen, noticiaBean.getIdmicrosite(), null));
        } else if (((Boolean) noticiaForm.get("imagenbor")).booleanValue()) {
            noticiaBean.setImagen(null);
        }

        if (noticiaBean.getImagen() != null) {
            if (("" + noticiaForm.get("imagennom")).length() > 0) {
                noticiaBean.getImagen().setNombre("" + noticiaForm.get("imagennom"));
            }
        }

        // Recuperamos los campos traducibles de la noticia
        List<TraduccionNoticia> traducciones = (List<TraduccionNoticia>) noticiaForm.get("traducciones");
        List<?> lenguajes = idiomaDelegate.listarLenguajes();

        // Tratamos los documentos (Archivos)
        for (int i = 0; i < lenguajes.size(); i++) {
            TraduccionNoticia traduccionNoticiaBean = (TraduccionNoticia) noticiaBean.getTraduccion("" + lenguajes.get(i));
            TraduccionNoticia traduccionNoticiaForm = (TraduccionNoticia) traducciones.get(i);
            if (traduccionNoticiaBean != null) {
                if (traduccionNoticiaBean.getDocu() != null) {
                    traduccionNoticiaBean.getDocu().setIdmicrosite(noticiaBean.getIdmicrosite());
                    traduccionNoticiaForm.setDocu(traduccionNoticiaBean.getDocu());
                }
                traducciones.set(i, traduccionNoticiaForm);
            }
        }

        // Si es una modificación solo se cambian los textos
        if (noticiaForm.get("id") == null) {
            VOUtils.populate(noticiaBean, noticiaForm);  // form --> bean
        } else {
            List<TraduccionNoticia> llista = (List<TraduccionNoticia>) noticiaForm.get("traducciones");
            List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();

            for (int i = 0; i < llista.size(); i++) {
                if (noticiaBean.getTraducciones().containsKey(((Idioma) langs.get(i)).getLang())) {
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setSubtitulo(llista.get(i).getSubtitulo());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setFuente(llista.get(i).getFuente());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setLaurl(llista.get(i).getLaurl());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setUrlnom(llista.get(i).getUrlnom());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setDocu(llista.get(i).getDocu());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setTexto(llista.get(i).getTexto());
                    noticiaBean.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setUri(llista.get(i).getUri());
                } else {
                    TraduccionNoticia traduccio = new TraduccionNoticia();
                    traduccio.setTitulo(llista.get(i).getTitulo());
                    traduccio.setSubtitulo(llista.get(i).getSubtitulo());
                    traduccio.setFuente(llista.get(i).getFuente());
                    traduccio.setLaurl(llista.get(i).getLaurl());
                    traduccio.setUrlnom(llista.get(i).getUrlnom());
                    traduccio.setDocu(llista.get(i).getDocu());
                    traduccio.setTexto(llista.get(i).getTexto());
                    traduccio.setUri(llista.get(i).getUri());

                    noticiaBean.getTraducciones().put(((Idioma) langs.get(i)).getLang(), traduccio);
                }
            }
        }

        // Tratamos documentos (Nombres)
        FormFile[] ficheros = (FormFile[]) noticiaForm.get("ficheros");
        boolean[] ficherosbor = (boolean[]) noticiaForm.get("ficherosbor");

        for (int i = 0; i < ficheros.length; i++) {
            TraduccionNoticia traduccion = (TraduccionNoticia) noticiaBean.getTraduccion("" + lenguajes.get(i));
            if (traduccion != null) {
                if (archivoValido(ficheros[i])) {
                    traduccion.setDocu(populateArchivo(traduccion.getDocu(), ficheros[i], noticiaBean.getIdmicrosite(), null));
                } else if (ficherosbor[i]) {
                    traduccion.setDocu(null);
                }
            }
        }

        return noticiaBean;
    }
    
    /**
     * Método que vuelca los datos del Bean de Noticia al formulario del usuario
     * @param request		petici´´on de usuario
     * @param noticiaForm	formulario dinámico enviado por usuario
     * @param idNoticia		id de la noticia
     * @param micrositeBean	bean de microsite
     * @throws Exception
     */
    private void setBeantoForm (HttpServletRequest request, noticiaForm noticiaForm, Long idNoticia, Microsite micrositeBean) throws Exception  {

    	NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate(); 
    	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
    	AccesibilidadDelegate accesibilidadDelegate = DelegateUtil.getAccesibilidadDelegate();
    	Noticia noticiaBean = null;
    	Noticia noticiaBeanSession = (Noticia)request.getSession().getAttribute("MVS_noticia");
            	
            	// Si el contenido tiene errores de accesibilidad,cargamos una lista con los mismos para recorrerla en el jsp
    			Iterator<?> it = idiomaDelegate.listarLenguajes().iterator();	
    			int x=0;
    	    	while (it.hasNext()) {
    	    		String idi = (String)it.next();
    	    		Accesibilidad acce = accesibilidadDelegate.obtenerAccesibilidad(Catalogo.SRVC_MICRO_ELEMENTOS, idNoticia, idi);
    	    		if (acce!=null) request.setAttribute("MVS_w3c_"+x,acce);
    	    		x++;
    	    	}

                if (noticiaDelegate.checkSite(micrositeBean.getId(),idNoticia)) {
                	addMessageError(request, "info.seguridad");
                	throw new Exception();
                }

                if  (noticiaBeanSession != null) {
	                if (noticiaBeanSession.getId().longValue() == idNoticia.longValue())
	                	noticiaBean = (Noticia)request.getSession().getAttribute("MVS_noticia");
	                else {	
	                	noticiaBean = noticiaDelegate.obtenerNoticia(idNoticia);
	                	request.getSession().setAttribute("MVS_noticia", noticiaBean);
	                }
                } else {	
                	noticiaBean = noticiaDelegate.obtenerNoticia(idNoticia);
                	request.getSession().setAttribute("MVS_noticia", noticiaBean);
                }
                
                if (noticiaBean.getIdmicrosite().longValue()!= micrositeBean.getId().longValue()) throw new Exception();
               
                noticiaForm.setFcaducidad(noticiaBean.getFcaducidad());
                noticiaForm.setFpublicacion(noticiaBean.getFpublicacion());
                noticiaForm.set("visible",noticiaBean.getVisible());
                noticiaForm.set("visibleweb",noticiaBean.getVisibleweb());
                noticiaForm.set("orden", noticiaBean.getOrden());
                noticiaForm.set("idTipo",noticiaBean.getTipo().getId());
                
                VOUtils.describe(noticiaForm, noticiaBean);  // bean --> form
    
                if (noticiaBean.getImagen() != null) {
                	noticiaForm.set("imagennom",noticiaBean.getImagen().getNombre());
                	noticiaForm.set("imagenid",noticiaBean.getImagen().getId());
                }
              
                List<?> lenguajes = idiomaDelegate.listarLenguajes();
                String[] ficherosnom = new String[lenguajes.size()];
                Long[] ficherosid =  new Long[lenguajes.size()];
                for (int i=0;i<lenguajes.size();i++) {
                    TraduccionNoticia  traduccion = (TraduccionNoticia) noticiaBean.getTraduccion(""+lenguajes.get(i));
                    if (traduccion!=null)
                    if (traduccion.getDocu()!=null) {
                    	ficherosnom[i]=traduccion.getDocu().getNombre();
                    	ficherosid[i]=traduccion.getDocu().getId();
                    }
                }
                noticiaForm.set("ficherosnom",ficherosnom);
                noticiaForm.set("ficherosid",ficherosid);

    } 
    
    /**
     * Método que traduce un formulario de Noticia
     * @param request	  petición de usuario
     * @param noticiaForm formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void traducir (HttpServletRequest request, noticiaForm noticiaForm) throws Exception  {	

        //Tratamos documentos (Nombres)
        FormFile[] ficheros =  (FormFile[]) noticiaForm.get("ficheros");
        String[] ficherosnom =  (String[]) noticiaForm.get("ficherosnom");
        boolean[] ficherosbor = (boolean[]) noticiaForm.get("ficherosbor");
    	
    	
    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getServletContext().getAttribute("traductor");
    		String idiomaOrigen = "ca";

            TraduccionNoticia noticiaOrigen = (TraduccionNoticia) noticiaForm.get("traducciones", 0);
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");

            Iterator<?> itTradFichas = ((ArrayList<?>) noticiaForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
            int i = 0;
            
            while (itLang.hasNext()){

            	String idiomaDesti = itLang.next();
            	TraduccionNoticia noticiaDesti = (TraduccionNoticia) itTradFichas.next();
	
			   	if (noticiaDesti == null) {
			   		micrositeBean.setTraduccion(idiomaDesti, new TraduccionNoticia());
			   		noticiaDesti = (TraduccionNoticia) micrositeBean.getTraduccion(idiomaDesti);
			   	}
            	
            	
            	//Comprobamos que el idioma Destino está configurado en el Microsite si no está no se traduce
            	if (idiomasMicro.contains(idiomaDesti)) {

	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(noticiaOrigen, noticiaDesti)) {

	            		   //Tratamos documentos
	            		   if (archivoValido(ficheros[i]))  noticiaDesti.setDocu(populateArchivo(noticiaDesti.getDocu(), ficheros[i], micrositeBean.getId(), null));
	                	   else if (ficherosbor[i])         noticiaDesti.setDocu(null);
	                	   else if (noticiaOrigen.getDocu() != null) noticiaDesti.setDocu(crearNuevoArchivo(noticiaOrigen.getDocu(), micrositeBean.getId(), null));  

	                	   if (noticiaDesti.getDocu() != null) 
	                		   if (ficherosnom[i].length()>0) 
	                			   noticiaDesti.getDocu().setNombre(ficherosnom[i]);
	                		   else  noticiaDesti.getDocu().setNombre(noticiaOrigen.getDocu().getNombre());
	            			
	            			
	            			
	            			request.setAttribute("mensajes", "traduccioCorrecte");
	            		}
	            		else {
	            			request.setAttribute("mensajes", "traduccioIncorrecte");
	            			break;
	            		}
	            	} else i=i+1;
            	} else i=i+1;
            }
            
			if (request.getAttribute("mensajes").equals("traduccioCorrecte")) addMessage(request, "mensa.traduccion.confirmacion");
				else addMessageError(request, "mensa.traduccion.error");
           
			log.info("Traducción noticia - Id: " + (Long) noticiaForm.get("id"));
    }
    
}


