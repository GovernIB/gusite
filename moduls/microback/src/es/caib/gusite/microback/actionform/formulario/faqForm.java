package es.caib.gusite.microback.actionform.formulario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.*;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las faqs
 *
 *@author Indra
 *
 */
public class faqForm extends TraDynaActionForm {

	private static final long serialVersionUID = -8364831019229187283L;
    protected static Log log = LogFactory.getLog(faqForm.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
    }

    /* (non-Javadoc)
     * @see es.caib.gusite.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        set("fecha", new SimpleDateFormat("dd/MM/yyyy 00:00").format(new java.util.Date()));
    }

   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	if (httpServletRequest.getParameter("modifica") != null || httpServletRequest.getParameter("anyade") != null) {
            if (!FechaValida("" + get("fecha"))) {
                errors.add("fecha", new ActionError("error.faqs.fecha"));
            }

            if (("" + get("idTema")).equals("null") || (Long) get("idTema") == 0) {
                errors.add("tema", new ActionError("error.faqs.tema"));
            }

            try {
                IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
                List lang = idiomaDelegate.listarLenguajes();

                for (int i = 0; i < lang.size(); i++) {
                    if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
                        TraduccionFaq trad = (TraduccionFaq) ((ArrayList) get("traducciones")).get(i);
                        if (trad.getPregunta().length() == 0) {
                            errors.add("titulo", new ActionError("error.faq.pregunta"));
                        }
                    }
                }

            } catch (Throwable t) {
                log.error("Error comprobando la validación de una faq.", t);
            }
        }

        return errors;
    }
       
    public void setFecha(Date fecha) {

    	if (fecha != null) {
    		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		set("fecha", df.format(fecha));
    	}
    }

    public Date getFecha() {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = (String) get("fecha");
        if (fecha.length() == 10) {
            fecha+=" 00:00";
        }
        try {
            return df.parse(fecha);

        } catch (ParseException pe) {
            return null;
        }
    }

    
}
