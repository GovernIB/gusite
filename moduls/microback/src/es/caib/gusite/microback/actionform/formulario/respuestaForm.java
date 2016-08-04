package es.caib.gusite.microback.actionform.formulario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.*;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las respuestas en encuestas
 *
 *@author Indra
 *
 */
public class respuestaForm extends TraDynaActionForm {

	private static final long serialVersionUID = 5223218924562075313L;
	protected static Log log = LogFactory.getLog(preguntaForm.class);


    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
    }

    /* (non-Javadoc)
     * @see es.caib.gusite.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        super.reset(mapping, request);
    }

   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    if(httpServletRequest.getParameter("modifica")!=null || httpServletRequest.getParameter("anyade")!=null) {

        if (!esEntero(""+get("orden")) )
        	errors.add("orden", new ActionError("error.respuestas.orden"));
        
        try {
        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
        	List lang = idiomaDelegate.listarLenguajes();
        	
        	for (int i=0;i<lang.size();i++) {
        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
        			TraduccionRespuesta  trad = (TraduccionRespuesta)((ArrayList)get("traducciones")).get(i);
        			if (trad.getTitulo().length()==0)
        				errors.add("titulo", new ActionError("error.respuestas.titulo"));
        		}
        	}
			
        } catch (Throwable t) {
            log.error("Error comprobando titulo de respuestas", t);
        }    
        

    }
    	return errors;

    }
        

    
}