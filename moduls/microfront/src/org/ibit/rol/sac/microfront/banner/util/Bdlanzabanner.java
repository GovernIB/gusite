package org.ibit.rol.sac.microfront.banner.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.estadistica.util.StatManager;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micromodel.TraduccionBanner;
import org.ibit.rol.sac.micropersistence.delegate.BannerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Clase Bdlanzabanner. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdlanzabanner {

	protected static Log log = LogFactory.getLog(Bdlanzabanner.class);
	
	private HttpServletRequest req;
	private String URL="";
	private Banner banner = new Banner();
	private int publico=1;
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Método público que borra las variables del banner
	 */
	public void dispose() {
		URL = null;
		banner = null;
		req = null;
	}
	
	/**
	 * Constructor de  la clase. Carga el banner a traves de la request
	 * @param request
	 */
	public Bdlanzabanner(HttpServletRequest request) {
		req=request;
		
		String headAutorizacion = "" + request.getHeader("Authorization");
		//para saber si es intranet o internet
		if (!headAutorizacion.equals("null")) publico = 2;
		 	else publico=1;		
		
		preparaUrl();
	}

	/**
	 * Método privado para preparar la url
	 */
	private void preparaUrl() {
		try {
			String idioma = (String)req.getSession().getAttribute("MVS_idioma");
			BannerDelegate bannerdel = DelegateUtil.getBannerDelegate();
			banner = new Banner();
			banner = bannerdel.obtenerBanner(new Long(req.getParameter("idbanner")));
			if (banner!=null) {
				if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				else req.getSession().getServletContext().setAttribute("bufferStats", 
						StatManager.grabarestadistica(banner, publico, 
								(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));

				URL = ((TraduccionBanner)banner.getTraduccion(idioma.toLowerCase())).getUrl();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Método público que devuelve la url
	 * @return String una URL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Método público que devuelve un Banner
	 * @return Banner  un Banner
	 */
	public Banner getBanner() {
		return banner;
	}
	
}
