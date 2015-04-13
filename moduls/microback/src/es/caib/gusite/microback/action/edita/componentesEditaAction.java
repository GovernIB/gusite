package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.componenteForm;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.MicrositeCompleto;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * Action que edita los componentes de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/componenteEdita" <BR> 
 *  name="componenteForm" <BR> 
 *  input="/componentesAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleComponente.jsp" <BR>
 *  
 *  @author Indra
 */
public class componentesEditaAction extends BaseAction 
{
	
	protected static Log log = LogFactory.getLog(componentesEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
  
    	try {
    	
    	ComponenteDelegate componenteDelegate = DelegateUtil.getComponentesDelegate();
    	TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
    	Componente componenteBean=null;
    	componenteForm componenteForm = (componenteForm) form;
    	Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	
        if (request.getParameter("accion") != null) {
            
	    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null
	    	|| (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar")))){

		    	componenteBean = setFormtoBean(request, componenteForm, micrositeBean);
	           
		    	componenteDelegate.grabarComponente(componenteBean);
	
		       	if (componenteForm.get("id") == null){
		       		componenteForm.set("id",componenteBean.getId());
		       		addMessageWithDate(request, "mensa.nuevocompo");
		       	} else addMessageWithDate(request, "mensa.modifcompo"); 
               
	       } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir"))) 
	    	   traducir(request, componenteForm);

        } else setBeantoForm(request,componenteForm, new Long(""+request.getParameter("id")), micrositeBean);      

        request.setAttribute("tiposCombo", tipoDelegate.listarCombo(micrositeBean.getId())); 
        return mapping.findForward("detalle");
        
		} catch (Exception e) {
	        addMessageError(request, "peticion.error");
	        return mapping.findForward("info");    		
		}
    }
    
    
    /**
     * Método que vuelca los datos del formulario en el Bean de Componente
     * @param request			 petición
     * @param componenteForm	 formulario dinámico enviado por usuario
     * @param micrositeBean		 bean del microsite
     * @return Componente 		 devuelve bean de Componente con los datos del formulario
     * @throws Exception
     */
    private Componente setFormtoBean (HttpServletRequest request, componenteForm componenteForm, Microsite micrositeBean) throws Exception  {

    	ComponenteDelegate componenteDelegate = DelegateUtil.getComponentesDelegate();
    	Componente componenteBean = null;
    	
    	if (componenteForm.get("id") == null) {  
        	componenteBean = new Componente(); // Es Alta
        } else {  // Es modificacion
        	componenteBean = componenteDelegate.obtenerComponente((Long)componenteForm.get("id"));
        	if (componenteBean.getIdmicrosite().longValue()!=micrositeBean.getId().longValue()) throw new Exception();
        }
    	
	    componenteBean.setIdmicrosite(micrositeBean.getId());
	   	componenteBean.setNombre(""+componenteForm.get("nombre"));
	   	componenteBean.setFilas(""+componenteForm.get("filas"));
	   	
	   	if (componenteForm.get("numelem")!=null)
	   		componenteBean.setNumelementos(new Integer(""+componenteForm.get("numelem")));
	   	else
	   		componenteBean.setNumelementos(null);
	   	
	   	componenteBean.setOrdenacion(new Integer(""+componenteForm.get("orden")));
	   	componenteBean.setSoloimagen(""+componenteForm.get("soloimg"));
		
	    // Establezco el objeto Tipo de Noticia
	    Tipo tp=null;
	    TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
	    if (componenteForm.get("id") == null)  	tp = bdTipo.obtenerTipo((Long)componenteForm.get("idTipo"));
	    else 			        	tp = componenteBean.getTipo();
	    tp.setId((Long)componenteForm.get("idTipo"));
	    componenteBean.setTipo(tp);
	    
	    //	Establezco la imagen bullet
	    FormFile imagen = (FormFile) componenteForm.get("imagen");
	    if (archivoValido(imagen))
	    	componenteBean.setImagenbul(populateArchivo(componenteBean.getImagenbul(), imagen, null, null));
	    else if (((Boolean) componenteForm.get("imagenbor")).booleanValue()) componenteBean.setImagenbul(null);
	    
	    if (componenteBean.getImagenbul() != null) 
	        if ((""+componenteForm.get("imagennom")).length()>0) 
	        	componenteBean.getImagenbul().setNombre(""+componenteForm.get("imagennom"));
	
	    VOUtils.populate(componenteBean, componenteForm);  // form --> bean
    	
    	return componenteBean;
    }
    

    /**
     * Método que vuelca los datos del Bean de Componente al formulario del usuario
     * @param request			petición
     * @param componenteForm	formulario dinámico enviado por usuario
     * @param idComponente		Id del componente
     * @param micrositeBean		bean de microsite
     * @throws Exception
     */
    private void setBeantoForm (HttpServletRequest request, componenteForm componenteForm, Long idComponente, Microsite micrositeBean) throws Exception  {

    	ComponenteDelegate componenteDelegate = DelegateUtil.getComponentesDelegate();
    	Componente componenteBean = null;

        if (idComponente != null){
            if (componenteDelegate.checkSite(micrositeBean.getId(),idComponente)) {
            	addMessageError(request, "info.seguridad");
            	throw new Exception();
            }
            componenteBean = componenteDelegate.obtenerComponente(idComponente);
        	if (componenteBean.getIdmicrosite().longValue()!=micrositeBean.getId().longValue())	throw new Exception();
        	}
        
            componenteForm.set("nombre", componenteBean.getNombre());
            componenteForm.set("filas", componenteBean.getFilas());
            componenteForm.set("numelem", componenteBean.getNumelementos());
            componenteForm.set("orden", componenteBean.getOrdenacion());
            componenteForm.set("idTipo", componenteBean.getTipo().getId());
            componenteForm.set("soloimg", componenteBean.getSoloimagen());
            
            VOUtils.describe(componenteForm, componenteBean);  // bean --> form
  
            if (componenteBean.getImagenbul() != null) {
            	componenteForm.set("imagennom", componenteBean.getImagenbul().getNombre());
            	componenteForm.set("imagenid", componenteBean.getImagenbul().getId());
            }
    	
    }     
    
    
    /**
     * Método que traduce un formulario de Componente
     * @param request					petición de usuario
     * @param componenteForm			formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void traducir (HttpServletRequest request, componenteForm componenteForm) throws Exception  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getAttribute("traductor");
    		String idiomaOrigen = "ca";

            TraduccionComponente ComponenteOrigen = (TraduccionComponente) componenteForm.get("traducciones", 0);
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");

            Iterator<?> itTradFichas = ((ArrayList<?>) componenteForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
            
            while (itLang.hasNext()){

            	String idiomaDesti = itLang.next();
            	TraduccionComponente ComponenteDesti = (TraduccionComponente) itTradFichas.next();
	
			   	if (ComponenteDesti == null) {
			   		micrositeBean.setTraduccion(idiomaDesti, new TraduccionComponente());
			   		ComponenteDesti = (TraduccionComponente) micrositeBean.getTraduccion(idiomaDesti);
			   	}
      	
            	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
            	if (idiomasMicro.contains(idiomaDesti)) {

	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(ComponenteOrigen, ComponenteDesti)) {
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
           
			log.info("Traducción Componente - Id: " + (Long) componenteForm.get("id"));
    }    
    
}

