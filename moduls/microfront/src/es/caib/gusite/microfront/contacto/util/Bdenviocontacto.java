package es.caib.gusite.microfront.contacto.util;

import java.io.InputStream;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.base.bean.Pardato;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.microfront.util.correo.CorreoEngineService;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;



public class Bdenviocontacto extends Bdbase   {
	
	protected static Log log = LogFactory.getLog(Bdenviocontacto.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private String cuerpomensaje="";
	private Hashtable hashparametros = new Hashtable();
	
	/**
	 * Constructor de la clase, carga los datos necesarios para el envio del mail,
	 *  mensaje, titulo,etc a partir de la request.
	 * @param request
	 * @throws Exception
	 */
	public Bdenviocontacto(HttpServletRequest request) throws Exception {

		super(request);
		req = request;		
		Bdcontacto bdcontacto = new Bdcontacto(request);
		java.util.ArrayList para= bdcontacto.getListalineas();
		hashparametros = (Hashtable<?, ?>)req.getSession().getAttribute("MVS_listparamform");
		 
         try {	    
			procesaformulario(para);
			enviarformulario();
         } catch (Exception e) {
			log.error(e.getMessage());
			error=true;
			beanerror.setAviso("Error");
			beanerror.setMensaje("Se ha producido un error desconocido en el envio del formulario. " + e.getMessage());
		}
	}

	/**
	 * El formulario no lo tenemos en ninguna clase 'form', así que
	 * recorreremos todos los parametros que se han enviado por el
	 * request. <BR>
	 * De esta forma podremos procesar el formulario. 
	 * Lo recorreremos y lo iremos empaquetando en un string que será el 
	 * cuerpo del mensaje a enviar por correo. 
	 * Además en sesión tenemos la variable MVS_listparamform, que contiene
	 * un hashtable con el nombre del campo. 
	 *
	 * @param para
	 * @throws Exception
	 */
	private void procesaformulario(java.util.ArrayList para) throws Exception {
		
		java.util.Iterator iterp = para.iterator();
				 
		String parametrofijo ="" + getParameter(Microfront.PLANG); 
		if (parametrofijo.equals(Microfront.PLANG)) 
	    	  cuerpomensaje+="Idioma = " + getParameter(parametrofijo) + "\n";
	     	
	    while(iterp.hasNext()) {
		      // Campos fijos que vienen del request: lang,idsite,cont,btnanar. Evidentemente, no hay que tratarlos
	    	  Pardato  lineasdatocontacto= (Pardato)iterp.next();
			  String paramcompletoName  = lineasdatocontacto.getValue();
			 
			  int indice= paramcompletoName.indexOf("name=\"")+6;
			  paramcompletoName = paramcompletoName.substring(indice);
			  indice=paramcompletoName.indexOf("\"");
			  String  paramName= paramcompletoName.substring(0,indice);
		      if (!paramName.equals(Microfront.PLANG) && !paramName.equals(Microfront.PIDSITE)
		    		  && !paramName.equals(Microfront.PCONT) && !paramName.equals(Microfront.PBTNANAR)) {
			      //tratamiento generico del campo
		    	  String paramNamehash = quitarrequerido(paramName);
		    	  String campovalor;
		    	  
		    	  // campovalor =((Pardato)hashparametros.get(paramNamehash)).getKey() + " = ";
		    	  campovalor = lineasdatocontacto.getKey()+ " = ";
			      String[] paramValues = (String[]) getParamValues(paramName);
			      if (paramValues.length == 1) {
					  String paramValue = paramValues[0];
					  if (paramValue.length() == 0)
						  campovalor+="[sin valor]"+ "\n";
					  else
						  campovalor+= paramValue + "\n";

				  } else  {
					  for (int i=0; i < paramValues.length; i++) {
						  campovalor+= paramValues[i] + ", ";
					  }
					  campovalor+="\n";
				  
				  }
			      cuerpomensaje=  cuerpomensaje+campovalor;
		      }	      
	    }
	}    
	
	
	/**
	 * Método privado que se encarga de enviar via correo electrónico la información del formulario
	 * @throws Exception
	 */
	private void enviarformulario() throws Exception  {
		
		ContactoDelegate contactoldel = DelegateUtil.getContactoDelegate();
		String idcontacto = "" + getParameter(Microfront.PCONT);
		try {
			Long idcont = new Long(Long.parseLong(idcontacto));
			Contacto contacto = contactoldel.obtenerContacto(idcont);
		
			java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
	        String mensaje_asunto = contacto.getTitulocontacto(idioma.toLowerCase());
	        StringBuffer mensaje_cuerpo = new StringBuffer("");
	       
	        mensaje_cuerpo.append(cuerpomensaje);
	      
	        mensaje_cuerpo.append(fecha.getTime().toString());        

	        CorreoEngineService correo = new CorreoEngineService();
	       if((InputStream)getParameter("docAnex") != null)   
		       correo.setFile((InputStream)getParameter("docAnex"), (String)getParameter("fileName"));
	        correo.initCorreo(contacto.getEmail(), mensaje_asunto , false, mensaje_cuerpo);
            if (!correo.enviarCorreo()) throw new Exception("");
        } catch (Exception e) {
           throw new Exception(e.getMessage());
        }
	}
	
	/**
	 *  Quitar requerido
	 * @param texto
	 * @return String 
	 */
	private String quitarrequerido(String texto) {
		String retorno=texto;
		int indice=texto.indexOf(Microfront.VCAMPO_REQUERIDO);
		if (indice==0) retorno=texto.substring(indice+Microfront.VCAMPO_REQUERIDO.length(),texto.length());
		return retorno;
	}

	public String getCuerpomensaje() {
		return cuerpomensaje;
	}

	public boolean isError() {
		return error;
	}
	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 */
	public String setServicio() {
		return Microfront.RCONTACTO;
	}

}
