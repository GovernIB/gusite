package org.ibit.rol.sac.microfront.home.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.ibit.rol.sac.microfront.home.util.Bdhome;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Action IntranetLoginAction <P>
 *  Definición Struts:<BR>
 *  action path="/intranetlogin" <BR> 
 *  unknown="true" <BR>
 *  forward name="intranetlogin" path=/v4/home/intranetlogin.jsp 
 * @author Indra
 *
 */
public class IntranetLoginAction extends Action{

	protected static Log log = LogFactory.getLog(IntranetLoginAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
		long time1=System.currentTimeMillis();
	  	String forwardlocal="intranetlogin";
	    
	    //averiguar información de la home
	    Bdhome bdhome = new Bdhome(request);
	    
    	long time2=System.currentTimeMillis();
    	log.info("IntranetLoginAction " + (time2-time1));
	    
	    return mapping.findForward(forwardlocal);
	}
}