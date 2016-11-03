package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.agendaForm;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorException;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micromodel.Accesibilidad;
import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionAgendaPK;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.solrutiles.solr.model.Catalogo;

/**
 * Action que edita las agendas (eventos) de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/agendaEdita" <BR> 
 *  name="agendaForm" <BR> 
 *  input="/agendasAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleAgenda.jsp" <BR>
 *  
 *  @author Indra
 */
public class agendasEditaAction extends BaseAction {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */
	
	protected static Log log = LogFactory.getLog(agendasEditaAction.class);
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {

    	AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
        Agenda age = null;
        agendaForm agendaForm = (agendaForm) form;
        if (request.getParameter("accion") != null) {

            /*-------------------- GUARDAR --------------------*/
            if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar"))) {
                boolean isNuevaActividad = false;

                // ALTA
                if (agendaForm.get("id") == null) {
                    age = new Agenda();
                    isNuevaActividad = true;

	            // MODIFICACION
                } else {
                    age = bdAgenda.obtenerAgenda((Long) agendaForm.get("id"));
                    //************COMPROBACION DE IDES*************
                    if (age.getIdmicrosite().longValue() != ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().longValue()) {
                        addMessageError(request, "peticion.error");
                        return mapping.findForward("info");
                    }
                }

                age.setIdmicrosite(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId());
		       	age.setFinicio(agendaForm.getFinicio());
		       	age.setFfin(agendaForm.getFfin());
		       	age.setVisible("" + agendaForm.get("visible"));
		       	age.setOrganizador("" + agendaForm.get("organizador"));
		
		        // Establezco el objeto Actividad del evento
		        Actividadagenda activi = null;
		        ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();

                if (agendaForm.get("id") == null) {
                    activi = bdActivi.obtenerActividad((Long) agendaForm.get("idActividad"));
                } else {
                    activi = age.getActividad();
                }

                if (agendaForm.get("idActividad") != null && (Long) agendaForm.get("idActividad") != 0) {
                    if (activi == null) {
                        activi = new Actividadagenda();
                    }
                    activi.setId(((Long) agendaForm.get("idActividad")).longValue());
                }
		        age.setActividad(activi);
		        
				List tradform = (List) agendaForm.get("traducciones");
				List langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
				for (int i = 0; i < langs.size(); i++) {
					TraduccionAgenda trad = (TraduccionAgenda)age.getTraduccion("" + langs.get(i));
					TraduccionAgenda agetrad = (TraduccionAgenda) tradform.get(i);
					if (trad != null) {
						agetrad.setDocumento(trad.getDocumento());
						agetrad.setImagen(trad.getImagen());
						tradform.set(i, agetrad);
					}
				}

				if (agendaForm.get("id") == null) {
					VOUtils.populate(age, agendaForm);  // form --> bean
				} else {
		            List<TraduccionAgenda> llista = (List<TraduccionAgenda>)((DynaActionForm) form).get("traducciones");
		    		List<?> langs1 = DelegateUtil.getIdiomaDelegate().listarIdiomas();
		    		
		    		for (int i = 0; i < llista.size(); i++) {
		    			if (age.getTraducciones().containsKey(((Idioma) langs1.get(i)).getLang())) {
		    				age.getTraducciones().get(((Idioma) langs1.get(i)).getLang()).setDescripcion(llista.get(i).getDescripcion());
		    				age.getTraducciones().get(((Idioma) langs1.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());
		    				age.getTraducciones().get(((Idioma) langs1.get(i)).getLang()).setUrl(llista.get(i).getUrl());
		    				age.getTraducciones().get(((Idioma) langs1.get(i)).getLang()).setUrlnom(llista.get(i).getUrlnom());

		    			} else {
		    				TraduccionAgenda traduccio = new TraduccionAgenda();
		    				
		    				TraduccionAgendaPK idt = new TraduccionAgendaPK();
		    				idt.setCodigoAgenda(age.getId());
		    				idt.setCodigoIdioma( ((Idioma)langs.get(i)).getLang());
		    				traduccio.setId(idt);
		    				
		    				traduccio.setDescripcion(llista.get(i).getDescripcion());
		    				traduccio.setTitulo(llista.get(i).getTitulo());
		    				traduccio.setUrl(llista.get(i).getUrl());
		    				traduccio.setUrlnom(llista.get(i).getUrlnom());
		    				age.getTraducciones().put(((Idioma)langs1.get(i)).getLang(), traduccio);
		    			}
		    		}
				}
	           
	   		   	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
	   		   	List lang = idiomaDelegate.listarLenguajes();
	   		   	FormFile[] aux = (FormFile[]) agendaForm.get("ficheros");
	   		   	String[] auxnom = (String[]) agendaForm.get("ficherosnom");
	   		   	boolean[] auxbor = (boolean[]) agendaForm.get("ficherosbor");
	   		   	FormFile[] aux2 = (FormFile[]) agendaForm.get("imagenes");
	           	String[] auxnom2 = (String[]) agendaForm.get("imagenesnom");
	           	boolean[] auxbor2 = (boolean[]) agendaForm.get("imagenesbor");

	        	Archivo[] imagenesTraducidas = (Archivo[]) request.getSession().getAttribute("imagenesTraducidas");
	        	Archivo[] ficherosTraducidos = (Archivo[]) request.getSession().getAttribute("ficherosTraducidos");
	           	
	           	for (int i = 0; i < aux.length; i++) {
	           		TraduccionAgenda  traduccion = (TraduccionAgenda) age.getTraduccion("" + lang.get(i));
		            if (traduccion != null) {
                        if (archivoValido(aux[i])) {
                            traduccion.setDocumento(populateArchivo(traduccion.getDocumento(), aux[i], null, null));
                        } else if (auxbor[i]) {
                            traduccion.setDocumento(null);
                        }else if (ficherosTraducidos != null) {
                            if(ficherosTraducidos[i] != null) {
                                ficherosTraducidos[i].setIdi("" + lang.get(i));
                                traduccion.setDocumento(ficherosTraducidos[i]);
                            }
                        }

                        if (archivoValido(aux2[i])) {
                            traduccion.setImagen(populateArchivo(traduccion.getImagen(), aux2[i], null, null));
                        } else if (auxbor2[i]) {
                            traduccion.setImagen(null);
                        } else if (imagenesTraducidas != null) {
                            if (imagenesTraducidas[i] != null) {
                                imagenesTraducidas[i].setIdi("" + lang.get(i));
                                traduccion.setImagen(imagenesTraducidas[i]);
                            }
                        }

                        if (traduccion.getDocumento() != null) {
                            if (auxnom[i].length() > 0) {
                                traduccion.getDocumento().setNombre(auxnom[i]);
                            }
                        }

                        if (traduccion.getImagen() != null) {
                            if (auxnom2[i].length() > 0) {
                                traduccion.getImagen().setNombre(auxnom2[i]);
                            }
                        }
		            }
		        }
	           	
	            request.getSession().removeAttribute("imagenesTraducidas");
	            request.getSession().removeAttribute("ficherosTraducidos");
	            
		       	bdAgenda.grabarAgenda(age);
		       	
		       	agendaForm.set("id",  age.getId());
		       	agendaForm = setImagDocumentToForm (age, agendaForm);
                
		        
		        // Relleno el combo de Actividades
	            request.setAttribute("actividadesCombo", bdActivi.listarCombo(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
	       			       	
		       	if (isNuevaActividad) {
                    addMessageWithDate(request, "mensa.nouesdeveniment");
                } else {
                    addMessageWithDate(request, "mensa.modifesdeveniment");
                }

		       	return mapping.findForward("detalle");

            /********************** TRADUCIR ************************/
            } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir"))) {

                // Relleno el combo de Actividades
                ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
                request.setAttribute("actividadesCombo", bdActivi.listarCombo(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
                agendaForm = traducir(request, agendaForm);

                return mapping.findForward("detalle");

            /********************** BORRAR ************************/
            } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.borrar"))) {

                Long id = (Long) agendaForm.get("id");
                bdAgenda.borrarAgenda(id);
                agendaForm.resetForm(mapping, request);

                ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
                request.setAttribute("actividadesCombo", bdActivi.listarCombo(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
                addMessageAlert(request, "agenda.borrar.sinid");

                return mapping.findForward("detalle");

            /********************** CREAR ************************/
            } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.crear"))) {

                // Relleno el combo de Actividades
                ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
                request.setAttribute("actividadesCombo", bdActivi.listarCombo(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
                agendaForm.resetForm(mapping, request);
                request.setAttribute("agendaForm",agendaForm);
                return mapping.findForward("detalle");
            }
        }

        /********************** EDITA *************************/
        if (request.getParameter("id") != null) {
            Long id = new Long("" + request.getParameter("id"));

            // Si el contenido tiene errores de accesibilidad,cargamos una lista con los mismos para recorrerla en el jsp
            AccesibilidadDelegate accDel = DelegateUtil.getAccesibilidadDelegate();
            IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
            Iterator it = ididel.listarLenguajes().iterator();
            int x = 0;
            while (it.hasNext()) {
                String idi = (String) it.next();
                Accesibilidad acce = accDel.obtenerAccesibilidad(Catalogo.SRVC_MICRO_EVENTOS, id, idi);
                if (acce != null) {
                    request.setAttribute("MVS_w3c_"+x,acce);
                }
                x++;
            }

            age = bdAgenda.obtenerAgenda(id);
            //************COMPROBACION DE IDES*************
            if (age.getIdmicrosite().longValue() != ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().longValue()) {
                addMessageError(request, "peticion.error");
                return mapping.findForward("info");
            }
            //*********************************************

            agendaForm fdet = (agendaForm) form;

            fdet.setFinicio(age.getFinicio());
            fdet.setFfin(age.getFfin());
            fdet.set("visible", age.getVisible());
            fdet.set("organizador", age.getOrganizador());
            if (age.getActividad() != null && age.getActividad().getId() != null) {
                fdet.set("idActividad", age.getActividad().getId());
            }
            VOUtils.describe(fdet, age);  // bean --> form

            // Relleno el combo de Actividades
            ActividadDelegate bdActividad = DelegateUtil.getActividadagendaDelegate();
            request.setAttribute("actividadesCombo", bdActividad.listarCombo(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
            fdet = setImagDocumentToForm(age, fdet);

            return mapping.findForward("detalle");
        }

        addMessageError(request, "peticion.error");
        return mapping.findForward("info");
    }
    
    /**
     * Método que traduce un formulario de Agenda
     * @author Indra
     * @param request			petición de usuario
     * @param agendaForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private agendaForm traducir (HttpServletRequest request, agendaForm agendaForm) throws TraductorException  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getServletContext().getAttribute("traductor");
    		String idiomaOrigen = "ca";

    		TraduccionAgenda agendaOrigen = (TraduccionAgenda) agendaForm.get("traducciones", 0);
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");

            Iterator<?> itTradFichas = ((ArrayList<?>) agendaForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
            
            while (itLang.hasNext()){

            	String idiomaDesti = itLang.next();
            	TraduccionAgenda agendaDesti = (TraduccionAgenda) itTradFichas.next();
			   	if (agendaDesti == null) {
			   		micrositeBean.setTraduccion(idiomaDesti, new TraduccionAgenda());
			   		agendaDesti = (TraduccionAgenda) micrositeBean.getTraduccion(idiomaDesti);
			   	}
            	
            	//Comprobamos que el idioma Destino está configurado en el Microsite si no está no se traduce
            	if (idiomasMicro.contains(idiomaDesti)) {
	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(agendaOrigen, agendaDesti)) {
	            			request.setAttribute("mensajes", "traduccioCorrecte");
	            		}
	            		else {
	            			request.setAttribute("mensajes", "traduccioIncorrecte");
	            			break;
	            		}
	            	}
            	}
            }
            
			if (request.getAttribute("mensajes").equals("traduccioCorrecte")) addMessage(request, "mensa.traduccion.confirmacion");
			 else addMessageError(request, "mensa.traduccion.error");

			try{
				agendaForm = traducirArchivos(request, agendaForm, micrositeBean,traductor.getListLang());
			}catch(Exception e){
				addMessageError(request, "mensa.traduccion.archivos.error");
			}
			
			log.info("Traducción Agenda - Id: " + (Long) agendaForm.get("id"));
			return agendaForm;
    }    
    
    /**
     * Método para cargar los documentos y imagenes en el formulario 
     * @author Indra
     * @param agenda			Agenda
     * @param agendaForm		formulario
     * @throws Exception
     */
    private agendaForm setImagDocumentToForm (Agenda agenda, agendaForm agendaForm) throws Exception  {
    	
	        IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
	        List<?> lang = idiomaDelegate.listarLenguajes();
	        String[] auxnom = new String[lang.size()];
	        Long[] auxid =  new Long[lang.size()];
	        String[] auxnom2 = new String[lang.size()];
	        Long[] auxid2 =  new Long[lang.size()];
	        for (int i=0;i<lang.size();i++) {
	            TraduccionAgenda  traduccion = (TraduccionAgenda) agenda.getTraduccion(""+lang.get(i));
	            if (traduccion!=null) {
	            	if (traduccion.getDocumento()!=null) {
	            		auxnom[i]=traduccion.getDocumento().getNombre();
	            		auxid[i]=traduccion.getDocumento().getId();
	            	}
	            	if (traduccion.getImagen()!=null) {
	            		auxnom2[i]=traduccion.getImagen().getNombre();
	            		auxid2[i]=traduccion.getImagen().getId();
	            	}
	            }
	        }
	        agendaForm.set("ficherosnom",auxnom);
	        agendaForm.set("ficherosid",auxid);
	        agendaForm.set("imagenesnom",auxnom2);
	        agendaForm.set("imagenesid",auxid2);
	        
    	return agendaForm;
    }
    /**
     * Método que traduce los archivos de un formulario
     * @author Indra
     * @param request			petición de usuario
     * @param agendaForm		formulario dinámico enviado por usuario
     * @param micrositeBean     Microsite
     * @param lang              Idiomas configurados en la aplicación
     * @throws Exception
     */
    private agendaForm traducirArchivos(HttpServletRequest request, agendaForm agendaForm,Microsite micrositeBean, List<?> lang) throws Exception  {
    	
    	/** Recuperar Informacion información **/
    	FormFile[] ficheros =  (FormFile[]) agendaForm.get("ficheros");
    	String[] ficherosNoms =  (String[]) agendaForm.get("ficherosnom");
        Long[] ficherosIds = (Long[])agendaForm.get("ficherosid");
		boolean[] ficherosBors = (boolean[]) agendaForm.get("ficherosbor");
		FormFile[] imagenes =  (FormFile[]) agendaForm.get("imagenes");
       	String[] imagenesNoms =  (String[]) agendaForm.get("imagenesnom");
       	boolean[] imagenesBors = (boolean[]) agendaForm.get("imagenesbor");
        Long[] imagenesIds = (Long[])agendaForm.get("imagenesid");
        
       //si no tiene ficheros adjuntos que traducir sale del metodo
       if (imagenesIds[0] == null &&  ficherosIds[0] == null ){
    	   if((imagenes[0] == null && ficheros[0] == null)) 
    		   return agendaForm;
    	   else if((imagenes[0].getFileName().equals("")) && (ficheros[0].getFileName().equals(""))){
    		   return agendaForm;   
    	   }
       }
       
       try{
	        ArchivoDelegate bdArchivos = DelegateUtil.getArchivoDelegate();
	    	/** Tratar Imagen **/
	        Archivo[] archivoImagenResult = new Archivo[lang.size()];
	       
	        if (imagenesIds[0] != null || (imagenes[0]!= null && !imagenes[0].getFileName().equals(""))){
			            Archivo imagenDefault = null;
			            if (!(""+ imagenesIds[0]).equals("null") ){
			            	imagenDefault= bdArchivos.obtenerArchivo(imagenesIds[0]);	
			            }else{
			            	imagenDefault= populateArchivo(null, imagenes[0], null, null);
			            	archivoImagenResult[0] = imagenDefault;
			            }
			        	
				        for (int i=0;i<imagenes.length;i++) {
				        	if(micrositeBean.getIdiomas().contains(lang.get(i))){
					        	if (i== 0 && (""+imagenesNoms[i]).equals("")){
					        		imagenesNoms[i] = imagenDefault.getNombre();
					        	}
					        	
					        	if (imagenesNoms[i] != null && i >0){
					        		
					        		if (imagenesNoms[i].equals("")){
					        			if (imagenesBors[i] == false){
					        				imagenes [i] = imagenes[0];
						   		   			imagenesNoms [i] =  imagenesNoms[0];
						   		   			imagenesIds [i] =  imagenesIds[0];
						   		   			imagenesBors [i] =  imagenesBors[0];
						   		   			
									archivoImagenResult[i] = crearNuevoArchivo(imagenDefault, null, null);
					        			}
					        		}
					        	}
				        	}
				        }
	        }
	        
	    	/** Tratar Fichero **/
	        Archivo[] archivoFicheroResult = new Archivo[lang.size()];
	      
	        if (ficherosIds[0]!= null  || (ficheros[0]!= null && !ficheros[0].getFileName().equals(""))){
	
		        	Archivo ficheroDefault = null;
		            if (!(""+ ficherosIds[0]).equals("null") ){
		            	ficheroDefault= bdArchivos.obtenerArchivo(ficherosIds[0]);  	
		            }else{
		            	ficheroDefault= populateArchivo(null, ficheros[0], null, null);
		            	archivoFicheroResult[0] = ficheroDefault;
		            }
	
			        for (int i=0;i<ficheros.length;i++) {
			        	if(micrositeBean.getIdiomas().contains(lang.get(i))){
				        	if (i== 0 && (""+ficherosNoms[i]).equals("")){
				        		ficherosNoms[i] = ficheroDefault.getNombre();
				        	}
				        	if (ficherosNoms[i] != null && i >0){
				        		if (ficherosNoms[i].equals("")){
				        			if (ficherosBors[i] == false){
					   		   			ficheros [i] = ficheros[0];
					   		   			ficherosNoms [i] =  ficherosNoms[0];
					   		   			ficherosIds [i] =  ficherosIds[0];
					   		   			ficherosBors [i] =  ficherosBors[0];
					   		   			
					   		   			archivoFicheroResult [i] = crearNuevoArchivo(ficheroDefault,null,null); 
				        			}
				        		}
				        	}
			        	}
			        }
	
	        }
	        
	
	        /** almacenar información **/
	        request.getSession().setAttribute("imagenesTraducidas",archivoImagenResult);
	        request.getSession().setAttribute("ficherosTraducidos",archivoFicheroResult);
        
       }catch(Exception e){
    	   log.error("No se han podido traducir los ficheros del evento"); 
    	  
    	   throw new Exception(e);
       }
        agendaForm.set("ficheros",ficheros);
		agendaForm.set("ficherosnom", ficherosNoms);
        agendaForm.set("ficherosid", ficherosIds);
		agendaForm.set("ficherosbor",ficherosBors);
		agendaForm.set("imagenes",imagenes);
        agendaForm.set("imagenesnom", imagenesNoms);
        agendaForm.set("imagenesbor", imagenesBors);
        agendaForm.set("imagenesid", imagenesIds);
      

    	return agendaForm;
    }
   
    
}

