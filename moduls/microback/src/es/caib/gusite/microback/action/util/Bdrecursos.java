package es.caib.gusite.microback.action.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.base.bean.MenuRecurso;
import es.caib.gusite.microback.base.bean.Recurso;
import es.caib.gusite.microback.utils.Cadenas;
import es.caib.gusite.microback.utils.Fechas;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;
import es.caib.gusite.micropersistence.delegate.TiposervicioDelegate;

/**
 * Clase que es llamada desde la action de mostrar las urls de un site (RecursosAction).
 * 
 * @author Indra
 *
 */
public class Bdrecursos {
	
	private String _Hcarpetaroig = "0";
	private String _Hcarpetagroc = "1";
	private String _Hpagina = "10";
	
	private ArrayList<Object> listaoriginal = new ArrayList<Object>();
	private ArrayList<MenuRecurso> listaarbol = new ArrayList<MenuRecurso>();
	private ArrayList<MenuRecurso> listaarchivos = new ArrayList<MenuRecurso>();
	private ArrayList<Object> listaarchivoscontenido = new ArrayList<Object>();	
	
	private Microsite microsite;
	private Long idcontenidolong=null;
	private String homeDesactivado=null;
	private String id=null;
	private String idMenu=null;
	
	protected static Log log = LogFactory.getLog(Bdrecursos.class);

	  public Bdrecursos(HttpServletRequest request) {
		  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		   
		  
		  String idcontenido = "" + request.getParameter("page");
		  try { 
		    	idcontenidolong=new Long(idcontenido);
		  } catch (Exception e) {
		    	idcontenido="null";
		  }
		  	
		  this.homeDesactivado = "" + request.getParameter("homedesactivado");
		  this.id = "" + request.getParameter("id");	
		  this.idMenu = "" + request.getParameter("idMenu");		  
	  } 
	  
	 /**
	 * Método que obtiene las urls de un microsite
	 * @param idiomapasado
	 */
	public void doObtainUrls(String idiomapasado) {		 	
		  montartodoURLs(idiomapasado);
		  montartodoArchivos(idiomapasado);
		  montartodoArchivosContenido(idiomapasado);
		  montartodoArbol(idiomapasado);
	  }
	  
	/**
	 * Método que obtiene las urls de documentos de un microsite 
	 * @param idiomapasado
	 */
	public void doObtainDocumentos(String idiomapasado) {
		  montartodoArchivos(idiomapasado);
		  montartodoArchivosContenido(idiomapasado);
	  }
	  
	  /***************************************************************/
	  /****************   LISTAR URLS GENERICAS    *******************/
	  /***************************************************************/
	  
	/**
	 * Método que guarda un listado con todas las urls de un microsite según un idioma
	 * 
	 * @param idiomapasado Id de idioma
	 */
	private void montartodoURLs(String idiomapasado) {
		
		try {

			TraduccionMicrosite microtrad = (TraduccionMicrosite) microsite.getTraduccion(idiomapasado);
			if (microtrad == null)
				microtrad = (TraduccionMicrosite) microsite.getTraduccion(Idioma.getIdiomaPorDefecto());
			
			String micronombre = microtrad.getTitulo();

			//Se añade para que no añada el elemento home cuando se llama a recursos desde página de inicio
			if (homeDesactivado != null && !homeDesactivado.isEmpty() && "true".equals(homeDesactivado)) {
				listaoriginal = new ArrayList<Object>();
			} else {
				listaoriginal = listarhome();
			}
			listaoriginal.addAll(listarmapa());

			ArrayList<?> listserofr = new ArrayList<Object>();
			if (microsite.getServiciosOfrecidos() != null)
				listserofr = Cadenas.getArrayListFromString(microsite.getServiciosOfrecidos());

			Iterator<?> iter = listserofr.iterator();
			while (iter.hasNext()) {

				TiposervicioDelegate tiposerdel = DelegateUtil.getTiposervicioDelegate();
				Tiposervicio tiposervicio = tiposerdel.obtenerTiposervicio(new Long((String) iter.next()));

				if (tiposervicio.getReferencia().equals(Microback.RAGENDA)) {
					listaoriginal.addAll(listaragenda(idiomapasado, micronombre));
				}
				else if (tiposervicio.getReferencia().equals(Microback.RCONTACTO)) {
					listaoriginal.addAll(listarcontactos(idiomapasado, micronombre));
				}
				else if (tiposervicio.getReferencia().equals(Microback.RFAQ)) {
					listaoriginal.addAll(listarfaqs(idiomapasado, micronombre));
				}
				else if (tiposervicio.getReferencia().equals(Microback.RNOTICIA)) {
					listaoriginal.addAll(listarnoticias(idiomapasado, micronombre));
				}
				else if (tiposervicio.getReferencia().equals(Microback.RENCUESTA)) {
					listaoriginal.addAll(listarencuestas(idiomapasado, micronombre));
				}
				else if (tiposervicio.getReferencia().equals(Microback.RQSSI)) {
					listaoriginal.addAll(listarqssi(idiomapasado, micronombre));
				}

			}

		} catch (Exception e) {
			
			log.error("[montartodoURLs] " + e.getMessage());
			listaoriginal = new ArrayList();
			
		}

	}
	  
  
	/**
	 * Método que guarda un listado con las urls de la home de un microsite
	 * @return ArrayList
	 */
	private ArrayList<Object> listarhome() {
		  ArrayList<Object> lista = new ArrayList<Object>();
		  Recurso recurso = new Recurso();
		  recurso.setTipo("");
		  recurso.setTitulo("Home");
		  // si se quiere traduccion para aleman e ingles deberia preguntarse por idioma
		  //o bien  ponerlo en fichero de properties como un Resource Boundle...
		  recurso.setUrlnom("Home");
		  recurso.setHead(_Hpagina);
		  recurso.setUrl("home.do?idsite=" + microsite.getId());
		  lista.add(recurso);
		  return lista;
	  }

	/**
	 * Método que guarda un listado con las urls de mapa de un microsite
	 * @return	ArrayList
	 */
	private ArrayList<Recurso> listarmapa() {
		  ArrayList<Recurso> lista = new ArrayList<Recurso>();
		  Recurso recurso = new Recurso();
		  recurso.setTipo("");
		  recurso.setTitulo("Mapa");
		  recurso.setUrlnom("Mapa");
		  recurso.setHead(_Hpagina);
		  recurso.setUrl("mapa.do?idsite=" + microsite.getId());
		  lista.add(recurso);
		  return lista;
	  }	  
	  
	/**
	 * Método que guarda un listado con las urls de contacto de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList listarcontactos(String idiomapasado,String micronombre) throws Exception {
		  ArrayList lista = new ArrayList();
		  try {
			  MenuRecurso menurecurso = new MenuRecurso();
			  String titmenu="";
			  if(idiomapasado.equals("es")){
				    titmenu="Listado de formularios de Contacto";}
			  else {
				  	titmenu="Llistat de formularis de Contacte";
			  }
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setHead(_Hcarpetagroc);
			  menurecurso.setUrl("contactos.do?idsite=" + microsite.getId());
			  
			  //ahora los contactos		  
			  
		    	ContactoDelegate contactodel = DelegateUtil.getContactoDelegate();
		    	contactodel.init(microsite.getId());		    	
		    	contactodel.setWhere("where (trad.id.codigoIdioma='" + idiomapasado + "' or trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto()+"') and contacto.idmicrosite=" + microsite.getId());
		    	contactodel.setPagina(1);contactodel.setTampagina(Microback.MAX_INTEGER);
		        Iterator<?> iter = contactodel.listarContactos().iterator();
		        while (iter.hasNext()) {
		        	Contacto contacto = (Contacto)iter.next();
		        	Recurso recurso = new Recurso();
		        	recurso.setHead(_Hpagina);
		        	String titulo = contacto.getTitulocontacto(idiomapasado,true);
		        	if (titulo.equals("[sense titol]")) titulo =contacto.getTitulocontacto(Idioma.getIdiomaPorDefecto(),true);
		  		  	recurso.setTitulo(titulo);
		  		  	recurso.setUrlnom((micronombre+">"+titmenu+">"+titulo).replaceAll("'","´"));
		  		  	recurso.setUrl("contacto.do?idsite=" + microsite.getId() + "&cont=" + contacto.getId());
		  		  	menurecurso.getListacosas().add(recurso);
		        }
		        
		      lista.add(menurecurso);  
		  } catch (Exception e) {
			  log.warn("[listarcontactos] Error listando contactos.");
		  }		      
		  return lista;
	  }		  

	
	/**
	 * Método que guarda un listado con las urls de faqs de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 */	
	private ArrayList listarfaqs(String idiomapasado,String micronombre) {
		  ArrayList lista = new ArrayList();
		  Recurso recurso = new Recurso();
		  String titmenu="";
		  if(idiomapasado.equals("es")){
			  titmenu="Listado de Faqs";
		  }else{
			  titmenu="Llistat de Faqs";
		  }
		  recurso.setTitulo(titmenu);
		  recurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
		  recurso.setHead(_Hpagina);
		  recurso.setUrl("faqs.do?idsite=" + microsite.getId());
		  lista.add(recurso);
		  return lista;
	  }	

	/**
	 * Método que guarda un listado con las urls de qssi de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 * @throws Exception
	 */	  
	private ArrayList listarqssi(String idiomapasado,String micronombre) throws Exception {
		  
		  ArrayList lista = new ArrayList();
		  try {
			    MenuRecurso menurecurso = new MenuRecurso();
			    String titmenu="";
			    if(idiomapasado.equals("es"))
			    	titmenu="Listado de Qssi";
			    else
			    {
			    	titmenu="Llistat de Qssi";
		  		}
			    menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
			    menurecurso.setTitulo(titmenu);
			    menurecurso.setHead(_Hcarpetaroig);
			    menurecurso.setUrl("");
			  
	
			     FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
			     qssidel.init(microsite.getId(),idiomapasado);qssidel.setPagina(1);qssidel.setTampagina(Microback.MAX_INTEGER);
			     
				 Iterator<?> iter = qssidel.listarFrqssisrec(idiomapasado).iterator();
				 long  ide = 0;
				 while (iter.hasNext()) {
					 Frqssi qssi = (Frqssi)iter.next();
						
						if(!(qssi.getId()==ide))
		    			 { 
							TraduccionFrqssi tradqss = (TraduccionFrqssi)qssi.getTraduccion(idiomapasado);
		 
							 if (!(tradqss.getNombre()==null))
		    				 {	 
								 Recurso recurso = new Recurso();
								 recurso.setHead(_Hcarpetaroig);
								 recurso.setUrl("qssi.do?idsite=" + microsite.getId() + "&cont=" + qssi.getId().longValue()); //id de la encuesta
 								 recurso.setTitulo( tradqss.getNombre()); // titulo qssi
 								 recurso.setUrlnom((micronombre+">"+titmenu+">"+tradqss.getNombre()).replaceAll("'","´"));
								 menurecurso.getListacosas().add(recurso);
								 ide=qssi.getId();
								
		    				 }
						
		    			 }
					}		    
				 lista.add(menurecurso);		     
			  
		  } catch (Exception e) {
			  log.warn("[listarqssi] Error listando qssi.");
		  }
		  return lista;
	  }	  
	  
	/**
	 * Método que guarda un listado con las urls de noticias de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList listarnoticias(String idiomapasado,String micronombre) throws Exception {
		
		  ArrayList lista = new ArrayList();
		  try {		  
			  MenuRecurso menurecurso = new MenuRecurso();
			  String titmenu="";
			  if(idiomapasado.equals("es"))
				  titmenu="Listados";
			  else{
				  titmenu="Llistats";
			  }
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setHead(_Hcarpetaroig);
			  menurecurso.setUrl("");
			  
			  //ahora los tipos y luego, dentro las noticias (o elementos)
			  
				TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
				tipodel.init(microsite.getId(),idiomapasado);
				tipodel.setWhere("where (trad.id.codigoIdioma='" + idiomapasado + "' or trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto()+"') and tipo.idmicrosite=" + microsite.getId());
				tipodel.setOrderby2( " order by tipo.id, trad.id.codigoIdioma desc");
				tipodel.setPagina(1);tipodel.setTampagina(Microback.MAX_INTEGER);
				Iterator<?> iter = tipodel.listarTiposrec(idiomapasado).iterator();
				
				long  idet = 0;
			    while (iter.hasNext()) {
			    	
			      Tipo tipo = (Tipo)iter.next();
			      if(!(tipo.getId()==idet))
			      {
			    	  TraduccionTipo tratipo = (TraduccionTipo)tipo.getTraduccion(idiomapasado);
			      	if (!(tratipo.getNombre()==null))
		    	    {	 
		    		  
			    	MenuRecurso menurecurso2 = new MenuRecurso();
			    	menurecurso2.setTitulo(tratipo.getNombre());  
			    	menurecurso2.setHead(_Hcarpetagroc);
			    	menurecurso2.setUrl("noticias.do?idsite=" + microsite.getId() + "&tipo=" + tipo.getId());
			    	menurecurso2.setUrlnom((micronombre+">"+titmenu+">"+tratipo.getNombre()).replaceAll("'","´"));
				
			    	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			    	noticiadel.init(microsite.getId());
			    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			    	String wherenoticias = "where  noti.idmicrosite = " + microsite.getId() + " and noti.tipo = " + tipo.getId();
			    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
			    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
			    	noticiadel.setWhere(wherenoticias);
			    	noticiadel.setOrderby2(" order by noti.fpublicacion desc,noti.id,trad.id.codigoIdioma desc");
			    	noticiadel.setPagina(1);noticiadel.setTampagina(Microback.MAX_INTEGER);
			    	List<?> listanoticias=noticiadel.listarNoticiasThin(idiomapasado);
		        
		    		Iterator<?> iter2 = listanoticias.iterator();
		    		
		    		long  ide = 0;
		    		while (iter2.hasNext()) {
		    				Noticia noti = (Noticia)iter2.next();
		    		 	if(!(noti.getId()==ide))
		    			 {  
		    				TraduccionNoticia tranoti = (TraduccionNoticia)noti.getTraduccion(idiomapasado);
		    				 if (!(tranoti.getTitulo()==null))
		    				 {	 
		    					  
		    					 Recurso recurso = new Recurso();
				    			 recurso.setHead(_Hpagina);
		    					 recurso.setTitulo(tranoti.getTitulo());
		    					 recurso.setUrlnom((micronombre+">"+titmenu+">"+tratipo.getNombre()+">"+tranoti.getTitulo()).replaceAll("'","´"));
		    					 recurso.setUrl("noticia.do?idsite=" + microsite.getId() + "&cont=" + noti.getId());
		    					 menurecurso2.getListacosas().add(recurso);
		    					 ide=noti.getId();
		    				     
      				       }   
		    			}
		    		}
		    		menurecurso.getListacosas().add(menurecurso2);
		    		idet=tipo.getId();
				}
			      	
			  }
			    
			}
		  lista.add(menurecurso);
		  } catch (Exception e) {
			  log.warn("[listarnoticias] Error listando listados y elementos.");
		  }		  
		  return lista;
	  }		  

	
	/**
	 * Método que guarda un listado con las urls de agendas (eventos) de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 * @throws Exception
	 */	
	private ArrayList<?> listaragenda(String idiomapasado,String micronombre) throws Exception {
		  ArrayList lista = new ArrayList();
		  try {
				  MenuRecurso menurecurso = new MenuRecurso();
				  String titmenu="";
				  if(idiomapasado.equals("es")){
					    titmenu="Mostrar agenda";}
				  else {
					  	titmenu="Mostrar agenda";//añadir resto de idiomas si se quiere aleman o ingles.
				  }
				  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
				  menurecurso.setTitulo(titmenu);
				  menurecurso.setHead(_Hcarpetagroc);
				  menurecurso.setUrl("agendas.do?idsite=" + microsite.getId());
				  
		
				    AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
			    	agendadel.init(microsite.getId(),idiomapasado);
			    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			    	agendadel.setWhere("where (trad.id.codigoIdioma='"+Idioma.getIdiomaPorDefecto()+"' or trad.id.codigoIdioma='"+idiomapasado+"')and agenda.idmicrosite=" + microsite.getId() + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
			    	agendadel.setOrderby2(" order by agenda.id, trad.id.codigoIdioma desc");
			    	agendadel.setPagina(1);agendadel.setTampagina(Microback.MAX_INTEGER);
			        Iterator<?> iter = agendadel.listarAgendasrec(idiomapasado).iterator();
			        
			    	long  ide = 0;
			        while (iter.hasNext()) {
			        	
			        	Agenda agenda = (Agenda)iter.next();
			        	if(!(agenda.getId()==ide))
			        	{
			        		TraduccionAgenda tradage = (TraduccionAgenda)agenda.getTraduccion(idiomapasado);
			        		 
							 if (!(tradage.getTitulo()==null))
		    				 {	 
				        	java.text.SimpleDateFormat diaitem = new java.text.SimpleDateFormat("yyyyMMdd");
				        	Recurso recurso = new Recurso();
				  		  	recurso.setHead(_Hpagina);
				  		  	recurso.setTitulo(tradage.getTitulo());
				  		  	recurso.setUrlnom((micronombre+">Mostrar Agenda>"+tradage.getTitulo()).replaceAll("'","´"));
				  		  	recurso.setUrl("agenda.do?idsite=" + microsite.getId() + "&cont=" + diaitem.format(agenda.getFinicio()));
				  		  	menurecurso.getListacosas().add(recurso);
				  		    ide=agenda.getId();
			  		  	
		    				 }
			        	}
			      }
				  
			      lista.add(menurecurso);
		  } catch (Exception e) {
			  log.warn("[listaragenda] Error listando agenda.");
		  }
		  return lista;
	  }
	  
	/**
	 * Método que guarda un listado con las urls de encuestas de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList<?> listarencuestas(String idiomapasado,String micronombre) throws Exception {
		  
		  ArrayList lista = new ArrayList<Object>();
		  try {
			  MenuRecurso menurecurso = new MenuRecurso();
			  String titmenu="";
			  if(idiomapasado.equals("es"))
			  {
				  titmenu="Listado encuestas";
		  	  }else
		  	  {
		  		  titmenu="Llistat d`enquestes";
			  }
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));	 
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setHead(_Hcarpetagroc);
			  menurecurso.setUrl("encuestas.do?idsite=" + microsite.getId());
		      EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			  encuestadel.initra(microsite.getId(),idiomapasado);encuestadel.setPagina(1);encuestadel.setTampagina(Microback.MAX_INTEGER);
			  
				Iterator<?> iter = encuestadel.listarEncuestasrec(idiomapasado).iterator();
				long  ide = 0;
				while (iter.hasNext()) {
					Encuesta enc = (Encuesta)iter.next();
					
					if(!(enc.getId()==ide))
	    			 { 
						TraduccionEncuesta traenc = (TraduccionEncuesta)enc.getTraduccion(idiomapasado);
	 
						 if (!(traenc.getTitulo()==null))
	    				 {	 
							 Recurso recurso = new Recurso();
	    				 	 recurso.setHead(_Hpagina);
	    				 	 recurso.setUrl("encuesta.do?idsite=" + microsite.getId() + "&cont=" + enc.getId().longValue()); //id de la encuesta
							 recurso.setTitulo(traenc.getTitulo()); // titulo de la encuesta
							 recurso.setUrlnom((micronombre+">"+titmenu+">"+traenc.getTitulo()).replaceAll("'","´"));
							 menurecurso.getListacosas().add(recurso);
							 ide=enc.getId();
	    				 }
					
	    			 }
				}		    
			  
			  lista.add(menurecurso);
		  } catch (Exception e) {
			  log.warn("[listarencuestas] Error listando encuestas.");
		  }
		  return lista;
	  }	  
	  
	
	   
	  
	  
	  
	  /***************************************************************/
	  /****************   LISTAR ARBOL CONTENIDOS  *******************/
	  /***************************************************************/	  
	  
      


	/**
	 * Método que guarda un listado de urls de contenidos de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoArbol(String idiomapasado) {

		  
			try {
				
			 
				  TraduccionMicrosite microtrad  = (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
				  if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.getIdiomaPorDefecto());
				  String micronombre = microtrad.getTitulo();
				  microsite.getTraduccion(idiomapasado);
				
				MenuDelegate menudel = DelegateUtil.getMenuDelegate();
				  Iterator<?> iter = menudel.listarMenuMicrosite(microsite.getId(), new Long(0), "T", idiomapasado).iterator();
				  				
				//recorrer los menus
				  
			    while (iter.hasNext()) {
			    	Menu menu = (Menu)iter.next();
			    	MenuRecurso menurecurso = new MenuRecurso();
			    	
			    	TraduccionMenu tramen = (TraduccionMenu)menu.getTraduce();
			    	if (tramen==null) {
			    		tramen=(TraduccionMenu)menu.getTraduccion(Idioma.getIdiomaPorDefecto());
			    	}	
			    	String desctitul=tramen.getNombre();
					menurecurso.setTitulo(tramen.getNombre() );
					menurecurso.setHead(_Hcarpetaroig);
					menurecurso.setUrl("");
					menurecurso.setUrlnom((micronombre+">"+tramen.getNombre()).replaceAll("'","´"));
	    	 
			    	//recorrer las paginas y coger las visibles y no caducadas
					Iterator<?> iterpaginas = menu.getContenidos().iterator();
				    while (iterpaginas.hasNext()) {
				    	Contenido conte = (Contenido)iterpaginas.next();
				    	//Si se pasa la id (contenido) y el idMenu y coinciden con el de esta iteración, entonces hay que ignorarlo. 
				    	if (id != null && idMenu != null && 
				    	conte.getId().toString().equals(id) && menu.getId().toString().equals(idMenu) ) {
				    		continue;
				    	}
				    	Recurso recurso = new Recurso();
				    	
				    	TraduccionContenido tracon= (TraduccionContenido)conte.getTraduccion(idiomapasado);
				    	if (tracon==null || (tracon.getTitulo()==null)){
				    		tracon= (TraduccionContenido)conte.getTraduccion(Idioma.getIdiomaPorDefecto());
				    	}
				    	if (((TraduccionContenido)conte.getTraduccion()).getTitulo()!=null) {
				    		  //(conte.getVisible().equals("S")) && 
					    	if (  (Fechas.vigente(conte.getFpublicacion(), conte.getFcaducidad())) ) {
					    		
					    		recurso.setHead(_Hpagina);
								recurso.setUrl("contenido.do?idsite=" + microsite.getId() + "&cont=" + conte.getId().longValue()); //id del contenido
								recurso.setTitulo(tracon.getTitulo()); // titulo del contenido
								recurso.setUrlnom((micronombre+">"+desctitul+">"+tracon.getTitulo()).replaceAll("'","´"));
								menurecurso.getListacosas().add(recurso);
					    		
					    	}
				    	}
				    }

				    
			    	//recoger los submenus. y dentro de los submenus recorrer las paginas y coger las visibles y no caducadas.
				    Iterator<?> itermenus = menudel.listarMenuMicrosite(microsite.getId(), menu.getId(), "T", idiomapasado).iterator();
 
				    while (itermenus.hasNext()) {
				    	Menu submenu = (Menu)itermenus.next();
				    	MenuRecurso menurecurso2 = new MenuRecurso();
				    	
				    	TraduccionMenu trasubmen = (TraduccionMenu)submenu.getTraduce();
				    	if (trasubmen==null) {
				    		trasubmen=(TraduccionMenu)submenu.getTraduccion(Idioma.getIdiomaPorDefecto());
				    	}	
				    	desctitul+=">"+trasubmen.getNombre();
						menurecurso2.setTitulo(trasubmen.getNombre() );
				    	menurecurso2.setHead(_Hcarpetaroig);
				    	menurecurso2.setUrl("");				    	
				    	menurecurso2.setUrlnom((micronombre+">"+trasubmen.getNombre()).replaceAll("'","´"));
				    	//recorrer las paginas y coger las visibles y no caducadas
						Iterator<?> iterpaginassub = submenu.getContenidos().iterator();
					    while (iterpaginassub.hasNext()) {
					    	Contenido contesub = (Contenido)iterpaginassub.next();
					    	//Si se pasa la id (contenido) y el idMenu y coinciden con el de esta iteración, entonces hay que ignorarlo. 
					    	if (id != null && idMenu != null && 
					    			contesub.getId().toString().equals(id) && submenu.getId().toString().equals(idMenu) ) {
					    			continue;
					    	}
					    	TraduccionContenido traconsu= (TraduccionContenido)contesub.getTraduccion(idiomapasado);
					    	if (traconsu==null ||(traconsu.getTitulo()==null)){
					    		traconsu= (TraduccionContenido)contesub.getTraduccion(Idioma.getIdiomaPorDefecto());
					    	}
					    	if (((TraduccionContenido)contesub.getTraduccion()).getTitulo()!=null) {
					    		    //(contesub.getVisible().equals("S")) &&  
						    	if (  (Fechas.vigente(contesub.getFpublicacion(), contesub.getFcaducidad())) ) {
						    		Recurso recurso = new Recurso();
						    		recurso.setHead(_Hpagina);
									recurso.setUrl("contenido.do?idsite=" + microsite.getId() + "&cont=" + contesub.getId().longValue()); //id del contenido
									recurso.setUrlnom((micronombre+">"+desctitul+">"+traconsu.getTitulo()).replaceAll("'","´"));
									recurso.setTitulo( traconsu.getTitulo() ); // titulo del contenido
									menurecurso2.getListacosas().add(recurso);
						    	}
					    	}
					    }
					    
					    menurecurso.getListacosas().add(menurecurso2);
				    	
				    }
				    
				    
				    listaarbol.add(menurecurso);
					
			    }
			    
			    
			} catch (Exception e) {
				log.error("[montartodoArbol] " + e.getMessage());
				listaarbol = new ArrayList<MenuRecurso>();
			}
		  
		  
		  
	  }	               

 
	  
	  /***************************************************************/
	  /****************   LISTAR ARCHIVOS          *******************/
	  /***************************************************************/

	/**
	 * Método que guarda un listado con las urls de archivos de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoArchivos(String idiomapasado ) {
		  try {
			  
			  TraduccionMicrosite microtrad  =  (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
			  if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.getIdiomaPorDefecto());
			  String micronombre = microtrad.getTitulo();
			  
			  MenuRecurso menurecurso = new MenuRecurso();
			  String titmenu ="";
			  if(idiomapasado.equals("es")){
				    titmenu="Archivos comunes";}
			  else {
				  	titmenu="arxius comuns";  
			  }
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
			  menurecurso.setHead(_Hcarpetaroig);
			  menurecurso.setUrl("");			  
			  
			  ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
			  Iterator<?> iter = bdConte.listarDocumentos(microsite.getId().toString(),null).iterator();
			  while (iter.hasNext()) {
				  Archivo archi = (Archivo)iter.next();
				  Recurso recurso = new Recurso();
				  recurso.setHead("" + archi.getId());
				  recurso.setTipo(archi.getMime());
				  recurso.setTitulo(archi.getNombre());
				  recurso.setUrlnom((micronombre+">"+titmenu+">"+archi.getNombre()).replaceAll("'","´"));
				  recurso.setUrl("archivopub.do?ctrl=MCRST" + microsite.getId().longValue() + "ZI" + archi.getId().longValue() + "&id=" + archi.getId().longValue());
				  menurecurso.getListacosas().add(recurso);
			  }
			  listaarchivos.add(menurecurso);
			  
		  } catch (Exception e) {
			  log.error("[montartodoArchivos] " + e.getMessage());
			  
		  }
	  }

	  
	  
	  /***************************************************************/
	  /************   LISTAR ARCHIVOS de UN CONTENIDO      ***********/
	  /***************************************************************/

	/**
	 * Método que guarda un listado con las urls de archivos de los contenidos de un microsite según idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoArchivosContenido(String idiomapasado) {
		  
			try {  
				
				
			    if (idcontenidolong!=null) {
			    	  TraduccionMicrosite microtrad  = (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
					  if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.getIdiomaPorDefecto());
					  String micronombre = microtrad.getTitulo();
			    	  String titmenu ="";
					  if(idiomapasado.equals("es")){
						    titmenu="Archivos especificos del contenido";}
					  else {
						  	titmenu="Arxius especifics del contingut ";  
					  }
					  MenuRecurso menurecurso = new MenuRecurso();
					  menurecurso.setTitulo(titmenu);
					  menurecurso.setHead(_Hcarpetaroig);
					  menurecurso.setUrl("");			  
					  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","´"));
					  
					  ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
					  Iterator<?> iter = bdConte.listarDocumentos(microsite.getId().toString(),""+idcontenidolong).iterator();
					  while (iter.hasNext()) {
						  Archivo archi = (Archivo)iter.next();
						  Recurso recurso = new Recurso();
						  recurso.setHead("" + archi.getId());
						  recurso.setTipo(archi.getMime());
						  recurso.setTitulo(archi.getNombre());
						  recurso.setUrlnom((micronombre+">"+titmenu+">"+archi.getNombre()).replaceAll("'","´"));
						  recurso.setUrl("archivopub.do?ctrl=" + Microback.RCONTENIDO + idcontenidolong.longValue() + "ZI" + archi.getId().longValue() + "&id=" + archi.getId().longValue());
						  menurecurso.getListacosas().add(recurso);
					  }
					  listaarchivos.add(menurecurso);
			    }
		  } catch (Exception e) {
			  log.error("[montartodoArchivosContenido] " + e.getMessage());
			  
		  }
	  }
	  
	  
	  
	  
	  /***************************************************************/
	  /****************   GETTERS / SETTERS        *******************/
	  /***************************************************************/

	  
		public List<MenuRecurso> getListaarbol() {
			return listaarbol;
		}


		public List<MenuRecurso> getListaarchivos() {
			return listaarchivos;
		}


		public List<?> getListaoriginal() {
			return listaoriginal;
		}

		public ArrayList<?> getListaarchivoscontenido() {
			return listaarchivoscontenido;
		}

		public void setHomeDesactivado(final String pHomeDesactivado) {
			this.homeDesactivado = pHomeDesactivado;
		}
		
		public String getHomeDesactivado() {
			return this.homeDesactivado;
		}
}

