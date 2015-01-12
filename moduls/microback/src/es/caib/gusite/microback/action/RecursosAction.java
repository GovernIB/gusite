package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.Bdrecursos;


/**
 * Action que maneja el action de visualizar las urls disponibles en el microsite <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/recursos"<BR> 
 *  name="BuscaOrdenaRecursoActionForm" <BR>
 *  unknown="false" <BR>
 *  forward name="listarrecursos" path="/listaRecursos.jsp" <BR>
 *  forward name="listarecursostiny" path="/tinymce/jscripts/tiny_mce/plugins/insertararchivos/archivos.jsp"
 *  
 *  @author - Indra
 */
public class RecursosAction extends Action   {


	protected static Log log = LogFactory.getLog(RecursosAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    { 
		String elforward="listarrecursos"; 
		String idiomapasado=""+request.getParameter("lang");
		if (idiomapasado.equals("null")) idiomapasado="ca";
		
		try {
			String paramtiny="" + request.getParameter("tiny");
			String onlydocs="" + request.getParameter("onlydocs");
			
			Bdrecursos bdrecursos = new Bdrecursos(request);
			if (onlydocs.equals("yes")) {
				bdrecursos.doObtainDocumentos(idiomapasado);
				request.setAttribute("MVA_listaArchivos",bdrecursos.getListaarchivos());
				elforward="listarecursostiny";
			} else {
				bdrecursos.doObtainUrls(idiomapasado);
				request.setAttribute("MVA_listaURLs",bdrecursos.getListaoriginal());
				request.setAttribute("MVA_listaArchivos",bdrecursos.getListaarchivos());
				request.setAttribute("MVA_listaArbol",bdrecursos.getListaarbol());
				elforward="listarrecursos";
			}
			
			if (paramtiny.equals("true"))
				request.setAttribute("MVA_forcetiny","true");
			else
				request.setAttribute("MVA_forcetiny","false");

		} catch (Exception e) {
			elforward="error";
		}
        return mapping.findForward(elforward);
    }
	
}
