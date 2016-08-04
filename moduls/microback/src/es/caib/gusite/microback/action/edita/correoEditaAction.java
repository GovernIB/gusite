package es.caib.gusite.microback.action.edita;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.correoForm;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.micromodel.Correo;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;

/**
 * Action que edita los convocatorias de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/distribucionEdita" <BR> 
 *  name="convocatoriaForm" <BR> 
 *  input="/convocatoriasAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleDistribucion.jsp" <BR>
 *  forward name="info" path="/infoDistribucion.jsp"
 *  
 *  @author Salvador Antich
 */
public class correoEditaAction extends BaseAction 
{

	protected static Log log = LogFactory.getLog(correoEditaAction.class);


	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		correoForm mailForm = (correoForm)form;
		//********************************************************
        //*********** EDICION / CREACION DE LA LISTA DE DISTRIBUCION **********
        //********************************************************

    	if 	((String)request.getParameter("accion") != null) {
    		
    		if (request.getParameter("accion").equals("crear")) {
    			mailForm.resetForm();       		
    			return mapping.findForward("detalle");
    			
    		}else if (request.getParameter("accion").equals("guardar")) {	        	
	        	guardar(mailForm, request);
	        	addMessage(request, "mensa.anadecorreo");
	       		addMessage(request, "mensa.editarldistribucion", "" + mailForm.getId());
	           	addMessage(request, "mensa.listaldistribucion");
	        	return mapping.findForward("info");
	        }
    	}else if (request.getParameter("id")!=null) { 
    		try{
        		recuperar(form, request);	                
        	}catch(Exception e){
        		addMessage(request, "peticion.error");
                return mapping.findForward("info");
        	}
            return mapping.findForward("detalle");
        }
		request.setAttribute("correoForm", mailForm);
		//Refresco de parámetro MVS de menú
		Base.menuRefresh(request);

		return mapping.findForward("detalle");    
	}
	
	private boolean guardar(ActionForm form, HttpServletRequest request) throws Exception{
		boolean nuevo = false;
		correoForm mailForm = (correoForm)form;
		LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
		
		Correo correo = distribDelegate.consultaCorreo(mailForm.getCorreo());
    	if (correo == null)	{
    		nuevo = true;
			correo = new Correo();
        }		
    	correo.setCorreo(mailForm.getCorreo());
    	correo.setNombre(mailForm.getNombre());	        
		correo.setApellidos(mailForm.getApellidos());
		correo.setIntentoEnvio(mailForm.getIntentoEnvio());
		correo.setTraceError(mailForm.getTraceError());
		correo.setNoEnviar(("S".equals(mailForm.getNoEnviar()))?true:false);
		distribDelegate.actualizaCorreo(correo);
		distribDelegate.anadeCorreo(mailForm.getId(), correo.getCorreo());
    	return nuevo;
	}
	
	private void recuperar(ActionForm form, HttpServletRequest request)throws Exception{
		correoForm mailForm = (correoForm)form;
		LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
		Correo correo = distribDelegate.consultaCorreo(request.getParameter("correo"));
    	//*********************************************
		mailForm.setId(new Long(request.getParameter("id")));
		mailForm.setCorreo(correo.getCorreo());
		mailForm.setNombre(correo.getNombre());	        
		mailForm.setApellidos(correo.getApellidos());
		mailForm.setIntentoEnvio(correo.getIntentoEnvio());
		mailForm.setTraceError(correo.getTraceError());
		if (correo.getNoEnviar()!=null && correo.getNoEnviar()) 
			mailForm.setNoEnviar("S");
		else
			mailForm.setNoEnviar("N");
	}
}

