package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.Bdcomponentes;

/**
 * Action que utiliza solo TinyMCE y que visualiza componentes de un microsite (elementos, banner, agenda...).<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/listartinycomponentes"<BR> 
 *  forward name="listarcomponentes" path="/tinymce/jscripts/tiny_mce/plugins/componentesmicros/componentes.jsp"
 *  
 *  @author - Indra
 */
public class ComponentesTinyAction extends Action   {


	protected static Log log = LogFactory.getLog(ComponentesTinyAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
		String elforward="listarcomponentes";
		try {
			Bdcomponentes bdcomponentes = new Bdcomponentes(request, form);
			request.setAttribute("MVA_listacomponentes",bdcomponentes.getListaoriginal());


		} catch (Exception e) {
			elforward="error";
		}
        return mapping.findForward(elforward);
    }
	
}