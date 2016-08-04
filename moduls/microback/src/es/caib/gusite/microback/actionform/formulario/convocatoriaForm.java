package es.caib.gusite.microback.actionform.formulario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.*;
import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para los contenidos
 *
 *@author Indra
 *
 */
public class convocatoriaForm extends ActionForm {

	protected ResourceBundle propertiesMessages = ResourceBundle.getBundle("sac-microback-messages");
	private static final long serialVersionUID = -3575980351954309067L;
    protected static Log log = LogFactory.getLog(convocatoriaForm.class);


    public void reset(ActionMapping mapping, HttpServletRequest request){
        super.reset(mapping, request);
    }

    public void resetForm() {    	
    	setId(null);
       	setNombre("");
       	setDescripcion("");
       	setEnvioError("");
       	setUltimoEnvio("");
       	setEncuesta(null);
       	setResCorreo(null);
       	setPreConfirmacion(null);
       	setResConfirmacion(null);
       	setDestinatarios(Collections.EMPTY_SET);
       	setOtrosDestinatarios("");
       	setTxtAsunto("");
       	setTxtMensaje("");        
    }
   
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	if(httpServletRequest.getParameter("modifica")!=null || httpServletRequest.getParameter("anyade")!=null) {
	    	if ( getNombre()==null || getNombre().equals("")) 
	            errors.add("nombre", new ActionError("error.convocatoria.nombre"));	    	

	        if (errors.size()!=0) httpServletRequest.setAttribute("errores", "yes");
    	}
    	return errors;

    }
    
    private Long id;
   	private String nombre;
   	private String descripcion;
   	private String envioError;
   	private String ultimoEnvio;
   	private Long encuesta;
   	private Long resCorreo;
   	private Long preConfirmacion;
   	private Long resConfirmacion;
   	private Set destinatarios = Collections.EMPTY_SET;
   	private String otrosDestinatarios;
   	private String txtAsunto;
   	private String txtMensaje;

	public ResourceBundle getPropertiesMessages() {
		return propertiesMessages;
	}

	public void setPropertiesMessages(ResourceBundle propertiesMessages) {
		this.propertiesMessages = propertiesMessages;
	}

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		convocatoriaForm.log = log;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEnvioError() {
		return envioError;
	}

	public void setEnvioError(String envioError) {
		this.envioError = envioError;
	}

	public String getUltimoEnvio() {
		return ultimoEnvio;
	}

	public void setUltimoEnvio(String ultimoEnvio) {
		this.ultimoEnvio = ultimoEnvio;
	}

	public Long getEncuesta() {
		return encuesta;
	}

	public void setEncuesta(Long encuesta) {
		this.encuesta = encuesta;
	}

	public Long getResCorreo() {
		return resCorreo;
	}

	public void setResCorreo(Long preCorreo) {
		this.resCorreo = preCorreo;
	}

	public Long getPreConfirmacion() {
		return preConfirmacion;
	}

	public void setPreConfirmacion(Long preConfirmacion) {
		this.preConfirmacion = preConfirmacion;
	}

	public Long getResConfirmacion() {
		return resConfirmacion;
	}

	public void setResConfirmacion(Long resConfirmacion) {
		this.resConfirmacion = resConfirmacion;
	}

	public String getTxtMensaje() {
		return txtMensaje;
	}

	public void setTxtMensaje(String txtMensaje) {
		this.txtMensaje = txtMensaje;
	}

	public String getTxtAsunto() {
		return txtAsunto;
	}

	public void setTxtAsunto(String txtAsunto) {
		this.txtAsunto = txtAsunto;
	}

	public String getOtrosDestinatarios() {
		return otrosDestinatarios;
	}

	public void setOtrosDestinatarios(String otrosDestinatarios) {
		this.otrosDestinatarios = otrosDestinatarios;
	}

	public Set getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(Set destinatarios) {
		this.destinatarios = destinatarios;
	}
   	
}
