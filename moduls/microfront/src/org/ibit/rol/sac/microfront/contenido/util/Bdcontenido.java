package org.ibit.rol.sac.microfront.contenido.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.DelegateBase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.estadistica.util.StatManager;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micromodel.Menu;
import org.ibit.rol.sac.micromodel.TraduccionMenu;
import org.ibit.rol.sac.microfront.util.Fechas;
import org.ibit.rol.sac.microfront.util.microtag.MicrositeParser;
import org.ibit.rol.sac.micromodel.TraduccionContenido;

/**
 *  Clase Bdcontenido.  Manejador de la petición de un contenido. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdcontenido extends Bdbase  {

	protected static Log log = LogFactory.getLog(Bdcontenido.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private Contenido contenidolocal = new Contenido();
	private String titulomollapa="";
	private String urlredireccionada;
    private String tipobeta="";
	private DelegateBase _delegateBase;

	/**
	 * @override
	 */
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Método público que borra las variables
	 */
	public void dispose() {
		contenidolocal = null;
		titulomollapa = null;
		urlredireccionada = null;
		tipobeta = null;
		req = null;
		_delegateBase = null;
		super.dispose();
	}
	
	/**
	 * Constructor de la clase, carga el contenido a partir del contenido pedido (idcontenido)
	 * @param request
	 * @throws Exception
	 */
	public Bdcontenido(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		_delegateBase = new DelegateBase(request);
		if ((microsite!=null) && (existeServicio(Microfront.RCONTENIDO))) {
			tipobeta = "" + req.getParameter("tipo");
			recogercontenido();	
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}		
		
	}

	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de contenidos.
	 */
	public String setServicio() {
		return Microfront.RCONTENIDO;
	}	
	
	/**
	 * Método privado que recoge el contenido a partir del id del contenido.
	 */
	private void recogercontenido(){
		String idcontenido = "" + req.getParameter("cont");
	    
		try {
			
			Long idcont = new Long(Long.parseLong(idcontenido));
			contenidolocal = _delegateBase.obtenerContenido(idcont, idioma, microsite);
			urlredireccionada = ((TraduccionContenido)contenidolocal.getTraduccion(idioma)).getUrl();
	    	
			//obtenemos el menu padre 
			Menu menu = _delegateBase.obtenerMenuBranch(contenidolocal.getIdmenu(), idioma);
	    	
			titulomollapa = (menu.getVisible().equals("S"))?((TraduccionMenu)menu.getTraduccion(idioma)).getNombre():"";
				
			//comprobacion de menu en el microsite
			if (!(menu.getIdmicrosite().longValue()==idsite.longValue())) {
					log.error("[error logico] idsite.longValue=" + idsite.longValue() + ", menu.getIdmicrosite.longValue=" + menu.getIdmicrosite().longValue());
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El contenido solicitado no pertenece al site.");
					error=true;
			}
			
			//o bien comprobacion de que no esté vacio
			if (contenidolocal.getTraduccion(idioma)==null) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El contenido solicitado no contiene información.");
					error=true;
			}
			
			//o bien comprobacion de que esté vigente
			if (!Fechas.vigente(contenidolocal.getFpublicacion(), contenidolocal.getFcaducidad())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El contenido solicitado está caducado.");
					error=true;
			}			
			
			//o bien comprobacion de que no esté vacio
			if (!contenidolocal.getVisible().equals("S")) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El contenido solicitado no está disponible al público");
					error=true;
			}			
			
			//si llegamos aqui.... todo ok, así que grabamos la estadistica
			if (!error) {
				if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
					else req.getSession().getServletContext().setAttribute("bufferStats", 
							StatManager.grabarestadistica(contenidolocal,idsite, super.publico, 
									(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("", "S' ha produït un error al recuperar la pàgina.");
			error=true;
		}
		
	}
	
	private void reemplazarTags() {
		
		if (contenidolocal.getTraduccion(idioma)!=null) {
			if (((TraduccionContenido)contenidolocal.getTraduccion(idioma)).getTexto()!=null) {
				int noticias=3;
				if (microsite.getNumeronoticias()!=0) noticias=microsite.getNumeronoticias();	
				if (tipobeta.equals("beta")) {
					MicrositeParser microparser = new MicrositeParser(microsite.getRestringido(),((TraduccionContenido)contenidolocal.getTraduccion(idioma)).getTxbeta(), idsite, idioma, noticias);
					microparser.doParser(idioma);
					((TraduccionContenido)contenidolocal.getTraduccion(idioma)).setTxbeta(microparser.getHtmlParsed().toString());
				} else {
					MicrositeParser microparser = new MicrositeParser(microsite.getRestringido(),((TraduccionContenido)contenidolocal.getTraduccion(idioma)).getTexto(), idsite, idioma, noticias);
					microparser.doParser(idioma);
					((TraduccionContenido)contenidolocal.getTraduccion(idioma)).setTexto(microparser.getHtmlParsed().toString());
				}
			}
		}
	}
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Contenido getContenidolocal() {
		return contenidolocal;
	}

	public void setContenidolocal(Contenido contenidolocal) {
		this.contenidolocal = contenidolocal;
	}

	public String getTitulomollapa() {
		return titulomollapa;
	}

	public String getUrlredireccionada() {
		return urlredireccionada;
	}

	public void setUrlredireccionada(String urlredireccionada) {
		this.urlredireccionada = urlredireccionada;
	}	

}
