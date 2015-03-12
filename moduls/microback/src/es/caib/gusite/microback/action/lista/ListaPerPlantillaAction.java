package es.caib.gusite.microback.action.lista;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.PerPlantillasForm;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.micromodel.Plantilla;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcerda on 24/02/2015.
 */
public class ListaPerPlantillaAction extends BaseAction {

    protected static Log log = LogFactory.getLog(ListaPerPlantillaAction.class);

    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;

        //************* ERROR DE VALIDACION ENCUESTA *************
        if (request.getSession().getAttribute("PerPlantillasForm") != null && request.getAttribute(Globals.ERROR_KEY) != null) {
            PerPlantillasForm fdet = (PerPlantillasForm) request.getSession().getAttribute("PerPlantillasForm");
            request.setAttribute("PerPlantillasForm", fdet);
            return mapping.findForward("detallePerPlantilla");
        }

        //********************** CREAMOS *************************
        if (f.getAccion().equals("crear")) {
            request.getSession().removeAttribute("PerPlantillasForm");
            List<Plantilla> plantillas = DelegateUtil.getPlantillaDelegate().listarPlantilla();
            request.setAttribute("plantillas", plantillas);
            return mapping.findForward("crearPerPlantilla");
        }

        //********************* BORRAMOS *************************
        if (f.getAccion().equals("borrar")) {
            PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();
            List<Long> ids = new ArrayList<Long>();
            for (String id : f.getSeleccionados()) {
                ids.add(Long.parseLong(id));
            }
            personalizacionPlantillaDelegate.borrarPersonalizacionPlantillas(ids);

            addMessage(request, "plantilla.listaPlantillas");
            addMessage(request, "plantilla.listaPlanBorradas", ids.toString());
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
