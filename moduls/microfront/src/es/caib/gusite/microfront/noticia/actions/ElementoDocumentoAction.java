package es.caib.gusite.microfront.noticia.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RedirectingActionForward;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.noticia.util.Bdnoticia;
import es.caib.gusite.micromodel.TraduccionNoticia;

/**
 *  Action que consulta un documento de un elemento(noticia) <P>
 *  Definición Struts:<BR>
 *  action path="/elementodocumento" <BR> 
 *  unknown="false" <BR>
 *  
 *  @author - Indra
 */
public class ElementoDocumentoAction extends BaseAction  {
	
	protected static Log log = LogFactory.getLog(ElementoDocumentoAction.class);
	
	  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		  	//SSSSSxxxZIyyy
		  	String forwardlocal="archivopub.do?ctrl=";   
		  	try {
			    	 
			  	Bdnoticia bdnoticia = new Bdnoticia(request);
			    
			  	if (!bdnoticia.isError()) {
	
		  			request.setAttribute("MVS_noticia", bdnoticia.getNoticia());
				    request.setAttribute("MVS_tiponoticia", bdnoticia.getDesctiponoticia());
				    String iddocumento = "" + ((TraduccionNoticia)bdnoticia.getNoticia().getTraduccion(bdnoticia.getIdioma())).getDocu().getId().longValue();
				    forwardlocal+= Microfront.RMICROSITE + bdnoticia.getIdsite().longValue() + Microfront.separatordocs + iddocumento + "&id=" + iddocumento;
				    
			        ActionForward elforward = new RedirectingActionForward();
			        elforward.setPath(forwardlocal);
			        return elforward;
	
			    } else {
			    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_DOCUMENT));				    	
				}

		    }  catch (Exception e) {
		    	log.warn("[ElementoDocumentoAction] ha solicitado un documento con datos incosistentes.");
	        	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_DOCUMENT));
	        } 		  	  	

	  }
		
}
