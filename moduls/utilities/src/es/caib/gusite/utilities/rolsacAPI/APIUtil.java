package es.caib.gusite.utilities.rolsacAPI;

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
    	
    	/*if (API_STRATEGY == STRATEGY.WS)
    		CertificadoUtil.autentificar(keyStorePass, keyStoreName);*/
    	
    	return (RolsacQueryService)BeanUtils.getAdapter("rolsac", APIUtil.API_STRATEGY);
    	
    }
    
    public static EstadisticaInsertService getEstadisticaInsertService() {
		
        return (EstadisticaInsertService)BeanUtils.getAdapter("estadistica", APIUtil.API_STRATEGY);
        
	}

}
