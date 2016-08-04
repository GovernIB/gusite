package es.caib.gusite.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Action que valida y trata el listado de documentos de un microsite <BR>
 *  
 *  @author Indra
 */
public class ListaDocumentosAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(ListaDocumentosAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

	    ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
	    
	    request.setAttribute("listaDocs", bdConte.listarDocumentos(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().toString(),null));
	    String idcontenido = (request.getParameter("page") != null) ? request.getParameter("page") : "null";
	    
	    if (!idcontenido.equals("null"))
	    	request.setAttribute("listaDocsPropios", bdConte.listarDocumentos(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().toString(),idcontenido));
	    
	    return mapping.findForward("listarDocs");

    }

}