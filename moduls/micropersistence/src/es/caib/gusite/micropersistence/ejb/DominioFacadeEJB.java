package es.caib.gusite.micropersistence.ejb;

import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SessionBean que sirve para generar las interfaces que deberan cumplir los
 * ejbs de dominios remotos
 * 
 * @ejb.bean name="sac/micropersistence/DominioFacade"
 *           jndi-name="es.caib.gusite.micropersistence.DominioFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @author Indra
 * 
 */
public abstract class DominioFacadeEJB implements SessionBean {

	private static final long serialVersionUID = -4619525793037709635L;
	protected static Log log = LogFactory.getLog(DominioFacadeEJB.class);

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked = "true"
	 */
	public void ejbCreate() throws CreateException {
		log.info("ejbCreate: " + this.getClass());
	}

	/**
	 * @ejb.interface-method
	 * @ejb.permission unchecked = "true"
	 */
	public Map<?, ?> obtenerListado(String id, Map<?, ?> parametros) {
		return null;
	}

}
