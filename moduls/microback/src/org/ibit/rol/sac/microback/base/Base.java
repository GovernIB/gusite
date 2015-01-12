package org.ibit.rol.sac.microback.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microback.Microback;
import org.ibit.rol.sac.microback.base.bean.Pardato;
import org.ibit.rol.sac.microback.utils.Cadenas;
import org.ibit.rol.sac.microback.utils.Fechas;

import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.Tipo;
import org.ibit.rol.sac.micromodel.Tiposervicio;
import org.ibit.rol.sac.micromodel.TraduccionEncuesta;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TiposervicioDelegate;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;
import org.ibit.rol.sac.micromodel.Usuario;

/**
 * Clase básica para manejar información.
 * 
 * Elementos que van a estar en sesion:<BR>
 * MVS_microsite: (Microsite) El microsite actual<BR> 
 * MVS_menugenerico: (ArrayList) Lista de menu generico(el bean es Pardato)<BR>
 * MVS_menuespecifico: (ArrayList) Lista de menu de contenidos específicos (el bean es Micromenu)<BR> 
 * MVS_menusinmenu: (ArrayList) Lista de menu sin menu(el bean es Pardato)<BR>
 * username: login del usuario logado<BR>
 * rolesnames: hashtable con los roles del usuario<BR>
 * rolesnamestxt: string informativo de los roles del usuario<BR>
 * MVS_usuario: bean de usuario<BR>
 * MVS_rol_sys_adm: 'yes' si es system o admin, 'no' si no tienen ninguno de esos perfiles 
 * 
 * @author Indra
 *
 */

public class Base {

	protected static Log log = LogFactory.getLog(Base.class);
	
	private static String[] roles = new String[]{"sacsystem", "sacadmin", "sacoper", "sacsuper", "sacindra"};	
	
	/**
	 * Método estático que recarga en sesion el microsite.
	 * Variables que mete en sesion:
	 * MVS_microsite: bean microsite completo
	 * tituloMicro: string informativo con el titulo del microsite
	 * @param idmicrosite
	 * @param request
	 * @throws Exception
	 */
	public static void micrositeRefresh(Long idmicrosite, HttpServletRequest request) throws Exception  {
		
		try {
			//meter en sesion el microsite
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			Microsite microsite = micrositedel.obtenerMicrosite(idmicrosite);
			microsite.setFuncionalidadTraduccion();
			
			request.getSession().setAttribute("MVS_microsite",microsite);
			request.getSession().setAttribute("tituloMicro", ((TraduccionMicrosite)microsite.getTraduccion()).getTitulo() );
			
		} catch (Exception e) {
			throw new Exception("org.ibit.rol.sac.microback.base.base.micrositeRefresh-->Error: " + e.getMessage());				
		}
		
	}
	
	/**
	 * Método estático que recarga en sesion el microsite.
	 * Variables que mete en sesion:
	 * MVS_microsite: bean microsite completo
	 * tituloMicro: string informativo con el titulo del microsite
	 * @param micrositeBean
	 * @param request
	 * @throws Exception
	 */
	public static void micrositeRefreshByBean(Microsite micrositeBean, HttpServletRequest request) throws Exception  {
		
		TiposervicioDelegate tipoServicioDelegate = DelegateUtil.getTiposervicioDelegate();
		
		try {
			//meter en sesion el microsite
        	micrositeBean.setMvsUrlMigapan(Base.obtenerMigaContenidoFromURI(micrositeBean.getUrlcampanya()));
        	micrositeBean.setTiposServicios(tipoServicioDelegate.listarTipos());
        	micrositeBean.setFuncionalidadTraduccion();
        	
        	request.getSession().setAttribute("MVS_microsite", micrositeBean);
			request.getSession().setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
			
		} catch (Exception e) {
			throw new Exception("org.ibit.rol.sac.microback.base.base.micrositeRefresh-->Error: " + e.getMessage());				
		}
		
	}	
	
	/**
	 * Método estatico que recarga los mencus dinámicos.
	 * Variables que mete en sesion:
	 * MVS_menugenerico: menú generico
	 * @param request
	 * @throws Exception
	 */
	public static void menuRefresh(HttpServletRequest request) throws Exception {
		request.getSession().setAttribute("MVS_menuespecifico",null);
		request.getSession().setAttribute("MVS_menugenerico",montarmenugenerico(request));
		request.getSession().setAttribute("MVS_menusinmenu",null);
	}
	
	/**
	 * Método estatico que borra todas las variables de sesión relacionadas con el microsite
	 * @param request
	 */
	public static void borrarVSession(HttpServletRequest request) {
		request.getSession().removeAttribute("tituloMicro");
		request.getSession().removeAttribute("MVS_microsite");
		request.getSession().removeAttribute("MVS_menuespecifico");
		request.getSession().removeAttribute("MVS_menugenerico");
		request.getSession().removeAttribute("MVS_menusinmenu");
	}
	
	
	
	/**
	 * Método estatico que mete en sesion el usuario logado y sus roles.
	 * Variables que se meten en sesion:
	 * username: login del usuario logado
	 * rolesnames: hashtable con los roles del usuario
	 * rolesnamestxt: string informativo de los roles del usuario
	 * MVS_usuario: bean de usuario
	 * MVS_rol_sys_adm: 'yes' si es system o admin, 'no' si no tienen ninguno de esos perfiles
	 * @param request
	 * @throws Exception
	 */
	public static void usuarioRefresh(HttpServletRequest request) throws Exception {

    	//recoger usuario..... 
    	UsuarioDelegate usudel= DelegateUtil.getUsuarioDelegate();
    	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
    	
    	// meter USUARIO Y ROLES en sesion
		Hashtable<String, String> rolenames=null;
		String rolenamestxt="";
    	if (request.getRemoteUser() != null) {
    		request.getSession().setAttribute("username", request.getRemoteUser());
            rolenames = new Hashtable<String, String>();
            for (int i = 0; i < roles.length; i++) {
                if (request.isUserInRole(roles[i])) {
                    rolenames.put(roles[i],roles[i]);
                    rolenamestxt+= roles[i] + ", ";
                }
            }
            rolenamestxt=rolenamestxt.substring(0,rolenamestxt.length()-2);
            request.getSession().setAttribute("rolenames", rolenames);
            request.getSession().setAttribute("rolenamestxt", "["+rolenamestxt+"]");
            //meter en sesion si el usuario es system o admin
            if ((rolenames.contains(roles[0])) || (rolenames.contains(roles[1])))
            	request.getSession().setAttribute("MVS_rol_sys_adm", "yes");
            else { 
            	if(rolenames.contains(roles[3])) request.getSession().setAttribute("MVS_rol_super", "yes");

            	request.getSession().setAttribute("MVS_rol_sys_adm", "no");
            }
        }        
    	 
        
    	request.getSession().setAttribute("MVS_usuario", usu);	
    	request.getSession().setAttribute("MVS_einaversion", Microback.microsites_name + " v" + Microback.microsites_version + " build: " + Microback.microsites_build);
	}
	

	/**
	 * Método que comprueba si un usuario tiene acceso a un microsite
	 * @param request
	 * @param idmicrosite
	 * @return 'true' en caso de tener acceso, 'false' en caso de no tener acceso o producirse un error
	 */
	public static boolean hasMicrositePermiso(HttpServletRequest request, Long idmicrosite) {
		
		boolean retorno=false;
    	
		try {
			//obtener microsite
			MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
			Microsite micrositep = bdMicro.obtenerMicrosite(idmicrosite);
			
			//obtener usuario
	    	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
	    	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
	        
	    	//obtener listado microsites del usuario
	    	List<?> micros = bdMicro.listarMicrositesbyUser(usu);
	    	Iterator<?> iter = micros.iterator();
	    	while (iter.hasNext()) {
	    		Microsite micro = (Microsite)iter.next();
	    		if (micro.getId().longValue()==micrositep.getId().longValue()) {
	    			retorno=true;  
	    			break;
	    		}
	    	}
	    	/*
	    	//obtener uo
	    	UnidadAdministrativaDelegate uniad=org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();
	        UnidadAdministrativa uo = null;
        
        	uo = uniad.obtenerUnidadAdministrativa(new Long(micro.getUnidadAdministrativa()));
        	
            if (usu.hasAccess(uo)) retorno=true;
            */        	
        } catch (Exception e) {
        	log.warn("[AVISO] se ha producido un error comprobando [microsite, usuario registrado, uo]");
        }
    	      
		return retorno;
	}	
	
	
	/**
	 * Monta la lista de menu del microback de los contenidos genéricos.
	 * @param request
	 * @return ArrayList
	 * @throws Exception
	 */
	private static ArrayList<Pardato> montarmenugenerico(HttpServletRequest request) throws Exception {
		
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		ArrayList<Pardato> listadeotros = new ArrayList<Pardato>();
		ArrayList<?> listadeofr = new ArrayList<Object>();
		
		try {
			if (microsite.getServiciosOfrecidos()!=null)
				listadeofr = Cadenas.getArrayListFromString(microsite.getServiciosOfrecidos());
			
			Iterator<?> iter=listadeofr.iterator();
			while (iter.hasNext()) {
				TiposervicioDelegate tiposerdel= DelegateUtil.getTiposervicioDelegate();
				Tiposervicio tiposervicio = tiposerdel.obtenerTiposervicio(new Long((String)iter.next()));
				String idtipo = "" + tiposervicio.getId();
				
				//tinglao para sacar los tipos de elementos
				if (tiposervicio.getReferencia().equals(Microback.RNOTICIA)) {
					
					//se saca sólo el listado de tipos
					Pardato pardatotipo = new Pardato("tipos.do?mntnmnt=yes", "Llistats");
		    		listadeotros.add(pardatotipo);
					
					
				} else {
					if (!idtipo.equals("100")){
			    		Pardato pardato = new Pardato(tiposervicio.getUrl(),tiposervicio.getNombre());
			    		listadeotros.add(pardato);
					}
				}
			}
		} catch (Exception e) {
			listadeotros = new ArrayList<Pardato>();
			throw new Exception("org.ibit.rol.sac.microback.base.base.montarmenugenerico-->Error: " + e.getMessage());
		}

		return listadeotros;
	}

	
	/**
	 * Retorna si el usuario logado es System o Administrador
	 * @param request
	 * @return boolean
	 */
	public static boolean isUserSysOradmin(HttpServletRequest request) {
		String sysOradmin = (String)request.getSession().getAttribute("MVS_rol_sys_adm");
		
		return sysOradmin.equals("yes");
	}

	
	/**
	 * Método que devuelve si el servicio que se soilicita es ofrecido o no por el microsite
	 */
	public static boolean existeServicio(Microsite microsite, String refservicio) throws Exception {
		boolean tmp=false;
		ArrayList<?> listserofr=new ArrayList<Object>();
			
		
				if (microsite.getServiciosOfrecidos()!=null)
					listserofr = Cadenas.getArrayListFromString(microsite.getServiciosOfrecidos());
				
				Iterator<?> iter=listserofr.iterator();
				while (iter.hasNext()) {

					TiposervicioDelegate tiposerdel= DelegateUtil.getTiposervicioDelegate();
					Tiposervicio tiposervicio = tiposerdel.obtenerTiposervicio(new Long((String)iter.next()));
					
					
					if (tiposervicio.getReferencia().equals(refservicio)){
						tmp=true;
					}
					
					
				}
		
		return tmp;
	}	
	
	
	/**
	 * Método que devuelve un string con la miga de pan de una uri del microsite.
	 * 
	 * @param uri
	 * @return String informando de la uri
	 * @throws Exception
	 */
	public static String obtenerMigaContenidoFromURI(String uri) throws Exception {
		
		String tmp=null;
		
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		
		tmp=rb.getString("url.info.nourl");
		
		try {
			if ((uri!=null) && (uri.length()>0)) {
				//contenido, listados, faqs, agenda, contacto, encuestas, procedimientos
				if (uri.indexOf("mapa.do")!=-1) {
					tmp=rb.getString("url.info.mapa");
				}				
				if (uri.indexOf("faqs.do")!=-1) {
					tmp=rb.getString("url.info.faqs");
				}
				if (uri.indexOf("procedimientos.do")!=-1) {
					tmp=rb.getString("url.info.procs");
				}		
				if (uri.indexOf("agendas.do")!=-1) {
					tmp=rb.getString("url.info.agenda");
				}
				if (uri.indexOf("contactos.do")!=-1) {
					tmp=rb.getString("url.info.contactos");
				}								
				if (uri.indexOf("encuestas.do")!=-1) {
					tmp=rb.getString("url.info.encuestas");
				}
				if (uri.indexOf("convocatoria.do")!=-1) {
					tmp=rb.getString("url.info.convocatoria");
				}								
				if (uri.indexOf("contenido.do")!=-1) {
					String pcont=Cadenas.getValueParameter(uri, "cont");
					if (!pcont.equals("-1")) {
						ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
						Contenido conte = bdConte.obtenerContenido(new Long(pcont));
						tmp = bdConte.migapan( ""+conte.getIdmenu(),conte.getId()) ;
					} else {
						tmp=rb.getString("url.info.mal");
					}
				}
				if (uri.indexOf("agenda.do")!=-1) {
					String pcont=Cadenas.getValueParameter(uri, "cont");
					if (!pcont.equals("-1")) {
						Date fecha = Fechas.string2date(pcont); 
        				java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("dd/MM/yyyy");
						tmp=rb.getString("url.info.eventos") + " " + dia.format(fecha);
					} else {
						tmp=rb.getString("url.info.mal");
					}
				}				
				if (uri.indexOf("encuesta.do")!=-1) {
					String pcont=Cadenas.getValueParameter(uri, "cont");
					if (!pcont.equals("-1")) {
						EncuestaDelegate encdel = DelegateUtil.getEncuestaDelegate();
						Encuesta encuesta = encdel.obtenerEncuesta(new Long(pcont));
						tmp = rb.getString("url.info.encuesta") + ": " + ((TraduccionEncuesta)encuesta.getTraduccion()).getTitulo() ;
					} else {
						tmp=rb.getString("url.info.mal");
					}
				}
				if (uri.indexOf("noticias.do")!=-1) {
					String pcont=Cadenas.getValueParameter(uri, "tipo");
					if (!pcont.equals("-1")) {
						TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
						Tipo tipo = tipodel.obtenerTipo(new Long(pcont));
						tmp = rb.getString("url.info.listado") + ": " + ((TraduccionTipo)tipo.getTraduccion()).getNombre() ;
					} else {
						tmp=rb.getString("url.info.mal");
					}
				}				
				if (uri.indexOf("noticia.do")!=-1) {
					String pcont=Cadenas.getValueParameter(uri, "cont");
					if (!pcont.equals("-1")) {
						NoticiaDelegate notidel = DelegateUtil.getNoticiasDelegate();
						Noticia noti = notidel.obtenerNoticia(new Long(pcont));
						tmp = rb.getString("url.info.elemento") + ": " + ((TraduccionNoticia)noti.getTraduccion()).getTitulo();
					} else {
						tmp=rb.getString("url.info.mal");
					}
				}				
				
				
			} else {
				tmp=rb.getString("url.info.vacia");
			}
		} catch (Exception e) {
			tmp=rb.getString("url.info.mal");
		}
		
		return tmp;
	}		
	
	

	
}
