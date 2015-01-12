package org.ibit.rol.sac.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action que visualiza los documentos del microsite <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/recursosEdita"<BR> 
 *  unknown="false" <BR>
 *  forward name="listarecursos" path="/listaarchivos.jsp" 
 *  
 *  @author - Indra
 */
public class RecursosDocsAction extends Action  {

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

	protected static Log log = LogFactory.getLog(RecursosDocsAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception   {

			// Listado de documentos generales (no dependen de una página de contenido)
			ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
			
        	request.setAttribute("listaDocs", bdConte.listarDocumentos(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().toString(), null));
		
            return mapping.findForward("listarecursos");

    }
	
}