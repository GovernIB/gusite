package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.gusite.microback.utils.Cadenas;
import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.microForm;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.microback.utils.General;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;

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
public class microEditaAction extends BaseAction  {
	
    protected static Log log = LogFactory.getLog(microEditaAction.class);
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TiposervicioDelegate tipoServicioDelegate = DelegateUtil.getTiposervicioDelegate();
		MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
        TemaFrontDelegate temaFrontDelegate = DelegateUtil.getTemaFrontDelegate();
		
		String[] roles = new String[]{"gussystem", "gusadmin", "gusoper", "gussuper"};
		Microsite micrositeBean = new Microsite();
		microForm microForm = (microForm) form;

    	/** Alta nuevo Microsite **/	
    	if ( "alta".equals(request.getParameter("accion")) ) {

        	Hashtable<?, ?> rolenames = (Hashtable<?, ?>) request.getSession().getAttribute("rolenames");
    		
    		if ( (rolenames.contains(roles[0])) || (rolenames.contains(roles[1])) )  {    		
    			//log.info("Usuario: '" + usu.getUsername() + "' con permisos para alta de microsite.");
    		} else {
		       	 addMessageError(request, "peticion.error");
		         return mapping.findForward("info");
    		}

            // Lista Check de tipos de servicio que aparecerán
           	micrositeBean.setTiposServicios(tipoServicioDelegate.listarTipos());

           	//Valores por defecto
           	micrositeBean.setTipomenu("1");
           	micrositeBean.setVersio("v5");
           	micrositeBean.setAcceso("P");
           		
            request.getSession().setAttribute("MVS_microsite", micrositeBean);
           	request.getSession().removeAttribute("microForm");
            List<TemaFront> temasFront = temaFrontDelegate.listarTemaFront();
            request.getSession().setAttribute("MVS_temas", temasFront);

    		return mapping.findForward("general");
    		
    	}
    	
    	/** Detalle Configuración General **/	
    	else if ( "general".equals(request.getParameter("accion")) ) {

    		if (request.getParameter("refresco") != null) {
  			
    			micrositeBean = (Microsite) request.getSession().getAttribute("MVS_microsite");
    			setBeantoForm(micrositeBean, microForm);
    			Base.micrositeRefreshByBean(micrositeBean, request);
    			request.setAttribute("microGeneral", "si");
    			
    			return mapping.findForward("inicio");
    			
    		} else if (request.getParameter("idsite") != null) {

    			Long idmicrosite = new Long(request.getParameter("idsite"));
            	micrositeBean = micrositeDelegate.obtenerMicrosite(idmicrosite);
                List<TemaFront> temasFront = temaFrontDelegate.listarTemaFront();

                if (!(Base.hasMicrositePermiso(request, idmicrosite))) {
                	
                	request.getSession().removeAttribute("MVS_microsite");
                	request.getSession().removeAttribute("MVS_menuespecifico");
                	request.getSession().removeAttribute("MVS_menugenerico");
                	request.getSession().removeAttribute("MVS_menusinmenu");
                	addMessageError(request, "peticion.error");
                
                	return mapping.findForward("info");
                
                }
                
            	//Guardem les dades del Microsite en el Formulari
                setBeantoForm(micrositeBean, microForm);

                request.getSession().setAttribute("MVS_temas", temasFront);
               	request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
               	               	
               	Base.micrositeRefreshByBean(micrositeBean, request);
            	
                return mapping.findForward("general");
                
    		}
    		
    	} 	
    	
    	/** Guardar **/	
		else if ( request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar")) ) {

			boolean isRolAdmin = "yes".equals(request.getSession().getAttribute("MVS_rol_sys_adm"));
      		micrositeBean = setFormtoBean(microForm, isRolAdmin);

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
		else if ( request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir")) ) { 
 			
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

		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getServletContext().getAttribute("traductor");
		String idiomaOrigen = "ca";

		//La traducción no depende de que esté guardados los datos sino del formulario
		boolean isRolAdmin = "yes".equals(request.getSession().getAttribute("MVS_rol_sys_adm"));
		Microsite micrositeBean = setFormtoBean(microForm, isRolAdmin);

        TraduccionMicrosite microOrigen = (TraduccionMicrosite) microForm.get("traducciones", 0);

        Iterator<?> itTradFichas = ((ArrayList<?>) microForm.get("traducciones")).iterator();                
        Iterator<String> itLang = traductor.getListLang().iterator(); 
        List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
        
        while (itLang.hasNext()) {
            
        	TraduccionMicrosite microDesti = (TraduccionMicrosite) itTradFichas.next();
        	String idiomaDesti = itLang.next();
        	
        	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
        	if (idiomasMicro.contains(idiomaDesti)) {

        		if (!idiomaOrigen.equals(idiomaDesti)) {
        			
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
        
        if ("traduccioCorrecte".equals(request.getAttribute("mensajes")))
        	micrositeBean.setMensajeInfo(getResources(request).getMessage("mensa.traduccion.confirmacion"));
    	else 
    		micrositeBean.setMensajeError(getResources(request).getMessage("mensa.traduccion.error"));
       
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
    private Microsite setFormtoBean(microForm microForm, boolean isRolAdmin) throws Exception  {
    	
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	Microsite micrositeBean = null;

    	if (microForm.get("id") == null) {
			micrositeBean = new Microsite();
		} else {
			micrositeBean = micrositeDelegate.obtenerMicrosite((Long) microForm.get("id"));
		}

    	//micrositeBean.setTitulo((String)microForm.get("titulo"));
    	micrositeBean.setVisible((String) microForm.get("visible"));
    	micrositeBean.setTipomenu((String) microForm.get("tipomenu"));
    	micrositeBean.setUrlcampanya((String) microForm.get("urlcampanya"));
    	micrositeBean.setNumeronoticias(((Integer) microForm.get("numeronoticias")).intValue());

        if (isRolAdmin) {
    		micrositeBean.setUnidadAdministrativa(Integer.parseInt(microForm.get("idUA").toString()));
            micrositeBean.setAnalytics((String) microForm.get("analytics"));
    		micrositeBean.setUri((String) microForm.get("uri"));
    		micrositeBean.setVersio((String) microForm.get("versio"));
    		micrositeBean.setAcceso((String) microForm.get("acceso"));
        	if (((String) microForm.get("domini")).equals("")) {
    			micrositeBean.setDomini(null);
    		} else {
    			micrositeBean.setDomini((String) microForm.get("domini"));
    		}
            micrositeBean.setRestringido(getFlagRestringido(micrositeBean.getVersio(), micrositeBean.getAcceso()));
            micrositeBean.setRol(getRolFinal(micrositeBean.getAcceso(), (String) microForm.get("rol")));
            String desarrollo = ((String) microForm.get("desarrollo")).equals("") ? "N" : "S";
            micrositeBean.setDesarrollo(desarrollo);
        }
    	
    	
    	micrositeBean.setBuscador((String) microForm.get("buscador"));
    	micrositeBean.setMenucorporativo((String) microForm.get("menucorporativo"));
    	micrositeBean.setEstiloCSSPatron((String) microForm.get("estiloCSSPatron"));
    	micrositeBean.setClaveunica((String) microForm.get("claveunica"));
   	
    	micrositeBean.setIdiomas((String[]) microForm.get("idiomas"));
    	micrositeBean.setFuncionalidadTraduccion();
    	micrositeBean.setServiciosOfrecidos((String[]) microForm.get("servofr"));
        Long idTema = (Long) microForm.get("tema");
        if (idTema != null) {
            micrositeBean.setTema(DelegateUtil.getTemaFrontDelegate().obtenerTemaFront(idTema));
        }

        FormFile imagen1 = (FormFile) microForm.get("imagenPrincipal");
        
        if (archivoValido(imagen1)) {
			micrositeBean.setImagenPrincipal(populateArchivo(micrositeBean.getImagenPrincipal(), imagen1, micrositeBean.getId(), null));
		} else if (((Boolean) microForm.get("imagenPrincipalbor")).booleanValue()) {
			micrositeBean.setImagenPrincipal(null);
		}
        
        if (micrositeBean.getImagenPrincipal() != null) {
			if (((String) microForm.get("imagenPrincipalnom")).length() > 0) {
				micrositeBean.getImagenPrincipal().setNombre((String) microForm.get("imagenPrincipalnom"));
			}
		}

        FormFile imagen2 = (FormFile) microForm.get("imagenCampanya");
        
        if (archivoValido(imagen2)) {
			micrositeBean.setImagenCampanya(populateArchivo(micrositeBean.getImagenCampanya(), imagen2, null, null));
		} else if (((Boolean) microForm.get("imagenCampanyabor")).booleanValue()) {
			micrositeBean.setImagenCampanya(null);
		}
        
        if (micrositeBean.getImagenCampanya() != null) {
			if (((String) microForm.get("imagenCampanyanom")).length() > 0) {
				micrositeBean.getImagenCampanya().setNombre((String) microForm.get("imagenCampanyanom"));
			}
		}

        FormFile imagen3 = (FormFile) microForm.get("estiloCSS");
        
        if (archivoValido(imagen3)) {
			micrositeBean.setEstiloCSS(populateArchivo(micrositeBean.getEstiloCSS(), imagen3, null, null));
		} else if (((Boolean) microForm.get("estiloCSSbor")).booleanValue()) {
			micrositeBean.setEstiloCSS(null);
		}
        
        if (micrositeBean.getEstiloCSS() != null) {
			if (((String) microForm.get("estiloCSSnom")).length() > 0) {
				micrositeBean.getEstiloCSS().setNombre((String) microForm.get("estiloCSSnom"));
			}
		}

        // Volcamos los datos traducibles.
    	if (microForm.get("id") == null) {
    		 VOUtils.populate(micrositeBean, microForm);
    	} else {
    		List<TraduccionMicrosite> llista = (List<TraduccionMicrosite>) microForm.get("traducciones");
    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
    		
    		for (int i = 0; i < llista.size(); i++) {
    			if (micrositeBean.getTraducciones().containsKey(((Idioma) langs.get(i)).getLang())) {
    				
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop1(llista.get(i).getTxtop1());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop1(llista.get(i).getUrlop1());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop2(llista.get(i).getTxtop2());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop2(llista.get(i).getUrlop2());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop3(llista.get(i).getTxtop3());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop3(llista.get(i).getUrlop3());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop4(llista.get(i).getTxtop4());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop4(llista.get(i).getUrlop4());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop5(llista.get(i).getTxtop5());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop5(llista.get(i).getUrlop5());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop6(llista.get(i).getTxtop6());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop6(llista.get(i).getUrlop6());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTxtop7(llista.get(i).getTxtop7());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUrlop7(llista.get(i).getUrlop7());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTitulocampanya(llista.get(i).getTitulocampanya());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setSubtitulocampanya(llista.get(i).getSubtitulocampanya());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setCabecerapersonal(llista.get(i).getCabecerapersonal());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setPiepersonal(llista.get(i).getPiepersonal());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setDescription(llista.get(i).getDescription());
    				micrositeBean.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setKeywords(llista.get(i).getKeywords());

    			} else {
    				TraduccionMicrosite traduccio = new TraduccionMicrosite();
					traduccio.setId(new TraduccionMicrositePK());
    				traduccio.getId().setCodigoMicrosite(micrositeBean.getId());
    				traduccio.getId().setCodigoIdioma(((Idioma) langs.get(i)).getLang());
    				
    				traduccio.setTitulo(llista.get(i).getTitulo());
    				traduccio.setTxtop1(llista.get(i).getTxtop1());
    				traduccio.setUrlop1(llista.get(i).getUrlop1());
    				traduccio.setTxtop2(llista.get(i).getTxtop2());
    				traduccio.setUrlop2(llista.get(i).getUrlop2());
    				traduccio.setTxtop3(llista.get(i).getTxtop3());
    				traduccio.setUrlop3(llista.get(i).getUrlop3());
    				traduccio.setTxtop4(llista.get(i).getTxtop4());
    				traduccio.setUrlop4(llista.get(i).getUrlop4());
    				traduccio.setTxtop5(llista.get(i).getTxtop5());
    				traduccio.setUrlop5(llista.get(i).getUrlop5());
    				traduccio.setTxtop6(llista.get(i).getTxtop6());
    				traduccio.setUrlop6(llista.get(i).getUrlop6());
    				traduccio.setTxtop7(llista.get(i).getTxtop7());
    				traduccio.setUrlop7(llista.get(i).getUrlop7());
    				traduccio.setTitulocampanya(llista.get(i).getTitulocampanya());
    				traduccio.setSubtitulocampanya(llista.get(i).getSubtitulocampanya());
    				traduccio.setCabecerapersonal(llista.get(i).getCabecerapersonal());
    				traduccio.setPiepersonal(llista.get(i).getPiepersonal());
    				traduccio.setDescription(llista.get(i).getDescription());
    				traduccio.setKeywords(llista.get(i).getKeywords());

    				micrositeBean.getTraducciones().put(((Idioma) langs.get(i)).getLang(), traduccio);
    			}
    		}
    	}
         // form --> bean
        
        return micrositeBean;
    }

    private String getRolFinal(String acceso, String rol) {
		if (acceso.equals("M") && !rol.equals("")) {
			return rol;
		}
		return null;
	}

	private String getFlagRestringido(String versio, String tipoAcceso) {

        if (versio.equals("v5")) {
            return "5";
        } else if (versio.equals("v4")) {
            return "2";
        } else if (versio.equals("v1")) {
            return "N";
        } else {
            return "S";
        }
    }

    /**
     * Método que vuelca los datos del Bean de Microsite al formulario del usuario
     * @author Indra
     * @param micrositeBean		Bean de tipo Microsite
     * @param microForm			formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void setBeantoForm(Microsite micrositeBean, microForm microForm) throws Exception  {
    	
		OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
		UnidadData ua = op.getUnidadData(micrositeBean.getUnidadAdministrativa(), "ca");
        microForm.set("nombreUA", ua!= null? ua.getNombre() : "");
        microForm.set("idUA", new Integer(micrositeBean.getUnidadAdministrativa()));
       	
        // Obtenemos la lista de UAs.
        //microForm.set("listaUAs", General.obtenerListaUAs());
        
     	AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
     	int nivelAcces = acces.existeAccesibilidadMicrosite(micrositeBean.getId());	            	
     	microForm.set("nivelAccesibilidad", nivelAcces);

        microForm.set("id", micrositeBean.getId());
    	//f.set("titulo", micrositeBean.getTitulo());
        microForm.set("visible", micrositeBean.getVisible());
        microForm.set("restringido", micrositeBean.getRestringido());
        microForm.set("rol", micrositeBean.getRol());
        microForm.set("domini", micrositeBean.getDomini());
        microForm.set("buscador", micrositeBean.getBuscador());
        microForm.set("tipomenu", micrositeBean.getTipomenu());
        microForm.set("urlcampanya", micrositeBean.getUrlcampanya());
        microForm.set("numeronoticias", new Integer(micrositeBean.getNumeronoticias()));
        microForm.set("menucorporativo", micrositeBean.getMenucorporativo());
        microForm.set("estiloCSSPatron", micrositeBean.getEstiloCSSPatron());
        microForm.set("claveunica", micrositeBean.getClaveunica());
        microForm.set("analytics", micrositeBean.getAnalytics());
        microForm.set("uri", micrositeBean.getUri());
        microForm.set("versio", micrositeBean.getVersio());
		microForm.set("acceso", micrositeBean.getAcceso());
        microForm.set("desarrollo", micrositeBean.getDesarrollo());
        if (micrositeBean.getTema() != null) {
            microForm.set("tema", micrositeBean.getTema().getId());
        }

    	microForm.set("idiomas", micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
    	micrositeBean.setFuncionalidadTraduccion();
    	microForm.set("servofr", micrositeBean.getServiciosOfrecidos(micrositeBean.getServiciosOfrecidos()));

        VOUtils.describe(microForm, micrositeBean);  // bean --> form
        
        if (micrositeBean.getImagenPrincipal() != null) {
        	microForm.set("imagenPrincipalnom", micrositeBean.getImagenPrincipal().getNombre());
        	microForm.set("imagenPrincipalid", micrositeBean.getImagenPrincipal().getId());
        }
        
        if (micrositeBean.getImagenCampanya() != null) {
        	microForm.set("imagenCampanyanom", micrositeBean.getImagenCampanya().getNombre());
        	microForm.set("imagenCampanyaid", micrositeBean.getImagenCampanya().getId());
        }
        
        if (micrositeBean.getEstiloCSS() != null) {
        	microForm.set("estiloCSSnom", micrositeBean.getEstiloCSS().getNombre());
        	microForm.set("estiloCSSid", micrositeBean.getEstiloCSS().getId());
        }
    }
    
	//MCR v1.1
    /*
     * El tag de hoja de estilos por defecto es una copia del bueno, pero quitandole la imagen de fondo al tag 'body'.
     * El objetivo de esto es que se visualice correctamente en el tinymce mientras estamos editando. 
     */
    
	public String tagCSS(Archivo archivocss) {
		String retorno = "";
		if (archivocss != null)
			retorno = "archivo.do?id=" + archivocss.getId().longValue();
		else
			retorno = "/sacmicrofront/css/dummy_estilos01_blau.css";
		return retorno;
	}

}
