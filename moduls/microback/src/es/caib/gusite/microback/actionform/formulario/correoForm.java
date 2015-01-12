package es.caib.gusite.microback.actionform.formulario;

import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *	Formulario para los contenidos
 *
 *@author Indra
 *
 */
public class correoForm extends ActionForm {

	protected ResourceBundle propertiesMessages = ResourceBundle.getBundle("sac-microback-messages");
	private static final long serialVersionUID = -3575980351954309067L;
    protected static Log log = LogFactory.getLog(correoForm.class);

    public void reset(ActionMapping mapping, HttpServletRequest request){
        super.reset(mapping, request);
    }

    public void resetForm() {
    	correo = null;
       	nombre = "";
       	apellidos = "";
       	ultimoEnvio = null;
    	traceError ="";
    	intentoEnvio = 0;
    	noEnviar = "";
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	    
    	return errors;

    }
    
    private Long id;
	private String correo;
	private String nombre;
	private String apellidos;
	private java.sql.Timestamp ultimoEnvio;
	private String traceError;
	private Integer intentoEnvio;
	private String noEnviar;

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public java.sql.Timestamp getUltimoEnvio() {
		return ultimoEnvio;
	}

	public void setUltimoEnvio(java.sql.Timestamp ultimoEnvio) {
		this.ultimoEnvio = ultimoEnvio;
	}

	public String getTraceError() {
		return traceError;
	}

	public void setTraceError(String traceError) {
		this.traceError = traceError;
	}

	public Integer getIntentoEnvio() {
		return intentoEnvio;
	}

	public void setIntentoEnvio(Integer intentoEnvio) {
		this.intentoEnvio = intentoEnvio;
	}

	public String getNoEnviar() {
		return noEnviar;
	}

	public void setNoEnviar(String noEnviar) {
		this.noEnviar = noEnviar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
   	
}
