package es.caib.gusite.microback.action.lista;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.PerPlantillasForm;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TemaFrontDelegate;
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
public class ListaTemaFrontAction extends BaseAction {

    protected static Log log = LogFactory.getLog(ListaTemaFrontAction.class);

    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;

        //************* ERROR DE VALIDACION ENCUESTA *************
        if (request.getSession().getAttribute("TemaFrontForm") != null && request.getAttribute(Globals.ERROR_KEY) != null) {
            PerPlantillasForm fdet = (PerPlantillasForm) request.getSession().getAttribute("TemaFrontForm");
            request.setAttribute("TemaFrontForm", fdet);
            return mapping.findForward("detalleTemaFront");
        }

        //********************** CREAMOS *************************
        if (f.getAccion().equals("crear")) {
            request.getSession().removeAttribute("TemaFrontForm");
            List<TemaFront> temaFronts = DelegateUtil.getTemaFrontDelegate().listarTemaFrontPadres();
            request.setAttribute("temasFrontPadres", temaFronts);
            return mapping.findForward("detalleTemaFront");
        }

        //********************* BORRAMOS *************************
        if (f.getAccion().equals("borrar")) {
            TemaFrontDelegate temaFrontDelegate = DelegateUtil.getTemaFrontDelegate();
            List<Long> ids = new ArrayList<Long>();
            for (String id : f.getSeleccionados()) {
                Long temaId = Long.parseLong(id);
                TemaFront tema = temaFrontDelegate.obtenerTemaFrontCompleto(temaId);
                if (tema.getTemasHijos().size() > 0) {
                    addMessage(request, "frontTemas.borrarTemaConHijos", tema.getNombre());
                	for (TemaFront temaHijo : tema.getTemasHijos()) {
                        addMessage(request, "frontTemas.hijo", "<a href='temaFrontEdita.do?id=" + temaHijo.getId() + "'>" + temaHijo.getNombre() + "</a>");
                	}
                } else if (tema.getMicrosites().size() > 0) {
                    addMessage(request, "frontTemas.borrarTemaConSites", tema.getNombre());
                	for (Microsite site : tema.getMicrosites()) {
                        addMessage(request, "frontTemas.site", "<a href='index.do?idsite=" + site.getId() + "'>" + site.getUri() + "</a>" );
                	}
                } else {
                    temaFrontDelegate.borrarTemaFront(tema);
    				ids.add(temaId );
                }
            }

            addMessage(request, "frontTemas.listaTemasFront");
            if (ids.size()>0) {
                addMessage(request, "frontTemas.listaTemasBorradas", ids.toString());
            }
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
