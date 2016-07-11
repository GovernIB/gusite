package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micromodel.TraduccionComponentePK;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * SessionBean para consultar Componente.
 * 
 * @ejb.bean name="sac/micropersistence/ComponenteFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ComponenteFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class ComponenteFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -1492166558649126596L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta de Componente....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select compo ";
		super.from = " from Componente compo join compo.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and compo.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "compo.nombre", "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Componente....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select compo ";
		super.from = " from Componente compo join compo.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "compo.nombre", "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Componente
	 * @throws IOException 
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarComponente(Componente compo) throws DelegateException, IOException {

		Session session = this.getSession();
		
		Componente original = null;
		Boolean nuevo = false;
		
		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
		Archivo imagen = null;
		
		try {
			
			Transaction tx = session.beginTransaction();
			if (compo.getId() == null) {
				nuevo = true;
			}

			Map<String, TraduccionComponente> listaTraducciones = new HashMap<String, TraduccionComponente>();
			if (nuevo) {
				
				Iterator<TraduccionComponente> it = compo.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionComponente trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				compo.setTraducciones(null);
				
				if (compo.getImagenbul() != null)
					imagen = compo.getImagenbul();
				
				
			} else {
				
				original = obtenerComponente(compo.getId());
				
				if (compo.getImagenbul() != null) {					
					imagen = compo.getImagenbul();
					//Si la imagen que pasa ya existe, entonces hay que buscar su contenido.
					//  Este método y este funcionamiento es de revisarlo y tirarlo, su funcionamiento deja bastante que desear.
					if (imagen.getId() != null && imagen.getDatos() == null) { 
						imagen.setDatos(archivoDelegate.obtenerContenidoFichero(imagen));
					}
				} else {
					
					// Antes había imagen pero ahora ya no la hay: solicitan borrado.
					if (original.getImagenbul() != null)
						archivosPorBorrar.add(original.getImagenbul());
					
				}
								
			}
			
			compo.setImagenbul(null);

			session.saveOrUpdate(compo);
			session.flush();
			
			// Imágenes.
			if (imagen != null) {
				
				if (nuevo) {
					
					archivoDelegate.insertarArchivo(imagen);
					
				} else {
					
					if (original.getImagenbul() != null)
						archivoDelegate.grabarArchivo(imagen);
					else
						archivoDelegate.insertarArchivo(imagen);
					
				}
				
				compo.setImagenbul(imagen);
				
				session.saveOrUpdate(compo);
				session.flush();
				
			}
			
			// Borramos archivos FKs del Componente que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			if (nuevo) {
				for (TraduccionComponente trad : listaTraducciones.values()) {
					final TraduccionComponentePK tradPk = trad.getId();
					tradPk.setCodigoComponente(compo.getId());
					trad.setId(tradPk);
					session.saveOrUpdate(trad);
				}
				session.flush();
				compo.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(compo, op);

			return compo.getId();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Obtiene un Componente
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Componente obtenerComponente(Long id) {

		Session session = this.getSession();
		try {
			Componente compo = (Componente) session.get(Componente.class, id);
			return compo;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Componentes
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Componente> listarComponentes() {

		Session session = this.getSession();
		List<Componente> componentes = new ArrayList<Componente>();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			query.setFetchSize(this.tampagina); //Se fija la cantidad de resultados en cada acceso
			
			Iterator<Componente> res = query.iterate();
			while (res.hasNext()) {
				
				Componente comp = res.next();
				componentes.add(comp);
			}
			
			
			return componentes;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un Componente
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarComponente(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			Componente componente = (Componente) session.get(Componente.class, id);

			Long idImagen = (componente.getImagenbul() != null) ? componente.getImagenbul().getId() : null;
			
			session.createQuery("delete from TraduccionComponente tc where tc.id.codigoComponente = " + id).executeUpdate();
			session.createQuery("delete from Componente compo where compo.id = " + id).executeUpdate();
			
			if (idImagen != null)
				archivoDelegate.borrarArchivo(idImagen);
			
			session.flush();
			tx.commit();
			
			this.close(session);

			this.grabarAuditoria(componente, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Comprueba que el componente pertenece al Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(Long site, Long id) {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("from Componente compo where compo.idmicrosite = "
							+ site.toString()
							+ " and compo.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
