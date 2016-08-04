package es.caib.gusite.microfront.faq.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.microfront.estadistica.util.StatManager;
import es.caib.gusite.microfront.faq.bean.Faqtema;
import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;


/**
 * Clase Bdcontenido. Manejador de la petición de listado de faqs.
 * Mete en una lista las faqs.
 * 
 * @author Indra
 *
 */

public class Bdlistafaqs extends Bdbase {
	
	protected static Log log = LogFactory.getLog(Bdlistafaqs.class);
	
	private List<?> listafaqs;
	private ArrayList<Faqtema> listafaqstema = new ArrayList<Faqtema>();
	private Hashtable<Object, Object> parametros = new Hashtable<Object, Object>();
	private boolean error = false;
	private HttpServletRequest req;
	
	/**
	 *  Constructor de la clase, carga ela lista de faqs
	 * @param request
	 * @param form
	 * @throws Exception
	 */
	public Bdlistafaqs(HttpServletRequest request, ActionForm form) throws Exception {
		super(request);
		if ((microsite!=null) && (existeServicio(Microfront.RFAQ))) {
			req=request;
			crearlistado();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}
	}

	/**
	 * Método privado utilizado para crear el listado de faqs
	 */
	private void crearlistado() {
		try {
		        prepararlistado();
		        
				//si llegamos aqui.... todo ok, así que grabamos la estadistica
				if (idsite.longValue()==0) error=true;
				 else if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
					 		else req.getSession().getServletContext().setAttribute("bufferStats", 
									StatManager.grabarestadistica(idsite, super.publico, 
											(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
		        
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de faqs.");
			error=true;
			listafaqs = null;
			parametros = new Hashtable<Object, Object>();
		}

	}
	
	/**
	 * Método que vuelca el listado de faqs obtenido del EJB en una
	 * lista de beans preparada para el jsp
	 *
	 */
	private void prepararlistado() throws Exception {
		TemaDelegate temadel = DelegateUtil.getTemafaqDelegate();
		temadel.init();temadel.setTampagina(Integer.MAX_VALUE);
		temadel.setWhere("where trad.id.codigoIdioma='" + idioma + "' and tema.idmicrosite=" + super.idsite);
		List<?> listatemas=temadel.listarTemas();
		Iterator<?> iter = listatemas.iterator();
		//recorrer los temas
		while (iter.hasNext()) {
				Temafaq tema = (Temafaq)iter.next();
		    	FaqDelegate faqdel = DelegateUtil.getFaqDelegate();
		    	faqdel.init();faqdel.setTampagina(Integer.MAX_VALUE);
		    	faqdel.setWhere("where trad.id.codigoIdioma='" + idioma + "' and faq.visible='S' and faq.tema=" + tema.getId());
		    	List<?> listafaqs=faqdel.listarFaqs();
		    	ArrayList<Faq> tmpfaqs=new ArrayList<Faq>();
		    	Faqtema faqtema = new Faqtema();
		    	Iterator<?> iter2 = listafaqs.iterator();
		    	//recorrer las faqs de cada uno de los temas
				while (iter2.hasNext()) {
		       	 	Faq faq = (Faq)iter2.next();
		       	 	faq.setIdi(idioma);
		       	 	//por si es nulo
		       	 	if  ( ((TraduccionFaq)faq.getTraduccion(idioma)!=null) &&
		       	 		  (((TraduccionFaq)faq.getTraduccion(idioma)).getPregunta()!=null) && 
		       	 		  (((TraduccionFaq)faq.getTraduccion(idioma)).getRespuesta()!=null) ) {
		       	 			tmpfaqs.add(faq);
		       	 	}

				}
				if ((TraduccionTemafaq)tema.getTraduccion(idioma)!=null) faqtema.setTema(((TraduccionTemafaq)tema.getTraduccion(idioma)).getNombre());
				faqtema.setListadopreguntas(tmpfaqs);
				listafaqstema.add(faqtema);
			
		}

	}
	
	public List<?> getListafaqs() {
		return listafaqs;
	}

	public Hashtable<Object, Object> getParametros() {
		return parametros;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public ArrayList<Faqtema> getListafaqstema() {
		return listafaqstema;
	}

	public void setListafaqstema(ArrayList<Faqtema> listafaqstema) {
		this.listafaqstema = listafaqstema;
	}	
	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de faqs.
	 */
	public String setServicio() {
		return Microfront.RFAQ;
	}
	
}
