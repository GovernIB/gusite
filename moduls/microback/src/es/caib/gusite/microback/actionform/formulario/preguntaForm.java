package es.caib.gusite.microback.actionform.formulario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.*;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las preguntas en encuestas
 *
 *@author Indra
 *
 */
public class preguntaForm extends TraDynaActionForm {

	private static final long serialVersionUID = -8529514312292367724L;
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

        if (get("imagen")!=null)
        	if (((FormFile)get("imagen")).getFileSize()>1024*1024*1)
        		errors.add("imagen", new ActionError("error.preguntas.imagen"));
        
        if (!esEntero(""+get("orden")) )
        	errors.add("orden", new ActionError("error.preguntas.orden"));
        
        if (!esEntero(""+get("maxContestadas")))
        	errors.add("maxContestadas", new ActionError("error.preguntas.numresp.max"));
        if (!esEntero(""+get("maxContestadas")))
        	errors.add("minContestadas", new ActionError("error.preguntas.numresp.min"));
        if (get("maxContestadas")!=null && get("maxContestadas") != null && (Integer)(get("maxContestadas"))<(Integer)get("minContestadas") && (Integer)(get("maxContestadas"))!=0)
        	errors.add("maxContestadas", new ActionError("error.preguntas.numresp.minmax"));
        
        try {
        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
        	List lang = idiomaDelegate.listarLenguajes();
        	
        	for (int i=0;i<lang.size();i++) {
        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
        			TraduccionPregunta  trad = (TraduccionPregunta)((ArrayList)get("traducciones")).get(i);
        			if (trad.getTitulo().length()==0)
        				errors.add("titulo", new ActionError("error.preguntas.titulo"));
        		}
        	}
			
        } catch (Throwable t) {
            log.error("Error comprobando titulo de preguntas", t);
        }
        
        
    }
    	return errors;

    }
        

    
}