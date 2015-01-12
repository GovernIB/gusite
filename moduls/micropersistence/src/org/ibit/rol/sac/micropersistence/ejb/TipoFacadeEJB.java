package org.ibit.rol.sac.micropersistence.ejb;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Tipo;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.plugins.PluginDominio;

/**
 * SessionBean para manipular los tipos de Noticias
 *
 * @ejb.bean
 *  name="sac/micropersistence/TipoFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.TipoFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
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
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Tipo tipo join tipo.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and tipo.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre", "tipo.clasificacion"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }
    /**
     * Inicializo los parámetros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site,String idiomapasado) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="select tipo.id,trad.nombre ";
    	super.from=" from Tipo tipo join tipo.traducciones trad ";
    	super.where=" where (index(trad)='"+Idioma.DEFAULT+"' or index(trad)='"+idiomapasado+"') and tipo.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }
    /**
     * Inicializo los parámetros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Tipo tipo join tipo.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }    
    
    /**
     * Crea o actualiza un tipo de noticia
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarTipo(Tipo tipo) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(tipo);
            session.flush();
            tx.commit();
            return tipo.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un tipo de noticia
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Tipo obtenerTipo(Long id) {
        Session session = getSession();
        try {
        	Tipo tipo = (Tipo) session.load(Tipo.class, id);
            return tipo;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Obtiene los valores del dominio
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Map<?, ?> obtenerListado(Long id, Map<?, ?> parametros) {
    	
        Session session = getSession();
        try {
        	Tipo tipo = (Tipo) session.load(Tipo.class, id);
        	PluginDominio plgDominio = new PluginDominio();
        	return plgDominio.obtenerListado(tipo, parametros);
        } catch (javax.naming.NamingException ne) {
        	throw new EJBException(ne);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (Exception e) {
            throw new EJBException(e);
		} finally {
            close(session);
        }
    }

    /**
     * Lista todos los tipos de noticias
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarTipos() {
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
     * Lista todos los tipos  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    
    public List<Tipo> listarTiposrec(String idiomapasado) {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
       	
        	Query query = session.createQuery(select+from+where+orderby);
        	ArrayList<Tipo> lista= new ArrayList<Tipo>(); 
        	ScrollableResults scr = query.scroll();
        	scr.first();
        	scr.scroll(cursor-1);
        	int i = 0;
            while (tampagina > i++) {
            Object[] fila = (Object[]) scr.get();
            Tipo tip =  new Tipo();
           	tip.setId((Long)fila[0]);
           	TraduccionTipo tratipo = new TraduccionTipo();
           	tratipo.setNombre((String)fila[1]);
          	tip.setTraduccion(idiomapasado,tratipo);
          	lista.add(tip);
          	if (!scr.next()) break;
            }
            scr.close();
        	return lista;
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
 

    /**
     * borra un tipo de noticia
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarTipo(Long id) {
        Session session = getSession();
        try {
        	Tipo tipo = (Tipo) session.load(Tipo.class, id);
            session.delete(tipo);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
   /**
     * Lista todos los tipos de noticias para usar en Combos
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCombo(Long idmicrosite) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Tipo tipo join tipo.traducciones trad where index(trad)='"+Idioma.DEFAULT+"' and tipo.idmicrosite=" + idmicrosite.toString() + " order by tipo.tipoelemento ");
        	return query.list();
        	
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
        	Query query = session.createQuery("from Tipo tipo where tipo.idmicrosite="+site.toString()+" and tipo.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Establece el filtro del tipo. 
     * Si es true devolverá sólo los externos. Si es false, devolverá todos menos los externos.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void setFiltroExterno(boolean externos) 
    {
        
    	String filtro=" ";
        if (externos) 
        	filtro = " tipo.tipoelemento='" + Tipo.TIPO_CONEXIO_EXTERNA + "'";
        else 
        	filtro = " tipo.tipoelemento<>'" + Tipo.TIPO_CONEXIO_EXTERNA + "'";


        if (where.length()>0)
            where=where+" AND ("+filtro+")";
        else
            where=" where "+filtro;

    }
 
    
    /**
     * Lista todas las distintas clasificaciones de un tipo de noticias
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<Tipo> comboClasificacion(Long idmicrosite) {
        Session session = getSession();
        try {
        	String hql = "select distinct tipo.clasificacion from Tipo tipo";
        	hql+=" where tipo.idmicrosite=" + idmicrosite.toString() + " order by tipo.clasificacion ";
        	Query query = session.createQuery(hql);
        	List<?> lista = query.list();
        	List<Tipo> clasif = new ArrayList<Tipo>();
        	
        	Iterator<?> it=lista.iterator();
        	while (it.hasNext()) {
        		String nombre = (String)it.next();
        		if (nombre!=null) {
        			Tipo tp = new Tipo();
        			tp.setClasificacion(nombre);
        			clasif.add(tp);
        		}
        	}
        	return clasif;
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene los valores del dominio
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public String obtenerPegoteHTMLExterno(Long id, Map<?, ?> parametros) {
    	
        Session session = getSession();
        try {
        	
        	Tipo tipo = (Tipo) session.load(Tipo.class, id);
        	Iterator<?> iter = parametros.keySet().iterator();
        	String laurl = tipo.getXurl();
        	if (laurl.indexOf("?")==-1)
        		laurl += "?wbxtrn";
        	while (iter.hasNext()) {
        		String paramkey = (String)iter.next();
        		String paramvalue = (String)parametros.get(paramkey);
        		laurl += "&" + paramkey + "=" + paramvalue;
        	}
        	return getHTTPEXTERNO ( laurl) ;
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (Exception e) {
            throw new EJBException(e);
		} finally {
            close(session);
        }
    }
    
    private String getHTTPEXTERNO(String laurl) {
	    
    	String str="";
		try {

				HttpURLConnection connection = (HttpURLConnection)new URL(laurl).openConnection();
	            connection.connect();
	            
	            DataInputStream dis = new DataInputStream(connection.getInputStream());
	            String inputLine;
	    	    
	            while ((inputLine = dis.readLine()) != null) {
	            	str+=inputLine+"\n";
	            }
	            dis.close();
	            return str;
		    	
	    } catch (MalformedURLException e) {
	    	log.error("La URL no es válida: "+ laurl+ " "+e);
	      	str="No hi ha conexió amb el servidor extern.";
	    } catch (IOException e) {
	    	log.error("No puedo conectar con "+ laurl+ " "+e);
	    	str="No hi ha conexió amb el servidor extern.";
	    }
	    return str;
	    
	}
    
}