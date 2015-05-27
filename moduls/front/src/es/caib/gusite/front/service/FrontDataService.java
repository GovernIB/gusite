package es.caib.gusite.front.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.agenda.AgendaCriteria;
import es.caib.gusite.front.faq.Faqtema;
import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.front.util.Cadenas;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoTemaFrontDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;
import es.caib.gusite.micropersistence.delegate.TemaFrontDelegate;

@Service
public class FrontDataService {

	protected static Log log = LogFactory.getLog(FrontDataService.class);

	@SuppressWarnings("unchecked")
	public Map<String, Set<Integer>> getDatosCalendario(Microsite microsite, Idioma lang) throws DelegateException {
		return this.getDatosCalendario(microsite, lang.getLang());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Set<Integer>> getDatosCalendario(Microsite microsite, String idioma) throws DelegateException {

		// CALENDARIO
		AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setWhere("where trad.id.codigoIdioma='" + idioma + "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId());
		agendadel.setPagina(1);
		agendadel.setTampagina(Integer.MAX_VALUE);
		List<Agenda> listagenda = (List<Agenda>) agendadel.listarAgendas();

		GregorianCalendar gc = new GregorianCalendar();
		Set<Integer> diasMesAgenda = new HashSet<Integer>();
		int anyo;
		int mes;

		// Establecemos el valor al día 1
		gc.set(Calendar.DAY_OF_MONTH, 1);

		Map<String, Set<Integer>> meses = new Hashtable<String, Set<Integer>>();

		int nummesesaretroceder = 2;
		int nummeses = 3;
		// retrocedemos 2 meses
		gc = Fechas.anteriorMes(gc);
		gc = Fechas.anteriorMes(gc);

		for (int i = 1; i <= nummeses + nummesesaretroceder; i++) {
			anyo = gc.get(Calendar.YEAR);
			mes = gc.get(Calendar.MONTH) + 1;
			diasMesAgenda = getDiasMesAgenda(listagenda, gc);
			meses.put(anyo + "-" + mes, diasMesAgenda);
			gc = Fechas.siguienteMes(gc);
		}

		return meses;

	}

	/**
	 * Devuelve una coleccion con los dias de un mes determinado en que hay
	 * eventos
	 */
	private static Set<Integer> getDiasMesAgenda(List<Agenda> listaagenda, GregorianCalendar gc) {

		Set<Integer> diasMes = new HashSet<Integer>();

		try {

			for (int i = 1; i <= gc.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
				GregorianCalendar fecha2 = new GregorianCalendar();
				fecha2.setTime(gc.getTime());
				fecha2.set(Calendar.DAY_OF_MONTH, i);
				fecha2.set(Calendar.HOUR_OF_DAY, 0);
				fecha2.set(Calendar.MINUTE, 0);
				Iterator<?> iter = listaagenda.iterator();
				while (iter.hasNext()) {
					Agenda agenda = (Agenda) iter.next();
					if (Fechas.between(agenda.getFinicio(), agenda.getFfin(), fecha2.getTime())) {
						diasMes.add(fecha2.get(java.util.Calendar.DAY_OF_MONTH));
					}
				}

			}
		} catch (Exception e) {
			log.error("[getDiasMesAgenda]: " + e.getMessage());
		}
		return diasMes;
	}

	@SuppressWarnings("unchecked")
	public List<Agenda> getDatosListadoHome(Microsite microsite, Idioma lang) throws DelegateException {
		// LISTADO
		AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();

		GregorianCalendar gc2 = new GregorianCalendar();
		java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
		agendadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId()
				+ " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
		agendadel.setOrderby2(" order by agenda.finicio asc");
		agendadel.setPagina(1);
		agendadel.setTampagina(3);
		List<Agenda> ret = (List<Agenda>) agendadel.listarAgendas();
		this.traducelista(ret, lang.getLang());
		return ret;
	}

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Agenda> getListadoAgenda(Microsite microsite, Idioma lang, Date fecha, int pagina) throws DelegateException {
		AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId());
		agendadel.setOrderby2(" ORDER BY agenda.actividad, NVL(agenda.ffin,SYSDATE)");

		// Indicamos la página a visualizar
		agendadel.setPagina(pagina);
		List<Agenda> listaeventos = (List<Agenda>) agendadel.listarAgendas(fecha, lang.getLang());
		this.traducelista(listaeventos, lang.getLang());
		ResultadoBusqueda<Agenda> ret = new ResultadoBusqueda<Agenda>(listaeventos, (Map<String, Integer>) agendadel.getParametros());

		return ret;
	}
	
	public Agenda loadAgenda(Long idCont, Idioma lang) throws ExceptionFrontPagina {

		try {
			AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
			Agenda ret = agendadel.obtenerAgenda(idCont);
			ret.setIdi(lang.getLang());
			if (ret.getActividad() != null) {
				ret.getActividad().setIdi(lang.getLang());
			}
			return ret;
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(" [loadAgenda, id=" + idCont + ", idioma=" + lang.getLang()
					+ " ] Error=" + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}	

	private void traducelista(List<Agenda> listaeventos, String lang) {
		for (Agenda agenda : listaeventos) {
			agenda.setIdi(lang);
			if (agenda.getActividad() != null) {
				agenda.getActividad().setIdi(lang);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Agenda> getListadoAgenda(Microsite microsite, Idioma lang, AgendaCriteria criteria) throws DelegateException {
		AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setTampagina(3); // TODO: esto está hardcoded y esmuy pequeño
		GregorianCalendar gc = new GregorianCalendar();
		// retrocedemos 2 meses
		gc = Fechas.anteriorMes(gc);
		gc = Fechas.anteriorMes(gc);

		GregorianCalendar gc2 = new GregorianCalendar();
		java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
		agendadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId()
				+ " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
		agendadel.setOrderby2(" order by agenda.finicio asc");

		if (criteria.getFiltro() != null && criteria.getFiltro().length() > 0) {
			agendadel.setFiltro(criteria.getFiltro());
		}

		if (criteria.getOrdenacion() != null && criteria.getOrdenacion().length() > 0) {
			agendadel.setOrderby(criteria.getOrdenacion());
		}

		agendadel.setPagina(criteria.getPagina());

		List<Agenda> listaeventos = (List<Agenda>) agendadel.listarAgendas(lang.getLang());
		this.traducelista(listaeventos, lang.getLang());
		ResultadoBusqueda<Agenda> ret = new ResultadoBusqueda<Agenda>(listaeventos, (Map<String, Integer>) agendadel.getParametros());
		return ret;
	}

	public List<Noticia> getNoticiasHome(Microsite site, Idioma lang) throws DelegateException {
		return this.getNoticiasHome(site, lang.getLang());
	}

	@SuppressWarnings("unchecked")
	public List<Noticia> getNoticiasHome(Microsite microsite, String idioma) throws DelegateException {
		int noticias = 3;
		if (microsite.getNumeronoticias() != 0) {
			noticias = microsite.getNumeronoticias();
		}

		NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		noticiadel.init();
		noticiadel.setPagina(1);
		noticiadel.setTampagina(noticias);
		java.sql.Date dt = new java.sql.Date((new Date()).getTime());
		String wherenoticias = "where noti.visible = 'S' and noti.idmicrosite = " + microsite.getId();
		wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
		wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
		wherenoticias += " and (noti.tipo.tipoelemento = " + Front.ELEM_NOTICIA + " )";
		noticiadel.setWhere(wherenoticias);
		noticiadel.setOrderby2(" order by noti.fpublicacion desc");

		List<Noticia> ret = noticiadel.listarNoticiasThin(idioma);
		for (Noticia n : ret) {
			n.setIdi(idioma);
			if (n.getTipo() != null) {
				n.getTipo().setIdi(idioma);
			}
		}
		return ret;
	}

	public List<Faqtema> listarFaqs(Microsite microsite, Idioma lang) throws DelegateException {
		TemaDelegate temadel = DelegateUtil.getTemafaqDelegate();
		temadel.init();
		temadel.setTampagina(Integer.MAX_VALUE);
		temadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and tema.idmicrosite=" + microsite.getId());
		List<?> listatemas = temadel.listarTemas();
		Iterator<?> iter = listatemas.iterator();
		List<Faqtema> listafaqstema = new ArrayList<Faqtema>();
		// recorrer los temas
		while (iter.hasNext()) {
			Temafaq tema = (Temafaq) iter.next();
			FaqDelegate faqdel = DelegateUtil.getFaqDelegate();
			faqdel.init();
			faqdel.setTampagina(Integer.MAX_VALUE);
			faqdel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and faq.visible='S' and faq.tema=" + tema.getId());
			List<?> listafaqs = faqdel.listarFaqs();
			ArrayList<Faq> tmpfaqs = new ArrayList<Faq>();
			Faqtema faqtema = new Faqtema();
			Iterator<?> iter2 = listafaqs.iterator();
			// recorrer las faqs de cada uno de los temas
			while (iter2.hasNext()) {
				Faq faq = (Faq) iter2.next();
				faq.setIdi(lang.getLang());
				// por si es nulo
				if (((TraduccionFaq) faq.getTraduccion(lang.getLang()) != null)
						&& (((TraduccionFaq) faq.getTraduccion(lang.getLang())).getPregunta() != null)
						&& (((TraduccionFaq) faq.getTraduccion(lang.getLang())).getRespuesta() != null)) {
					tmpfaqs.add(faq);
				}

			}
			if ((TraduccionTemafaq) tema.getTraduccion(lang.getLang()) != null) {
				faqtema.setTema(((TraduccionTemafaq) tema.getTraduccion(lang.getLang())).getNombre());
			}
			faqtema.setListadopreguntas(tmpfaqs);
			listafaqstema.add(faqtema);

		}
		return listafaqstema;
	}

	public Microsite getMicrosite(Long idSite, Idioma lang) throws ExceptionFrontMicro {
		try {
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			Microsite ret = micrositedel.obtenerMicrosite(idSite);
			ret.setIdi(lang.getLang());
			return ret;

		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	public Microsite getMicrositeByKey(String mkey, Idioma lang) throws ExceptionFrontMicro {
		try {
			DelegateBase _delegateBase = new DelegateBase();
			Microsite ret = _delegateBase.obtenerMicrositebyKey(mkey, lang.getLang());
			ret.setIdi(lang.getLang());
			return ret;

		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	public Microsite getMicrositeByUri(String uri, Idioma lang) throws ExceptionFrontMicro {
		try {
			DelegateBase _delegateBase = new DelegateBase();
			Microsite ret = _delegateBase.obtenerMicrositebyUri(uri, lang.getLang());
			ret.setIdi(lang.getLang());
			return ret;

		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	public TemaFront getTemaFrontByUri(String uri) throws ExceptionFrontPagina {
		try {
	        TemaFrontDelegate tfd = DelegateUtil.getTemaFrontDelegate();
			return tfd.obtenerTemabyUri(uri);
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	public List<ArchivoTemaFront> getArchivoTema(String uriTema, String filename) throws ExceptionFrontPagina {
		try {
	        ArchivoTemaFrontDelegate tfd = DelegateUtil.getArchivoTemaFrontDelegate();
			return tfd.searchByTemaNombre(uriTema, filename);
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
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
			return archi.obtenerArchivobyName(idSite, name);
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}

	}

	public Frqssi getFormularioQssi(Microsite microsite, Idioma lang, long idQssi) throws ExceptionFrontPagina {
		return this.getFormularioQssi(microsite, lang.getLang(), idQssi);
	}


	public Frqssi getFormularioQssi(Microsite microsite, String lang, long idQssi) throws ExceptionFrontPagina {

		try {
			FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
			Frqssi qssi = qssidel.obtenerFrqssi(idQssi);
			qssi.setIdi(lang);
			return qssi;

		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}


}
