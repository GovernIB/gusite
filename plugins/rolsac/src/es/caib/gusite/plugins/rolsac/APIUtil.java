package es.caib.gusite.plugins.rolsac;

import es.caib.rolsac.api.v2.estadistica.EstadisticaInsertService;
import es.caib.rolsac.api.v2.general.BeanUtils;
import es.caib.rolsac.api.v2.general.BeanUtils.STRATEGY;
import es.caib.rolsac.api.v2.general.CertificadoUtil;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;

public class APIUtil {
	
	public static final STRATEGY API_STRATEGY = STRATEGY.WS;
    
    public static enum Sexo {
        MASCULINO, 
        FEMENINO;
        
        public static Sexo getSexo(Integer sexo) {
            return sexo == 2 ? FEMENINO : MASCULINO;
        }
    };
    
    private APIUtil() {}
    
    public static RolsacQueryService getRolsacQueryService() {
    	
    	String keyStoreName = System.getProperty("es.caib.gusite.api.rolsac.keystore.nombre");
    	String keyStorePass = System.getProperty("es.caib.gusite.api.rolsac.keystore.pass");
    	String url = System.getProperty("es.caib.gusite.url.rolsac");
    	
    	if (API_STRATEGY == STRATEGY.WS)
    		CertificadoUtil.autentificar(keyStorePass, keyStoreName);
    	
    	RolsacQueryService rqs;
    	if (url == null || url.isEmpty()) {
    		rqs = (RolsacQueryService)BeanUtils.getAdapter("rolsac", APIUtil.API_STRATEGY);
    	} else {
    		rqs = (RolsacQueryService)BeanUtils.getAdapter("rolsac", APIUtil.API_STRATEGY, url);
    	}
    	return rqs;
    	
    }
    
    public static EstadisticaInsertService getEstadisticaInsertService() {
		
        return (EstadisticaInsertService)BeanUtils.getAdapter("estadistica", APIUtil.API_STRATEGY);
        
	}

}
