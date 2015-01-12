package es.caib.gusite.micropersistence.ejb;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.hibernate.Constants;
import es.caib.gusite.lucene.hibernate.HibernateDirectory;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.IndexEncontrado;
import es.caib.gusite.lucene.model.IndexResultados;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.IndexObject;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.rolsac.api.v2.exception.QueryServiceException;

/**
 * SessionBean para indexar Microsites.
 *
 * @ejb.bean
 *  name="sac/micropersistence/IndexerFacade"
 *  jndi-name="es.caib.gusite.micropersistence.IndexerFacade"
 *  type="Stateless"
 *  view-type="local"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class IndexerFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 6932846669646730684L;

	protected static Log log = LogFactory.getLog(IndexerFacadeEJB.class);

    public static final String FILESYSTEM_INDEX_TYPE = "filesystem";
    public static final String HIBERNATE_INDEX_TYPE = "hibernate";
    public static final int MIN_HITS = 1;
    public static final int MAX_HITS = 100;
    public static final double MIN_SCORE = 0.20;
    public static final String CAMPO_BUSQUEDAS = "text";
    public static final String OPER_ADDDOCUMENT = "addDocument";
    public static final String OPER_DICCIONARIO = "diccionario";
    public static final String OPER_OPTIMIZAR = "optimizar";

    private boolean bloqueado=false;

    private Analyzer analyzer;

    /**
     * Tipo de directorio Lucene
     * @ejb.env-entry value="${index.type}"
     */
    protected String indexType;

    /**
     * Ubicación del directorio de Lucene.
     * @ejb.env-entry value="${index.location}"
     */
    protected String indexLocation;

    /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
		log.info("Creando ejb:" + this);    	
        super.ejbCreate();
    }

    /**
     * Obtiene un booleano que indica si se esta haciendo alguna importacion
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public boolean isBloqueado() {
		return bloqueado;
	}

    /**
     * Establece si se esta importando o no.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public void setBloqueado(boolean importando) {
		this.bloqueado = importando;
	}

    /**
     * Indexa un objeto documento en un idioma
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void insertaObjeto(IndexObject indexObject, String idi, IndexWriter writer) {

    	try {
            if (indexObject != null) {
                if (indexObject.getTitulo() != null && indexObject.getTitulo().length() > 0) {
                    addDocumentoAlWriter(writer, indexObject, idi);
                }
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Borra un objeto documento en un idioma
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void borrarObjeto(String id, String idi) {

        try {
            Directory directory = getHibernateDirectory(idi);
            IndexReader reader = IndexReader.open(directory, false);
            try {
                reader.deleteDocuments(new Term(Catalogo.DESCRIPTOR_ID, id));
            } finally {
                try {
                    reader.close();
                    directory.close();
                } catch (IOException ex) {
                    throw new IOException(ex);
                }
            }

        } catch (IOException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Crea o actualiza un documento en el indexador
     * @throws DelegateException  
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public void indexarObjeto(Object objeto) throws DelegateException {

    	if (isBloqueado()) {
    		log.warn("Deshabilitada la indexacion");

    	} else {
	    	if (objeto instanceof Agenda) {
	    		AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
				bdAgenda.indexBorraAgenda(((Agenda) objeto).getId());
	           	bdAgenda.indexInsertaAgenda((Agenda) objeto, null);
	    	}

	    	if (objeto instanceof Noticia) {
	    		NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
				bdNoticia.indexBorraNoticia(((Noticia) objeto).getId());
	           	bdNoticia.indexInsertaNoticia((Noticia) objeto, null);
	    	}

	    	if (objeto instanceof Encuesta) {
	    		EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
				bdEncuesta.indexBorraEncuesta(((Encuesta) objeto).getId());
	           	bdEncuesta.indexInsertaEncuesta((Encuesta) objeto, null);
	    	}

	    	if (objeto instanceof Contenido) {
	    		ContenidoDelegate bdContenido = DelegateUtil.getContenidoDelegate();
				bdContenido.indexBorraContenido(((Contenido) objeto).getId());
	           	bdContenido.indexInsertaContenido((Contenido) objeto, null);
	    	}

	    	if (objeto instanceof Faq) {
	    		FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
	    		bdFaq.init(((Faq) objeto).getIdmicrosite());
				bdFaq.indexBorraFaqs(((Faq) objeto).getIdmicrosite());
	           	bdFaq.indexInsertaFaqs(((Faq) objeto).getIdmicrosite(), null);
	    	}
    	}
    }

    /**
     * Quita un documento en el indexador
     * @throws DelegateException 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public void desindexarObjeto(Object objeto) throws DelegateException {

        if (!isBloqueado()) {
            if (objeto instanceof Agenda) {
	    		AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
				bdAgenda.indexBorraAgenda(((Agenda) objeto).getId());
	    	}

	    	if (objeto instanceof Noticia) {
	    		NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
				bdNoticia.indexBorraNoticia(((Noticia) objeto).getId());
	    	}

	    	if (objeto instanceof Encuesta) {
	    		EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
				bdEncuesta.indexBorraEncuesta(((Encuesta) objeto).getId());
	    	}

	    	if (objeto instanceof Contenido) {
	    		ContenidoDelegate bdContenido = DelegateUtil.getContenidoDelegate();
				bdContenido.indexBorraContenido(((Contenido) objeto).getId());
	    	}

	    	if (objeto instanceof Faq) {
	    		FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
	    		bdFaq.init(((Faq) objeto).getIdmicrosite());
				bdFaq.indexBorraFaqs(((Faq) objeto).getIdmicrosite());
	           	bdFaq.indexInsertaFaqs(((Faq) objeto).getIdmicrosite(), null);
	    	}
    	}
    }

    /**
     * Optimiza el indice de busquedas
     * @throws IOException 
     * @throws LockObtainFailedException 
     * @throws CorruptIndexException 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public void optimizar(List<String> langs) throws IOException {

        for (String lang : langs) {
            Directory directory = getHibernateDirectory(lang);
            IndexWriter writer = new IndexWriter(directory, Analizador.getAnalizador(lang), MaxFieldLength.UNLIMITED);
            try {
                writer.setMergeFactor(20);
                writer.setMaxMergeDocs(Integer.MAX_VALUE);
                optimizarIndice(writer);
            } finally {
                writer.close();
                directory.close();
            }
        }
    }

    /**
     * Crea o actualiza el diccionario
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public void confeccionaDiccionario(String idioma) {

        actualizarDiccionario(idioma);
    }

    /**
     * Deseindexa todo un microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void desindexarMicrosite(Long idsite) {

        try {
        	List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
        	for (int i = 0; i < langs.size(); i++) {
	        	Directory directory = getHibernateDirectory("" + langs.get(i));
	            IndexReader reader = IndexReader.open(directory, false);
                try {
                    reader.deleteDocuments(new Term(Catalogo.DESCRIPTOR_MICRO, "" + idsite.longValue()));
                } finally {
                    try {
                        reader.close();
                        directory.close();
                    } catch (IOException ex) {
                        throw new IOException(ex);
                    }
                }
        	}

        } catch (IOException e) {
            throw new EJBException(e);
        } catch (DelegateException de) {
            throw new EJBException(de);
        }
    }

    /**
     * Busca documentos en un microsite para un idioma concreto, con opción de sugerir
     * en caso de haber encontrado nada interesante.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public IndexResultados buscar(String micro, String idioma, String idlista, String cadena, boolean sugerir) {

    	long startTime = System.currentTimeMillis();
        try {
            idioma = idioma.toLowerCase();
            Directory directory = getHibernateDirectory(idioma);
            IndexSearcher searcher = new IndexSearcher(directory);

            BooleanQuery queryOR = new BooleanQuery();
            BooleanQuery queryAND = new BooleanQuery();
            IndexResultados res = null;

            if (cadena != null && cadena.length() > 0) {
                Query query_compuestaAND = null;
            	Query query_compuestaOR = null;

            	Vector<String> words = getWords(cadena, CAMPO_BUSQUEDAS, idioma);

    			if (words.size() > 1) {
    				Query[] expandedQueries = new Query[words.size()];
    				query_compuestaAND = new PhraseQuery();
    				query_compuestaOR = new PhraseQuery();
    				((PhraseQuery) query_compuestaAND).setSlop(5);
    				((PhraseQuery) query_compuestaOR).setSlop(15);

    				for (int i = 0; i < words.size(); i++) {
    					((PhraseQuery) query_compuestaAND).add(new Term(CAMPO_BUSQUEDAS, (String) words.elementAt(i)));
    					((PhraseQuery) query_compuestaOR).add(new Term(CAMPO_BUSQUEDAS, (String) words.elementAt(i)));
    					expandedQueries[i] = new TermQuery(new Term(CAMPO_BUSQUEDAS, (String) words.elementAt(i)));
    				}
    				query_compuestaOR = query_compuestaOR.combine(expandedQueries); //combinarlas entre si. Es equivalente a un OR de todo con todo    				

    			} else if (words.size() == 1) {
    				query_compuestaAND = new TermQuery(new Term(CAMPO_BUSQUEDAS, (String) words.elementAt(0)));
    				query_compuestaOR = new TermQuery(new Term(CAMPO_BUSQUEDAS, (String) words.elementAt(0)));

    			} else if (words.size() == 0) {
    			    return new IndexResultados(null, 0, 0, cadena, "", null );
    			}

    			queryAND.add(query_compuestaAND, BooleanClause.Occur.MUST);
    			queryOR.add(query_compuestaOR, BooleanClause.Occur.MUST);

            } else {
            	return new IndexResultados(null, 0, 0, cadena, "", null );
            }

            if (micro != null && micro.length() > 0) {
            	queryAND.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_MICRO,micro)), BooleanClause.Occur.MUST );
            	queryOR.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_MICRO,micro)), BooleanClause.Occur.MUST );
            }

            if (idlista != null && idlista.length() > 0) {
            	queryAND.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_CLASIFICACION, Catalogo.SRVC_MICRO_ELEMENTOS + "." + idlista)), BooleanClause.Occur.MUST);
            	queryOR.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_CLASIFICACION, Catalogo.SRVC_MICRO_ELEMENTOS + "." + idlista)), BooleanClause.Occur.MUST);
            }

            // Evitamos los documentos caducados o que no han sido publicados aun
            String hoy = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());

            TermRangeQuery publica = new TermRangeQuery(Catalogo.DESCRIPTOR_PUBLICACION, "00000000", hoy, true, true);
            queryAND.add(publica, BooleanClause.Occur.MUST);
            queryOR.add(publica, BooleanClause.Occur.MUST);

            TermRangeQuery caduca = new TermRangeQuery(Catalogo.DESCRIPTOR_CADUCIDAD, hoy, "99999999", true, true);
            queryAND.add(caduca, BooleanClause.Occur.MUST);
            queryOR.add(caduca, BooleanClause.Occur.MUST);

            // Ejemplo: filtro por un tipo de elemento CONTENIDOS DE MICROSITE
            // AVISO: Note this query can be slow, as it needs to iterate over many terms
            //query.add( new WildcardQuery(new Term ("id", "MICRO.CONTENIDOS.*")) , BooleanClause.Occur.MUST);

            TopDocs hits = searcher.search(queryAND, RESULTATS_CERCA_TOTS);
            long endTime = 0;

            //Se hace un AND
            //si hay resultados->se muestran
            //si no hay resultados->se hace un sugerir y se hace un OR

            if ((hits.totalHits < MIN_HITS || hits.scoreDocs[0].score < MIN_SCORE) && sugerir) {
				String cadenaSugerida = "";
				Query quisoDecir = sugerir(cadena, idioma);

				if (quisoDecir != null) {
				    cadenaSugerida = quisoDecir.toString(CAMPO_BUSQUEDAS).replace('+', ' ');
				}

				TopDocs hits2 = searcher.search(queryOR, RESULTATS_CERCA_TOTS);
				endTime = System.currentTimeMillis();
				res = new IndexResultados(extractHits(hits2, cadena, directory), hits2.totalHits, (endTime - startTime), cadena, cadenaSugerida, "1");
				//res= new IndexResultados(null, 0, (endTime - startTime), cadena, cadenaSugerida, null );

			} else {
            	endTime = System.currentTimeMillis();
            	res= new IndexResultados(extractHits(hits, cadena, directory), hits.totalHits, (endTime - startTime), cadena, null, null);

            }
            searcher.close();

        	return res;            

        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        } catch (ParseException e) {
        	log.error(e.getMessage());
        	return null;
		}
    }

    /**
     * Retorna el diccionario del campo CAMPO_BUSQUEDAS
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ArrayList<String> diccionario(String idi) {

    	Iterator<?> it = null;
    	ArrayList<String> listawords = new ArrayList<String>();
        try {
            IndexReader indexReader = IndexReader.open(getHibernateDirectory(idi));
            Dictionary dictionary = new LuceneDictionary(indexReader, CAMPO_BUSQUEDAS);
            SpellChecker spellChecker = new SpellChecker(getHibernateDirectory(idi + "/dicc"));
            //spellChecker.clearIndex();
            IndexWriterConfig conf = new IndexWriterConfig(Constants.LUCENE_VERSION, Analizador.getAnalizador(idi));
            try {
                spellChecker.indexDictionary(dictionary, conf, true);
                it = (Iterator<?>) dictionary.getWordsIterator();

                while (it.hasNext()) {
                    listawords.add((String) it.next());
                }
            } finally {
                if (indexReader != null) {
                    indexReader.close();
                }
            }

        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return listawords;
    }

    /**
     * Re-indexa un microsite completo.
     * @throws QueryServiceException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void reindexarMicrosite(Long idsite) throws QueryServiceException {

        Session session = getSession();
        try {
        	log.info("Inicio indexacion micro " + idsite);
        	
        	ModelFilterObject filter = DelegateUtil.getMicrositeDelegate().obtenerFilterObject(idsite);
        	List langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();

        	String hql = "";
        	org.hibernate.Query query;
        	Iterator<?> iter;
        	StringBuffer stlog = new StringBuffer("");

        	//CONTENIDOS DEL MENU NO CADUCADOS
        	stlog = new StringBuffer("");
        	hql = "from Menu men where men.microsite.id = " + idsite.toString();
            query = session.createQuery(hql);
            List<?> list = query.list();
            iter = list.iterator();
            ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
            while (iter.hasNext()) {
                Menu men = (Menu) iter.next();

            	Iterator<?> itercont = men.getContenidos().iterator();
                Date hoy = new Date();
            	while (itercont.hasNext()) {
            		Contenido con = (Contenido) itercont.next();
            		// SOLO CONTENIDOS NO CADUCADOS
            		if (con.getFcaducidad() == null || con.getFcaducidad().getTime() >= hoy.getTime()) {
            			bdConte.indexBorraContenido(con.getId());
            			bdConte.indexInsertaContenido(con, filter);
            			stlog.append(con.getId() + " ");
            		}
                }
            }
            log.info("index cntsp [site:" + idsite + "] " + stlog.toString());
            session.flush();
            session.clear();

            // EVENTOS DE LA AGENDA NO CADUCADOS
            stlog = new StringBuffer("");
            hql = "from Agenda age where age.idmicrosite = " + idsite.toString();
            hql += " and ((age.ffin is null) OR (age.ffin >= sysdate))";
            query = session.createQuery(hql);
            list = query.list();
            iter = list.iterator();
            AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
            while (iter.hasNext()) {
            	Agenda age = (Agenda) iter.next();
            	bdAgenda.indexBorraAgenda(age.getId());
            	bdAgenda.indexInsertaAgenda(age, filter);
            	stlog.append(age.getId() + " ");
            }
            log.info("index gnd00 [site:" + idsite + "] " + stlog.toString());
            session.flush();
            session.clear();

            // ENCUESTAS NO CADUCADAS
            stlog = new StringBuffer("");
            hql = "from Encuesta encu where encu.idmicrosite = " + idsite.toString();
            hql += " and ((encu.fcaducidad is null) OR (encu.fcaducidad >= sysdate))";
            query = session.createQuery(hql);
            list = query.list();
            iter = list.iterator();
            EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
            while (iter.hasNext()) {
            	Encuesta encu = (Encuesta) iter.next();
            	bdEncuesta.indexBorraEncuesta(encu.getId());
            	bdEncuesta.indexInsertaEncuesta(encu, filter);
            	stlog.append(encu.getId() + " ");
            }
            log.info("index ncsts [site:" + idsite + "] " + stlog.toString());

            // FAQS
            FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
            bdFaq.init(idsite);
           	bdFaq.indexBorraFaqs(idsite);
           	bdFaq.indexInsertaFaqs(idsite, filter);
           	log.info("index fqs00 [site:" + idsite + "] Faqs");

            // ELEMENTOS NO CADUCADOS
           	stlog = new StringBuffer("");
            hql = "from Noticia noti where noti.idmicrosite = " + idsite.toString();
            hql += " and ((noti.fcaducidad is null) OR (noti.fcaducidad >= sysdate))";
            query = session.createQuery(hql);
            list = query.list();
            iter = list.iterator();
            NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
            while (iter.hasNext()) {
            	Noticia not = (Noticia) iter.next();
            	bdNoticia.indexBorraNoticia(not.getId());
            	bdNoticia.indexInsertaNoticia(not, filter);
            	stlog.append(not.getId() + " ");
            }
            log.info("index ntcs0 [site:" + idsite + "] " + stlog.toString());
            session.flush();
            session.clear();

            // DOCUMENTOS COMUNES
            stlog = new StringBuffer("");
            query = session.createQuery("from Archivo archi where archi.idmicrosite = " + idsite.toString() + " and archi.pagina is null");
            list = query.list();
            iter = list.iterator();
            ArchivoDelegate bdArchi = DelegateUtil.getArchivoDelegate();
            while (iter.hasNext()) {
	            try {
	            	Archivo archi = (Archivo) iter.next();
	            	bdArchi.indexBorraArchivo(archi.getId());
	            	bdArchi.indexInsertaArchivo(archi, filter);
	            	stlog.append(archi.getId() + " ");
	            } catch (Exception e) {
	               log.warn("No se ha podido indexar el archivo");
	            }
            }
            log.info("index documentos [site:" + idsite + "] " + stlog.toString());
            session.flush();
            session.clear();

            // Optimizamos el indice y diccionario
            optimizar(langs);
           	for (int i = 0; i < langs.size(); i++) {
           	    confeccionaDiccionario("" + langs.get(i));
           	}

			log.info("Fin indexacion micro " + idsite);

        } catch (DelegateException e) {
            throw new EJBException(e);
        } catch (HibernateException e) {
            throw new EJBException(e);
        } catch (IOException e) {
            throw new EJBException(e);
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Abrir el directorio del indice.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Directory getHibernateDirectory(String idi) {

        Directory directory = null;
        try {
            if (FILESYSTEM_INDEX_TYPE.equals(indexType)) {
//                directory = FSDirectory.open(new File(System.getProperty(indexLocation) + "\\" + idi));
                directory = FSDirectory.open(new File(indexLocation + "\\" + idi));

            } else if (HIBERNATE_INDEX_TYPE.equals(indexType)) {
                directory = new HibernateDirectory(getSessionFactory());

            } else {
                throw new EJBException("Tipo de Ãndice no valido: " + indexType);
            }

            if (!IndexReader.indexExists(directory)) {
                new IndexWriter(directory, analyzer, true, MaxFieldLength.UNLIMITED).close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return directory;
    }


    private synchronized void addDocumentoAlWriter(IndexWriter writer, IndexObject indexObject, String idioma) {

        // Añade un documento
        try {
            Document document = getDocument(indexObject);
            writer.addDocument(document, Analizador.getAnalizador(idioma));
    
        } catch (IOException e) {
            log.error("addDocumentoAlWriter [" + OPER_ADDDOCUMENT + "] de microsites: " + e.getMessage(), e);
        }
    }


    private synchronized void actualizarDiccionario(String idioma) {

        // Crea/Actualiza el diccionario
        try {
            IndexReader indexReader = IndexReader.open(getHibernateDirectory(idioma));
            try {
                Dictionary dictionary = new LuceneDictionary(indexReader, CAMPO_BUSQUEDAS);
                SpellChecker spellChecker = new SpellChecker(getHibernateDirectory(idioma + "/dicc"));
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Constants.LUCENE_VERSION, Analizador.getAnalizador(idioma));
                spellChecker.indexDictionary(dictionary, indexWriterConfig, true);
            } finally {
                indexReader.close();
            }

        } catch (IOException e) {
            log.error("actualizarDiccionario [" + OPER_DICCIONARIO + "] de microsites: " + e.getMessage(), e);
        }
    }


    private synchronized void optimizarIndice(IndexWriter writer) {

        // Optimiza el índice
        try {
            writer.optimize();

        } catch (IOException e) {
            log.error("operarConWriter [" + OPER_OPTIMIZAR + "] de microsites: " + e.getMessage(), e);
        }
    }


    private Document getDocument(IndexObject indexObject) {

        Document document = new Document();
        document.setBoost(indexObject.getBoost());

        /* Descriptores identificadores */
        if (indexObject.getId() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_ID, indexObject.getId(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getClasificacion() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_CLASIFICACION, indexObject.getClasificacion(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        /* Descriptores filtro */
        if (indexObject.getMicro() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_MICRO, indexObject.getMicro().toString(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getRestringido() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_RESTRINGIDO, indexObject.getRestringido(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getFamilia() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_FAMILIA, indexObject.getFamilia().toString(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getSeccion() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_SECCION, indexObject.getSeccion(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getMateria() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_MATERIA, indexObject.getMateria(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getUo() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_UO, indexObject.getUo(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        /* descriptores informacion del documento */
        if (indexObject.getTitulo() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_TITULO, indexObject.getTitulo(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getTituloserviciomain() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_TITULO_SRV_MAIN, indexObject.getTituloserviciomain(), Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getText() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_TEXT, indexObject.getText(), Field.Store.YES, Field.Index.toIndex(true, true)));
        }

        if (indexObject.getTextopcional() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_TEXTOPCIONAL, indexObject.getTextopcional(), Field.Store.YES, Field.Index.toIndex(true, true)));
        }

        if (indexObject.getUrl() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_URL, indexObject.getUrl(), Field.Store.YES, Field.Index.NO));
        }

        if (!indexObject.getPublicacion().equals("")) {
            document.add(new Field(Catalogo.DESCRIPTOR_PUBLICACION, indexObject.getPublicacion(), Field.Store.YES, Field.Index.toIndex(true, false)));
        } else {
            document.add(new Field(Catalogo.DESCRIPTOR_PUBLICACION, "00000000", Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (!indexObject.getCaducidad().equals("")) {
            document.add(new Field(Catalogo.DESCRIPTOR_CADUCIDAD, indexObject.getCaducidad(), Field.Store.YES, Field.Index.toIndex(true, false)));
        } else {
            document.add(new Field(Catalogo.DESCRIPTOR_CADUCIDAD, "99999999", Field.Store.YES, Field.Index.toIndex(true, false)));
        }

        if (indexObject.getDescripcion() != null) {
            document.add(new Field(Catalogo.DESCRIPTOR_DESCRIPCION, indexObject.getDescripcion(), Field.Store.YES, Field.Index.toIndex(true, false)));        
        }

        return document;
    }

	private Vector getWords(String cadena, String campo, String idioma) {

        Vector words = new Vector();                
        TokenStream tokens = Analizador.getAnalizador(idioma).tokenStream(campo, new StringReader(cadena));

        CharTermAttribute cattr = tokens.addAttribute(CharTermAttribute.class);
        try {
            tokens.reset();
            while (tokens.incrementToken()) {
                words.add(cattr.toString());
            }
            tokens.end();
            tokens.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

	private List<IndexEncontrado> extractHits(TopDocs hits, String cadena, Directory directory) throws IOException {

	    List<IndexEncontrado> hitList = new ArrayList<IndexEncontrado>();
	    IndexReader reader = IndexReader.open(directory);

	    for (ScoreDoc scoreDoc : hits.scoreDocs) {
	        Document doc = reader.document(scoreDoc.doc);
	        hitList.add(new IndexEncontrado(doc.get("id"), 
                doc.get(Catalogo.DESCRIPTOR_TITULO), 
                doc.get(Catalogo.DESCRIPTOR_DESCRIPCION), 
                doc.get(Catalogo.DESCRIPTOR_MICRO) , 
                doc.get(Catalogo.DESCRIPTOR_URL) , 
                new Float(100 * scoreDoc.score).intValue()));
	    }

	    return hitList;
	}	

	private Query sugerir(String queryString, String idi) throws ParseException {
	
		QuerySuggester querySuggester = new QuerySuggester(CAMPO_BUSQUEDAS, Analizador.getAnalizador(idi), getHibernateDirectory(idi + "/dicc"), idi);
        querySuggester.setDefaultOperator(QueryParser.AND_OPERATOR);
        Query query = querySuggester.parse((queryString.length() > 0) ? queryString : " ");
        
        return querySuggester.hasSuggestedQuery() ? query : null;
	}

	/**
	 * Clase que sobreescribe un método de QueryParser para permitir hacer sugerencias 
	 * cuando las búsquedas sean compuestas
	 */
	public class QuerySuggester extends QueryParser {

		private boolean suggestedQuery = false;
		private Directory spellIndexDirectory;
		private String idioma;

		public QuerySuggester(String field, Analyzer analyzer, Directory dir, String idi) {

			super(Constants.LUCENE_VERSION, field, analyzer);
			spellIndexDirectory = dir;
			idioma = idi;
		}

		public boolean hasSuggestedQuery() {

			return suggestedQuery;
		}

	}
	
}
