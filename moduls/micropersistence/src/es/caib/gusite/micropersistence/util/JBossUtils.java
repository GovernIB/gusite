package es.caib.gusite.micropersistence.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JBossUtils {
	protected static Log log = LogFactory.getLog(JBossUtils.class);

	public static Object lookupLocal(String jndiname) {

		try {
			InitialContext ic = new InitialContext();
			return ic.lookup(jndiname);
		} catch (NamingException e) {
			log.error("", e);
			return null;
		}
	}

	/*
	 * public static TransactionManager getTransactionManager() {
	 * 
	 * try { InitialContext ic = new InitialContext(); Object o =
	 * ic.lookup(TransactionManagerService.JNDI_NAME); TransactionManager tm =
	 * (TransactionManager) ic.lookup(TransactionManagerService.JNDI_NAME);
	 * return tm; } catch (NamingException e) { log.error("",e); return null; }
	 * }
	 * 
	 * public static Transaction getCurrentTransaction() { try {
	 * 
	 * javax.transaction.Transaction tx=
	 * getTransactionManager().getTransaction(); return tx;
	 * 
	 * } catch (SystemException e) { log.error("",e); return null; }
	 * 
	 * }
	 */
}
