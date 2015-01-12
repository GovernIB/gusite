package org.ibit.rol.sac.micropersistence.ejb;

import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;

import org.ibit.rol.sac.micromodel.Convocatoria;
import org.ibit.rol.sac.micromodel.DistribucionConvocatoria;
import org.ibit.rol.sac.micromodel.ListaDistribucion;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * SessionBean para consultar Convocatorias.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ConvocatoriaFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.ConvocatoriaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Salvador Antich
 */
public abstract class ConvocatoriaFacadeEJB extends HibernateEJB
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
     * Inicializo los parámetros de la consulta de Convocatoria.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long convocatoria, Long microsite) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Convocatoria c ";
    	super.where=" where c.id="+convocatoria+" and c.microsite.id="+microsite;
    	super.whereini=" ";
    	super.orderby=" ";
    	super.camposfiltro= new String[] {"c.nombre ", "c.descripcion ", "c.ultimoEnvio "};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }          
    
    /**
     * Inicializo los parámetros de la consulta de Convocatoria.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long microsite) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Convocatoria c ";
    	super.where="where c.microsite.id="+microsite;
    	super.whereini=" ";
    	super.orderby=" ";

    	super.camposfiltro= new String[] {"c.nombre ", "c.descripcion ", "c.ultimoEnvio "};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    } 
    
    /**
     * Crea o actualiza una convocatoria
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarConvocatoria(Convocatoria convocatoria) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(convocatoria);
            session.flush();
            tx.commit();
            
            return convocatoria.getId();
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Añade una lista de distribucion a una convocatoria
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void ponerDestinatario(Long idConvocatoria, Long idLDistribucion) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Convocatoria conv = (Convocatoria)session.get(Convocatoria.class, idConvocatoria);            
            DistribucionConvocatoria distrConv = new DistribucionConvocatoria(idConvocatoria, idLDistribucion);
            conv.getDestinatarios().add(conv);

            session.save(distrConv);
            session.flush();
            tx.commit();
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Quita los destinatarios de una convocatoria
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void quitarDestinatarios(Long idConvocatoria) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Criteria distribCrit = session.createCriteria(DistribucionConvocatoria.class);
            distribCrit.add(Expression.eq("idConvocatoria",idConvocatoria));
            for (Iterator it = distribCrit.list().iterator();it.hasNext();){
            	session.delete((DistribucionConvocatoria)it.next());
            }            
            Convocatoria conv = (Convocatoria)session.get(Convocatoria.class, idConvocatoria);
            conv.getDestinatarios().clear();
            session.update(conv);

            session.flush();
            tx.commit();
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }   
    
    /**
     * Obtiene una Convocatoria
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Convocatoria obtenerConvocatoria(Long id) {
        Session session = getSession();
        try {
        	Convocatoria convocatoria = (Convocatoria) session.load(Convocatoria.class, id);
            return convocatoria;
        } catch (ObjectNotFoundException oNe) {
        	log.error(oNe.getMessage());
        	return new Convocatoria();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Comprueba si existe una Convocatoria
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public boolean existeConvocatoria(Long id) {
        Session session = getSession();
        try {
        	List<?> lista= session.find("from Convocatoria c where c.id="+id.toString());
            return (lista.size()>0);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Lista todas las Convocatorias
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarAllConvocatorias() {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
           	
        	Query query = session.createQuery(select+from+orderby);
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
     * Lista todos los convocatorias de un microsite
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarConvocatorias() throws DelegateException {
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
     * borra un Convocatoria
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarConvocatoria(Long id) throws DelegateException {
        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Convocatoria convocatoria = (Convocatoria) session.load(Convocatoria.class, id);
            session.delete(convocatoria);
            
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Lista todas las encuestas de un microsite buenas para una convocatoria
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarEncuestasConvocatoria() throws DelegateException {
    	 Session session = getSession();
         try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
            	
         	Query query = session.createQuery("SELECT distinct enc.* FROM Encuesta enc, Pregunta pre_correo, Respuesta res_correo, Pregunta pre_conf, Respuesta res_conf" +
         			where + " and enc.id = pre_correo.idencuesta and pre_correo.id = res_correo.idpregunta and res_correo.tipo = 'I' and (pre_correo.multiresp = 'N' or" +
         			" (pre_correo.multiresp = 'S' and pre_correo.maxContestadas = 1 and pre_correo.minContestadas = 1)) and pre_correo.visiblecmp = 'S' and enc.id = pre_conf.idencuesta" +
         			" and pre_conf.id = res_conf.idpregunta and res_conf.tipo = 'N' and (pre_conf.multiresp = 'N' or (pre_conf.multiresp = 'S' and pre_conf.maxContestadas = 1" +
         			" and pre_conf.minContestadas = 1)) and pre_conf.visiblecmp = 'S'");
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
     * Lista correo a los que enviar una convocatoria
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCorreosEnvio(Long idConvocatoria, Integer nIntentos) throws DelegateException {
    	 Session session = getSession();
         try {
        	parametrosCons(); // Establecemos los parámetros de la paginación

        	Query query = session.createQuery("select distinct des.correo from ListaDistribucion as lis" +
        			" inner join lis.destinatarios as des, DistribucionConvocatoria as dis, Convocatoria con" +
        			" where dis.idDistribucion = lis.id and con.id = dis.idConvocatoria and des.noEnviar = 0 and" +
        			" con.id = :idConvocatoria" +
        			" and (des.intentoEnvio is null or :nIntentos = 0 or des.intentoEnvio < :nIntentos)" +
        			" and not exists " +
        			" (select resCorreo.dato from Convocatoria as con, RespuestaDato as resCorreo, UsuarioPropietarioRespuesta resConfirm" +
        			" where con.id = :idConvocatoria and resCorreo.idusuari = resConfirm.idusuario and" +
        			" con.respuestaCorreo.id = resCorreo.idrespueta and" +
        			" des.correo = resCorreo.dato)");

             query.setLong("idConvocatoria", idConvocatoria);
             query.setInteger("nIntentos", nIntentos);
         	return query.list();
         } catch (HibernateException he) {
             throw new EJBException(he);
         } finally {
             close(session);
         }
    }    

    /**
     * Lista correo a los que enviar una convocatoria
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCorreosEnvio(Long idConvocatoria, Integer nIntentos, String correo) throws DelegateException {
    	 Session session = getSession();
         try {
        	parametrosCons(); // Establecemos los parámetros de la paginación

        	Query query = session.createQuery("select distinct c.correo from Correo as c" +        			
        			" where c.noEnviar = 0 and c.correo = :email" +        			
        			" and (c.intentoEnvio is null or :nIntentos = 0 or c.intentoEnvio < :nIntentos)" +        			
        			" and c.correo not in " +
        			" (select resCorreo.dato from Convocatoria as con, RespuestaDato as resCorreo, UsuarioPropietarioRespuesta resConfirm" +
        			" where con.id = :idConvocatoria and resCorreo.idusuari = resConfirm.idusuario and" +
        			" con.respuestaCorreo.id = resCorreo.idrespueta)");

             query.setLong("idConvocatoria", idConvocatoria);
             query.setInteger("nIntentos", nIntentos);
             query.setString("email", correo);
         	return query.list();
         } catch (HibernateException he) {
             throw new EJBException(he);
         } finally {
             close(session);
         }
    }    

}