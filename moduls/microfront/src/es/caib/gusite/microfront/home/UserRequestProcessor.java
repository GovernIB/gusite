package es.caib.gusite.microfront.home;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.RequestProcessor;

import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.DelegateBase;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;


/**
 * 
 * Clase que controla en todo momento el acceso a los microsites restringidos.
 * En caso de un microsite restringido, si no estas logado redirige a una pantalla que
 * se le ha definido seguridad de SEYCON.
 * <BR>
 * Es una clase controller definida en el struts-config.
 * @author Indra
 * 
 */
public class UserRequestProcessor extends RequestProcessor {

	protected static final String INVALID_ROLE_SITES = "INVALID_ROLE_SITES";
	protected final static String _URLINTRANETLOGADO = "/intranethome.do";
	protected final static String _URLINTRANETLOGIN = "/intranetlogin.do";
	protected final static String _URLARCHIVO = "/archivopub.do";
	protected final static String _URLINVALIDROL = "/invalidrol.do";
	protected final static String _URLERRORSESSION = "/invalidsession.do";
	protected final static String _URLENVIOENQ = "/envioencuesta.do";
	
	protected static Log log = LogFactory.getLog(UserRequestProcessor.class);
	
	protected Exception exception;  // campo creado sólo para ser utilitzado para los tests unitarios
	
	private String paramIdsite;

	
	/**
  	 *Obtengo el usuario en la sesion de EJBs.
	 * Si es distinto de "nobody" quiere decir que está logado. Salimos y seguimos...
	 * Si es "nobody":
	 * 1. recogemos MVS_microsite de la sesion coyote.
	 * 	a.1 Si es nula --> hay que obtener microsite
	 * 	a.2 Si no es nulo --> Comprobar que coincide con el parametro del request.
	 * 							a.2.1 Si es igual, no hacemos nada
	 * 							a.2.2 Si no es igual, obtenemos el nuevo microsite 
	 * 
	 * 2. Comprobamos que el microsite es restringido.
	 * 	b.1 Si es restringido --> Hay que redireccionarlo para que pase por SEYCON
	 * 	b.2 Si no es restringido --> Salimos y seguimos
	 */
	public boolean processPreprocess( HttpServletRequest req, HttpServletResponse resp ) {
		String requestPath = req.getServletPath();
		String pmkey = null;
		
		
		// TODO procesamiento de las encuentas - quitar de aqui algo tan especifico como encuentas!
		
		 if (peticionNoEsMultipart(req)) {
			paramIdsite = obtenerIdMicrositeFromRequest(req);
			pmkey = obtenerKeyMicrositeFromRequest(req);
			
			ponerEnSessionLasRespuestasEncuenta(req);
			
		}else{ // cas en que sigui un multipart
			
			paramIdsite = ponerEnElRequestLasRespuestasEncuesta(req, paramIdsite);		
		}
		 
		
		//si archivopub o el propio intranethome, no hacemos nada
		if  ( (requestPath.equals(_URLARCHIVO)) || 
			  (requestPath.indexOf(_URLINTRANETLOGADO)!=-1) || 
			  (requestPath.indexOf(_URLINTRANETLOGIN)!=-1) || 
			  (requestPath.indexOf(_URLINVALIDROL)!=-1)  ||
			  (requestPath.indexOf(_URLERRORSESSION)!=-1) )  
			return true ;

		
		try {
			
			// TODO procesamiento de las encuentas - quitar de aqui algo tan especifico como encuentas!

			if (esUnaPeticionEnviarEncuesta(requestPath)){
				if(!esPeticionExternaYnoTieneKey(pmkey)) {
					req.getRequestDispatcher(_URLERRORSESSION  + "?idsite=" + paramIdsite).forward(req,resp);
					return false;
				}
			}
			
			if (esUnaPeticionExterna()) {  

				if ( esUnMicrositePublico(req, pmkey)  ) {   
					return true;
				}
			} else {

				if(!tieneRol(req)) {
					req.getRequestDispatcher(_URLINVALIDROL + "?idsite=" + paramIdsite).forward(req,resp);
					return false;
				}
				return true;
			}
			
		} catch (DelegateException de) {
			log.error("",de);
			exception = de;
		} catch (Exception e) {
			log.error("",e);
			exception = e;
			return true;
		}

		//Si llegamos aquí, redirigir a la pantalla de intranetlogin (que a su vez sera interceptada por SEYCON)
		try {
				
            req.getRequestDispatcher(_URLINTRANETLOGIN + "?idsite=" + paramIdsite).forward(req,resp);
            return false;
		}
		catch( Exception e ) {
			log.error( e.toString() );
			exception = e;
			return true ;
		}
		
	}


	protected String obtenerIdMicrositeFromRequest(HttpServletRequest req) {
		return "" + req.getParameter(Microfront.PIDSITE);
	}


	protected String obtenerKeyMicrositeFromRequest(HttpServletRequest req) {
		return "" + req.getParameter(Microfront.PMKEY);
	}

	
	class InvalidRolSites {
		List<Long> invalidIds = new ArrayList<Long>();
		
		public void addId(long id) {
			invalidIds.add(id);
		}

		public void removeId(long id) {
			invalidIds.remove(id);
		}
		
		public boolean contains(long id) {
			return 0<= Collections.binarySearch(invalidIds, id);
		}
		
	}
	
	protected boolean tieneRol(HttpServletRequest req) {
		
		Microsite microsite = obtenerMicrositeFromRequest(req);
		
		if(null==microsite) {
			return false;
		}
		
		
		InvalidRolSites invalidIds = getInvalidRoleSitesFromSession(req);

		long idSite = microsite.getId();

		if(invalidIds.contains(idSite)) {
			return false;
		}
		
		if (microsite.getRol()!=null) {  
				 if (req.isUserInRole(microsite.getRol())) {
					 invalidIds.removeId(idSite);
					 return true;
				 }
				 else {
					 invalidIds.addId(idSite);
					 return false;
				 }
		}
		
		invalidIds.removeId(idSite);
		return true;
		
	}


	protected Microsite obtenerMicrositeFromRequest(HttpServletRequest req) {
		Microsite microsite = null;
		try {
			String pmkey = obtenerKeyMicrositeFromRequest(req);
			if(micrositeTieneKey(pmkey)) {
				microsite = obtenerMicrositePorKey(req, pmkey);
			}
			if(null==microsite) {
				String id = obtenerIdMicrositeFromRequest(req);
				if(null!=id) {
					microsite = obtenerMicrositePorId(id);
				}
			}
		} catch (Exception e) {
			log.error("error obtenint microsite del request ",e);
			exception = e;
		}
		return microsite;
	}


	protected InvalidRolSites getInvalidRoleSitesFromSession(HttpServletRequest req) {
		InvalidRolSites invalidIds = (InvalidRolSites)req.getSession().getAttribute(INVALID_ROLE_SITES);
		if(null==invalidIds) {
			invalidIds = new InvalidRolSites();
			req.getSession().setAttribute(INVALID_ROLE_SITES,invalidIds);
		}
		return invalidIds;
	}

	
	protected Microsite obtenerMicrosite(HttpServletRequest req, String pmkey) throws Exception, DelegateException {
		
		Microsite microsite = obtenerMicrositeDeSession(req);
		
		if (microsite==null) {   
			
			if (micrositeTieneKey(pmkey)) {    
				microsite = obtenerMicrositePorKey(req, pmkey);
			} else{	
				microsite = obtenerMicrositePorId(paramIdsite);
			}
		} else {
			Long micrositeId = microsite.getId()==null?null:microsite.getId().longValue();
			if (null!=pmkey) {
				paramIdsite = "" + micrositeId;
			}
			if (siteSolicitadoEsDiferenteAlDeSession(paramIdsite, micrositeId)) {   
				microsite = obtenerMicrositePorId(paramIdsite);					
			}
		}
		return microsite;
	}

	protected boolean esPeticionExternaYnoTieneKey(String pmkey) {
		try {
			if (esUnaPeticionExterna()) {
				if (!micrositeTieneKey(pmkey)) {
					return false;
				}
			}
		} catch (DelegateException de) {
			log.error(de.getMessage());
			exception = de;
		}
		return true;
	}
	
	
	protected boolean peticionNoEsMultipart(HttpServletRequest req) {
		return !ServletFileUpload.isMultipartContent(req);
	}

	protected boolean siteSolicitadoEsDiferenteAlDeSession(String paramIdsite,
			Long micrositeId) {
		return !paramIdsite.equals("" + micrositeId);
	}

	protected Microsite obtenerMicrositePorId(String pidsite) throws DelegateException {
		MicrositeDelegate microdel = DelegateUtil.getMicrositeDelegate();
		return microdel.obtenerMicrosite(new Long(pidsite));
	}

	protected Microsite obtenerMicrositePorKey(HttpServletRequest req,
			String pmkey) throws Exception {
		Microsite microsite;
		DelegateBase dbase = new DelegateBase(req);
		microsite = dbase.obtenerMicrositebyKey(pmkey, "CA");
		return microsite;
	}

	protected Microsite obtenerMicrositeDeSession(HttpServletRequest req) {
		return (Microsite)req.getSession().getAttribute("MVS_microsite");
	}

	/**
	 * pmkey pot ser null, "null", o "<string>"
	 * @param pmkey
	 * @return
	 */
	protected boolean micrositeTieneKey(String pmkey) {
		
		if(StringUtils.isEmpty(pmkey)) {
			return false;
		}
		return !pmkey.equals("null");
	}

	protected boolean esUnaPeticionEnviarEncuesta(String requestPath) {
		return requestPath.indexOf(_URLENVIOENQ)!=-1;
	}

	protected boolean esUnMicrositePublico(HttpServletRequest req, String pmkey) {
		Microsite microsite=null;
		try {
			microsite = obtenerMicrosite(req, pmkey);
		} catch (Exception e) {
			exception = e;
		}
		return (!(microsite.getRestringido().equals("S"))) && (microsite.getRol()==null);
	}

	protected boolean esUnaPeticionExterna() throws DelegateException {
		MicrositeDelegate microdel = DelegateUtil.getMicrositeDelegate();
		return microdel.getUsuarioEJB().equals(Microfront._NOBODY);
	}

	protected String ponerEnElRequestLasRespuestasEncuesta(
			HttpServletRequest req, String pidsite) {
		ServletFileUpload upload = new ServletFileUpload();
		HashMap paramEncuesta = new HashMap();
		
		try {
			HashMap<String, ArrayList<String>> parameter = new HashMap<String, ArrayList<String>>();            	
			FileItemIterator iter = upload.getItemIterator(req);

			while (iter.hasNext()) 
		    {
		        FileItemStream fileItemStream = iter.next();
		        String strParamName  = fileItemStream.getFieldName();
		        InputStream streamIn = fileItemStream.openStream();
		        
		        if (fileItemStream.isFormField()) //son parametres 
		        {	                	
		            String strParamValue =Streams.asString(streamIn);
		            if (parameter.containsKey(strParamName)){
		            	parameter.get(strParamName).add(strParamValue);
		            }else{
		            	ArrayList<String> aux = new ArrayList<String>();
		            	aux.add(strParamValue);
		            	parameter.put(strParamName,aux);
		            }  	
		            if ("idsite".equals(strParamName))pidsite=strParamValue;
		        	
		        }else{ //es un fitxer
		        	if (!fileItemStream.isFormField()) 
		        	{
		        		ByteArrayInputStream bais;
		        		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		        		int b;
		    			while ((b=streamIn.read())!=-1) baos.write(b);
		    			bais = new ByteArrayInputStream(baos.toByteArray());
		    			baos.close();
		    			bais.close();

		            	paramEncuesta.put(strParamName, bais);
		            	req.setAttribute(strParamName, bais);
		        	
		            	ArrayList<String> aux = new ArrayList<String>();
		            	String fileName = fileItemStream.getName();
		            	aux.add(fileName);
		            	parameter.put("fileName",aux);
		        	}
		        }	                
		    } 
			for (String key : parameter.keySet()){
				 req.setAttribute(key, parameter.get(key).toArray());
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pidsite;
	}

	// Guardamos las respuestas que vienen prefijadas en una encuesta texte
	protected void ponerEnSessionLasRespuestasEncuenta(HttpServletRequest req) {
		if (req != null) {
			HashMap paramEncuesta = new HashMap();
			Map mapParameter = req.getParameterMap();
			for (Object o : mapParameter.keySet()) {
				if (((String) o).startsWith("resp")) {
					Long key = new Long(((String) o).substring(4));
					try {
						byte[] p = ((String[]) mapParameter.get(o))[0].getBytes("utf-8");
						paramEncuesta.put(key,(new String(p,"UTF-8")).replace("'", "\\'"));
					} catch (UnsupportedEncodingException e) {
						paramEncuesta.put(key,new String("ERROR ENCODING"));
					}
				}
			}
			if (paramEncuesta.size() > 0) req.getSession().setAttribute(Microfront.ENCPARAM, paramEncuesta);
		}
	}
	
}
