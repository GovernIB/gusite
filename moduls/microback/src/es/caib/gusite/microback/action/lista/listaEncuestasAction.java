package es.caib.gusite.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.microback.actionform.formulario.encuestaForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que valida y trata el listado de Encuestas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/encuestasAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleEncuesta" path="/detalleEncuesta.jsp"<BR> 
 *  
 *  @author Indra
 */
public class listaEncuestasAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaEncuestasAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
     
        //********************************************************
        //************* ERROR DE VALIDACION ENCUESTA *************
        //********************************************************
        if (request.getSession().getAttribute("encuestaForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	encuestaForm fdet=(encuestaForm) request.getSession().getAttribute("encuestaForm");
        	request.setAttribute("encuestaForm", fdet);
        	return mapping.findForward("detalleEncuesta");
        }
        
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("encuestaForm");
        	request.getSession().setAttribute("idmicrosite", (((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()).toString());
        	return mapping.findForward("detalleEncuesta");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id =null;
            EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                bdEncuesta.borrarEncuesta(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            request.getSession().setAttribute("idmicrosite", (((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()).toString());
        	addMessage(request, "mensa.listaencuestas");
            addMessage(request, "mensa.listaencborradas", new String(lis));
            
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}
