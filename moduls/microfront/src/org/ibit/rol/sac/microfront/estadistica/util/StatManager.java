package org.ibit.rol.sac.microfront.estadistica.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.ibit.rol.sac.microfront.Microfront;

import org.ibit.rol.sac.micromodel.Agenda;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micromodel.Contacto;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micromodel.ListaDistribucion;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EstadisticaDelegate;




import org.ibit.rol.sac.microfront.util.Fechas;

/**
 * 
 * Clase StatManager. Utilizada para gestionar la grabaci�n de las estad�sticas.
 * @author Indra
 *
 */
public class StatManager {
	
	/**
	 * M�todo p�blico para grabar las estad�sticas del componente Agenda.
	 * @param agenda
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Agenda agenda, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(agenda.getIdmicrosite(), agenda.getId(), Microfront.RAGENDA, publico, buffer);
	}
	
	/**
	 * M�todo p�blico para grabar las estad�sticas del componente Lista de distribuci�n.
	 * @param listadistribucion
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(ListaDistribucion lDistrib, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(lDistrib.getMicrosite().getId(), lDistrib.getId(), Microfront.RLDISTRIB, publico, buffer);
	}
	
	/**
	 * M�todo p�blico para grabar las estad�sticas del componente Banner.
	 * 
	 * @param banner
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Banner banner, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(banner.getIdmicrosite(), banner.getId(), Microfront.RBANNER, publico, buffer);
	}	

	/**
	 * M�todo p�blico para grabar las estad�sticas del componente contacto.
	 * 
	 * @param contacto
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Contacto contacto, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(contacto.getIdmicrosite(), contacto.getId(), Microfront.RCONTACTO, publico, buffer);
	}	
	
	/**
	 * M�todo p�blico para grabar las estad�sticas el contenido de un Microsite.
	 * @param contenido
	 * @param idmicrosite
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Contenido contenido, Long idmicrosite, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(idmicrosite, contenido.getId(), Microfront.RCONTENIDO, publico, buffer);
	}

	/**
	 * 
	 * M�todo p�blico para grabar las estad�sticas el acceso a un Microsite. Con el tipo de servicio RFAQ = "FQS00"
	 * @param idmicrosite
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Long idmicrosite, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(idmicrosite, idmicrosite, Microfront.RFAQ, publico, buffer);
	}	

	/**
	 * M�todo p�blico para grabar las estad�sticas el acceso a un Microsite. En funci�n de un tipo de servicio.
	 * @param idmicrosite
	 * @param tipo
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Long idmicrosite, String tipo, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(idmicrosite, idmicrosite, tipo, publico, buffer);
	}	
	
	/**
	 * M�todo p�blico para grabar las estad�sticas del componente Noticia.
	 * @param noticia
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Noticia noticia, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(noticia.getIdmicrosite(), noticia.getId(), Microfront.RNOTICIA, publico, buffer);
	}
	
	/**
	 * M�todo p�blico para grabar las estad�sticas de un Microsite. 
	 * @param microsite
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Microsite microsite, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(microsite.getId(), microsite.getId(), Microfront.RMICROSITE, publico, buffer);
	}	
	
	/**
	 * M�todo p�blico para grabar las estad�sticas de acceso a una encuesta.
	 * @param encuesta
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Encuesta encuesta, int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(encuesta.getIdmicrosite(), encuesta.getId(), Microfront.RENCUESTA, publico, buffer);
	}	
	
	
	/**
	 * M�todo privado al que llaman el resto. Es el encargado de sumar y grabar la estadistica
	 * @param idmicrosite
	 * @param iditem
	 * @param ref
	 * @throws DelegateException
	 */
	private static List<Estadistica> grabarStat(Long idmicrosite, Long iditem, String ref, int publico, List<Estadistica> buffer) throws DelegateException  {
		
		// La grabaci�n de estad�sticas no se realiza en el rango de horas de 1 a 2 para evitar
		// conflictos con el backup de BBDD
		Integer mes = new Integer(Fechas.formatfechaactual2Stats());
		EstadisticaDelegate statdel = DelegateUtil.getEstadisticaDelegate();
		Estadistica stat = statdel.obtenerEstadistica(idmicrosite, iditem, ref, mes, new Integer(publico));
		stat.setAccesos(stat.getAccesos()+1);
		
		if (!Fechas.betweenHours(new Date(), 1, 0, 2, 0)) { 
			//Grabamos la estad�stica actual
			statdel.grabarEstadistica(stat);
			
			//Tratamos el buffer en el caso que tenga estad�sticas y las grabamos en BBDD
			Iterator<Estadistica> iter = buffer.iterator();

			while (iter.hasNext()) {
	       	 stat = new Estadistica();
	       	 stat = (Estadistica)iter.next();
	       	 statdel.grabarEstadistica(stat);
	        }
			//Grabadas las estad�sticas limpiamos el buffer
			buffer.clear();
		}
		else buffer.add(stat);	

		return buffer;
	}
	
}
