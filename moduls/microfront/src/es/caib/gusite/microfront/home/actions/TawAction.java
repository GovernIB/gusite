package es.caib.gusite.microfront.home.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.util.microtag.MicrositeParser;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;

/**
 *  Action del taw <P>
 *  Definición Struts:<BR>
 *  action path="/taw" <BR> 
 *  unknown="false" <BR>
 *  forward name="ttritem" path="/v4/general/tawitem.jsp" 
 *  
 *  @author - Indra
 */
public class TawAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
	
		/* Por ejemplo:
		 * /sacmicrofront/taw.do?ttr=CNTSP&idioma=ca&id=1658&idsite=109&stat=no&previsual */
		
		try {	
			String forwardlocal="ttritem";
			String ttr = "" + request.getParameter("ttr");
			String id = "" + request.getParameter("id");
			String idioma = "" + request.getParameter("idioma");
			Long idcont = new Long(Long.parseLong(id));
			
			if (ttr.equals(Microfront.RNOTICIA)) {
				NoticiaDelegate bdNot = DelegateUtil.getNoticiasDelegate();
				Noticia noticia = bdNot.obtenerNoticia(idcont);
				noticia.setIdi(idioma);
				noticia.getTipo().setIdi(idioma);
				request.setAttribute("MVS_noticia", noticia);
			}
			
			if (ttr.equals(Microfront.RAGENDA)) {
				AgendaDelegate bdAge = DelegateUtil.getAgendaDelegate();
				Agenda agenda = bdAge.obtenerAgenda(idcont);
				agenda.setIdi(idioma);
				request.setAttribute("MVS_agenda", agenda);
			}
			
			if (ttr.equals(Microfront.RCONTENIDO)) {
				ContenidoDelegate bdCon = DelegateUtil.getContenidoDelegate();
				Contenido conte = bdCon.obtenerContenido(idcont);
				conte.setIdi(idioma);
				
				// ******************** Para evitar los tags de los componentes.  ******************************** //
				TraduccionContenido tracon = (TraduccionContenido)conte.getTraduccion(idioma);
				if ((tracon!=null) && (tracon.getTexto()!=null) && (tracon.getTexto().length()>0)) {
					String htmlatestear = "";
					htmlatestear = tracon.getTexto();
					MicrositeParser microparser = new MicrositeParser("2",htmlatestear, new Long(-1), idioma, 2);
					microparser.doParser2Comentario(idioma);
					String html2analizar = microparser.getHtmlParsed().toString();
					tracon.setTexto(html2analizar);
				}
				
				request.setAttribute("MVS_contenido", conte);
			}
				
			return mapping.findForward(forwardlocal);
    	
		} catch (Exception e) {			
			log.error(e.getMessage());
			return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA));
		}			
		
	}

}
