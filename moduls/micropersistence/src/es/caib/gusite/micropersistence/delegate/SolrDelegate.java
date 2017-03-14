package es.caib.gusite.micropersistence.delegate;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.solrutiles.solr.model.IndexEncontrado;
import es.caib.gusite.solrutiles.solr.model.IndexResultados;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrSearcher;
import es.caib.solr.api.exception.ExcepcionSolrApi;
import es.caib.solr.api.model.FilterSearch;
import es.caib.solr.api.model.PaginationSearch;
import es.caib.solr.api.model.ResultData;
import es.caib.solr.api.model.StoredData;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;


/**
 * Business delegate para manipular solr.
 * 
 * @author Indra
 */
public class SolrDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = 3017269661850900982L;
	private static Log log = LogFactory.getLog(SolrDelegate.class);
	
	public IndexResultados buscar(String idSession, String idMicro, String idi, Object object, String words,
			boolean b) throws DelegateException, ExcepcionSolrApi {
		
		
		
		final String username = GusitePropertiesUtil.getUserSOLR();
		final String password = GusitePropertiesUtil.getPassSOLR();
		final String index = GusitePropertiesUtil.getIndexSOLR();
		final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
		
		final SolrSearcher buscador = SolrFactory.getSearcher(IndexacionUtil.APLICACION_CALLER_ID,urlSolr, index, username, password);
		
		final FilterSearch filterSearch = new FilterSearch();		
		filterSearch.setElementoRaizCategoria(EnumCategoria.GUSITE_MICROSITE);
		filterSearch.setElementoRaizId(idMicro);
		
		List<EnumAplicacionId> aplicaciones = new ArrayList<EnumAplicacionId>();
		aplicaciones.add(EnumAplicacionId.GUSITE);
		filterSearch.setAplicaciones(aplicaciones);
		final PaginationSearch paginationSearch = new PaginationSearch();
		final ResultData resultadoSolr = buscador.buscar(idSession, words, EnumIdiomas.fromString(idi.toLowerCase()), filterSearch, paginationSearch);
		
		MicrositeDelegate microde = DelegateUtil.getMicrositeDelegate();
		Microsite microsite = microde.obtenerMicrosite(Long.parseLong(idMicro));
		List<IndexEncontrado> listIndex = new ArrayList<IndexEncontrado>();
		final TraduccionMicrosite traduccion = (TraduccionMicrosite) microsite.getTraduccion(idi);
		String desc = traduccion != null ? traduccion.getTitulo() : "";
		NumberFormat numberFormat = NumberFormat.getInstance();
		// trunca a dos digitos
		numberFormat.setMaximumFractionDigits(2);
		// le decimos al NumberFormat que el redondeado sea mediante truncamiento.
		 numberFormat.setRoundingMode(RoundingMode.DOWN);
		  
		for (StoredData res : resultadoSolr.getResultados()) { 
			res.getTitulo().get(EnumIdiomas.fromString(idi));
			String numTrun = numberFormat.format(res.getScore());
			numTrun=numTrun.replace(",", ".");
			boolean disponible = true;
			switch(res.getCategoria()) {
				case GUSITE_AGENDA:
					try {
						AgendaDelegate ageD = new AgendaDelegate();
						Agenda age = ageD.obtenerAgenda(Long.valueOf(res.getElementoId()));
						if(age==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
					}					
					break;
				case GUSITE_ARCHIVO:	
					try {
						String idElemento = res.getElementoId();						
						if (idElemento.contains("_" + idi)) {
							//en este caso es un archivo global, y la busqueda retorna un elemento en este idioma.
							idElemento = idElemento.replace("_" + idi, "");
						}
						
						Long id = Long.valueOf(idElemento);											
						ArchivoDelegate elemD = new ArchivoDelegate();
						Archivo elem = elemD.obtenerArchivo(id);
						if(elem==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
						try {
							if(res.getElementoId().contains("_")){
								log.error("Error, se está obteniendo en la busqueda un archivo comnun de un idioma diferente al actual (leng="+idi+") id: (" + res.getElementoId() + ") " + e.getMessage() );
							}
						} catch (Exception e2) {
							log.error(e.getMessage());
						}												
					}
					break;
				case GUSITE_CONTENIDO:	
					try {
						ContenidoDelegate elemD = new ContenidoDelegate();
						Contenido elem = elemD.obtenerContenido(Long.valueOf(res.getElementoId()));
						if(elem==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
					}
					break;
				case GUSITE_ENCUESTA:	
					try {
						EncuestaDelegate elemD = new EncuestaDelegate();
						Encuesta elem = elemD.obtenerEncuesta(Long.valueOf(res.getElementoId()));
						if(elem==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
					}
					break;
				case GUSITE_FAQ:
					try {
						FaqDelegate elemD = new FaqDelegate();
						Faq elem = elemD.obtenerFaq(Long.valueOf(res.getElementoId()));
						if(elem==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
					}
					break;
				case GUSITE_MICROSITE:	
					try {
						MicrositeDelegate elemD = new MicrositeDelegate();
						Microsite elem = elemD.obtenerMicrosite(Long.valueOf(res.getElementoId()));
						if(elem==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
					}
					break;
				case GUSITE_NOTICIA:	
					try {
						NoticiaDelegate elemD = new NoticiaDelegate();
						Noticia elem = elemD.obtenerNoticia(Long.valueOf(res.getElementoId()));
						if(elem==null){
							disponible = false;
						}
					} catch (Exception e) {
						disponible = false;
					}
					break;
				default:
					break;
			}				
			
			IndexEncontrado ind = new IndexEncontrado(res.getElementoId(), res.getTitulo().get(EnumIdiomas.fromString(idi)), 
					res.getDescripcion().get(EnumIdiomas.fromString(idi)), desc, res.getUrl().get(EnumIdiomas.fromString(idi)), Float.parseFloat(numTrun), disponible);
			listIndex.add(ind);
		}
		
		IndexResultados resultado = new IndexResultados(listIndex, (int)resultadoSolr.getNumResultados(), 0L, words, "", "");
		
		return resultado;
	}

	
}
