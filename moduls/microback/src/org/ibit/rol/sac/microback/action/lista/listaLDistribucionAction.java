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
import org.ibit.rol.sac.microback.actionform.TraDynaActionForm;
import org.ibit.rol.sac.microback.actionform.listaActionForm;
import org.ibit.rol.sac.microback.actionform.formulario.contenidoForm;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.ConvocatoriaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FrqssiDelegate;
import org.ibit.rol.sac.micropersistence.delegate.LDistribucionDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MenuDelegate;


/**
 * Action que valida y trata el listado de listas de distribucion de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="//ldistribucionAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleConte" path="/detalleContenido.jsp"<BR> 
 *  forward name="info" path="/infoContenido.jsp" 
 *  
 *  @author Indra
 */
public class listaLDistribucionAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaLDistribucionAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

    	listaActionForm f = (listaActionForm) form;
        
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("ldistribucionForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	TraDynaActionForm lDistribForm = (TraDynaActionForm) request.getSession().getAttribute("ldistribucionForm");
        	request.setAttribute("ldistribucionForm", lDistribForm);
            return mapping.findForward("detalleLDistribucion");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("ldistribucionForm");
        	return mapping.findForward("detalleLDistribucion");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id = null;
        	LDistribucionDelegate lDistribucioDel = DelegateUtil.getLlistaDistribucionDelegate();
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                lDistribucioDel.borrarListaDistribucion(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            addMessage(request, "mensa.listaldistribucion");
            addMessage(request, "mensa.listaldistribucionborradas", new String(lis));
            
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }    	
}