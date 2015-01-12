package es.caib.gusite.microfront.contacto.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.base.bean.Pardato;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.microfront.contacto.actionforms.BuscaOrdenaContactosActionForm;
import es.caib.gusite.microfront.util.microtag.MicroURI;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Clsae Bdlistacontactos. Manejador de la petición de la lista de contactos. 
 * Recoge los datos para mostrarlos en el front.
 * @author Indra
 *
 */
public class Bdlistacontactos extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdlistacontactos.class);
	
	private String url_sinpagina="";
	private List<?> listacontactos;
	private ArrayList<Pardato> listanombrecontactos = new ArrayList<Pardato>();
	private BuscaOrdenaContactosActionForm formulario = new BuscaOrdenaContactosActionForm();
	private HttpServletRequest req;
	private Hashtable<?, ?> parametros = new Hashtable<Object, Object>();
	private boolean error = false;
	private String URLcontacto=""; //en caso de que solo haya un contacto

	/**
	 * Constructor de la clase, carga la lista de contactos a partir de la request
	 * @param request
	 * @param form BuscaOrdenaContactosActionForm
	 * @throws Exception
	 */
	public Bdlistacontactos(HttpServletRequest request, ActionForm form) throws Exception {
		super(request);
		formulario = (BuscaOrdenaContactosActionForm) form;
		req = request;
		if ((microsite!=null) && (existeServicio(Microfront.RCONTACTO))) {
			crearlistado();
			preparaseulet();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}
	}

	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 */
	public String setServicio() {
		return Microfront.RCONTACTO;
	}
	
	/**
	 * Método privado que se encarga de guardar en una List la lista de contactos.
	 */
	private void crearlistado() {
		try {
		    	ContactoDelegate contactodel = DelegateUtil.getContactoDelegate();
		    	contactodel.init();
		    	contactodel.setWhere("where contacto.visible='S' and contacto.idmicrosite=" + super.idsite);
		    	
		        if (formulario.getFiltro()!= null && formulario.getFiltro().length()>0)
		        	contactodel.setFiltro(formulario.getFiltro());
		    
		        if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0)
		        	contactodel.setOrderby(formulario.getOrdenacion());
		
		        // Indicamos la página a visualizar
		        if (req.getParameter("pagina")!=null)
		        	contactodel.setPagina(Integer.parseInt(req.getParameter("pagina")));
		        else
		        	contactodel.setPagina(1);
		        
		        listacontactos=contactodel.listarContactos();
		        volcarlistadonombrecontactos();
		        parametros=(Hashtable<?, ?>) contactodel.getParametros();
		        
		        //Si hay algún registro limpiamos el filtro
		        if (listacontactos.size()==0) formulario.setFiltro("");
		        
				if (idsite.longValue()==0) error=true;
		        
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de contactos.");
			error=true;
			listacontactos = null;
			parametros = new Hashtable<Object, Object>();
		}
	}
	
	/**
	 * Método privado encargado de guardar en un ArrayList un pardato (titulocontacto, urlcontacto)
	 */
	private void volcarlistadonombrecontactos(){
    	Iterator<?> iter = listacontactos.iterator();
	    Contacto contacto;
        while (iter.hasNext()) {
        	 contacto = (Contacto)iter.next();
        	 Pardato pardato = new Pardato();
        	 pardato.setKey(contacto.getTitulocontacto(idioma.toLowerCase()));
        	 URLcontacto=MicroURI.uriContacto(idsite, contacto.getId(), idioma);
        	 pardato.setValue(URLcontacto);
        	 listanombrecontactos.add(pardato);
        }
	}
	
	/**
	 * método que quita el parametro 'pagina' de la lista de parametros del servlet
	 */
	private void preparaseulet() {
		url_sinpagina = url;
		int pospagina = url.indexOf(Microfront.PPAGINA);
		if (pospagina!=-1) {
			url_sinpagina = url.substring(0,pospagina);
		}
	}
	
	public List<?> getListacontactos() {
		return listacontactos;
	}

	public void setListacontactos(List<?> listacontactos) {
		this.listacontactos = listacontactos;
	}

	public Hashtable<?, ?> getParametros() {
		return parametros;
	}

	public void setParametros(Hashtable<Object, Object> parametros) {
		this.parametros = parametros;
	}

	public boolean isError() {
		return error;
	}

	public String getURLcontacto() {
		return URLcontacto;
	}	

	public ArrayList<Pardato> getListanombrecontactos() {
		return listanombrecontactos;
	}	
	
	public String getUrl_sinpagina() {
		return url_sinpagina;
	}

}
