package es.caib.gusite.front.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.agenda.AgendaCriteria;
import es.caib.gusite.front.faq.Faqtema;
import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;
import es.caib.gusite.utilities.rolsacAPI.APIUtil;
import es.caib.rolsac.api.v2.exception.QueryServiceException;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaDTO;

@Service
public class FrontDataService {

	protected static Log log = LogFactory.getLog(FrontDataService.class);

	@SuppressWarnings("unchecked")
	public Map<String, Map<Long, String>> getDatosCalendarioHome(Microsite microsite, Idioma lang) throws DelegateException {

		//CALENDARIO
    	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
    	agendadel.init();
    	agendadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId());
    	agendadel.setPagina(1);
    	agendadel.setTampagina(Integer.MAX_VALUE);
    	List<Agenda> listagenda = (List<Agenda>) agendadel.listarAgendas();
    	
		GregorianCalendar gc = new GregorianCalendar();
		Map<Long, String> listahash = new Hashtable<Long, String>();
		int anyo; int mes;

		// Establecemos el valor al día 1
		gc.set(Calendar.DAY_OF_MONTH, 1);
		
		Map<String, Map<Long, String>> meses = new Hashtable<String, Map<Long, String>>(); 
		
		int nummesesaretroceder=2;
		int nummeses = 3;
		//retrocedemos 2 meses
		gc = Fechas.anteriorMes(gc);
		gc = Fechas.anteriorMes(gc);
		
		for (int i=1;i<=nummeses+nummesesaretroceder;i++){
			anyo=gc.get(Calendar.YEAR); 
			mes=gc.get(Calendar.MONTH)+1;
			listahash = getHashMonthAgenda(listagenda, gc);
			meses.put(anyo + "-" + mes, listahash);
			gc = Fechas.siguienteMes(gc);
		}
		

		return meses;
    	
	
	}

	/**
	 * Devuelve una hash con el listado de eventos de un mes determinado.
	 * Key: dia del mes (long)
	 * Value: fecha (String formateado)
	 */
	private static Map<Long, String> getHashMonthAgenda(List<Agenda> listaagenda, GregorianCalendar gc) {

		Map<Long, String> mapaDiasMes = new Hashtable<Long, String>();
		
		try {
			
	    	java.text.SimpleDateFormat fech = new java.text.SimpleDateFormat("yyyyMMdd");
	    	for(int i=1;i<=gc.getActualMaximum(Calendar.DAY_OF_MONTH);i++) {
	    		GregorianCalendar fecha2 = new GregorianCalendar();
	    		fecha2.setTime(gc.getTime());
	    		fecha2.set(Calendar.DAY_OF_MONTH, i);
	    		fecha2.set(Calendar.HOUR_OF_DAY,0);
	    		fecha2.set(Calendar.MINUTE,0);
	    		String f_format2 = fech.format(fecha2.getTime());
	        	
	    		Iterator<?> iter = listaagenda.iterator();
	    		while (iter.hasNext()) {
	    			Agenda agenda = (Agenda)iter.next();
	    			if (Fechas.between(agenda.getFinicio(),agenda.getFfin(),fecha2.getTime())) {
	    				mapaDiasMes.put(new Long(fecha2.get(java.util.Calendar.DAY_OF_MONTH)),f_format2);
	    			}
	    		}
	    		
	    	}
		} catch (Exception e) {
			log.error("[getHashMonthAgenda]: " + e.getMessage());
		}
 		return mapaDiasMes;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Agenda> getDatosListadoHome(Microsite microsite, Idioma lang) throws DelegateException {
    	//LISTADO
    	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
    	agendadel.init();
    	
    	GregorianCalendar gc2 = new GregorianCalendar();
		java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
    	agendadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId() + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
    	agendadel.setOrderby2(" order by agenda.finicio asc");
    	agendadel.setPagina(1);
    	agendadel.setTampagina(3);
    	return (List<Agenda>) agendadel.listarAgendas();
	}

	
	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Agenda> getListadoAgenda(Microsite microsite, Idioma lang, Date fecha, int pagina) throws DelegateException {
    	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
    	agendadel.init();
    	agendadel.setWhere("where trad.id.codigoIdioma='"+lang.getLang()+"' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId());
    	agendadel.setOrderby2(" ORDER BY agenda.actividad, NVL(agenda.ffin,SYSDATE)");

        // Indicamos la página a visualizar
    	agendadel.setPagina(pagina);
        List<Agenda> listaeventos = (List<Agenda>)agendadel.listarAgendas(fecha,lang.getLang());
        traducelista(listaeventos, lang);
        ResultadoBusqueda<Agenda> ret = new ResultadoBusqueda<Agenda>(listaeventos, (Map<String, Integer>)agendadel.getParametros());
        
        return ret;
	}

	
	private void traducelista(List<Agenda> listaeventos, Idioma lang) {
		for (Agenda agenda  : listaeventos) {
			if (agenda.getActividad()!=null) 
				agenda.getActividad().setIdi(lang.getLang());
		}
	}

	

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Agenda> getListadoAgenda(Microsite microsite, Idioma lang, AgendaCriteria criteria) throws DelegateException {
		AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setTampagina(3); //TODO: esto está hardcoded y esmuy pequeño
		GregorianCalendar gc = new GregorianCalendar();
		//retrocedemos 2 meses
		gc = Fechas.anteriorMes(gc);
		gc = Fechas.anteriorMes(gc);
		
		GregorianCalendar gc2 = new GregorianCalendar();
		java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
		agendadel.setWhere("where trad.id.codigoIdioma='"+lang.getLang()+"' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId() + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
		agendadel.setOrderby2(" order by agenda.finicio asc");
		        
	    if (criteria.getFiltro()!= null && criteria.getFiltro().length()>0)
	    	agendadel.setFiltro(criteria.getFiltro());
	
	    if (criteria.getOrdenacion()!= null && criteria.getOrdenacion().length()>0)
	    	agendadel.setOrderby(criteria.getOrdenacion());
	
    	agendadel.setPagina(criteria.getPagina());
	    
        List<Agenda> listaeventos = (List<Agenda>)agendadel.listarAgendas(lang.getLang());
        traducelista(listaeventos, lang);
        ResultadoBusqueda<Agenda> ret = new ResultadoBusqueda<Agenda>(listaeventos, (Map<String, Integer>)agendadel.getParametros());
        return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Noticia> getNoticiasHome(Microsite microsite, Idioma lang) throws DelegateException {
		int noticias=3;
		if (microsite.getNumeronoticias()!=0){
			noticias=microsite.getNumeronoticias();
		}

    	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
    	noticiadel.init();noticiadel.setPagina(1);noticiadel.setTampagina(noticias);
    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
    	String wherenoticias="where noti.visible = 'S' and noti.idmicrosite = " + microsite.getId();
    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
    	noticiadel.setWhere(wherenoticias);
    	noticiadel.setOrderby2(" order by noti.fpublicacion desc");
    	 
    	return noticiadel.listarNoticiasThin(lang.getCodigoEstandar());
	}

	public List<Faqtema> listarFaqs(Microsite microsite, Idioma lang) throws DelegateException {
		TemaDelegate temadel = DelegateUtil.getTemafaqDelegate();
		temadel.init();temadel.setTampagina(Integer.MAX_VALUE);
		temadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and tema.idmicrosite=" + microsite.getId());
		List<?> listatemas=temadel.listarTemas();
		Iterator<?> iter = listatemas.iterator();
		List<Faqtema> listafaqstema = new ArrayList<Faqtema>();
		//recorrer los temas
		while (iter.hasNext()) {
				Temafaq tema = (Temafaq)iter.next();
		    	FaqDelegate faqdel = DelegateUtil.getFaqDelegate();
		    	faqdel.init();faqdel.setTampagina(Integer.MAX_VALUE);
		    	faqdel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and faq.visible='S' and faq.tema=" + tema.getId());
		    	List<?> listafaqs=faqdel.listarFaqs();
		    	ArrayList<Faq> tmpfaqs=new ArrayList<Faq>();
		    	Faqtema faqtema = new Faqtema();
		    	Iterator<?> iter2 = listafaqs.iterator();
		    	//recorrer las faqs de cada uno de los temas
				while (iter2.hasNext()) {
		       	 	Faq faq = (Faq)iter2.next();
		       	 	faq.setIdi(lang.getLang());
		       	 	//por si es nulo
		       	 	if  ( ((TraduccionFaq)faq.getTraduccion(lang.getLang())!=null) &&
		       	 		  (((TraduccionFaq)faq.getTraduccion(lang.getLang())).getPregunta()!=null) && 
		       	 		  (((TraduccionFaq)faq.getTraduccion(lang.getLang())).getRespuesta()!=null) ) {
		       	 			tmpfaqs.add(faq);
		       	 	}

				}
				if ((TraduccionTemafaq)tema.getTraduccion(lang.getLang())!=null) faqtema.setTema(((TraduccionTemafaq)tema.getTraduccion(lang.getLang())).getNombre());
				faqtema.setListadopreguntas(tmpfaqs);
				listafaqstema .add(faqtema);
			
		}
		return listafaqstema;
	}

	public UnitatAdministrativaDTO getUa(long idUa, Idioma lang) {
	    RolsacQueryService rqs = APIUtil.getRolsacQueryService();
	    UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
		uaCriteria.setId(String.valueOf(idUa));
		uaCriteria.setIdioma(lang.getLang());
		
		try {
			return rqs.obtenirUnitatAdministrativa(uaCriteria);
		} catch (QueryServiceException e) {
			log.error(e);
			return null;
		}
	}

	public Microsite getMicrosite(Long idSite, Idioma lang) throws ExceptionFrontMicro {
    	try {
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			return micrositedel.obtenerMicrosite(idSite);

    	} catch (DelegateException e) {
    		throw new ExceptionFrontMicro(e);
		}
	}
	
	public Microsite getMicrositeByKey(String mkey, Idioma lang) throws ExceptionFrontMicro {
    	try {
    		DelegateBase _delegateBase = new DelegateBase();
    		return _delegateBase.obtenerMicrositebyKey(mkey, lang.getLang());

    	} catch (DelegateException e) {
    		throw new ExceptionFrontMicro(e);
		}
	}
	
	
	public Archivo obtenerArchivo(Long id) throws ExceptionFrontPagina {
		try {
			ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			return archi.obtenerArchivo(id);
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e); 
		}
		
	}
	
	public Archivo obtenerArchivo(Long idSite, String name) throws ExceptionFrontPagina {
		try {
			ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			return  archi.obtenerArchivobyName(idSite, name);
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e); 
		}
		
	}

	public Frqssi getFormularioQssi(Microsite microsite, Idioma lang, long idQssi) throws ExceptionFrontPagina {

		try {
			FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
			Frqssi qssi = qssidel.obtenerFrqssi(idQssi);
			qssi.setIdi(lang.getLang());
			
			return qssi;

		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e); 
		}
	}

}
