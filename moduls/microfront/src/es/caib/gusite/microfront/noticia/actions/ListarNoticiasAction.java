package es.caib.gusite.microfront.noticia.actions;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.microfront.noticia.util.Bdlistanoticias;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 * 
 * TODO esta clase necesita mucha refactorizacion!
 * 
 *  Action para listar Noticias <P>
 *  Definición Struts:<BR>
 *  action path="/noticias" <BR> 
 *  unknown="false" <BR>
 *  forward name="listarnoticiasv4" path="/v4/noticia/listarnoticias.jsp"<BR>
 * 	forward name="listarnoticiasanyosv4" path="/v4/noticia/listarnoticiasanual.jsp" <BR>
 *	forward name="listarlinksnormalv4" path="/v4/noticia/listarlinksnormal.jsp"<BR>
 *	forward name="listarlinksanyosv4" path="/v4/noticia/listarlinksanual.jsp"   <BR>    
 *	forward name="listardocumentosnormalv4" path="/v4/noticia/listardocumentosnormal.jsp" <BR>
 *	forward name="listardocumentosanyosv4" path="/v4/noticia/listardocumentosanual.jsp" <BR> 
 *	forward name="listarnoticiasexternasv4" path="/v4/noticia/listarnoticiasexternas.jsp"   <BR> 
 *	forward name="listarnoticiasv1" path="/v1/noticia/listarnoticias.jsp"<BR>
 *	forward name="listarnoticiasanyosv1" path="/v1/noticia/listarnoticiasanual.jsp"  <BR>  
 *	forward name="listarlinksnormalv1" path="/v1/noticia/listarlinksnormal.jsp"<BR>
 *	forward name="listarlinksanyosv1" path="/v1/noticia/listarlinksanual.jsp"  <BR>     
 *	forward name="listardocumentosnormalv1" path="/v1/noticia/listardocumentosnormal.jsp"<BR>
 *	forward name="listardocumentosanyosv1" path="/v1/noticia/listardocumentosanual.jsp"<BR>      
 *	forward name="listarnoticiasexternasv1" path="/v1/noticia/listarnoticiasexternas.jsp"
 *  
 *  @author - Indra
 */	
public class ListarNoticiasAction extends BaseAction  {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//refactor! con este constructor hace muchas cosas
		Microsite microsite = buildMicrosite(); 
	    
	    String forwardlocal="listarnoticias";
	
	  	try {	 
	  		Bdlistanoticias bdlistanoticias = cargarListadoDeNoticias(form, request);
	    
		  	if (!bdlistanoticias.isError()) {
		  		
		    	request.setAttribute("MVS_seulet_sin",bdlistanoticias.getUrl_sinpagina());
			    request.setAttribute("MVS_parametros_pagina",bdlistanoticias.getParametros());
			    
			    //#49 Traducir listado
			    traduceNoticia(bdlistanoticias.getListanoticias(),bdlistanoticias.getIdioma());
			    
			    request.setAttribute("MVS_listado", bdlistanoticias.getListanoticias());
			    request.setAttribute("MVS_tipolistado", bdlistanoticias.getDesctiponoticia());
			    request.setAttribute("MVS_claseelemento", bdlistanoticias.getClaseelemento());
			    request.setAttribute("MVS_busqueda",""+bdlistanoticias.isBusqueda());
			    if(isPaginacioAnual(bdlistanoticias)) {
				    request.setAttribute("MVS_listadoanyos", bdlistanoticias.getListaanyos());
				    request.setAttribute("MVS_anyo", bdlistanoticias.getAnyo());
			    }
			    
			    if (bdlistanoticias.getClaseelemento().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
			    	if (isPaginacioAnual(bdlistanoticias)) {
			    		forwardlocal="listarnoticiasanyos";
			    	}
			    }
			    
			    if (bdlistanoticias.getClaseelemento().getTipoelemento().equals(Microfront.ELEM_LINK)) {
			    	if (isPaginacioAnual(bdlistanoticias)) {
			    		forwardlocal="listarlinksanyos";
			    	}
			    	else forwardlocal="listarlinksnormal";
			    }
			    
			    if (bdlistanoticias.getClaseelemento().getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
			    	if (isPaginacioAnual(bdlistanoticias)) {
			    		forwardlocal="listardocumentosanyos";
			    	}
			    	else forwardlocal="listardocumentosnormal";
			    }
			    
			    if (bdlistanoticias.getClaseelemento().getTipoelemento().equals(Microfront.ELEM_CONEXIO_EXTERNA)) {
		    		forwardlocal="listarnoticiasexternas";
		    		request.setAttribute("MVS_mchtml",bdlistanoticias.getHtmlExterno());
				}
			    
			    if (bdlistanoticias.getClaseelemento().getTipoelemento().equals(Microfront.ELEM_FOTO)) {
		    		String[] tamanyo = calcularTamanyoFoto(bdlistanoticias.getClaseelemento().getFotosporfila());
		    		request.setAttribute("MVS_anchoFoto",tamanyo[0]);
		    		request.setAttribute("MVS_altoFoto",tamanyo[1]);

			    	if (isPaginacioAnual(bdlistanoticias)) {
			    		forwardlocal="mostrarGaleriaFotosAnual";
			    	}
			    	else {
			    		forwardlocal="mostrarGaleriaFotos";
			    	}
		    		
				}				    

		  
				String sIdMenuContenidoNotic= request.getParameter("mcont");
				if ( (sIdMenuContenidoNotic != null) && (!sIdMenuContenidoNotic.equals(""))   ){
				  	request.setAttribute("MVS_menu_cont_notic",  sIdMenuContenidoNotic);
				}
			   
				/*** SIEMPRE FIJAS para version 4**/
				request.setAttribute("MVS2_mollapan", mollapan(request, bdlistanoticias.getIdioma()));
				menucaib(request, bdlistanoticias.getIdioma());
				/*** FIN SIEMPRE **/
			    
			    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
			    //OJO: es la estructura y no la hoja de estilos.
				microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
			    	return mapping.findForward(forwardlocal+"v1");
				else
			    	return mapping.findForward(forwardlocal+"v4");
		    
		    } else {
		    	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));
			}
	    
		} catch (ExceptionFrontPagina efp) {   	
		     log.error(efp.getMessage());
		     return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));
	
		} catch (ExceptionFrontMicro efm) {    	
			 log.error(efm.getMessage());
			 return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_MICRO));
			    	
		}  catch (Exception e) {      	
		     log.error(e.getMessage(),e);
		     return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));
		} 		    
			    
	}

	/**
	 * Traduce el idioma de la noticia
	 * 
	 * @param listanoticias
	 * @param idioma
	 */
	private void traduceNoticia(List<?> listaNoticias, String idioma) {
		for (Object noticia : listaNoticias) {
			((Noticia) noticia).setIdi(idioma);
		}
		
	}

	private boolean isPaginacioAnual(Bdlistanoticias bdlistanoticias) {
		return bdlistanoticias.getClaseelemento().getTipopagina().equals(Microfront.ELEM_PAG_ANUAL);
	}

	private String[] calcularTamanyoFoto(int numFotosFila) {
		int ancho=0, alto=0;
		
		if(0!=numFotosFila) {
			ancho = 100 / numFotosFila;  //max = 100%
			alto = 300 /  numFotosFila;  //max = 300px
		}
		return new String[] {Integer.toString(ancho), Integer.toString(alto)}; 
	}

	protected Microsite buildMicrosite() {
		return new Microsite();
	}

	protected Bdlistanoticias cargarListadoDeNoticias(ActionForm form,
			HttpServletRequest request) throws Exception {
		return new Bdlistanoticias(request, form);
	}
		  
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */		  
	private String mollapan(HttpServletRequest request, String idi) {
		StringBuffer stbuf = new StringBuffer("");
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idi.toUpperCase(), idi.toUpperCase()));
		String mvs_ua = "" + request.getSession().getAttribute("MVS_ua");
		String mvs_tipolistado = "" + request.getAttribute("MVS_tipolistado");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//añado el titulo del microsite
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		if (!(mvs_tipolistado.equals("null")) && (mvs_tipolistado.length()>0))
			stbuf.append("<li>&gt; " + mvs_tipolistado + "</li>");
		
		return stbuf.toString();
	}

		  
}
