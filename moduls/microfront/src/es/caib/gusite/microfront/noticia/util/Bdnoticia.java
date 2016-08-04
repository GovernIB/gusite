package es.caib.gusite.microfront.noticia.util;

import java.util.List;

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
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionTipo;

/**
 * Clase Bdnoticia. Manejador de la petición de una noticia.
 * 
 * Recupera la noticia para mostrarla en el front.
 * 
 * @author Indra
 *
 */
public class Bdnoticia extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdnoticia.class);
	
	private String desctiponoticia="";
	private HttpServletRequest req;
	private boolean error = false;
	private Noticia noticia = new Noticia();
	private DelegateBase _delegateBase;
	
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Método público que borra las variables
	 */
	public void dispose() {
		desctiponoticia = null;
		req = null;
		noticia = null;
		_delegateBase = null;
		super.dispose();
	}
	
	/**
	 * Contructor de la clase para recoger la noticia.
	 * @param request
	 * @throws Exception
	 */
	public Bdnoticia(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		_delegateBase = new DelegateBase(request);
		if ((microsite!=null) && (existeServicio(Microfront.RNOTICIA))) {
			recogernoticia();	
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
		return Microfront.RNOTICIA;
	}
	
	/**
	 * Método privado para recoger la noticia.
	 */
	private void recogernoticia(){
		String idnoticia = "" + req.getParameter(Microfront.PCONT);
		try {
			Long idcont = new Long(Long.parseLong(idnoticia));
			noticia = _delegateBase.obtenerNoticia(idcont, idioma);

			//comprobacion de microsite
			if (!(noticia.getIdmicrosite().longValue()==idsite.longValue())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}
			//comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}			

			//o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(), noticia.getFcaducidad())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El contenido solicitado está caducado.");
					error=true;
			}
			
			
			desctiponoticia = ((TraduccionTipo)noticia.getTipo().getTraduccion(idioma)).getNombre();
			//si llegamos aqui.... todo ok, así que grabamos la estadistica
			if (!error) {
				if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				 		else req.getSession().getServletContext().setAttribute("bufferStats", 
								StatManager.grabarestadistica(noticia, super.publico, 
										(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}
		
	}

	public boolean isError() {
		return error;
	}

	public Noticia getNoticia() {
		return noticia;
	}	
	
	public String getDesctiponoticia() {
		return desctiponoticia;
	}	
}
