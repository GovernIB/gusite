package es.caib.gusite.microback.action.buscaordena;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaPerPlantillasActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;
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
 * Created by brújula on 18/02/2015.
 */
public class BuscaOrdenaPerPlantillasAction extends BaseAction {

    protected static Log log = LogFactory.getLog(BuscaOrdenaPerPlantillasAction.class);

    private static final Long LONG_LIST = new Long(10);

    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        BuscaOrdenaPerPlantillasActionForm bForm = (BuscaOrdenaPerPlantillasActionForm) form;
        PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();

        long idMicrosite = ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId();

        Long nreg = personalizacionPlantillaDelegate.countByMicrosite(idMicrosite);

        HashMap<String, Long> parametrosPagina = new HashMap<String, Long>();
        if (nreg > 0) {
            String pag = request.getParameter("pagina");
            Long pagina = (pag != null) ? Long.parseLong(pag) : new Long(1);

            Long cursor = ((pagina-1)*10)+1;
            parametrosPagina.put("cursor", cursor);
            parametrosPagina.put("cursorFinal", cursor + 10);
            if (pagina > 1) {
                parametrosPagina.put("inicio", new Long(1));
                parametrosPagina.put("anterior", pagina - 1);
            }
            if (pagina <= nreg/LONG_LIST) {
                parametrosPagina.put("final", (nreg/LONG_LIST)+1);
                parametrosPagina.put("siguiente", pagina + 1);
            }

            List<PersonalizacionPlantilla> personalizacionPlantillas = personalizacionPlantillaDelegate.searchByMicrosite(idMicrosite, bForm.getOrdenacion(), Math.round(pagina-1), Math.round(LONG_LIST));
            request.setAttribute("FR_PERPLA", personalizacionPlantillas);

        } else {
            addMessageAlert(request, "plantilla.vacio");
        }

        parametrosPagina.put("nreg", nreg);
        request.setAttribute("parametrosPagina", parametrosPagina);
        request.getSession().removeAttribute("TemaFrontForm");

        return mapping.findForward("perPlantillas");
    }

}
