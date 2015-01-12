package org.ibit.rol.sac.microfront.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.model.AgrupacionMateria;
import org.ibit.rol.sac.persistence.delegate.AgrupacionMDelegate;
import org.ibit.rol.sac.persistence.delegate.DelegateException;
import org.ibit.rol.sac.persistence.delegate.DelegateUtil;
import org.ibit.rol.sac.persistence.delegate.IdiomaDelegate;

import es.caib.boib.bocaib.model.BoibModel;
import es.caib.sac.unitatOrganica.model.UOMinModel;

/**
 * Clase MenuCabecera. Utilizado para trabajar con las cabeceras del microsite
 * @author Indra
 *
 */
public class MenuCabecera {

	/*Se guardará en cada hash una entrada por idioma. Y a su vez, el `value` del hash será un ArrayList con el listado correspondiente.
	 * Excepto en darrerboib que el value será un String	 */	
	private static Hashtable<String, Collection<UOMinModel>> uos = new Hashtable<String, Collection<UOMinModel>>();
	private static Hashtable<String, Collection<?>> agrupacionmaterias = new Hashtable<String, Collection<?>>();
	private static Hashtable<String,String> urldarrerboib = new Hashtable<String,String>();
	private static Hashtable<String,UOMinModel> portavoz = new Hashtable<String,UOMinModel>();
	
	private static Log log = LogFactory.getLog( MenuCabecera.class  );
	
	/**
	 * Parte que se ejecuta la primera vez que se invoca a esta clase
	 */
	static {
		log.info("Refresco de menu estático de PortalCAIB en Microsites realizado.");
		refrescarMenu();
	}
	
	/**
	 * Método público para refrescar el Menu de los microsites
	 */
	public static void refrescarMenu() {
		try {

			IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
			Iterator<?> itLang = ididel.listarLenguajes().iterator();
			
			while (itLang.hasNext()) {
				String lang = (String)itLang.next();
				
		    	// Unidades Organizativas
				Collection<?> cuos=es.caib.sac.indra.moduls.Modulos.getUOs("1","cri",lang);
				
				// La conselleria de Portavoz la pongo a parte
				
				Collection nuevas_uas = new ArrayList();
				Iterator<?> it = cuos.iterator();
				UOMinModel conse;
				while (it.hasNext()) {
					conse = (UOMinModel)it.next();
					if (conse.getCodi().equals(Microfront.UO_PORTAVOZ)) {
						portavoz.put(lang, conse);
					}
					else {
						nuevas_uas.add(conse);
					}
				}
				
				uos.put(lang, nuevas_uas);
		    	
		    	// Grupos de materias
		    	Collection cagrupacionesm = getAgrupacionesMateria(lang);
		    	agrupacionmaterias.put(lang, cagrupacionesm);

		    	/* agarcia: desactivamos último boib
		    	// Ultimo BOIB
		    	BoibModel ultimoboib=es.caib.sac.indra.moduls.Modulos.getUltimoBoib();
		    	urldarrerboib.put(lang, "/boib/interior.do?lang="+lang+"&p_numero="+ultimoboib.getNumero() );
		    	*/
		    	
			}
			
		} catch (Exception e) {
			log.error("NO SE HA PODIDO CALCULAR EL MENU DE LA CABECERA DEL PORTALCAIB.");
			e.printStackTrace();
		}
		
	}
	
	
	public static Collection getAgrupacionesMateria (String idioma) {
		Collection<?> lista = null;
		ArrayList listaresultante = null;
		AgrupacionMDelegate agrumdel = DelegateUtil.getAgrupacionMDelegate(); 
		try {
			lista = agrumdel.listarAgrupacionM();
			
			listaresultante = new ArrayList(lista);
			Comparator comp = new AgrupacionMComparator();
		  	Collections.sort(listaresultante, comp);
		  	
		  	return listaresultante;			
			
			
		} catch (DelegateException e) {
			e.printStackTrace();
		}
		return listaresultante;
	}	
	
	
	
	  private static class AgrupacionMComparator implements Comparator {
		    public int compare(Object element1, Object element2) {
		    	Integer lower1 = new Integer(0) ;
		    	Integer lower2 = new Integer(0) ;
		    	lower1 = new Integer( ((AgrupacionMateria)element1).getSeccion().getOrden() );
		    	lower2 = new Integer( ((AgrupacionMateria)element2).getSeccion().getOrden() );
		    	return lower1.compareTo(lower2);
		    }
	  }	 
	
	
	  private static class UOSComparator implements Comparator {
		    public int compare(Object element1, Object element2) {
		    	
		    	String nom1 = ( ((UOMinModel)element1).getAbreviatura()!=null )?((UOMinModel)element1).getAbreviatura():"";
		    	String nom2 = ( ((UOMinModel)element2).getAbreviatura()!=null )?((UOMinModel)element2).getAbreviatura():"";
	    	
		    	return nom1.toLowerCase().compareTo(nom2.toLowerCase());
		    }
	  }

	/**
	 * Devuelve un listado de las consellerias
	 * @param lang
	 * @return ArrayList
	 */
	public static Collection<?> getUos(String lang) {
		Collection<?> lista = (Collection) uos.get(lang);
		if (lista == null) return lista;
		try {
	    	Collection listaDef = new  ArrayList();
	    	Iterator<?> conseDef = lista.iterator();    	
	    	while (conseDef.hasNext()) {
	    		UOMinModel cons = (UOMinModel)conseDef.next();    	
				if (!cons.getCodi().equals(Microfront.UO_PORTAVOZ)) {
					listaDef.add(cons);				
				}
	    	}		
	    	return listaDef;
		} catch (Exception e) {
			e.printStackTrace();
		}	    	
		return lista;		
	}
	
	/**
	 * Devuelve un listado de temas
	 * @param lang
	 * @return ArrayList
	 */
	public static Collection getAgrupacionmaterias(String lang) {
		return (Collection)agrupacionmaterias.get(lang);
	}

	/**
	 * Devuelve la conselleria de portavoz
	 * @param lang
	 * @return ArrayList
	 */
	public static UOMinModel getPortavoz(String lang) {
		return (UOMinModel)portavoz.get(lang);
	}	
	
	/**
	 * Devuelve una cadena con la url del ultimo boib.
	 * @param lang
	 * @return String
	 */
	public static String getUrldarrerboib(String lang) {
		return (String)urldarrerboib.get(lang);
	}
	
}
