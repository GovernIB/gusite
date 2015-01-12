package es.caib.gusite.microback.action.buscaordena;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaNoticiaActionForm;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * Action que prepara el listado de noticias (elementos de listado)  <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/noticias" <BR> 
 *  name="BuscaOrdenaNoticiaActionForm" <BR> 
 *  forward name="listarNotis" path="/listaNoticias.jsp"<BR>
 *  forward name="listarTipos" path="/tipos.do"
 *  
 *  @author Indra
 */
public class buscaordenaNoticiasAction extends BaseAction {

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

    protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.buscaordenaNoticiasAction.class);
     
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	String claseElemento = ""+request.getParameter("clase");
    	
    	if (claseElemento.equals("3")) {
    		request.getSession().setAttribute("connexioExterna", "si");
    		request.getSession().setAttribute("mntnmnt", "yes");
    		return mapping.findForward("listarTipos");
    		
    	} else {
    	
    	Microsite mic = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	if (mic==null) {
    		mic = new Microsite();
    		mic.setId(new Long(1));
    		request.getSession().setAttribute("MVS_microsite",mic);
    	}
    	
    	NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
    	bdNoticia.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

    	preparaTipoElemento(request);
    	
        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaNoticiaActionForm f = (BuscaOrdenaNoticiaActionForm) form;

        bdNoticia.setWhere(preparaWhereTipoElem(request, bdNoticia.getWhere()));
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdNoticia.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdNoticia.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdNoticia.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdNoticia.setPagina(1);
            
        List<?> lista=bdNoticia.listarNoticias();
        request.setAttribute("parametros_pagina",bdNoticia.getParametros());
        
        if(request.getSession().getAttribute("mensajeBorrarNoticia")!=null){
            
    		request.getSession().setAttribute("mensajeBorrarNoticia", null);
    		addMessageAlert(request, "mensa.listanotborradas.sinids");
        }
        
        if(request.getSession().getAttribute("mensajeClonarNoticia")!=null){
            
    		request.getSession().setAttribute("mensajeClonarNoticia", null);
    		addMessageAlert(request, "mensa.clonanoticia");
        }
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else  {
        	// Si no hay registros limpiamos el filtro
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessageAlert(request, "noticia.elements.cerca.noreg");
        		bdNoticia.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
            	preparaTipoElemento(request);
                bdNoticia.setWhere(preparaWhereTipoElem(request, bdNoticia.getWhere()));
        		lista = bdNoticia.listarNoticias();
        		request.setAttribute("parametros_pagina",bdNoticia.getParametros());
        		request.setAttribute("listado",lista);
        	} else addMessageAlert(request, "noticia.vacio");
            f.setFiltro("");
        }
        return mapping.findForward("listarNotis");
        
    	}
    }

    /**
     * Método que devuelve un string con la sentencia where modificada para sacar sólo las páginas por tipo de elemento
     * @param request
     * @param whereOld
     * @return String
     */
    private String preparaWhereTipoElem(HttpServletRequest request, String whereOld) {
    	String nuevowhere=whereOld;
    	
    	
    	String idtipo = "" + request.getParameter("tipo");
    	try {
    		
    		if (idtipo.equals("null")) {		//lo intentamos recuperar de session
    			idtipo = "" + request.getSession().getAttribute("MVS_idtipo");
	    	}
    		
	    	if (!idtipo.equals("null")) {
	    		
	    		request.getSession().setAttribute("MVS_idtipo", idtipo);
	    		
	    		String filtro = " noti.tipo=" + idtipo;
	            if (nuevowhere.length()>0) 	nuevowhere=nuevowhere+" AND ("+filtro+")";
	            else		            	nuevowhere=" where "+filtro;
		    }
    	} catch (Exception ex) {
    		log.warn("No se ha pasado el id del tipo de noticia.");
    	}
    	
    	return nuevowhere;
    }

    /**
     * Método que mete en el request el bean "Tipo" de elemento y los "componentes" relacionados con dicho tipo
     * @param request
     */
    private void preparaTipoElemento(HttpServletRequest request) {
    	
		String idtipo = "" + request.getParameter("tipo");
		
		try {

			if (idtipo.equals("null")) { // lo intentamos recuperar de session
				idtipo = "" + request.getSession().getAttribute("MVS_idtipo");
			}

			TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
			Tipo tipo = tipodel.obtenerTipo(new Long(idtipo));
			request.setAttribute("MVS_tipo_elemento", tipo);

			ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate();
			compodel.init();
			compodel.setPagina(1);
			compodel.setTampagina(Microback.MAX_INTEGER);
			compodel.setWhere("where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and compo.tipo.id=" + tipo.getId());
			
			if (compodel.listarComponentes().size() > 0)
				request.setAttribute("MVS_lista_componentes", compodel.listarComponentes());

		} catch (Exception ex) {
			
			log.warn("[preparaTipoElemento] Tipo elemento desconocido.");
			
		}
    	
    	
    }    
    
    
}

