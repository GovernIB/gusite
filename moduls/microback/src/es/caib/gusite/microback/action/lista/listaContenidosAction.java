package es.caib.gusite.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.contenidoForm;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;


/**
 * Action que valida y trata el listado de contenidos de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/contenidosAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleConte" path="/detalleContenido.jsp"<BR> 
 *  forward name="info" path="/infoContenido.jsp" 
 *  
 *  @author Indra
 */
public class listaContenidosAction extends BaseAction {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */

	protected static Log log = LogFactory.getLog(listaContenidosAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

    	ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
    	
    	//Metemos en el request el CSS que utilizará el tinymce
    	Microsite micro = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	request.setAttribute("MVS_css_tiny",tagCSS(micro.getEstiloCSS()));    	
    	
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("contenidoForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	contenidoForm fdet=(contenidoForm) request.getSession().getAttribute("contenidoForm");

        	request.setAttribute("contenidoForm", fdet);
            request.setAttribute("listaDocs", bdConte.listarDocumentos(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().toString(), ""+fdet.get("id")));
            request.setAttribute("menu", ""+fdet.get("idMenu") );
            request.setAttribute("migapan", bdConte.migapan("",null) );
            request.setAttribute("validacion", "si");
            return mapping.findForward("detalleConte");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( (""+request.getParameter("op")).equals("crear")) {
        	
        	String idmenu="";
        	if (request.getParameter("idmenu")!=null) { // alta desde el arbol de menu
        		idmenu=""+request.getParameter("idmenu");
        	   	request.getSession().removeAttribute("contenidoForm");
        	   	request.setAttribute("menu", idmenu );
        	   	request.setAttribute("migapan", bdConte.migapan(idmenu,null) );
            	return mapping.findForward("detalleConte");
        	}

        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( (""+request.getParameter("op")).equals("borrar")) {
            Long id =null;
           
            if (request.getParameter("id")!=null) {
            	id=new Long(""+request.getParameter("id"));
            	
            	if (bdConte.checkSite(id, ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId())) {
            		addMessage(request, "info.seguridad");
            		return mapping.findForward("info");
            	}

            	Contenido con=bdConte.obtenerContenido(id);
            	bdConte.borrarContenido(id);

            	 // debemos reordenar todo el arbol de menú
               	MenuDelegate bdMenu = DelegateUtil.getMenuDelegate();
           		bdMenu.Reordena(con.getOrden(), 'b', ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
          		
           		addMessage(request, "mensa.listacontborradas");
                return mapping.findForward("info");

            }
            
            
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

	//MCR v1.1
	public String tagCSS(Archivo archivocss) {
		String retorno="";
		if (archivocss!=null)
			retorno="archivo.do?id=" + archivocss.getId().longValue();
		else
			retorno="/sacmicrofront/css/dummy_estilos01_blau.css";
		return retorno;
	}	
	
	
	
}