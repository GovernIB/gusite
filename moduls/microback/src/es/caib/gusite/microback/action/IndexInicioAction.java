package es.caib.gusite.microback.action;

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

import es.caib.gusite.microback.base.Base;
import es.caib.gusite.microback.base.bean.Pardato;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.micropersistence.exception.UsuarioInexistenteException;

/**
 * Action que guarda las últimas modificaciones del microsite en un atributo de petición  MVS_ultimsmodificats <P>
 * 
 * 	Definición Struts:<BR>
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
		request.setAttribute("MVS_urlpublica", obtenerUrlPublica(request));
		//aprobechamos para actualizar la variable de sesion MVS_microsite
		
        return mapping.findForward("inicio");
    }
	
	
	private String obtenerUrlPublica(HttpServletRequest request) throws Exception {
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		
		if (microsite == null){
			UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
	     	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
	 	
	     	usudel.isUsuarioNulo(usu);
		}
		
		
		if (microsite.getRestringido().equals("5")) {
            String newContext = System.getProperty("es.caib.gusite.context.front");
            return newContext + "/" + microsite.getUri() + "/";
		} else {
			return "/sacmicrofront/index.do?lang=ca&mkey=" + microsite.getClaveunica();
			
		}
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
