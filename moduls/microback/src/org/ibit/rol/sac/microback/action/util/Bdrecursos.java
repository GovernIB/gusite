package org.ibit.rol.sac.microback.action.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microback.Microback;
import org.ibit.rol.sac.microback.base.bean.MenuRecurso;
import org.ibit.rol.sac.microback.base.bean.Recurso;
import org.ibit.rol.sac.microback.utils.Cadenas;
import org.ibit.rol.sac.microback.utils.Fechas;
import org.ibit.rol.sac.micromodel.Agenda;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Contacto;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Menu;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.Tipo;
import org.ibit.rol.sac.micromodel.Frqssi;
import org.ibit.rol.sac.micromodel.Tiposervicio;
import org.ibit.rol.sac.micromodel.TraduccionAgenda;
import org.ibit.rol.sac.micromodel.TraduccionContenido;
import org.ibit.rol.sac.micromodel.TraduccionEncuesta;
import org.ibit.rol.sac.micromodel.TraduccionFrqssi;
import org.ibit.rol.sac.micromodel.TraduccionMenu;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.delegate.AgendaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.ContactoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MenuDelegate;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TiposervicioDelegate;
import org.ibit.rol.sac.micropersistence.delegate.FrqssiDelegate;

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
	
	protected static Log log = LogFactory.getLog(Bdrecursos.class);

	  public Bdrecursos(HttpServletRequest request) {
		  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		   
		  
		  String idcontenido = "" + request.getParameter("page");
		  try { 
		    	idcontenidolong=new Long(idcontenido);
		  } catch (Exception e) {
		    	idcontenido="null";
		  }
		  	
	  } 
	  
	 /**
	 * M�todo que obtiene las urls de un microsite
	 * @param idiomapasado
	 */
	public void doObtainUrls(String idiomapasado) {
		 
		
		  montartodoURLs(idiomapasado);
		  montartodoArchivos(idiomapasado);
		  montartodoArchivosContenido(idiomapasado);
		  montartodoArbol(idiomapasado);
	  }
	  
	/**
	 * M�todo que obtiene las urls de documentos de un microsite 
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
	 * M�todo que guarda un listado con todas las urls de un microsite seg�n un idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoURLs(String idiomapasado) {
		  try {
			  
			 TraduccionMicrosite microtrad  = (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
			 if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.DEFAULT);
			 String micronombre = microtrad.getTitulo();
			  
		  	listaoriginal = listarhome();
		  	listaoriginal.addAll(listarmapa());
		  	
		  	
			ArrayList<?> listserofr = new ArrayList<Object>();
			if (microsite.getServiciosOfrecidos()!=null)
				listserofr = Cadenas.getArrayListFromString(microsite.getServiciosOfrecidos());
			
			Iterator<?> iter=listserofr.iterator();
			while (iter.hasNext()) {

				TiposervicioDelegate tiposerdel= DelegateUtil.getTiposervicioDelegate();
				Tiposervicio tiposervicio = tiposerdel.obtenerTiposervicio(new Long((String)iter.next()));
				
			
				if (tiposervicio.getReferencia().equals(Microback.RAGENDA)) {
					listaoriginal.addAll(listaragenda(idiomapasado,micronombre));
				}
				if (tiposervicio.getReferencia().equals(Microback.RCONTACTO)) {
					listaoriginal.addAll(listarcontactos(idiomapasado,micronombre));
				}
				if (tiposervicio.getReferencia().equals(Microback.RFAQ)) {
					listaoriginal.addAll(listarfaqs(idiomapasado,micronombre));
				}			
				if (tiposervicio.getReferencia().equals(Microback.RNOTICIA)) {
					listaoriginal.addAll(listarnoticias(idiomapasado,micronombre)); 
				}		
				if (tiposervicio.getReferencia().equals(Microback.RPROCEDIMIENTO)) {
					listaoriginal.addAll(listarprocedimientos(idiomapasado,micronombre));
				}				
				if (tiposervicio.getReferencia().equals(Microback.RENCUESTA)) {
					listaoriginal.addAll(listarencuestas(idiomapasado,micronombre));
				}					
				if (tiposervicio.getReferencia().equals(Microback.RQSSI)) {
					listaoriginal.addAll(listarqssi(idiomapasado,micronombre));
				}				
				
			}
			
			
			
			
		  } catch (Exception e) {
			  log.error("[montartodoURLs] " + e.getMessage());
			  listaoriginal = new ArrayList();
		  }
	  }
	  
  
	/**
	 * M�todo que guarda un listado con las urls de la home de un microsite
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
	 * M�todo que guarda un listado con las urls de mapa de un microsite
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
	 * M�todo que guarda un listado con las urls de contacto de un microsite seg�n idioma
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
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setHead(_Hcarpetagroc);
			  menurecurso.setUrl("contactos.do?idsite=" + microsite.getId());
			  
			  //ahora los contactos		  
			  
		    	ContactoDelegate contactodel = DelegateUtil.getContactoDelegate();
		    	contactodel.init(microsite.getId());
		    	contactodel.setWhere("where contacto.idmicrosite=" + microsite.getId());
		    	contactodel.setPagina(1);contactodel.setTampagina(Microback.MAX_INTEGER);
		        Iterator<?> iter = contactodel.listarContactos().iterator();
		        while (iter.hasNext()) {
		        	Contacto contacto = (Contacto)iter.next();
		        	Recurso recurso = new Recurso();
		        	recurso.setHead(_Hpagina);
		        	String titulo = contacto.getTitulocontacto(idiomapasado);
		        	if (titulo.equals("[sense titol]")) titulo =contacto.getTitulocontacto(Idioma.DEFAULT);
		  		  	recurso.setTitulo(titulo);
		  		  	recurso.setUrlnom((micronombre+">"+titmenu+">"+titulo).replaceAll("'","�"));
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
	 * M�todo que guarda un listado con las urls de faqs de un microsite seg�n idioma
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
		  recurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
		  recurso.setHead(_Hpagina);
		  recurso.setUrl("faqs.do?idsite=" + microsite.getId());
		  lista.add(recurso);
		  return lista;
	  }	

	/**
	 * M�todo que guarda un listado con las urls de qssi de un microsite seg�n idioma
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
			    menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
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
 								 recurso.setUrlnom((micronombre+">"+titmenu+">"+tradqss.getNombre()).replaceAll("'","�"));
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
	 * M�todo que guarda un listado con las urls de procedimientos de un microsite seg�n idioma
	 * @param idiomapasado	Id de idioma
	 * @param micronombre	Nombre del microsite
	 * @return ArrayList
	 * @throws Exception
	 */	
	private ArrayList listarprocedimientos(String idiomapasado,String micronombre) {
		  ArrayList lista = new ArrayList();
		  Recurso recurso = new Recurso();
		  String titmenu="";
		  if(idiomapasado.equals("es"))
		  {
			  titmenu="Listado de Procedimientos";
		  }else
		  {
			  titmenu="Llistat de Procediments";
		  }
		  recurso.setTitulo(titmenu);
		  recurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
		  recurso.setHead(_Hpagina);
		  recurso.setUrl("procedimientos.do?idsite=" + microsite.getId());
		  lista.add(recurso);
		  return lista;
	  }		  
	  
	/**
	 * M�todo que guarda un listado con las urls de noticias de un microsite seg�n idioma
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
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setHead(_Hcarpetaroig);
			  menurecurso.setUrl("");
			  
			  //ahora los tipos y luego, dentro las noticias (o elementos)
			  
				TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
				tipodel.init(microsite.getId(),idiomapasado);
				tipodel.setWhere("where (index(trad)='" + idiomapasado + "' or index(trad)='" + Idioma.DEFAULT+"') and tipo.idmicrosite=" + microsite.getId());
				tipodel.setOrderby2( " order by tipo.id,index(trad) desc");
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
			    	menurecurso2.setUrlnom((micronombre+">"+titmenu+">"+tratipo.getNombre()).replaceAll("'","�"));
				
			    	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			    	noticiadel.init(microsite.getId());
			    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			    	String wherenoticias="where (index(trad)='" + idiomapasado + "'or index(trad)='" + Idioma.DEFAULT+"')  and noti.idmicrosite=" + microsite.getId() + " and noti.tipo=" + tipo.getId();
			    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
			    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
			    	noticiadel.setWhere(wherenoticias);
			    	noticiadel.setOrderby2(" order by noti.fpublicacion desc,noti.id,index(trad) desc");
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
		    					 recurso.setUrlnom((micronombre+">"+titmenu+">"+tratipo.getNombre()+">"+tranoti.getTitulo()).replaceAll("'","�"));
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
	 * M�todo que guarda un listado con las urls de agendas (eventos) de un microsite seg�n idioma
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
					  	titmenu="Mostrar agenda";//a�adir resto de idiomas si se quiere aleman o ingles.
				  }
				  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
				  menurecurso.setTitulo(titmenu);
				  menurecurso.setHead(_Hcarpetagroc);
				  menurecurso.setUrl("agendas.do?idsite=" + microsite.getId());
				  
		
				    AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
			    	agendadel.init(microsite.getId(),idiomapasado);
			    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			    	agendadel.setWhere("where (index(trad)='"+Idioma.DEFAULT+"' or index(trad)='"+idiomapasado+"')and agenda.idmicrosite=" + microsite.getId() + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
			    	agendadel.setOrderby2(" order by agenda.id, index(trad) desc");
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
				  		  	recurso.setUrlnom((micronombre+">Mostrar Agenda>"+tradage.getTitulo()).replaceAll("'","�"));
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
	 * M�todo que guarda un listado con las urls de encuestas de un microsite seg�n idioma
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
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));	 
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
							 recurso.setUrlnom((micronombre+">"+titmenu+">"+traenc.getTitulo()).replaceAll("'","�"));
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
	 * M�todo que guarda un listado de urls de contenidos de un microsite seg�n idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoArbol(String idiomapasado) {

		  
			try {
				
			 
				  TraduccionMicrosite microtrad  = (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
				  if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.DEFAULT);
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
			    		tramen=(TraduccionMenu)menu.getTraduccion(Idioma.DEFAULT);
			    	}	
			    	String desctitul=tramen.getNombre();
					menurecurso.setTitulo(tramen.getNombre() );
					menurecurso.setHead(_Hcarpetaroig);
					menurecurso.setUrl("");
					menurecurso.setUrlnom((micronombre+">"+tramen.getNombre()).replaceAll("'","�"));
	    	 
			    	//recorrer las paginas y coger las visibles y no caducadas
					Iterator<?> iterpaginas = menu.getContenidos().iterator();
				    while (iterpaginas.hasNext()) {
				    	Contenido conte = (Contenido)iterpaginas.next();
				    	Recurso recurso = new Recurso();
				    	
				    	TraduccionContenido tracon= (TraduccionContenido)conte.getTraduccion(idiomapasado);
				    	if (tracon==null || (tracon.getTitulo()==null)){
				    		tracon= (TraduccionContenido)conte.getTraduccion(Idioma.DEFAULT);
				    	}
				    	if (((TraduccionContenido)conte.getTraduccion()).getTitulo()!=null) {
				    		  //(conte.getVisible().equals("S")) && 
					    	if (  (Fechas.vigente(conte.getFpublicacion(), conte.getFcaducidad())) ) {
					    		
					    		recurso.setHead(_Hpagina);
								recurso.setUrl("contenido.do?idsite=" + microsite.getId() + "&cont=" + conte.getId().longValue()); //id del contenido
								recurso.setTitulo(tracon.getTitulo()); // titulo del contenido
								recurso.setUrlnom((micronombre+">"+desctitul+">"+tracon.getTitulo()).replaceAll("'","�"));
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
				    		trasubmen=(TraduccionMenu)submenu.getTraduccion(Idioma.DEFAULT);
				    	}	
				    	desctitul+=">"+trasubmen.getNombre();
						menurecurso2.setTitulo(trasubmen.getNombre() );
				    	menurecurso2.setHead(_Hcarpetaroig);
				    	menurecurso2.setUrl("");				    	
				    	menurecurso2.setUrlnom((micronombre+">"+trasubmen.getNombre()).replaceAll("'","�"));
				    	//recorrer las paginas y coger las visibles y no caducadas
						Iterator<?> iterpaginassub = submenu.getContenidos().iterator();
					    while (iterpaginassub.hasNext()) {
					    	Contenido contesub = (Contenido)iterpaginassub.next();
					    	
					    	TraduccionContenido traconsu= (TraduccionContenido)contesub.getTraduccion(idiomapasado);
					    	if (traconsu==null ||(traconsu.getTitulo()==null)){
					    		traconsu= (TraduccionContenido)contesub.getTraduccion(Idioma.DEFAULT);
					    	}
					    	if (((TraduccionContenido)contesub.getTraduccion()).getTitulo()!=null) {
					    		    //(contesub.getVisible().equals("S")) &&  
						    	if (  (Fechas.vigente(contesub.getFpublicacion(), contesub.getFcaducidad())) ) {
						    		Recurso recurso = new Recurso();
						    		recurso.setHead(_Hpagina);
									recurso.setUrl("contenido.do?idsite=" + microsite.getId() + "&cont=" + contesub.getId().longValue()); //id del contenido
									recurso.setUrlnom((micronombre+">"+desctitul+">"+traconsu.getTitulo()).replaceAll("'","�"));
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
	 * M�todo que guarda un listado con las urls de archivos de un microsite seg�n idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoArchivos(String idiomapasado ) {
		  try {
			  
			  TraduccionMicrosite microtrad  =  (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
			  if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.DEFAULT);
			  String micronombre = microtrad.getTitulo();
			  
			  MenuRecurso menurecurso = new MenuRecurso();
			  String titmenu ="";
			  if(idiomapasado.equals("es")){
				    titmenu="Archivos comunes";}
			  else {
				  	titmenu="arxius comuns";  
			  }
			  menurecurso.setTitulo(titmenu);
			  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
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
				  recurso.setUrlnom((micronombre+">"+titmenu+">"+archi.getNombre()).replaceAll("'","�"));
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
	 * M�todo que guarda un listado con las urls de archivos de los contenidos de un microsite seg�n idioma
	 * @param idiomapasado	Id de idioma
	 */
	private void montartodoArchivosContenido(String idiomapasado) {
		  
			try {  
				
				
			    if (idcontenidolong!=null) {
			    	  TraduccionMicrosite microtrad  = (TraduccionMicrosite)microsite.getTraduccion(idiomapasado);
					  if (microtrad==null) microtrad  = (TraduccionMicrosite)microsite.getTraduccion(Idioma.DEFAULT);
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
					  menurecurso.setUrlnom((micronombre+">"+titmenu).replaceAll("'","�"));
					  
					  ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
					  Iterator<?> iter = bdConte.listarDocumentos(microsite.getId().toString(),""+idcontenidolong).iterator();
					  while (iter.hasNext()) {
						  Archivo archi = (Archivo)iter.next();
						  Recurso recurso = new Recurso();
						  recurso.setHead("" + archi.getId());
						  recurso.setTipo(archi.getMime());
						  recurso.setTitulo(archi.getNombre());
						  recurso.setUrlnom((micronombre+">"+titmenu+">"+archi.getNombre()).replaceAll("'","�"));
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

}

