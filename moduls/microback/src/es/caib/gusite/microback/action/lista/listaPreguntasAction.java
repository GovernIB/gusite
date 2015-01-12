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
import es.caib.gusite.microback.actionform.formulario.preguntaForm;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que valida y trata el listado de preguntas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/preguntasAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalle" path="/detallePregunta.jsp" 
 *  
 *  @author Indra
 */
public class listaPreguntasAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaPreguntasAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

    	//********************************************************
        //************* ERROR DE VALIDACION PREGUNTA *************
        //********************************************************
    	
        if (request.getSession().getAttribute("preguntaForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	
        	preguntaForm fdet=(preguntaForm) request.getSession().getAttribute("preguntaForm");
        	request.setAttribute("preguntaForm", fdet);
        	
        	EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
    		Encuesta enc = bdEncuesta.obtenerEncuesta(new Long(""+fdet.get("idencuesta")));
    		request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
        	request.setAttribute("titpregunta","");
        	request.setAttribute("idencuesta", ""+fdet.get("idencuesta"));
        	
        	return mapping.findForward("detalle");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    	
    }

}




    	