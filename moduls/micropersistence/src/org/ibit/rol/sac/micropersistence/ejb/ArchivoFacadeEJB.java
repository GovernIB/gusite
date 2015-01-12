package org.ibit.rol.sac.micropersistence.ejb;

import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;
import net.sf.hibernate.expression.Expression;

import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.IndexObject;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * SessionBean para obtener archivos de BD.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ArchivoFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.ArchivoFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class ArchivoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 81125150632029055L;

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
     * Obtiene el archivo
     * Comprobamos que pertenece al microsite o es público (microsite=0)
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Archivo obtenerArchivo(Long id) {
        Session session = getSession();
        try {
        	Archivo archi = new Archivo();
        	
        	Query query = session.createQuery("from Archivo archi where archi.id="+id.toString());
        	if (query.list().size()==1)	archi=(Archivo)query.list().get(0);
            Hibernate.initialize(archi);
        	
        	return archi;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Obtiene un archivo por el nombre
     * Comprobamos que pertenece al microsite o es público (microsite=0)
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Archivo obtenerArchivobyName(Long site, String nombre) {
        Session session = getSession();
        try {
            Criteria criteriID = session.createCriteria(Archivo.class);
            criteriID.add(Expression.eq("idmicrosite", site));
            criteriID.add(Expression.eq("nombre", nombre));      

            List<?> listArchi = criteriID.list();
        	Iterator<?> iterArchi = listArchi.iterator();
       	 	Archivo archi = new Archivo();
            while (iterArchi.hasNext()) {
				archi = (Archivo)iterArchi.next();
				return archi;
            }            
            
            Long site0 = new Long(0);
            Criteria criteri = session.createCriteria(Archivo.class);
            criteri.add(Expression.eq("idmicrosite", site0));
            criteri.add(Expression.eq("nombre", nombre));   
            listArchi = criteri.list();
        	iterArchi = listArchi.iterator();
            while (iterArchi.hasNext()) {
				archi = (Archivo)iterArchi.next();            	 
				String hql="from Noticia not join not.traducciones trad join trad.docu doc where doc.id='" + archi.getId() + "' and not.idmicrosite='"+site+ "'";
				Query query = session.createQuery(hql);
				List<?> listNot = query.list();
				if (listNot.size()==1) return archi;
            }
            return archi;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }    

    /**
     * Comprueba que el elemento pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Archivo archi where (archi.idmicrosite=0 or archi.idmicrosite="+site.toString()+") and archi.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    

   /**
     * Inserta un nuevo documento en la BD
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long insertarArchivo(Archivo archi) throws DelegateException {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(archi);
            session.flush();
            tx.commit();
            return archi.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Borra un documento de la BD
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarArchivo(Long id) throws DelegateException {
        Session session = getSession();
        try {
        	Archivo archi = (Archivo) session.load(Archivo.class, id);
            session.delete(archi);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Crea o actualiza un archivo
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarArchivo(Archivo archi) {
        Session session = getSession();
        boolean nuevo=false;
        try {
            Transaction tx = session.beginTransaction();
            if (archi.getId()==null) nuevo=true;
            session.saveOrUpdate(archi);
            session.flush();
            tx.commit();

            return archi.getId();
            
        } catch (HibernateException he) {
        	if (!nuevo) indexBorraArchivo(archi.getId());
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    

    /***************************************************************************************/
    /*******************             INDEXACION         ************************************/
    /***************************************************************************************/	    

	 /**
     * Añade un archivo al indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public void indexInsertaArchivo(Archivo archi, ModelFilterObject filter)  {
		
		if (archi.getDatos()!=null) {
			IndexObject io= new IndexObject();
			io.addArchivo(archi);

			try {
				if (io.getText().length()>0) {
					
					io.setId(Catalogo.SRVC_MICRO_DOCUMENTOS + "." + archi.getId());
					io.setClasificacion(Catalogo.SRVC_MICRO_DOCUMENTOS);

					io.setMicro(filter.getMicrosite_id());
					io.setRestringido(filter.getRestringido());
					io.setUo(filter.getUo_id());
					io.setMateria(filter.getMateria_id());
					io.setSeccion(filter.getSeccion_id());
					io.setFamilia(filter.getFamilia_id());

					io.setTitulo(archi.getNombre());
					io.setUrl("/sacmicrofront/archivopub.do?ctrl=MCRST"+io.getMicro().toString()+"ZI"+archi.getId().toString()+"&id="+archi.getId().toString());
					io.setCaducidad("");
					io.setPublicacion("");
					if (io.getText().length()>200) io.addDescripcionLine(io.getText().substring(0,199)+"...");
                	else io.addDescripcionLine(io.getText());
					
					for (int i = 0; i < langs.size(); i++) {
						DelegateUtil.getIndexerDelegate().insertaObjeto(io, (String) langs.get(i));
					}
				}
			}
			catch (DelegateException ex) {
				throw new EJBException(ex);
			}
			catch (Exception e) {
				log.warn("[indexInsertaArchivo:" + archi.getId() + "] No se ha podido indexar elemento. " + e.getMessage());
			}
		}
        
	}
	
	 /**
     * Elimina el archivo del indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public void indexBorraArchivo(Long id)  {
		
		try {
			for (int i = 0; i < langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(Catalogo.SRVC_MICRO_DOCUMENTOS + "." + id, ""+langs.get(i));
			}
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}
        
	}    
  
}