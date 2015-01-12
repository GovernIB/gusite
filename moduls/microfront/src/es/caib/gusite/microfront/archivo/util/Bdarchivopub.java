package es.caib.gusite.microfront.archivo.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;

/**
 * 
 * Clase Bdarchivopub. Recoge los datos para mostrarlos en el front.
 *  
 * @author pmelia
 *
 */
public class Bdarchivopub {
	
	protected static Log log = LogFactory.getLog(Bdarchivopub.class);
	
	HttpServletRequest req;
	/**
	 * Constructor de la clase
	 * @param request
	 */
	public Bdarchivopub(HttpServletRequest request){
		req=request;
	}
	
	/**
	 * 
	 * Este método comprueba que en el parametro 'ctrl' del request se ha pasado el siguiente
     * formato: SSSSSxxxZIyyy
     * donde SSSSS es el tipo de servicio sacado de la clase es.caib.gusite.microfront.Microfront.
     * donde xxx es el id del elemento al que pertenece el documento
     * donde ZI es el separador
     * donde yyy es el id del documento
     * En el parametro id se ha pasado también el id de la imagen. 
     * 
	 * @return boolean
	 */
	public boolean checkcontrol() {
		
		boolean retorno=true;
		
		try {

			//comprobar si solicitamos por nombre el archivo. Si es así----> no hay chequeo
			String bynombre = "" +  req.getParameter(Microfront.PNAME);
			if (!bynombre.equals("null"))	
				return retorno;
				
			//primero, comprobar que 'ctrl' tiene el formato correcto
			String ctrl = "" + req.getParameter("ctrl");
			String SSSSS;
			Long xxx;
			Long yyy;
			SSSSS=ctrl.substring(0,5);
			xxx=new Long(ctrl.substring(5,ctrl.indexOf(Microfront.separatordocs)));
			yyy=new Long(ctrl.substring(ctrl.indexOf(Microfront.separatordocs)+2,ctrl.length()));
			
			Long idelemento = new Long(req.getParameter("id"));
			
			if (idelemento.longValue()!=yyy.longValue())
				retorno = false;
			
			if ((retorno) && (SSSSS.equals(Microfront.RAGENDA))) {
				AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
				Agenda agenda = agendadel.obtenerAgenda(xxx);
				String previ= ""+req.getSession().getAttribute("previsualiza");
			    if (!previ.equals("si"))
			    {	
					if (!agenda.getVisible().equals("S"))
					{
						retorno=false;
					}
			    }
			}
			
			if ((retorno) && (SSSSS.equals(Microfront.RCONTENIDO))) {
				ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
				Contenido contenido = contenidodel.obtenerContenido(xxx);
				String previ= ""+req.getSession().getAttribute("previsualiza");
			    if (!previ.equals("si"))
			    {
					if (!contenido.getVisible().equals("S"))
					{ 
						retorno=false;
					}
			    }
			}
			
			if ((retorno) && (SSSSS.equals(Microfront.RNOTICIA))) {
				NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
				Noticia noticia = noticiadel.obtenerNoticia(xxx);
				String previ= ""+req.getSession().getAttribute("previsualiza");
			    if (!previ.equals("si"))
			    {
					if (!noticia.getVisible().equals("S"))
					{
						retorno=false;
				    }
				}
			}
			
			if ((retorno) && (SSSSS.equals(Microfront.RMICROSITE))) {
				MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
				Microsite microsit = micrositedel.obtenerMicrosite(xxx);
				String previ= ""+req.getSession().getAttribute("previsualiza");
			    if (!previ.equals("si"))
			    {
			    	if (!microsit.getVisible().equals("S"))
			    	{ 
			    		retorno=false;
			    	}		
				}
			    //segundo, comprobar que yyy es igual que el parametro id
			 }
			
		}catch (Exception e) {
			retorno=false;
			log.error(e.getMessage());
		}
		
		return retorno;
	}
	
}
