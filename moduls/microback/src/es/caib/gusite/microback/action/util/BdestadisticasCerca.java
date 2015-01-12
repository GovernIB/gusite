package es.caib.gusite.microback.action.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;



/**
 * Clase que es llamada desde el action de busqueda de los resultados de la 
 * encuesta en función de una respuesta EstadisticaEncuestaCercaAction 
 * 
 * @author Indra
 *
 */
public class BdestadisticasCerca {
	
	private Encuesta encuesta;
	private HttpServletRequest req;
	private Microsite microsite;
	private EncuestaDelegate encuestadel = null;
	private Collection<Pregunta> CollectPreguntasOrd = null;
	private String idEncSelected;
	private String idPregSelected;
	private String idRespSelected;
	
	protected static Log log = LogFactory.getLog(Bdestadisticasdatos.class);

	
	  public BdestadisticasCerca(HttpServletRequest request) throws Exception {
		  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		  req=request;
		  
		  idEncSelected="" + req.getParameter("idencuesta"); 
		  idPregSelected="" + req.getParameter("slctpregunta");
		  idRespSelected="" + req.getParameter("slctresposta");
		  
		  encuestadel = DelegateUtil.getEncuestaDelegate();

		  recogerEncuesta(idEncSelected);			  
		  
		  Long idPreg = new Long(idPregSelected);
		  if (idPreg.intValue() != -1){
		      resultadoBusqueda( new Long(idEncSelected),new Long(idRespSelected));
	  	 } else{ // -1 muestra los resultados de las preguntas sin aplicar ningún filtro
	  		  recogerPreguntas( new Long(idEncSelected));
	  	 }
		  
	  }
	  
	  /**
	   * Método que devuelve las preguntas ordenadas, buscando las respuestas que han realizado determinados usuarios.
	   * Los usuarios serán filtrados en función de si han marcado una determinada respuesta 
	   * 
	   * @param pidEnc Id de la encuesta
	   * @param pIdResp Id de la Respuesta que se quiere buscar
	   * @throws Exception
	   */
	  private void resultadoBusqueda( Long pidEnc, Long pIdResp) throws Exception{
		  

		Collection<Pregunta> tempPreguntas = new ArrayList<Pregunta>() ; 

		Collection<String> usuariosRespuesta = new ArrayList<String>() ;
		usuariosRespuesta = obtenerUsuariosRespuesta(pIdResp);
				
	    Hashtable<String, String> hashNumResp = new Hashtable<String, String>();
	    hashNumResp = (Hashtable<String, String>) encuestadel.obtenerNumVotosByResp(usuariosRespuesta);
	     
	    Iterator<?> iter = encuestadel.listarPreguntasOrdAsc(pidEnc).iterator() ;
		while (iter.hasNext()) {
				List<Respuesta> newRespu = new ArrayList<Respuesta>();
				Pregunta preg = (Pregunta)iter.next();

				Iterator<?> iter2 = preg.getRespuestas().iterator();	
				int totalNumRespuestas = 0;

				while (iter2.hasNext()) {
				    Respuesta respuesta = (Respuesta)iter2.next();
				    respuesta.getNrespuestas();
					    
				    int numRespu = 0;
					String sNumRespu = (String) hashNumResp.get(respuesta.getId().toString());
						    
					if (sNumRespu != null){
						numRespu = (new Integer(sNumRespu)).intValue();
					}
					totalNumRespuestas = totalNumRespuestas +  numRespu;
	
					respuesta.setNrespuestas(new Integer(numRespu));
					newRespu.add(respuesta);	
				}
				if (!newRespu.isEmpty()){
					preg.setRespuestas(newRespu); 
					preg.setNrespuestas(totalNumRespuestas);
				}
				tempPreguntas.add(preg); 

           }
			
		 CollectPreguntasOrd = tempPreguntas;	
	  }
	  
	  /**
	   * Método que obtiene los usuarios que han respondido a una determinada respuesta
	   * @param pIdResp 	Id de la Respuesta que se quiere buscar
	   * @return Colletion	Listado de usuarios
	   * @throws Exception
	   */
	  private Collection<String> obtenerUsuariosRespuesta(Long pIdResp)throws Exception{
		  
		  Collection<String> usuarisResposta = new ArrayList<String>() ;
		  int index = 0 ; 
			  
		  Iterator<?> listUsuarisResposta =   encuestadel.obtenerUsuariosRespuesta(pIdResp).iterator();
		  while (listUsuarisResposta.hasNext()) {
			  UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta)listUsuarisResposta.next();			 
			  usuarisResposta.add(upm.getId().getIdusuario().toString());
			  index ++;	 
		  }
		  return usuarisResposta;
		
	  }
	  
	  /**
	   * Método que recoge la encuesta
	   * @param idencuesta Id de la encuesta
	   * @throws Exception
	   */
	  private void recogerEncuesta(String idencuesta) throws Exception {
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
	   *  Método que almacena en una collección las preguntas de una encuesta.
	   *  Las preguntas se guardaran ordenadas ascendentemente por el campo orden
	   * @param pidEnc Id de la encuesta
	   * @throws Exception
	   */
	  private void recogerPreguntas(Long pidEnc) throws Exception {
		  Collection<Pregunta> collect = new ArrayList<Pregunta>() ; 
		  Iterator<?> iter = encuestadel.listarPreguntasOrdAsc(pidEnc).iterator() ;
		  while (iter.hasNext()) {
			  Pregunta preg = (Pregunta)iter.next();
			  collect.add(preg); 
		  }
		  CollectPreguntasOrd = collect;	
		}


	  public Encuesta getEncuesta() {
		  return encuesta;
	  }

	  public Collection<Pregunta> getPreguntasOrdAsc() {
		  return CollectPreguntasOrd;
	  }
	  
	  public String getIdEncSelected(){
		  return idEncSelected;
	  }
	  
	  public String getIdPregSelected(){
		  return idPregSelected;
	  }
	  
	  public String getIdRespSelected(){
		  return idRespSelected;
	  }
	  
	
}
