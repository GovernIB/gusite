package org.ibit.rol.sac.microback.action.edita;

import java.util.ArrayList;
import java.util.Iterator;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.TraDynaActionForm;
import org.ibit.rol.sac.microback.utils.VOUtils;
import org.ibit.rol.sac.micromodel.Actividadagenda;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionActividadagenda;
import org.ibit.rol.sac.micropersistence.delegate.ActividadDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

import es.indra.gusite.microintegracion.traductor.TraductorMicrosites;

/**
 * Action que edita las actividades de un microsite<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/actividadEdita" <BR> 
 *  name="actividadForm" <BR> 
 *  input="/actividadesAcc.do" <BR>
 *	scope="session" <BR>
 *  forward name="detalle" path="/detalleActividad.jsp"
 *  
 *  @author - Indra
 */
public class actividadesEditaAction extends BaseAction 
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
	
	protected static Log log = LogFactory.getLog(actividadesEditaAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActividadDelegate bdActividad = DelegateUtil.getActividadagendaDelegate();
    	Actividadagenda activi=null;
    	TraDynaActionForm actividadForm = (TraDynaActionForm) form;
    	
    	if(request.getParameter("accion")!=null){
        	/********************** GUARDAR *************************/
    		if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar"))) {
    			
	    		boolean isNuevaActividad = false;
	    		    
	    		// ALTA
	    		if (actividadForm.get("id") == null) {  
	    			activi = new Actividadagenda(); 
		        	isNuevaActividad = true;
		        // MODIFICACION 	
	    		} else {  
		            activi = bdActividad.obtenerActividad((Long)actividadForm.get("id"));
		            //************COMPROBACION DE IDES*************
		            if (activi.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue()){
		            	addMessageError(request, "peticion.error");
		                return mapping.findForward("info");
		            }
	    		 }
		        	
		        activi.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
		        VOUtils.populate(activi, actividadForm);  // form --> bean
		        
		        bdActividad.grabarActividad(activi);
		        actividadForm.set("id",activi.getId());
		        
		       	if(isNuevaActividad) addMessageWithDate(request, "mensa.nuevaactividad");
		       		else addMessageWithDate(request, "mensa.modifactividad");

		       	return mapping.findForward("detalle");
		       	
		    /********************** TRADUCIR ************************/	
    		}else if	(request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir"))) {
	    	   
    			traducir (request, actividadForm);
	    	    return mapping.findForward("detalle");
	      
		  /********************** BORRAR ************************/	
	        }else if	(request.getParameter("accion").equals(getResources(request).getMessage("operacion.borrar"))) {
	    	   
	           try{
		        	ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
		            Long id = (Long)actividadForm.get("id");
		          
		            bdActivi.borrarActividad(id);
		            actividadForm.resetForm(mapping, request);
		            addMessageAlert(request, "actividad.borrar.sinid");
	            } catch (Exception e) {
	        		addAlert(request, "agenda.activitat.alert.elementosnoborrados");
			      	return mapping.findForward("detalle");	
			   }
	            return mapping.findForward("detalle");
	       

  		 /********************** CREAR ************************/
	      }else if	(request.getParameter("accion").equals(getResources(request).getMessage("operacion.crear"))) {
	    	  actividadForm.resetForm(mapping, request);
	    	  request.setAttribute("actividadForm",actividadForm);
	    	  return mapping.findForward("detalle");
	    	  
	      }
        }
        
    	/********************** EDITA *************************/
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));

            	if (bdActividad.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
                	addMessageError(request, "info.seguridad");
                	return mapping.findForward("info");
                }

            	activi = bdActividad.obtenerActividad(id);
            	//************COMPROBACION DE IDES*************
            	if (activi.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessageError(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	TraDynaActionForm fdet=(TraDynaActionForm) form;
                VOUtils.describe(fdet, activi);  // bean --> form

                return mapping.findForward("detalle");

        }

        addMessageError(request, "peticion.error");
        return mapping.findForward("info");
    }
    
    
    /**
     * Método que traduce un formulario de una actividad de la agenda
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinámico enviado por usuario
     * @throws Exception
     * @author Indra
     */
    private void traducir (HttpServletRequest request, TraDynaActionForm actividadForm) throws Exception  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getAttribute("traductor");
    		String idiomaOrigen = "ca";

    		TraduccionActividadagenda actividadagendaOrigen = (TraduccionActividadagenda) actividadForm.get("traducciones", 0);
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");

            Iterator<?> itTradFichas = ((ArrayList<?>) actividadForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            
            while (itLang.hasNext()){

            	String idiomaDesti = itLang.next();
            	TraduccionActividadagenda actividadagendaDesti = (TraduccionActividadagenda) itTradFichas.next();
	
			   	if (actividadagendaDesti == null) {
			   		micrositeBean.setTraduccion(idiomaDesti, new TraduccionActividadagenda());
			   		actividadagendaDesti = (TraduccionActividadagenda) micrositeBean.getTraduccion(idiomaDesti);
			   	}

            	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
            	if (micrositeBean.getIdiomas().contains(idiomaDesti)) {

	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(actividadagendaOrigen, actividadagendaDesti)) {
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
			
           
			log.info("Traducción Actividad - Id: " + (Long) actividadForm.get("id"));
    }    
    
    
}
