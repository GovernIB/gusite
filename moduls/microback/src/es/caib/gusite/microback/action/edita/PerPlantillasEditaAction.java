package es.caib.gusite.microback.action.edita;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.PerPlantillasForm;
import es.caib.gusite.microback.actionform.formulario.TemaFrontForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micromodel.Plantilla;
import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by brújula on 23/02/2015.
 */
public class PerPlantillasEditaAction extends BaseAction {

    protected static Log log = LogFactory.getLog(PerPlantillasEditaAction.class);

    private static final Long ORDER_DEFAULT = new Long(1);

    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PerPlantillasForm perPlantillasForm = (PerPlantillasForm) form;

        //************* GUARDAR PER_PLANTILLA *************
        if (esGuardar(request)) {
            crearActualizarPerPlantilla(request, perPlantillasForm);

        //************* NUEVA PER_PLANTILLA *************
        } else if (esNueva(perPlantillasForm)) {
            nuevaPerPlantilla(request, perPlantillasForm);

        //************* CARGAR EXISTENTE *************
        } else {
            cargarPerPlantilla(request, perPlantillasForm);
        }

        return mapping.findForward("detalle");
    }

    private boolean esGuardar(HttpServletRequest request) {
        return request.getParameter("accion") != null && request.getParameter("accion").equals("guardar");
    }

    private boolean esNueva(PerPlantillasForm perPlantillasForm) {
        return perPlantillasForm.get("id") == null;
    }

    private void crearActualizarPerPlantilla(HttpServletRequest request, PerPlantillasForm perPlantillasForm) throws DelegateException {

        PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();
        PersonalizacionPlantilla perPlantilla;
        Long id = (Long) perPlantillasForm.get("id");
        if (id == null) {
            perPlantilla = new PersonalizacionPlantilla();
            perPlantilla = setFormToBean(perPlantillasForm, perPlantilla);
            perPlantilla.setOrden(ORDER_DEFAULT);

            TemaFront temaFront = null;
            TemaFrontForm temaFrontForm = (TemaFrontForm) request.getSession().getAttribute("TemaFrontForm");
            if (temaFrontForm != null) {
                Long idTema = (Long) temaFrontForm.get("id");
                temaFront = DelegateUtil.getTemaFrontDelegate().obtenerTemaFront(idTema);
            }
            perPlantilla.setTema(temaFront);

            Microsite microsite = (Microsite) request.getSession().getAttribute("MVS_microsite");
            perPlantilla.setMicrosite(microsite);

            personalizacionPlantillaDelegate.crearPersonalizacionPlantilla(perPlantilla);
        } else {
            perPlantilla = personalizacionPlantillaDelegate.obtenerPersonalizacionPlantilla(id);
            perPlantilla = setFormToBean(perPlantillasForm, perPlantilla);
            personalizacionPlantillaDelegate.actualizarPersonalizacionPlantilla(perPlantilla);
        }
        addMessage(request, "plantilla.info.modifica");
    }

    private void nuevaPerPlantilla(HttpServletRequest request, PerPlantillasForm perPlantillasForm) throws DelegateException {

        Long id = Long.parseLong(request.getParameter("plan"));
        Plantilla plantilla = DelegateUtil.getPlantillaDelegate().obtenerPlantilla(id);
        perPlantillasForm.set("plantilla", plantilla.getTitulo());
        perPlantillasForm.set("idPlantilla", plantilla.getId());
        if (plantilla.getDescripcion() != null) {
            perPlantillasForm.set("ayuda", plantilla.getDescripcion());
        }
        
        // Plantilla en blanco
        Long pp = Long.parseLong(request.getParameter("pp"));
        if (pp != null) {
        	if (pp == -1) {
                perPlantillasForm.set("contenido", this.obtenerContenidoPlantillaBase(request, plantilla) );
        	} else {
        		//plantilla existente
                PersonalizacionPlantilla personalizacionPlantilla = DelegateUtil.getPersonalizacionPlantillaDelegate().obtenerPersonalizacionPlantilla(pp);
                if (personalizacionPlantilla != null) {
                    perPlantillasForm.set("contenido", personalizacionPlantilla.getContenido());
                }
        	}
        }
    }

    private String obtenerContenidoPlantillaBase(HttpServletRequest request, Plantilla plantilla) {
		//código de base, lo leemos del zip del tema
		InputStream templatesZip = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/template-front.zip");
        ZipInputStream zis = new ZipInputStream(templatesZip);
        ZipEntry entry;
		try {
			entry = zis.getNextEntry();
	        String templateFileName = plantilla.getNombre() + ".html";
	        while (entry != null) {
	            if (entry.getName().equals(templateFileName)) {
	            	StringBuilder s = new StringBuilder();
	            	byte[] buffer = new byte[1024];
	            	int read = 0;
		    	    while ((read = zis.read(buffer, 0, 1024)) >= 0) {
		    	    	s.append(new String(buffer, 0, read));
		    	    }
		    	    return s.toString();
	    	     }
	            entry = zis.getNextEntry();
	        }
	        return null;
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}

    private void cargarPerPlantilla(HttpServletRequest request, PerPlantillasForm perPlantillasForm) throws DelegateException {

        Long id = Long.parseLong(request.getParameter("id"));
        PersonalizacionPlantilla personalizacionPlantilla = DelegateUtil.getPersonalizacionPlantillaDelegate().obtenerPersonalizacionPlantilla(id);
        setBeanToForm(personalizacionPlantilla, perPlantillasForm);
    }

    private void setBeanToForm(PersonalizacionPlantilla personalizacionPlantilla, PerPlantillasForm perPlantillasForm) {

        perPlantillasForm.set("id", personalizacionPlantilla.getId());
        perPlantillasForm.set("plantilla", personalizacionPlantilla.getPlantilla().getNombre());
        perPlantillasForm.set("titulo", personalizacionPlantilla.getTitulo());
        perPlantillasForm.set("idPlantilla", personalizacionPlantilla.getPlantilla().getId());
        if (personalizacionPlantilla.getContenido() != null) {
            perPlantillasForm.set("contenido", personalizacionPlantilla.getContenido());
        }

        if (personalizacionPlantilla.getPlantilla().getDescripcion() != null) {
            perPlantillasForm.set("ayuda", personalizacionPlantilla.getPlantilla().getDescripcion());
        }
    }

    private PersonalizacionPlantilla setFormToBean(PerPlantillasForm perPlantillasForm, PersonalizacionPlantilla personalizacionPlantilla) throws DelegateException {

        personalizacionPlantilla.setId((Long) perPlantillasForm.get("id"));
        personalizacionPlantilla.setTitulo((String) perPlantillasForm.get("titulo"));
        Long idPlantilla = (Long) perPlantillasForm.get("idPlantilla");
        personalizacionPlantilla.setContenido((String) perPlantillasForm.get("contenido"));
        if (idPlantilla != null) {
            Plantilla plantilla = DelegateUtil.getPlantillaDelegate().obtenerPlantilla(idPlantilla);
            personalizacionPlantilla.setPlantilla(plantilla);
        }

        return personalizacionPlantilla;
    }
}
