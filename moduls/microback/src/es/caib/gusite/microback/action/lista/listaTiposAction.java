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
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * Action que valida y trata el listado de tipos (listados) de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/tiposAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleTipo" path="/detalleTipo.jsp"<BR> 
 *  forward name="listarTipos" path="/tipos.do"
 *  
 *  @author Indra
 */
public class listaTiposAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaNoticiasAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
        
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("tiponotForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	TraDynaActionForm fdet=(TraDynaActionForm) request.getSession().getAttribute("tiponotForm");
        	request.setAttribute("tiponotForm", fdet);
            // combo para que clasifiquen los listados
        	request.setAttribute("validacion", "si");
            request.setAttribute("listaClasificacion", bdTipo.comboClasificacion(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));

            return mapping.findForward("detalleTipo");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("tiponotForm");
            // combo para que clasifiquen los listados
            request.setAttribute("listaClasificacion", bdTipo.comboClasificacion(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));

        	return mapping.findForward("detalleTipo");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
           
        	//request.getSession().setAttribute("mntnmnt", "yes");
        	
        	try {
        		
        	Long id =null;
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                bdTipo.borrarTipo(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            request.getSession().setAttribute("mensajeBorrarTipo", new String(lis));
            
            return mapping.findForward("listarTipos");
            
        	} catch (Exception e) {
        		
        	  request.getSession().setAttribute("elementosNoBorrados", "si");	
        	  return mapping.findForward("listarTipos");	
        	}
        }

        addMessageError(request, "peticion.error");
        return mapping.findForward("info");

    }

}