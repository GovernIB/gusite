package es.caib.gusite.microfront.home.actions;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RedirectingActionForward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

import es.caib.gusite.microfront.home.actions.IntranetAction;
import es.caib.gusite.microfront.home.util.Bdhome;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Action Intranet <P>
 *  Definición Struts:<BR>
 *  action path="/intranet/home" <BR> 
 *  unknown="true" <BR>
 *  forward name="intranet" path=/v4/home/intranet.jsp
 * @author Indra
 *
 */
public class IntranetAction extends Action{
	
	protected static Log log = LogFactory.getLog(IntranetAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
	  
	  	long time1=System.currentTimeMillis();
	    //averiguar información de la home
	    Bdhome bdhome = new Bdhome(request);
	    
	    //redirigir a la home buena
        ActionForward elforward = new RedirectingActionForward();
        if (request.getSession().getAttribute("redirect") != null ) {
        	String redirect = (String) request.getSession().getAttribute("redirect");
        	request.getSession().removeAttribute("redirect");
            elforward.setPath(redirect);
        } else {
            elforward.setPath("/home.do?idsite=" + bdhome.getMicrosite().getId() + "&lang=" + bdhome.getIdioma());
        }


    	long time2=System.currentTimeMillis();
    	log.info("IntranetAction " + (time2-time1));

        return elforward;
	}
}