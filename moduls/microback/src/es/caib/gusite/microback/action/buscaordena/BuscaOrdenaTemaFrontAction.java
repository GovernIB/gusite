package es.caib.gusite.microback.action.buscaordena;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaTemaFrontActionForm;
import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TemaFrontDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brújula on 19/02/2015.
 */
public class BuscaOrdenaTemaFrontAction extends BaseAction {

    protected static Log log = LogFactory.getLog(BuscaOrdenaTemaFrontAction.class);

    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        BuscaOrdenaTemaFrontActionForm bForm = (BuscaOrdenaTemaFrontActionForm) form;
        TemaFrontDelegate temaFrontDelegate = DelegateUtil.getTemaFrontDelegate();

        List<TemaFront> temaFronts = temaFrontDelegate.listarTemaFrontOrden(bForm.getOrdenacion());
        request.setAttribute("temaFronts", temaFronts);

        HashMap<String, Integer> parametrosPagina = new HashMap<String, Integer>();
        parametrosPagina.put("nreg", temaFronts.size());
        request.setAttribute("parametrosPagina", parametrosPagina);

        // Limpiamos el microsite de la sessión
        request.getSession().removeAttribute("MVS_microsite");

        return mapping.findForward("temaFront");
    }
}
