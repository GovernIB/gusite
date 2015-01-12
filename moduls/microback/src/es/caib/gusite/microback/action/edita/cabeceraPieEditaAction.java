package es.caib.gusite.microback.action.edita;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.microForm;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;

/**
 * Action que edita la cabecera y el pie de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/cabeceraPieEdita" <BR> 
 *  name="microForm" <BR> 
 *  input="/microAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *	forward name="general" path="/microGeneral.jsp"<BR>
 *	forward name="cabpie" path="/microCabpie.jsp"<BR>
 *	forward name="home" path="/detallePaginaInicio.jsp"<BR>
 *	forward name="inicio" path="/index.jsp"<BR>
 *	forward name="refresco" path="/pleaseWaitCabPie.jsp"
 *  
 *  @author Indra
 */
public class cabeceraPieEditaAction extends BaseAction 
{
    protected static Log log = LogFactory.getLog(cabeceraPieEditaAction.class);

	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	microForm microForm = (microForm) form;

    	if ((""+request.getParameter("accion")).equals("cabpie")) {
    		
    		if (request.getParameter("refresco") != null) {
      			
    			Microsite micrositeBean = (Microsite) request.getSession().getAttribute("MVS_microsite");
    			setBeantoForm(micrositeBean, microForm);
    			Base.micrositeRefreshByBean(micrositeBean, request);
    			request.setAttribute("microCabPie", "si");
    			return mapping.findForward("inicio");
    			
    		} else if (request.getParameter("idsite")!=null) {
    		
    		//Cargamos los datos de la Cabecera y Pie de Microsite
    		cargar(request, microForm);
 	
        	return mapping.findForward("cabpie");
        	
    		}
  
    	} else if((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.guardar"))) {
	
	    		//Guardamos los cambios en BBDD
	    		Microsite micrositeBean = guardar(request, microForm);
			    
		        //Al cambiar las propiedades generales es necesario un refresco de Microsite
	            //Antes se le pedía al usuario que apretara un link
	           	Base.micrositeRefreshByBean(micrositeBean, request);
	           	Base.menuRefresh(request);
	    		
	    		return mapping.findForward("refresco");
    	
    	} else if ((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.traducir"))) { 
	 			
	    		//Traducimos el formulario y añadimos los atributos de la petición para Configuración General
	 			Microsite micrositeBean = traducir (request, microForm, "CABPIE");

				//Al cambiar las propiedades generales es necesario un refresco de Microsite
	            //Antes se le pedía al usuario que apretara un link
	           	Base.micrositeRefreshByBean(micrositeBean, request);
	           	Base.menuRefresh(request);
	 			
				return mapping.findForward("cabpie");
		} 

    	 addMessageError(request, "peticion.error");
         return mapping.findForward("info");
 
    }
	
    
    /**
     * Método que traduce un formulario de Configuración de Microsite
     * @author Indra
     * @param request	petición de usuario
     * @param dForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private Microsite traducir (HttpServletRequest request, microForm microForm, String configuracion) throws Exception  {	

    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getAttribute("traductor");
    	String idiomaOrigen = "ca";

        TraduccionMicrosite microOrigen = (TraduccionMicrosite) microForm.get("traducciones", 0);
        Microsite micrositeBean =  micrositeDelegate.obtenerMicrosite((Long)microForm.get("id"));

       Iterator<?> itTradFichas = ((ArrayList<?>) microForm.get("traducciones")).iterator();                
       Iterator<String> itLang = traductor.getListLang().iterator(); 
            
            while (itLang.hasNext()){
                
            	TraduccionMicrosite microDesti = (TraduccionMicrosite) itTradFichas.next();
            	String idiomaDesti = itLang.next();
            	
            	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
            	if (micrositeBean.getIdiomas().contains(idiomaDesti)) {
            	
	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(microOrigen, microDesti, configuracion)) {
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
           
			log.info("Traducción Configuración Microsite - Id: " + (Long) microForm.get("id"));
			
    	    //Metemos en el request el CSS que utilizará el tinymce
    	    micrositeBean.setMvsCssTiny(tagCSS(micrositeBean.getEstiloCSS()));
          	
           	request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
           	
           	return micrositeBean;

    } 	
    
    
    /**
     * Método que guarda los cambios de un formulario de Configuración de Microsite
     * @param request	petición de usuario
     * @param microForm		formulario dinámico enviado por usuario
     * @return Microsite
     * @throws Exception
     */
    private Microsite guardar (HttpServletRequest request, microForm microForm) throws Exception  {
    
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	Microsite micrositeBean = setFormtoBean(microForm);

		Date fecha = new Date();
  		String [] fechaHora = {DateFormat.getDateInstance(DateFormat.LONG, new Locale("ca","ES")).format(fecha).replaceAll("/", "de"),
  							   DateFormat.getTimeInstance(DateFormat.LONG, new Locale("ca","ES")).format(fecha).replaceAll("/", "de")};
  		
        		//Guardamos cambios en BBDD
	            micrositeDelegate.grabarMicrosite(micrositeBean);
	             	
		       log.info("Configuración Microsite modificada - Id: " + (Long) microForm.get("id"));

		        micrositeBean.setMvsCssTiny(tagCSS(micrositeBean.getEstiloCSS()));
    
	    	    micrositeBean.setMensajeInfo(
	    	    		getResources(request).getMessage("micro.modifcabpie") + 
	    	    		getResources(request).getMessage("micro.info.ultimamodificacio.microsite", fechaHora));
	           	
	    	    request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
	    	    
	    	   return micrositeBean; 
    }    
    
    /**
     * Método que vuelca los datos del formulario en el Bean de Microsite
     * @param microForm		formulario dinámico enviado por usuario
     * @return Microsite devuelve bean de Microsite con los datos del formulario
     * @throws Exception
     */
    private Microsite setFormtoBean (microForm microForm) throws Exception  {
    	
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	Microsite micrositeBean =  micrositeDelegate.obtenerMicrosite((Long)microForm.get("id"));
    	
    	micrositeBean.setOptMapa((String)microForm.get("optMapa"));
    	micrositeBean.setOptFaq((String)microForm.get("optFaq"));
    	micrositeBean.setOptContacto((String)microForm.get("optContacto"));
    	micrositeBean.setOpt1((String)microForm.get("opt1"));
    	micrositeBean.setOpt2((String)microForm.get("opt2"));
    	micrositeBean.setOpt3((String)microForm.get("opt3"));
    	micrositeBean.setOpt4((String)microForm.get("opt4"));
    	micrositeBean.setOpt5((String)microForm.get("opt5"));
    	micrositeBean.setOpt6((String)microForm.get("opt6"));
    	micrositeBean.setOpt7((String)microForm.get("opt7"));
    	micrositeBean.setTipocabecera((String)microForm.get("tipocabecera"));
    	micrositeBean.setTipopie((String)microForm.get("tipopie"));
		
		VOUtils.populate(micrositeBean, microForm);  // form --> bean de las traducciones

        return micrositeBean;
	
    }	
    
    
    /**
     * Método que guarda los cambios de un formulario de Configuración de Microsite
     * @param request	petición de usuario
     * @param microForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void cargar (HttpServletRequest request, microForm microForm) throws Exception  {
    
    	Microsite micrositeBean = (Microsite) request.getSession().getAttribute("MVS_microsite");
    	setBeantoForm(micrositeBean, microForm);

	        	
	    //Guardamos los atributos de petición necesarios
	    request.setAttribute("microForm", microForm);
	    request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
	           	
	    //Metemos en el request el CSS que utilizará el tinymce
	    request.setAttribute("MVS_css_tiny",tagCSS(micrositeBean.getEstiloCSS()));   
    }     
    
    
    
    /**
     * Método que vuelca el Bean de Microsite al formulario
     * @param micrositeBean	bean del microsite
     * @param microForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void setBeantoForm (Microsite micrositeBean, microForm microForm) throws Exception  {

    	microForm.set("id",micrositeBean.getId());
		microForm.set("optMapa",micrositeBean.getOptMapa());
		microForm.set("optFaq",micrositeBean.getOptFaq());
		microForm.set("optContacto",micrositeBean.getOptContacto());
    	microForm.set("opt1",micrositeBean.getOpt1());
    	microForm.set("opt2",micrositeBean.getOpt2());
    	microForm.set("opt3",micrositeBean.getOpt3());
    	microForm.set("opt4",micrositeBean.getOpt4());
    	microForm.set("opt5",micrositeBean.getOpt5());
    	microForm.set("opt6",micrositeBean.getOpt6());
    	microForm.set("opt7",micrositeBean.getOpt7());
    	microForm.set("tipocabecera",micrositeBean.getTipocabecera());
    	microForm.set("tipopie",micrositeBean.getTipopie());
    	microForm.set("claveunica",micrositeBean.getClaveunica());
      	
    	VOUtils.describe(microForm, micrositeBean);  // bean --> form de las traducciones
	
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
