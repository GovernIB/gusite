package org.ibit.rol.sac.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.respuestaForm;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Pregunta;
import org.ibit.rol.sac.micromodel.TraduccionEncuesta;
import org.ibit.rol.sac.micromodel.TraduccionPregunta;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que valida y trata el listado de respuestas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/respuestasAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalle" path="/detalleRespuesta.jsp" 
 *  
 *  @author Indra
 */
public class listaRespuestasAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaRespuestasAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

    	//********************************************************
        //************* ERROR DE VALIDACION PREGUNTA *************
        //********************************************************
    	
        if (request.getSession().getAttribute("respuestaForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	
        	respuestaForm fdet=(respuestaForm) request.getSession().getAttribute("respuestaForm");
        	request.setAttribute("respuestaForm", fdet);

           	EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
    		Pregunta pre = bdEncuesta.obtenerPregunta(new Long(""+fdet.get("idpregunta")));
    		Encuesta enc = bdEncuesta.obtenerEncuesta(pre.getIdencuesta());
    		    		
    		request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
        	request.setAttribute("titpregunta",((TraduccionPregunta)pre.getTraduccion()).getTitulo());
        	request.setAttribute("idencuesta", ""+enc.getId());

        	
        	return mapping.findForward("detalle");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    	
    }

}