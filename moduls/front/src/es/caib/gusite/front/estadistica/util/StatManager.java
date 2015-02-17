package es.caib.gusite.front.estadistica.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EstadisticaDelegate;

/**
 * 
 * Clase StatManager. Utilizada para gestionar la grabación de las estadísticas.
 * 
 * @author Indra
 * 
 */
public class StatManager {

	/**
	 * Método público para grabar las estadísticas del componente Agenda.
	 * 
	 * @param agenda
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Agenda agenda,
			int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(agenda.getIdmicrosite(), agenda.getId(),
				Front.RAGENDA, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas del componente Lista de
	 * distribución.
	 * 
	 * @param listadistribucion
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(
			ListaDistribucion lDistrib, int publico, List<Estadistica> buffer)
			throws DelegateException {
		return grabarStat(lDistrib.getMicrosite().getId(), lDistrib.getId(),
				Front.RLDISTRIB, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas del componente contacto.
	 * 
	 * @param contacto
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Contacto contacto,
			int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(contacto.getIdmicrosite(), contacto.getId(),
				Front.RCONTACTO, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas el contenido de un Microsite.
	 * 
	 * @param contenido
	 * @param idmicrosite
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Contenido contenido,
			Long idmicrosite, int publico, List<Estadistica> buffer)
			throws DelegateException {
		return grabarStat(idmicrosite, contenido.getId(), Front.RCONTENIDO,
				publico, buffer);
	}

	/**
	 * 
	 * Método público para grabar las estadísticas el acceso a un Microsite. Con
	 * el tipo de servicio RFAQ = "FQS00"
	 * 
	 * @param idmicrosite
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Long idmicrosite,
			int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(idmicrosite, idmicrosite, Front.RFAQ, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas el acceso a un Microsite. En
	 * función de un tipo de servicio.
	 * 
	 * @param idmicrosite
	 * @param tipo
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Long idmicrosite,
			String tipo, int publico, List<Estadistica> buffer)
			throws DelegateException {
		return grabarStat(idmicrosite, idmicrosite, tipo, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas del componente Noticia.
	 * 
	 * @param noticia
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Noticia noticia,
			int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(noticia.getIdmicrosite(), noticia.getId(),
				Front.RNOTICIA, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas de un Microsite.
	 * 
	 * @param microsite
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Microsite microsite,
			int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(microsite.getId(), microsite.getId(),
				Front.RMICROSITE, publico, buffer);
	}

	/**
	 * Método público para grabar las estadísticas de acceso a una encuesta.
	 * 
	 * @param encuesta
	 * @param publico
	 * @param buffer
	 * @return List
	 * @throws DelegateException
	 */
	public static List<Estadistica> grabarestadistica(Encuesta encuesta,
			int publico, List<Estadistica> buffer) throws DelegateException {
		return grabarStat(encuesta.getIdmicrosite(), encuesta.getId(),
				Front.RENCUESTA, publico, buffer);
	}

	/**
	 * Método privado al que llaman el resto. Es el encargado de sumar y grabar
	 * la estadistica
	 * 
	 * @param idmicrosite
	 * @param iditem
	 * @param ref
	 * @throws DelegateException
	 */
	private static List<Estadistica> grabarStat(Long idmicrosite, Long iditem,
			String ref, int publico, List<Estadistica> buffer)
			throws DelegateException {

		// La grabación de estadísticas no se realiza en el rango de horas de 1
		// a 2 para evitar
		// conflictos con el backup de BBDD
		Integer mes = new Integer(Fechas.formatfechaactual2Stats());
		EstadisticaDelegate statdel = DelegateUtil.getEstadisticaDelegate();
		Estadistica stat = statdel.obtenerEstadistica(idmicrosite, iditem, ref,
				mes, new Integer(publico));
		stat.setAccesos(stat.getAccesos() + 1);

		if (!Fechas.betweenHours(new Date(), 1, 0, 2, 0)) {
			// Grabamos la estadística actual
			statdel.grabarEstadistica(stat);

			// Tratamos el buffer en el caso que tenga estadísticas y las
			// grabamos en BBDD
			Iterator<Estadistica> iter = buffer.iterator();

			while (iter.hasNext()) {
				stat = new Estadistica();
				stat = iter.next();
				statdel.grabarEstadistica(stat);
			}
			// Grabadas las estadísticas limpiamos el buffer
			buffer.clear();
		} else {
			buffer.add(stat);
		}

		return buffer;
	}

}
