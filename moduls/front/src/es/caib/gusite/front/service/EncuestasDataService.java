package es.caib.gusite.front.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;


import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

@Service
public class EncuestasDataService {
	

	protected static Log log = LogFactory.getLog(EncuestasDataService.class);

//	@SuppressWarnings("unchecked")
//	public ResultadoBusqueda<Encuesta> getListadoFormularios (
//			Microsite microsite, Idioma lang, BaseCriteria formulario) throws DelegateException {
//
//    	EncuestaDelegate encuestadel=DelegateUtil.getEncuestaDelegate();
//		encuestadel.init();
//		encuestadel.setWhere("where encuesta.visible='S' and encuesta.idmicrosite=" + microsite.getId());
//    	
//    
//    	
//        if (formulario.getFiltro()!= null && formulario.getFiltro().length()>0)
//        	encuestadel.setFiltro(formulario.getFiltro());
//    
//        if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0)
//        	encuestadel.setOrderby(formulario.getOrdenacion());
//
//        // Indicamos la página a visualizar
//        encuestadel.setPagina(formulario.getPagina());
//        
//		List<Encuesta> listaencuestas = (List<Encuesta>)encuestadel.listarEncuestas();
//        ResultadoBusqueda<Encuesta> ret = new ResultadoBusqueda<Encuesta>(listaencuestas, (Map<String, Integer>)encuestadel.getParametros());
//        return ret;
//		
//		
//	}
//	
	
	public Encuesta getEncuestas(Microsite microsite, Idioma lang,
			long idEncuesta) throws DelegateException {

		EncuestaDelegate encuestadel=DelegateUtil.getEncuestaDelegate();
		return encuestadel.obtenerEncuesta(idEncuesta);

	}



	
	

	
}
