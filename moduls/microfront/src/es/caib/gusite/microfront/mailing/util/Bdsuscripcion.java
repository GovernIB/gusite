package es.caib.gusite.microfront.mailing.util;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.microfront.base.DelegateBase;
import es.caib.gusite.microfront.estadistica.util.StatManager;
import es.caib.gusite.microfront.util.Fechas;
import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;

/**
 * Clase Bdnoticia. Manejador de la petición de una noticia.
 * 
 * Recupera la noticia para mostrarla en el front.
 * 
 * @author Indra
 *
 */
public class Bdsuscripcion extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdsuscripcion.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private List listaDistrib = null;
	private DelegateBase _delegateBase;
	private ResourceBundle rb;
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Método público que borra las variables
	 */
	public void dispose() {
		req = null;
		_delegateBase = null;
		super.dispose();
	}
	
	/**
	 * Contructor de la clase para recoger la noticia.
	 * @param request
	 * @throws Exception
	 */
	public Bdsuscripcion(HttpServletRequest request) throws Exception {
		super(request);
		req = request;		
		rb = ResourceBundle.getBundle("ApplicationResources_front", request.getLocale());
		_delegateBase = new DelegateBase(request);
		
		if ((microsite!=null) && (existeServicio(Microfront.RLDISTRIB))) {
			recogerlista();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}
	}

	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de noticias.
	 */
	public String setServicio() {
		return Microfront.RLDISTRIB;
	}
	
	/**
	 * Método privado para recoger la noticia.
	 */
	private void recogerlista(){
		try {			
			listaDistrib = _delegateBase.obtenerListadoDistribucionMicrosite(idsite);
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}
		
	}
	
	public void alta(){
		LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
		try {
			if (req.getParameter(Microfront.PEMAIL) != null &&
					((String)req.getParameter(Microfront.PEMAIL)).matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$") &&
					req.getParameter(Microfront.PDISTRIB) != null){
				Long idLista = Long.parseLong(req.getParameter(Microfront.PDISTRIB));
				if(DelegateUtil.getLlistaDistribucionDelegate().obtenerListaDistribucion(idLista).getPublico()){
					String email = req.getParameter(Microfront.PEMAIL);
					distribDel.anadeCorreo(idLista, email);
					if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
					else req.getSession().getServletContext().setAttribute("bufferStats", 
							StatManager.grabarestadistica(distribDel.obtenerListaDistribucion(idLista), super.publico, 
									(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
				}else
					throw new Exception("Faltan parámetros");
			}else 
				throw new Exception("Faltan parámetros");
		} catch (Exception e) {
			log.error(e.getMessage());
			error = true;
		}		
	}
	
	public void baixa(){
		try {
			if(req.getParameter(Microfront.PEMAIL) != null){
				for(Object l:listaDistrib){
					LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
					distribDel.borrarCorreo(((ListaDistribucion)l).getId(), req.getParameter(Microfront.PEMAIL));
					if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
					else req.getSession().getServletContext().setAttribute(
							"bufferStats", 
							StatManager.grabarestadistica((ListaDistribucion)l, 
							super.publico, 
							(List<Estadistica>)req.getSession().getServletContext().getAttribute("bufferStats")));
				}
				req.setAttribute("msg", rb.getString("mailing.baixa"));
			}else 
				throw new Exception("Faltan parámetros");
		} catch (Exception e) {
			log.error(e.getMessage());
			error = true;
		}
	}

	public boolean isError() {
		return error;
	}

	public List<?> getListaLDistribucion() {
		return listaDistrib;
	}	
	
}
