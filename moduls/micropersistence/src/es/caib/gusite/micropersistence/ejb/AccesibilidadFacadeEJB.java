package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * SessionBean para consultar Accesibilidad.
 *
 * @ejb.bean
 *  name="sac/micropersistence/AccesibilidadFacade"
 *  jndi-name="es.caib.gusite.micropersistence.AccesibilidadFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class AccesibilidadFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -5418440926294056000L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
    
    /**
     * Crea o actualiza una accesibilidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Long grabarAccesibilidad(Accesibilidad accesibilidad) {

        Session session = getSession();
        try {
            boolean nuevo = (accesibilidad.getId() == null) ? true : false;
            Microsite site = (Microsite) session.get(Microsite.class, accesibilidad.getCodmicro());
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(accesibilidad);
            session.flush();
            tx.commit();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Accesibilidad.class.getSimpleName());
            auditoria.setIdEntidad(accesibilidad.getId().toString());
            auditoria.setMicrosite(site);
            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            auditoria.setOperacion(op);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

            return accesibilidad.getId();
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * Método que borra una accesibilidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void borrarAccesibilidad(Accesibilidad accesibilidad) {

        Session session = getSession();
        try {
            Microsite site = (Microsite) session.get(Microsite.class, accesibilidad.getCodmicro());
            Transaction tx = session.beginTransaction();
            session.delete(accesibilidad);
            session.flush();
            tx.commit();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Accesibilidad.class.getSimpleName());
            auditoria.setIdEntidad(accesibilidad.getId().toString());
            auditoria.setMicrosite(site);
            auditoria.setOperacion(Auditoria.ELIMINAR);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }
        
    /**
     * Obtiene una accesibilidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Accesibilidad obtenerAccesibilidad(Long id) {

        Session session = getSession();
        try {
        	Accesibilidad accesibilidad = (Accesibilidad) session.get(Accesibilidad.class, id);
            return accesibilidad;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
   
    /**
     * Obtiene una accesibilidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Accesibilidad obtenerAccesibilidad(String servicio, Long iditem, String idioma) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Accesibilidad.class);
            criteri.add(Expression.eq("servicio", servicio));
            criteri.add(Expression.eq("iditem", iditem));
            criteri.add(Expression.eq("idioma", idioma));
            Accesibilidad accesibilidad = null;
            if (criteri.list().size() > 0) {
                accesibilidad = (Accesibilidad) criteri.list().get(0);
            }
            return accesibilidad;

	    } catch (HibernateException he) {
	        throw new EJBException(he);
	    } finally {
	        close(session);
	    }
    }
    
    /**
     * Obtiene toda accesibilidad del site
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<?> obtenerAccesibilidadMicro(Long id) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Accesibilidad.class);
            criteri.add(Expression.eq("codmicro", id));
            criteri.addOrder(Order.asc("iditem"));
            return (List<?>) criteri.list();

	    } catch (HibernateException he) {
	        throw new EJBException(he);
	    } finally {
	        close(session);
	    }
    }

    /**
     * Obtiene toda accesibilidad del site
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<?>  obtenerAccesibilidadItem(Long id) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Accesibilidad.class);
            criteri.add(Expression.eq("iditem", id));
            return (List<?>) criteri.list();

	    } catch (HibernateException he) {
	        throw new EJBException(he);
	    } finally {
	        close(session);
	    }
    }

    /**
     * Determina si existe accesibilidad del item
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Boolean existeAccesibilidadContenido(Long id) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Accesibilidad.class);
            criteri.add(Expression.eq("iditem", id));
            if (criteri.list().size() > 0) {
                return true;
            } else {
                return false;
            }

	    } catch (HibernateException he) {
	        throw new EJBException(he);
	    } finally {
	        close(session);
	    }
    } 
    
	/**
	 * Determina si existe accesibilidad del microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public int existeAccesibilidadMicrosite(Long id) {
		
		Session session = getSession();
		try {
			int nivelXHTML = 0;
			int nivelTaw = 0;
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Expression.eq("codmicro", id));
			List<?> lista = criteri.list();
			
			if (lista.size() > 0) {
				List<?> Lista = criteri.list();
				Iterator<?> iter = Lista.iterator();
				
				while (iter.hasNext()) {
					Accesibilidad acces = (Accesibilidad) iter.next();
					if (acces.getTawresultado() != null && Integer.parseInt(acces.getTawresultado()) > nivelTaw) {
                        nivelTaw = Integer.parseInt(acces.getTawresultado());
                    }
					
					if (acces.getResultado() != null && Integer.parseInt(acces.getResultado()) > nivelXHTML) {
                        nivelXHTML = Integer.parseInt(acces.getResultado());
                    }
				}
				
				if (nivelTaw > 1) {
                    return nivelTaw;
                } else if (nivelXHTML > 1) {
                    return nivelXHTML + 3;
                } else {
                    return 0;
                }
			} else {
                return 0;
            }

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
    
    /**
     * borra una accesibilidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void borrarAccesibilidad(Long id) {

        Session session = getSession();
        try {
        	Accesibilidad accesibilidad = (Accesibilidad) session.get(Accesibilidad.class, id);
            Microsite site = (Microsite) session.get(Microsite.class, accesibilidad.getCodmicro());
            session.delete(accesibilidad);
            session.flush();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Accesibilidad.class.getSimpleName());
            auditoria.setIdEntidad(accesibilidad.getId().toString());
            auditoria.setMicrosite(site);
            auditoria.setOperacion(Auditoria.ELIMINAR);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }

    /**
     * borra accesibilidad completa del microsite
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void borrarAccesibilidadMicro(Long id) {

        Session session = getSession();
        try {
            Microsite site = (Microsite) session.get(Microsite.class, id);
            Criteria criteri = session.createCriteria(Accesibilidad.class);
            criteri.add(Expression.eq("codmicro", id));
            List<?> lista = criteri.list();
            Iterator<?> iter = lista.iterator();
           
            while (iter.hasNext()) {
                Accesibilidad accesibilidad = (Accesibilidad) iter.next();
            	Transaction tx = session.beginTransaction();
                session.delete(accesibilidad);
                session.flush();
                tx.commit();
            }
            close(session);

            iter = lista.iterator();
            while (iter.hasNext()) {
                Accesibilidad accesibilidad = (Accesibilidad) iter.next();
                Auditoria auditoria = new Auditoria();
                auditoria.setEntidad(Accesibilidad.class.getSimpleName());
                auditoria.setIdEntidad(accesibilidad.getId().toString());
                auditoria.setMicrosite(site);
                auditoria.setOperacion(Auditoria.ELIMINAR);
                DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);
            }
           
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * Devuelve un mapa con listados de diferentes elementos (contenidos, noticias, agendas)
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public HashMap<String, ArrayList<Accesibilidad>> obtenerMapaListados(Long idmicrosite) throws DelegateException {

    	 Session session = getSession();
         try {
        	 HashMap<String, ArrayList<Accesibilidad>> maparetorno = new HashMap<String, ArrayList<Accesibilidad>>();
        	 
        	 //obtenemos los contenidos
        	 String sql = "select contenido, accesi" +
                     " from Contenido contenido, Accesibilidad accesi" +
                     " where accesi.codmicro = " + idmicrosite.toString() +
                     " and accesi.servicio = 'MCS.CNTSP'" +
                     " and accesi.iditem = contenido.id" +
                     " order by contenido.id";

        	 Query query = session.createQuery(sql);
        	 ArrayList<Accesibilidad> listaretorno = new ArrayList<Accesibilidad>();
        	 Iterator<?> iter = query.list().iterator();
        	 while (iter.hasNext()) {
        		 Object[] fila = (Object[]) iter.next();
                 Contenido conte = (Contenido)fila[0];
        		 Accesibilidad accesi = (Accesibilidad)fila[1];
        		 //ponemos servicio el titulo del contenido
        		 TraduccionContenido tracon = (TraduccionContenido) conte.getTraduccion(Idioma.getIdiomaPorDefecto());
        		 if ((tracon != null) && (tracon.getTitulo() != null)) {
                     accesi.setServicio(tracon.getTitulo());
                 }
        		 listaretorno.add(accesi);
        	 }
        	 maparetorno.put("kContenidos", listaretorno);
        	  
        	 //obtenemos las noticias
        	 sql = "select noticia, accesi" +
                     " from Noticia noticia, Accesibilidad accesi" +
                     " where accesi.codmicro = " + idmicrosite.toString() +
                     " and accesi.servicio = 'MCS.NTCS0'" +
                     " and accesi.iditem = noticia.id" +
                     " order by noticia.id";

        	 query = session.createQuery(sql);
        	 listaretorno = new ArrayList<Accesibilidad>();
        	 iter = query.list().iterator();
        	 while (iter.hasNext()) {
        		 Object[] fila = (Object[]) iter.next();
                 Noticia noticia = (Noticia) fila[0];
        		 Accesibilidad accesi = (Accesibilidad)fila[1];
        		 //ponemos servicio el titulo de la noticia
        		 TraduccionNoticia tranot = (TraduccionNoticia) noticia.getTraduccion(Idioma.getIdiomaPorDefecto());
        		 if ((tranot != null) && (tranot.getTitulo() != null)) {
                     accesi.setServicio(tranot.getTitulo());
                 }
        		 listaretorno.add(accesi);
        	 }
        	 maparetorno.put("kNoticias", listaretorno);
        	 
        	 //obtenemos los eventos
        	 sql = "select agenda, accesi" +
                     " from Agenda agenda, Accesibilidad accesi" +
                     " where accesi.codmicro = " + idmicrosite.toString() +
                     " and accesi.servicio = 'MCS.GND00'" +
                     " and accesi.iditem = agenda.id" +
                     " order by agenda.id";

        	 query = session.createQuery(sql);
        	 listaretorno = new ArrayList<Accesibilidad>();
        	 iter = query.list().iterator();
        	 while (iter.hasNext()) {
        		 Object[] fila = (Object[]) iter.next();
        		 Agenda agenda = (Agenda)fila[0];
        		 Accesibilidad accesi = (Accesibilidad) fila[1];
        		 //ponemos servicio el titulo de la agenda
        		 TraduccionAgenda traage = (TraduccionAgenda) agenda.getTraduccion(Idioma.getIdiomaPorDefecto());
        		 if ((traage != null) && (traage.getTitulo() != null)) {
                     accesi.setServicio(traage.getTitulo());
                 }
        		 listaretorno.add(accesi);
        	 }
        	 maparetorno.put("kAgendas", listaretorno);
        	  	
        	 return maparetorno;

         } catch (HibernateException he) {
             throw new EJBException(he);
         } finally {
             close(session);
         }
    }
    
}
