package org.ibit.rol.sac.micropersistence.ejb;

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

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.ibit.rol.sac.micropersistence.delegate.AgendaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.ArchivoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.FaqDelegate;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.ibit.lucene.analysis.AlemanAnalyzer;
import org.ibit.lucene.analysis.CastellanoAnalyzer;
import org.ibit.lucene.analysis.CatalanAnalyzer;
import org.ibit.lucene.analysis.InglesAnalyzer;
import org.ibit.lucene.hibernate.HibernateDirectory;
import org.ibit.rol.sac.micropersistence.ejb.HibernateEJB;
import org.ibit.rol.sac.micropersistence.util.lucene.analysis.FrancesAnalyzer;
import org.ibit.rol.sac.micromodel.Agenda;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Faq;
import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.IndexEncontrado;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.IndexObject;
import org.ibit.lucene.indra.model.IndexResultados;
import org.ibit.rol.sac.micromodel.Menu;
import org.ibit.rol.sac.micromodel.Noticia;


/**
 * SessionBean para indexar Microsites.
 *
 * @ejb.bean
 *  name="sac/micropersistence/IndexerFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.IndexerFacade"
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
    public static final int MIN_HITS=1;
    public static final int MAX_HITS=100;
    public static final double MIN_SCORE=0.20;
    public static final String CAMPO_BUSQUEDAS="text";
    public static final String OPER_ADDDOCUMENT="addDocument";
    public static final String OPER_DICCIONARIO="diccionario";
    public static final String OPER_OPTIMIZAR="optimizar";

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
    public void insertaObjeto(IndexObject indexObject, String idi) {
    	if (indexObject.getTitulo().length()>0)
    		operarConWriter(OPER_ADDDOCUMENT, indexObject, idi);
        
    }


    /**
     * Borra un objeto documento en un idioma
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void borrarObjeto(String id, String idi) {
        try {
        	Directory directory = getHibernateDirectory(idi);
            IndexReader reader = IndexReader.open(directory);
            reader.deleteDocuments(new Term(Catalogo.DESCRIPTOR_ID, id));
            reader.close();
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
				bdAgenda.indexBorraAgenda(((Agenda)objeto).getId());
	           	bdAgenda.indexInsertaAgenda((Agenda)objeto, null);
	    	}
	    	if (objeto instanceof Noticia) {
	    		NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
				bdNoticia.indexBorraNoticia(((Noticia)objeto).getId());
	           	bdNoticia.indexInsertaNoticia((Noticia)objeto, null);
	    	}
	    	if (objeto instanceof Encuesta) {
	    		EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
				bdEncuesta.indexBorraEncuesta(((Encuesta)objeto).getId());
	           	bdEncuesta.indexInsertaEncuesta((Encuesta)objeto, null);
	    	}
	    	if (objeto instanceof Contenido) {
	    		ContenidoDelegate bdContenido = DelegateUtil.getContenidoDelegate();
				bdContenido.indexBorraContenido(((Contenido)objeto).getId());
	           	bdContenido.indexInsertaContenido((Contenido)objeto, null);
	    	}
	    	if (objeto instanceof Faq) {
	    		FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
	    		bdFaq.init(((Faq)objeto).getIdmicrosite());
				bdFaq.indexBorraFaqs(((Faq)objeto).getIdmicrosite());
	           	bdFaq.indexInsertaFaqs(((Faq)objeto).getIdmicrosite(), null);
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
				bdAgenda.indexBorraAgenda(((Agenda)objeto).getId());
	    	}
	    	if (objeto instanceof Noticia) {
	    		NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
				bdNoticia.indexBorraNoticia(((Noticia)objeto).getId());
	    	}	  
	    	if (objeto instanceof Encuesta) {
	    		EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
				bdEncuesta.indexBorraEncuesta(((Encuesta)objeto).getId());
	    	}
	    	if (objeto instanceof Contenido) {
	    		ContenidoDelegate bdContenido = DelegateUtil.getContenidoDelegate();
				bdContenido.indexBorraContenido(((Contenido)objeto).getId());
	    	}
	    	if (objeto instanceof Faq) {
	    		FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
	    		bdFaq.init(((Faq)objeto).getIdmicrosite());
				bdFaq.indexBorraFaqs(((Faq)objeto).getIdmicrosite());
	           	bdFaq.indexInsertaFaqs(((Faq)objeto).getIdmicrosite(), null);
	    	}
    	} 	
    }    
    
    /**
     * Optimiza el indice de busquedas
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public void optimizar(String idioma) {
    	
    	operarConWriter(OPER_OPTIMIZAR, null, idioma);

    }    
    
    /**
     * Crea o actualiza el diccionario
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public void confeccionaDiccionario(String idioma) {
    	
    	operarConWriter(OPER_DICCIONARIO, null, idioma);

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
	        	Directory directory = getHibernateDirectory(""+langs.get(i));
	            IndexReader reader = IndexReader.open(directory);
	            reader.deleteDocuments(new Term(Catalogo.DESCRIPTOR_MICRO, "" + idsite.longValue()));
	            reader.close();
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
    public IndexResultados buscar(String micro, String idi, String idlista, String cadena, boolean sugerir) {
    	long startTime = System.currentTimeMillis();
        try {
        	idi = idi.toLowerCase();
            Directory directory = getHibernateDirectory(idi);
            IndexSearcher searcher = new IndexSearcher(directory);

            BooleanQuery queryOR = new BooleanQuery();
            BooleanQuery queryAND = new BooleanQuery();
            IndexResultados res= null;
            
            Vector<String> words = new Vector<String>();
            if (cadena!=null && cadena.length()>0) {
            	Query query_compuestaAND = null;
            	Query query_compuestaOR = null;
            	
    			TokenStream tokens = getAnalizador(idi).tokenStream(CAMPO_BUSQUEDAS, new StringReader (cadena));
    			
    			Token token;
    			while (true) {
    				try {
    					token = tokens.next();
    				} catch (IOException e) {
    					token = null;
    				}
    				if (token == null) break;
    				words.addElement(token.termText());
    			}
    			try {
    				tokens.close();
    			} catch (IOException e) {}                	
    			
    			if (words.size() > 1) {
    				Query[] expandedQueries=new Query[words.size()];
    				query_compuestaAND = new PhraseQuery();
    				query_compuestaOR = new PhraseQuery();
    				((PhraseQuery)query_compuestaAND).setSlop(5);
    				((PhraseQuery)query_compuestaOR).setSlop(15);

    				for (int i = 0; i < words.size(); i++) {
    					((PhraseQuery)query_compuestaAND).add(new Term(CAMPO_BUSQUEDAS, (String)words.elementAt(i)));
    					((PhraseQuery)query_compuestaOR).add(new Term(CAMPO_BUSQUEDAS, (String)words.elementAt(i)));
    					expandedQueries[i] = new TermQuery(new Term(CAMPO_BUSQUEDAS, (String)words.elementAt(i)));
    				}
    				query_compuestaOR = query_compuestaOR.combine(expandedQueries); //combinarlas entre si. Es equivalente a un OR de todo con todo    				
    				
    				
    			} 
    			else if (words.size() == 1) {
    				query_compuestaAND = new TermQuery(new Term(CAMPO_BUSQUEDAS, (String)words.elementAt(0)));
    				query_compuestaOR = new TermQuery(new Term(CAMPO_BUSQUEDAS, (String)words.elementAt(0)));
    			} 
    			else if (words.size()==0) return new IndexResultados(null, 0, 0, cadena, "", null );

    			queryAND.add(query_compuestaAND, BooleanClause.Occur.MUST);
    			queryOR.add(query_compuestaOR, BooleanClause.Occur.MUST);
    			
            } else {
            	return new IndexResultados(null, 0, 0, cadena, "", null );
            }

            
            if (micro!=null && micro.length()>0) {
            	queryAND.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_MICRO,micro)), BooleanClause.Occur.MUST );
            	queryOR.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_MICRO,micro)), BooleanClause.Occur.MUST );
            }
            if (idlista!=null && idlista.length()>0) {
            	queryAND.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_CLASIFICACION,Catalogo.SRVC_MICRO_ELEMENTOS+"."+idlista)), BooleanClause.Occur.MUST );
            	queryOR.add(new TermQuery(new Term(Catalogo.DESCRIPTOR_CLASIFICACION,Catalogo.SRVC_MICRO_ELEMENTOS+"."+idlista)), BooleanClause.Occur.MUST );
            }
            
            
            // Evitamos los documentos caducados o que no han sido publicados aun
            Term fechamin_pub= new Term (Catalogo.DESCRIPTOR_PUBLICACION,"00000000");
            Term fechamax_cad= new Term (Catalogo.DESCRIPTOR_CADUCIDAD,"99999999");

            String hoy=new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
            Term hoy_pub= new Term (Catalogo.DESCRIPTOR_PUBLICACION, hoy );
            Term hoy_cad= new Term (Catalogo.DESCRIPTOR_CADUCIDAD, hoy );

            RangeQuery publica = new RangeQuery(fechamin_pub, hoy_pub , true);
            RangeQuery caduca = new RangeQuery(hoy_cad, fechamax_cad , true);
            
            queryAND.add(publica, BooleanClause.Occur.MUST);
            queryAND.add(caduca, BooleanClause.Occur.MUST);
            queryOR.add(publica, BooleanClause.Occur.MUST);
            queryOR.add(caduca, BooleanClause.Occur.MUST);
            
            // Ejemplo: filtro por un tipo de elemento CONTENIDOS DE MICROSITE
            // AVISO: Note this query can be slow, as it needs to iterate over many terms
            //query.add( new WildcardQuery(new Term ("id", "MICRO.CONTENIDOS.*")) , BooleanClause.Occur.MUST);
            
            Hits hits = searcher.search(queryAND);
            long endTime=0;
            
            
            //Se hace un AND
            //si hay resultados->se muestran
            //si no hay resultados->se hace un sugerir y se hace un OR
            
            if ((hits.length() < MIN_HITS || hits.score(0) < MIN_SCORE) && sugerir) {
				String cadenaSugerida = "";
				Query quisoDecir = sugerir(cadena, idi );
				
				if (quisoDecir != null) cadenaSugerida = quisoDecir.toString(CAMPO_BUSQUEDAS).replace('+',' ');
				Hits hits2 = searcher.search(queryOR);
				
				endTime = System.currentTimeMillis();
				
				res= new IndexResultados( extractHits(hits2, cadena), hits2.length(), (endTime - startTime), cadena, cadenaSugerida, "1" );
				
				//res= new IndexResultados(null, 0, (endTime - startTime), cadena, cadenaSugerida, null );
			}
            else {
            	endTime = System.currentTimeMillis();
            	res= new IndexResultados( extractHits(hits, cadena), hits.length(), (endTime - startTime), cadena, null, null );

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
    	
    	
    	IndexReader indexReader = null;
    	Dictionary dictionary = null;
    	Iterator<?> it = null;
    	ArrayList<String> listawords= new ArrayList<String>();
        try {
        	indexReader = IndexReader.open(getHibernateDirectory(idi));
            dictionary = new LuceneDictionary(indexReader, CAMPO_BUSQUEDAS);
            SpellChecker spellChecker = new SpellChecker(getHibernateDirectory(idi+"/dicc"));
            //spellChecker.clearIndex();
            spellChecker.indexDictionary(dictionary);

            it = dictionary.getWordsIterator();
            
            while (it.hasNext()) {
            	listawords.add((String)it.next());
            }

            if (indexReader != null) indexReader.close();
        } 
        catch (IOException ex) {
        	log.error(ex.getMessage());
            return null;
        }
        
        return listawords;
        
    }
    
    
    /**
     * Re-indexa un microsite completo.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void reindexarMicrosite(Long idsite) {
        Session session = getSession();

        try {
        	
        	
        	log.info("Inicio indexacion micro "+idsite);
        	
        	ModelFilterObject filter= DelegateUtil.getMicrositeDelegate().obtenerFilterObject(idsite);
        	List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
        	
        	String hql="";
        	net.sf.hibernate.Query query;
        	Iterator<?> iter;
        	StringBuffer stlog = new StringBuffer("");
            
        	//CONTENIDOS DEL MENU NO CADUCADOS
        	stlog = new StringBuffer("");
        	hql="from Menu men where men.idmicrosite="+idsite.toString();
            query = session.createQuery(hql);
            iter = query.iterate();
            ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
            while (iter.hasNext()) {
            	Menu men = (Menu) iter.next();
            	
            	Iterator<?> itercont=men.getContenidos().iterator();
                Date hoy= new Date();
            	while (itercont.hasNext()) {
            		Contenido con = (Contenido) itercont.next();
            		// SOLO CONTENIDOS NO CADUCADOS
            		if (con.getFcaducidad()==null || con.getFcaducidad().getTime()>=hoy.getTime()) {
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
            hql="from Agenda age where age.idmicrosite="+idsite.toString();
            hql+=" and ( (age.ffin is null) OR (age.ffin>=sysdate) )";
            query = session.createQuery(hql);
            iter = query.iterate();
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
            hql="from Encuesta encu where encu.idmicrosite="+idsite.toString();
            hql+=" and ( (encu.fcaducidad is null) OR (encu.fcaducidad>=sysdate) )";
            query = session.createQuery(hql);
            iter = query.iterate();
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
            hql="from Noticia not where not.idmicrosite="+idsite.toString();
            hql+=" and ( (not.fcaducidad is null) OR (not.fcaducidad>=sysdate) )";
            query = session.createQuery(hql);
            iter = query.iterate();
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
            query = session.createQuery("from Archivo archi where archi.idmicrosite="+idsite.toString()+" and archi.pagina is null");
            iter = query.iterate();
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
           	for (int i = 0; i < langs.size(); i++) {
            		optimizar(""+langs.get(i));
            		confeccionaDiccionario(""+langs.get(i));
           	}
           

			log.info("Fin indexacion micro "+idsite);
        
        } catch (DelegateException e) {
            throw new EJBException(e);
        } catch (HibernateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
        
        
    }
    
    private Directory getHibernateDirectory(String idi) throws IOException {
        Directory directory;
        if (FILESYSTEM_INDEX_TYPE.equals(indexType)) {
        	//directory = FSDirectory.getDirectory(new File(indexLocation+"/micro/"+idi));
        	directory = FSDirectory.getDirectory(new File(indexLocation+"/"+idi));
        } else if (HIBERNATE_INDEX_TYPE.equals(indexType)) {
            directory = new HibernateDirectory(getSessionFactory());
        } else {
            throw new EJBException("Tipo de índice no valido: " + indexType);
        }
        if (!IndexReader.indexExists(directory)) {
            new IndexWriter(directory, analyzer, true).close();
        }
        return directory;
    }

    
    
    private synchronized void operarConWriter(String operacion, IndexObject indexObject, String idioma) {
    	try {
			Directory directory = getHibernateDirectory(idioma);
			IndexWriter writer = new IndexWriter(directory, getAnalizador(idioma), false);
			writer.setMergeFactor(25);
			writer.setMaxMergeDocs(625);

			
			//añade un documento
    		if (operacion.equals(OPER_ADDDOCUMENT)) {
                Document document = getDocument(indexObject);
                writer.addDocument(document);
    		}
    		
    		//crea/actualiza el diccionario
    		if (operacion.equals(OPER_DICCIONARIO)) {
    			IndexReader indexReader = IndexReader.open(getHibernateDirectory(idioma));
    			Dictionary dictionary = new LuceneDictionary(indexReader, CAMPO_BUSQUEDAS);
                SpellChecker spellChecker = new SpellChecker(getHibernateDirectory(idioma+"/dicc"));
                spellChecker.indexDictionary(dictionary);
                if (indexReader != null) indexReader.close();
    		}
    		
    		//optimiza el indice
    		if (operacion.equals(OPER_OPTIMIZAR)) {
    			writer.optimize();
    		}
    		
			writer.close();
    	} catch (IOException e) {
    		log.error("operarConWriter [" + operacion + "] de microsites: " + e.getMessage(),e);
    	}
    }    
    
    private Document getDocument(IndexObject indexObject) {
        Document document = new Document();

        document.setBoost(indexObject.getBoost());
        
        /* descriptores identificadores */
        if (indexObject.getId()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_ID, indexObject.getId(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getClasificacion()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_CLASIFICACION, indexObject.getClasificacion(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        
        /* descriptores filtro */
        if (indexObject.getMicro()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_MICRO, indexObject.getMicro().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));        
        if (indexObject.getRestringido()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_RESTRINGIDO, indexObject.getRestringido(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getFamilia()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_FAMILIA, indexObject.getFamilia().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getSeccion()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_SECCION, indexObject.getSeccion(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getMateria()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_MATERIA, indexObject.getMateria(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getUo()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_UO, indexObject.getUo(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        
        /* descriptores informacion del documento */
        if (indexObject.getTitulo()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_TITULO, indexObject.getTitulo(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getTituloserviciomain()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_TITULO_SRV_MAIN, indexObject.getTituloserviciomain(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getText()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_TEXT, indexObject.getText(), Field.Store.NO, Field.Index.TOKENIZED));
        if (indexObject.getTextopcional()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_TEXTOPCIONAL, indexObject.getTextopcional(), Field.Store.NO, Field.Index.TOKENIZED));
        if (indexObject.getUrl()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_URL, indexObject.getUrl(), Field.Store.YES, Field.Index.NO));
        if (!indexObject.getPublicacion().equals(""))	document.add(new Field(Catalogo.DESCRIPTOR_PUBLICACION, indexObject.getPublicacion(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        else 							        		document.add(new Field(Catalogo.DESCRIPTOR_PUBLICACION, "00000000", Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (!indexObject.getCaducidad().equals(""))	   	document.add(new Field(Catalogo.DESCRIPTOR_CADUCIDAD, indexObject.getCaducidad(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        else 								     	  	document.add(new Field(Catalogo.DESCRIPTOR_CADUCIDAD, "99999999", Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (indexObject.getDescripcion()!=null)	document.add(new Field(Catalogo.DESCRIPTOR_DESCRIPCION, indexObject.getDescripcion(), Field.Store.YES, Field.Index.UN_TOKENIZED));        
        
        return document;
    }

	private Analyzer getAnalizador(String idi) {

		if (idi.toLowerCase().equals("de"))      	analyzer = new AlemanAnalyzer();
        else if (idi.toLowerCase().equals("en")) 	analyzer = new InglesAnalyzer();
        else if (idi.toLowerCase().equals("ca")) 	analyzer = new CatalanAnalyzer();
		else if (idi.toLowerCase().equals("es"))    analyzer = new CastellanoAnalyzer();
		else  analyzer = new FrancesAnalyzer();

		return analyzer;
	}
	
	private List<IndexEncontrado> extractHits(Hits hits, String cadena) throws IOException {
	    List<IndexEncontrado> hitList = new ArrayList<IndexEncontrado>();
	    for (int i = 0, count = 0; i < hits.length() && count++ < MAX_HITS; i++) {
	    	Document doc=hits.doc(i);
            hitList.add(new IndexEncontrado(doc.get("id"), 
            								doc.get(Catalogo.DESCRIPTOR_TITULO), 
            								doc.get(Catalogo.DESCRIPTOR_DESCRIPCION), 
            								doc.get(Catalogo.DESCRIPTOR_MICRO) , 
            								doc.get(Catalogo.DESCRIPTOR_URL) , 
            								new Float(100*hits.score(i)).intValue()));
        }
	    return hitList;
	}	
	
	
	private Query sugerir(String queryString, String idi) throws ParseException {
	
		try {
			QuerySuggester querySuggester = new QuerySuggester(CAMPO_BUSQUEDAS, getAnalizador(idi), getHibernateDirectory(idi+"/dicc"), idi);
			querySuggester.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = querySuggester.parse((queryString.length()>0)?queryString:" ");
			
			return querySuggester.hasSuggestedQuery() ? query : null;

		} catch (IOException e) {
			throw new ParseException(e.getMessage());
		}

	}
	
	
	/**
	 *  Clase que sobreescribe un método de QueryParser para permitir hacer sugerencias 
	 * cuando las búsquedas sean compuestas
	 */
	public class QuerySuggester extends QueryParser {
		private boolean suggestedQuery = false;
		private Directory spellIndexDirectory;
		private String idioma;
		public QuerySuggester(String field, Analyzer analyzer, Directory dir, String idi) {
			super(field, analyzer);
			spellIndexDirectory=dir;
			idioma=idi;
		}
		protected Query getFieldQuery(String field, String queryText) throws ParseException {
			// Copied from org.apache.lucene.queryParser.QueryParser
			// replacing construction of TermQuery with call to getTermQuery()
			// which finds close matches.
		    TokenStream source = getAnalyzer().tokenStream(field, new StringReader(queryText));
			Vector<String> v = new Vector<String>();
			Token t;

			while (true) {
				try {	t = source.next();	} 
				catch (IOException e) {	t = null;}
				if (t == null)	break;
				v.addElement(t.termText());
			}
			try {	source.close();	} 
			catch (IOException e) {	// ignore 
			}

			if (v.size() == 0) {
				return null;
			}
			else if (v.size() == 1)	{
				return new TermQuery(getTerm(field, (String) v.elementAt(0)));
			}
			else {
				PhraseQuery q = new PhraseQuery();
				q.setSlop(getPhraseSlop());
				for (int i = 0; i < v.size(); i++) {
					q.add(getTerm(field, (String) v.elementAt(i)));
				}
				return q;
			}
		}
		private Term getTerm(String field, String queryText) throws ParseException {
			
			try {
				SpellChecker spellChecker = new SpellChecker(spellIndexDirectory);
				if (spellChecker.exist(queryText)) 	return new Term(field, queryText);

				String[] similarWords = spellChecker.suggestSimilar(queryText, 1, IndexReader.open(getHibernateDirectory(idioma)), CAMPO_BUSQUEDAS, true);
				
				if (similarWords.length == 0) 	return new Term(field, queryText);
				suggestedQuery = true;
				return new Term(field, similarWords[0]);
			} catch (IOException e) {
				throw new ParseException(e.getMessage());
			}
		}		
		
		public boolean hasSuggestedQuery() {
			return suggestedQuery;
		}
		
	}

	
	
}
