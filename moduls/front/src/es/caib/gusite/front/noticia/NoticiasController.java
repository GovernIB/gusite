package es.caib.gusite.front.noticia;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * 
 * @author brujula-at4
 *
 */
@Controller
public class NoticiasController extends BaseController {
	
	private static Log log = LogFactory.getLog(NoticiasController.class);

	@Autowired
    protected NoticiasDataService noticiasDataService;
	
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/l/{tipo}") 
	public String listarnoticias (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("tipo") long idTipo,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="filtro", required = false, defaultValue="") String filtro,
					@RequestParam(value="pagina", required = false, defaultValue="1") int pagina,
					@RequestParam(value="ordenacion", required = false, defaultValue="") String ordenacion,
					HttpServletRequest req) {
		
		return this.listarnoticias(siteId, lang, idTipo, model, new NoticiaCriteria(filtro, pagina, ordenacion), mcont, pcampa, req);
		
  	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/l/{tipo}/{anyo}") 
	public String listarnoticias (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("tipo") long idTipo,
					@PathVariable("anyo") int anyo,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="filtro", required = false, defaultValue="") String filtro,
					@RequestParam(value="pagina", required = false, defaultValue="1") int pagina,
					@RequestParam(value="ordenacion", required = false, defaultValue="") String ordenacion,
					HttpServletRequest req) {
		
		return this.listarnoticias(siteId, lang, idTipo, model, new NoticiaCriteria(filtro, pagina, ordenacion, anyo), mcont, pcampa, req);
		
  	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	private String listarnoticias (
					SiteId siteId, 
					Idioma lang,
					long idTipo,
					Model model,
					NoticiaCriteria criteria,
					String mcont,
					String pcampa,
					HttpServletRequest req) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Tipo tipo = this.getTipo(idTipo);
			criteria.setTipo(tipo);
			
			String plantillaForward = "";

			if (tipo.getTipoelemento().equals(Tipo.TIPO_CONEXIO_EXTERNA)) {

		    	model.addAttribute("MVS_mchtml", this.noticiasDataService.cargaExterno(tipo, req));
		    	plantillaForward = this.templateNameFactory.listarNoticiasExternas(microsite);
			    
			} else {
				ResultadoNoticias<Noticia> noticias = null;
				List<String> listaAnyos = null;
				if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_NORMAL)) {
					
					//Paginación normal
					criteria.setAnyo(0);
					
				} else {
					//Paginación anual
					listaAnyos = this.noticiasDataService.obtenerListaAnyos(microsite, lang, tipo);
		
					if (criteria.getAnyo() == 0) {
						if (listaAnyos != null && listaAnyos.size()>0){
							
							if(listaAnyos.get(0)=="Tots"){
			
//								criteria.setAnyo(-1);
								listaAnyos.remove(0);
								listaAnyos.add(0, "-1");
								criteria.setAnyo(Integer.parseInt(listaAnyos.get(0))); //coger el primero
							}
							else{
								criteria.setAnyo(Integer.parseInt(listaAnyos.get(0))); //coger el primero
							}
								
//							criteria.setAnyo(Integer.parseInt(listaAnyos.get(0))); //coger el primero
						}
						else {
							criteria.setAnyo(Fechas.obtenerAnyo(new Date())); //obtener el del año en curso
						}
					}
				}

				noticias = this.noticiasDataService.listarNoticias(microsite, lang, criteria );


				if (noticias.isError()) {

					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
					
				} else {
			  		
					model.addAttribute("MVS_seulet_sin", this.urlFactory.listarNoticiasSinPagina(microsite, lang, tipo, criteria, mcont, pcampa));
			    	model.addAttribute("MVS_parametros_pagina",noticias.getParametros());
			    	model.addAttribute("MVS_listado", noticias.getResultados());
			    	
		        	String desctiponoticia = ((TraduccionTipo)tipo.getTraduccion(lang.getLang())).getNombre();
			    	model.addAttribute("MVS_tipolistado", desctiponoticia);
			    	
			    	model.addAttribute("MVS_claseelemento", tipo);
			    	model.addAttribute("MVS_busqueda",""+noticias.isBusqueda());
			    	
					if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
					    model.addAttribute("MVS_listadoanyos", listaAnyos);
					    model.addAttribute("MVS_anyo", criteria.getAnyo());
					    
					}

				    if (tipo.getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {

						if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory.listarNoticiasAnyos(microsite);
				    	} else {
				    		plantillaForward = this.templateNameFactory.listarNoticias(microsite);
				    	}

				    }
				    
				    if (tipo.getTipoelemento().equals(Microfront.ELEM_LINK)) {
						if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
				    		plantillaForward = this.templateNameFactory.listarLinksAnyos(microsite);
				    	} else {
				    		plantillaForward = this.templateNameFactory.listarLinks(microsite);
				    	}
				    }
				    
				    if (tipo.getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
						if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory.listarDocumentosAnyos(microsite);
				    	} else {
				    		plantillaForward = this.templateNameFactory.listarDocumentos(microsite);
				    	}
				    }
				    
				    if (tipo.getTipoelemento().equals(Microfront.ELEM_FOTO)) {
			    		String[] tamanyo = calcularTamanyoFoto(tipo.getFotosporfila());
			    		model.addAttribute("MVS_anchoFoto",tamanyo[0]);
			    		model.addAttribute("MVS_altoFoto",tamanyo[1]);

						if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory.mostrarGaleriaFotosAnyos(microsite);
				    	}
				    	else {
				    		plantillaForward = this.templateNameFactory.mostrarGaleriaFotos(microsite);
				    	}
			    		
					}	
								    
				    //TODO: averiguar para qué sirve mcont. Creo que para la opción de menú que hay que resaltar (confirmar)
					if ( !StringUtils.isEmpty(mcont)  ){
						model.addAttribute("MVS_menu_cont_notic", mcont);
					}
				   
				}				
				
			}
		  	
		    cargarMollapan(microsite, tipo, model, lang);

		    return plantillaForward;
	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}      

	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return 
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Tipo tipo, Model model, Idioma lang) {
		
		List<PathItem> path = super.getBasePath(microsite, model, lang);

    	String desctiponoticia = ((TraduccionTipo)tipo.getTraduccion(lang.getLang())).getNombre();
		if (!StringUtils.isEmpty(desctiponoticia)) {
			path.add(new PathItem(desctiponoticia, this.urlFactory.listarNoticias(microsite, lang, tipo)));
		}
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	    
	    return path;
		
	}	
	
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return 
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Noticia noticia, Model model, Idioma lang) {
		
		List<PathItem> path = cargarMollapan(microsite, noticia.getTipo(), model, lang);
		
		//TODO: añadir el título de la noticia?
		path.add(new PathItem(this.getMessage("noticia.detalle", lang), this.urlFactory.noticia(microsite, lang, noticia)));
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	    
	    return path;
		
	}	
	
	private Tipo getTipo(long idTipo) throws ExceptionFrontPagina {
		TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
		try {
			Tipo tipo = tipodel.obtenerTipo( idTipo );
			if (tipo == null) {
				throw new ExceptionFrontPagina("Tipo no encontrado: " + idTipo);
			}
			return tipo;
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}


	/**
	 * TODO: investigar por qué esto da un "ambiguous..."
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	@RequestMapping("{mkey}/{lang}/l/{tipo}") 
	public String listarnoticias (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("tipo") TipoNoticiaId tipo,
					Model model,
					//@ModelAttribute NoticiaCriteria criteria,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		
	  	try {
	  		
	  		//TODO: completar cuando se tengan nemotécnicos en los tipos de noticia
	  		//		se obtendrá la clave para el nemotècnico
	  		int idTipo = Integer.parseInt(tipo.nemotecnic);
	  		return listarnoticias(siteId, lang, idTipo, model, pcampa);
	  		
	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	// TODO:
        	//return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));
        	
        	return "errorgenerico";
        }      

	}
	 */

	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	@RequestMapping("{mkey}/l/{tipo}") 
	public String listarnoticias (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("tipo") TipoNoticiaId tipo,
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		
	  	try {
	  		
	  		return listarnoticias(siteId, new Idioma(DEFAULT_IDIOMA), tipo, model, pcampa);
	  		
	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	// TODO:
        	//return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));
        	
        	return "errorgenerico";
        }      

	}
	 */

	
	private String[] calcularTamanyoFoto(int numFotosFila) {
		int ancho=0, alto=0;
		
		if(0!=numFotosFila) {
			ancho = 100 / numFotosFila;  //max = 100%
			alto = 300 /  numFotosFila;  //max = 300px
		}
		return new String[] {Integer.toString(ancho), Integer.toString(alto)}; 
	}


	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: noticia debería ser el nemotecnico de la noticia
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/n/{noticia}") 
	public String noticia (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("noticia") long idNoticia,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Noticia noticia = this.noticiasDataService.loadNoticia(idNoticia, lang);

			//comprobacion de microsite
			if (noticia.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
					log.error("El elemento solicitado no pertenece al site");
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			//comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}			

			//o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(), noticia.getFcaducidad())) {
				log.error("El contenido solicitado está caducado");
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			
		    model.addAttribute("MVS_noticia", noticia);
		    if (noticia.getImagen()!= null){
		    	
		    	//TODO: this is weird
		    	if (noticia.getImagen().getPeso()> 100*1024) {
		    		model.addAttribute("MVS_anchoImg", "460");
		    	} else {
			    	model.addAttribute("MVS_anchoImg", "");
		    	}
		    }	

		    model.addAttribute("MVS_tiponoticia", ((TraduccionTipo)noticia.getTipo().getTraduccion(lang.getLang())).getNombre());

		    //solo quiero que añada el atributo en el caso de cargar noticia del listarnoticias.jsp
	        if ( (mcont != null) && (!mcont.equals(""))  ){
	    	      model.addAttribute("MVS_menu_cont_notic",  mcont);
	        }
	
		    cargarMollapan(microsite, noticia, model, lang);

		    return this.templateNameFactory.mostrarNoticia(microsite);
		    
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
	 * TODO: documento debería ser el nemotecnico de la noticia
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/d/{documento}") 
	public String documento (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("documento") long idDocumento,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Noticia noticia = this.noticiasDataService.loadNoticia(idDocumento, lang);

			//comprobacion de microsite
			if (noticia.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
					log.error("El elemento solicitado no pertenece al site");
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			//comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}			

			//o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(), noticia.getFcaducidad())) {
				log.error("El contenido solicitado está caducado");
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			
			Long iddocumento = ((TraduccionNoticia)noticia.getTraduccion(lang.getLang())).getDocu().getId();
			return "forward:" + this.urlFactory.archivopubById(microsite, iddocumento);
		    
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}      
		
		
  	}
	
	
	
	@Override
	public String setServicio() {
		
		return Microfront.RNOTICIA;
	}

	
	
	
}
