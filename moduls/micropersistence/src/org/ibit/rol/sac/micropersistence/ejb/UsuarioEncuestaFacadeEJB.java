package org.ibit.rol.sac.micropersistence.ejb;


import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;


import org.ibit.rol.sac.micromodel.UsuarioEncuesta;


import javax.ejb.CreateException;
import javax.ejb.EJBException;



/**
 * SessionBean para gestionar usuarios de las encuestas.
 *
 * @ejb.bean
 *  name="sac/micropersistence/UsuarioEncuestaFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.UsuarioEncuestaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class UsuarioEncuestaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 884284998737422833L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    @Override
	public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de los usuarios encuesta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from UsuarioEncuesta usuenc ";
    	super.where="";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"usuenc.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }   

       
    /**
     * Crea o actualiza una respuesta dato
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public long grabarUsuarioEncuesta(UsuarioEncuesta usuario) {
      
       Session session = getSession();
       try {

            session.save(usuario);
            session.flush();
            return usuario.getId();
        
       } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
        	close(session);
        }
    }


    /**
     * Obtiene un usuario de una encuesta.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.oper},${role.super}"
     */
    public UsuarioEncuesta obtenerUsuarioEncuesta(Long id) {
        Session session = getSession();
        try {
        	UsuarioEncuesta usuario = (UsuarioEncuesta) session.load(UsuarioEncuesta.class, id);
            Hibernate.initialize(usuario);
            return usuario;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Borra un usuario.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarUsuarioEncuesta(Long id) {
        Session session = getSession();
        try {

        	UsuarioEncuesta usuario = (UsuarioEncuesta) session.load(UsuarioEncuesta.class, id);
            
            session.delete(usuario);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }



}
