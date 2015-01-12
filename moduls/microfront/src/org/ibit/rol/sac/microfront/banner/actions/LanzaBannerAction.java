package org.ibit.rol.sac.microfront.banner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microfront.banner.util.Bdlanzabanner;


/**
 *  Action que consulta una agenda <P>
 *  Definición Struts:<BR>
 *  unknown="false" <BR>
 *  action path="/lanzabanner" <BR> 
 *  
 *  @author - Indra
 */
public class LanzaBannerAction extends Action  {
	
	protected static Log log = LogFactory.getLog(LanzaBannerAction.class);

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		Bdlanzabanner bdlanzabanner = new Bdlanzabanner(request);
		response.sendRedirect(bdlanzabanner.getURL());
		return null;
	}
	
}