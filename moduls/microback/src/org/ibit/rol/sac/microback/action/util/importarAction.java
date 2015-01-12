package org.ibit.rol.sac.microback.action.util;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Iterator;
import java.util.StringTokenizer;


import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.ibit.rol.sac.microback.Microback;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.importarForm;
import org.apache.struts.action.*;
import org.ibit.rol.sac.microback.utils.betwixt.Configurator;
import org.ibit.rol.sac.microback.utils.log.MicroLog;
import org.ibit.rol.sac.micromodel.*;
import org.ibit.rol.sac.micropersistence.delegate.*;
import org.ibit.rol.sac.model.UnidadAdministrativa;
import org.ibit.rol.sac.persistence.delegate.UnidadAdministrativaDelegate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action que importa un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="importar"<BR> 
 *  name="importarForm"<BR>
 *	scope="request" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/importar.jsp"
 *  
 *  @author - Indra
 */
public class importarAction extends BaseAction {

	protected static Log log = LogFactory.getLog(importarAction.class);
	private static String[] roles = new String[]{"sacsystem", "sacadmin"};
	protected String mensaje="";
	protected Hashtable<String,Long> tablamapeos=new Hashtable<String,Long>();
	protected Hashtable<String, String> hshURLs=new Hashtable<String, String>();
	protected Long idmicroant;
	private String _hashcode;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		_hashcode = "" + this.hashCode() + new SimpleDateFormat("hhmmss").format(new java.util.Date()); //un poco chapuza, pero sirve perfectamente
		
		Hashtable<String, String> rolenames=new Hashtable<String,String>();
    	// recoger usuario.....
    	if (request.getSession().getAttribute("MVS_usuario")==null) {
        	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
        	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
        	request.getSession().setAttribute("MVS_usuario", usu);	
    	}
    	if (request.getSession().getAttribute("rolenames")==null) {
           	if (request.getRemoteUser() != null) {
        		request.getSession().setAttribute("username", request.getRemoteUser());
                rolenames = new Hashtable<String,String>();
                for (int i = 0; i < roles.length; i++) 
                    if (request.isUserInRole(roles[i])) rolenames.put(roles[i],roles[i]);
                request.getSession().setAttribute("rolenames", rolenames);
            }        
    	}
    	
		// Solo podrán importar los roles sacsystem y sacadmin
    	rolenames=(Hashtable<String, String>)request.getSession().getAttribute("rolenames");

		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
            return mapping.findForward("info");
		}

		request.getSession().removeAttribute("MVS_importprocessor");
		request.getSession().removeAttribute("MVS_avisoindexadorimportprocessor");
		
		
		importarForm f = (importarForm) form;
		
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		try {
		
	    	if (f.getArchi()!=null) {
	    		
	    		boolean tmpIndexacion=false;
	    		if ( (f.getIndexar()!=null) && (f.getIndexar().equals("S")) ) 
	    				tmpIndexacion=true;
	    		
	    		addImportLog("Inici importació, USUARI: [" + request.getSession().getAttribute("username") + "]");
	    		addImportLogVisual(request, (String)rb.getObject("logimport.upload"));
	    		addImportLogVisual(request, (String)rb.getObject("logimport.tamano") + ": " + f.getArchi().getFileSize() + " bytes");
	    		addImportLogVisual(request, (String)rb.getObject("logimport.integridad.ini"));
	        	BeanReader beanReader = new BeanReader();
	            Configurator.configure(beanReader);
	        	MicrositeCompleto micro= (MicrositeCompleto)beanReader.parse(f.getArchi().getInputStream());
	        	addImportLogVisual(request, (String)rb.getObject("logimport.integridad.fin"));

	        	// Comprobamos si la versión con que se exportó y la versión donde se importa es la misma
	        	if (Double.valueOf(micro.getVersion()) > Double.valueOf(Microback.microsites_version))
	        	{
	        		addMessage(request, "micro.importar.version", micro.getVersion() , Microback.microsites_version);
	    	       	return mapping.findForward("info");
	        	}
	        	addImportLogVisual(request, (String)rb.getObject("logimport.version")+ " [" + micro.getVersion() + "]");
	    		// si hay un nuevo título, lo actualizo en el idioma por defecto
	        	if (f.getNuevonombre().length()>0) ((TraduccionMicrosite)micro.getTraduccion()).setTitulo(""+f.getNuevonombre());

	        	// Actualizo la fecha de creación
	        	micro.setFecha(new Date());
	        	mensaje="";
	        	tablamapeos=new Hashtable<String,Long>();
	        	hshURLs=new Hashtable<String,String>();
	        	
	        	try{
	        		UnidadAdministrativaDelegate uniad=org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();                              
	        		UnidadAdministrativa UA =uniad.obtenerUnidadAdministrativa(new Long(micro.getIdUA()));
	        	}catch(Exception e){
	        		log.error((String)rb.getObject("logimport.errorUA"));
	    			addMessage(request, "peticion.errorUA");
	    			addImportLogVisualStackTrace(request, (String)rb.getObject("logimport.errorUA"), e.getStackTrace());
	    			return mapping.findForward("info");
	        	}
	        	
	        	CrearMicro(micro, request, f.getTarea());
	        	request.setAttribute("mensaje",mensaje);
	        	
	        	if (tmpIndexacion) {
	        		String aviso=(String)rb.getObject("micro.importar.aviso1") + (String)rb.getObject("micro.importar.aviso2") +(String)rb.getObject("micro.importar.aviso3");
	        		request.getSession().setAttribute("MVS_avisoindexadorimportprocessor" , aviso);
	        		
	        		addImportLogVisual(request, "Comença indexació");
	        		IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
	                indexo.reindexarMicrosite(micro.getId());
	                addImportLogVisual(request, "Fi indexació");
	        	}
	
	        	addImportLog("Fi importació, USUARI: [" + request.getSession().getAttribute("username") + "], nou MICROSITE: [" +micro.getId() + "]");
	    	}
		} catch (Exception e) {
			log.error((String)rb.getObject("logimport.error"));
			addMessage(request, "peticion.error");
			addImportLogVisualStackTrace(request, (String)rb.getObject("logimport.error"), e.getStackTrace());
			return mapping.findForward("info");
			
		}
    	return mapping.findForward("detalle");
	}

	/**
	 *  Esta función realiza las operaciones necesarias para crear un nuevo microsite
	 *  basándose en el Microsite importado en XML
	 * @param mic
	 * @throws DelegateException 
	 */
	private void CrearMicro (MicrositeCompleto mic, HttpServletRequest request, String tarea) throws DelegateException {
		
		MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
		IndexerDelegate indexdel = DelegateUtil.getIndexerDelegate();
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		
		Set activi = mic.getActividades();
		Set agenda = mic.getAgendas();
		
		Set tpnoti = mic.getTiponotis();
		Set notics = mic.getNoticias();
		
		Set frqssi = mic.getFrqssis();

		Set temas = mic.getTemas();
		Set faq = mic.getFaqs();

		Set menus = mic.getMenus();
		
		Set banners =mic.getBanners();
		Set docus = mic.getDocus();
		Set contactos = mic.getFormularioscontacto();
		Set compos =  mic.getComponentes();
		
		Set encuestas = mic.getEncuestas();

		/* Se realizan las importaciones objeto o objeto debido al particular diseño 
    	 * de la BD excepto con los objetos que cuelgan directamente y sin hijos del
    	 * microsite, además hay que actualizar las URL de ciertos campos.
    	 * 
    	 * Elementos insertados independientemente:
    	 * Actividades, Agendas, Tipo de Noticias, Noticias, Formularios QSSI, Temas, Faqs, Banners
    	 * Menus, Formularios de Contacto, Documentos , Encuestas y Componentes
    	 * 
    	 * Elementos insertados junto con el bean MicrositeCompleto:
    	 * Microsite, Idiomas Microsite, Procedimientos
    	 * 
    	 */ 
		try {

			
	    	
			// In/habilitamos que se indexen cosas en lucene
	    	indexdel.setBloqueado(true);
	    	
	    	// Elimino del bean los objetos que grabaré posteriormente
	    	mic.setActividades(null);
	    	mic.setAgendas(null);
	    	mic.setFaqs(null);
	    	mic.setMenus(null);
	    	mic.setNoticias(null);
	    	mic.setFrqssis(null);
	    	mic.setTemas(null);
	    	mic.setTiponotis(null);
	    	mic.setBanners(null);
	    	mic.setDocus(null);
	    	mic.setFormularioscontacto(null);
	    	mic.setComponentes(null);
	    	mic.setEncuestas(null);
	    	
	    	// Inserto el microsite sin los objetos independientes
	    	idmicroant=mic.getId(); // necesario para saber el id a sustituir en los enlaces
	    	mic.setId(null);
	    	
	    	Long idmicronuevo = null;
	    	if (tarea.equals("R")) {
	    		//reemplazar	    		
	    		idmicronuevo=bdMicro.reemplazarMicrositeCompleto(mic);
	    		
	    		// Guardamos resumen de estadisticas para el micro site reemplazado
	    		EstadisticaDelegate estdel=DelegateUtil.getEstadisticaDelegate();
	    		estdel.crearEstadisticasMicroReemplazado(idmicroant, idmicronuevo );
	    		// Eliminar estadísticas
	    		estdel.borrarEstadisticasMicroSite(idmicroant);
	    		addImportLogVisual(request, "Reemplaçat Microsite: " + mic.getClaveunica());
	    	} else {
	    		//crear
	    		idmicronuevo=bdMicro.grabarMicrositeCompleto(mic);
	    		addImportLogVisual(request, "Crear Nou Microsite: " + mic.getClaveunica());
	    	}

	    	InsertaContactos(contactos, mic, request); 
	    	InsertaBanners (banners, mic, request);
	    	InsertaAgendas (activi, agenda, mic, request);
	    	InsertaNoticias (tpnoti, notics, mic, request);
	    	InsertaFrqssis (frqssi, mic, request);
	    	InsertaFaqs (temas, faq, mic, request);
	    	InsertaMenusContenidos (menus, mic, request);
	    	InsertaComponentes ( compos, mic, request); 
	    	InsertaEncuestas (encuestas, mic, request);
	    	InsertaDocus (docus, mic, request); //cambio orden
	    	
	    	tablamapeos.put(new String("MIC_"+idmicroant),idmicronuevo.longValue());
	    	
	    	calculaHshURLs(idmicronuevo); addImportLogVisual(request, (String)rb.getObject("logimport.referencias.calculo"));
	    	mic=bdMicro.obtenerMicrositeCompleto(mic.getId()); // refresco el microsite
	    	ActualizaURLMicrositeCompleto(tablamapeos, mic, request); addImportLogVisual(request, (String)rb.getObject("logimport.referencias.proceso"));
	    	
	    	if (mensaje.length()==0)
	    		mensaje="<strong>"+ (String)rb.getObject("logimport.creado") +"</strong>";
	    	addImportLogVisual(request, mensaje);	    	
		}
		catch (Exception ex) {
			mensaje+=(String)rb.getObject("logimport.error") + ": "+
			((TraduccionMicrosite)mic.getTraduccion()).getTitulo()+"</strong><br/><br/>"+ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		} finally {
			indexdel.setBloqueado(false);//Habilitamos que se indexen cosas en lucene
		}
		return;
	}

	/**
	 *  Esta función crea los formularios de contacto en el nuevo Microsite
	 */

	private void InsertaContactos (Set conts, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Contacto> lista= new HashSet<Contacto>();
		try {
			ContactoDelegate bdContacto = DelegateUtil.getContactoDelegate();
			Iterator<?> it = conts.iterator();
		
			while (it.hasNext())  {
				Contacto cont=(Contacto)it.next();
				// Replicamos el formulario para no modificar el original
				Contacto contnuevo= (Contacto)BeanUtils.cloneBean(cont);
				contnuevo.setId(null);
				contnuevo.setIdmicrosite(mic.getId());
				bdContacto.grabarContacto(contnuevo);
				tablamapeos.put(new String("FRM_"+cont.getId()),contnuevo.getId());
				lista.add(contnuevo);
				stlog.append(contnuevo.getId() + " ");
			}
			mic.setFormularioscontacto(lista);
			addImportLogVisual(request, (String)rb.getObject("logimport.formularios") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.formularios") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.formularios.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
		
	}

	
	/**
	 *  Esta función crea los documentos en el nuevo Microsite
	 */

	private void InsertaDocus (Set docus, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Archivo> lista= new HashSet<Archivo>();
		ArchivoDelegate bdDocu = DelegateUtil.getArchivoDelegate();
		try {
			Iterator<?> it = docus.iterator();
			while (it.hasNext())  {
				Archivo doc=(Archivo)it.next();
				// Replicamos el documento para no modificar el original
				Archivo docnuevo= (Archivo)BeanUtils.cloneBean(doc);
				docnuevo.setId(null);
				docnuevo.setIdmicrosite(mic.getId().longValue());
                
				if (doc.getPagina()!=null) {
					Long newidpagina = (Long)tablamapeos.get("CON_" + doc.getPagina().longValue());
					docnuevo.setPagina(newidpagina);
				}
				bdDocu.insertarArchivo(docnuevo);
				
//				System.out.println("Id Document Anterior: " + doc.getId());
//				System.out.println("Nom de l' arxiu: " + doc.getNombre());
//				System.out.println("IdSite: " + doc.getIdmicrosite());
//				System.out.println("Tipus: " + doc.getMime());
//				System.out.println("Pagina: " + doc.getPagina());
//				
//				System.out.println("Id Document Nou: " + docnuevo.getId());
//				System.out.println("Nom de l' arxiu: " + docnuevo.getNombre());
//				System.out.println("IdSite: " + docnuevo.getIdmicrosite());
//				System.out.println("Tipus: " + docnuevo.getMime());
//				System.out.println("Pagina: " + docnuevo.getPagina());
				
				tablamapeos.put(new String("DCM_"+doc.getId()),docnuevo.getId());
				
				lista.add(docnuevo);
				stlog.append(docnuevo.getId() + " ");
			}
			mic.setDocus(lista);
			addImportLogVisual(request, (String)rb.getObject("logimport.documentos") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.documentos") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.documentos.error") + ": "+ ex.toString()+"<br/>";
  
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

	
	/**
	 *  Esta función crea los banners en el nuevo Microsite
	 */
	private void InsertaBanners (Set banners, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Banner> lista= new HashSet<Banner>();
		BannerDelegate bdBanner = DelegateUtil.getBannerDelegate();
		try {
			Iterator<?> it = banners.iterator();
			while (it.hasNext())  {
				Banner ban=(Banner)it.next();
				// Replicamos el banner para no modificar el original
				Banner bannuevo= (Banner)BeanUtils.cloneBean(ban);
				bannuevo.setId(null);
				bannuevo.setIdmicrosite(mic.getId());
				bdBanner.grabarBanner(bannuevo);
				tablamapeos.put(new String("BAN_"+ban.getId()),bannuevo.getId());
				lista.add(bannuevo);
				stlog.append(bannuevo.getId() + " ");
			}
			mic.setBanners(lista);
			addImportLogVisual(request, (String)rb.getObject("logimport.banners") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.banners") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.banners.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
		
	}

	
	/**
	 *  Esta función crea las actividades y los eventos de la agenda
	 *  en el nuevo Microsite
	 */
	private void InsertaAgendas (Set activis, Set agendas, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Long idact_nueva;
		Set<Actividadagenda> lista1= new HashSet<Actividadagenda>();
		Set<Agenda> lista2= new HashSet<Agenda>();
		ActividadDelegate bdActividad = DelegateUtil.getActividadagendaDelegate();
		AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
		try {
			Iterator<?> it = activis.iterator();
			while (it.hasNext())  {
				Actividadagenda act=(Actividadagenda)it.next();
				// Replicamos la actividad para no modificar la original
				Actividadagenda actnueva= (Actividadagenda)BeanUtils.cloneBean(act);
				actnueva.setId(null);
				actnueva.setIdmicrosite(mic.getId());
				// creamos las actividades
				idact_nueva=bdActividad.grabarActividad(actnueva);
				lista1.add(actnueva);
				// buscamos eventos de agenda con esa actividad
				Iterator<?> it1 = agendas.iterator();
				while (it1.hasNext())  {
					Agenda age=(Agenda)it1.next();
					if (age.getActividad().getId().compareTo(act.getId())==0) {
						// creo el evento de agenda
						Agenda agenueva= (Agenda)BeanUtils.cloneBean(age);
						agenueva.setId(null);
						agenueva.getActividad().setId(idact_nueva);
						agenueva.setIdmicrosite(mic.getId());
						agenueva.getActividad().setIdmicrosite(mic.getId());
						bdAgenda.grabarAgenda(agenueva);
						lista2.add(agenueva);
						stlog.append(agenueva.getId() + " ");
					}
				}
			}
			mic.setActividades(lista1);
			mic.setAgendas(lista2);
			addImportLogVisual(request, (String)rb.getObject("logimport.eventos") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.eventos") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.eventos.error") + ": <br/>"+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

	
	/**
	 *  Esta función crea los tipos de noticias y las noticias
	 *  en el nuevo Microsite
	 */
	private void InsertaNoticias (Set tpnotis, Set notics, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Long idtpnotic_nueva;
		Set<Tipo> lista1= new HashSet<Tipo>();
		Set<Noticia> lista2= new HashSet<Noticia>();
		TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
		NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
		try {
			Iterator<?> it = tpnotis.iterator();
			while (it.hasNext())  {
				Tipo tp=(Tipo)it.next();
				// Replicamos el tipo de noticia para no modificar la original
				Tipo tpnuevo= (Tipo)BeanUtils.cloneBean(tp);
				tpnuevo.setId(null);
				tpnuevo.setIdmicrosite(mic.getId());
				// creamos los tipos de noticias
				idtpnotic_nueva=bdTipo.grabarTipo(tpnuevo);
				tablamapeos.put(new String("TPN_"+tp.getId()),idtpnotic_nueva);
				lista1.add(tpnuevo);
				// buscamos noticias con ese tipo de noticia
				Iterator<?> it1 = notics.iterator();
				while (it1.hasNext())  {
					Noticia not=(Noticia)it1.next();
					if (not.getTipo().getId().compareTo(tp.getId())==0) {
						// creo la noticia
						Noticia notnueva= (Noticia)BeanUtils.cloneBean(not);
						notnueva.setId(null);
						notnueva.getTipo().setId(idtpnotic_nueva);
						notnueva.setIdmicrosite(mic.getId());
						notnueva.getTipo().setIdmicrosite(mic.getId());
						bdNoticia.grabarNoticia(notnueva);
						tablamapeos.put(new String("NOT_"+not.getId()),notnueva.getId());
						lista2.add(notnueva);
						stlog.append(notnueva.getId() + " ");
					}
				}
			}
			mic.setTiponotis(lista1);
			mic.setNoticias(lista2);
			addImportLogVisual(request, (String)rb.getObject("logimport.elementos") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.elementos") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.elementos.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}

	}
	
	/**
	 *  Esta función crea los Formularios QSSI
	 *  en el nuevo Microsite
	 */
	private void InsertaFrqssis (Set frqssis, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Frqssi> lista= new HashSet<Frqssi>();
		FrqssiDelegate bdFrqssi = DelegateUtil.getFrqssiDelegate();
		try {
			Iterator<?> it = frqssis.iterator();
			while (it.hasNext())  {
				Frqssi frq=(Frqssi)it.next();
				// Replicamos el frqssi para no modificar el original
				Frqssi frqnuevo= (Frqssi)BeanUtils.cloneBean(frq);
				frqnuevo.setId(null);
				frqnuevo.setIdmicrosite(mic.getId());
				bdFrqssi.grabarFrqssi(frqnuevo);
				tablamapeos.put(new String("FRQ_"+frq.getId()),frqnuevo.getId());
				lista.add(frqnuevo);
				stlog.append(frqnuevo.getId() + " ");
			}
			mic.setFrqssis(lista);
			addImportLogVisual(request, (String)rb.getObject("logimport.qssi") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.qssi") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.qssi.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje,  ex.getStackTrace());
		}
		
	}
	

	/**
	 *  Esta función crea los temas y las faqs en el nuevo Microsite
	 */
	private void InsertaFaqs (Set temas, Set faqs, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Long idtema_nuevo;
		Set<Temafaq> lista1= new HashSet<Temafaq>();
		Set<Faq> lista2= new HashSet<Faq>();
		TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
		FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();

		try {
			Iterator<?> it = temas.iterator();
			while (it.hasNext())  {
				Temafaq tema=(Temafaq)it.next();
				// Replicamos el tema de las faq para no modificar la original
				Temafaq temanuevo= (Temafaq)BeanUtils.cloneBean(tema);
				temanuevo.setId(null);
				temanuevo.setIdmicrosite(mic.getId());
				// creamos los temas de las faqs
				idtema_nuevo=bdTema.grabarTema(temanuevo);
				lista1.add(temanuevo);
				// buscamos las faq con ese Tema
				Iterator<?> it1 = faqs.iterator();
				while (it1.hasNext())  {
					Faq faq=(Faq)it1.next();
					if (faq.getTema().getId().compareTo(tema.getId())==0) {
						// creo la faq
						Faq faqnuevo= (Faq)BeanUtils.cloneBean(faq);
						faqnuevo.setId(null);
						faqnuevo.getTema().setId(idtema_nuevo);
						faqnuevo.setIdmicrosite(mic.getId());
						faqnuevo.getTema().setIdmicrosite(mic.getId());
						bdFaq.grabarFaq(faqnuevo);
						lista2.add(faqnuevo);
						stlog.append(faqnuevo.getId() + " ");
					}
				}
			}
			mic.setTemas(lista1);
			mic.setFaqs(lista2);
			addImportLogVisual(request, (String)rb.getObject("logimport.faqs") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.faqs") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.faqs.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}
	
	
	/**
	 *  Esta función crea las encuestas en el nuevo Microsite
	 */
	private void InsertaComponentes (Set compos, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Componente> lista= new HashSet<Componente>();
		ComponenteDelegate bdCompo = DelegateUtil.getComponentesDelegate();
		try {
			Iterator<?> it = compos.iterator();
			while (it.hasNext())  {
				Componente cmp=(Componente)it.next();
				Long idanterior=cmp.getId();
				// Replicamos el componente para no modificar el original
				Componente cmpnuevo= (Componente)BeanUtils.cloneBean(cmp);
				cmpnuevo.setId(null);
				cmpnuevo.setIdmicrosite(mic.getId());

				/***** VRS: pegote para recoger el nuevo tipo de listado asociado al componente ******/ 
				Long idtpnuevo = (Long)tablamapeos.get(new String("TPN_"+ cmp.getTipo().getId()));
				Iterator<?> iter = mic.getTiponotis().iterator();
				while (iter.hasNext()) {
					Tipo tptmp= (Tipo)iter.next();
					if (tptmp.getId().longValue()==idtpnuevo.longValue()) {
						cmpnuevo.setTipo(tptmp);
						break;
					}
				}
				/****** VRS: fin pegote **************************************************************/
				
				bdCompo.grabarComponente(cmpnuevo);
				tablamapeos.put(new String("COM_"+idanterior),cmpnuevo.getId()); 
				lista.add(cmpnuevo);
				stlog.append(cmpnuevo.getId() + " ");
			}
			mic.setComponentes(lista);
			addImportLogVisual(request, (String)rb.getObject("logimport.componentes") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.componentes") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.componentes.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
		
	}

	/**
	 *  Esta función crea los menus y contenidos en el nuevo Microsite
	 */
	private void InsertaMenusContenidos (Set menus, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlogmenu = new StringBuffer("");
		StringBuffer stlogpagina = new StringBuffer("");
		Long idmenu_nuevo;
		Set<Menu> lista1= new HashSet<Menu>();
		Set<Contenido> lista2= new HashSet<Contenido>();
		Hashtable<Long,Long> mapeos=new Hashtable<Long,Long>();
		MenuDelegate bdMenu = DelegateUtil.getMenuDelegate();
		ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();

		try {
			Iterator<?> it = menus.iterator();
			// Guardo primero los padres
			while (it.hasNext())  {
				Menu men=(Menu)it.next();
				if (men.getPadre().intValue()==0) {
					// Replicamos el menu para no modificar la original
					Menu menunuevo= (Menu)BeanUtils.cloneBean(men);
					menunuevo.setId(null);
					menunuevo.setIdmicrosite(mic.getId());
					Set contenidos=menunuevo.getContenidos();
					menunuevo.setContenidos(null);  // vacío los contenidos
					// creamos el menus nuevo vacio
					idmenu_nuevo=bdMenu.grabarMenu(menunuevo);
	
					mapeos.put(men.getId(), menunuevo.getId());  // anterior y nuevo del padre
					
					// buscamos los contenidos de ese menu
					Iterator<?> it1 = contenidos.iterator();
					lista2= new HashSet<Contenido>();
					while (it1.hasNext())  {
						Contenido con=(Contenido)it1.next();
						// creo el contenido
						Contenido connuevo= (Contenido)BeanUtils.cloneBean(con);
						connuevo.setId(null);
						connuevo.setIdmenu(idmenu_nuevo);
						bdConte.grabarContenido(connuevo);
						tablamapeos.put(new String("CON_"+con.getId()),connuevo.getId());
						lista2.add(connuevo);
						stlogpagina.append(connuevo.getId() + " ");
					}
					menunuevo.setContenidos(lista2);
					lista1.add(menunuevo);
					stlogmenu.append(menunuevo.getId() + " ");
				}
			}
			
			it = menus.iterator();
			// Por último los hijos
			while (it.hasNext())  {
				Menu men=(Menu)it.next();
				if (men.getPadre().intValue()>0) {
					// Replicamos el menu para no modificar la original
					Menu menunuevo= (Menu)BeanUtils.cloneBean(men);
					menunuevo.setId(null);
					menunuevo.setIdmicrosite(mic.getId());
					menunuevo.setPadre((Long)mapeos.get(men.getPadre()));
					Set contenidos=menunuevo.getContenidos();
					menunuevo.setContenidos(null);  // vacío los contenidos
					// creamos el menus nuevo vacio
					idmenu_nuevo=bdMenu.grabarMenu(menunuevo);
	
					// buscamos los contenidos de ese menu
					Iterator<?> it1 = contenidos.iterator();
					lista2= new HashSet<Contenido>();
					while (it1.hasNext())  {
						Contenido con=(Contenido)it1.next();
						// creo el contenido
						Contenido connuevo= (Contenido)BeanUtils.cloneBean(con);
						connuevo.setId(null);
						connuevo.setIdmenu(idmenu_nuevo);
						bdConte.grabarContenido(connuevo);
						tablamapeos.put(new String("CON_"+con.getId()),connuevo.getId());
						lista2.add(connuevo);
						stlogpagina.append(connuevo.getId() + " ");
					}
					menunuevo.setContenidos(lista2);
					lista1.add(menunuevo);
					stlogmenu.append(menunuevo.getId() + " ");
				}
			}			
			
			mic.setMenus(lista1);
			addImportLogVisual(request, (String)rb.getObject("logimport.menus") + ": " + stlogmenu.toString());
			addImportLogVisual(request, (String)rb.getObject("logimport.paginas") + ": " + stlogpagina.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.menus") + ": " + stlogmenu.toString());
			addImportLogVisual(request, (String)rb.getObject("logimport.paginas") + ": " + stlogpagina.toString());
			mensaje+=(String)rb.getObject("logimport.paginas.error") + ": "+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
		
	}
	
	
	/**
	 *  Esta función crea las encuestas en el nuevo Microsite
	 */
	private void InsertaEncuestas (Set encus, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Encuesta> lista= new HashSet<Encuesta>();
		EncuestaDelegate bdEncu = DelegateUtil.getEncuestaDelegate();
		try {
			Iterator<?> it = encus.iterator();
			while (it.hasNext())  {
				Encuesta enc=(Encuesta)it.next();
				// Replicamos la encuesta para no modificar la original
				Encuesta encnueva= (Encuesta)BeanUtils.cloneBean(enc);
				encnueva.setId(null);
				encnueva.setIdmicrosite(mic.getId());
				bdEncu.grabarEncuesta(encnueva);
				tablamapeos.put(new String("ENC_"+enc.getId()),encnueva.getId());
				lista.add(encnueva);
				stlog.append(encnueva.getId() + " ");
			}
			mic.setEncuestas(lista);
			addImportLogVisual(request, (String)rb.getObject("logimport.encuestas") + ": " + stlog.toString());
		}
		catch (Exception ex) {
			addImportLogVisual(request, (String)rb.getObject("logimport.encuestas") + ": " + stlog.toString());
			mensaje+=(String)rb.getObject("logimport.encuestas.error") + ": <br/>"+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
		
	}
	
	
	// Actualiza los objetos que tienen campo URL
	private void ActualizaURLMicrositeCompleto(Hashtable mapa, MicrositeCompleto mic, HttpServletRequest request) {
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		try {
			// Actualizo banners
			Set objs=mic.getBanners();
			Iterator<?> it;
			if (objs!=null) {
				it=objs.iterator();
				while (it.hasNext())  {
					Banner obj=(Banner)it.next();
					obj=(Banner)actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getBannerDelegate().grabarBanner(obj);
				}
			}
    	
			// Actualizo agendas
			objs=mic.getAgendas();
			if (objs!=null) {
				it=objs.iterator();
				while (it.hasNext())  {
					Agenda obj=(Agenda)it.next();
					obj=(Agenda)actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getAgendaDelegate().grabarAgenda(obj);
				}
			}
			
			// Actualizo contenidos de los menus

			Set menus=mic.getMenus();
			if (menus!=null) {
				Iterator<?> itmenu=menus.iterator();
				while (itmenu.hasNext()) {
					Menu menu=(Menu)itmenu.next();
					
					objs=menu.getContenidos();
					if (objs!=null) {
						it=objs.iterator();
						while (it.hasNext())  {
							Contenido obj=(Contenido)it.next();
							obj=(Contenido)actualizaURL(obj, mic.getId(), request);
							DelegateUtil.getContenidoDelegate().grabarContenido(obj);
						}
					}
					
				}
				
			}

			
			// Actualizo faqs
			objs=mic.getFaqs();
			if (objs!=null) {
				it=objs.iterator();
				while (it.hasNext())  {
					Faq obj=(Faq)it.next();
					obj=(Faq)actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getFaqDelegate().grabarFaq(obj);
				}
			}

			// Actualizo Noticias
			objs=mic.getNoticias();
			if (objs!=null) {
				it=objs.iterator();
				while (it.hasNext())  {
					Noticia obj=(Noticia)it.next();
					obj=(Noticia)actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getNoticiasDelegate().grabarNoticia(obj);
				}
			}
			
			// Actualizo Frqssis
			objs=mic.getFrqssis();
			if (objs!=null) {
				it=objs.iterator();
				while (it.hasNext())  {
					Frqssi obj=(Frqssi)it.next();
					obj=(Frqssi)actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getFrqssiDelegate().grabarFrqssi(obj);
				}
			}			
			
			// Actualizo Documentos / Archivos
			objs=mic.getDocus();
			if (objs!=null) {
				it=objs.iterator();
				while (it.hasNext())  {
					Archivo obj=(Archivo)it.next();
					obj=(Archivo)actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getArchivoDelegate().grabarArchivo(obj);
				}
			}				
			
			// Actualizo el microsite solo
			MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
			Microsite mic2 = bdMicro.obtenerMicrosite(mic.getId());
			mic2=(Microsite)actualizaURL(mic2, mic.getId(), request);
			bdMicro.grabarMicrosite(mic2);
			
		}
		catch (Exception ex) {
			mensaje+=(String)rb.getObject("logimport.urls.error") + ": <br/>"+ ex.toString()+"<br/>";
			addImportLogVisualStackTrace(request, "[WARN] " + mensaje, ex.getStackTrace());
		}
	
		
	}
		
	private void calculaHshURLs(Long idsitenew) {
		  
		  //TODO El día que se utilizen keys significativas para identificar objetos, este tinglao que hay aquí no
		  //no tendrá sentido (ya no hará falta). De todas formas, habrá que pensar en los microsites ya existentes.
		
		
		  Hashtable<String,String> hshmapeoimagenes = new Hashtable<String,String>();
		   
		  String oldurl="";
		  String newurl="";
		  Enumeration<String> enumera = tablamapeos.keys();
		     while (enumera.hasMoreElements()) {
		      String clave = (String)enumera.nextElement();
		      String newidcont = "" + ((Long)tablamapeos.get(clave)).longValue();
		      String oldidcont = clave.substring(4,clave.length());
		 
		      if (clave.indexOf("NOT_")!=-1) {
		       oldurl="noticia.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="noticia.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);
		       oldurl="noticia.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="noticia.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);		       
		       oldurl="noticia.do?cont="+oldidcont+"&idsite="+idmicroant.longValue();
		       newurl="noticia.do?cont="+newidcont+"&idsite="+idsitenew.longValue();
		       hshURLs.put(oldurl,newurl);
		       oldurl="archivopub.do?ctrl=NTCS0"+oldidcont;
		       newurl="archivopub.do?ctrl=NTCS0"+newidcont;
		       hshmapeoimagenes.put(oldurl,newurl);       
		      }
		      
		      if (clave.indexOf("TPN_")!=-1) {
		       oldurl="noticias.do?idsite="+idmicroant.longValue()+"&tipo="+oldidcont;
		       newurl="noticias.do?idsite="+idsitenew.longValue()+"&tipo="+newidcont;
		       hshURLs.put(oldurl,newurl);
		       oldurl="noticias.do?idsite="+idmicroant.longValue()+"&tipo="+oldidcont;
		       newurl="noticias.do?idsite="+idsitenew.longValue()+"&tipo="+newidcont;
		       hshURLs.put(oldurl,newurl);		       
		       oldurl="noticias.do?tipo="+oldidcont+"&idsite="+idmicroant.longValue();
		       newurl="noticias.do?tipo="+newidcont+"&idsite="+idsitenew.longValue();
		       hshURLs.put(oldurl,newurl);
		       
		       //ahora las de los tags dummys de contenido
		       oldurl="propertyid=\""+oldidcont+"\"";
		       newurl="propertyid=\""+newidcont+"\"";
		       hshURLs.put(oldurl,newurl);
		       oldurl="propertyID=\""+oldidcont+"\"";
		       newurl="propertyID=\""+newidcont+"\"";
		       hshURLs.put(oldurl,newurl);
		      }      

		      if (clave.indexOf("COM_")!=-1) {
			       oldurl="componenteid=\""+oldidcont+"\"";
			       newurl="componenteid=\""+newidcont+"\"";
			       hshURLs.put(oldurl,newurl);
			       oldurl="componenteID=\""+oldidcont+"\"";
			       newurl="componenteID=\""+newidcont+"\"";
			       hshURLs.put(oldurl,newurl);		       
			      } 
		      
		      if (clave.indexOf("ENC_")!=-1) {
			       oldurl="encuesta.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
			       newurl="encuesta.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
			       hshURLs.put(oldurl,newurl);
			       oldurl="encuesta.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
			       newurl="encuesta.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
			       hshURLs.put(oldurl,newurl);		       
			       oldurl="encuesta.do?cont="+oldidcont+"&idsite="+idmicroant.longValue();
			       newurl="encuesta.do?cont="+newidcont+"&idsite="+idsitenew.longValue();
			       hshURLs.put(oldurl,newurl);
			       
			       //ahora las de los tags dummys de encuesta para el contenido
			       oldurl="componenteid=\""+oldidcont+"\"";
			       newurl="componenteid=\""+newidcont+"\"";
			       hshURLs.put(oldurl,newurl);			       
			       oldurl="componenteID=\""+oldidcont+"\"";
			       newurl="componenteID=\""+newidcont+"\"";
			       hshURLs.put(oldurl,newurl);		       
			  }		      
		      
		      if (clave.indexOf("FRM_")!=-1) {
		       oldurl="contacto.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="contacto.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);
		       oldurl="contacto.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="contacto.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);		       
		       oldurl="contacto.do?cont="+oldidcont+"&idsite="+idmicroant.longValue();
		       newurl="contacto.do?cont="+newidcont+"&idsite="+idsitenew.longValue();
		       hshURLs.put(oldurl,newurl);
		      }        
		      
		      if (clave.indexOf("FRQ_")!=-1) {
		       oldurl="frqssi.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="frqssi.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);
		       oldurl="frqssi.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="frqssi.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);		       
		       oldurl="frqssi.do?cont="+oldidcont+"&idsite="+idmicroant.longValue();
		       newurl="frqssi.do?cont="+newidcont+"&idsite="+idsitenew.longValue();
		       hshURLs.put(oldurl,newurl);
		      }        		      
		      
		      if (clave.indexOf("CON_")!=-1) {
		       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="contenido.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);
		       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
		       newurl="contenido.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
		       hshURLs.put(oldurl,newurl);		       
		       oldurl="contenido.do?cont="+oldidcont+"&idsite="+idmicroant.longValue();
		       newurl="contenido.do?cont="+newidcont+"&idsite="+idsitenew.longValue();
		       hshURLs.put(oldurl,newurl);
		       
		       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&amp;cont="+oldidcont;
		       newurl="contenido.do?idsite="+idsitenew.longValue()+"&amp;cont="+newidcont;
		       hshURLs.put(oldurl,newurl);
		       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&amp;cont="+oldidcont;
		       newurl="contenido.do?idsite="+idsitenew.longValue()+"&amp;cont="+newidcont;
		       hshURLs.put(oldurl,newurl);		       
		       oldurl="contenido.do?cont="+oldidcont+"&amp;idsite="+idmicroant.longValue();
		       newurl="contenido.do?cont="+newidcont+"&amp;idsite="+idsitenew.longValue();
		       hshURLs.put(oldurl,newurl);			       
		       
		       
		       oldurl="archivopub.do?ctrl=CNTSP"+oldidcont;
		       newurl="archivopub.do?ctrl=CNTSP"+newidcont;
		       hshmapeoimagenes.put(oldurl,newurl);
		      }      
		 
		      if (clave.indexOf("BAN_")!=-1) {
		       oldurl="archivopub.do?ctrl=BNNR0"+oldidcont;
		       newurl="archivopub.do?ctrl=BNNR0"+newidcont;
		       hshmapeoimagenes.put(oldurl,newurl);
		      }
		      
		      /* FNRUIZ */
		      // Los documento de la cabecera y pie del microsite ¿¿¿???
		      if (clave.indexOf("MIC_")!=-1) {
			       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
			       newurl="contenido.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
			       hshURLs.put(oldurl,newurl);
			       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&cont="+oldidcont;
			       newurl="contenido.do?idsite="+idsitenew.longValue()+"&cont="+newidcont;
			       hshURLs.put(oldurl,newurl);		       
			       oldurl="contenido.do?cont="+oldidcont+"&idsite="+idmicroant.longValue();
			       newurl="contenido.do?cont="+newidcont+"&idsite="+idsitenew.longValue();
			       hshURLs.put(oldurl,newurl);	
			       
			       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&amp;cont="+oldidcont;
			       newurl="contenido.do?idsite="+idsitenew.longValue()+"&amp;cont="+newidcont;
			       hshURLs.put(oldurl,newurl);
			       oldurl="contenido.do?idsite="+idmicroant.longValue()+"&amp;cont="+oldidcont;
			       newurl="contenido.do?idsite="+idsitenew.longValue()+"&amp;cont="+newidcont;
			       hshURLs.put(oldurl,newurl);		       
			       oldurl="contenido.do?cont="+oldidcont+"&amp;idsite="+idmicroant.longValue();
			       newurl="contenido.do?cont="+newidcont+"&amp;idsite="+idsitenew.longValue();
			       hshURLs.put(oldurl,newurl);	
			       
		    	   oldurl="archivopub.do?ctrl=MCRST"+oldidcont;
			       newurl="archivopub.do?ctrl=MCRST"+newidcont;
			       hshmapeoimagenes.put(oldurl,newurl);
		      }
		      /* FNRUIZ */
		      
		     }
		     
		     //ahora se vuelve a recorrer y se cogen solo los documentos y se concatenan a lo que ya habíamos montado temporalmente
		  Enumeration<String> enumera2 = tablamapeos.keys();
		     while (enumera2.hasMoreElements()) {
		      String clave = (String)enumera2.nextElement();
		      String newidcont = "" + ((Long)tablamapeos.get(clave)).longValue();
		      String oldidcont = clave.substring(4,clave.length());
		      
		      if (clave.indexOf("DCM_")!=-1) {
		       Enumeration<String> enumera3 = hshmapeoimagenes.keys();
		          while (enumera3.hasMoreElements()) {
		           String clave3 = (String)enumera3.nextElement();
		           String newidcont3 = (String)hshmapeoimagenes.get(clave3);
		           
		           oldurl=clave3+"ZI"+oldidcont+"&amp;id="+oldidcont;
		           newurl=newidcont3+"ZI"+newidcont+"&amp;id="+newidcont;
		           hshURLs.put(oldurl,newurl);  
		           oldurl=clave3+"ZI"+oldidcont+"&amp;id="+oldidcont;
		           newurl=newidcont3+"ZI"+newidcont+"&amp;id="+newidcont;
		           hshURLs.put(oldurl,newurl);  		           
		          }
		      }      
		     }
		     
		     //ahora las que son fijas en cada servicio
		  oldurl="home.do?idsite="+idmicroant.longValue();
		  newurl="home.do?idsite="+idsitenew.longValue();
		  hshURLs.put(oldurl,newurl);       
		  
		  oldurl="faqs.do?idsite="+idmicroant.longValue();
		  newurl="faqs.do?idsite="+idsitenew.longValue();
		  hshURLs.put(oldurl,newurl);      
		  
		  oldurl="agendas.do?idsite="+idmicroant.longValue();
		  newurl="agendas.do?idsite="+idsitenew.longValue();
		  hshURLs.put(oldurl,newurl);      
		 
		  oldurl="contactos.do?idsite="+idmicroant.longValue();
		  newurl="contactos.do?idsite="+idsitenew.longValue();
		  hshURLs.put(oldurl,newurl);  
		  
		  oldurl="mapa.do?idsite="+idmicroant.longValue();
		  newurl="mapa.do?idsite="+idsitenew.longValue();
		  hshURLs.put(oldurl,newurl);    
		  
		  oldurl="procedimientos.do?idsite="+idmicroant.longValue();
		  newurl="procedimientos.do?idsite="+idsitenew.longValue();
		  hshURLs.put(oldurl,newurl);      
		  
		 }
		 
		 private Object actualizaURL(Object objeto, Long idsitenew, HttpServletRequest request) {
		  Object retorno=null;
		  retorno=objeto;
		  
		  
		  if (objeto instanceof Noticia) {
		   Noticia noti = (Noticia)objeto;
		   Map<?, ?> mapatraducciones = (Map<?, ?>)noti.getTraduccionMap();
		   Iterator<?> iter = mapatraducciones.values().iterator();
		   while (iter.hasNext()) {
			   TraduccionNoticia tranot = (TraduccionNoticia)iter.next();
			   if (tranot!=null) {
				   tranot.setLaurl(reemplazaURL(tranot.getLaurl(), "" + idsitenew.longValue() , request));
				   tranot.setTexto(reemplazaContenido(tranot.getTexto(), "" +idmicroant.longValue(), "" + idsitenew.longValue(), request));
			   }
		   }
		      return noti;
		  }
		  
/*		  if (objeto instanceof Frqssi) {
			   Frqssi frq = (Frqssi)objeto;
			   Map mapatraducciones = (Map)frq.getTraduccionMap();
			   Iterator iter = mapatraducciones.values().iterator();
			   while (iter.hasNext()) {
				   TraduccionFrqssi trafrq = (TraduccionFrqssi)iter.next();
				   if (trafrq!=null) {					   
					   trafrq.setNombre(reemplazaContenido(trafrq.getNombre(), "" +idmicroant.longValue(), "" + idsitenew.longValue()));
				   }
			   }
			      return frq;
			  }		  
*/
		  
		  if (objeto instanceof Agenda) {
		   Agenda agen = (Agenda)objeto;
		   Map<?, ?> mapatraducciones = (Map<?, ?>)agen.getTraduccionMap();
		   Iterator<?> iter = mapatraducciones.values().iterator();
		   while (iter.hasNext()) {
			   TraduccionAgenda traagen = (TraduccionAgenda)iter.next();	
			   if (traagen!=null) {
				   traagen.setUrl(reemplazaURL(traagen.getUrl(), "" + idsitenew.longValue() , request));
			   }
		   }

		      return agen;
		  }
		 
		  if (objeto instanceof Banner) {
		   Banner ban = (Banner)objeto;
		   		Map<?, ?> mapatraducciones = (Map<?, ?>)ban.getTraduccionMap();
			   Iterator<?> iter = mapatraducciones.values().iterator();
			   while (iter.hasNext()) {
				   TraduccionBanner traban = (TraduccionBanner)iter.next();
				   if (traban!=null) {
					   traban.setUrl(reemplazaURL(traban.getUrl(), "" + idsitenew.longValue() , request));
				   }
			   }		   
		      return ban;
		  }  
		 
		  if (objeto instanceof Faq) {
		   Faq faq = (Faq)objeto;
		   		Map<?, ?> mapatraducciones = (Map<?, ?>)faq.getTraduccionMap();
			   Iterator<?> iter = mapatraducciones.values().iterator();
			   while (iter.hasNext()) {
				   TraduccionFaq trafaq = (TraduccionFaq)iter.next();
				   if (trafaq!=null) {
					   trafaq.setUrl(reemplazaURL(trafaq.getUrl(), "" + idsitenew.longValue() , request));
				   }
			   }				   
		      return faq;
		  }  
		  
		  
		  if (objeto instanceof Contenido) {
		   Contenido conte = (Contenido)objeto;
		   		Map<?, ?> mapatraducciones = (Map<?, ?>)conte.getTraduccionMap();
			   Iterator<?> iter = mapatraducciones.values().iterator();
			   while (iter.hasNext()) {
				   TraduccionContenido traconte = (TraduccionContenido)iter.next();
				   if (traconte!=null) {
					   traconte.setUrl(reemplazaURL(traconte.getUrl(), "" + idsitenew.longValue() , request));
					   traconte.setTexto(reemplazaContenido(traconte.getTexto(), "" +idmicroant.longValue(), "" + idsitenew.longValue(), request));
				   }
			   }
		  }  
		  
		 
		  if (objeto instanceof Archivo) {
			   Archivo archi = (Archivo)objeto;
			   		Map<?, ?> mapatraducciones = (Map<?, ?>)archi.getTraduccionMap();
				   Iterator<?> iter = mapatraducciones.values().iterator();
				   while (iter.hasNext()) {
					   TraduccionArchivo traarchi = (TraduccionArchivo)iter.next();
					   if (traarchi!=null) {
						   traarchi.setUrl(reemplazaURL(traarchi.getUrl(), "" + idsitenew.longValue() , request));
						   traarchi.setTexto(reemplazaContenido(traarchi.getTexto(), "" +idmicroant.longValue(), "" + idsitenew.longValue(), request));
					   }
				   }
			  }  
		  
		  
		  if (objeto instanceof Microsite) {
		   Microsite micro = (Microsite)objeto;
		   
		   
		   micro.setUrlcampanya(reemplazaURL(micro.getUrlcampanya(), "" + idsitenew.longValue() , request));
		   micro.setUrlhome(reemplazaURL(micro.getUrlhome(), "" + idsitenew.longValue() , request));
		   
		   Map<?, ?> mapatraducciones = (Map<?, ?>)micro.getTraduccionMap();
		   Iterator<?> iter = mapatraducciones.values().iterator();
		   while (iter.hasNext()) {
			   TraduccionMicrosite tramicro = (TraduccionMicrosite)iter.next();	
			   if (tramicro!=null) {
				   tramicro.setCabecerapersonal(reemplazaContenido(tramicro.getCabecerapersonal(), "" +idmicroant.longValue(), "" + idsitenew.longValue(), request));
				   tramicro.setPiepersonal(reemplazaContenido(tramicro.getPiepersonal(), "" +idmicroant.longValue(), "" + idsitenew.longValue(), request));
				   tramicro.setUrlop1(reemplazaURL(tramicro.getUrlop1(), "" + idsitenew.longValue() , request));
				   tramicro.setUrlop2(reemplazaURL(tramicro.getUrlop2(), "" + idsitenew.longValue() , request));
				   tramicro.setUrlop3(reemplazaURL(tramicro.getUrlop3(), "" + idsitenew.longValue() , request));
				   tramicro.setUrlop4(reemplazaURL(tramicro.getUrlop4(), "" + idsitenew.longValue() , request));
				   tramicro.setUrlop5(reemplazaURL(tramicro.getUrlop5(), "" + idsitenew.longValue() , request));
				   tramicro.setUrlop6(reemplazaURL(tramicro.getUrlop6(), "" + idsitenew.longValue() , request));
				   tramicro.setUrlop7(reemplazaURL(tramicro.getUrlop7(), "" + idsitenew.longValue() , request));
			   }
		   }
		   
		      return micro;
		  }  
		  
		  return retorno;
		 }
		 
		 private String reemplazaContenido(String cadenacontenido, String idsiteold, String idsitenew, HttpServletRequest request) {
			  String retornocontenido="";
			  retornocontenido=cadenacontenido;
			  if (cadenacontenido!=null) {
				  	StringBuffer stbuf=new StringBuffer(cadenacontenido);
				  	Enumeration<String> enumera = hshURLs.keys();
				     while (enumera.hasMoreElements()) {
						      String oldstringtmp = (String)enumera.nextElement();
						      String newstringtmp = (String)hshURLs.get(oldstringtmp);
						      
						      //retornocontenido = retornocontenido.replaceAll(oldstringtmp, newstringtmp);
						      int pos = stbuf.indexOf(oldstringtmp);
						      while (pos > -1) {
									stbuf.replace(pos, pos + oldstringtmp.length(), newstringtmp);
									pos = stbuf.toString().indexOf(oldstringtmp, pos + newstringtmp.length());
						      }
				     }
				     retornocontenido=stbuf.toString();
			  }   
			  return retornocontenido;
		 }
		 
		 private String reemplazaURL(String cadenaurl, String idsitenew, HttpServletRequest request) {
		  String retornourl="";
		  
		   
		  retornourl = cadenaurl;
		  if (retornourl!=null) {
				  //noticia.do?idsite=234&cont=32
				  if (retornourl.indexOf("noticia.do")!=-1)  {
				   retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "NOT_", idsitenew, request);
				  } 

				  //frqssi.do?idsite=24&cont=15
				  if (retornourl.indexOf("frqssi.do")!=-1)  {
				    retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "FRQ_", idsitenew, request);
				  }				  
				  
				  //noticias.do?idsite=24&tipo=7
				  if (retornourl.indexOf("noticias.do")!=-1)  {
				    retornourl = reemplazaParametrosURL1(retornourl, "idsite", "tipo", "TPN_", idsitenew, request);
				  }
				 
				  //contacto.do?idsite=24&cont=15
				  if (retornourl.indexOf("contacto.do")!=-1)  {
				    retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "FRM_", idsitenew, request);
				  }
				 
				  //contenido.do?idsite=24&cont=65
				  if (retornourl.indexOf("contenido.do")!=-1)  {
				    retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "CON_", idsitenew, request);
				  }

				  //encuesta.do?idsite=24&cont=65
				  if (retornourl.indexOf("encuesta.do")!=-1)  {
				    retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "ENC_", idsitenew, request);
				  }
				  
				  //home.do?idsite=24
				  if (retornourl.indexOf("home.do")!=-1)  {
				    retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
				  }
				  
				  //mapa.do?idsite=24
				  if (retornourl.indexOf("mapa.do")!=-1)  {
				    retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
				  }
				 
				  //agendas.do?idsite=24
				  if (retornourl.indexOf("agendas.do")!=-1)  {
				    retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
				  }
				  
				  //faqs.do?idsite=24
				  if (retornourl.indexOf("faqs.do")!=-1)  {
				    retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
				  } 
				 
				  //contactos.do?idsite=24
				  if (retornourl.indexOf("contactos.do")!=-1)  {
				    retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
				  }
				 
				  //procedimientos.do?idsite=24
				  if (retornourl.indexOf("procedimientos.do")!=-1)  {
				    retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
				  }  
				  
				  //archivopub.do?ctrl=CNTSP234ZI207&id=207
				  if (retornourl.indexOf("archivopub.do")!=-1)  {
				   retornourl = reemplazaParametrosURL3(retornourl, "ctrl", "id", "DCM_", request);
				  }  
		  }
		  return retornourl;
		 }
		 
		 private String reemplazaParametrosURL1(String cadena, String nomparamIdsite, String nomparam2, String tipomapeo, String idsitenew, HttpServletRequest request) {
		  //reemplazar 2 parametros
		  ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		  String retornourl="";
		  retornourl = cadena;
		  try {
		    
		    String txt_param_key="";
		    String txt_param_value="";
		 
		       String str[]=null;
		       String strparam[]=null;
		      
		       StringTokenizer st=new StringTokenizer(retornourl,"?");
		       int n=st.countTokens();
		       if (n==2) {
		         //uri más parametros
		         str= new String[n];
		         for (int i=0;i<n;i++) {
		          str[i]=st.nextToken();
		         }
		         if (str[1].length()>12) {
		           StringTokenizer stparam=new StringTokenizer(str[1],"&");
		           int z=stparam.countTokens();
		           strparam= new String[z];
		           for (int i=0;i<z;i++) {
			             strparam[i]=stparam.nextToken();
			             StringTokenizer stkeyvalue=new StringTokenizer(strparam[i],"=");
			             int y=stkeyvalue.countTokens();
			             if (y>1) {
					               txt_param_key=stkeyvalue.nextToken();
					               txt_param_value=stkeyvalue.nextToken();
					               if (txt_param_key.equals( nomparamIdsite )) {
					                retornourl=retornourl.replaceAll(nomparamIdsite+"="+txt_param_value,nomparamIdsite+"="+idsitenew);
					               }
					               if (txt_param_key.equals( nomparam2 )) {
					            	if ( tablamapeos.get(tipomapeo + txt_param_value) != null)
					            	{
					            		String newid = "" + ((Long)tablamapeos.get(tipomapeo + txt_param_value)).longValue();
					            		retornourl=retornourl.replaceAll(nomparam2+"="+txt_param_value,nomparam2+"="+newid);
					            	}
					               }
			             }
		           }
		          
		         }

		       }  
		 
		  } catch (Exception e) {
			   log.error((String)rb.getObject("logimport.url1.error") + ": " + e.getMessage());
			   addImportLogVisualStackTrace(request, "[WARN] " +(String)rb.getObject("logimport.url1.error") + ": " + e.getMessage(), e.getStackTrace());
		  }
		  return retornourl;
		 }
		 
		 private String reemplazaParametrosURL2(String cadena, String nomparam1, String idsitenew, HttpServletRequest request) {
		  //reemplazar 1 parametro, y sin tipo de mapeo
			 ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		  String retornourl="";
		  retornourl = cadena;
		  try {  
		  
		    String txt_param_key="";
		    String txt_param_value="";
		  
		       String str[]=null;
		       String strparam[]=null;
		      
		       StringTokenizer st=new StringTokenizer(retornourl,"?");
		       int n=st.countTokens();
		       if (n==2) {
		         //uri más parametros
		         str= new String[n];
		         for (int i=0;i<n;i++) {
		          str[i]=st.nextToken();
		         }
		         if (str[1].length()>5) {
		           StringTokenizer stparam=new StringTokenizer(str[1],"&");
		           int z=stparam.countTokens();
		           strparam= new String[z];
		           for (int i=0;i<z;i++) {
		             strparam[i]=stparam.nextToken();
		             StringTokenizer stkeyvalue=new StringTokenizer(strparam[i],"=");
		             int y=stkeyvalue.countTokens();
		             if (y>0) {
		               txt_param_key=stkeyvalue.nextToken();
		               txt_param_value=stkeyvalue.nextToken();
		               if (txt_param_key.equals( nomparam1 )) {
		                retornourl=retornourl.replaceAll(nomparam1+"="+txt_param_value,nomparam1+"="+idsitenew);
		               }
		             }
		           }
		          
		         }
		       }  
		  } catch (Exception e) {
			   log.error((String)rb.getObject("logimport.url2.error") + ": " + e.getMessage());
			   addImportLogVisualStackTrace(request, "[WARN] " +(String)rb.getObject("logimport.url2.error") + ": " + e.getMessage(), e.getStackTrace());
		  }
		 
		  
		  return retornourl;
		 } 
		 
		 private String reemplazaParametrosURL3(String cadena, String nomparamCtrl, String nomparam2, String tipomapeo, HttpServletRequest request) {
		  //reemplazar 2 parametros de archivos de imagen
			 ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		  String retornourl="";
		  retornourl = cadena;
		  try {  
		  
		    String txt_param_key="";
		    String txt_param_value="";
		  
		       String str[]=null;
		       String strparam[]=null;
		   
		    StringTokenizer st=new StringTokenizer(retornourl,"?");
		    int n=st.countTokens();
		    if (n==2) {
		      //uri más parametros
		         str= new String[n];
		         for (int i=0;i<n;i++) {
		          str[i]=st.nextToken();
		         }
		         if (str[1].length()>15) {
		           StringTokenizer stparam=new StringTokenizer(str[1],"&");
		           int z=stparam.countTokens();
		           strparam= new String[z];
		           for (int i=0;i<z;i++) {
		             strparam[i]=stparam.nextToken();
		             StringTokenizer stkeyvalue=new StringTokenizer(strparam[i],"=");
		             int y=stkeyvalue.countTokens();
		             if (y==2) {
		               txt_param_key=stkeyvalue.nextToken();
		               txt_param_value=stkeyvalue.nextToken();
		               
		               if (txt_param_key.equals( nomparamCtrl )) {
		                //aquí hay que averiguar ademas el id del servicio y el tipo de servicio
		                String tiposervicio=txt_param_value.substring(0,5);
		                int pos_zi = txt_param_value.indexOf("ZI");
		                String oldidservicio=txt_param_value.substring(5,pos_zi);
		                String oldiditem = txt_param_value.substring(pos_zi + 2, txt_param_value.length());
		                
		                //String newiditem = (String)tablamapeos.get(tipomapeo + oldiditem);
		                String newiditem = "" + ((Long)tablamapeos.get(tipomapeo + oldiditem)).longValue();
		                
		                
		                String newidservicio="";
		                if (tiposervicio.equals("BNNR0")) 
		                 newidservicio = "" + ((Long)tablamapeos.get("BAN_" + oldidservicio)).longValue();
		                if (tiposervicio.equals("CNTSP")) 
		                 newidservicio = "" + ((Long)tablamapeos.get("CON_" + oldidservicio)).longValue();
		                if (tiposervicio.equals("NTCS0")) 
		                 newidservicio = "" + ((Long)tablamapeos.get("NOT_" + oldidservicio)).longValue();
		                if (tiposervicio.equals("FQSSI")) 
		                 newidservicio = "" + ((Long)tablamapeos.get("FRQ_" + oldidservicio)).longValue();
		                if (tiposervicio.equals("MCRST"))
		                	newidservicio = "" + ((Long)tablamapeos.get("MIC_" + oldidservicio)).longValue();
		                
		                retornourl=retornourl.replaceAll(nomparamCtrl+"="+tiposervicio+oldidservicio+"ZI"+oldiditem,nomparamCtrl+"="+tiposervicio+newidservicio+"ZI"+newiditem);
		                
		               }
		               if (txt_param_key.equals( nomparam2 )) {
			            	if ( tablamapeos.get(tipomapeo + txt_param_value) != null)
			            	{
			            		String newid = "" + ((Long)tablamapeos.get(tipomapeo + txt_param_value)).longValue();
			            		retornourl=retornourl.replaceAll(nomparam2+"="+txt_param_value,nomparam2+"="+newid);
			            	}
		               }
		             }
		           }
		          
		         }
		    }  
		  } catch (Exception e) {
			   log.error((String)rb.getObject("logimport.url3.error") + ": " + e.getMessage());
			   addImportLogVisualStackTrace(request, "[WARN] " +(String)rb.getObject("logimport.url3.error") + ": " + e.getMessage(), e.getStackTrace());
		  }
		  
		  return retornourl;
		 } 

		 private void addImportLog(String mensaje) {
			 MicroLog.addLog("{i" + _hashcode + "} " + mensaje);
		 }
		 
		 @SuppressWarnings("unused")
		private void addImportLogStackTrace(String mensaje, StackTraceElement[] mensajes) {
			 MicroLog.addLogStackTrace("{i" + _hashcode + "} " + mensaje, mensajes );
		 }		 
		 
		 private void addImportLogVisual(HttpServletRequest request, String mensaje) {
			 MicroLog.addLogVisual(request, "{i" + _hashcode + "} " + mensaje);
		 }
		 
		 private void addImportLogVisualStackTrace(HttpServletRequest request, String mensaje, StackTraceElement[] mensajes) {
			 MicroLog.addLogVisualStackTrace(request, "{i" + _hashcode + "} " + mensaje, mensajes );
		 }		 
}
