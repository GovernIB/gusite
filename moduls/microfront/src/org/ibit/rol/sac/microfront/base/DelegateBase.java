package org.ibit.rol.sac.microfront.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.base.bean.MenuFront;
import org.ibit.rol.sac.microfront.util.Cadenas;
import org.ibit.rol.sac.microfront.util.Fechas;
import org.ibit.rol.sac.microfront.util.microtag.MicrositeParser;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Menu;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.Tiposervicio;
import org.ibit.rol.sac.micromodel.TraduccionContenido;
import org.ibit.rol.sac.micromodel.Traducible;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.LDistribucionDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MenuDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TiposervicioDelegate;

import es.caib.sac.indra.moduls.Modulos;
import es.caib.sac.unitatOrganica.model.UOModel;


/**
 * Manejador de obtener objetos listos para ser visualizados en la web.
 * Utiliza ObjectCache para ir cacheando todos los objetos que se van demandando.
 * 
 * @author Indra
 *
 */
public class DelegateBase {

	protected static Log _log = LogFactory.getLog(DelegateBase.class);
	protected HttpServletRequest _request = null;
	
	public DelegateBase(HttpServletRequest request) throws Exception {
		_request = request;
	}
	

	/**
	 * Método que devuelve un microsite a partir de su clave unica.
	 * @param key String con el mkey del microsite
	 * @param idioma String con el idioma
	 * @return Microsites
	 * @throws Exception
	 */
	public Microsite obtenerMicrositebyKey(String key, String idioma) throws Exception  {
		MicrositeDelegate microdel = DelegateUtil.getMicrositeDelegate();
		try {
		    	
		    Microsite micro = microdel.obtenerMicrositebyKey(key);
		    micro.setIdi(idioma); 		
			return micro;	
			
		} catch (Exception e) {
			throw new Exception(" [obtenerMicrositebyKey, " + key + ", " + idioma + " ] Error: " + e.getMessage());
		}
	}	
	

	/**
	 * Método que devuelve el menú principal de un microsite listo para ser visualizado por el público.
	 * @param idmicrosite Long con el id del microsite
	 * @param idioma String con el idioma
	 * @return ArrayList con objetos "Menufront"
	 * @throws Exception
	 */
	public ArrayList<?> obtenerMainMenu(Long idmicrosite, String idioma) throws Exception  {
		try {
	    	ArrayList<?> listamenu = montarmenu(idmicrosite, idioma);
	    	return listamenu;
		} catch (Exception e) {
			throw new Exception(" [obtenerMainMenu, " + idmicrosite + ", " + idioma + " ] Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Método que devuelve un contenido listo para ser visualizado por el público.
	 * Parsea todas los tags de HTML de microsite.
	 * También parsea la versión Beta del contenido.
	 * @param idcontenido Long con el id del contenido
	 * @param idioma String con el idioma
	 * @param microsite Microsite al que pertenece el contenido
	 * @return Contenido
	 * @throws Exception
	 */
	public Contenido obtenerContenido(Long idcontenido, String idioma, Microsite microsite) throws Exception  {
		try {
			
			ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
			Contenido contenido = contenidodel.obtenerContenido(idcontenido);
			contenido.setIdi(idioma);
			String urlredireccionada = ((TraduccionContenido)contenido.getTraduccion(idioma)).getUrl();
			if (urlredireccionada==null) 
				contenido = reemplazarTags(contenido, idioma, microsite);
			
			return contenido;
			
		} catch (Exception e) {
			throw new Exception(" [obtenerContenido, idsite=" + microsite.getId() + ", cont=" + idcontenido + ", idioma=" + idioma + " ] Error=" + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3) );
		}
		
	}
	
	/**
	 * Obtiene una rama de menú completa lista para ser visualizada por el público.
	 * @param idmenu Long con el identificador del menu
	 * @param idioma String con el idioma
	 * @return Menu
	 * @throws Exception
	 */
	public Menu obtenerMenuBranch(Long idmenu, String idioma) throws Exception  {
		try {		
			MenuDelegate menudel = DelegateUtil.getMenuDelegate();
	    	Menu menu = menudel.obtenerMenuThin(idmenu,idioma);
			return menu;
		} catch (Exception e) {
			throw new Exception(" [obtenerMenuBranch, idmenu=" + idmenu + ", idioma=" + idioma + " ] Error: " + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
		
	}
	
	/**
	 * Obtiene una noticia lista para ser visualizada por el público.
	 * @param idnoticia Long con el identificador de la noticia.
	 * @param idioma String con el idioma
	 * @return Noticia
	 * @throws Exception
	 */
	public Noticia obtenerNoticia(Long idnoticia, String idioma) throws Exception  {
		try {
			
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			Noticia noticia = noticiadel.obtenerNoticia(idnoticia);
			noticia.setIdi(idioma);
			noticia.getTipo().setIdi(idioma);			
			return noticia;
			
		} catch (Exception e) {
			throw new Exception(" [obtenerNoticia, idnoticia=" + idnoticia + ", idioma=" + idioma + " ] Error: " + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
		
	}
	
	/**
	 * Metodo que devuleve un listado de las listas de distribucion de un microsite
	 * @param microsite Microsite
	 * @param idioma String con el idioma
	 * @return ArrayList Lista de objetos "Tiposervicio".
	 * @throws Exception
	 */
	public List<?> obtenerListadoDistribucionMicrosite(Long idmicrosite) throws Exception  {
		List l = new ArrayList();
		try {
			LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
			distribDel.init(idmicrosite);
			return distribDel.listarListaDistribucion();
		}catch(Exception e){
			throw new Exception(" [obtenerListadoDistribucionMicrosite, idsite=" + idmicrosite + " ] Error: " + e.getMessage()+ "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}
		
	/**
	 * Metodo que devuleve un listado de servicios ofrecidos por un microsite
	 * @param microsite Microsite
	 * @param idioma String con el idioma
	 * @return ArrayList Lista de objetos "Tiposervicio".
	 * @throws Exception
	 */
	public ArrayList<Object> obtenerListadoServiciosMicrosite(Microsite microsite, String idioma) throws Exception  {
		try {
		    	
			ArrayList<Object> listserofrtoken=new ArrayList<Object>();
			ArrayList<Object> listserofr = new ArrayList<Object>();
			if (microsite.getServiciosOfrecidos()!=null)
				listserofrtoken = Cadenas.getArrayListFromString(microsite.getServiciosOfrecidos());
			
			Iterator<Object> iter=listserofrtoken.iterator();
			while (iter.hasNext()) {
				TiposervicioDelegate tiposerdel= DelegateUtil.getTiposervicioDelegate();
				Tiposervicio tiposervicio = tiposerdel.obtenerTiposervicio(new Long((String)iter.next()));
				listserofr.add(tiposervicio);
			}
	    	
	    	return listserofr;	    	
		} catch (Exception e) {
			throw new Exception(" [obtenerListadoServiciosMicrosite, idsite=" + microsite.getId() + ", idioma=" + idioma + " ] Error: " + e.getMessage()+ "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}
	
	
	/**
	 * Obtiene un listado de centros asociados a una Unidad Administrativa.
	 * @param coduo Long con el id de la ua.
	 * @param idioma String con el idioma
	 * @return Collection Coleccion de objetos "LlocModel"
	 * @throws Exception
	 */
	public Collection<?> obtenerUacentres(Long coduo, String idioma) throws Exception  {
		try {
			Collection<?> direcciones = Modulos.centres(""+coduo, idioma);
			return direcciones;
		} catch (Exception e) {
			throw new Exception(" [obtenerUacentres, coduo=" + coduo + ", idioma=" + idioma + " ] Error: " + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}
	
	/**
	 * Obtenr los detalles de una Unidad Administrativa
	 * @param coduo codigo de la Unidad Administrativa
	 * @param idioma idioma String con el idioma
	 * @return  UOModel Objeto UOMOdel
	 * @throws Exception
	 */
	public UOModel getUaDetails(String coduo, String idioma) throws Exception  {
		try {
			UOModel uo = Modulos.getDetails(coduo, idioma);
			return uo;
		} catch (Exception e) {
			throw new Exception(" [getUaDetails, coduo=" + coduo + ", idioma=" + idioma + " ] Error: " + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}
	
	
/* ****************      METODOS   PRIVADOS          ******************* */
	
	/**
	 * Método que devuelve una lista con el menú según el idioma.
	 * @param idi
	 * @return ArrayList
	 */
	private ArrayList<MenuFront> montarmenu(Long idmicrosite, String idi) throws Exception {
		ArrayList<MenuFront> listadomenu = new ArrayList<MenuFront>();
		
		//los menus de primer nivel ya salen ordenados
		try {
			
			MenuDelegate menudel = DelegateUtil.getMenuDelegate();
			//recorrer los menus
			Iterator<?> iter = menudel.listarMenuMicrosite(idmicrosite, new Long(0), "S", idi).iterator();
		    while (iter.hasNext()) {
		    	Menu menu = (Menu)iter.next();
		    	MenuFront menufront = new MenuFront(menu) ;
		    	menufront.setIdi(idi);
		    	
		    	//recorrer las paginas y coger las visibles y no caducadas
				Iterator<?> iterpaginas = menu.getContenidos().iterator();
			    while (iterpaginas.hasNext()) {
			    	String iditmp = idi; 
			    	Contenido conte = (Contenido)iterpaginas.next();
			    	TraduccionContenido tracon = (TraduccionContenido)conte.getTraduccion(idi);
			    	if (tracon==null) { iditmp=Idioma.DEFAULT; tracon = (TraduccionContenido)conte.getTraduccion(iditmp); }
			    	if ( (tracon!=null) && (tracon.getTitulo()!=null) ){
				    	if ( (conte.getVisible().equals("S")) && (Fechas.vigente(conte.getFpublicacion(), conte.getFcaducidad())) ) {
				    		conte.setIdi(iditmp);
					    	if ((tracon.getUrl()!=null) && (tracon.getUrl().indexOf("http")!=-1))
					    		conte.setUrlExterna("true");
					    	else
					    		conte.setUrlExterna("false");
				    		menufront.getListacosas().add(conte);
				    	}
			    	}
			    }
			    
		    	//recoger los submenus. y dentro de los submenus recorrer las paginas y coger las visibles y no caducadas.
			    Iterator<?> itermenus = menudel.listarMenuMicrosite(idmicrosite, menu.getId(), "S", idi).iterator();
			    while (itermenus.hasNext()) {
			    	Menu submenu = (Menu)itermenus.next();
			    	MenuFront menufrontsub = new MenuFront(submenu) ;
			    	menufrontsub.setIdi(idi);
			    	
			    	//recorrer las paginas y coger las visibles y no caducadas
					Iterator<?> iterpaginassub = submenu.getContenidos().iterator();
				    while (iterpaginassub.hasNext()) {
				    	String iditmp = idi; 
				    	Contenido contesub = (Contenido)iterpaginassub.next();
				    	TraduccionContenido tracon = (TraduccionContenido)contesub.getTraduccion(idi);
				    	if (tracon==null) { iditmp=Idioma.DEFAULT; tracon = (TraduccionContenido)contesub.getTraduccion(iditmp); }
				    	if ( (tracon!=null) && (tracon.getTitulo()!=null) ){
					    	if ( (contesub.getVisible().equals("S")) && (Fechas.vigente(contesub.getFpublicacion(), contesub.getFcaducidad())) ) {
					    		contesub.setIdi(iditmp);
						    	if ((tracon.getUrl()!=null) && (tracon.getUrl().indexOf("http")!=-1))
						    		contesub.setUrlExterna("true");
						    	else
						    		contesub.setUrlExterna("false");
					    		menufrontsub.getListacosas().add(contesub);
					    	}
				    	}
				    }
				    
				    //los elementos que cuelgan del primer nivel tienen que reordenarse
				    menufront.getListacosas().add(menufrontsub);   	
			    }

			    menufront.setListacosas( ordenarlista(menufront.getListacosas()) );
			    menufront.setIdi(idi);
			    listadomenu.add(menufront);
			    	
		    }
		    return listadomenu;
		    
		} catch (Exception e) {
			listadomenu = new ArrayList<MenuFront>();
			throw new Exception(" [montarmenu, " + idmicrosite + ", " + idi + " ] Error=" + e.getMessage() +"\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3) );			
		}
	}
	
	/**
	 *  Comparar Menu
	 */
	private static class ComparatorMenu implements Comparator {
	    public int compare(Object element1, Object element2) {
	    	
	    	Integer lower1 = new Integer(0) ;
	    	Integer lower2 = new Integer(0) ;
	    	if (element1 instanceof Contenido) {
	    		lower1 = new Integer( ((Contenido)element1).getOrden() );
	    	} else {
	    		lower1 = new Integer( ((MenuFront)element1).getOrden() );
	    	}
	    	
	    	if (element2 instanceof Contenido) {
	    		lower2 = new Integer( ((Contenido)element2).getOrden() );
	    	} else {
	    		lower2 = new Integer( ((MenuFront)element2).getOrden() );
	    	}	    	

	    	return lower1.compareTo(lower2);
	    }
	  }
	
	/**
	 *  Método privado utilizado para ordenar una lista
	 * @param listaoriginal
	 * @return Arraylist lista ordenada
	 */
	private ArrayList<Traducible> ordenarlista(ArrayList<Traducible> listaoriginal) {

		ArrayList<Traducible> listaresultante = new ArrayList<Traducible>(listaoriginal);
		Comparator comp = new ComparatorMenu();	  
	  	Collections.sort(listaresultante, comp);
	  	return listaresultante;
		  	
	  }	
	
	/**
	 * Método privado para remplazar tags.
	 * @param contenido
	 * @param idioma
	 * @param microsite
	 * @return contenido contenido con los tags remplazados
	 * @throws Exception
	 */
	private Contenido reemplazarTags(Contenido contenido, String idioma, Microsite microsite) throws Exception {
		try {
			if (contenido.getTraduccion(idioma)!=null) {
				if (((TraduccionContenido)contenido.getTraduccion(idioma)).getTexto()!=null) {
					int noticias=3;
					if (microsite.getNumeronoticias()!=0) noticias=microsite.getNumeronoticias();	
					
					MicrositeParser microparser = new MicrositeParser(microsite.getRestringido(),((TraduccionContenido)contenido.getTraduccion(idioma)).getTxbeta(), microsite.getId(), idioma, noticias);
					microparser.doParser(idioma);
					((TraduccionContenido)contenido.getTraduccion(idioma)).setTxbeta(microparser.getHtmlParsed().toString());
				
					MicrositeParser microparser2 = new MicrositeParser(microsite.getRestringido(),((TraduccionContenido)contenido.getTraduccion(idioma)).getTexto(), microsite.getId(), idioma, noticias);
					microparser2.doParser(idioma);
					((TraduccionContenido)contenido.getTraduccion(idioma)).setTexto(microparser2.getHtmlParsed().toString());
					
				}
			}
			return contenido;
		} catch (Exception e) {
			throw new Exception(" [reemplazarTags, idsite=" + microsite.getId() + ", cont=" + contenido.getId() + ", idioma=" + idioma + " ] Error=" + e.getMessage() +"\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3) );			
		}	
	}
	
}
