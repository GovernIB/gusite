package org.ibit.rol.sac.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.Faq;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.IndexObject;
import org.ibit.rol.sac.micromodel.TraduccionFaq;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * SessionBean para consultar Faq.
 *
 * @ejb.bean
 *  name="sac/micropersistence/FaqFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.FaqFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class FaqFacadeEJB extends HibernateEJB
{

	private static final long serialVersionUID = -836117598281319916L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        try {
        	super.langs= DelegateUtil.getIdiomaDelegate().listarLenguajes();
        }
        catch(Exception ex) {
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
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Faq faq join faq.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and faq.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.pregunta","trad.respuesta","trad.url","trad.urlnom"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }        
 
    /**
     * Inicializo los parámetros de la consulta de Faq.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Faq faq join faq.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.pregunta","trad.respuesta","trad.url","trad.urlnom"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }      
    
    /**
     * Crea o actualiza una faq
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarFaq(Faq faq) {
        Session session = getSession();
        boolean nuevo=false;
        try {
            Transaction tx = session.beginTransaction();
            if (faq.getId()==null) nuevo=true;
            session.saveOrUpdate(faq);
            session.flush();
            tx.commit();
            
            //if (!nuevo) indexBorraFaqs(faq.getIdmicrosite());
           	//indexInsertaFaqs(faq.getIdmicrosite(), null);
            
            return faq.getId();
        } catch (HibernateException he) {
        	if (!nuevo) indexBorraFaqs(faq.getIdmicrosite());
            throw new EJBException(he);
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
        	Faq faq = (Faq) session.load(Faq.class, id);
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
           	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
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
           	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
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
    public void borrarFaq(Long id) {
        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Faq faq = (Faq) session.load(Faq.class, id);
            session.delete(faq);
            
            //indexBorraFaqs(faq.getIdmicrosite());
            
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
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
        	Query query = session.createQuery("from Faq faq where faq.idmicrosite="+site.toString()+" and faq.id="+id.toString());
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
    public void indexInsertaFaqs(Long idsite, ModelFilterObject filter)  {
		
		IndexObject io= new IndexObject();
		try {
			
			//DelegateUtil.getFaqDelegate().listarFaqs();
			
			if (filter==null) 
				filter=DelegateUtil.getMicrositeDelegate().obtenerFilterObject(idsite);
				
			if (filter!=null) {
				if (filter.getBuscador().equals("N")) return;
			}
			
			for (int i = 0; i < langs.size(); i++) {
				String idi = (String) langs.get(i);
				
			
				
				io = new IndexObject();
				io.setId(Catalogo.SRVC_MICRO_FAQS + "." + idsite);
				io.setClasificacion(Catalogo.SRVC_MICRO_FAQS);				
				
				io.setMicro(filter.getMicrosite_id());
				io.setRestringido(filter.getRestringido());
				io.setUo(filter.getUo_id());
				io.setMateria(filter.getMateria_id());
				io.setSeccion(filter.getSeccion_id());
				io.setFamilia(filter.getFamilia_id());				

	            io.setTitulo("Faqs");
	            io.setUrl("/sacmicrofront/faqs.do?lang="+idi+"&idsite="+idsite.toString());
				io.setCaducidad("");
				io.setPublicacion("");
				io.setDescripcion("");
				io.setTituloserviciomain(filter.getTraduccion(idi).getMaintitle());
				
				
				Iterator<?> iter = listarFaqs().iterator();
				while (iter.hasNext()) {
					Faq faq = (Faq)iter.next();
		            
					TraduccionFaq trad=((TraduccionFaq)faq.getTraduccion(idi));
		            if (trad!=null) {

		            	io.addTextLine(trad.getPregunta());
		            	io.addTextLine(trad.getRespuesta());
		            	
		            }

				}    
				io.addTextopcionalLine(filter.getTraduccion(idi).getMateria_text());
				io.addTextopcionalLine(filter.getTraduccion(idi).getSeccion_text());
				io.addTextopcionalLine(filter.getTraduccion(idi).getUo_text());
				
	            

	            if (io.getText().length()>0)
	            	DelegateUtil.getIndexerDelegate().insertaObjeto(io, idi);
			}
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}
		catch (Exception e) {
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
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}
        
	}    
    
}