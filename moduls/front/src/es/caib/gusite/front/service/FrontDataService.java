package es.caib.gusite.front.service;

import java.io.IOException;
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
import es.caib.gusite.front.general.bean.ErrorMicrosite;
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
	public Map<String, Set<Integer>> getDatosCalendario(final Microsite microsite, final Idioma lang)
			throws DelegateException {
		return this.getDatosCalendario(microsite, lang.getLang());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Set<Integer>> getDatosCalendario(final Microsite microsite, final String idioma)
			throws DelegateException {

		// CALENDARIO
		final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setWhere("where trad.id.codigoIdioma='" + idioma + "' and agenda.visible='S' and agenda.idmicrosite="
				+ microsite.getId());
		agendadel.setPagina(1);
		agendadel.setTampagina(Integer.MAX_VALUE);
		final List<Agenda> listagenda = (List<Agenda>) agendadel.listarAgendas();

		GregorianCalendar gc = new GregorianCalendar();
		Set<Integer> diasMesAgenda = new HashSet<Integer>();
		int anyo;
		int mes;

		// Establecemos el valor al día 1
		gc.set(Calendar.DAY_OF_MONTH, 1);

		final Map<String, Set<Integer>> meses = new Hashtable<String, Set<Integer>>();

		final int nummesesaretroceder = 2;
		final int nummeses = 3;
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
	 * Devuelve una coleccion con los dias de un mes determinado en que hay eventos
	 */
	private static Set<Integer> getDiasMesAgenda(final List<Agenda> listaagenda, final GregorianCalendar gc) {

		final Set<Integer> diasMes = new HashSet<Integer>();

		try {

			for (int i = 1; i <= gc.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
				final GregorianCalendar fecha2 = new GregorianCalendar();
				fecha2.setTime(gc.getTime());
				fecha2.set(Calendar.DAY_OF_MONTH, i);
				fecha2.set(Calendar.HOUR_OF_DAY, 0);
				fecha2.set(Calendar.MINUTE, 0);
				final Iterator<?> iter = listaagenda.iterator();
				while (iter.hasNext()) {
					final Agenda agenda = (Agenda) iter.next();
					if (Fechas.between(agenda.getFinicio(), agenda.getFfin(), fecha2.getTime())) {
						diasMes.add(fecha2.get(java.util.Calendar.DAY_OF_MONTH));
					}
				}

			}
		} catch (final Exception e) {
			log.error("[getDiasMesAgenda]: " + e.getMessage());
		}
		return diasMes;
	}

	@SuppressWarnings("unchecked")
	public List<Agenda> getDatosListadoHome(final Microsite microsite, final Idioma lang) throws DelegateException {
		// LISTADO
		final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();

		final GregorianCalendar gc2 = new GregorianCalendar();
		final java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
		agendadel.setWhere(
				"where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite="
						+ microsite.getId() + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
		agendadel.setOrderby2(" order by agenda.finicio asc");
		agendadel.setPagina(1);
		agendadel.setTampagina(3);
		final List<Agenda> ret = (List<Agenda>) agendadel.listarAgendas();
		this.traducelista(ret, lang.getLang());
		return ret;
	}

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Agenda> getListadoAgenda(final Microsite microsite, final Idioma lang, final Date fecha,
			final int pagina) throws DelegateException {
		final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setWhere("where trad.id.codigoIdioma='" + lang.getLang()
				+ "' and agenda.visible='S' and agenda.idmicrosite=" + microsite.getId());
		agendadel.setOrderby2(" ORDER BY agenda.actividad, NVL(agenda.ffin,SYSDATE)");

		// Indicamos la página a visualizar
		agendadel.setPagina(pagina);
		final List<Agenda> listaeventos = (List<Agenda>) agendadel.listarAgendas(fecha, lang.getLang());
		this.traducelista(listaeventos, lang.getLang());
		final ResultadoBusqueda<Agenda> ret = new ResultadoBusqueda<Agenda>(listaeventos,
				(Map<String, Integer>) agendadel.getParametros());

		return ret;
	}

	public Agenda loadAgenda(final Long idCont, final Idioma lang) throws ExceptionFrontPagina {

		try {
			final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
			final Agenda ret = agendadel.obtenerAgenda(idCont);
			ret.setIdi(lang.getLang());
			if (ret.getActividad() != null) {
				ret.getActividad().setIdi(lang.getLang());
			}
			return ret;
		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(" [loadAgenda, id=" + idCont + ", idioma=" + lang.getLang() + " ] Error="
					+ e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}

	private void traducelista(final List<Agenda> listaeventos, final String lang) {
		for (final Agenda agenda : listaeventos) {
			agenda.setIdi(lang);
			if (agenda.getActividad() != null) {
				agenda.getActividad().setIdi(lang);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Agenda> getListadoAgenda(final Microsite microsite, final Idioma lang,
			final AgendaCriteria criteria) throws DelegateException {
		final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		agendadel.init();
		agendadel.setTampagina(3); // TODO: esto está hardcoded y esmuy pequeño
		GregorianCalendar gc = new GregorianCalendar();
		// retrocedemos 2 meses
		gc = Fechas.anteriorMes(gc);
		gc = Fechas.anteriorMes(gc);

		final GregorianCalendar gc2 = new GregorianCalendar();
		final java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
		agendadel.setWhere(
				"where trad.id.codigoIdioma='" + lang.getLang() + "' and agenda.visible='S' and agenda.idmicrosite="
						+ microsite.getId() + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
		agendadel.setOrderby2(" order by agenda.finicio asc");

		if (criteria.getFiltro() != null && criteria.getFiltro().length() > 0) {
			agendadel.setFiltro(criteria.getFiltro());
		}

		if (criteria.getOrdenacion() != null && criteria.getOrdenacion().length() > 0) {
			agendadel.setOrderby(criteria.getOrdenacion());
		}

		agendadel.setPagina(criteria.getPagina());

		final List<Agenda> listaeventos = (List<Agenda>) agendadel.listarAgendas(lang.getLang());
		this.traducelista(listaeventos, lang.getLang());
		final ResultadoBusqueda<Agenda> ret = new ResultadoBusqueda<Agenda>(listaeventos,
				(Map<String, Integer>) agendadel.getParametros());
		return ret;
	}

	public List<Noticia> getNoticiasHome(final Microsite site, final Idioma lang) throws DelegateException {
		return this.getNoticiasHome(site, lang.getLang());
	}

	@SuppressWarnings("unchecked")
	public List<Noticia> getNoticiasHome(final Microsite microsite, final String idioma) throws DelegateException {
		int noticias = 3;
		if (microsite.getNumeronoticias() != 0) {
			noticias = microsite.getNumeronoticias();
		}

		final NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		noticiadel.init();
		noticiadel.setPagina(1);
		noticiadel.setTampagina(noticias);
		final java.sql.Date dt = new java.sql.Date((new Date()).getTime());
		String wherenoticias = "where noti.visible = 'S' and noti.idmicrosite = " + microsite.getId();
		wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
		wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
		wherenoticias += " and (noti.tipo.tipoelemento = " + Front.ELEM_NOTICIA + " )";
		noticiadel.setWhere(wherenoticias);
		noticiadel.setOrderby2(" order by noti.fpublicacion desc");

		final List<Noticia> ret = noticiadel.listarNoticiasThin(idioma);
		for (final Noticia n : ret) {
			n.setIdi(idioma);
			if (n.getTipo() != null) {
				n.getTipo().setIdi(idioma);
			}
		}
		return ret;
	}

	public List<Faqtema> listarFaqs(final Microsite microsite, final Idioma lang) throws DelegateException {
		final TemaDelegate temadel = DelegateUtil.getTemafaqDelegate();
		temadel.init();
		temadel.setTampagina(Integer.MAX_VALUE);
		temadel.setWhere(
				"where trad.id.codigoIdioma='" + lang.getLang() + "' and tema.idmicrosite=" + microsite.getId());
		final List<?> listatemas = temadel.listarTemas();
		final Iterator<?> iter = listatemas.iterator();
		final List<Faqtema> listafaqstema = new ArrayList<Faqtema>();
		// recorrer los temas
		while (iter.hasNext()) {
			final Temafaq tema = (Temafaq) iter.next();
			final FaqDelegate faqdel = DelegateUtil.getFaqDelegate();
			faqdel.init();
			faqdel.setTampagina(Integer.MAX_VALUE);
			faqdel.setWhere("where trad.id.codigoIdioma='" + lang.getLang() + "' and faq.visible='S' and faq.tema="
					+ tema.getId());
			final List<?> listafaqs = faqdel.listarFaqs();
			final ArrayList<Faq> tmpfaqs = new ArrayList<Faq>();
			final Faqtema faqtema = new Faqtema();
			final Iterator<?> iter2 = listafaqs.iterator();
			// recorrer las faqs de cada uno de los temas
			while (iter2.hasNext()) {
				final Faq faq = (Faq) iter2.next();
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

	public Microsite getMicrosite(final Long idSite, final Idioma lang) throws ExceptionFrontMicro {
		try {
			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			final Microsite ret = micrositedel.obtenerMicrosite(idSite);
			if (ret != null) {
				ret.setIdi(lang.getLang());
			}
			return ret;

		} catch (final DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	public Microsite getMicrositeByKey(final String mkey, final Idioma lang) throws ExceptionFrontMicro {
		try {
			final DelegateBase _delegateBase = new DelegateBase();
			final Microsite ret = _delegateBase.obtenerMicrositebyKey(mkey, lang.getLang());
			ret.setIdi(lang.getLang());
			return ret;

		} catch (final DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	public Microsite getMicrositeByUri(final String uri, final Idioma lang) throws ExceptionFrontMicro {
		try {
			final DelegateBase _delegateBase = new DelegateBase();
			final Microsite ret = _delegateBase.obtenerMicrositebyUri(uri, lang.getLang());
			if (ret != null) {
				ret.setIdi(lang.getLang());
			}
			return ret;

		} catch (final DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	public TemaFront getTemaFrontByUri(final String uri) throws ExceptionFrontPagina {
		try {
			final TemaFrontDelegate tfd = DelegateUtil.getTemaFrontDelegate();
			return tfd.obtenerTemabyUri(uri);
		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	public List<ArchivoTemaFront> getArchivoTema(final String uriTema, final String filename)
			throws ExceptionFrontPagina {
		try {
			final ArchivoTemaFrontDelegate tfd = DelegateUtil.getArchivoTemaFrontDelegate();
			return tfd.searchByTemaNombre(uriTema, filename);
		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	public Archivo obtenerArchivo(final Long id,Boolean visualizar,String uri) throws ExceptionFrontPagina {
		try {
			boolean comprobar = false;
			final ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			Archivo archivo = archi.obtenerArchivo(id);
			if(archivo!=null) {
			 comprobar = esComprobacion(archivo.getNombre());
		    }else {
		    	log.error("Archivo  no disponible");
				throw new ExceptionFrontPagina(ErrorMicrosite.ERROR_DOCU_MSG +":"+uri);
		    }
			if(!visualizar && comprobar) {
			if (!archi.visible(id)) {
				log.error("Archivo  no disponible");
				throw new ExceptionFrontPagina(ErrorMicrosite.ERROR_DOCU_MSG +":"+uri);
				//throw new ExceptionFrontPagina(ErrorMicrosite.ERROR_DOCU_MSG);
			}
			}
			//return archi.obtenerArchivo(id);
			return archivo;
		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	public byte[] obtenerContenidoArchivo(final Archivo archivo) throws ExceptionFrontPagina, IOException {
		try {
			final ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			return archi.obtenerContenidoFichero(archivo);
		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	public Archivo obtenerArchivo(final Long idSite, final String name) throws ExceptionFrontPagina {
		try {
			final ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			return archi.obtenerArchivobyName(idSite, name);
		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}

	}

	public Frqssi getFormularioQssi(final Microsite microsite, final Idioma lang, final long idQssi)
			throws ExceptionFrontPagina {
		return this.getFormularioQssi(microsite, lang.getLang(), idQssi);
	}

	public Frqssi getFormularioQssi(final Microsite microsite, final String lang, final long idQssi)
			throws ExceptionFrontPagina {

		try {
			final FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
			final Frqssi qssi = qssidel.obtenerFrqssi(idQssi);
			qssi.setIdi(lang);
			return qssi;

		} catch (final DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	private boolean esComprobacion(String nombre) {
		String ext = "";
		boolean comprobar=false;
		int i = nombre.lastIndexOf('.');
		if (i > 0) {
			ext = nombre.substring(i+1).toUpperCase();
		}


		if(ext.equals("PDF") || ext.equals("DOC") || ext.equals("DOCX") || ext.equals("XLS") || ext.equals("XLSX") || ext.equals("ODT")) {
			comprobar =  true;
		}

		return comprobar;


	}
}
