package es.caib.gusite.plugins.rolsac;

import es.caib.rolsac.api.v2.estadistica.EstadisticaInsertService;
import es.caib.rolsac.api.v2.general.BeanUtils;
import es.caib.rolsac.api.v2.general.BeanUtils.STRATEGY;
import es.caib.rolsac.api.v2.general.CertificadoUtil;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;

/**
 * Clase API. 
 * 
 * @author slromero
 *
 */
public class APIUtil {
	
	/** Estrategia: WS o EJB. **/
	public static final STRATEGY API_STRATEGY = STRATEGY.WS;
    
	/** Enumerado del sexo. **/
    public static enum Sexo {
        MASCULINO, 
        FEMENINO;
        
        public static Sexo getSexo(Integer sexo) {
            return sexo == 2 ? FEMENINO : MASCULINO;
        }
    };
    
    /** Constructor. **/
    private APIUtil() {}
    
    /**
     * Obtiene el servicio de rolsac. 
     * @return
     */
    public static RolsacQueryService getRolsacQueryService() {
    	
    	final String keyStoreName = System.getProperty("es.caib.gusite.api.rolsac.keystore.nombre");
    	final String keyStorePass = System.getProperty("es.caib.gusite.api.rolsac.keystore.pass");
    	final String url = System.getProperty("es.caib.gusite.url.rolsac");
    	
    	if (API_STRATEGY == STRATEGY.WS) {
    		CertificadoUtil.autentificar(keyStorePass, keyStoreName);
    	}
    	
    	RolsacQueryService rqs;
    	if (url == null || url.isEmpty()) {
    		rqs = (RolsacQueryService)BeanUtils.getAdapter("rolsac", APIUtil.API_STRATEGY);
    	} else {
    		rqs = (RolsacQueryService)BeanUtils.getAdapter("rolsac", APIUtil.API_STRATEGY, url);
    	}
    	return rqs;
    	
    }
    
    /**
     * Obtiene el servicio de estadística.
     * @return
     */
    public static EstadisticaInsertService getEstadisticaInsertService() {
    	final String url = System.getProperty("es.caib.gusite.url.rolsac");
    	EstadisticaInsertService eis;
    	if (url == null || url.isEmpty()) {
    		eis = (EstadisticaInsertService)BeanUtils.getAdapter("estadistica", APIUtil.API_STRATEGY);
    	} else {
    		eis = (EstadisticaInsertService)BeanUtils.getAdapter("estadistica", APIUtil.API_STRATEGY, url);
    	}
    	return eis;
	}

}
