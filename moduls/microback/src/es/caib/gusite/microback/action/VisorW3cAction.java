package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.extractor.taw.TawResultBean;
import es.caib.gusite.extractor.tidy.TidyResultBean;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.microback.utils.microtag.MicrositeParser;
import es.caib.gusite.microback.utils.w3c.Testeador;
import es.caib.gusite.micromodel.Accesibilidad;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;

/**
 * Action visor W3C de un microsite <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/visorw3c"<BR> 
 *  unknown="false" <BR>
 *  forward name="visorw3c" path="/visorw3c.jsp" 
 *  
 *  @author - Indra
 */
public class VisorW3cAction extends BaseAction {

	
	protected static Log log = LogFactory.getLog(VisorW3cAction.class);
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		AccesibilidadDelegate bdAcc = DelegateUtil.getAccesibilidadDelegate();
		ContenidoDelegate bdCon = DelegateUtil.getContenidoDelegate();
		NoticiaDelegate bdNot = DelegateUtil.getNoticiasDelegate();
		AgendaDelegate bdAge = DelegateUtil.getAgendaDelegate();
		
		String iditemacce = "" + request.getParameter("id");
		
		Accesibilidad acce = bdAcc.obtenerAccesibilidad(new Long(iditemacce));
		
		if (acce!=null) {
		
			String htmlatestear = "";
			
			if (acce.getServicio().equals(Catalogo.SRVC_MICRO_ELEMENTOS)) {
				Noticia noticia = bdNot.obtenerNoticia(acce.getIditem());
				noticia.setIdi(acce.getIdioma());
				noticia.getTipo().setIdi(acce.getIdioma());
				TraduccionNoticia tracon = (TraduccionNoticia)noticia.getTraduccion(acce.getIdioma());
				if ((tracon!=null) && (tracon.getTexto()!=null) && (tracon.getTexto().length()>0)) 
					htmlatestear = tracon.getTexto();
				request.setAttribute("MVS_noticia", noticia);
			}
			
			if (acce.getServicio().equals(Catalogo.SRVC_MICRO_EVENTOS)) {
				Agenda agenda = bdAge.obtenerAgenda(acce.getIditem());
				agenda.setIdi(acce.getIdioma());
				TraduccionAgenda tracon = (TraduccionAgenda)agenda.getTraduccion(acce.getIdioma());
				if ((tracon!=null) && (tracon.getDescripcion()!=null) && (tracon.getDescripcion().length()>0)) 
					htmlatestear = tracon.getDescripcion();
				request.setAttribute("MVS_agenda", agenda);
			}
			
			if (acce.getServicio().equals(Catalogo.SRVC_MICRO_CONTENIDOS)) {
				Contenido conte = bdCon.obtenerContenido(acce.getIditem());
				conte.setIdi(acce.getIdioma());
				TraduccionContenido tracon = (TraduccionContenido)conte.getTraduccion(acce.getIdioma());
				if ((tracon!=null) && (tracon.getTexto()!=null) && (tracon.getTexto().length()>0)) 
					htmlatestear = tracon.getTexto();
				request.setAttribute("MVS_contenido", conte);
			}
			
			
			// ******************** Para evitar los tags de los componentes.  ******************************** //
			MicrositeParser microparser = new MicrositeParser("2",htmlatestear, acce.getCodmicro(), acce.getIdioma(), 2);
			microparser.doParser2Comentario(acce.getIdioma());
			String html2analizar = microparser.getHtmlParsed().toString();
			
			//Analizamos con el Tidy
			TidyResultBean resultado = Testeador.testeoPegoteHTML(html2analizar);
			
			//Analizamos con el Taw. Se monta una url preparada para sera analizada por el taw.
			//ejemplo: sacmicrofront/taw.do?ttr=CNTSP&idioma=ca&id=1658&idsite=109
			
			String ttr = acce.getServicio().equals(Catalogo.SRVC_MICRO_CONTENIDOS)?"CNTSP":acce.getServicio().equals(Catalogo.SRVC_MICRO_EVENTOS)?"GND00":"NTCS0";
			/*
			String servidor = "http://"+ Microback._HOSTCAIB;
	    	String value = System.getProperty("es.indra.caib.rolsac.oficina");
	    	
	        if ((value == null) || value.equals("N"))
	        	servidor = "localhost";			
			*/
			
			StringBuffer urlbuf = request.getRequestURL();
			int posSeparadorProtocol=urlbuf.indexOf("//");
			String _protocolo = (urlbuf.substring(0, posSeparadorProtocol+2)).toString();
			String _servidor = request.getServerName();
			String _puerto = "" + request.getServerPort();
			
			
			TawResultBean resultadotaw = Testeador.testeoTaw( _protocolo + _servidor + ":" + _puerto + "/sacmicrofront/taw.do?ttr=" + ttr + "&idioma=" + acce.getIdioma() + "&id=" + acce.getIditem() + "&idsite=" + acce.getCodmicro());
			
			request.setAttribute("MVS_tidyresultado", resultado);
			request.setAttribute("MVS_tawresultado", resultadotaw);
			request.setAttribute("MVS_accesibilidad", acce);
			
			
		} else {
			request.setAttribute("MVS_errorpeticion", "Peticion incorrecta");
		}
		
		
		return mapping.findForward("visorw3c");
	}
}
