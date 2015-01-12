package org.ibit.rol.sac.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate;

/**
 * Action que busca en el índice Lucene<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/busca"<BR> 
 *  unknown="false" <BR>
 *  forward name="resultados" path="/buscar.jsp"
 *  
 *  @author - Indra
 */
public class BuscaAction extends BaseAction  {


	protected static Log log = LogFactory.getLog(BuscaAction.class);
	
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {

		IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
        String words=""+request.getParameter("words");
        String micro=""+request.getParameter("micro");
        String idi=""+request.getParameter("idi");
        
        
        request.setAttribute("listado", indexo.buscar(micro, idi, null, words, true));
        
        //request.setAttribute("diccionario", indexo.diccionario(idi));
        
        request.setAttribute("micro", micro);
        request.setAttribute("idi", idi);

        
       	return mapping.findForward("resultados");

		
    }
	
}