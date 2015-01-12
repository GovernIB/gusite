package es.caib.gusite.micropersistence.plugins;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.intf.DominioFacade;
import es.caib.gusite.micropersistence.intf.DominioFacadeHome;
import es.caib.gusite.micropersistence.util.UsernamePasswordCallbackHandler;
import es.caib.gusite.micropersistence.util.UtilEjb;

//TODO: Ver como se podrían cachear dominios

/**
 * Plugin que permite acceder a dominios
 * @author INDRA
 */
public class PluginDominio {
	
	private static Log log = LogFactory.getLog(PluginDominio.class);
	
	
	public PluginDominio() {
	}

	/**
	 * Método para obtener listado de los valores del dominio
	 * @param dominio
	 * @param parametros
	 * @return Map
	 * @throws Exception
	 */
	public Map<?, ?> obtenerListado(Tipo dominio,Map<?, ?> parametros) throws Exception{		

		// Accedemos a dominio
		Map<?, ?> valoresDominio = resuelveDominioEJB(dominio,parametros);
		return valoresDominio;
	}
	
	/**
	 * Método privado para resolver el dominio EJB
	 * @param dominio
	 * @param parametros
	 * @return Map
	 * @throws Exception
	 */
	private Map<?, ?> resuelveDominioEJB(Tipo dominio,Map<?, ?> parametros) throws Exception {
		log.debug("Accedemos a Dominio EJB");
		LoginContext lc = null;
		String jndiName = dominio.getXjndi();
		DominioFacadeHome home = null;
		try {
			if((dominio.getXusr() != null) && (!dominio.getXusr().equals(""))) {	
				CallbackHandler handler = null; 
				handler = new UsernamePasswordCallbackHandler( dominio.getXusr(), dominio.getXpwd() ); 
				lc = new LoginContext("client-login", handler);
			    lc.login();
			}
			
			home = ( DominioFacadeHome ) lookupHome( dominio, jndiName, DominioFacadeHome.class );
			DominioFacade dominioEJB = home.create();
			return dominioEJB.obtenerListado( dominio.getXid(), parametros );
		}
		catch( Exception exc ) {
			log.error( exc );
			throw exc;
		}
		finally {
			if ( lc != null ){
				lc.logout();
			}
		}
	}
	
			
	private Object lookupHome( Tipo dominio, String jndiName, Class<DominioFacadeHome> narrowTo) throws Exception {
		log.debug("Acceso remoto a " + dominio.getXjndi() + " [" + dominio.getXurl() + "]");	
		return UtilEjb.lookupHome(dominio.getXjndi(),false /* local */,dominio.getXurl(),narrowTo);  		
	}
		
	
    
}

