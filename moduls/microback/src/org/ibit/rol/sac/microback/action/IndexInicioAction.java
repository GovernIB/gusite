package org.ibit.rol.sac.microback.action;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.microback.base.bean.Pardato;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionContenido;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action que guarda las �ltimas modificaciones del microsite en un atributo de petici�n  MVS_ultimsmodificats <P>
 * 
 * 	Definici�n Struts:<BR>
 *  action path="/index_inicio"<BR> 
 *  unknown="false" <BR>
 *  forward name="inicio" path="/index_inicio.jsp"
 *  
 *  @author - Indra
 */
public class IndexInicioAction extends Action  {


	protected static Log log = LogFactory.getLog(IndexInicioAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {

		/* ************************************************************ */
		/* ***************** prueba de indexer ************************ */
		/* ************************************************************ */
		//IndexerDelegate indexador = DelegateUtil.getIndexerDelegate();
		//indexador.reindexarMicrosites();

		/* ************************************************************ */
		/* *****************   fin  prueba de indexer ***************** */
		/* ************************************************************ */		
		
		
		request.setAttribute("MVS_ultimsmodificats", obtenerListaUltimasModificaciones(request));
		//request.setAttribute("MVS_einaversion", Microback.microsites_name + " v" + Microback.microsites_version + " build: " + Microback.microsites_build);
        return mapping.findForward("inicio");
    }
	
	
	private ArrayList<Pardato> obtenerListaUltimasModificaciones(HttpServletRequest request) {
		ArrayList<Pardato> listaretorno=new ArrayList<Pardato>();
		String idcontenido=null;
		try {
			ContenidoDelegate contedel = DelegateUtil.getContenidoDelegate();
			
			
			//aprobechamos para actualizar la variable de sesion MVS_microsite
			Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			Base.micrositeRefresh(microsite.getId(),request);
			StringTokenizer st=new StringTokenizer(microsite.getServiciosSeleccionados(),";");
    		int n=st.countTokens();
    		
    		for (int i=0;i<n;i++) {
    				idcontenido=st.nextToken();
    				if (contedel.existeContenido(new Long(idcontenido))) {
		    			Contenido conte = contedel.obtenerContenido(new Long(idcontenido));
		    			Pardato pardato = new Pardato(""+conte.getId(), ( (((TraduccionContenido)conte.getTraduce()).getTitulo()!=null)?((TraduccionContenido)conte.getTraduce()).getTitulo():"[sin titulo]" )  );
		    			listaretorno.add(pardato);
    				}
    		}
			
		
		} catch (Exception e) {
			log.warn("Contenido id=" + idcontenido + ": no se ha mostrado en el listado de ultimos contenidos modificados.");
		}
		
		return listaretorno;
	}
}
