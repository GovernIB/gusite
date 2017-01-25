package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;

/**
 * Action que devuelve un fichero con la info del clob <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/indexarPrincipal"<BR> 
 *  unknown="false" <BR>
 *  forward name="indexarPrincipal" path="/indexarPrincipal.jsp"
 *  
 *  @author - Indra
 */
public class ArchivoInfoAction extends BaseAction {

	
	protected static Log log = LogFactory.getLog(IndexarPrincipalAction.class);
	
		
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
	
    	SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();
    	String idSolrJob= request.getParameter("id");
    	SolrPendienteJob solrPendJob = solrPendienteDel.obtenerSolrPendienteJob(new Long(idSolrJob));
    	response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"archivo.txt\"");
				
		response.getOutputStream().write(procesarSaltosLinea(solrPendJob.getInfo()).getBytes());

        return null;

	}

	/**
	 * Sustituye los br por saltos de línea
	 * @param info
	 * @return
	 */
	private String procesarSaltosLinea(String info) {
		 while (info.indexOf("<br />") != -1) {
			 info = info.replace("<br />","\n");
		 }
		 while (info.indexOf("<br/>") != -1) {
			 info = info.replace("<br/>","\n");
		 }
		 while (info.indexOf("<br>") != -1) {
			 info = info.replace("<br>","\n");
		 }
		 while (info.indexOf("<br >") != -1) {
			 info = info.replace("<br >","\n");
		 }
		
		 return info;
	}	
	


}
