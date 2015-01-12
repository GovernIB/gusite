package org.ibit.rol.sac.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaSeleccionarLDistribucionActionForm;
import org.ibit.rol.sac.micropersistence.delegate.ConvocatoriaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


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
public class listaSeleccionarLDistribucionAction extends BaseAction {

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

    	BuscaOrdenaSeleccionarLDistribucionActionForm f = (BuscaOrdenaSeleccionarLDistribucionActionForm) form;
        
        ConvocatoriaDelegate convDel = DelegateUtil.getConvocatoriaDelegate();
        convDel.quitarDestinatarios(f.getIdConvocatoria());    	    	
    	if (f.getSeleccionados() != null) {
			for (int i = 0; i < f.getSeleccionados().length; i++) {
				convDel.ponerDestinatario(f.getIdConvocatoria(), new Long(f.getSeleccionados()[i]));
			}
		}
		String url = mapping.findForward("detalleConvocatoria").getPath() + "?id=" + f.getIdConvocatoria();
        return new ActionForward(url);    	
    }    	
}