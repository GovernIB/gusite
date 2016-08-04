package es.caib.gusite.microback.action.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.RespuestaDatoDelegate;

/**
 * Clase que es llamada desde la action EstadisticasEncuestasDatosAction
 * 
 * @author Indra
 *
 */
public class Bdestadisticasdatos {
	
	private ArrayList<?> listadatos;
	private Encuesta encuesta;
	private Pregunta pregunta;
	private Respuesta respuesta;
	private HttpServletRequest req;
	private Microsite microsite;
	EncuestaDelegate encuestadel = null;
	
	protected static Log log = LogFactory.getLog(Bdestadisticasdatos.class);

	
	  public Bdestadisticasdatos(HttpServletRequest request) throws Exception {
		  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		  req=request;
		  
		  String pIdenc="" + req.getParameter("idenc");
		  String pIdpreg="" + req.getParameter("idpreg");
		  String pIdres="" + req.getParameter("idres");
		  
		  if ((pIdenc.equals("null")) || (pIdpreg.equals("null")) || (pIdres.equals("null")) ) {
			  	listadatos=null;
				throw new Exception("Se ha producido un error solicitando el listado de datos de una encuesta");
		  } else {
			  encuestadel = DelegateUtil.getEncuestaDelegate();
			  recogerencuesta(pIdenc);			  
			  recogerpregunta(pIdpreg);
			  recogerrespuesta(pIdres);
			  recogerdatos();
		  }
		  

	  }
	  
		/**
		 * Método que recoge la encuesta
		 * @param idencuesta	Id de la encuesta
		 * @throws Exception
		 */
		private void recogerencuesta(String idencuesta) throws Exception {
			boolean error=false;
			
			
			try {
				Long idcont = new Long(Long.parseLong(idencuesta));
				Encuesta encu = encuestadel.obtenerEncuesta(idcont);
				encuesta = encu;

				//comprobacion de microsite
				if (!(encuesta.getIdmicrosite().longValue()==microsite.getId().longValue())) {
					log.warn("[AVISO PERMISOS] Se ha intentado acceder a una encuesta sin los permisos necesarios" );
					error=true;
				}
			} catch (Exception e) {
				
				error=true;
			}
			if (error) {
				encuesta=null;
				throw new Exception("El elemento solicitado no pertenece al site");
			}
			
			
		}

		/**
		 * Método que recoge la pregunta
		 * @param idpregunta	Id de la pregunta
		 * @throws Exception
		 */
		private void recogerpregunta(String idpregunta) throws Exception {
			boolean error=false;
			
			
			try {
				Long idcont = new Long(Long.parseLong(idpregunta));
				
				Pregunta pregu = encuestadel.obtenerPregunta(idcont);
				pregunta = pregu;

				//comprobacion de respuesta
				if (!(encuesta.getId().longValue()==pregunta.getIdencuesta().longValue())) {
					log.warn("[AVISO PERMISOS] Se ha intentado acceder a una encuesta sin los permisos necesarios" );
					error=true;
				}
			} catch (Exception e) {
				
				error=true;
			}
			if (error) {
				pregunta=null;
				throw new Exception("El elemento solicitado no pertenece al site");
			}
			
		}

		/**
		 * Método que recoge la respuesta
		 * @param idrespuesta	Id de la respuesta
		 * @throws Exception
		 */
		private void recogerrespuesta(String idrespuesta) throws Exception {
			boolean error=false;
			
			
			try {
				Long idcont = new Long(Long.parseLong(idrespuesta));
				
				Respuesta resp = encuestadel.obtenerRespuesta(idcont);
				respuesta = resp;

				//comprobacion de pregunta
				if (!(pregunta.getId().longValue()==respuesta.getIdpregunta().longValue())) {
					log.warn("[AVISO PERMISOS] Se ha intentado acceder a una encuesta sin los permisos necesarios" );
					error=true;
				}
			} catch (Exception e) {
				
				error=true;
			}
			if (error) {
				respuesta=null;
				throw new Exception("El elemento solicitado no pertenece al site");
			}
			
		}
		
		/**
		 * 	Método que guarda los datos en una lista según una respuesta
		 * @throws Exception
		 */
		private void recogerdatos() throws Exception {
			boolean error=false;
			try {
				RespuestaDatoDelegate resdatdel = DelegateUtil.getRespuestaDatoDelegate();
				listadatos = resdatdel.listarDatosByResp2(respuesta.getId());
				
			} catch (Exception e) {
				error=true;
			}
			if (error) {
				listadatos=null;
				throw new Exception("Se ha producido un error inesperado");
			}
		}
		
		
		public ArrayList getListadatos() {
			return listadatos;
		}

		public Encuesta getEncuesta() {
			return encuesta;
		}

		public Pregunta getPregunta() {
			return pregunta;
		}

		public Respuesta getRespuesta() {
			return respuesta;
		}	  
	  
	
}
