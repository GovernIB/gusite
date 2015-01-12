package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.base.Base;
import es.caib.gusite.micropersistence.util.log.MicroLog;

/**
 * Action que administra un microsite  <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/home"<BR> 
 *  unknown="false" <BR>
 *  forward name="inicio" path="/index.jsp" 
 *  forward name="listadomicrosites" path="/microsites.do"
 *  
 *  @author - Indra
 */
public class HomeAction extends BaseAction  {


	protected static Log log = LogFactory.getLog(HomeAction.class);
	
		
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {

		siEstaEnMantenimientoAvisar(request);
		
	   	
    	/***************************************************************/
    	/***************   RECOGER USUARIO Y ROLES   *******************/
    	/***************************************************************/    	
    	Base.usuarioRefresh(request);
    	String sysOradmin = (String)request.getSession().getAttribute("MVS_rol_sys_adm");
    	

    	/***************************************************************/
    	/********************  LISTADO MICROSITES  *********************/
    	/***************************************************************/
    	String psite = ""+request.getParameter("idsite");
    	String paccion = ""+request.getParameter("accion");
        if ( (psite.equals("null")) &  (paccion.equals("null")) )  {
        	return mapping.findForward("listadomicrosites");
        }  
    	

    	
    	/***************************************************************/
    	/********************     IMPORTAR         *********************/
    	/***************************************************************/
        String importar=""+request.getParameter("accion");
        if ( (importar.equals("importar")) &&  (sysOradmin.equals("yes")) )  {
        	Base.borrarVSession(request);
        	request.setAttribute("MVS_pagina_inicio", "importar.do");
        	return mapping.findForward("inicio");
        }    	
        
        
    	/***************************************************************/
    	/********************     EXPORTAR         *********************/
    	/***************************************************************/
        String exportar=""+request.getParameter("accion");
        if (exportar.equals("exportar")) {
        	Long idsite = new Long(""+request.getParameter("idsite"));
			if (Base.hasMicrositePermiso(request, idsite)) {
				Base.micrositeRefresh(idsite,request);
				Base.menuRefresh(request);
	        	request.setAttribute("MVS_pagina_inicio", "exportador.do");
	        	return mapping.findForward("inicio");
			} else {
				Base.borrarVSession(request);
				log.warn("[AVISO] Intento de acceso al microsite id=" + idsite + " sin los permisos necesarios");
				MicroLog.addLog("[AVISO] Intento de acceso al microsite [" + idsite + "] sin los permisos necesarios");
				addMessage(request, "peticion.error");
				request.setAttribute("MVS_index_con_info", "yes");
			}
        }         

        
    	/***************************************************************/
    	/*********************     CONSOLA         *********************/
    	/***************************************************************/
        String consola=""+request.getParameter("accion");
        if ( (consola.equals("consola")) &&  (sysOradmin.equals("yes")) )  {
        	Base.borrarVSession(request);
        	request.setAttribute("MVS_pagina_inicio", "consola.do");
        	return mapping.findForward("inicio");
        }    	
        
        
        
        /***************************************************************/
    	/********************         ALTA         *********************/
    	/***************************************************************/        
        String alta=""+request.getParameter("accion");
        if ( (alta.equals("alta")) &&  (sysOradmin.equals("yes")) )  {
        	Base.borrarVSession(request);
        	request.setAttribute("MVS_pagina_inicio", "microEdita.do?accion=alta");
        	return mapping.findForward("inicio");
        }

        
    	/***************************************************************/
    	/********************     ELIMINAR         *********************/
    	/***************************************************************/
        String eliminar=""+request.getParameter("accion");
        if ( (eliminar.equals("eliminar")) &&  (sysOradmin.equals("yes")) )  {
            Long idmicro = new Long(0);
            idmicro = new Long(""+request.getParameter("idsite"));        	
        	Base.borrarVSession(request);
        	request.setAttribute("MVS_pagina_inicio", "eliminarmicro.do?idsite="+idmicro);
        	MicroLog.addLog("Eliminació Microsite: [" + request.getParameter("idsite") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
        	return mapping.findForward("inicio");
        }    	
        	

            
        
        
        /***************************************************************/
    	/******************  MODIFICACION/GESTION   ********************/
    	/***************************************************************/          
		Long idmicrosite = new Long(0);
		try {
			
			if (request.getParameter("idsite") != null)
			idmicrosite = new Long(""+request.getParameter("idsite"));
			else if (request.getAttribute("idsite") != null)
				idmicrosite = new Long(""+request.getAttribute("idsite"));
			if (Base.hasMicrositePermiso(request, idmicrosite)) {
				Base.micrositeRefresh(idmicrosite,request);
				Base.menuRefresh(request);
			} else {
				Base.borrarVSession(request);
				log.warn("[AVISO] Intento de acceso al microsite id=" + idmicrosite + " sin los permisos necesarios");
				MicroLog.addLog("[AVISO] Intento de acceso al microsite [" + idmicrosite + "] sin los permisos necesarios, Usuari: [" + request.getSession().getAttribute("username") + "]");
				addMessage(request, "peticion.error");
				request.setAttribute("MVS_index_con_info", "yes");
			}
	
			MicroLog.addLog("Access al backoffice del Microsite: [" + request.getParameter("idsite") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
		} catch (Exception e) {
			Base.borrarVSession(request);
			log.error("Se ha producido un error accediendo al microsite: " + idmicrosite + "\n error=" + e.getMessage());
			MicroLog.addLogStackTrace("Se ha producido un error accediendo al microsite: " + idmicrosite + "\n error=" + e.getMessage(), e.getStackTrace());
			addMessage(request, "peticion.error");
			request.setAttribute("MVS_index_con_info", "yes");
		}
		
		
		return mapping.findForward("inicio");
		
    }
	
	protected void siEstaEnMantenimientoAvisar(HttpServletRequest request) {
		if(estaEnMantenimiento() ) {
			request.setAttribute("MVS_manteniment", new Object());
		}
	}


	protected boolean estaEnMantenimiento() {
		String mantenimiento = System.getProperty("es.caib.gusite.mantenimiento");
		return null!=mantenimiento && "si".equalsIgnoreCase(mantenimiento);
	}
	
}
