package org.ibit.rol.sac.microfront.home.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.util.microtag.MParserAgenda;
import org.ibit.rol.sac.microfront.util.microtag.MParserBanner;
import org.ibit.rol.sac.microfront.util.microtag.MParserElemento;
import org.ibit.rol.sac.microfront.util.microtag.MParserHTML;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micropersistence.delegate.BannerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


/**
 *  Clase Bdhome. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdhome extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdhome.class);
	
	private String tipohome = ""; //1 y 2=plantillas, 3=imagen, 4=url,  5=despliege servicios,   -1=error
	private boolean error = false;
	private ArrayList<String> listabanners = new ArrayList<String>();
	private ArrayList<Object> listatotalpl5 = new ArrayList<Object>();
	private String tagHtmlCampanya=""; 
	private String tagHtmlAgendaCalendario="-1"; //un valor por defecto para indicar que no tiene 
	private String tagHtmlAgendaListado="-1"; //un valor por defecto para indicar que no tiene
	private String tagHtmlNoticias="-1"; //un valor por defecto para indicar que no tiene 
		
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables
	 */
	public void dispose() {
		tipohome = null;
		listabanners = null;
		listatotalpl5 = null;
		tagHtmlCampanya = null;
		tagHtmlAgendaCalendario = null;
		tagHtmlAgendaListado = null;
		tagHtmlNoticias = null;
		super.dispose();
	}
	
	/**
	 * Constructor de la clase, carga la home a partir de la request
	 * @param request
	 * @throws Exception
	 */
	public Bdhome(HttpServletRequest request) throws Exception {	
		super(request);
		boolean error = super.getErrorBase();
		crearhome();	
	}

	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que no estamos en ningun servicio.
	 */
	public String setServicio() {
		return Microfront.RMICROSITE;
	}
	
	/**
	 * Metodo privado para crear la home
	 * @throws DelegateException
	 */
	private void crearhome() throws DelegateException {
		try {
			tipohome = microsite.getPlantilla();
			if (tipohome.equals("1")) montarplantilla12();
			if (tipohome.equals("2")) montarplantilla12();
			if (tipohome.equals("3")) montarplantilla12();
			if (tipohome.equals("4")) montarplantilla4();
			if (tipohome.equals("5")) montarplantilla12();
		
		} catch (Exception e) {
			log.error(e.getMessage());
			tipohome="-1"; //error
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}	
	}

	/**
	 * Método privado para montar la plantilla12: campaña, agenda, noticias, banners
	 * @throws DelegateException
	 */
	private void montarplantilla12() throws DelegateException {
		prepararcampanya();
		prepararAgenda();
		prepararNoticias();
		prepararbanners(2);
	}
	
	/**
	 * Método privado para montar plantilla4: preparar campaña
	 * @throws DelegateException
	 */
	private void montarplantilla4() throws DelegateException {
		prepararcampanya();
	}

	
	/**
	 * Método privado para preparar la Agenda, en el caso que ese microsite tenga asignado ese componente 
	 */
	private void prepararAgenda() {
		if (existeServicio(Microfront.RAGENDA)) {
			MParserAgenda parseagenda = new MParserAgenda(microsite.getRestringido());
			tagHtmlAgendaCalendario=parseagenda.getHtmlAgendaCalendario(idsite,idioma,3).toString();
			tagHtmlAgendaListado=parseagenda.getHtmlAgendaListado(idsite,idioma,3).toString();
		}
	}
	
	/**
	 * Método privado para preparar la Noticia, en el caso que ese microsite tenga asignado ese componente 
	 */
	private void prepararNoticias() {
		if (existeServicio(Microfront.RNOTICIA)) {
			int noticias=3;
			if (microsite.getNumeronoticias()!=0)
				noticias=microsite.getNumeronoticias();
			MParserElemento parseelemento = new MParserElemento(microsite.getRestringido());
			tagHtmlNoticias=parseelemento.getHtmlNoticias(idsite,idioma, noticias).toString();
		}
	}
	
	/**
	 * Método privado para preparar el Banner, en el caso que ese microsite tenga asignado ese componente 
	 */
	private void prepararbanners(int numerobanners) {
		if (existeServicio(Microfront.RBANNER)) {
			//obtener el listado y sacar los que nos interesen
			try {
				Hashtable<Long, String> hashbanners = new Hashtable<Long, String>();
				MParserBanner parsebanner = new MParserBanner(microsite.getRestringido());
				
				BannerDelegate bannerdel = DelegateUtil.getBannerDelegate();
				Banner banner = new Banner();
				bannerdel.init();bannerdel.setTampagina(Integer.MAX_VALUE);
				String where="where index(trad)='" + idioma + "' and banner.visible='S' and banner.idmicrosite=" + idsite;
				where+=" and (banner.fpublicacion is null OR banner.fpublicacion<=SYSDATE) and (banner.fcaducidad is null OR banner.fcaducidad>=SYSDATE)";
				bannerdel.setWhere(where);
				List<?> listabannerslocal;
				listabannerslocal=bannerdel.listarBanners();
				
				if (listabannerslocal.size()<numerobanners)
					numerobanners=listabannerslocal.size();
				
				while (hashbanners.size()<numerobanners) {
					int numerobanner = getNumeroaleatorio(listabannerslocal.size());
					Iterator<?> iter = listabannerslocal.iterator();
					for (int i=1; i<numerobanner; i++) iter.next();
					banner = new Banner();
					banner = (Banner)iter.next();
					String htmlbanner=parsebanner.getHtmlBanner(idsite,idioma,banner).toString();
					hashbanners.put(banner.getId(),htmlbanner);
				}

				Enumeration<Long> enumera = hashbanners.keys();
		    	while (enumera.hasMoreElements()) {
		    		Long key = (Long)enumera.nextElement();
		    		String txt= (String)hashbanners.get(key);
		    		listabanners.add(txt);
		    	}
		    	
			} catch(Exception e) {
				log.error("[getHtmlBanner]: " + e.getMessage());
			}
			
		}
	}
	
	/**
	 * Método privado para preparar la campanya. 
	 */
	private void prepararcampanya() {
		MParserHTML parsehtml = new MParserHTML(microsite.getRestringido());
		tagHtmlCampanya=parsehtml.getHtmlCampanya(microsite,idioma).toString();
	}

	
	/**
	 * Método que devuelve un numero aleatorio.
	 * Utiliza el Microfront.contadorbanner que es un Long genérico a la aplicacion.
	 * Es una forma de garantizar que si se piden dos banners a la vez no se da el mismo.
	 * 
	 * @param maximo
	 * @return int
	 */
	private static int getNumeroaleatorio(int maximo) {
		Random rand = new Random();
		Microfront.contadorbanner = new Long(Microfront.contadorbanner.longValue() + 1);
		
		Long operando1=new Long(Microfront.contadorbanner.longValue()+(long)rand.nextInt(maximo));
		
		Long operando2=new Long(maximo);
		int resto = operando1.intValue() % operando2.intValue();
		
		int retorno=0;
		retorno = resto+1;
		return retorno;
	}		
	
	
	//********* geters  **********************
	public String getTipohome() {
		return tipohome;
	}

	public boolean isError() {
		return error;
	}	

	public void setError(boolean error) {
		this.error = error;
	}	
	
	public ArrayList<String> getListabanners() {
		return listabanners;
	}

	public String getUrlhome() {
		String retorno;
		retorno = microsite.getUrlhome() + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}

	public String getTagHtmlCampanya() {
		return tagHtmlCampanya;
	}

	public String getTagHtmlAgendaCalendario() {
		return tagHtmlAgendaCalendario;
	}

	public String getTagHtmlNoticias() {
		return tagHtmlNoticias;
	}

	public String getTagHtmlAgendaListado() {
		return tagHtmlAgendaListado;
	}

	public ArrayList<Object> getListatotalpl5() {
		return listatotalpl5;
	}

}
