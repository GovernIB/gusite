package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;

import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;

/**
 * SessionBean para consultar Faq.
 *
 * @ejb.bean
 *  name="sac/micropersistence/FaqFacade"
 *  jndi-name="es.caib.gusite.micropersistence.FaqFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class FaqFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -836117598281319916L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        try {
        	super.langs= DelegateUtil.getIdiomaDelegate().listarLenguajes();
        } catch(Exception ex) {
        	throw new EJBException(ex);
        }
    }

    /**
     * Inicializo los parámetros de la consulta de Faq.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site) {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "select faq";
    	super.from = " from Faq faq join faq.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and faq.idmicrosite = " + site.toString();
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"trad.pregunta", "trad.respuesta", "trad.url", "trad.urlnom"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }        
 
    /**
     * Inicializo los parámetros de la consulta de Faq.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "select faq";
    	super.from = " from Faq faq join faq.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"trad.pregunta", "trad.respuesta", "trad.url", "trad.urlnom"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }      
    
    /**
     * Crea o actualiza una faq
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarFaq(Faq faq) throws DelegateException {

        Session session = getSession();
        try {
            boolean nuevo = (faq.getId() == null) ? true : false;
         	Transaction tx = session.beginTransaction();
            this.microsite = (Microsite) session.get(Microsite.class, faq.getIdmicrosite());

            Map<String, TraduccionFaq> listaTraducciones = new HashMap<String, TraduccionFaq>();
            if (nuevo) {
                Iterator<TraduccionFaq> it = faq.getTraducciones().values().iterator();
                while (it.hasNext()) {
                    TraduccionFaq trd = it.next();
                    listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
                }
                faq.setTraducciones(null);
            }

            session.saveOrUpdate(faq);
            session.flush();

            if (nuevo) {
                for (TraduccionFaq trad : listaTraducciones.values()) {
                    trad.getId().setCodigoFaq(faq.getId());
                    session.saveOrUpdate(trad);
                }
                session.flush();
                faq.setTraducciones(listaTraducciones);
            }

            tx.commit();
            close(session);

            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            gravarAuditoria(Faq.class.getSimpleName(), faq.getId().toString(), op);

            return faq.getId();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una faq
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Faq obtenerFaq(Long id) {

        Session session = getSession();
        try {
        	Faq faq = (Faq) session.get(Faq.class, id);
            return faq;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todas las faqs
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarFaqs() {

        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
        	Query query = session.createQuery(select + from + where + orderby);
            query.setFirstResult(cursor - 1);
            query.setMaxResults(tampagina);
        	return query.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Lista todas las faqs poniendole un idioma por defecto
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public ArrayList<Faq> listarFaqs(String idioma) {

        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
        	Query query = session.createQuery(select + from + where + orderby);
            query.setFirstResult(cursor - 1);
            query.setMaxResults(tampagina);
            return crearlistadostateful(query.list(),idioma);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * borra una faq
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarFaq(Long id) throws DelegateException {

        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Faq faq = (Faq) session.get(Faq.class, id);
            this.microsite = (Microsite) session.get(Microsite.class, faq.getIdmicrosite());

            session.createQuery("delete from TraduccionFaq tfaq where tfaq.id.codigoFaq = " + id).executeUpdate();
            session.createQuery("delete from Faq faq where faq.id = " + id).executeUpdate();
            session.flush();
            tx.commit();
            close(session);

            gravarAuditoria(Faq.class.getSimpleName(), id.toString(), Auditoria.ELIMINAR);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * metodo privado que devuelve un arraylist 'nuevo'.
     * Vuelca el contenido del listado e inicializa el idioma del bean.
     * @param listado
     * @param idioma
     * @return
     */
    private ArrayList<Faq> crearlistadostateful(List<?> listado, String idioma) {

        ArrayList<Faq> lista = new ArrayList<Faq>();
        Iterator<?> iter = listado.iterator();
        Faq faq;
        while (iter.hasNext()) {
            faq = new Faq();
            faq = (Faq)iter.next();
            faq.setIdi(idioma);
            lista.add(faq);
        }
        return lista;
    }

    /**
     * Comprueba que el elemento pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {

        Session session = getSession();
        try {
        	Query query = session.createQuery("from Faq faq where faq.idmicrosite = " + site.toString() + " and faq.id = " + id.toString());
        	return query.list().isEmpty();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Añade la faq al indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void indexInsertaFaqs(Long idsite, ModelFilterObject filter) {

        IndexObject io = new IndexObject();
        try {
            if (filter == null) {
                filter = DelegateUtil.getMicrositeDelegate().obtenerFilterObject(idsite);
            }

            if (filter != null && filter.getBuscador().equals("N")) {
			    return;
			}

            IndexerDelegate indexerDelegate = DelegateUtil.getIndexerDelegate();
			for (int i = 0; i < langs.size(); i++) {
                String idioma = (String) langs.get(i);
				io = new IndexObject();

				// Configuración del writer
                Directory directory = indexerDelegate.getHibernateDirectory(idioma);
                IndexWriter writer = new IndexWriter(directory, Analizador.getAnalizador(idioma), false, MaxFieldLength.UNLIMITED);
                writer.setMergeFactor(20);
                writer.setMaxMergeDocs(Integer.MAX_VALUE);

                try {
                    io.setId(Catalogo.SRVC_MICRO_FAQS + "." + idsite);
                    io.setClasificacion(Catalogo.SRVC_MICRO_FAQS);

                    io.setMicro(filter.getMicrosite_id());
                    io.setRestringido(filter.getRestringido());
                    io.setUo(filter.getUo_id());
                    io.setMateria(filter.getMateria_id());
                    io.setSeccion(filter.getSeccion_id());
                    io.setFamilia(filter.getFamilia_id());

                    io.setTitulo("Faqs");
                    io.setUrl("/sacmicrofront/faqs.do?lang=" + idioma + "&idsite=" + idsite.toString());
                    io.setCaducidad("");
                    io.setPublicacion("");
                    io.setDescripcion("");
                    io.setTituloserviciomain(filter.getTraduccion(idioma).getMaintitle());

                    Iterator<?> iter = listarFaqs().iterator();
                    while (iter.hasNext()) {
                        Faq faq = (Faq) iter.next();
                        TraduccionFaq trad = ((TraduccionFaq) faq.getTraduccion(idioma));
                        if (trad != null) {
                            io.addTextLine(trad.getPregunta());
                            io.addTextLine(trad.getRespuesta());
                        }
                    }

                    io.addTextopcionalLine(filter.getTraduccion(idioma).getMateria_text());
                    io.addTextopcionalLine(filter.getTraduccion(idioma).getSeccion_text());
                    io.addTextopcionalLine(filter.getTraduccion(idioma).getUo_text());

                    if (io.getText().length() > 0) {
                        indexerDelegate.insertaObjeto(io, idioma, writer);
                    }
                } finally {
                    writer.close();
                    directory.close();
                }
			}

		} catch (DelegateException ex) {
		    throw new EJBException(ex);
		} catch (Exception e) {
		    log.warn("[indexInsertaFaqs: ] No se ha podido indexar elemento. ");
		}
	}

    /**
     * Elimina la faq en el indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public void indexBorraFaqs(Long idsite)  {

        try {
            for (int i = 0; i < langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(Catalogo.SRVC_MICRO_FAQS + "." +idsite, ""+langs.get(i));
			}
		} catch (DelegateException ex) {
			throw new EJBException(ex);
		}
	}

}
