package es.caib.gusite.micropersistence.ejb;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.plugins.PluginDominio;

/**
 * SessionBean para manipular los tipos de Noticias
 *
 * @ejb.bean name="sac/micropersistence/TipoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.TipoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * @author Indra
 */

public abstract class TipoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 4442710019314005495L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(final Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select tipo ";
		super.from = " from Tipo tipo join tipo.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and tipo.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre", "tipo.clasificacion" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(final Long site, final String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select tipo.id,trad.nombre ";
		super.from = " from Tipo tipo join tipo.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado + "') and tipo.idmicrosite = " + site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select tipo ";
		super.from = " from Tipo tipo join tipo.traducciones trad ";
		super.where = " where trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un tipo de noticia
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarTipo(final Tipo tipo) {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (tipo.getId() == null) ? true : false;
			final Transaction tx = session.beginTransaction();

			final Map<String, TraduccionTipo> listaTraducciones = new HashMap<String, TraduccionTipo>();
			if (nuevo) {
				final Iterator<TraduccionTipo> it = tipo.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionTipo trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				tipo.setTraducciones(null);
			} else {// #28 Incidencia borrando traducciones
				String listIdiomaBorrar = "";
				final Iterator<TraduccionTipo> it = tipo.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionTipo trd = it.next();
					listIdiomaBorrar += "'" + trd.getId().getCodigoIdioma() + "'";
					if (it.hasNext()) {
						listIdiomaBorrar += ",";
					}
				}
				// Borramos los idiomas que no pertenecen a contenido y existen en la BBDD
				if (!listIdiomaBorrar.isEmpty()) {
					final Query query = session
							.createQuery("select tradTip from TraduccionTipo tradTip where tradTip.id.codigoTipo = "
									+ tipo.getId() + " and tradTip.id.codigoIdioma not in (" + listIdiomaBorrar + ") ");
					final List<TraduccionTipo> traduciones = query.list();
					for (final TraduccionTipo traducI : traduciones) {
						session.delete(traducI);
					}
					session.flush();
				}
			}

			session.saveOrUpdate(tipo);
			session.flush();

			if (nuevo) {
				for (final TraduccionTipo trad : listaTraducciones.values()) {
					trad.getId().setCodigoTipo(tipo.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				tipo.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(tipo, op);

			return tipo.getId();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un tipo de noticia
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Tipo obtenerTipo(final Long id) {

		final Session session = this.getSession();
		try {
			final Tipo tipo = (Tipo) session.get(Tipo.class, id);
			return tipo;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Tipo a partir de la URI
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Tipo obtenerTipoDesdeUri(final String idioma, final String uri, final String site) {

		final Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session.createQuery(
						"select tipo from Tipo tipo JOIN tipo.traducciones tt where tt.id.codigoIdioma = :idioma and tt.uri = :uri and tipo.idmicrosite = :site");
				query.setParameter("idioma", idioma);

			} else {
				query = session.createQuery(
						"select tipo from Tipo tipo JOIN tipo.traducciones tt where tt.uri = :uri and tipo.idmicrosite = :site");
			}
			query.setParameter("uri", uri);
			query.setParameter("site", Long.valueOf(site));
			query.setMaxResults(1);
			return (Tipo) query.uniqueResult();

		} catch (final ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Tipo();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene los valores del dominio
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Map<?, ?> obtenerListado(final Long id, final Map<?, ?> parametros) {

		final Session session = this.getSession();
		try {
			final Tipo tipo = (Tipo) session.get(Tipo.class, id);
			final PluginDominio plgDominio = new PluginDominio();
			return plgDominio.obtenerListado(tipo, parametros);

		} catch (final javax.naming.NamingException ne) {
			throw new EJBException(ne);
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} catch (final Exception e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los tipos de noticias
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarTipos() {

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los tipos
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Tipo> listarTiposrec(final String idiomapasado) {

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			final ArrayList<Tipo> lista = new ArrayList<Tipo>();
			final ScrollableResults scr = query.scroll();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				final Object[] fila = scr.get();
				final Tipo tip = new Tipo();
				tip.setId((Long) fila[0]);
				final TraduccionTipo tratipo = new TraduccionTipo();
				tratipo.setNombre((String) fila[1]);
				tip.setTraduccion(idiomapasado, tratipo);
				lista.add(tip);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un tipo de noticia
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarTipo(final Long id) {
		borrarTipo(id, true);
	}

	/**
	 * borra un tipo de noticia
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarTipo(final Long id, final boolean indexar) {

		final Session session = this.getSession();
		try {
			final Tipo tipo = (Tipo) session.get(Tipo.class, id);

			final Transaction tx = session.beginTransaction();
			session.createQuery("delete from TraduccionTipo tt where tt.id.codigoTipo = " + id).executeUpdate();
			session.createQuery("delete from Tipo t where t.id = " + id).executeUpdate();
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(tipo, Auditoria.ELIMINAR);

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Retorna la query que Lista todos los tipos de noticias para usar en Combos
	 */
	private String listarComboQuery(final Long idmicrosite, final String filtro) {
		return "select tipo" + " from Tipo tipo" + " join tipo.traducciones trad" + " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'" + " and tipo.idmicrosite = " + idmicrosite.toString() + filtro
				+ " order by tipo.tipoelemento ";
	}

	/**
	 * Lista todos los tipos de noticias para usar en Combos
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarCombo(final Long idmicrosite) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(listarComboQuery(idmicrosite, ""));
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los tipos de noticias para usar en Combos, filtrando los tipos
	 * indicados
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarCombo(final Long idmicrosite, final ArrayList<String> tiposNoIncluidos) {
		String filtro = "";
		if (tiposNoIncluidos != null && tiposNoIncluidos.size() > 0) {
			final boolean inicial = true;
			for (final String tipo : tiposNoIncluidos) {
				if (!inicial) {
					filtro += ",";
				}
				filtro += tipo;
			}
			if (!StringUtils.isEmpty(filtro.trim())) {
				filtro = " and tipo.tipoelemento not in ('" + filtro + "') ";
			}
		}
		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(listarComboQuery(idmicrosite, filtro));
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(final Long site, final Long id) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery("select tipo from Tipo tipo where tipo.idmicrosite = "
					+ site.toString() + " and tipo.id = " + id.toString());
			return query.list().isEmpty();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Establece el filtro del tipo. Si es true devolverá sólo los externos. Si es
	 * false, devolverá todos menos los externos.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void setFiltroExterno(final boolean externos) {

		String filtro = " ";
		if (externos) {
			filtro = " tipo.tipoelemento='" + Tipo.TIPO_CONEXIO_EXTERNA + "'";
		} else {
			filtro = " tipo.tipoelemento<>'" + Tipo.TIPO_CONEXIO_EXTERNA + "'";
		}

		if (this.where.length() > 0) {
			this.where = this.where + " AND (" + filtro + ")";
		} else {
			this.where = " where " + filtro;
		}
	}

	/**
	 * Lista todas las distintas clasificaciones de un tipo de noticias
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Tipo> comboClasificacion(final Long idmicrosite) {

		final Session session = this.getSession();
		try {
			String hql = "select distinct tipo.clasificacion from Tipo tipo";
			hql += " where tipo.idmicrosite = " + idmicrosite.toString() + " order by tipo.clasificacion ";
			final Query query = session.createQuery(hql);
			final List<?> lista = query.list();
			final List<Tipo> clasif = new ArrayList<Tipo>();

			final Iterator<?> it = lista.iterator();
			while (it.hasNext()) {
				final String nombre = (String) it.next();
				if (nombre != null) {
					final Tipo tp = new Tipo();
					tp.setClasificacion(nombre);
					clasif.add(tp);
				}
			}
			return clasif;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene los valores del dominio
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String obtenerPegoteHTMLExterno(final Long id, final Map<?, ?> parametros) {

		final Session session = this.getSession();
		try {
			final Tipo tipo = (Tipo) session.get(Tipo.class, id);
			final Iterator<?> iter = parametros.keySet().iterator();
			String laurl = tipo.getXurl();
			if (laurl.indexOf("?") == -1) {
				laurl += "?wbxtrn";
			}

			while (iter.hasNext()) {
				final String paramkey = (String) iter.next();
				final String paramvalue = (String) parametros.get(paramkey);
				laurl += "&" + paramkey + "=" + paramvalue;
			}
			return this.getHTTPEXTERNO(laurl);

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} catch (final Exception e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	private String getHTTPEXTERNO(final String laurl) {

		String str = "";
		try {
			final HttpURLConnection connection = (HttpURLConnection) new URL(laurl).openConnection();
			connection.connect();

			final DataInputStream dis = new DataInputStream(connection.getInputStream());
			String inputLine;

			while ((inputLine = dis.readLine()) != null) {
				str += inputLine + "\n";
			}
			dis.close();
			return str;

		} catch (final MalformedURLException e) {
			log.error("La URL no es válida: " + laurl + " " + e);
			str = "No hi ha conexió amb el servidor extern.";
		} catch (final IOException e) {
			log.error("No puedo conectar con " + laurl + " " + e);
			str = "No hi ha conexió amb el servidor extern.";
		}

		return str;
	}

}
