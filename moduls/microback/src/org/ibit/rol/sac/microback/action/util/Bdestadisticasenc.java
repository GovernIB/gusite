package org.ibit.rol.sac.microback.action.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microback.Microback;
import org.ibit.rol.sac.microback.base.bean.Recurso;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Pregunta;
import org.ibit.rol.sac.micromodel.TraduccionEncuesta;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;

/**
 * Clase que es llamada desde la action EstadisticasEncuestasAction
 * 
 * @author Indra
 *
 */
public class Bdestadisticasenc {

	
	private ArrayList<Recurso> listaencuestas;	//la lista maneja bean de recurso
	private Encuesta encuesta;
	private boolean individual=false;
	private HttpServletRequest req;
	private Microsite microsite;

	protected static Log log = LogFactory.getLog(Bdestadisticasenc.class);
	
	  public Bdestadisticasenc(HttpServletRequest request) throws Exception {
		  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		  req=request;
		  
		  String pIdenc="" + req.getParameter("idenc");
		  if (pIdenc.equals("null")) {
			  //listado de encuesta
			  individual=false;
			  recogerlistaencuestas();
		  } else {
			  //averiguar estadistica individual
			  recogerencuesta(pIdenc);
			  individual=true;
		  }
	  }
	  
	  
	/**
	 * Método que recoge un listado de encuestas
	 * @throws Exception
	 */
	  private void recogerlistaencuestas() throws Exception {
		  	boolean error=false;
			EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			listaencuestas = new ArrayList<Recurso>();
			try {
				encuestadel.init(microsite.getId());encuestadel.setPagina(1);encuestadel.setTampagina(Microback.MAX_INTEGER);
				
				Iterator<?> iter = encuestadel.listarEncuestas().iterator();
				while (iter.hasNext()) {
					Encuesta enc = (Encuesta)iter.next();
					//se utiliza el bean recurso, aunque 
					Recurso recurso = new Recurso();
					recurso.setUrl("" + enc.getId().longValue()); //id de la encuesta
					recurso.setTitulo( ((TraduccionEncuesta)enc.getTraduce()).getTitulo() ); // titulo de la encuesta
					
					if (enc.getFpublicacion()!=null){
						java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
						recurso.setTipo(df2.format( enc.getFpublicacion() ));
					}
					
					int max=0;
					Iterator<?> iter2 = encuestadel.listarPreguntas(enc.getId()).iterator();
					while (iter2.hasNext()) {
						Pregunta preg = (Pregunta)iter2.next();
						if ( (preg.getNrespuestas()!=null) && (preg.getNrespuestas().intValue()>max) ) 
								max=preg.getNrespuestas().intValue();
					}
					
					recurso.setHead("" + max); //numero maximo de respuestas
					
					listaencuestas.add(recurso);
					
				}
				
			} catch (Exception e) {
				error=true;
			}
			if (error) {
				listaencuestas=null;
				throw new Exception("Se ha producido un error solicitando el listado de encuestas");
			}
		  
		  
	  }
	  

		/**
		 * Método que recoge una encuesta según su id
		 * @param idencuesta	Id de la encuesta
		 * @throws Exception
		 */	  
		private void recogerencuesta(String idencuesta) throws Exception {
			boolean error=false;
			EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			
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


		public Encuesta getEncuesta() {
			return encuesta;
		}


		public boolean isIndividual() {
			return individual;
		}


		public List getListaencuestas() {
			return listaencuestas;
		}		  
	
	
}
