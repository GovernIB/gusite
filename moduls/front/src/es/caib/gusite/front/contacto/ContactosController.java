package es.caib.gusite.front.contacto;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thymeleaf.util.StringUtils;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.Pardato;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.front.service.ContactosDataService;
import es.caib.gusite.front.service.CorreoEngineService;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionLineadatocontacto;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * 
 * @author brujula-at4
 *
 */
@Controller
public class ContactosController extends BaseController {
	
	private static Log log = LogFactory.getLog(ContactosController.class);

	@Autowired
    protected ContactosDataService contactosDataService;
	

	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/contact/") 
	public String listarcontactos (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="filtro", required = false, defaultValue="") String filtro,
					@RequestParam(value="pagina", required = false, defaultValue="1") int pagina,
					@RequestParam(value="ordenacion", required = false, defaultValue="") String ordenacion,					
					HttpServletRequest req) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			BaseCriteria criteria = new BaseCriteria(filtro, pagina, ordenacion);
			ResultadoBusqueda<Contacto> formularios = this.contactosDataService.getListadoFormularios(microsite, lang, criteria);

	    	if (formularios.getTotalNumRecords() == 1) {
	    		//solo hay uno.... incluimos el formulario
	    		return "forward:" + this.urlFactory.contacto(microsite, lang, formularios.getResultados().iterator().next());
	    	} 
	    	

    		List<Pardato> listaNombreContactos = new ArrayList<Pardato>();
	        for (Contacto contacto : formularios.getResultados()) {
	        	 Pardato pardato = new Pardato();
	        	 pardato.setKey(contacto.getTitulocontacto(lang.getLang().toLowerCase()));
	        	 pardato.setValue( this.urlFactory.contacto(microsite, lang, contacto) );
	        	 listaNombreContactos.add(pardato);
	        }
    		
    		model.addAttribute("MVS_seulet_sin", this.urlFactory.listarContactosSinPagina(microsite, lang, criteria));
    		model.addAttribute("MVS_parametros_pagina",formularios.getParametros());
    		model.addAttribute("MVS_listado", listaNombreContactos);

		    cargarMollapan(microsite, model, lang);

		    return this.templateNameFactory.listarContactos(microsite);

	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}      

	}

	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/contacto/") 
	public String listarcontactosEs (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="filtro", required = false, defaultValue="") String filtro,
					@RequestParam(value="pagina", required = false, defaultValue="1") int pagina,
					@RequestParam(value="ordenacion", required = false, defaultValue="") String ordenacion,					
					HttpServletRequest req) {
		
		return this.listarcontactos(siteId, new Idioma(LANG_ES), model, mcont, pcampa, filtro, pagina, ordenacion, req);
	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/contacte/") 
	public String listarcontactosCa (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="filtro", required = false, defaultValue="") String filtro,
					@RequestParam(value="pagina", required = false, defaultValue="1") int pagina,
					@RequestParam(value="ordenacion", required = false, defaultValue="") String ordenacion,					
					HttpServletRequest req) {
		
		return this.listarcontactos(siteId, new Idioma(LANG_CA), model, mcont, pcampa, filtro, pagina, ordenacion, req);
	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/contact/") 
	public String listarcontactosEn (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="filtro", required = false, defaultValue="") String filtro,
					@RequestParam(value="pagina", required = false, defaultValue="1") int pagina,
					@RequestParam(value="ordenacion", required = false, defaultValue="") String ordenacion,					
					HttpServletRequest req) {
		
		return this.listarcontactos(siteId, new Idioma(LANG_EN), model, mcont, pcampa, filtro, pagina, ordenacion, req);
	}
	
	
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/{lang}/contact/{contacto}/")
	public String contacto (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("contacto") long idContacto,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					HttpServletRequest req) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Contacto contacto = this.contactosDataService.getFormulario(microsite, lang, idContacto);

			//comprobacion de microsite
			if (contacto.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
					log.error("El elemento solicitado no pertenece al site");
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			//comprobacion de visibilidad
			if (!contacto.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}			
    		
		    model.addAttribute("MVS_contacto", contacto);
		    model.addAttribute("MVS_contacto_titulo", contacto.getTitulocontacto(lang.getLang()));
		    model.addAttribute("MVS_contacto_listatags", montaListaTags(microsite, lang, contacto));
		    model.addAttribute("cont", contacto.getId());
		    model.addAttribute("idsite", contacto.getIdmicrosite());

		    cargarMollapan(microsite, model, lang, contacto);

		    return this.templateNameFactory.contacto(microsite);

	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}      

	}


	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="{mkey}/{lang}/contact/{contacto}/") 
	public String enviarContacto (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("contacto") long idContacto,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="docAnex", required=false) CommonsMultipartFile docAnexFileData,					
					MultipartHttpServletRequest req) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Contacto contacto = this.contactosDataService.getFormulario(microsite, lang, idContacto);

			//comprobacion de microsite
			if (contacto.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
					log.error("El elemento solicitado no pertenece al site");
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			//comprobacion de visibilidad
			if (!contacto.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}			
    		
		    model.addAttribute("idsite", contacto.getIdmicrosite());
		    
		    String mensaje = this.procesaFormulario(contacto, lang, req);
		    
		    if (docAnexFileData==null) {
			    this.enviarFormulario(contacto, lang, mensaje, null, null);
		    } else {
			    this.enviarFormulario(contacto, lang, mensaje, docAnexFileData.getInputStream(), docAnexFileData.getOriginalFilename());
		    }

		    cargarMollapan(microsite, model, lang, contacto);

		    return this.templateNameFactory.envioContacto(microsite);

	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (IOException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}      

	}
	
	
	/**
	 * Método privado que se encarga de enviar via correo electrónico la información del formulario
	 * @throws ExceptionFrontPagina 
	 * @throws Exception
	 */
	private void enviarFormulario(Contacto contacto, Idioma lang, String mensaje, InputStream docAnex, String fileName) throws ExceptionFrontPagina {
		
		java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
        String mensaje_asunto = contacto.getTitulocontacto(lang.getLang().toLowerCase());
        StringBuffer mensaje_cuerpo = new StringBuffer("");
       
        mensaje_cuerpo.append(mensaje);
        mensaje_cuerpo.append(fecha.getTime().toString());        

        CorreoEngineService correo = new CorreoEngineService();
       if(docAnex != null) {   
	       correo.setFile(docAnex, fileName);
       }
        correo.initCorreo(contacto.getEmail(), mensaje_asunto , false, mensaje_cuerpo);
        if (!correo.enviarCorreo()) {
        	throw new ExceptionFrontPagina("Problema enviando correo");
        }
	}
	
	
	/**
	 * Recorreremos el formulario y lo iremos empaquetando en un string que será el 
	 * cuerpo del mensaje a enviar por correo. 
	 *
	 */
	private String procesaFormulario(Contacto contacto, Idioma lang, HttpServletRequest req) {
			
		    StringBuilder cuerpoMensaje = new StringBuilder();
		    cuerpoMensaje.append("Idioma = ").append(lang.getLang()).append("\n");
		     	
		    for (Lineadatocontacto linea : contacto.getLineasdatocontacto()) {

		    	if (linea.getTipo().equals(Contacto.RTYPE_TITULO)) {
		    		continue; //el tipo n
		    	}
		    	String paramName = ((linea.getObligatorio()==1)?Microfront.VCAMPO_REQUERIDO:"") + linea.getId().toString();
		    	String campovalor = ((TraduccionLineadatocontacto)linea.getTraduccion(lang.getLang())).getTexto() + " = ";
		    	String[] paramValues = req.getParameterValues(paramName);
		    	if (paramValues.length == 1) {
					  String paramValue = paramValues[0];
					  if (paramValue.length() == 0) {
						  campovalor+="[sin valor]"+ "\n";
					  } else {
						  campovalor+= paramValue + "\n";
					  }
			    } else  {
					  for (int i=0; i < paramValues.length; i++) {
						  campovalor+= paramValues[i] + ", ";
					  }
					  campovalor+="\n";
					  
				}
		    	cuerpoMensaje.append(campovalor);
		    }
		    return cuerpoMensaje.toString();
	}    
		


	/**
	 * Prepara un arraylist que contiene el bean pardato.
	 * Prepara una hashtable que contiene el bean pardato.
	 * En el bean se mete:
	 * en el key: el texto o titulo
	 * en el value: el tag html del elemento del formulario
	 * 
	 * La clave en el hash será el id de lineadatocontacto.
	 * @param lang 
	 * @return 
	 *
	 */
	private List<Pardato> montaListaTags(Microsite microsite, Idioma lang, Contacto contacto) {

		List<Pardato> listalineas = new ArrayList<Pardato>(); 
		
	    for (Lineadatocontacto ld : contacto.getLineasdatocontacto()) {
	        	 Pardato pardato = new Pardato();
	        	 MParserHTML parserhtml = new MParserHTML(microsite.getRestringido());
	        	 if ((ld.getTipo().equals(Contacto.RTYPE_TEXTAREA)) || (ld.getTipo().equals(Contacto.RTYPE_TEXTO))) {
		        	 pardato.setKey(((TraduccionLineadatocontacto)ld.getTraduccion(lang.getLang())).getTexto()) ;
		        	 if (ld.getLineas()==0) 
		        		 pardato.setValue(parserhtml.getTagText(
				        				 	ld.getId().toString(),
				        				 	ld.getTamano(),
				        			 		ld.getObligatorio()).toString());
		        	 else
		        		 pardato.setValue(parserhtml.getTagTextarea(
				        				 	ld.getId().toString(),
				        				 	50,
				        				 	ld.getLineas(),
				        			 		ld.getObligatorio()).toString());
	        	 }
	        	 if  ((ld.getTipo().equals(Contacto.RTYPE_SELECTORMULTIPLE)) ||  (ld.getTipo().equals(Contacto.RTYPE_SELECTOR))) {
		        	 pardato.setKey(getNombreinselect(((TraduccionLineadatocontacto)ld.getTraduccion(lang.getLang())).getTexto()));
		        	 pardato.setValue(parserhtml.getTagSelect(
		        			 		ld.getId().toString(),
		        			 		getListaopciones(((TraduccionLineadatocontacto)ld.getTraduccion(lang.getLang())).getTexto()),
		        			 		ld.getTipo(),
		        			 		ld.getLineas(),
		        			 		ld.getObligatorio()).toString());
		        	 
	        	 }
	        	 if (!ld.getTipo().equals(Contacto.RTYPE_TITULO)) {
		        	 listalineas.add(pardato);
	        	 }
	        }
	        return listalineas; 
		}


    /**
     * devuelve la primera cadena de texto de un string utilizando como
     * separador Microfront.separatorwords
     * @param cadena
     * @return
     */

    
    private String getNombreinselect(String cadena) {
    	String retorno="";
    	if (cadena.length()>0) {
	        String txseparador = "" + Microfront.separatorwordsform;
	        String[] listastringcadenas = cadena.split(txseparador);
	        if(listastringcadenas.length==0){
	        }
	        else{
	        	retorno=listastringcadenas[0];
	        }
	        
	    }
    	return retorno;
    }
	
    /**
     * Devuelve un arraylist que contiene strings.
     * En la lista se introducen todos menos el primero.
     * Se utiliza como separador del string Microfront.separatorwords
     * @param cadena
     * @return
     */
    private ArrayList<String> getListaopciones(String cadena) {
    	ArrayList<String> lista=new ArrayList<String>();
	    if (cadena.length()>0) {
	        String txseparador = "" + Microfront.separatorwordsform;
	        String[] listastringcadenas = cadena.split(txseparador);
	        for (int i=1;i<listastringcadenas.length;i++)
	          if (listastringcadenas[i].length()>0) lista.add(listastringcadenas[i]);
	    }
	    return lista;
    }
    
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/contacto/{contacto}/") 
	public String contactoEs (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("contacto") long idContacto,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					HttpServletRequest req) {
		
		return this.contacto(siteId, new Idioma(LANG_ES), idContacto, model, mcont, pcampa, req);
	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/contacte/{contacto}/") 
	public String contactoCa (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("contacto") long idContacto,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					HttpServletRequest req) {
		
		return this.contacto(siteId, new Idioma(LANG_CA), idContacto, model, mcont, pcampa, req);
	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/contact/{contacto}/") 
	public String contactoEn (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("contacto") long idContacto,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					HttpServletRequest req) {
		
		return this.contacto(siteId, new Idioma(LANG_EN), idContacto, model, mcont, pcampa, req);
	}
	
	
	
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return 
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Model model, Idioma lang) {
		
		List<PathItem> path = super.getBasePath(microsite, model, lang);

		path.add(new PathItem(getMessage("listarcontactos.frmcontacto", lang), this.urlFactory.listarContactos(microsite, lang)));
		
	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	    
	    return path;
		
	}	
	
	private void cargarMollapan(Microsite microsite, Model model, Idioma lang,Contacto contacto) {
		
		List<PathItem> path = this.cargarMollapan(microsite, model, lang);
		
		/* original:
		path.add(new PathItem(getMessage("contacto.formulario", lang), this.urlFactory.contacto(microsite, lang, contacto)));
		
		Mejor usar el título del formulario:
		*/
		String titulo = contacto.getTitulocontacto(lang.getLang());
		if (StringUtils.isEmpty(titulo)) {
			titulo = getMessage("contacto.formulario", lang);
		}
		path.add(new PathItem(titulo, this.urlFactory.contacto(microsite, lang, contacto)));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
		
	}
	
	
	
	
	@Override
	public String setServicio() {
		
		return Microfront.RCONTACTO;
	}

	
	
	
}
