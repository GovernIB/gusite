package es.caib.gusite.micropersistence.delegate;

import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import es.caib.gusite.lucene.model.IndexResultados;
import es.caib.gusite.micromodel.IndexObject;
import es.caib.gusite.micropersistence.intf.IndexerFacadeLocal;
import es.caib.gusite.micropersistence.intf.IndexerFacadeLocalHome;
import es.caib.gusite.micropersistence.util.IndexerFacadeUtil;
import es.caib.rolsac.api.v2.exception.QueryServiceException;

/**
 * Business delegate para manipular el indice.
 * @author Indra
 */
public class IndexerDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 135604472297642298L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

    /**
     * Indexa un objeto documento en un idioma
     * @param indexObject
     * @param idi
     * @throws DelegateException
     */
    public synchronized void insertaObjeto(IndexObject indexObject, String idioma, IndexWriter writer) throws DelegateException {
        try {
            local.insertaObjeto(indexObject, idioma, writer);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }

    /**
     *  Borra un objeto documento en un idioma
     * @param id
     * @param idi
     * @throws DelegateException
     */
    public synchronized void borrarObjeto(String id, String idi) throws DelegateException {
        try {
            local.borrarObjeto(id, idi);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Crea o actualiza un documento en el indexador
     * @param objeto
     * @throws DelegateException
     */
    public synchronized void indexarObjeto(Object objeto) throws DelegateException {
        try {
            local.indexarObjeto(objeto);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Quita un documento en el indexador
     * @param objeto
     * @throws DelegateException
     */
    public synchronized void desindexarObjeto(Object objeto) throws DelegateException {
        try {
            local.desindexarObjeto(objeto);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }    
    
    /**
     *  Crea o actualiza el diccionario
     * @param idi
     * @throws DelegateException
     */
    public synchronized void confeccionaDiccionario(String idi) throws DelegateException {
        try {
            local.confeccionaDiccionario(idi);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }
         
    /**
     * Deseindexa todo un microsite
     * @param idsite
     * @throws DelegateException
     */
    public void desindexarMicrosite(Long idsite) throws DelegateException {
        try {
        	local.desindexarMicrosite(idsite);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }    
    
    /**
     * Busca documentos en un microsite para un idioma concreto, con opción de sugerir
     * @param micro
     * @param idi
     * @param idlista
     * @param cadena
     * @param sugerir
     * @return IndexResultados
     * @throws DelegateException
     */
    public IndexResultados buscar(String micro, String idi, String idlista, String cadena, boolean sugerir) throws DelegateException {
        try {
            return local.buscar( micro, idi, idlista, cadena, sugerir);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }
   
    /**
     * Retorna el diccionario del campo CAMPO_BUSQUEDAS
     * @param idi
     * @return ArrayList
     * @throws DelegateException
     */
    public synchronized ArrayList<?> diccionario(String idi) throws DelegateException {
        try {
            return local.diccionario(idi);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Re-indexa un microsite completo.
     * @param idsite
     * @throws DelegateException
     * @throws QueryServiceException 
     */
    public synchronized void reindexarMicrosite(Long idsite) throws DelegateException {
        try {
            local.reindexarMicrosite(idsite);
        } catch (EJBException e) {
            throw new DelegateException(e);
        } catch (QueryServiceException e) {
            throw new DelegateException(e);
        }
    }

    public boolean isBloqueado() throws DelegateException {
	   	try {
	   		return local.isBloqueado();
	       } catch (EJBException e) {
	           throw new DelegateException(e);
	       }
	}   
   
   public void setBloqueado(boolean bloqueado) throws DelegateException {
	   	try {
	   		local.setBloqueado(bloqueado);
	       } catch (EJBException e) {
	           throw new DelegateException(e);
	       }
	}       

   public Directory getHibernateDirectory(String idi) throws DelegateException {
       try {
           return local.getHibernateDirectory(idi);
       } catch (EJBException e) {
           throw new DelegateException(e);
       }
   }


    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private IndexerFacadeLocal local;

    protected IndexerDelegate() throws DelegateException {
        try {
            IndexerFacadeLocalHome home = IndexerFacadeUtil.getLocalHome();
            local = home.create();

        } catch (NamingException e) {
            throw new DelegateException(e);
        } catch (CreateException e) {
            throw new DelegateException(e);
        } catch (EJBException e) {
            throw new DelegateException(e);
        }
    }

}
