package org.ibit.rol.sac.microback.action.edita;

import java.util.*; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.microForm;
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.microback.utils.VOUtils;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micropersistence.delegate.AccesibilidadDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TiposervicioDelegate;
import org.ibit.rol.sac.model.TraduccionUA;
import org.ibit.rol.sac.model.UnidadAdministrativa;
import org.ibit.rol.sac.persistence.delegate.UnidadAdministrativaDelegate;


import es.indra.gusite.microintegracion.traductor.TraductorMicrosites;

/**
 * Action que edita las propiedades de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/microEdita" <BR> 
 *  name="microForm" <BR> 
 *  input="/microAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="general" path="/microGeneral.jsp"<BR>
 *	forward name="cabpie" path="/microCabpie.jsp"<BR>
 *	forward name="home" path="/detallePaginaInicio.jsp"<BR>
 *	forward name="info" path="/info.jsp"<BR>
 *	forward name="inicio" path="/index.jsp"<BR>
 *	forward name="refresco" path="/pleaseWaitGeneral.jsp"
 *  
 *  @author Indra
 */
public class microEditaAction extends BaseAction 
{
    protected static Log log = LogFactory.getLog(microEditaAction.class);
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TiposervicioDelegate tipoServicioDelegate = DelegateUtil.getTiposervicioDelegate();
		MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
		
		String[] roles = new String[]{"sacsystem", "sacadmin", "sacoper", "sacsuper"};
		Microsite micrositeBean = new Microsite();
		microForm microForm = (microForm) form;
    	

    	/** Alta nuevo Microsite **/	
    	if ( (""+request.getParameter("accion")).equals("alta")) {

        	Hashtable<?, ?> rolenames = (Hashtable<?, ?>) request.getSession().getAttribute("rolenames");
    		
    		if ( (rolenames.contains(roles[0])) || (rolenames.contains(roles[1])) )  {    		
    			//log.info("Usuario: '" + usu.getUsername() + "' con permisos para alta de microsite.");
    		} else {
		       	 addMessageError(request, "peticion.error");
		         return mapping.findForward("info");
    		}

            // Lista Check de tipos de servicio que aparecerán
           	micrositeBean.setTiposServicios(tipoServicioDelegate.listarTipos());
            request.getSession().setAttribute("MVS_microsite", micrositeBean);
           	request.getSession().removeAttribute("microForm");

    		return mapping.findForward("general");
    	}
    	
    	/** Detalle Configuración General **/	
    	else if ( (""+request.getParameter("accion")).equals("general")) {

    		if (request.getParameter("refresco") != null) {
  			
    			micrositeBean = (Microsite) request.getSession().getAttribute("MVS_microsite");
    			setBeantoForm(micrositeBean, microForm);
    			Base.micrositeRefreshByBean(micrositeBean, request);
    			request.setAttribute("microGeneral", "si");
    			return mapping.findForward("inicio");
    			
    		} else if (request.getParameter("idsite")!=null) {

    			Long idmicrosite= new Long(""+request.getParameter("idsite"));
            	micrositeBean = micrositeDelegate.obtenerMicrosite(idmicrosite);

                if(!(Base.hasMicrositePermiso(request, idmicrosite)))
                {
                	request.getSession().removeAttribute("MVS_microsite");
                	request.getSession().removeAttribute("MVS_menuespecifico");
                	request.getSession().removeAttribute("MVS_menugenerico");
                	request.getSession().removeAttribute("MVS_menusinmenu");
                	addMessageError(request, "peticion.error");
                    return mapping.findForward("info");
                }
                
            	//Guardem les dades del Microsite en el Formulari
                setBeantoForm(micrositeBean, microForm);

               	request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
               	Base.micrositeRefreshByBean(micrositeBean, request);
            	
                return mapping.findForward("general");
    		}
    		
    		
    	} 	
    	
    	/** Guardar **/	
		else if((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.guardar"))) {

      		micrositeBean = setFormtoBean(microForm);

    		//Guardamos cambios en BBDD
            micrositeDelegate.grabarMicrosite(micrositeBean);

            if (microForm.get("id") == null) {
  			
      			setBeantoForm(micrositeBean, microForm); 
			
      			micrositeBean.setMensajeInfo(getResources(request).getMessage("micro.info.nuevo.microsite", dateAndHour));
      			
      			log.info("Creado nuevo Microsite - Id: " + micrositeBean.getId());

            } else {

      			micrositeBean.setMensajeInfo(getResources(request).getMessage("micro.info.modifica.microsite", dateAndHour));
           
                log.info("Modificado Microsite - Id: " + micrositeBean.getId());

            }    		
    		
            Base.micrositeRefreshByBean(micrositeBean, request);
           	Base.menuRefresh(request);

        	return mapping.findForward("refresco");
    	} 
    	
    	/** Traducir **/	
		else if ((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.traducir"))) { 
 			
    		//Traducimos el formulario y añadimos los atributos de la petición para Configuración General
 			micrositeBean = traducir (request, microForm, "GENERAL");

 			request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo());
            Base.micrositeRefreshByBean(micrositeBean, request);
           	Base.menuRefresh(request);
	
			return mapping.findForward("general");
		} 
    	
    	 addMessageError(request, "peticion.error");
         return mapping.findForward("info");
         
         
    }
	
    
    /**
     * Método que traduce un formulario de Configuración de Microsite
     * @author Indra
     * @param request		petición de usuario
     * @param microForm		formulario dinámico enviado por usuario
     * @param configuracion	indica si la traducción es de los campos de configuracion general
     * @throws Exception
     */
    private Microsite traducir (HttpServletRequest request, microForm microForm, String configuracion) throws Exception  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getAttribute("traductor");
    		String idiomaOrigen = "ca";

    		//La traducción no depende de que esté guardados los datos sino del formulario
    		Microsite micrositeBean = setFormtoBean(microForm);

            TraduccionMicrosite microOrigen = (TraduccionMicrosite) microForm.get("traducciones", 0);

            Iterator<?> itTradFichas = ((ArrayList<?>) microForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            
            while (itLang.hasNext()){
                
            	TraduccionMicrosite microDesti = (TraduccionMicrosite) itTradFichas.next();
            	String idiomaDesti = itLang.next();
            	
            	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
            	if (micrositeBean.getIdiomas().contains(idiomaDesti)) {

            		if (!idiomaOrigen.equals(idiomaDesti)) 
	            	{
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(microOrigen, microDesti, configuracion)) 
	            			request.setAttribute("mensajes", "traduccioCorrecte");
	            		else {
	            			request.setAttribute("mensajes", "traduccioIncorrecte");
	            			break;
	            		}
	            	}
	            }
            }
            
            if (request.getAttribute("mensajes").equals("traduccioCorrecte")) 
			   micrositeBean.setMensajeInfo(getResources(request).getMessage("mensa.traduccion.confirmacion"));
            	else micrositeBean.setMensajeError(getResources(request).getMessage("mensa.traduccion.error"));

           
			log.info("Traducción Configuración Microsite - Id: " + (Long) microForm.get("id"));
			
			return micrositeBean;
			
    } 	
    
    /**
     * Método que vuelca los datos del formulario en el Bean de Microsite
     * @author Indra
     * @param microForm	 formulario dinámico enviado por usuario
     * @return Microsite devuelve bean de Microsite con los datos del formulario
     * @throws Exception
     */
    private Microsite setFormtoBean (microForm microForm) throws Exception  {
    	
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	Microsite micrositeBean = null;

    	if (microForm.get("id") == null) {  
    		micrositeBean = new Microsite(); 
        } else micrositeBean = micrositeDelegate.obtenerMicrosite((Long)microForm.get("id"));

    	//micrositeBean.setTitulo((String)microForm.get("titulo"));
		micrositeBean.setUnidadAdministrativa(Integer.parseInt(microForm.get("idUA").toString()));
    	micrositeBean.setVisible((String)microForm.get("visible"));
    	micrositeBean.setTipomenu((String)microForm.get("tipomenu"));
    	micrositeBean.setUrlcampanya((String)microForm.get("urlcampanya"));
    	micrositeBean.setNumeronoticias(((Integer)microForm.get("numeronoticias")).intValue());
    	micrositeBean.setRestringido((String)microForm.get("restringido"));

    	if (((String)microForm.get("rol")).equals("")) micrositeBean.setRol(null);
    	else micrositeBean.setRol((String)microForm.get("rol"));
    	
    	if (((String)microForm.get("domini")).equals("")) micrositeBean.setDomini(null);
    	else micrositeBean.setDomini((String)microForm.get("domini"));
    	
    	micrositeBean.setBuscador((String)microForm.get("buscador"));
    	micrositeBean.setMenucorporativo((String)microForm.get("menucorporativo"));
    	micrositeBean.setEstiloCSSPatron((String)microForm.get("estiloCSSPatron"));
    	micrositeBean.setClaveunica((String)microForm.get("claveunica"));
    	micrositeBean.setIdiomas((String[]) microForm.get("idiomas"));
    	micrositeBean.setFuncionalidadTraduccion();
    	micrositeBean.setServiciosOfrecidos((String[]) microForm.get("servofr"));
    	
        FormFile imagen1 = (FormFile) microForm.get("imagenPrincipal");
        
        if (archivoValido(imagen1)) 
        	micrositeBean.setImagenPrincipal(populateArchivo(micrositeBean.getImagenPrincipal(), imagen1, null, null));
        else if (((Boolean) microForm.get("imagenPrincipalbor")).booleanValue()) micrositeBean.setImagenPrincipal(null);
        
        if (micrositeBean.getImagenPrincipal() != null) 
            if ((""+microForm.get("imagenPrincipalnom")).length()>0) 
            	micrositeBean.getImagenPrincipal().setNombre(""+microForm.get("imagenPrincipalnom"));

        FormFile imagen2 = (FormFile) microForm.get("imagenCampanya");
        if (archivoValido(imagen2)) 
        	micrositeBean.setImagenCampanya(populateArchivo(micrositeBean.getImagenCampanya(), imagen2, null, null));
        else if (((Boolean) microForm.get("imagenCampanyabor")).booleanValue()) micrositeBean.setImagenCampanya(null);
        
        if (micrositeBean.getImagenCampanya() != null) 
            if ((""+microForm.get("imagenCampanyanom")).length()>0) 
            	micrositeBean.getImagenCampanya().setNombre(""+microForm.get("imagenCampanyanom"));

        FormFile imagen3 = (FormFile) microForm.get("estiloCSS");
        if (archivoValido(imagen3)) 
        	micrositeBean.setEstiloCSS(populateArchivo(micrositeBean.getEstiloCSS(), imagen3, null, null));
        else if (((Boolean) microForm.get("estiloCSSbor")).booleanValue()) micrositeBean.setEstiloCSS(null);
        
        if (micrositeBean.getEstiloCSS() != null) 
            if ((""+microForm.get("estiloCSSnom")).length()>0) 
            	micrositeBean.getEstiloCSS().setNombre(""+microForm.get("estiloCSSnom"));

        //Bolcamos los datos traducibles
        VOUtils.populate(micrositeBean, microForm);  // form --> bean
        
        return micrositeBean;
	
    }
    
    
    
    /**
     * Método que vuelca los datos del Bean de Microsite al formulario del usuario
     * @author Indra
     * @param micrositeBean		Bean de tipo Microsite
     * @param microForm			formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void setBeantoForm (Microsite micrositeBean, microForm microForm) throws Exception  {

    	//obtengo la unidad administrativa
    	UnidadAdministrativaDelegate uaDelegate= org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();
    	UnidadAdministrativa uo = uaDelegate.obtenerUnidadAdministrativa(new Long(micrositeBean.getUnidadAdministrativa()));
   	
        microForm.set("nombreUA",((TraduccionUA)uo.getTraduccion("ca")).getNombre());
        microForm.set("idUA",new Integer(micrositeBean.getUnidadAdministrativa()));
        
     	AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
     	int nivelAcces = acces.existeAccesibilidadMicrosite(micrositeBean.getId());	            	
     	microForm.set("nivelAccesibilidad",nivelAcces);
    	

        microForm.set("id",micrositeBean.getId());
    	//f.set("titulo", micrositeBean.getTitulo());
        microForm.set("visible",micrositeBean.getVisible());
        microForm.set("restringido",micrositeBean.getRestringido());
        microForm.set("rol",micrositeBean.getRol());
        microForm.set("domini",micrositeBean.getDomini());
        microForm.set("buscador",micrositeBean.getBuscador());
        microForm.set("tipomenu", micrositeBean.getTipomenu());
        microForm.set("urlcampanya", micrositeBean.getUrlcampanya());
        microForm.set("numeronoticias", new Integer(micrositeBean.getNumeronoticias()));
        microForm.set("menucorporativo",micrositeBean.getMenucorporativo());
        microForm.set("estiloCSSPatron", micrositeBean.getEstiloCSSPatron());
        microForm.set("claveunica",micrositeBean.getClaveunica());
    	microForm.set("idiomas", micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
    	micrositeBean.setFuncionalidadTraduccion();
    	microForm.set("servofr", micrositeBean.getServiciosOfrecidos(micrositeBean.getServiciosOfrecidos()));

        VOUtils.describe(microForm, micrositeBean);  // bean --> form
        
        if (micrositeBean.getImagenPrincipal() != null) {
        	microForm.set("imagenPrincipalnom",micrositeBean.getImagenPrincipal().getNombre());
        	microForm.set("imagenPrincipalid",micrositeBean.getImagenPrincipal().getId());
        }
        if (micrositeBean.getImagenCampanya() != null) {
        	microForm.set("imagenCampanyanom",micrositeBean.getImagenCampanya().getNombre());
        	microForm.set("imagenCampanyaid",micrositeBean.getImagenCampanya().getId());
        }
        if (micrositeBean.getEstiloCSS() != null) {
        	microForm.set("estiloCSSnom",micrositeBean.getEstiloCSS().getNombre());
        	microForm.set("estiloCSSid",micrositeBean.getEstiloCSS().getId());
        }
	
    } 
    

	//MCR v1.1
    /*
     * El tag de hoja de estilos por defecto es una copia del bueno, pero quitandole la imagen de fondo al tag 'body'.
     * El objetivo de esto es que se visualice correctamente en el tinymce mientras estamos editando. 
     */
    
	public String tagCSS(Archivo archivocss) {
		String retorno="";
		if (archivocss!=null)
			retorno="archivo.do?id=" + archivocss.getId().longValue();
		else
			retorno="/sacmicrofront/css/dummy_estilos01_blau.css";
		return retorno;
	}	
	
}
