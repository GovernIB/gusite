package es.caib.gusite.microfront.cercador.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.solrutiles.solr.model.IndexEncontrado;
import es.caib.gusite.solrutiles.solr.model.IndexResultados;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrSearcher;
import es.caib.solr.api.model.FilterSearch;
import es.caib.solr.api.model.PaginationSearch;
import es.caib.solr.api.model.ResultData;
import es.caib.solr.api.model.StoredData;
import es.caib.solr.api.model.types.EnumIdiomas;

/**
 * Clase Bdcercador. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdcercador extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdcercador.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private IndexResultados resultado;
    
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables
	 */
	public void dispose() {
		resultado = null;
		req = null;
		super.dispose();
	}

	/**
	 * Constructor de la clase. Carga los datos a partir de la request.
	 * @param request
	 * @throws Exception
	 */
	public Bdcercador(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if ((microsite!=null) && (microsite.getBuscador().equals("S"))) {
			buscar();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}			
	}

	/**
	 * Implementacion del método abstracto.
	 * Da igual qué servicio es. 
	 */
	public String setServicio() {
		return Microfront.RCONTENIDO;
	}	
	
	/**
	 * Método privado, realizar la búsqueda.
	 */
	private void buscar() {
		
		try {
			
			final String username = GusitePropertiesUtil.getUserSOLR();
			final String password = GusitePropertiesUtil.getPassSOLR();
			final String index = GusitePropertiesUtil.getIndexSOLR();
			final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
			
			final SolrSearcher buscador = SolrFactory.getSearcher(urlSolr, index, username, password);
			
			//IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
			final String words = "" + req.getParameter("cerca");
			final String idi = "" + req.getSession().getAttribute("MVS_idioma");
			
			final FilterSearch filterSearch = new FilterSearch();
			filterSearch.setMicrositeId(microsite.getId().toString());
			final PaginationSearch paginationSearch = new PaginationSearch();
			final ResultData resultadoSolr = buscador.buscar(req.getSession().getId(), words, EnumIdiomas.fromString(idi.toLowerCase()), filterSearch, paginationSearch);
			convertirResultado(resultadoSolr, idi.toLowerCase(),words);
		} catch (Exception e) {
			
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", e.getMessage());
			error = true;
			
		}
		
	}

	/**
	 * Convierte resultadoSolr en IndexResultados.
	 * @param resultadoSolr
	 * @param idi 
	 */
	private void convertirResultado(ResultData resultadoSolr, String idi, String busqueda) {
		// No se implementan consulta sugerida,salto ni score
		resultado = new IndexResultados(prepararLista(resultadoSolr.getResultados(),idi), (int) resultadoSolr.getNumResultados(), 0L, busqueda, "", "");
		resultado.setNumEncontrados((int) resultadoSolr.getNumResultados());
	}

	/**
	 * Informa la lista resultado a partir de storedData
	 * @param resultados
	 * @param idi idioma
	 * @return
	 */
	private List<IndexEncontrado> prepararLista(List<StoredData> resultados, String idi) {
		List<IndexEncontrado> listIndex = new ArrayList<IndexEncontrado>();
		final TraduccionMicrosite traduccion = (TraduccionMicrosite) microsite.getTraduccion(idi);
		String desc = traduccion != null ? traduccion.getTitulo() : "";
		for (StoredData res : resultados) { 
			res.getTitulo().get(EnumIdiomas.fromString(idi));
			IndexEncontrado index = new IndexEncontrado(res.getElementoId(), res.getTitulo().get(EnumIdiomas.fromString(idi)), 
					res.getDescripcion().get(EnumIdiomas.fromString(idi)), desc, res.getUrl().get(EnumIdiomas.fromString(idi)), 0);
			listIndex.add(index);
		}
		
		return listIndex;
		
	}

	/**
	 * Devuelve true o false en función de si hay error.
	 * @return Devuelve true o false en función de si hay error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Devuelve el resultado de la búsqueda
	 * @return IndexResultado
	 */
	public IndexResultados getResultado() {
		return resultado;
	}
	
}
