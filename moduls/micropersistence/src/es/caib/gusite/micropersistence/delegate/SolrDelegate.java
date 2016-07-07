package es.caib.gusite.micropersistence.delegate;

import java.util.ArrayList;
import java.util.List;

import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;
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

	public IndexResultados buscar(String idMicro, String idi, Object object, String words,
			boolean b) throws DelegateException, ExcepcionSolrApi {
		
		
		
		final String username = GusitePropertiesUtil.getUserSOLR();
		final String password = GusitePropertiesUtil.getPassSOLR();
		final String index = GusitePropertiesUtil.getIndexSOLR();
		final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
		
		final SolrSearcher buscador = SolrFactory.getSearcher(urlSolr, index, username, password);
		
		final FilterSearch filterSearch = new FilterSearch();
		filterSearch.setMicrositeId(idMicro);
		final PaginationSearch paginationSearch = new PaginationSearch();
		final ResultData resultadoSolr = buscador.buscar(words, EnumIdiomas.fromString(idi.toLowerCase()), filterSearch, paginationSearch);
		
		MicrositeDelegate microde = DelegateUtil.getMicrositeDelegate();
		Microsite microsite = microde.obtenerMicrosite(Long.parseLong(idMicro));
		List<IndexEncontrado> listIndex = new ArrayList<IndexEncontrado>();
		final TraduccionMicrosite traduccion = (TraduccionMicrosite) microsite.getTraduccion(idi);
		String desc = traduccion != null ? traduccion.getTitulo() : "";
		for (StoredData res : resultadoSolr.getResultados()) { 
			res.getTitulo().get(EnumIdiomas.fromString(idi));
			IndexEncontrado ind = new IndexEncontrado(res.getElementoId(), res.getTitulo().get(EnumIdiomas.fromString(idi)), 
					res.getDescripcion().get(EnumIdiomas.fromString(idi)), desc, res.getUrl().get(EnumIdiomas.fromString(idi)), 15);
			listIndex.add(ind);
		}
		
		IndexResultados resultado = new IndexResultados(listIndex, (int)resultadoSolr.getNumResultados(), 0L, words, "", "");
		
		return resultado;
	}

	
}
