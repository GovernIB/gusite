package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micropersistence.util.DateUtils;

/**
 * SessionBean para consultar Estadistica.
 *
 * @ejb.bean
 *  name="sac/micropersistence/EstadisticaFacade"
 *  jndi-name="es.caib.gusite.micropersistence.EstadisticaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class EstadisticaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 271632211626503906L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta de Estadistica.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina = 50;
    	super.pagina = 0;
    	super.select = "";
    	super.from = " from Estadistica stat ";
    	super.where = " ";
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"stat.item", "stat.referencia", "stat.mes"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }    
    
    /**
     * Crea o actualiza una estadistica
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Long grabarEstadistica(Estadistica estadistica) {

        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(estadistica);
            session.flush();
            tx.commit();
            return estadistica.getId();
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
 
    /**
     * Obtiene una estadistica
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Estadistica obtenerEstadistica(Long id) {

        Session session = getSession();
        try {
        	Estadistica estadistica = (Estadistica) session.get(Estadistica.class, id);
            return estadistica;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una estadistica pasando los parametros idmicrosite,item,ref y mes
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Estadistica obtenerEstadistica(Long idmicrosite, Long item, String ref, Integer mes, Integer publico) {

        Session session = getSession();
        Estadistica estadistica1;
        try {
            List<?> estadisticas = session.createCriteria(Estadistica.class)
                    .add(Restrictions.eq("idmicrosite", idmicrosite))
            		.add(Restrictions.eq("item", item))
                    .add(Restrictions.eq("referencia", ref))
            		.add(Restrictions.eq("mes", mes))
                    .add(Restrictions.eq("publico", publico))
                    .list();

            Estadistica estadistica = null;
            if (estadisticas.isEmpty()) {
                estadistica = new Estadistica(idmicrosite, item, ref, mes.intValue(), publico.intValue());
            } else {
                Iterator<?> iter = estadisticas.iterator();
                if (iter.hasNext()) {
                    estadistica = (Estadistica) iter.next();
                }
            }
            estadistica1 = estadistica;
            return estadistica1;

	    } catch (HibernateException he) {
	        throw new EJBException(he);
	    } finally {
	        close(session);
	    }
    }

    /**
     * Lista todas las Estadistica
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<Estadistica> listarEstadisticas() {

        Session session = getSession();
        try {
            parametrosCons(); // Establecemos los parámetros de la paginación
            Query query = session.createQuery(select + from + where + orderby);
            query.setFirstResult(cursor - 1);
            query.setMaxResults(tampagina);
            return crearlistadostateful(query.list());

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * metodo privado que devuelve un arraylist 'nuevo'.
     * Vuelca el contenido del listado.
     * @param listado
     * @return ArrayList
     */
    private ArrayList<Estadistica> crearlistadostateful(List<?> listado) {

		ArrayList<Estadistica> lista = new ArrayList<Estadistica>();
    	Iterator<?> iter = listado.iterator();
    	Estadistica stat;
         while (iter.hasNext()) {
        	 stat = new Estadistica();
        	 stat = (Estadistica)iter.next();
        	 lista.add(stat);
         }

         return lista;
    }
    
    /**
     * Borra la Estadistica de un MicroSite
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void borrarEstadisticasMicroSite(Long idmicrosite) {

        Session session = getSession();
        try {
            List<?> estadisticas = session.createQuery("from Estadistica as a where a.idmicrosite = " + idmicrosite).list();
            Estadistica estadistica = null;
            if (!estadisticas.isEmpty()) {
                Iterator<?> iter = estadisticas.iterator();
                while (iter.hasNext()) {
                    estadistica = (Estadistica) iter.next();
                    session.delete(estadistica);
                    session.flush();
                }
            }

	    } catch (HibernateException he) {
	        throw new EJBException(he);
	    } finally {
	        close(session);
	    }
    }    
    
    /**
     * Crea la Estadistica de un MicroSite que se sobreescribe
     * estaba.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Long crearEstadisticasMicroReemplazado(Long antiguo, Long nuevo) {

        Session session = getSession();
        try {
            // Recuperamos los datos del microsite y el resumen de estadisticas lo pasamos al nuevo microsite.
            init();
        	parametrosCons();
        	super.select = "select stat.referencia, SUM(stat.accesos)";
            super.from = " from Estadistica stat";
            super.where = " where stat.idmicrosite = " + antiguo.toString();
            String groupby = " group by stat.referencia ";
        	Query query = session.createQuery(select + from + where + groupby + orderby);
            query.setFirstResult(cursor - 1);

            Transaction tx = session.beginTransaction();
            Iterator<?> res = query.iterate();
            int i = 0;
            int publico = 1;
            while (res.hasNext()) {
                Object[] fila = (Object[]) res.next();
                Estadistica stat = new Estadistica();
                stat.setIdmicrosite(nuevo);

                String ref = new String((String) fila[0]);
                if (ref == "MCRST") {
                    stat.setItem(nuevo);
                } else {
                    stat.setItem(new Long(-1));
                }

                stat.setReferencia(ref);
                Long acess = (Long) fila[1];
                stat.setAccesos(acess.intValue());
                stat.setPublico(publico);
                Integer mes = new Integer(DateUtils.formatfechaactual2Stats());
                stat.setMes(mes);
                
                session.saveOrUpdate(stat);
                session.flush();
                i++;
            }
            tx.commit();
            return nuevo;
            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }    
    
}