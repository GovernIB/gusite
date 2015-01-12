package es.caib.gusite.front.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

@Service
public class ContactosDataService {

	protected static Log log = LogFactory.getLog(ContactosDataService.class);

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Contacto> getListadoFormularios (
			Microsite microsite, Idioma lang, BaseCriteria formulario) throws DelegateException {

    	ContactoDelegate contactodel = DelegateUtil.getContactoDelegate();
    	contactodel.init();
    	contactodel.setWhere("where contacto.visible='S' and contacto.idmicrosite=" + microsite.getId());
    	
        if (formulario.getFiltro()!= null && formulario.getFiltro().length()>0)
        	contactodel.setFiltro(formulario.getFiltro());
    
        if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0)
        	contactodel.setOrderby(formulario.getOrdenacion());

        // Indicamos la página a visualizar
       	contactodel.setPagina(formulario.getPagina());
        
		List<Contacto> listacontactos = (List<Contacto>)contactodel.listarContactos();
        ResultadoBusqueda<Contacto> ret = new ResultadoBusqueda<Contacto>(listacontactos, (Map<String, Integer>)contactodel.getParametros());
        return ret;
		
		
	}
	
	public Contacto getFormulario(Microsite microsite, Idioma lang,
			long idContacto) throws DelegateException {
		ContactoDelegate contactoldel = DelegateUtil.getContactoDelegate();
		return contactoldel.obtenerContacto(idContacto);
	}
	

	
	

	
}
